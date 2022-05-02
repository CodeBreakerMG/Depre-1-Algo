package pe.edu.pucp.g4algoritmos.solucion1;


import java.util.List;

import org.javatuples.Triplet;

import pe.edu.pucp.g4algoritmos.model.Oficina;

public class RepositorySA {
    
    private List<Oficina> cities;
    private List<Triplet<String, Long, Integer>> listaTiempos ;
    private double tiempoSalida;

    public RepositorySA(List<Oficina> cities,  List<Triplet<String, Long, Integer>> listaTiempos, long tiempoSalida) {
        this.cities = cities;
        this.listaTiempos = listaTiempos;
        this.tiempoSalida = ConstantesSA.MILI_HORAS *  tiempoSalida;
    }

    public Oficina getOficina(int index){
        return cities.get(index);
    }  

    public double getTiempoSalida(){
        return tiempoSalida;
    }
    
    

    public List<Triplet<String, Long, Integer>> getListaTiempos() {
        return listaTiempos;
    }

    public void setListaTiempos(List<Triplet<String, Long, Integer>> listaTiempos) {
        this.listaTiempos = listaTiempos;
    }

    public int getNumberOfOficinas(){
        return cities.size();
    }

    public double getTiempoMaximoOficina(Oficina oficina){
        /*Devuelve el tiempo promedio de los pedidos de la oficina*/
        return listaTiempos.get(cities.indexOf(oficina)).getValue1() * ConstantesSA.MILI_HORAS;
    }

    public int getNumeroPedidosOficina(Oficina oficina){
        /*Devuelve el tiempo promedio de los pedidos de la oficina*/
        return listaTiempos.get(cities.indexOf(oficina)).getValue2();
    }
    
}