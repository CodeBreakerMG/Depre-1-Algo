package pe.edu.pucp.g4algoritmos.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class AStarAlgorithm {
    
    private Node source; //Starting Vertex
    private Node destination; //Destination Vertex;
    private Set<Node> explored; //Set of explored nodes.
    private PriorityQueue<Node> queue; //A queue that enqueues according to values.
   
    public AStarAlgorithm(Node source, Node destination) {
        this.source = source;
        this.destination = destination;
        this.explored = new HashSet<>();
        this.queue = new PriorityQueue<>(new NodeComparator()); //Necessary when using java priority queues, needs to compare with somethin
    }

    public void run() {
        
        queue.add(source);
        while(!queue.isEmpty()) {

            //We always get the node with the lowest f(x) value possile
            Node current = queue.poll(); 
            explored.add(current);

            //We have found the destination node
            if (current == destination)
                break;

            //Looping through adjacent nodes.
            for (Edge edge: current.getAdjacencyList()){
                Node child = edge.getTarget();
                double cost = edge.getWeight();
                double tempG = current.getG() + cost;
                double tempF = tempG + heuristicEucledian(current, destination);

                //if we haven't considered the child and f(x) is higher:
                if(explored.contains(child) && tempF >= child.getF())
                    continue;
                
                //Else, if we have not visited the child OR the f(x) score is lower
                else if(!queue.contains(child) || tempF < child.getF()) {
                    
                    // Tracking the shortest path (predecessor)
                    child.setParent(current);
                    child.setG(tempG);
                    child.setF(tempF);

                    //If the child has already been inserted, it must be updated
                    if (queue.contains(child))
                        queue.remove(child);
                    queue.add(child);
                }
            }
        }
    }

    private double heuristicEucledian(Node node1, Node node2) {
        //Approximated distance between 2 nodes.
        //Euclidean Method.

        return Math.sqrt(
                Math.pow(node1.getX() - node2.getX(),2) + 
                Math.pow(node1.getY() - node2.getY(),2) 
        );      
    }
    
    public void printSolutionPath(){
        List<Node> path = new ArrayList<>();
        for (Node node = destination; node != null; node = node.getParent()){
            path.add(node);
        }

        Collections.reverse(path);
        System.out.print(path);
    }

}

