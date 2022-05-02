package pe.edu.pucp.g4algoritmos.solucion1;

public class ConstantesSA{
    
    private ConstantesSA(){

    }

    public static final double MIN_COORDINATE = -2;
    public static final double MAX_COORDINATE = 2;

    /*Temperature controls which states to accept and/or consider*/
    public static final double MIN_TEMPERATURE = 1;
    public static final double MAX_TEMPERATURE = 100;

    /*
    If the cooling rate is big, we consider just a few states in the search space.
    The cooling rate controls the number of states the algorithm will consider.
    */ 
    public static final double COOLING_RATE = 0.02; //Rate of decrease of temperature


}
