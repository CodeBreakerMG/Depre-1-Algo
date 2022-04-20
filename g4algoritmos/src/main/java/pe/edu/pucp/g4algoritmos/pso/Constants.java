package pe.edu.pucp.g4algoritmos.pso;

public class Constants {
    
    private Constants(){

    }

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

    public static double f(double[] data){
        /*The parameter for this function is an array of two parameters because we are working on 2 dimensions*/

        /*

        Cost function f:  
        f(x,y) = exp(-x*x-y*y)*sin(x) 
        
        */
        return Math.exp(-data[0]*data[0]-data[1]*data[1])*Math.sin(data[0]);
        
        
        //P.D: it is a 2 dimensional function, a picture can be found on this proyect

        
    }
    
}
