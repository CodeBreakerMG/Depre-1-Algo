package pe.edu.pucp.g4algoritmos.astar;


import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {

    //We will Compare the nodes according to their f(x) value
    @Override
    public int compare(Node o1, Node o2) {
        return Double.compare(o1.getF(), o2.getF());
    }
    
}
