package pe.edu.pucp.g4algoritmos.solucion1;


public class SimulatedAnnealing {

    private SingleTour actualState;
    private SingleTour nextState;
    private SingleTour bestState;
    
    public void simulate() {

        double temp = ConstantesSA.MAX_TEMPERATURE;

        actualState = new SingleTour();
        actualState.generateIndividual();
        bestState = new SingleTour(actualState.getTour());

        System.out.println("Initial Solution Distance: " + actualState.getDistance());

        while ( temp > ConstantesSA.MIN_TEMPERATURE ) {

            nextState = generateNeighborState(actualState);
            double currentEnergy = actualState.getDistance();
            double neighborEnergy = nextState.getDistance();

            if (acceptanceProbability(currentEnergy, neighborEnergy, temp) > Math.random())
                actualState = new SingleTour(nextState.getTour());

            if (actualState.getDistance() < bestState.getDistance())
                bestState = new SingleTour(actualState.getTour());

            temp *= (1 - ConstantesSA.COOLING_RATE);
        }
    }

    private SingleTour generateNeighborState(SingleTour actualState) {
		
		SingleTour newState = new SingleTour(actualState.getTour());
		
		int randomIndex1 = (int) (Math.random()*newState.getTourSize());
		int randomIndex2 = (int) (Math.random()*newState.getTourSize());
		
		City city1 = newState.getCity(randomIndex1);
		City city2 = newState.getCity(randomIndex2);
		
		newState.setCity(randomIndex1, city2);
		newState.setCity(randomIndex2, city1);
		
		return newState;
    }

    private double acceptanceProbability(double actualEnergy, double newEnergy, double temperature){
        /*This is the Metropolis-Function*/ 

        //1. We check is the neighbor state IS BETTER THAN the actual state (in this case, the lower the better)
        if (newEnergy < actualEnergy){
            return 1.0;            
        }

        //2. If it isn't better, we may still accept it based on the metropolis function and T temperature. 
        return Math.exp( (actualEnergy - newEnergy ) / temperature );
    }

    public SingleTour getBest(){
        return bestState;
    }

}
