package pe.edu.pucp.g4algoritmos.solucion1;
 
import java.util.ArrayList;
import java.util.List;
import java.io.PrintWriter;
import java.util.Date;


import org.javatuples.Triplet;

import pe.edu.pucp.g4algoritmos.model.Oficina;
import pe.edu.pucp.g4algoritmos.model.Tramo;

public class SimulatedAnnealing {

    private SingleTour actualState;
    private SingleTour nextState;
    private SingleTour bestState;

    private RepositorySA repository;
    
    
    public SimulatedAnnealing(List<Oficina> oficinas,  List<Triplet<String, Long, Integer>> listaTiempos, long tiempoInicio, Date fechaInicio ){
        
        this.repository = new RepositorySA(oficinas, listaTiempos, tiempoInicio, fechaInicio);
        
    }
    
    public void simulate(PrintWriter writer) {

        double temp = ConstantesSA.MAX_TEMPERATURE;

        actualState = new SingleTour(repository);
        actualState.generateIndividual(repository, writer);
        bestState = new SingleTour(actualState.getTour(), actualState.getTiemposLlegadaOficinas(), actualState.getTramosARecorrerPorOficina(), actualState.getCosto(), repository);

        //System.out.println("Costo Solucion Actual: " + actualState.getCosto());

        while ( temp > ConstantesSA.MIN_TEMPERATURE ) {

            nextState = generateNeighborState(actualState);

            //**SI NO CUMPLE CON LOS TIEMPOS LIMITES, NO SE ACEPTA
            
            /*
            if (nextState.cumpleConEntregas(repository) == false){
                temp *= (1 - ConstantesSA.COOLING_RATE);
                continue;
            }
            */
            double currentEnergy = actualState.getCosto();
            double neighborEnergy = nextState.getCosto();


            if (acceptanceProbability(currentEnergy, neighborEnergy, temp) > Math.random())
                actualState = new SingleTour(nextState.getTour(), nextState.getTiemposLlegadaOficinas(), nextState.getTramosARecorrerPorOficina(), nextState.getCosto(), repository);

            if (actualState.getCosto() < bestState.getCosto())
                bestState = new SingleTour(actualState.getTour(), actualState.getTiemposLlegadaOficinas(), actualState.getTramosARecorrerPorOficina(), actualState.getCosto(), repository);
            temp *= (1 - ConstantesSA.COOLING_RATE);
        }
    }

    private SingleTour generateNeighborState(SingleTour actualState) {
		
		SingleTour newState = new SingleTour(actualState.getTour(), actualState.getTiemposLlegadaOficinas(), actualState.getTramosARecorrerPorOficina(), actualState.getCosto(), repository);
		
        /*Cerseorarse que no sea almacen*/

        int randomIndex1 = (int) (Math.random()*newState.getTourSize());
		int randomIndex2 = (int) (Math.random()*newState.getTourSize());
		
        if(newState.getciudadesPedido().size()>2){
            while (newState.getciudadesPedido().get(randomIndex1).EsAlmacen() == true || newState.getciudadesPedido().get(randomIndex2).EsAlmacen() == true ) {
                randomIndex1 = (int) (Math.random()*newState.getTourSize());
                randomIndex2 = (int) (Math.random()*newState.getTourSize());
            }
        }
		
		Oficina city1 = newState.getOficina(randomIndex1);
		Oficina city2 = newState.getOficina(randomIndex2);
		
		newState.setOficina(randomIndex1, city2, this.repository);
		newState.setOficina(randomIndex2, city1, this.repository);
		newState.setCosto(newState.calcularCostoTotal(repository));

		return newState;
    }

    private double acceptanceProbability(double actualEnergy, double newEnergy, double temperature){
        /*This is the Metropolis-Function*/ 


        //1. We check is the neighbor state IS BETTER THAN the actual state (in this case, the lower the better)
        if (newEnergy < actualEnergy){
            return 1.0;            
        }

        //2. If it isn't better, we may still accept it based on the metropolis function and T temperature. 
        return Math.exp((actualEnergy - newEnergy ) / temperature );
    }



    public SingleTour getBest(){
        return bestState;
    }

    public List<Oficina> getBestListaOficina(){
        return bestState.getciudadesPedido();
    }

    public  List<List<Tramo>> getBestTramosXOficina(){
        return bestState.getTramosARecorrerPorOficina();
    }

    public List<Tramo> getBestListaTramos(){
        List<Tramo> bestlista = new ArrayList<>();

        for (List<Tramo> tramos : bestState.getTramosARecorrerPorOficina() )
            bestlista.addAll(tramos);

        return bestlista;
    }

    public Double getBestCosto(){
        return bestState.getCosto();
    }
}
