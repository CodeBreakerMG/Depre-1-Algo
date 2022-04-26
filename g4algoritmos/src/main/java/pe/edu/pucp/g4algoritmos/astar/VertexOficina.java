package pe.edu.pucp.g4algoritmos.astar;

import java.util.List;
import java.util.ArrayList;

import pe.edu.pucp.g4algoritmos.model.Mapa;
import pe.edu.pucp.g4algoritmos.model.Oficina;
import pe.edu.pucp.g4algoritmos.model.Tramo;

public class VertexOficina {

    private Oficina oficina;
    private List<Arista> listaAristas; //Aristas desde aqui hasta alla.

    //parameters for AStar Search
    private double g; //Cost from start to here
    private double h; //Cost from here to target, heuristically calculated
    private double f;
    //track de adjacency list (neighbors)

    //it Tracks the previous node in the shortest path
    private VertexOficina parent;

    public VertexOficina(Oficina oficina, GrafoAStar grafoAStar) {
        this.oficina = oficina;
        this.listaAristas = obtenerAristas(grafoAStar);
    }

    public VertexOficina(Oficina oficina) {
        this.oficina = oficina;
        this.listaAristas  = new ArrayList<>();
    }


    public List<Arista> obtenerAristas(GrafoAStar grafoAStar){

        List<Arista> auxListaAristas = new ArrayList<>();
        List<Tramo> listaTramos = Mapa.getTramosByOficinaInicio(this.oficina.getCodigo());

        for (int i = 0; i < listaTramos.size(); i++){
            //Se obtiene la arista con la lista de tramos y el nodo final.
            Arista arista = new Arista(listaTramos.get(i), grafoAStar.getVertexByCodigoOficina(oficina.getCodigo()));
            auxListaAristas.add(arista);
        }

        return auxListaAristas;
        
    }

    public Oficina getOficina() {
        return oficina;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public List<Arista> getListaAristas() {
        return listaAristas;
    }

    public void setListaAristas(List<Arista> listaAristas) {
        this.listaAristas = listaAristas;
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
