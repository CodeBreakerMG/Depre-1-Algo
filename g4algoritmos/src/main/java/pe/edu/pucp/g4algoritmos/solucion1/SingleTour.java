package pe.edu.pucp.g4algoritmos.solucion1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
 
import pe.edu.pucp.g4algoritmos.model.Oficina;
import pe.edu.pucp.g4algoritmos.model.Tramo;

public class SingleTour {
    
    private List<Oficina> ciudadesPedido;
    private List<Double> tiemposLlegadaOficinas; //Tiempo de llegada real a cada oficina (en horas)
    private List<List<Tramo>> tramosARecorrerPorOficina;
    private double costo = 0; //summ of the path

    public SingleTour(RepositorySA repositorio){
        ciudadesPedido = new ArrayList<>();
        for (int i = 0; i < repositorio.getNumberOfOficinas(); ++i)
          ciudadesPedido.add(null);
    }

    public SingleTour(List<Oficina> cities, List<Double> tiemposLlegadaOficinas,  List<List<Tramo>> tramosARecorrer, double costo, RepositorySA repositorio){
        this.ciudadesPedido = new ArrayList<>();
        this.tramosARecorrerPorOficina = new ArrayList<>();
        this.tiemposLlegadaOficinas = new ArrayList<>();

        for ( int i = 0; i < cities.size(); i++){
            ciudadesPedido.add(cities.get(i));
            this.tiemposLlegadaOficinas.add(tiemposLlegadaOficinas.get(i));
            List<Tramo> tramos = new ArrayList<>();
            for (Tramo tramo : tramosARecorrer.get(i) ){
                tramos.add(tramo);
            }
            this.tramosARecorrerPorOficina.add(tramos);
        }
        this.costo = costo;
    }

    private double calcularTramos(RepositorySA repository) {
        
        this.tramosARecorrerPorOficina = new ArrayList<>();
        tiemposLlegadaOficinas = new ArrayList<>();
        double tiempoTotal = 0.0;
        
        for (int OficinaIndex = 0; OficinaIndex < ciudadesPedido.size(); ++OficinaIndex){
            Oficina fromOficina = ciudadesPedido.get(OficinaIndex);
            Oficina destinationOficina = null;
            //System.out.println(tiempoTotal);
            if(OficinaIndex + 1 < ciudadesPedido.size())
                destinationOficina = ciudadesPedido.get(OficinaIndex + 1);
            else
                destinationOficina = ciudadesPedido.get(0);

            System.out.println("");    
            System.out.println("Oficina inicial: " + fromOficina.getProvincia());
            System.out.println("Oficina final: " + destinationOficina.getProvincia());
            System.out.println(""); 
            List<Tramo> tramosParciales = fromOficina.recorridoHasta(destinationOficina);
            double tiempoLlegada = 0.0;
            System.out.println("Tramos parciales: ");
            for (Tramo t : tramosParciales){
                System.out.print(t.getCiudadInicio() + "-" + t.getCiudadFin() + " ");
            }
            System.out.println("");
            for (Tramo tramo : tramosParciales)
                tiempoLlegada += tramo.getCosto();

            /*MODIFICAR PARA CUANDO SE REALICEN MANTENIMIENTOS*/
            tiempoLlegada += (tiemposLlegadaOficinas.isEmpty() == true) ? repository.getTiempoSalida() : tiemposLlegadaOficinas.get(OficinaIndex - 1);
            
            tiempoTotal += tiempoLlegada;
            tiemposLlegadaOficinas.add(tiempoLlegada);        
            this.tramosARecorrerPorOficina.add(tramosParciales);
            
        }

        return tiempoTotal;
        
    }
 
    private void cambiarTramosPorOficina(int oficinaIndex, RepositorySA repository){

        Oficina fromOficina = ciudadesPedido.get(oficinaIndex);
        Oficina destinationOficina = null;

        if(oficinaIndex + 1 < ciudadesPedido.size())
            destinationOficina = ciudadesPedido.get(oficinaIndex + 1);
        else
            destinationOficina = ciudadesPedido.get(0);

        List<Tramo> tramosParciales = fromOficina.recorridoHasta(destinationOficina);
        setTramosParciales(tramosParciales, oficinaIndex);

    } 

    public double calcularCostoTotal(RepositorySA repository){


        tiemposLlegadaOficinas = new ArrayList<>();
        double tiempoTotal = 0.0;
        for (int index = 0; index < ciudadesPedido.size(); index++){
            double tiempo = 0.0;
            List<Tramo> tramos = tramosARecorrerPorOficina.get(index);
            for (Tramo tramo : tramos)
                tiempo += tramo.getCosto();

            /*MODIFICAR PARA CUANDO SE REALICEN MANTENIMIENTOS*/
            tiempo += (index == 0) ? repository.getTiempoSalida() : tiemposLlegadaOficinas.get(index - 1); 
            tiemposLlegadaOficinas.add(tiempo);
            tiempoTotal += tiempo;
        }         
        return tiempoTotal;
    }

    private double calcularCosto(){
        
        double cost = 0.0;
        
            for (double c : tiemposLlegadaOficinas)
                cost += c;

        return cost;
    }


    public boolean cumpleConEntregas(RepositorySA repository){
        
        double tiempoMaxOficina;
        double tiempoLlegafaOficina;
        for (int i = 0; i < this.getTourSize(); i++){
            tiempoMaxOficina = repository.getTiempoMaximoOficina(this.getOficina(i));
            tiempoLlegafaOficina = tiemposLlegadaOficinas.get(i);
            //if (repository.getTiempoMaximoOficina(this.getOficina(i)) < tiemposLlegadaOficinas.get(i))
            if (tiempoMaxOficina < tiempoLlegafaOficina);
            return false;
        }
        return true;
        
    }


    public void generateIndividual(RepositorySA repositorio) {
        /*Function to generate a random individual (random ciudadesPedido)*/
        //This is how we generate the hamiltonian cycle
        List<Oficina> newListOficina = new ArrayList<>();
        for (int index = 0; index < repositorio.getNumberOfOficinas(); ++index){
            if(repositorio.getOficina(index).EsAlmacen() == false){
                newListOficina.add(repositorio.getOficina(index));
                //setOficina(index, repositorio.getOficina(index));
            }
        }
        
        //The order is randomized
        //Collections.shuffle(ciudadesPedido);
        Collections.shuffle(newListOficina);
        setOficina(0, repositorio.getAlmacen());
        for (int index = 0; index < newListOficina.size(); ++index){
            
           setOficina(index+1, newListOficina.get(index));
            
        }
        calcularTramos(repositorio); 
        this.costo = calcularCosto();
        

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


    public void setTramosParciales(List<Tramo> tramos, int index) {
        this.tramosARecorrerPorOficina.set(index, tramos);
    }

    public List<Double> getTiemposLlegadaOficinas() {
        return tiemposLlegadaOficinas;
    }

    public void setTiemposLlegadaOficinas(List<Double> tiemposLlegadaOficinas) {
        this.tiemposLlegadaOficinas = tiemposLlegadaOficinas;
    }



    public List<List<Tramo>> getTramosARecorrerPorOficina() {
        return tramosARecorrerPorOficina;
    }

    public void setTramosARecorrerPorOficina(List<List<Tramo>> tramosARecorrerPorOficina) {
        this.tramosARecorrerPorOficina = tramosARecorrerPorOficina;
    }

    public void setCosto(double cost) {
        this.costo = cost;
    }

    public double getCosto(){
        return this.costo;
    }


    public void setOficina(int index, Oficina Oficina, RepositorySA repository){
        
        ciudadesPedido.set(index, Oficina);

        //Anterior:
        if (index == 0)
            cambiarTramosPorOficina(ciudadesPedido.size() - 1, repository);
        else
            cambiarTramosPorOficina(index - 1, repository);
        
        //Posterior
        cambiarTramosPorOficina(index, repository);

        //Calculo del nuevo costo (Y tiempo de llegada a cada oficina)
        
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
