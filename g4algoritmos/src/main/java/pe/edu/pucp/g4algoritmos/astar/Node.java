package pe.edu.pucp.g4algoritmos.astar;

import java.util.ArrayList;
import java.util.List;

public class Node {
    
    private String name;
    private double x;
    private double y;
    
    //parameters for AStar Search
    private double g; //Cost from start to here
    private double h; //Cost from here to target, heuristically calculated
    private double f;
    //track de adjacency list (neighbors)
    private List<Edge> adjacencyList;

    //it Tracks the previous node in the shortest path
    private Node parent;

    public Node(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.adjacencyList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public void setF(double f){
        this.f = f;
    }
    
    public double getF() {
        return f;
    }

    public double calculateF(){
        f = g + h;
        return f;
    }

    public List<Edge> getAdjacencyList() {
        return adjacencyList;
    }

    public void addNeighbor(Edge edge) {
        this.adjacencyList.add(edge);
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Node [name= " + name + "]";
    }



}

