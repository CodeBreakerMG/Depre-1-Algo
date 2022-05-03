package pe.edu.pucp.g4algoritmos;

import java.io.File;

import pe.edu.pucp.g4algoritmos.astar.AStarMain;
import pe.edu.pucp.g4algoritmos.model.Mapa;
import pe.edu.pucp.g4algoritmos.pso.PSOMain;

/*

DEPRE-1-ALGORITMOS
ODIPARPACK SW

CLASE DE JAVA PARA EJECUTAR EL PROYECTO (MAIN)
DESDE ESTE FILE SE TIENE QUE EJECUTAR TO DO.

 */
public class App 
{
    public static void main( String[] args )
    {
        // Carga de datos
        final String sep = File.separator;

        Mapa.cargarAlmacenesYOficinas(System.getProperty("user.dir")+sep+"data"+sep+"inf226.oficinas.txt");
        Mapa.cargarTramos(System.getProperty("user.dir")+sep+"data"+sep+"inf226.tramos.v.2.0.txt");
        Mapa.cargarPedidos(System.getProperty("user.dir")+sep+"data"+sep+"inf226.ventas202203.txt",
                           System.getProperty("user.dir")+sep+"data"+sep+"inf226.ventas202204.txt",
                           System.getProperty("user.dir")+sep+"data"+sep+"inf226.ventas202205.txt");
        Mapa.cargarBloqueos(System.getProperty("user.dir")+sep+"data"+sep+"inf226.bloqueo.01.txt",
                            System.getProperty("user.dir")+sep+"data"+sep+"inf226.bloqueo.02.txt",
                            System.getProperty("user.dir")+sep+"data"+sep+"inf226.bloqueo.03.txt");
        
        System.out.println(String.format("Cantidad de oficinas:  %4d", Mapa.listaOficinas.size()));
        System.out.println(String.format("Cantidad de almacenes: %4d", Mapa.listaAlmacenes.size()));
        System.out.println(String.format("Cantidad de tramos:    %4d", Mapa.listaTramos.size()));
        System.out.println(String.format("Cantidad de pedidos:   %4d", Mapa.listaPedidos.size()));
        System.out.println(String.format("Cantidad de bloqueos:  %4d", Mapa.listaBloqueos.size()));
        

        //Función que llama al A-STAR
        AStarMain.Main();

        //Función que llama al PSO:
        PSOMain.Main();
    }
}
