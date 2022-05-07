package pe.edu.pucp.g4algoritmos.solucion2;

import java.util.List;

import pe.edu.pucp.g4algoritmos.model.Camion;
import pe.edu.pucp.g4algoritmos.model.Oficina;

import java.util.ArrayList;

public class HybridParticleSwarmOptimization {
    
    private List<Position> globalBestSolution;  //Best global solution 
    private Particle[] particleSwarm;           //Collection of particles that will compose the swarm
    private int epochs;                         //To count how many iterations we are going to make

    
    private RepositoryPSO repository;
    private int num_dimensions ;                //Numero de oficinas
    private int num_almacenes ;                 //Numero de almacenes
    private double min = 0.0;

    public HybridParticleSwarmOptimization(List<Oficina> oficinas, List<Camion> camiones, List<Oficina> almacenes, double tiempoSalida) {

        /*Repositorio*/   
        this.repository = new RepositoryPSO(oficinas, camiones, almacenes, tiempoSalida);
        
        /*Definición de las constantes del problema*/
        num_dimensions = oficinas.size();
        num_almacenes = almacenes.size();   //Se asume solo un almacen, por el momento:
        this.min = 0.0;

        this.globalBestSolution = new ArrayList<>(num_dimensions); 
        this.particleSwarm = new Particle[ConstantesPSO.NUM_PARTICLES]; 

        /*Generar primera partícula*/
        generateRandomSolution();
    }


    private void generateRandomSolution() {
        for (int i = 0; i < num_dimensions; ++i){
            double randCoordinate = random(this.min, (double)num_almacenes);
            Position pos = new Position(repository.getOficina(i), randCoordinate);
            this.globalBestSolution.set(i, pos);
        }
    }

    public void solve(){

        //1. Initialize particles
        for (int i = 0; i < ConstantesPSO.NUM_PARTICLES; ++i){
            List<Double>  locations = initializeLocations();
            List<Double> velocities = initializeVelocities();

            this.particleSwarm[i] = new Particle(repository.getOficinas(), locations, velocities);
        }

        while ( epochs < ConstantesPSO.MAX_ITERATIONS){

            for (Particle actualParticle : this.particleSwarm){

        //2. Updating the velocities of each particle until number of iterations is complete
                for (int i = 0; i < actualParticle.getVelocities().size(); ++i ){
                    double rp = Math.random();
                    double rg = Math.random();

                    double speed =  ConstantesPSO.w*actualParticle.getVelocity(i) 
                                    + ConstantesPSO.c1*rp*(actualParticle.getBestPosition(i).getRandomPosition() - actualParticle.getPosition(i).getRandomPosition())
                                    + ConstantesPSO.c2*rg*(globalBestSolution.get(i).getRandomPosition() - actualParticle.getPosition(i).getRandomPosition());

                    actualParticle.setVelocity(i, speed);

                }

        //3. Updating the positions of each particle until number of iterations is complete
                for (int i = 0; i < actualParticle.getCurrentPositions().size(); ++i ){
                    actualParticle.setPosition(i,   actualParticle.getPosition(i).getRandomPosition() + actualParticle.getVelocity(i));
                
                //Position can't be out of bound:
                    if (actualParticle.getPosition(i).getRandomPosition() < min)
                        actualParticle.setPosition(i,min);

                    else if (actualParticle.getPosition(i).getRandomPosition() > (double)num_almacenes)
                        actualParticle.setPosition(i,(double)num_almacenes);     
                }


        //4. Check for best position for the particle
        if (fitnessFunction(actualParticle.getCurrentPositions()) < fitnessFunction(actualParticle.getBestPositions() ))  {
            actualParticle.setBestPositions(actualParticle.getCurrentPositions());
        }
        //5. Check for best position for the particle
        if (fitnessFunction(actualParticle.getBestPositions()) < fitnessFunction(this.globalBestSolution)){
            setGlobalBestPositions(actualParticle.getBestPositions());
        }   


            }

            epochs++;
        }
    }

    



    private List<Double> initializeLocations(){
        /**/
        List<Double> newLocations = new ArrayList<>();

        for (int i = 0; i < num_dimensions ; i++){
            newLocations.add(random(this.min, (double) num_almacenes));
        }

        return newLocations;
    }

    private List<Double>  initializeVelocities(){

        List<Double> newVelocities = new ArrayList<>();

        for (int i = 0; i < num_dimensions ; i++){
            random(-( (double) num_almacenes- this.min),  (double) num_almacenes - this.min);
        }
 
        return newVelocities;
    }

    private double random(double min, double max) {
        return min + (max-min) * Math.random();
    }


    public void setGlobalBestPositions(List<Position> positions) {
        
        globalBestSolution = new ArrayList<>();
        
        for (int i = 0; i < positions.size(); i++ ){
            globalBestSolution.add(positions.get(i));
        }
    }

    public double fitnessFunction(List<Position> positions){
        return ConstantesPSO.f(positions, num_dimensions, num_almacenes);
    }
}

/*

List<Oficina> oficinas,  List<Triplet<String, Long, Integer>> listaTiempos, long tiempoInicio ){
        
        this.repository = new RepositorySA(oficinas, listaTiempos, tiempoInicio);
*/