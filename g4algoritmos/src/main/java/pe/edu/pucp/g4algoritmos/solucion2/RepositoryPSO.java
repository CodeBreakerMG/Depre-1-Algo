package pe.edu.pucp.g4algoritmos.solucion2;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;

import pe.edu.pucp.g4algoritmos.model.Camion;
import pe.edu.pucp.g4algoritmos.model.Oficina;


public class RepositoryPSO {
    
    private List<Oficina> oficinas; //LISTA DE OFICINAS NO INCLUYE ALMACEN
    private List<Camion> camiones;
    private List<Oficina> almacenes;

    private double tiempoSalida;
    private double num_dimensions;
    private int num_almacenes;
    

    public RepositoryPSO(List<Oficina> oficinas, double tiempoSalida) {
        this.oficinas = oficinas;
        this.tiempoSalida = tiempoSalida;

        this.almacenes = new ArrayList<>();
        this.almacenes.add(oficinas.get(0));
        
        this.num_almacenes = 1;
        this.num_dimensions = oficinas.size();
    }

    public RepositoryPSO(List<Oficina> oficinas, List<Camion> camiones, List<Oficina> almacenes, double tiempoSalida) {
        
        this.oficinas = oficinas;
        this.camiones = camiones;
        this.almacenes = almacenes;

        this.tiempoSalida = tiempoSalida;
        this.num_almacenes = almacenes.size();
        this.num_dimensions = oficinas.size();
    }

    public List<Oficina> getOficinas() {
        return oficinas;
    }

    public Oficina getOficina(int index){
        return oficinas.get(index);
    }

    public List<Camion> getCamiones() {
        return camiones;
    }
 
    public Camion getCamion(int index){
        return camiones.get(index);
    }

    public List<Oficina> getAlmacenes() {
        return almacenes;
    }
    
    public Oficina getAlmacen(int index){
        return almacenes.get(index);
    }


    public double getNumDimensions() {
        return num_dimensions;
    }
 
    public int getNumAlmacenes() {
        return num_almacenes;
    }

    public double getTiempoSalida() {
        return tiempoSalida;
    }
 
}
