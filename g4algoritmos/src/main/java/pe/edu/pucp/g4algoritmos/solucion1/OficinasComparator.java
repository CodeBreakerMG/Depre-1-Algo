package pe.edu.pucp.g4algoritmos.solucion1;

import java.util.Comparator;
import java.util.List;

import org.javatuples.Triplet;

import pe.edu.pucp.g4algoritmos.model.Oficina;

public class OficinasComparator implements Comparator<Oficina> {


    private final List<Triplet<String, Long, Integer>> orderedList;
    private boolean appendFirst;

    public OficinasComparator(List<Triplet<String, Long, Integer>> orderedList, boolean appendFirst) {
        this.orderedList = orderedList;
        this.appendFirst = appendFirst;
    }

    @Override
    public int compare(Oficina o1, Oficina o2) {

        
        int pos1 = -1;
        int pos2 = -1;

        for (int i = 0; i < orderedList.size(); i++){
            if(orderedList.get(i).getValue0() == o1.getCodigo() && pos1 == -1){
                pos1 = i;
                continue;
            }
            if(orderedList.get(i).getValue0() == o2.getCodigo() && pos2 == -1){
                pos2 = i;
                continue;
            }
            if (pos1 != -1 && pos2 != -1)
                break;
        }

        if (pos1 != -1 && pos2 != -1)
            return pos1 - pos2;
        else if (pos1 != -1)
            return (appendFirst) ? 1 : -1;
        else if (pos2 != -1)
            return (appendFirst) ? -1 : 1;
        return 0;
    }
    
}


/*
2

import java.util.Comparator;
import java.util.List;

public class ListComparator implements Comparator<String> {

    private final List<String> orderedList;
    private boolean appendFirst;

    public ListComparator(List<String> orderedList, boolean appendFirst) {
        this.orderedList = orderedList;
        this.appendFirst = appendFirst;
    }

    @Override
    public int compare(String o1, String o2) {
        if (orderedList.contains(o1) && orderedList.contains(o2))
            return orderedList.indexOf(o1) - orderedList.indexOf(o2);
        else if (orderedList.contains(o1))
            return (appendFirst) ? 1 : -1;
        else if (orderedList.contains(o2))
            return (appendFirst) ? -1 : 1;
        return 0;
    }
}


*/