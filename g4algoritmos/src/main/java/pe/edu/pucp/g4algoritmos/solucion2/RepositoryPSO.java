package pe.edu.pucp.g4algoritmos.solucion2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.edu.pucp.g4algoritmos.model.Camion;
import pe.edu.pucp.g4algoritmos.model.Oficina;


public class RepositoryPSO {
    
    private List<OficinaPSO> oficinas; //LISTA DE OFICINAS NO INCLUYE ALMACEN
    private List<List<Camion>> camionesPorAlmacen;
    private List<Oficina> almacenes;

    private Date tiempoSalida;
    private double num_dimensions;
    
    private int[][] cantidadTipoCamionPorAlmacen = new int[10][3]; //A: 0, b: 1 , C:2

    public RepositoryPSO(List<OficinaPSO> oficinas, List<Oficina> almacenes, Date tiempoSalida) {
        
        this.oficinas = oficinas;
        this.almacenes = almacenes;

        this.tiempoSalida = tiempoSalida;
        
        this.num_dimensions = oficinas.size();
    }

    public RepositoryPSO(List<OficinaPSO> oficinas, List<List<Camion>> camionesPorAlmacen, List<Oficina> almacenes, Date tiempoSalida) {
        
        this.oficinas = oficinas;
        this.almacenes = almacenes;
        
        this.camionesPorAlmacen = new ArrayList<>();
        this.camionesPorAlmacen.addAll(camionesPorAlmacen);
        
        
        for (int i = 0; i < almacenes.size(); i++){
            cantidadTipoCamionPorAlmacen[i] = calcularTipoCamionesPorAlmacen(i);
        }

        this.tiempoSalida = tiempoSalida;
        
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

    public List<List<Camion>> getCamiones(){
        return this.camionesPorAlmacen;
    }

    public List<Camion> getCamionesPorAlmacen(int index) {
        if (index >= almacenes.size())
            return null;

        return camionesPorAlmacen.get(index);
    }
 
    public Camion getCamion(int indexAlmacen, int indexEnAlmacen){
        return camionesPorAlmacen.get(indexAlmacen).get(indexEnAlmacen);
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
        return almacenes.size();
    }

    public Date getTiempoSalida() {
        return tiempoSalida;
    }

        
    public int[][] getCantidadTipoCamionPorAlmacen() {
        return cantidadTipoCamionPorAlmacen;
    }

    public int cantidadCamionesPorAlmacen(int indexAlmacen){

        int returninValue = 0;
        try{
            returninValue = cantidadTipoCamionPorAlmacen[indexAlmacen][0] +
            cantidadTipoCamionPorAlmacen[indexAlmacen][1] +
            cantidadTipoCamionPorAlmacen[indexAlmacen][2] ;
        
        }catch (Exception ex){
            System.out.println(ex);
            System.out.println(indexAlmacen);
        }
        
        return returninValue;
    }

 
    public int cantCamionesTipoA(int indexAlmacen){
        return cantidadTipoCamionPorAlmacen[indexAlmacen][0];
    }
    public int cantCamionesTipoB(int indexAlmacen){
        return cantidadTipoCamionPorAlmacen[indexAlmacen][1];
    }
    public int cantCamionesTipoC(int indexAlmacen){
        return cantidadTipoCamionPorAlmacen[indexAlmacen][2];
    }

    public int[] calcularTipoCamionesPorAlmacen(int indexAlmacen){

        int[] counter = {0,0,0}; //0: A, 1: B, 
        List<Camion> list = camionesPorAlmacen.get(indexAlmacen);

        for (Camion c : list){
            if (c.tipo() == 'A' && counter[0] < ConstantesPSO.MAX_NUM_CAMIONES)
                    counter[0]++; 
            else if (c.tipo() == 'B' && counter[1] < ConstantesPSO.MAX_NUM_CAMIONES)
                counter[1]++; 
            else if (counter[2] < ConstantesPSO.MAX_NUM_CAMIONES)
                counter[2]++; 
        }
 
        return counter;
    }

}
