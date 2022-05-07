package pe.edu.pucp.g4algoritmos.solucion2;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pe.edu.pucp.g4algoritmos.model.Oficina;

public class ConstantesPSO {
 
    public final static int NUM_DIMENSIONS = 2;
    public final static int NUM_PARTICLES = 10;
    public final static int MAX_ITERATIONS = 1000;
    public final static double MIN_X = -2;
    public final static double MAX_X = 2;
    public final static double MIN_Y = -2;
    public final static double MAX_Y = 2;
    public final static double w = 0.729; //Inertia Weight 
    public final static double c1 = 1.49; //Cognitive Weight / Local
    public final static double c2 = 1.49; //Social Weight / Global

    /*Pensalization Constants*/
    public final static double theta = 5.0;
    public final static double lambda = 2.0;
    
    
    public static double f(double[] data){
        /*The parameter for this function is an array of two parameters because we are working on 2 dimensions*/

        /*

        Cost function f:  
        f(x,y) = exp(-x*x-y*y)*sin(x) 
        
        */
        return Math.exp(-data[0]*data[0]-data[1]*data[1])*Math.sin(data[0]);
        
        
        //P.D: it is a 2 dimensional function, a picture can be found on this proyect

        
    }

    public static double f(List<Position> positions, int num_dimensions, int num_almacenes){

        List<List<Oficina>> oficinasPorAlmacen = new ArrayList<>(num_almacenes);
        
        for (Position p: positions){
            int aQueAlmacenPertenece = (int) p.getRandomPosition();
            
        }

        for (int i = 0; i < num_almacenes; i++){
            
        }

        /*

        Sd: City sequence of depot d

        DS(p): Travelling time of trip p
        TV(p): Amount of time window violation in trip p
        LV(p): Amount of load violation of vehicle in trip p
        
        */

        
        return 0;
    }
}
