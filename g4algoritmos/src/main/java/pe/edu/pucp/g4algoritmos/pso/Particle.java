package pe.edu.pucp.g4algoritmos.pso;


public class Particle {
    
    private double[] position; //  xi -> (x,y)
    private double[] velocity; //  vi -> (x,y)
    private double[] bestPosition; //  pi -> (x,y)


    public Particle(double[] position, double[] velocity) {

        this.position = new double[Constants.NUM_DIMENSIONS];
        this.velocity = new double[Constants.NUM_DIMENSIONS];
        this.bestPosition = new double[Constants.NUM_DIMENSIONS];

        System.arraycopy(velocity, 0, this.velocity, 0, velocity.length);
        System.arraycopy(position, 0, this.position, 0, position.length);
    
    }

    public void checkBestSolution(double[] globalBestSolution){
        //We are trying to find the MINIMUM, hence, the best has to be smaller
        if (Constants.f(this.bestPosition) < Constants.f(globalBestSolution))
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
        
        return "Best position so far: (" + this.bestPosition[0] + "," + this.bestPosition[0] + ")";
    }
}
