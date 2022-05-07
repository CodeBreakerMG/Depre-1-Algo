package pe.edu.pucp.g4algoritmos.solucion2;

import java.util.Comparator;

public class PositionComparator implements Comparator<Position>{

    @Override
    public int compare(Position o1, Position o2) {
        
        return Double.compare(o1.getRandomPosition(), o2.getRandomPosition());
    }
    
}
