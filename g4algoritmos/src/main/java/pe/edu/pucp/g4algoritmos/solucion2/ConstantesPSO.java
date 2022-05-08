package pe.edu.pucp.g4algoritmos.solucion2;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

import pe.edu.pucp.g4algoritmos.model.AuxiliaryFunctions;
import pe.edu.pucp.g4algoritmos.model.Camion;
import pe.edu.pucp.g4algoritmos.model.Oficina;
import pe.edu.pucp.g4algoritmos.model.Tramo;

public class ConstantesPSO {
 
    public final static int NUM_DIMENSIONS = 2;
    public final static int NUM_PARTICLES = 10;
    public final static int MAX_ITERATIONS = 100;
    public final static double MIN_X = -2;
    public final static double MAX_X = 2;
    public final static double MIN_Y = -2;
    public final static double MAX_Y = 2;
    public final static double w = 0.1; //Inertia Weight 
    public final static double c1 = 1.49; //Cognitive Weight / Local
    public final static double c2 = 1.49; //Social Weight / Global

    /*Pensalization Constants*/
    public final static double theta = 5.0;
    public final static double lambda = 2.0;
    public static final double MILI_HORAS = 3.6e-6; //MILI to hours 

    //Variable global:
    public static double costoParcial;
    
    public static double f(double[] data){
        /*The parameter for this function is an array of two parameters because we are working on 2 dimensions*/

        /*

        Cost function f:  
        f(x,y) = exp(-x*x-y*y)*sin(x) 
        
        */
        return Math.exp(-data[0]*data[0]-data[1]*data[1])*Math.sin(data[0]);
        
        
        //P.D: it is a 2 dimensional function, a picture can be found on this proyect

        
    }

    public static double f(List<Position> positions, int num_dimensions, int num_almacenes, Date tiempo_salida, List<Oficina> almacenes, List<Camion> camiones){

        //Funcion Fitness basado en el paper 

        int i,j;
        PriorityQueue<Position> queue = new PriorityQueue<>(new PositionComparator());
        List<List<Oficina>> oficinasPorAlmacen = new ArrayList<>();
        List<List<Date>> tiempoMinimoOficinasPorAlmacen = new ArrayList<>();
        
        //List<List<Double>> paquetesPorAlmacen = new ArrayList<>();
        
        for (Position p: positions){
            queue.add(p);
        }

        for (i = 0; i < num_almacenes; i++){
            double pos = (double) i;
            List<Oficina> listOficinas = new ArrayList<>();
            List<Date> tiemposMinimos = new ArrayList<>();
            do{
                Position position = queue.peek();
                
                if (position == null)
                    break;
                pos = position.getRandomPosition();
                
                if (pos < (double)(i + 1)){
                    listOficinas.add(position.getOficina());
                    tiemposMinimos.add(position.getL());
                    queue.poll();
                }

            }while (pos < (double)(i + 1));
            
            oficinasPorAlmacen.add(listOficinas);
            tiempoMinimoOficinasPorAlmacen.add(tiemposMinimos);
        }

        double cost = 0.0;
        /*

        Sd: City sequence of depot d

        DS(p): Travelling time of trip p
        TV(p): Amount of time window violation in trip p
        LV(p): Amount of load violation of vehicle in trip p

        SE ASUME, POR EL MOMENTO, UN CAMION Y UN PATH O TRIP
        
        */

        double ds = 0.0;
        double tv = 0.0;
        double lv = 0.0;

        //DS & TV:
        for (i = 0; i < num_almacenes; i++){
            
            List<Date> tiempoLlegadaPorOficina = calcularCostoYTiempoLlegadasRuta(oficinasPorAlmacen.get(i), almacenes.get(i), tiempo_salida);
            
            ds += costoParcial; //costo parcial calculado en: calcularCostoYTiempoLlegadasRuta
            
            /*
            for (j = 0; j < oficinasPorAlmacen.get(i).size(); j++){
                tv += AuxiliaryFunctions.compareDates(tiempoMinimoOficinasPorAlmacen.get(i).get(j), tiempoLlegadaPorOficina.get(i)) >= 0 ? 
                    Math.abs(tiempoMinimoOficinasPorAlmacen.get(i).get(j).getTime() - tiempoLlegadaPorOficina.get(i).getTime()) * MILI_HORAS : 
                    0;
            }*/

        }

        cost = ds + theta * tv + lambda * lv;

        return cost;
    }

 


    private static List<Date> calcularCostoYTiempoLlegadasRuta(List<Oficina> oficinas, Oficina almacen, Date tiempo_salida) {
        
        costoParcial = 0.0; //Costo de la ruta actual EN HORAS. CONSTANTE DE ESTA CLASE

        //Se realizará un TSP y se obtendrá el costo de la ruta

        List<Date> tiempoLlegadaPorOficina = new ArrayList<>(); //Tiempos de Llegada por Oficina de oficina i a j, 
        double tiempo_salida_horas = tiempo_salida.getTime() * MILI_HORAS;

        List<Oficina> listaOficinas = new ArrayList<>();
        listaOficinas.add(almacen);
        listaOficinas.addAll(oficinas);



        for (int index = 0; index < listaOficinas.size(); ++index){
            
            double tiempoLlegadaOficinaDestino = 0.0;
            //Mismo Loop utilizado que en SingleTour.calcularTramos
            Oficina fromOficina = listaOficinas.get(index);
            Oficina destinationOficina = null;

            if(index + 1 < listaOficinas.size())
                destinationOficina = listaOficinas.get(index + 1);
            else
                destinationOficina = listaOficinas.get(0);
            
            List<Tramo> tramosParciales = fromOficina.recorridoHasta(destinationOficina);

            for (Tramo t : tramosParciales){
                tiempoLlegadaOficinaDestino += t.getCosto();
            }

            costoParcial += tiempoLlegadaOficinaDestino;

            if (index == 0){
                Date tiempoParcial = tiempo_salida;
                tiempoParcial = AuxiliaryFunctions.addHoursToJavaUtilDate(tiempoParcial, tiempoLlegadaOficinaDestino);
                tiempoLlegadaPorOficina.add(tiempoParcial);
            }
            else {
                Date tiempoParcial = AuxiliaryFunctions.addHoursToJavaUtilDate(tiempoLlegadaPorOficina.get(index - 1), tiempoLlegadaOficinaDestino);
                tiempoLlegadaPorOficina.add(tiempoParcial);
            }
            
        }
 
        return tiempoLlegadaPorOficina;
    }

    public void printOficinasXAlmacen(List<List<OficinaPSO>> oficinasXAlmacen){

        DecimalFormat myFormatter = new DecimalFormat("0#");
        
        int i = 1;
        for (List<OficinaPSO> lista : oficinasXAlmacen){
            System.out.println("Lista almacen: " +  myFormatter.format(i));
            int j = 1;
            for (OficinaPSO oficina : lista){
                System.out.println("\t" +  myFormatter.format(j) + ". " + oficina.toString());
                j++;
            }
            i++;
        }
    }
}
