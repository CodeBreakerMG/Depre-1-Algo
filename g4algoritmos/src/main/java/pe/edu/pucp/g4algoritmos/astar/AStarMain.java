package pe.edu.pucp.g4algoritmos.astar;

import java.io.File;

import pe.edu.pucp.g4algoritmos.model.Mapa;

/*

DEPRE-1-ALGORITMOS
ODIPARPACK SW

Clase que llama a la ejecución del algoritmo A*

La idea es adecuar el presente algoritmo para desarrollar el problema.
Evidentemente, por el momento, el algoritmo resuelve una simple función de optimización
Trata de buscar el camino con menor costo desde el origen hasta el destino.

 */

public class AStarMain {

    public static void Main()
    {
        final String sep = File.separator;

        Mapa.cargarAlmacenesYOficinas(System.getProperty("user.dir")+sep+"data"+sep+"inf226.oficinas.txt");
        Mapa.cargarTramos(System.getProperty("user.dir")+sep+"data"+sep+"inf226.tramos.v.2.0.txt");
        Mapa.cargarPedidos(System.getProperty("user.dir")+sep+"data"+sep+"inf226.ventas202203.txt",
                           System.getProperty("user.dir")+sep+"data"+sep+"inf226.ventas202204.txt",
                           System.getProperty("user.dir")+sep+"data"+sep+"inf226.ventas202205.txt");
        
        //System.out.println(String.format("Cantidad de oficinas: %4d", Mapa.listaOficinas.size() + Mapa.listaAlmacenes.size()));
        //System.out.println(String.format("Cantidad de tramos:   %4d", Mapa.listaTramos.size()));
        //System.out.println(String.format("Cantidad de pedidos:  %4d", Mapa.listaPedidos.size()));

        Node n1 = new Node("A",0,0);
        Node n2 = new Node("B",10,20);
        Node n3 = new Node("C",20,40);
        Node n4 = new Node("D",30,10);
        Node n5 = new Node("E",40,30);
        Node n6 = new Node("F",50,10);
        Node n7 = new Node("G",50,40);
        
        n1.addNeighbor(new Edge(n2, 10));
        n1.addNeighbor(new Edge(n4, 50));
        
        n2.addNeighbor(new Edge(n3, 10));
        n2.addNeighbor(new Edge(n4, 20));
        
        n3.addNeighbor(new Edge(n5, 10));
        n3.addNeighbor(new Edge(n7, 30));
        
        n4.addNeighbor(new Edge(n6, 80));
        
        n5.addNeighbor(new Edge(n6, 50));
        n5.addNeighbor(new Edge(n7, 10));
        
        n7.addNeighbor(new Edge(n6, 10));
		
        AStarAlgorithmOriginal search = new AStarAlgorithmOriginal(n1,n6);
        search.run();
        search.printSolutionPath();
    }
    
}
