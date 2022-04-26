package pe.edu.pucp.g4algoritmos.astar;

import java.util.List;
import java.util.Map;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;


import java.util.ArrayList;
import java.util.HashMap;

import pe.edu.pucp.g4algoritmos.model.Oficina;
import pe.edu.pucp.g4algoritmos.model.Pedido;
import pe.edu.pucp.g4algoritmos.model.Ruta;

public class PrimeraSolucion {
    
    public static List<List<Pedido>> listaZonasDeReparto = new ArrayList<>(); //9 zonas de reparto, por defecto.
                //Maxima distancia entre zonas para generar una zona de reparto
                //coordenadas centricas para cada una de estas zonas
    public static int cantidadZonasDeReparto = 9;                             //Numero de zonas, 9 por defecto  
    public static List <Ruta> planesDeTransporte = new ArrayList<>();         //Lista de los planes de transporte
    public static List<Oficina> listaOficinas = new ArrayList<>();
    public static double areaMaxima = 0.0;
    public static Polygon polygon ;
    public static GeometryFactory factory = new GeometryFactory();
    //public static Geometry envelope = polygon.getEnvelope();
    public static List<Coordinate> coordinates;
    
    //mapa.put("nombre","cecilio");
    //mapa.put("apellidos","alvarez");
    
    public PrimeraSolucion() {
    }


    public void inicializar(){
        
        coordinates = generateCoordinatesOficina();
        //areaMaxima = areaPoligono();
    }

    public void generarZonasReparto(){
        

    }

    public static Polygon generarPoligono(){

    //List<Coordinate> coordinates = new
    GeometryFactory factory = new GeometryFactory();
    Polygon polygon = createPolygon();
    Geometry envelope = polygon.getEnvelope();
    return polygon;

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

    public static List<Coordinate> generateCoordinatesOficina(){

        List<Coordinate> coordinates = new ArrayList<>();
        final Envelope envelope = polygon.getEnvelopeInternal();
        
        for (Oficina oficina : listaOficinas){
            Coordinate coordinate = new Coordinate(oficina.getCoordX(), oficina.getCoordY());
            coordinates.add(coordinate);
        }
        return coordinates;
    }

    public static Polygon createPolygon(){
        Polygon polygon = factory.createPolygon(coordinates.toArray(new Coordinate[0]));
        return polygon;
    }
    /*
    public List<Geometry> split(Polygon p) {
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
        Geometry ll = JTS.toGeometry(llEnv).intersection(p);
        Geometry lr = JTS.toGeometry(lrEnv).intersection(p);
        Geometry ul = JTS.toGeometry(ulEnv).intersection(p);
        Geometry ur = JTS.toGeometry(urEnv).intersection(p);
        ret.add(ll);
        ret.add(lr);
        ret.add(ul);
        ret.add(ur);
    
        return ret;
      }
      */
    //This gives this for y
}
