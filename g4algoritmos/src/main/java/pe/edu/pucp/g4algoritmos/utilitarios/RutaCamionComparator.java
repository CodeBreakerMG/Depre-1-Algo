package pe.edu.pucp.g4algoritmos.utilitarios;
import java.util.Comparator;

import pe.edu.pucp.g4algoritmos.model.Ruta;

public class RutaCamionComparator implements Comparator<Ruta> {
    @Override
    public int compare(Ruta r1, Ruta r2) {
        String camion1 = r1.getCamion().getCodigo();
        String camion2 = r2.getCamion().getCodigo();
        return camion1.compareTo(camion2);
    }
}

/*
 * 
import java.util.Comparator;

public class VertexComparator implements Comparator<VertexOficina> {

    //We will Compare the nodes according to their f(x) value
    @Override
    public int compare(VertexOficina o1, VertexOficina o2) {
        return Double.compare(o1.getF(), o2.getF());
    }
    
}
 * 
 */