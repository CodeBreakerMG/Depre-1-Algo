package pe.edu.pucp.g4algoritmos.pso;


public class ParticleSwarmOptimization {
    
    private double [] globalBestSolutions;  //Best global solution 
    private Particle[] particleSwarm;       //Collection of particles that will compose the swarm
    private int epochs;                     //To count how many iterations we are going to make


    public ParticleSwarmOptimization() {
        this.globalBestSolutions = new double[Constants.NUM_DIMENSIONS]; 
        this.particleSwarm = new Particle[Constants.NUM_PARTICLES]; 
        generateRandomSolution();
    }

    private void generateRandomSolution(){
        
        for (int i = 0; i < Constants.NUM_DIMENSIONS; ++i){
            double randCoordinate = random(Constants.MIN_X, Constants.MAX_X);
            this.globalBestSolutions[i] = randCoordinate;
        }
    }

    public void solve() {

        //1. Initialize particles
        for (int i = 0; i < Constants.NUM_PARTICLES; ++i){
            double[] locations = initializeLocation();
            double[] velocities = initializeVelocity();

            this.particleSwarm[i] = new Particle(locations, velocities);
        }

        while ( epochs < Constants.MAX_ITERATIONS){
            
            for (Particle actualParticle : this.particleSwarm){
        //2. Updating the velocities of each particle until number of iterations is complete
                for (int i = 0; i < actualParticle.getVelocity().length; ++i ){
                    double rp = Math.random();
                    double rg = Math.random();

                    actualParticle.getVelocity()[i] = 
                        Constants.w*actualParticle.getVelocity()[i] + Constants.c1*rp*(actualParticle.getBestPosition()[i] - actualParticle.getPosition()[i]) 
                        + Constants.c2*rg*(  this.globalBestSolutions[i]  - actualParticle.getPosition()[i]) ;
                }
        //3. Updating the positions of each particle until number of iterations is complete
                
                for (int i = 0; i < actualParticle.getPosition().length; ++i ){
                    
                    actualParticle.getPosition()[i] += actualParticle.getVelocity()[i];

                //Position can't be out of bound:
                if (actualParticle.getPosition()[i] < Constants.MIN_X)
                    actualParticle.getPosition()[i] = Constants.MIN_X;

                else if (actualParticle.getPosition()[i] > Constants.MAX_X)
                    actualParticle.getPosition()[i] = Constants.MAX_X;       
                }

        //4. Check for best position for the particle
                if (Constants.f(actualParticle.getPosition()) < Constants.f(actualParticle.getBestPosition() ))  {
                    actualParticle.setBestPosition(actualParticle.getPosition());
                }
        //4. Check for best position for the particle
                if (Constants.f(actualParticle.getBestPosition()) < Constants.f(this.globalBestSolutions)){
                     System.arraycopy(actualParticle.getBestPosition(),0, globalBestSolutions, 0, actualParticle.getBestPosition().length);
                }   
            }
            epochs++;
        }
    }

    private double[] initializeLocation(){
        /**/ 
        double x = random(Constants.MIN_X, Constants.MAX_X);
        double y = random(Constants.MIN_Y, Constants.MAX_Y);

        double[] newLocation = new double[] {x,y};
        return newLocation;
    }

    private double[] initializeVelocity(){
        /**/ 
        double vx = random(-(Constants.MAX_X - Constants.MIN_X), Constants.MAX_X - Constants.MIN_X);
        double vy = random(-(Constants.MAX_Y - Constants.MIN_Y), Constants.MAX_Y - Constants.MIN_Y);

        double[] newVelocity = new double[] {vx,vy};
        return newVelocity;
    }

    private double random(double min, double max){
        return min + (max-min) * Math.random();
    }

    public void showSolution(){
        System.out.println("Solution for PSO Problem");
        System.out.println("Best solution x: " + this.globalBestSolutions[0] + ". Best solution y: " + this.globalBestSolutions[1]);
        System.out.println("Value f(x,y) = " + Constants.f(globalBestSolutions));

    }
}
