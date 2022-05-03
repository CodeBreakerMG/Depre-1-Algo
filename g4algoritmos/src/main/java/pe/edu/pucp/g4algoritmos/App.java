package pe.edu.pucp.g4algoritmos;

import pe.edu.pucp.g4algoritmos.solucion1.*;
import pe.edu.pucp.g4algoritmos.pso.PSOMain;

import java.io.File;

import pe.edu.pucp.g4algoritmos.model.Mapa;
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
        //Función que llama al A-STAR
        PrimeraSolucion solucion = new PrimeraSolucion();

        //Primero, cargar la data al maestro MAPA
        final String sep = File.separator;

        Mapa.cargarAlmacenesYOficinas(System.getProperty("user.dir")+sep+"data"+sep+"inf226.oficinas.txt");
        Mapa.cargarTramos(System.getProperty("user.dir")+sep+"data"+sep+"inf226.tramos.v.2.0.txt");
        Mapa.cargarPedidos(System.getProperty("user.dir")+sep+"data"+sep+"inf226.ventas202203.txt",
                           System.getProperty("user.dir")+sep+"data"+sep+"inf226.ventas202204.txt",
                           System.getProperty("user.dir")+sep+"data"+sep+"inf226.ventas202205.txt");
        
        System.out.println(String.format("Cantidad de oficinas: %4d", Mapa.listaOficinas.size() + Mapa.listaAlmacenes.size()));
        System.out.println(String.format("Cantidad de tramos:   %4d", Mapa.listaTramos.size()));
        System.out.println(String.format("Cantidad de pedidos:  %4d", Mapa.listaPedidos.size()));

        //Función que llama al PSO:
        //PSOMain.Main();
    }
}
