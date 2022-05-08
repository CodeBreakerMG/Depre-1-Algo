package pe.edu.pucp.g4algoritmos.solucion2;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import javafx.scene.control.RadioMenuItem;
import pe.edu.pucp.g4algoritmos.model.Camion;
import pe.edu.pucp.g4algoritmos.model.Oficina;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

/*

CAMBIOS A REALIZAR: 

Particula PSO (arreglo de N ciudades) = 
	[
		ciudades,
		orden de entrga (double-> parte entera: almacen, parte decimal: orden),
		viajes o rutas (double con dos decimales -> entera: almacen, parte decimal: numero de camion(i.e. : 01, 02, 03 ...) )
	];


Consideraciones: 

**EN ESTE CONTEXTO, VIAJE O RUTA TIENE UN SOLO CAMION, POR LO QUE VIAJE = CAMION POR ALMACEN
**SE DEBE MINIMIZAR LOS CAMIONES A UTILIZAR, POR LO UE SE IMPLEMENTARA IOP PARA RESOLVERLO
**Se debería considerar utilizar un máximo de camiones por tipo.

Camion: debe ser random de 0 a 3 parte entera, y parte decimal solo de 00-num camiones del almacen

Tonces, para generalo, se debe hacer un foorloop por almacen


*/ 

public class HybridParticleSwarmOptimization {
    
    private List<Position> globalBestSolution;  //Best global solution 
    private Particle[] particleSwarm;           //Collection of particles that will compose the swarm
    private int epochs = 0;                         //To count how many iterations we are going to make
    private double globalBestCost;

    
    private RepositoryPSO repository;
    private int num_dimensions ;                //Numero de oficinas
    private int num_almacenes ;                 //Numero de almacenes
    private double min = 0.0;                   //min value (as parameter constant of PSO)

    public HybridParticleSwarmOptimization(List<OficinaPSO> oficinas, List<List<Camion>> camiones, List<Oficina> almacenes, Date tiempoSalida) {

        /*Repositorio*/   
        this.repository = new RepositoryPSO(oficinas, camiones, almacenes, tiempoSalida);
        
        /*Definición de las constantes del problema*/
        num_dimensions = oficinas.size();
        num_almacenes = almacenes.size();   //Se asume solo un almacen, por el momento:

        initializa();
    }

    public HybridParticleSwarmOptimization(List<OficinaPSO> oficinas, List<Oficina> almacenes, Date tiempoSalida) {

        /*Repositorio*/   
        this.repository = new RepositoryPSO(oficinas, almacenes, tiempoSalida);
        
        /*Definición de las constantes del problema*/
        num_dimensions = oficinas.size();
        num_almacenes = almacenes.size();   //Se asume solo un almacen, por el momento:
        
        initializa();

    }

    private void initializa(){

        this.globalBestSolution = new ArrayList<>(num_dimensions); 
        this.particleSwarm = new Particle[ConstantesPSO.NUM_PARTICLES]; 

        /*Generar primera partícula*/
        generateRandomSolution();
    }


    private void generateRandomSolution() {
        for (int i = 0; i < num_dimensions; ++i){
            double randomAlmacen = random(this.min, (double)num_almacenes); //before: randomPosition
            double randomCamion = exactRandom((int)randomAlmacen);
            Position pos = new Position(repository.getOficinaPSO(i), randomAlmacen, randomCamion);
            this.globalBestSolution.add(pos);
        }
    }



    public void solve(){

        //1. Initialize particles
        for (int i = 0; i < ConstantesPSO.NUM_PARTICLES; ++i){
            List<Double>  locations = initializeLocations();
            List<Double>  vehicles = initializeVehicles(locations);
            List<Double> velocities = initializeVelocities();

            this.particleSwarm[i] = new Particle(repository.getOficinasPSO(), locations, vehicles, velocities);
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

                    double newPosition = actualParticle.getPosition(i).getRandomPosition() + actualParticle.getVelocity(i);
                
                //Position can't be out of bound:
                    if (newPosition < min)
                        actualParticle.setPosition(i,min, exactRandom((int)min));

                    else if (newPosition >= (double)num_almacenes)
                        actualParticle.setPosition(i,(double)num_almacenes -  0.000000001, exactRandom(num_almacenes - 1));     

                    else{
                        double newVehicle = exactRandom((int)newPosition);
                        actualParticle.setPosition(i, newPosition, newVehicle);
                    }
                }


        //4. Check for best position for the particle
                if (fitnessFunction(actualParticle.getCurrentPositions()) < fitnessFunction(actualParticle.getBestPositions() ))  {
                    actualParticle.setBestPositions(actualParticle.getCurrentPositions());
                }


        //5. Check for best position for the particle
                double gBest = fitnessFunction(actualParticle.getBestPositions());
                if (gBest < fitnessFunction(this.globalBestSolution)){
                    setGlobalBestPositions(actualParticle.getBestPositions());
                    globalBestCost = gBest;
                    System.out.println("Best So far: " + gBest);
                }   
            }
            epochs++;
        }
    }


    private List<Double> initializeVehicles(List<Double> locations) {
        List<Double> newLocations = new ArrayList<>();

        for (int i = 0; i < num_dimensions ; i++){
            double randomAlmacen = locations.get(i);
            double loc = exactRandom((int) randomAlmacen);
            newLocations.add(loc);
        }

        return newLocations;
    }

    private List<Double> initializeLocations(){
        /**/
        List<Double> newLocations = new ArrayList<>();

        for (int i = 0; i < num_dimensions ; i++){
            double loc = random(this.min, (double) num_almacenes);
            newLocations.add(loc);
        }

        return newLocations;
    }

    private List<Double>  initializeVelocities(){

        List<Double> newVelocities = new ArrayList<>();

        for (int i = 0; i < num_dimensions ; i++){
            newVelocities.add(random(-( (double) num_almacenes- this.min),  (double) num_almacenes - this.min));
        }
 
        return newVelocities;
    }

    private double random(double min, double max) {
        return min + (max-min) * Math.random();
    }

    private double exactRandom(int randomAlmacen) {

        Random rand = new Random();
        int min = 0;
        int max = repository.cantidadCamionesPorAlmacen(randomAlmacen) - 1;
        double randomNum = (double) (rand.nextInt((max - min) + 1) + min);
        

        return randomAlmacen + (randomNum / 100);
    }

    public void setGlobalBestPositions(List<Position> positions) {
        
        globalBestSolution = new ArrayList<>();
        
        for (int i = 0; i < positions.size(); i++ ){
            globalBestSolution.add(positions.get(i));
        }
    }

    public double fitnessFunction(List<Position> positions){
        return ConstantesPSO.f(positions, num_dimensions, num_almacenes, repository.getTiempoSalida(), repository.getAlmacenes(), repository.getCamiones(), repository);
    }

    public void printOficinasXAlmacen(){

        int i,j;
        PriorityQueue<Position> queue = new PriorityQueue<>(new PositionComparator());
        List<List<Oficina>> oficinasPorAlmacen = new ArrayList<>();
        List<List<Double>> order = new ArrayList<>();
        
        //List<List<Double>> paquetesPorAlmacen = new ArrayList<>();
        
        for (Position p: globalBestSolution){
            queue.add(p);
        }

        for (i = 0; i < num_almacenes; i++){
            double pos = (double) i;
            List<Oficina> listOficinas = new ArrayList<>();
            List<Double> ordenlocal = new ArrayList<>();
            do{
                Position position = queue.peek();
                
                if (position == null)
                    break;
                pos = position.getRandomPosition();
                
                if (pos < (double)(i + 1)){
                    listOficinas.add(position.getOficina());
                    ordenlocal.add(pos);
                    queue.poll();
                }

            }while (pos < (double)(i + 1));
            
            oficinasPorAlmacen.add(listOficinas);
            order.add(ordenlocal);
        }


        i = 1;
        for (List<Oficina> lista : oficinasPorAlmacen){
            System.out.println("Lista almacen: " + i);
            j = 1;
            for (Oficina oficina : lista){
                System.out.println("\t" +  j + ". " + oficina.toString() + ". POS: " + order.get(i-1).get(j-1));
                j++;
            }
            i++;
        }
        /*
        for (Position position : globalBestSolution){
            System.out.println(position.getOficina().getProvincia() + ". POS: " + position.getRandomPosition());
          //  i++;
        }*/
        System.out.println("Global Besto Cost: " + globalBestCost);
    }
}

