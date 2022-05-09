package pe.edu.pucp.g4algoritmos.solucion2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/*LS-VND, from PAPER*/

public class LS_VND {
    
    //List<Position> Position();

    public static List<Position> improveSolutions(List<Position> positions, int num_almacenes, RepositoryPSO repository){

        List<List<Position>> positionsByDepot = new ArrayList<>(); //DEPOT == almacen
        
        /*1. Grouping cities by Depot, using "Position" class variable */
        for (int i = 0; i < num_almacenes; i++){
            List<Position> listPositions = new ArrayList<>();
            for (Position p : positions){
                if ( i <= p.getDepot()  && p.getDepot() < i + 1){
                    listPositions.add(p);
                }
            }
            positionsByDepot.add(listPositions);
        }

        for (List<Position> pos : positionsByDepot)
            pos = LS_VND_Framework(pos, positionsByDepot.size(), repository);

        List<Position> newPositions = new ArrayList<>();
        
        for (List<Position> pos : positionsByDepot)
            newPositions.addAll(pos);

        return newPositions;

    }



    private static List<Position> LS_VND_Framework(List<Position> S,int num_almacenes, RepositoryPSO repository) {

        int k = 1;

        
        while (k <= 3){
            List<Position> Sprime = new ArrayList<>();
            
            if (S.size() == 0 ){
                return S;
            }

            if (k == 1) Sprime = Reinsertion(S);
            if (k == 2) Sprime = Exchange(S);
            if (k == 3) Sprime = Reverse(S);
            double SCost = ConstantesPSO.f(S, num_almacenes, repository);
            double SprimeCost = ConstantesPSO.f(Sprime, num_almacenes, repository);
            if (SprimeCost < SCost){
                S = new ArrayList<>();
                S.addAll(Sprime);
                k = 1;
            }
            else{
                k++;
            }
        }

        return S;
    }



    private static List<Position> Reinsertion(List<Position> positions){

        Random rand = new Random();
        int max = positions.size() - 1;
        int min = 0;

        int randomLocation = rand.nextInt((max - min) + 1) + min;
        int randomIndex = rand.nextInt((max - min) + 1) + min;

        Position pos = positions.get(randomLocation);
        positions.remove(pos);

        positions.add(randomIndex, pos);

        return positions;
    }   

    private static List<Position> Exchange(List<Position> positions){

        Random rand = new Random();
        int max = positions.size() - 1;
        int min = 0;

        int randomLocation1 = rand.nextInt((max - min) + 1) + min;
        int randomLocation2 = rand.nextInt((max - min) + 1) + min;

        Position pos1 = positions.get(randomLocation1);
        Position pos2 = positions.get(randomLocation2);
        

        positions.set(randomLocation2, pos1);
        positions.set(randomLocation1, pos2);

        return positions;

    }

    private static List<Position> Reverse(List<Position> positions){
        Collections.reverse(positions);
        return positions;
    }

    
}
