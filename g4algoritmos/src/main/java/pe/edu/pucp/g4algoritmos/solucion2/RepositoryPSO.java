package pe.edu.pucp.g4algoritmos.solucion2;
import org.javatuples.Triplet;
import java.util.List;

import pe.edu.pucp.g4algoritmos.model.Camion;
import pe.edu.pucp.g4algoritmos.model.Oficina;


public class RepositoryPSO {
    
    private List<Oficina> oficinas;
    private double tiempoSalida;
    private List<Camion> camiones;
    private List<Oficina> almacenes;

    public RepositoryPSO(List<Oficina> oficinas, double tiempoSalida) {
        this.oficinas = oficinas;
        this.tiempoSalida = tiempoSalida;
    }

    public RepositoryPSO(List<Oficina> oficinas, double tiempoSalida, List<Camion> camiones, List<Oficina> almacenes) {
        this.oficinas = oficinas;
        this.tiempoSalida = tiempoSalida;
        this.camiones = camiones;
        this.almacenes = almacenes;
    }

    public List<Oficina> getOficinas() {
        return oficinas;
    }

    public double getTiempoSalida() {
        return tiempoSalida;
    }

    public List<Camion> getCamiones() {
        return camiones;
    }

    public void setCamiones(List<Camion> camiones) {
        this.camiones = camiones;
    }

    public List<Oficina> getAlmacenes() {
        return almacenes;
    }

    public void setAlmacenes(List<Oficina> almacenes) {
        this.almacenes = almacenes;
    }

}
