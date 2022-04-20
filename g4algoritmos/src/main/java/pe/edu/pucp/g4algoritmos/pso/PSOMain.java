package pe.edu.pucp.g4algoritmos.pso;

/*

DEPRE-1-ALGORITMOS
ODIPARPACK SW

Clase que llama a la ejecución del algoritmo PSO

 */

public class PSOMain {

    public static void Main()
    {
        ParticleSwarmOptimization optimization = new ParticleSwarmOptimization();
        optimization.solve();
        optimization.showSolution();
    }
}
    

