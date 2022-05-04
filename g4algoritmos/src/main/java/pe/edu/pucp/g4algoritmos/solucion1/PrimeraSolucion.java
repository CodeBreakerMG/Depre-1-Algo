package pe.edu.pucp.g4algoritmos.solucion1;

import pe.edu.pucp.g4algoritmos.model.*;
import pe.edu.pucp.g4algoritmos.utilitarios.Stats;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import org.javatuples.Triplet;
import org.locationtech.jts.algorithm.locate.SimplePointInAreaLocator;

public class PrimeraSolucion{

    /*Variables OUTPUT*/ 
    public static List<List<Pedido>> listaPedidosPorZona = new ArrayList<>(); //Lista de Pedidos por Zona de reparto
    public static List<List<Oficina>> listaOficinasXZona = new ArrayList<>(); //Lista de Oficinas por zona de reparto
    public static List<Long> tiempoDeSalidasZona = new ArrayList<>();
    
    public static List<Geometry> listaZonas = new ArrayList<>(); //Lista de Zonas (Formato de Polígono)

    public static List <Ruta> planesDeTransporte = new ArrayList<>(); //Lista de los planes de transporte o RUTAS por camion
    
    /*Variables INPUT*/ 
    public static List<Pedido> listaPedidos = new ArrayList<>(); //Lista inicial de los pedidos
    public static List<Oficina> listaOficinas = new ArrayList<>(); //Lista de oficinas que tienen al menos un pedido
    public static List<Camion> listaCamiones = new ArrayList<>(); //Lista de camiones disponibles de un ALMACEN
    
    public static Oficina almacen; //Almacen seleccionado para esta lista de pedidos
    public static int paquetes; //Cantidad total de paquetes A 

    public static int cantidadZonasDeReparto = 9;                             //Numero de zonas, 9 por defecto  
    public static Polygon poligonPedidos ; //Polígono que contiene todas las zonas, y a su vez las coordenadas de todos los pedidos
    public static List<Coordinate> coordinates; //Coordenadas de todas las oficinas.
    
    /*Variables Auxiliares*/ 

    public static GeometryFactory factory = new GeometryFactory(); //Variable auxiliar de Geometría. Para generar zonas de reparto
    
    public PrimeraSolucion(){}

    public void inicializar(List<Pedido> listaPed, Oficina alm){
        
        Mapa.inicializarGrafoAstar();

        //Inputs de la PrimeraSolucion
        this.listaPedidos = listaPed;
        this.almacen = alm;

        /*I. Oficinas de todos los pedido*/
        listaOficinas = contabilizarOficinas();
        System.out.println(String.format("Cantidad de oficinas:  %4d", listaOficinas.size()));
        /*II: Polígono o área de reparto total (solo incluye oficinas con al menos un pedido*/
        coordinates = generateCoordinatesOficina();
        poligonPedidos = crearPoligono();


        
        listaCamiones = seleccionarCam();
        System.out.println(String.format("Cantidad de camiones:  %4d", listaCamiones.size()));
        listaZonas = generarZonasReparto();

        
        listaPedidosPorZona = asignarPedidosPorZona();

        //Lo demas se hara con Simulated Annealing
        //ordenarPedidos();
        //planesDeTransporte = asignarPedidosCamiones();

        //areaMaxima = areaPoligono();
        simulatedAnnealing();
        
    }

    /*1. Determinar lista de oficinas de los pedidos*/ 
    public static List<Oficina> contabilizarOficinas(){
        List<Oficina> oficinas = new ArrayList<>();
        for (Pedido p : listaPedidos){
            if(oficinas.contains(p.getOficina()) == false) {
                oficinas.add(p.getOficina());
            }
        }
        return oficinas;
    }

    /*2. Determinar los puntos totales de cada oficina*/ 
    public static List<Coordinate> generateCoordinatesOficina(){
        List<Coordinate> coordinates = new ArrayList<>();
        for (Oficina oficina : listaOficinas){
            Coordinate coordinate = new Coordinate(oficina.getCoordX(), oficina.getCoordY());
            coordinates.add(coordinate);
        }
        return coordinates;
    }


    /*3. Generar AREA TOTAL DE REPARTO polígono para distribución */ 
    public static Polygon crearPoligono(){

        // El polígono a crear será un rectángulo circunscrito a la
        // figura formada por las coordenadas de los puntos de la zona
        
        double x_max = Double.NEGATIVE_INFINITY;
        double x_min = Double.POSITIVE_INFINITY;
        double y_max = Double.NEGATIVE_INFINITY;
        double y_min = Double.POSITIVE_INFINITY;
        
        for(Coordinate c : coordinates) {
            if(c.getX() > x_max) x_max = c.getX();
            if(c.getX() < x_min) x_min = c.getX();
            if(c.getY() > y_max) y_max = c.getY();
            if(c.getY() < y_min) y_min = c.getY();
        }
        
        Coordinate[] polygon_coordinates = new Coordinate[] {
            new Coordinate(x_min, y_max),  //  1-----2  //
            new Coordinate(x_max, y_max),  //  |     |  //
            new Coordinate(x_max, y_min),  //  |     |  //
            new Coordinate(x_min, y_min),  //  4-----3  //
            new Coordinate(x_min, y_max)
        };

        Polygon polygon = factory.createPolygon(polygon_coordinates);
        return polygon;
    }

    //Determinar CUANTAS ZONAS DE REPARTO VAMOS A UTILIZAR (Cuantos camiones y qué camiones vamos a utilizar).
    /*4. Determinar lista de camiones a utilizar*/
    public static List<Camion> seleccionarCam(){

        int [] countCamiones = Mapa.calcularTipoCamionesPorAlmacen(almacen);
        List<Camion> camiones = new ArrayList<>();
        int cantidadPaquetes = 0;

        for (Pedido p : listaPedidos)
            cantidadPaquetes += p.getCantidadTotal();
        
        int cantidadCamionesA = (int) Math.round(cantidadPaquetes / 90.0);

        if(cantidadCamionesA < countCamiones[0]){
            cantidadCamionesA = countCamiones[0] - cantidadCamionesA;
        }
        else{
            cantidadCamionesA = countCamiones[0];
        }

        if (cantidadCamionesA > 0)
            camiones.addAll(Mapa.extractListaCamionesPorAlmacen(almacen, 'A', cantidadCamionesA));

        return camiones;
    }
    public static List<Camion> seleccionarCamiones(){
        
        int [] countCamiones = Mapa.calcularTipoCamionesPorAlmacen(almacen);
        List<Camion> camiones = new ArrayList<>();
        /*
        int cantidadPaquetes30 = 0; //Contabilizar paquetes que pesen hasta 10
        int cantidadPaquetes45 = 0; //Contabilizar paquetes que pesen hasta 30
        int cantidadPaquetes1000 = 0; 

        
    

        for (Pedido p : listaPedidos){
            if(p.getCantidadActual() <= 30) 
                cantidadPaquetes30 += p.getCantidadActual();
            else if (p.getCantidadActual() <= 45)
                cantidadPaquetes45 += p.getCantidadActual();
            else
                cantidadPaquetes1000 += p.getCantidadActual();
        }      

        int cantidadCamionesC = cantidadPaquetes30 / 30;
        int cantidadCamionesB = cantidadPaquetes45 / 45;
        int cantidadCamionesA = cantidadPaquetes1000 / 90;

        */

        //Pendiente a mejorar con un algoritmo de AI:

        int cantidadPaquetes = 0;

        for (Pedido p : listaPedidos)
            cantidadPaquetes += p.getCantidadActual();
        
        int cantidadCamionesC = (int) Math.round(cantidadPaquetes / 30.0);
        int cantidadCamionesB = cantidadCamionesC;

        if (countCamiones[2] - cantidadCamionesC > 0){ //Si hay mayor cantidad de camiones que los requeridos
            countCamiones[2] -= cantidadCamionesC;
            cantidadCamionesB = 0;
        }
        else{
            cantidadCamionesC = countCamiones[2];
            cantidadCamionesB =- countCamiones[2];

            cantidadPaquetes -= cantidadCamionesC * 30;
            
            countCamiones[2] = 0;

            cantidadCamionesB = (int) Math.round(cantidadPaquetes / 45.0);
        }



        int cantidadCamionesA = cantidadCamionesB;

        if (cantidadCamionesB > cantidadCamionesC) { //Si ya no hay camiones C disponibles
            if (countCamiones[1] - cantidadCamionesB > 0){ //Si hay mayor cantidad de camiones B que los requeridos
                countCamiones[1] -= cantidadCamionesB;
                cantidadCamionesA = 0;
                
            }
            else{
                cantidadCamionesB = countCamiones[1];
                cantidadCamionesA -= countCamiones[1];
    
                cantidadPaquetes -= cantidadCamionesB * 45;
                
                countCamiones[1] = 0;
    
                cantidadCamionesA = (int) Math.round(cantidadPaquetes / 90.0);
            }
        }

        if (cantidadCamionesA > cantidadCamionesB) { //Si ya no hay camiones B disponibles
            if (countCamiones[0] - cantidadCamionesA > 0){ //Si hay mayor cantidad de camiones B que los requeridos
                countCamiones[0] -= cantidadCamionesA;
               
            }
            else{
                cantidadCamionesA = countCamiones[0];   
                cantidadPaquetes -= cantidadCamionesA * 90;
                
                countCamiones[0] = 0;
    
                
            }
        }

        camiones.addAll(Mapa.extractListaCamionesPorAlmacen(almacen, 'C', cantidadCamionesC));
        if (cantidadCamionesB > 0)
            camiones.addAll(Mapa.extractListaCamionesPorAlmacen(almacen, 'B', cantidadCamionesB));
        if (cantidadCamionesA > 0)
            camiones.addAll(Mapa.extractListaCamionesPorAlmacen(almacen, 'A', cantidadCamionesA));


        return camiones;
    }



    /*5. Generar Zonas de Reparto*/ 

    public List<Geometry> generarZonasReparto(){
    
        final double MAX_DESV_STD = 2.0;
        GeometryFactory gf = new GeometryFactory();

        // Creación de HashMap con zonas y cantidad de oficinas = 0
        List<Polygon> listZonas = splitRectangle(poligonPedidos);
        HashMap<Polygon, Integer> mapaZonasCantOficinas = new HashMap<>();
        for(Polygon zona : listZonas){
            mapaZonasCantOficinas.put(zona, 0);
        }

        // Determinación de cantidad de oficinas        
        for(Oficina ofic : listaOficinas){
            for(Polygon zona : mapaZonasCantOficinas.keySet()){
                Point oficPoint = gf.createPoint(new Coordinate(ofic.getCoordX(), ofic.getCoordY()));
                if(zona.contains(oficPoint)){
                    mapaZonasCantOficinas.put(zona, mapaZonasCantOficinas.get(zona)+1);
                    break;
                }
            }
        }


        // Se tiene un HashMap de zonas con su cantidad de oficinas respectivas.        
        // Itera hasta tener una desviación estándar menor o igual a MAX_DESV_STD
        while (Stats.desvest(mapaZonasCantOficinas.values()) > MAX_DESV_STD){
            // Hallar zona con mayor cantidad de oficinas a las cuales llegar
            Polygon zonaMaxCantOficinas = Collections.max(mapaZonasCantOficinas.entrySet(), HashMap.Entry.comparingByValue()).getKey();
            
            // Dividir la oficina con mayor cantidad de oficinas
            List<Polygon> nuevaDivision = splitRectangle(zonaMaxCantOficinas);
            
            // Creación de HashMap con subzonas y cantidad de oficinas = 0
            HashMap<Polygon, Integer> nuevaDivCantOficMap = new HashMap<>();
            for(Polygon zona : nuevaDivision){
                nuevaDivCantOficMap.put(zona, 0);
            }
            // Determinación de la cantidad de oficinas en cada subzona
            for(Oficina ofic : listaOficinas){
                for(Polygon zona : nuevaDivCantOficMap.keySet()){
                    Point oficPoint = gf.createPoint(new Coordinate(ofic.getCoordX(), ofic.getCoordY()));
                    if(zona.contains(oficPoint)){
                        nuevaDivCantOficMap.put(zona, nuevaDivCantOficMap.get(zona)+1);
                        break;
                    }
                }
            }

            // Quitar zona e insertar subzonas con sus respectivas cantidades
            mapaZonasCantOficinas.remove(zonaMaxCantOficinas);
            mapaZonasCantOficinas.putAll(nuevaDivCantOficMap);
        }
        // Generación de lista de zonas de reparto
        List<Geometry> listaSubZonas = new ArrayList<>();
        for(Polygon p : mapaZonasCantOficinas.keySet()){
            listaSubZonas.add(p.getEnvelope());
        }
        return listaSubZonas;
    }

    /*
    public HashMap<Geometry,Integer> getCostoZonas(List<Geometry> listZonas){

        
        for (Geometry zona : listaZonas ){
            Coordinate coordPedido = new Coordinate(pedido.getOficina().getCoordX(), pedido.getOficina().getCoordY());    
            if (SimplePointInAreaLocator.isContained(coordPedido, zona)){
                pedidosZona.add(pedido);   
                if (oficinasZona.contains(pedido.getOficina()) == false )
                    oficinasZona.add(pedido.getOficina());
            }
        }
        
    }
    */

    /*6. Asignar pedidos por zona Zonas de Reparto*/ 
    public List<List<Pedido>> asignarPedidosPorZona(){

        List<List<Pedido>> listaPedidosXZona = new ArrayList<>(); 
        
        //listaOficinasXZona

        for (Geometry zona : listaZonas ){
            List<Pedido> pedidosZona = new ArrayList<>();
            List<Oficina> oficinasZona = new ArrayList<>();
            for (Pedido pedido : listaPedidos){
                Coordinate coordPedido = new Coordinate(pedido.getOficina().getCoordX(), pedido.getOficina().getCoordY());    
                if (SimplePointInAreaLocator.isContained(coordPedido, zona)){
                    pedidosZona.add(pedido);   
                    if (oficinasZona.contains(pedido.getOficina()) == false )
                        oficinasZona.add(pedido.getOficina());
                }
            }    
            listaPedidosXZona.add(pedidosZona);
            listaOficinasXZona.add(oficinasZona);
        }

        return listaPedidosXZona;
    }

    /*7. Ordenamiento de prioridades de pedidos según tiempo, distancia, etc*/


    public void simulatedAnnealing(){

    /*
        Función que determinará el orden de las oficinas en base a 
            1. Tiempo de entrega pedidos
            2. Distancia (recorrido más corto posible)
    */ 

        for (int i = 0; i < listaZonas.size(); i++){
            long tiempoSalida = tiempoMaximoRegistroPedidos(listaPedidosPorZona.get(i));
            List<Triplet<String, Long, Integer>> listaTiempos = tiempoMaximoPedidos(listaPedidosPorZona.get(i), listaOficinasXZona.get(i));
            listaOficinasXZona.get(i).sort(new OficinasComparator(listaTiempos, false));
            SimulatedAnnealing sa = new SimulatedAnnealing(listaOficinasXZona.get(i), listaTiempos, tiempoSalida);
            sa.simulate();
            //System.out.println("Best Solution: "  + sa.getBest().getDistance());
            //System.out.println(sa.getBest());        

        }

        System.out.println("El simulated annealing terminó");
    }


/*FUNCIONES AUXILIARES*/
    public static double areaPoligono()
    {
        // Initialize area
        double area = 0.0;
        List<Geometry> ret = new ArrayList<>();

        // Calculate value of shoelace formula
        int j =  listaOficinas.size() - 1;
        for (int i = 0; i < listaOficinas.size(); i++ ){
            area += (listaOficinas.get(i).getCoordX() +
                     listaOficinas.get(j).getCoordX() )* 
                     (listaOficinas.get(i).getCoordY() +
                     listaOficinas.get(j).getCoordY() );
            j = 1;
        }
 
        return Math.abs(area / 2.0);
    }


    public List<Geometry> split(Polygon p) {

        /*TAREA QWERTY, QUE SE PUEDA DIVIDIR POR N partes*/ 
        List<Geometry> ret = new ArrayList<>();
        final Envelope envelope = p.getEnvelopeInternal();
        double minX = envelope.getMinX();
        double maxX = envelope.getMaxX();
        double midX = minX + (maxX - minX) / 2.0;
        double minY = envelope.getMinY();
        double maxY = envelope.getMaxY();
        double midY = minY + (maxY - minY) / 2.0;
    
        Envelope llEnv = new Envelope(minX, midX, minY, midY);
        Envelope lrEnv = new Envelope(midX, maxX, minY, midY);
        Envelope ulEnv = new Envelope(minX, midX, midY, maxY);
        Envelope urEnv = new Envelope(midX, maxX, midY, maxY);
        Geometry ll = factory.toGeometry(llEnv).intersection(p);
        Geometry lr = factory.toGeometry(lrEnv).intersection(p);
        Geometry ul = factory.toGeometry(ulEnv).intersection(p);
        Geometry ur = factory.toGeometry(urEnv).intersection(p);
        ret.add(ll);
        ret.add(lr);
        ret.add(ul);
        ret.add(ur);
    
        return ret;
    }

    public List<Polygon> splitRectangle(Polygon p){
        if(!p.isRectangle()) return null;

        double x_max = Double.NEGATIVE_INFINITY;
        double x_min = Double.POSITIVE_INFINITY;
        double y_max = Double.NEGATIVE_INFINITY;
        double y_min = Double.POSITIVE_INFINITY;
        
        for(Coordinate c : p.getCoordinates()) {
            if(c.getX() > x_max) x_max = c.getX();
            if(c.getX() < x_min) x_min = c.getX();
            if(c.getY() > y_max) y_max = c.getY();
            if(c.getY() < y_min) y_min = c.getY();
        }

        double x_med = (x_max + x_min) / 2;
        double y_med = (y_max + y_min) / 2;

        // Creación de coordenadas de los puntos //
        // 1---2---3 //
        // |   |   | //
        // 4---5---6 //
        // |   |   | //
        // 7---8---9 //
        Coordinate c1 = new Coordinate(x_min, y_max);
        Coordinate c2 = new Coordinate(x_med, y_max);
        Coordinate c3 = new Coordinate(x_max, y_max);
        Coordinate c4 = new Coordinate(x_min, y_med);
        Coordinate c5 = new Coordinate(x_med, y_med);
        Coordinate c6 = new Coordinate(x_max, y_med);
        Coordinate c7 = new Coordinate(x_min, y_min);
        Coordinate c8 = new Coordinate(x_med, y_min);
        Coordinate c9 = new Coordinate(x_max, y_min);

        // Creación de subzona 1
        Coordinate[] p1_coordinates = new Coordinate[] {c1,c2,c5,c4,c1};
        Polygon p1 = factory.createPolygon(p1_coordinates);

        // Creación de subzona 2
        Coordinate[] p2_coordinates = new Coordinate[] {c2,c3,c6,c5,c2};
        Polygon p2 = factory.createPolygon(p2_coordinates);

        // Creación de subzona 3
        Coordinate[] p3_coordinates = new Coordinate[] {c5,c6,c9,c8,c5};
        Polygon p3 = factory.createPolygon(p3_coordinates);

        // Creación de subzona 4
        Coordinate[] p4_coordinates = new Coordinate[] {c4,c5,c8,c7,c4};
        Polygon p4 = factory.createPolygon(p4_coordinates);

        // Retorno de lista de subzonas
        List<Polygon> listaSubZonas = Arrays.asList(p1,p2,p3,p4);
        return listaSubZonas;        
    }

    public static List<List<Pedido>> asignarPedidosAOficina(List<List<Pedido>> listaPedidosxZona, List<Oficina> listaOficinas){

        int i;
        List<List<Pedido>> listaTemp = new ArrayList<>();
        for(Oficina o : listaOficinas){
            List<Pedido> listPed = new ArrayList<>();
            for(List<Pedido> ped: listaPedidosxZona){
                for(Pedido p: ped){
                    if(o.getCodigo().equals(p.getOficina().getCodigo())){
                        listPed.add(p);
                    }
                }
            }
            listaTemp.add(listPed);
        }
        return listaTemp;
    }

    public static List<Triplet<String, Long, Integer>> tiempoMaximoPedidos(List<Pedido> pedidos, List<Oficina> oficinas){
        
        List<Triplet<String, Long, Integer>> listaTiempos = new ArrayList<>(); //String: CodOficina, Long: tiempo promedio en milisegundos, integer: num Pedidos)
        
        
        for (Oficina o : oficinas){
            long tiempo = 9 * (long)10e13; //en milisegundos
            int numeroPedidos = 0;
            for (Pedido p : pedidos){
                if (p.getOficina().getCodigo() == o.getCodigo()){
                    numeroPedidos++;
                    if (tiempo > p.getFechaHoraLimite().getTime())
                        tiempo = p.getFechaHoraLimite().getTime();
                }

            }    
            Triplet<String, Long, Integer> tiemposOficina = new Triplet<>(o.getCodigo(), tiempo, numeroPedidos);
            listaTiempos.add(tiemposOficina);
        }


        listaTiempos.sort(new TiemposOficinaComparator());
        //Collections.sort(oficinas, Comparator.comparing(item -> listaTiempos.indexOf(item)));

        return listaTiempos;

    }

    public static long tiempoMaximoRegistroPedidos(List<Pedido> pedidos){
            long tiempo = 0;
            for (Pedido p :pedidos){
                if (tiempo < p.getFechaHoraPedido().getTime()){
                    tiempo = p.getFechaHoraPedido().getTime();
                }
            }

            return tiempo;
    }
    
}