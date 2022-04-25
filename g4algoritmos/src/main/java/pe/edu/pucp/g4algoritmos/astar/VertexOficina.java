package pe.edu.pucp.g4algoritmos.astar;

import java.util.List;
import java.util.ArrayList;

import pe.edu.pucp.g4algoritmos.model.Mapa;
import pe.edu.pucp.g4algoritmos.model.Oficina;
import pe.edu.pucp.g4algoritmos.model.Tramo;

public class VertexOficina {

    private Oficina oficina;
    private List<Tramo> listaTramos; //tramos desde aqui hasta alla.

    //parameters for AStar Search
    private double g; //Cost from start to here
    private double h; //Cost from here to target, heuristically calculated
    private double f;
    //track de adjacency list (neighbors)

    //it Tracks the previous node in the shortest path
    private VertexOficina parent;

    public VertexOficina(Oficina oficina) {
        this.oficina = oficina;
        this.listaTramos = obtenerTramos();
    }

    public List<Tramo> obtenerTramos(){

        List<Tramo> auxListaTramos = Mapa.getTramosByOficinaInicio(this.oficina.getCodigo());
        return auxListaTramos;
        
    }

    public Oficina getOficina() {
        return oficina;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public List<Tramo> getListaTramos() {
        return listaTramos;
    }

    public void setListaTramos(List<Tramo> listaTramos) {
        this.listaTramos = listaTramos;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public VertexOficina getParent() {
        return parent;
    }

    public void setParent(VertexOficina parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Node [name= " + this.oficina.getProvincia() + "]";
    }
    
}
