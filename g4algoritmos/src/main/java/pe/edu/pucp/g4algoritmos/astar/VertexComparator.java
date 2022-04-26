package pe.edu.pucp.g4algoritmos.astar;

import java.util.Comparator;

public class VertexComparator implements Comparator<VertexOficina> {

    //We will Compare the nodes according to their f(x) value
    @Override
    public int compare(VertexOficina o1, VertexOficina o2) {
        return Double.compare(o1.getF(), o2.getF());
    }
    
}