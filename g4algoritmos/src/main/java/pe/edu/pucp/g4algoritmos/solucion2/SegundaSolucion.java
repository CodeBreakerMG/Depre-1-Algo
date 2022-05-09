package pe.edu.pucp.g4algoritmos.solucion2;

import pe.edu.pucp.g4algoritmos.model.*;
import pe.edu.pucp.g4algoritmos.utilitarios.Stats;

import java.io.PrintWriter;
import java.util.List;

import org.locationtech.jts.algorithm.locate.SimplePointInAreaLocator;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class SegundaSolucion {
    
        /*Variables OUTPUT*/ 
        private  List<Date> tiempoDeSalidasZona = new ArrayList<>();
        
        private  List <Ruta> planesDeTransporte = new ArrayList<>(); //Lista de los planes de transporte o RUTAS por camion

        /*Variables INPUT*/ 
        private  List<Pedido> listaPedidos; //Lista inicial de los pedidos
        private  List<List<OficinaPSO>> listaOficinasPorZona; //Lista de oficinas que tienen al menos un pedido
        private  List<Oficina> listaAlmacenes; //Lista de oficinas que tienen al menos un pedido
        private  List<List<Camion>> listaCamionesPorAlmacen; //Lista de camiones disponibles de un todos los almacenes


        private  int paquetes; //Cantidad total de paquetes A 
        
        public long inicializar(List<Pedido> pedidos, PrintWriter writer){
            
            System.out.println("Solución PSO: ");
            
            this.listaAlmacenes = Mapa.listaAlmacenes;
            this.listaPedidos = pedidos;
            this.listaCamionesPorAlmacen = new ArrayList<>();

            for (Oficina almacen : listaAlmacenes){
                List<Camion> camiones = Mapa.getCamionesPorAlmacen(almacen);
                this.listaCamionesPorAlmacen.add(camiones) ;
            }

            /*I. Oficinas de todos los pedido*/
            List<OficinaPSO> listaOficinas = new ArrayList<>();
            listaOficinas = contabilizarOficinas();
            writer.println(String.format("Cantidad de oficinas a recorrer:  %4d", listaOficinas.size()));

            /*II. Generar Zonas de Reparto y asignar oficinas*/
            List<Coordinate> coordinates =  generateCoordinatesOficina(listaOficinas);
            Polygon polygonReparto = crearPoligono(coordinates);
            
            List<Geometry> listaZonas = generarZonasReparto(polygonReparto, listaOficinas); //Lista de Zonas (Formato de Polígono)
        
            this.listaOficinasPorZona = asignarOficinasPorZona(listaZonas, listaOficinas);

            /*III. Generación de la solución: PSO*/
            LocalDateTime startTime  = LocalDateTime.now();
            particleSwarmOptimization();
            LocalDateTime endTime = LocalDateTime.now();

            return ChronoUnit.SECONDS.between(startTime, endTime);
            
        }

        /*1. Determinar lista de oficinas de los pedidos*/ 
        private List<OficinaPSO> contabilizarOficinas() {
            List<OficinaPSO> oficinasPSO = new ArrayList<>();
            List<Oficina> oficinas = new ArrayList<>();

            for (Pedido p : listaPedidos){
                if(oficinas.contains(p.getOficina()) == false) {
                    oficinas.add(p.getOficina());
                }
            }

            for (Oficina o: oficinas){
                long max_miliseconds = 9 * (long)10e13; //en milisegundos
                long min_miliseconds = 0; //en milisegundos
                int cargaPaquetes = 0;
                OficinaPSO oficinaPSO = new OficinaPSO(o);
                for (Pedido p : listaPedidos){
                    if (p.getOficina().getCodigo() == o.getCodigo()){
                        oficinaPSO.addPedido(p);
                        max_miliseconds = p.getFechaHoraLimite().getTime() < max_miliseconds ? p.getFechaHoraLimite().getTime() : max_miliseconds;
                        min_miliseconds = p.getFechaHoraPedido().getTime() > min_miliseconds ? p.getFechaHoraPedido().getTime() : min_miliseconds;
                        cargaPaquetes += p.getCantidadActual();
                        
                    }
                }
                oficinaPSO.setTiempoLimiteLlegada(new Date(max_miliseconds));
                oficinaPSO.setTiempoMinimoSalidaCamion(new Date(min_miliseconds));
                oficinaPSO.setQ(cargaPaquetes);
                if (cargaPaquetes > 90)
                    System.out.println("Excedida carga paquetes a una ofina "+  cargaPaquetes);
                oficinasPSO.add(oficinaPSO);
            }

            return oficinasPSO;
        }



    /*2. Determinar los puntos totales de cada oficina*/ 
    public List<Coordinate> generateCoordinatesOficina(List<OficinaPSO> oficinas){
        List<Coordinate> coordinates = new ArrayList<>();
        for (OficinaPSO oficina : oficinas){
            Coordinate coordinate = new Coordinate(oficina.getX(), oficina.getY());
            coordinates.add(coordinate);
        }
        return coordinates;
    }


    /*3. Generar AREA TOTAL DE REPARTO polígono para distribución */ 
    public Polygon crearPoligono(List<Coordinate> coordinates){

        // El polígono a crear será un rectángulo circunscrito a la
        // figura formada por las coordenadas de los puntos de la zona
        
        GeometryFactory factory = new GeometryFactory(); //Variable auxiliar de Geometría. Para generar zonas de reparto
    

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


    public List<Geometry> generarZonasReparto(Polygon polygonPedidos, List<OficinaPSO> listaOficinas){
    
        final double MAX_DESV_STD = 5.0;
        GeometryFactory gf = new GeometryFactory();

        if (listaOficinas.size() < 5){
            List<Geometry> listaSubZonas = new ArrayList<>();

            listaSubZonas.add(polygonPedidos);
            return listaSubZonas;
            
        }

        // Creación de HashMap con zonas y cantidad de oficinas = 0
        List<Polygon> listZonas = splitRectangle(polygonPedidos);
        HashMap<Polygon, Integer> mapaZonasCantOficinas = new HashMap<>();
        for(Polygon zona : listZonas){
            mapaZonasCantOficinas.put(zona, 0);
        }

        // Determinación de cantidad de oficinas        
        for(OficinaPSO ofic : listaOficinas){
            for(Polygon zona : mapaZonasCantOficinas.keySet()){
                Point oficPoint = gf.createPoint(new Coordinate(ofic.getX(), ofic.getY()));
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
            for(OficinaPSO ofic : listaOficinas){
                for(Polygon zona : nuevaDivCantOficMap.keySet()){
                    Point oficPoint = gf.createPoint(new Coordinate(ofic.getX(), ofic.getY()));
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

        /*6. Asignar pedidos por zona Zonas de Reparto*/ 
        public List<List<OficinaPSO>> asignarOficinasPorZona(List<Geometry> listaZonas, List<OficinaPSO> listaOficinas){

            List<List<OficinaPSO>> listaOficinasXZona = new ArrayList<>(); 
            
            //listaOficinasXZona
    
            for (Geometry zona : listaZonas ){
                
                List<OficinaPSO> oficinasZona = new ArrayList<>();                
                for (OficinaPSO o : listaOficinas){

                    if (SimplePointInAreaLocator.isContained(new Coordinate(o.getX(), o.getY()), zona)){
                        oficinasZona.add(o);
                    }
                }    
                listaOficinasXZona.add(oficinasZona);
            }
    
            return listaOficinasXZona;
        }



        /*7. Llamar al PSO. Determinar lista de oficinas de los pedidos*/ 
        private double particleSwarmOptimization(){

            double costoTotal = 0.0;

            for (List<OficinaPSO> listaOficinas : listaOficinasPorZona){

                if (listaOficinas.size() == 0) continue;

                updateListaCamiones();

                Date tiempoSalida = listaOficinas.get(0).tiempoMinimoSalidaCamion();

                for (OficinaPSO oficina : listaOficinas)
                    tiempoSalida = AuxiliaryFunctions.minimumDate(oficina.tiempoMinimoSalidaCamion(), tiempoSalida);
    
                HybridParticleSwarmOptimization pso = new HybridParticleSwarmOptimization(listaOficinas, listaCamionesPorAlmacen, listaAlmacenes, tiempoSalida);
                pso.solve();
                pso.printOficinasXAlmacen();
                pso.executeChanges();
                costoTotal += pso.getBestCost();
               // planesDeTransporte = HybridParticleSwarmOptimization
    
            }
            System.out.println("Se termino el PSO con exito. \nCosto de la Solución: " + costoTotal );

            return costoTotal;

        }

        /*AUX FUNCTIONS*/

        
        private void updateListaCamiones() {

            this.listaCamionesPorAlmacen = new ArrayList<>();
            for (Oficina almacen : listaAlmacenes){

                int numCamiones = Mapa.getCamionesPorAlmacen(almacen).size();

                if (numCamiones <= 0){
                    listaAlmacenes.remove(almacen);
                    updateListaCamiones();
                    break;
                }

                listaCamionesPorAlmacen.add(Mapa.getCamionesPorAlmacen(almacen));
            }

        }

        public List<Polygon> splitRectangle(Polygon p){

            
            if(!p.isRectangle()) return null;
    
            GeometryFactory factory = new GeometryFactory(); //Variable auxiliar de Geometría. Para generar zonas de reparto

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

        
            //Calcula el tiempo del pedido que se realizó último en horas
        public static long tiempoMaximoRegistroPedidos(List<Pedido> pedidos){
            long tiempo = 0;
            
            for (Pedido p :pedidos){
                if (tiempo < p.getFechaHoraPedido().getTime()){
                    tiempo = p.getFechaHoraPedido().getTime();
                }
            }

            return tiempo;
        }

        public static Date fechaHoraMaximaSalida(List<Pedido> pedidos){
            long tiempo = 0;
            Date fecha = new Date();
            for (Pedido p :pedidos){
                if (tiempo < p.getFechaHoraPedido().getTime()){
                    tiempo = p.getFechaHoraPedido().getTime();
                    fecha = p.getFechaHoraPedido();
                }
            }

            return fecha;
        }
        //Procedimiento para imprimir la fecha de Salida del camión para la entrega de pedidos en una zoma.
        public static String formatDateString(Date fecha){
            SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss yyyy/MM/dd ");
            String cadena= f.format(fecha);
            return cadena; 
        }

}
