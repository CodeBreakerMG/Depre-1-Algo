package pe.edu.pucp.g4algoritmos.solucion1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
 
import pe.edu.pucp.g4algoritmos.model.Oficina;
import pe.edu.pucp.g4algoritmos.model.Tramo;

public class SingleTour {
    
    private List<Oficina> ciudadesPedido;
    private List<Tramo> tramosARecorrer;
    private double costo = 0;; //summ of the path

    public SingleTour(RepositorySA repositorio){
        ciudadesPedido = new ArrayList<>();
        for (int i = 0; i < repositorio.getNumberOfOficinas(); ++i)
          ciudadesPedido.add(null);
        tramosARecorrer = getTramos();
        costo = calcularCosto();

    }

    public SingleTour(List<Oficina> cities){
        ciudadesPedido = new ArrayList<>();
        for (Oficina Oficina : cities)
            ciudadesPedido.add(Oficina);
        tramosARecorrer = getTramos();
        costo = calcularCosto();
    }

    /*
    public double getCost(){

        AStarModified search;
        //new AStarModified(new VertexOficina(ruta.getListaOficinas().get(i)) , new VertexOficina(ruta.getListaOficinas().get(i+1)));
        //search.run();
        //List<Tramo> tramos = new ArrayList<>();
                //tramos = search.printSolutionPath(); Se debería obtener la lista de tramos desde la primera ciudad a la segunda
        //ruta.addTramos(tramos);
  
        
        if (cost == 0) {
            int ciudadesPedidocost = 0;

            for (int OficinaIndex = 0; OficinaIndex < ciudadesPedido.size(); ++OficinaIndex){
                Oficina fromOficina = ciudadesPedido.get(OficinaIndex);
                Oficina destinationOficina = null;

                if(OficinaIndex + 1 < ciudadesPedido.size())
                    destinationOficina = ciudadesPedido.get(OficinaIndex + 1);
                else
                    destinationOficina = ciudadesPedido.get(0);

                ciudadesPedidocost += fromOficina.costTo(destinationOficina);

            }
            cost = ciudadesPedidocost;
        }
        return cost;
    }
*/

    private List<Tramo> getTramos() {
        
        
        //new AStarModified(new VertexOficina(ruta.getListaOficinas().get(i)) , new VertexOficina(ruta.getListaOficinas().get(i+1)));
        //search.run();
        //List<Tramo> tramos = new ArrayList<>();
                //tramos = search.printSolutionPath(); Se debería obtener la lista de tramos desde la primera ciudad a la segunda
        //ruta.addTramos(tramos);
        List<Tramo> tramos = new ArrayList<>();
               

        for (int OficinaIndex = 0; OficinaIndex < ciudadesPedido.size(); ++OficinaIndex){
            Oficina fromOficina = ciudadesPedido.get(OficinaIndex);
            Oficina destinationOficina = null;

            if(OficinaIndex + 1 < ciudadesPedido.size())
                destinationOficina = ciudadesPedido.get(OficinaIndex + 1);
            else
                destinationOficina = ciudadesPedido.get(0);

            tramos.addAll(fromOficina.recorridoHasta(destinationOficina));

        }
            
        
        return tramos;
    }

    private double calcularCosto(){
        double cost = 0.0;

        for (Tramo tramo : tramosARecorrer)
            cost += tramo.getCosto();

        return cost;
    }

    public void generateIndividual(RepositorySA repositorio) {
        /*Function to generate a random individual (random ciudadesPedido)*/
        //This is how we generate the hamiltonian cycle
        
        for (int OficinaIndex=0; OficinaIndex<repositorio.getNumberOfOficinas(); ++OficinaIndex)
            setOficina(OficinaIndex, repositorio.getOficina(OficinaIndex));

        //The order is randomized
        Collections.shuffle(ciudadesPedido);
    }

    public List<Oficina> getciudadesPedido() {
        return ciudadesPedido;
    }

    public void setciudadesPedido(List<Oficina> ciudadesPedido) {
        this.ciudadesPedido = ciudadesPedido;
    }

    
    public List<Oficina> getTour() {
        return ciudadesPedido;
    }

    public void setTour(List<Oficina> ciudadesPedido) {
        this.ciudadesPedido = ciudadesPedido;
    }




    public void setCost(int cost) {
        this.costo = cost;
    }

    public double getCosto(){
        return this.costo;
    }


    public void setOficina(int index, Oficina Oficina){
        ciudadesPedido.set(index, Oficina);
    }

    public Oficina getOficina(int index){
        return ciudadesPedido.get(index);
    }

    public int getTourSize(){
        return ciudadesPedido.size();
    }

    @Override
    public String toString() {
        String s = "";
        for (Oficina Oficina : ciudadesPedido){
            s += Oficina.toString() + " - ";
        }

        return s;
    }




    
}
