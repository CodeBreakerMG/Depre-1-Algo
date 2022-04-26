package pe.edu.pucp.g4algoritmos.astar;

import java.util.ArrayList;
import java.util.List;

import pe.edu.pucp.g4algoritmos.model.Oficina;
import pe.edu.pucp.g4algoritmos.model.Tramo;

import pe.edu.pucp.g4algoritmos.model.Mapa;

public class GrafoAStar {
  
    public List<VertexOficina> listaOficinas = new ArrayList<>();
    public List<Arista> listaTramos = new ArrayList<>();
    

    public GrafoAStar(){
        
        for (Oficina oficina : Mapa.listaOficinas){
            VertexOficina vertice = new VertexOficina(oficina);
            listaOficinas.add(vertice);
        }
        

        for (Tramo tramo : Mapa.listaTramos){
            Arista arista = new Arista(tramo, getVertexByCodigoOficina(tramo.getCiudadFin().getCodigo()));
            listaTramos.add(arista);
        }
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

    
    
}
