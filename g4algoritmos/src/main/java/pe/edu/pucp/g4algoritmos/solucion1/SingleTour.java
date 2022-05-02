package pe.edu.pucp.g4algoritmos.solucion1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pe.edu.pucp.g4algoritmos.model.Oficina;

public class SingleTour {
    
    private List<Oficina> tour;
    private int distance; //summ of the path

    public SingleTour(){
        tour = new ArrayList<>();
        //for (int i = 0; i < Repository.getNumberOfCities(); ++i)
        //    tour.add(null);
    }

    public SingleTour(List<Oficina> cities){
        tour = new ArrayList<>();
        for (Oficina Oficina : cities)
            tour.add(Oficina);
    }

    public double getDistance(){
        if (distance == 0) {
            int tourDistance = 0;

            for (int OficinaIndex = 0; OficinaIndex < tour.size(); ++OficinaIndex){
                Oficina fromOficina = tour.get(OficinaIndex);
                Oficina destinationOficina = null;

                if(OficinaIndex + 1 < tour.size())
                    destinationOficina = tour.get(OficinaIndex + 1);
                else
                    destinationOficina = tour.get(0);

                //tourDistance += fromOficina.distanceTo(destinationOficina);

            }
            distance = tourDistance;
        }
        return distance;
    }


    public void generateIndividual() {
        /*Function to generate a random individual (random tour)*/
        //This is how we generate the hamiltonian cycle
        
        //for (int OficinaIndex=0; OficinaIndex<Repository.getNumberOfCities(); ++OficinaIndex)
        //    setOficina(OficinaIndex, Repository.getOficina(OficinaIndex));

        //The order is randomized
        Collections.shuffle(tour);
    }

    public List<Oficina> getTour() {
        return tour;
    }

    public void setTour(List<Oficina> tour) {
        this.tour = tour;
    }


    public void setDistance(int distance) {
        this.distance = distance;
    }


    public void setOficina(int index, Oficina Oficina){
        tour.set(index, Oficina);
    }

    public Oficina getOficina(int index){
        return tour.get(index);
    }

    public int getTourSize(){
        return tour.size();
    }

    @Override
    public String toString() {
        String s = "";
        for (Oficina Oficina : tour){
            s += Oficina.toString() + " - ";
        }

        return s;
    }




    
}
