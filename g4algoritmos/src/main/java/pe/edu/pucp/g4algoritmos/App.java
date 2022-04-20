package pe.edu.pucp.g4algoritmos;

import pe.edu.pucp.g4algoritmos.astar.AStarMain;
import pe.edu.pucp.g4algoritmos.pso.PSOMain;

/*

DEPRE-1-ALGORITMOS
ODIPARPACK SW

CLASE DE JAVA PARA EJECUTAR EL PROYECTO (MAIN)
DESDE ESTE FILE SE TIENE QUE EJECUTAR TODO.

 */
public class App 
{
    public static void main( String[] args )
    {
        //Función que llama al A-STAR
        AStarMain.Main();

        //Función que llama al PSO:
        PSOMain.Main();
    }
}
