package pe.edu.pucp.g4algoritmos.astar;

import java.util.Comparator;

import pe.edu.pucp.g4algoritmos.model.Oficina;

public class OficinaComparator implements Comparator<Oficina> {

    //We will Compare the nodes according to their f(x) value
    @Override
    public int compare(Oficina o1, Oficina o2) {
        return Double.compare(o1.getF(), o2.getF());
    }
}
