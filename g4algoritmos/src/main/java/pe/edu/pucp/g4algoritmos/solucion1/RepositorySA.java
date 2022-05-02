package pe.edu.pucp.g4algoritmos.solucion1;

import java.util.ArrayList;
import java.util.List;

import pe.edu.pucp.g4algoritmos.model.Oficina;

public class RepositorySA {
    
    private List<Oficina> cities;

    public RepositorySA(List<Oficina> cities) {
        this.cities = cities;
    }

    public void addOficina(Oficina Oficina) {
        cities.add(Oficina);
    }

    public Oficina getOficina(int index){
        return cities.get(index);
    }

    public int getNumberOfOficinas(){
        return cities.size();
    }
    
}
