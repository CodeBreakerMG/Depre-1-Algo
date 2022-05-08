package pe.edu.pucp.g4algoritmos.solucion2;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.edu.pucp.g4algoritmos.model.Camion;
import pe.edu.pucp.g4algoritmos.model.Oficina;


public class RepositoryPSO {
    
    private List<OficinaPSO> oficinas; //LISTA DE OFICINAS NO INCLUYE ALMACEN
    private List<Camion> camiones;
    private List<Oficina> almacenes;

    private Date tiempoSalida;
    private double num_dimensions;
    private int num_almacenes;
    

    public RepositoryPSO(List<OficinaPSO> oficinas, List<Oficina> almacenes, Date tiempoSalida) {
        
        this.oficinas = oficinas;
        this.almacenes = almacenes;

        this.tiempoSalida = tiempoSalida;
        this.num_almacenes = almacenes.size();
        this.num_dimensions = oficinas.size();
    }

    public RepositoryPSO(List<OficinaPSO> oficinas, List<Camion> camiones, List<Oficina> almacenes, Date tiempoSalida) {
        
        this.oficinas = oficinas;
        this.camiones = camiones;
        this.almacenes = almacenes;

        this.tiempoSalida = tiempoSalida;
        this.num_almacenes = almacenes.size();
        this.num_dimensions = oficinas.size();
    }

    public List<Oficina> getOficinas() {
        List<Oficina> list = new ArrayList<>();

        for (OficinaPSO o : this.oficinas)
            list.add(o.getOficina());
        return list;
    }

    public Oficina getOficina(int index){
        return oficinas.get(index).getOficina();
    }

    public List<OficinaPSO> getOficinasPSO() {
        
        return this.oficinas;
    }

    public OficinaPSO getOficinaPSO(int index){
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

    public Date getTiempoSalida() {
        return tiempoSalida;
    }
 
}
