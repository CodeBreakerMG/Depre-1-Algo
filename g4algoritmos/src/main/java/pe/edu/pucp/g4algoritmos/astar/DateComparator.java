package pe.edu.pucp.g4algoritmos.astar;
import pe.edu.pucp.g4algoritmos.model.Pedido;
import java.util.Comparator;

public class DateComparator implements Comparator<Pedido>{
    @Override
    public int compare(Pedido a, Pedido b) {
        return a.getFechaHoraLimite().compareTo(b.getFechaHoraLimite());
    }
}