package pe.edu.pucp.g4algoritmos.astar;

import java.util.ArrayList;
import java.util.List;

import pe.edu.pucp.g4algoritmos.model.Oficina;
import pe.edu.pucp.g4algoritmos.model.Tramo;

import pe.edu.pucp.g4algoritmos.model.Mapa;

public class GrafoAStar {
  
    private List<VertexOficina> listaOficinas;
    private List<Arista> listaTramos ;
    

    public GrafoAStar(){
        
        listaOficinas = new ArrayList<>();
        listaTramos = new ArrayList<>();
    }

    public GrafoAStar(int mode) {
        //Modo 1: Obtener La lista de todas las oficinas en el mapa
        if (mode == 1) {
            for (Oficina oficina : Mapa.listaOficinas){
                VertexOficina vertice = new VertexOficina(oficina);
                listaOficinas.add(vertice);
            }
            
    
            for (Tramo tramo : Mapa.listaTramos){
                Arista arista = new Arista(tramo, getVertexByCodigoOficina(tramo.getCiudadFin().getCodigo()));
                listaTramos.add(arista);
            }

        }

        //Modo 2: Crear desde 0 y asignar una lista de oficinas luego.
        else{
            listaOficinas = new ArrayList<>();
           listaTramos = new ArrayList<>();
        }
        //this.listaOficinas = listaOficinas;
        
    }

    public GrafoAStar(List<VertexOficina> listaOficinas) {
        this.listaOficinas = listaOficinas;
        
    }

    public GrafoAStar(List<VertexOficina> listaOficinas, List<Arista> listaTramos) {
        this.listaOficinas = listaOficinas;
        this.listaTramos = listaTramos;
    }

    public VertexOficina getVertexByCodigoOficina(String codigoOficina){

        for (VertexOficina vertex : listaOficinas){
            if (vertex.getOficina().getCodigo() == codigoOficina)
                return vertex;
        }

        return null;
    }

    public List<VertexOficina> getListaOficinas() {
        return listaOficinas;
    }

    public void setListaOficinas(List<VertexOficina> listaOficinas) {
        this.listaOficinas = listaOficinas;
    }

    public List<Arista> getListaTramos() {
        return listaTramos;
    }

    public void setListaTramos(List<Arista> listaTramos) {
        this.listaTramos = listaTramos;
    }

    public void addVertex(VertexOficina vertexOficina){
        this.listaOficinas.add(vertexOficina);
    }
    
    
}
