package pe.edu.pucp.g4algoritmos.solucion2;

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

    public Particle(List<OficinaPSO> listOficinas, List<Double> velocity) {

        this.currentPosition = new ArrayList<>();
        this.costo = 0;
        this.velocity = new ArrayList<>();
        this.bestPosition = new ArrayList<>();

        System.arraycopy(velocity, 0, this.velocity, 0, velocity.size());

        int i = 0;
        for (OficinaPSO oficina : listOficinas ){
            Position pos = new Position(oficina, 0.0);
            this.currentPosition.add(pos);

            this.velocity.add(velocity.get(i));
            i++;
        }
    }

    public Particle(List<OficinaPSO>listOficinas, List<Double> positions, List<Double> velocity) {

        this.currentPosition = new ArrayList<>();
        this.costo = 0;
        this.velocity = new ArrayList<>();
        this.bestPosition = new ArrayList<>();

        for (int i = 0; i < listOficinas.size(); i++ ){
            Position pos = new Position(listOficinas.get(i), positions.get(i));
            this.currentPosition.add(pos);
            this.bestPosition.add(pos);
            this.velocity.add(velocity.get(i));
        }
    }

    public void checkBestSolution(double[] globalBestSolution){
        //We are trying to find the MINIMUM, hence, the best has to be smaller
        //if (ConstantesPSO.f(this.bestPosition) < ConstantesPSO.f(globalBestSolution))
        //    globalBestSolution = this.bestPosition;
    }


    /*Getters, Setters and Overrides*/
    
    public List<Position> getCurrentPositions() {
        return currentPosition;
    }

    public void setCurrentPosition(List<Position> currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Position getPosition(int index){
        return currentPosition.get(index);
    }

    public void setPosition(int index, double position) {
        Position auxPosition = this.currentPosition.get(index);
        auxPosition.setRandomPosition(position);
        this.currentPosition.set(index, auxPosition);
    }

    public List<Double> getVelocities() {
        return velocity;
    }

    public void setVelocities(List<Double> velocity) {
        this.velocity = velocity;
    }

    public void setVelocity(int index, double speed) {
        this.velocity.set(index, speed);
    }


    public double getVelocity(int index){
        return velocity.get(index);
    }

    public List<Position> getBestPositions() {
        return bestPosition;
    }

    public void setBestPositions(List<Position> bestPosition) {
        this.bestPosition = bestPosition;
    }

    public Position getBestPosition(int index) {
        return bestPosition.get(index);
    }
    
    public void setBestPosition(int index, double position) {
        Position auxPosition = this.bestPosition.get(index);
        auxPosition.setRandomPosition(position);
        this.bestPosition.set(index, auxPosition);
    }


    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    @Override
    public String toString() {
        return "Particle [bestPosition=" + bestPosition + ", costo=" + costo + ", currentPosition=" + currentPosition
                + ", velocity=" + velocity + "]";
    }


    
}

