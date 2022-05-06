ackage pe.edu.pucp.g4algoritmos.solucion2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
 
import pe.edu.pucp.g4algoritmos.model.Oficina;
import pe.edu.pucp.g4algoritmos.model.Tramo;

public class Particle {


    private List<Position> currentPosition; //xi -> actual (oficina, posicion= "almacen"."prioridad")
    private List<Double>   velocity;        //vi -> (velocidad para la posicion: siendo posicion= "almacen"."prioridad")
    private List<Position> bestPosition;    //pi -> mejor local (oficina, posicion= "almacen"."prioridad")
    private double costo = 0; //summ of the path

    public Particle(List<Oficina> listOficinas, List<Double> velocity) {

        this.currentPosition = new ArrayList<>(listOficinas.size());
        
        this.velocity = new ArrayList<>(listOficinas.size());
        this.bestPosition = new ArrayList<>(listOficinas.size());

        System.arraycopy(velocity, 0, this.velocity, 0, velocity.size());

        
        System.arraycopy(currentPosition, 0, this.currentPosition, 0, velocity.size());
        
    
    }

    public void checkBestSolution(double[] globalBestSolution){
        //We are trying to find the MINIMUM, hence, the best has to be smaller
        if (ConstantesPSO.f(this.bestPosition) < ConstantesPSO.f(globalBestSolution))
            globalBestSolution = this.bestPosition;
    }

    /*Getters, Setters and Overrides*/

    public double[] getPosition() {
        return position;
    }


    public void setPosition(double[] position) {
        this.position = position;
    }


    public double[] getVelocity() {
        return velocity;
    }


    public void setVelocity(double[] velocity) {
        this.velocity = velocity;
    }


    public double[] getBestPosition() {
        return bestPosition;
    }


    public void setBestPosition(double[] bestPosition) {
        this.bestPosition = bestPosition;
    }


    @Override
    public String toString() {
        
        return "Best position so far: (" + this.bestPosition[0] + "," + this.bestPosition[1] + ")";
    }
}

