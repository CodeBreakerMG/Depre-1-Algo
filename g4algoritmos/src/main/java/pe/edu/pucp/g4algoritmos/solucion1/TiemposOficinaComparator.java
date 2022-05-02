package pe.edu.pucp.g4algoritmos.solucion1;


import java.util.Comparator;

import org.javatuples.Triplet;

public class TiemposOficinaComparator implements Comparator<Triplet<String, Long, Integer>> {
        //We will Compare the nodes according to their f(x) value
        @Override
        public int compare(Triplet<String, Long, Integer> o1,Triplet<String, Long, Integer> o2) {
            return Long.compare(o1.getValue1(), o2.getValue1());
        }
        
}



 