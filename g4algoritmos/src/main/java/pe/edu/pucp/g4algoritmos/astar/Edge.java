package pe.edu.pucp.g4algoritmos.astar;


public class Edge {
    private double weight;
    private Node target;

    public Edge(Node target, double weight) {
        this.weight = weight;
        this.target = target;
    }

    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public Node getTarget() {
        return target;
    }
    public void setTarget(Node target) {
        this.target = target;
    }
    
}
