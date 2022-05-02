package pe.edu.pucp.g4algoritmos.solucion1;
import pe.edu.pucp.g4algoritmos.model.*;
import java.util.List;
import java.util.ArrayList;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.noding.FastNodingValidator;
import org.locationtech.jts.algorithm.locate.SimplePointInAreaLocator;

public class PrimeraSolucion{

    public static List<List<Pedido>> listaPedidosPorZona = new ArrayList<>(); //9 zonas de reparto, por defecto.
    public static List<List<Oficina>> listaOficinasXZona = new ArrayList<>(); 
                //Maxima distancia entre zonas para generar una zona de reparto
                //coordenadas centricas para cada una de estas zonas
    public static List<Geometry> listaZonas = new ArrayList<>();
    
    public static List<Pedido> listaPedidos = new ArrayList<>(); //Lista inicial de los pedidos
    public static int cantidadZonasDeReparto = 9;                             //Numero de zonas, 9 por defecto  
    public static List <Ruta> planesDeTransporte = new ArrayList<>();         //Lista de los planes de transporte
    public static List<Oficina> listaOficinas = new ArrayList<>();
    public static List<Camion> listaCamiones = new ArrayList<>();
    public static double areaMaxima = 0.0;
    public static Polygon poligonPedidos ;
    public static GeometryFactory factory = new GeometryFactory();
    //public static Geometry envelope = polygon.getEnvelope();
    public static List<Coordinate> coordinates;
    public static int paquetes;
    public static Oficina almacen; //Almacen seleccionado

    public PrimeraSolucion(){
        
    }

    public void inicializar(){
        
        listaOficinas = contabilizarOficinas();
        coordinates = generateCoordinatesOficina();
        poligonPedidos = crearPoligono();
        listaCamiones = seleccionarCamiones();
        listaZonas = generarZonasReparto();

        
        listaPedidosPorZona = asignarPedidosPorZona();

        //Lo demas se hara con Simulated Annealing
        //ordenarPedidos();
        //planesDeTransporte = asignarPedidosCamiones();

        //areaMaxima = areaPoligono();
    }

    /*1. Determinar lista de oficinas de los pedidos*/ 
    public static List<Oficina> contabilizarOficinas(){
        List<Oficina> oficinas = new ArrayList<>();
        for (Pedido p : listaPedidos){
            if(listaOficinas.contains(p.getOficina()) == false) {
                listaOficinas.add(p.getOficina());
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
        //Generar polígono


        return coordinates;
    }


    /*3. Generar AREA TOTAL DE REPARTO polígono para distribución */ 
    public static Polygon crearPoligono(){
        //Para arreglar: debe crear un poligono considerando solamente puntos exteriores.
        Polygon polygon = factory.createPolygon(coordinates.toArray(new Coordinate[0]));
        return polygon;
    }

    //Determinar CUANTAS ZONAS DE REPARTO VAMOS A UTILIZAR (Cuantos camiones y qué camiones vamos a utilizar).
    /*4. Determinar lista de camiones a utilizar*/
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
        
        int numeroZonas = listaCamiones.size();
        numeroZonas = 4;
        List<Geometry> listZonas = split(poligonPedidos);
        return listZonas;
    }

  
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

    /*Ordenamiento de prioridades de pedidos según tiempo, distancia, etc*/


    public void simulatedAnnealing(){

        for (int i = 0; i < listaZonas.size(); i++){
/*
            SimulatedAnnealing sa = new SimulatedAnnealing(listaOficinasXZona.get(index));
            sa.simulate();
            System.out.println("Best Solution: "  + sa.getBest().getDistance());
            System.out.println(sa.getBest());        
*/
        }


    }



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
}