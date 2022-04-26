package pe.edu.pucp.g4algoritmos.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import pe.edu.pucp.g4algoritmos.model.Mapa;
import pe.edu.pucp.g4algoritmos.model.Tramo;

public class AStarModified {

    private VertexOficina source; //Starting Vertex
    private VertexOficina destination; //Destination Vertex;
    private Set<VertexOficina> explored; //Set of explored VertexOficinas.
    private PriorityQueue<VertexOficina> queue; //A queue that enqueues according to values.
    private GrafoAStar grafo = new GrafoAStar();
   
    public AStarModified(VertexOficina source, VertexOficina destination) {
        this.source = source;
        this.destination = destination;
        this.explored = new HashSet<>();
        this.queue = new PriorityQueue<>(new VertexComparator()); //Necessary when using java priority queues, needs to compare with somethin
    }

    public void run() {
        
        queue.add(source);
        while(!queue.isEmpty()) {

            //We always get the VertexOficina with the lowest f(x) value possile
            VertexOficina current = queue.poll(); 
            explored.add(current);

            //We have found the destination VertexOficina
            if (current == destination)
                break;

            //Looping through adjacent VertexOficinas.
            for (Arista arista: current.getListaAristas()){
                
                //Aqui verificar si el tramo/arista esta bloqueado
                if (arista.getTramo().estaBloqueado())
                    continue;

                VertexOficina child = arista.getTarget();
                
                double cost = arista.getCosto(); //Este es el costo calculado por el tramo. 
                double tempG = current.getG() + cost;
                double tempF = tempG + calcularH(current, destination);

                //if we haven't considered the child and f(x) is higher:
                if(explored.contains(child) && tempF >= child.getF())
                    continue;
                
                //Else, if we have not visited the child OR the f(x) score is lower
                else if(!queue.contains(child) || tempF < child.getF()) {
                    
                    // Tracking the shortest path (predecessor)
                    child.setParent(current);
                    child.setG(tempG);
                    child.setF(tempF);

                    //If the child has already been inserted, it must be updated
                    if (queue.contains(child))
                        queue.remove(child);
                    queue.add(child);
                }
            }
        }
    }

    private double calcularH(VertexOficina current, VertexOficina destination){
        
        double costo = heuristicEucledian(current, destination);
        costo = (costo / Mapa.velocidadCamiones) ;
        return costo;

        //El costo del tramo sería: peso tiempo + destinos de los pedidos
        /*+

        SOLUCION:
            -> Primero sería tener un algoritmo padre que llame al a*.

            Este algoritmo padre primero distribuirá los pedidos que tiene en la flota de camiones que tiene,
                -> por lo que, tendría que asignar los pedidos cuyas coordenadas sean las mas cercanas a un camion, 
                    para que haga la menor cantidad de viajes posibles
                        -> necesitariamos otro algoritmo que realice esta cercania entre pedidos,
                                - debe buscar entregar los pedidos en el menor tiempo posible
                                - 1. agrupar los pedidos por zona //agrupar por tiempo o time windows. (9 zonas, ejemplo: costa norte, costa sur, costa centro, )
                                        -> Ordenar los pedidos segun tiempo de entrega
                                        -> Ordenar los pedidos segun cercania al almacen 
                                - 2. Maximizar la carga de cada uno de estos camiones // utilizar los mas chicos primero
                                    -> Knapsack proble. mochila
                                - 3. generar las ciudades por las que tienen que pasar estos camiones.

                -> Una vez que los camiones estén listos, se realizará el plan de transporte, la que involucra pasar por los tramos debidos.
                    Aquí es donde entraría el a*, para cada camion.

                            -> Tenemos que agregar los bloqueos. ✓

                
                    

        
        */

    }

    //Heuristica Euclidiana
    private double heuristicEucledian(VertexOficina current, VertexOficina destination) {
        //Approximated distance between 2 VertexOficinas.
        //Euclidean Method.
        /*
        return Math.sqrt(
                Math.pow(current.getX() - destination.getX(),2) + 
                Math.pow(current.getY() - destination.getY(),2) 
        );   

        */

        double costo = Mapa.calcularDistancia(current.getOficina(), destination.getOficina());

        return costo;
    } 

    public void printSolutionPath(){
        List<VertexOficina> path = new ArrayList<>();
        for (VertexOficina VertexOficina = destination; VertexOficina != null; VertexOficina = VertexOficina.getParent()){
            path.add(VertexOficina);
        }

        Collections.reverse(path);
        System.out.print(path);
    }


}

