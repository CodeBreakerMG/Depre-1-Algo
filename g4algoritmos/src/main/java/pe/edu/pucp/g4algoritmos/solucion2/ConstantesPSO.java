package pe.edu.pucp.g4algoritmos.solucion2;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

import org.javatuples.Triplet;

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
    public static final int MAX_NUM_CAMIONES = 4;

    //Variable global:
    
    public static double costoParcial;
    public static double cargaParcial;
    
    public static double f(double[] data){
        /*The parameter for this function is an array of two parameters because we are working on 2 dimensions*/

        /*

        Cost function f:  
        f(x,y) = exp(-x*x-y*y)*sin(x) 
        
        */
        return Math.exp(-data[0]*data[0]-data[1]*data[1])*Math.sin(data[0]);
        
        
        //P.D: it is a 2 dimensional function, a picture can be found on this proyect

        
    }

    public static double f(List<Position> positions, int num_dimensions, int num_almacenes, RepositoryPSO repository){

        /*Funcion Fitness basado en el paper */

        /*Variables definitions*/
        int i,j;                                                   //int values to traverse lists.
        List<List<Position>> positionsByDepot = new ArrayList<>(); //DEPOT == almacen

        double cost = 0.0;  //Total cost of solution (in unit of time: HOURS * other stuff) (R(phi))
        double ds   = 0.0;  //Summ of all times of solution. Summ of DS(p)
        double tv   = 0.0;  //Summ of all time window penalty violation. Summ of (TV(p) * THETA). tv: time violation
        double lv   = 0.0;  //Summ of all load violations. Summ of (LV(p) * LAMBDA). lv: load violation
        
        /*1. Grouping cities by Depot, using "Position" class variable */
        for (i = 0; i < num_almacenes; i++){
            List<Position> listPositions = new ArrayList<>();
            for (Position p : positions){
                if ( i <= p.getDepot()  && p.getDepot() < i + 1){
                    listPositions.add(p);
                }
            }
            positionsByDepot.add(listPositions);
        }

                /*
        Sd: City sequence of depot d

        DS(p): Travelling time of trip p
        TV(p): Amount of time window violation in trip p
        LV(p): Amount of load violation of vehicle in trip p

        SE ASUME, POR EL MOMENTO, UN CAMION Y UN PATH O TRIP
        */

        /*2. Cost Calculations*/

        for (i = 0; i < num_almacenes; i++){

            Triplet<List<Date>, Double, Integer> values = calcularCostoYTiempoLlegadasRuta(positionsByDepot.get(i), i, repository);

            //calcularCostoYTiempoLlegadasRuta(oficinasPorAlmacen.get(i), almacenes.get(i), cargaOficinaAlmacen.get(i),camionesPorAlmacen.get(i),tiempo_salida);
        }



        return cost;
    }


 
    private static Triplet<List<Date>, Double, Integer> calcularCostoYTiempoLlegadasRuta(List<Position> positions, int indexAlmacen, RepositoryPSO repository) {
        
        double costo = 0.0; //Costo de la ruta actual EN HORAS. CONSTANTE DE ESTA CLASE
        int carga = 0; //Carga de paquetes
        List<Date> tiempoLlegadaPorOficina = new ArrayList<>(); //Tiempos de Llegada por Oficina de oficina i a j, 

        OficinaPSO almacen = new OficinaPSO(repository.getAlmacen(indexAlmacen));

        /*1. Asignar Oficinas por Camion*/
        List<List<OficinaPSO>> oficinasPorCamion = new ArrayList<>();

        /*1.1 Contabilizacion de camiones del almacen*/
        List<Integer> camionesIndex = new ArrayList<>();
        for (Position p: positions){
            if (camionesIndex.contains(p.getVehiclePosition()) == false){
                camionesIndex.add(p.getVehiclePosition());
            }
        }

        /*1.2 Asignación*/
        for (int i : camionesIndex){
            List<OficinaPSO> oficinas = new ArrayList<>();
            for (Position p: positions){
                if (p.getVehiclePosition() == i){
                    oficinas.add(p.getOficinaPSO());
                }
            }    
            oficinasPorCamion.add(oficinas);
        } 
        
        /*2. Enrutamiento de cada camion*/
        for (int i : camionesIndex){

            List<OficinaPSO> oficinasConAlmacen = new ArrayList<>(); //Lista de Oficinas con almacén como oficina 0, se realizará un TSP.
            oficinasConAlmacen.add(almacen);                      //Almacén como ciudad 0.
            oficinasConAlmacen.addAll(oficinasPorCamion.get(i));

            Triplet<List<Date>,Double,Integer> outputValues = calcularCostoRutas(oficinasConAlmacen);
    
        }
 
        return new Triplet<List<Date>,Double,Integer>(tiempoLlegadaPorOficina, costo, carga);

    }

    public static Triplet<List<Date>, Double, Integer>  calcularCostoRutas(List<OficinaPSO> listaOficinas) {

        double costo = 0.0; //Costo de la ruta actual EN HORAS. CONSTANTE DE ESTA CLASE
        int carga = 0; //Carga de paquetes
        List<Date> tiempoLlegadaPorOficina = new ArrayList<>(); //Tiempos de Llegada por Oficina de oficina i a j, 
        Date tiempo_salida = AuxiliaryFunctions.setDateWithTime("2025-12-31 00:00:00");

        for (OficinaPSO o : listaOficinas)
            tiempo_salida = AuxiliaryFunctions.maximumDate(tiempo_salida, o.tiempoMinimoSalidaCamion());

        for (int index = 0; index < listaOficinas.size(); ++index){
            
            double tiempoLlegadaOficinaDestino = 0.0;
            //Mismo Loop utilizado que en SingleTour.calcularTramos
            OficinaPSO fromOficina = listaOficinas.get(index);
            OficinaPSO destinationOficina = null;

            if(index + 1 < listaOficinas.size())
                destinationOficina = listaOficinas.get(index + 1);
            else
                destinationOficina = listaOficinas.get(0);
            
            tiempoLlegadaOficinaDestino += fromOficina.costToApprox(destinationOficina);

            costo += tiempoLlegadaOficinaDestino;

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
        //FALTA CARGA
        return new Triplet<List<Date>,Double,Integer>(tiempoLlegadaPorOficina, costo, carga);
    }



    public static double f(List<Position> positions, int num_dimensions, int num_almacenes, Date tiempo_salida, List<Oficina> almacenes, List<List<Camion>> camiones, RepositoryPSO repository){

        /*Funcion Fitness basado en el paper */
 




        /* antigua solucion  */
        PriorityQueue<Position> queue = new PriorityQueue<>(new PositionComparator());
        List<List<Oficina>> oficinasPorAlmacen = new ArrayList<>();
        List<List<Integer>> cargaOficinaAlmacen = new ArrayList<>();
        List<List<Camion>> camionesPorAlmacen = new ArrayList<>();
        List<List<Date>> tiempoMinimoOficinasPorAlmacen = new ArrayList<>();
        

        List<Position> listPositions = new ArrayList<>();
        listPositions.addAll(positions);
        Collections.sort(listPositions, new PositionComparator());
        //List<List<Double>> paquetesPorAlmacen = new ArrayList<>();
        
        for (Position p: positions){
            queue.add(p);
        }

        for (i = 0; i < num_almacenes; i++){
            double pos = (double) i;
            List<Oficina> listOficinas = new ArrayList<>();
            List<Camion> listCamiones = new ArrayList<>();
            List<Date> tiemposMinimos = new ArrayList<>();
            List<Integer> cargaOficina = new ArrayList<>();
            do{
                Position position = queue.peek();
                
                if (position == null)
                    break;
                pos = position.getRandomPosition();
                
                if (pos < (double)(i + 1)){
                    listOficinas.add(position.getOficina());
                    cargaOficina.add(position.getOficinaPSO().getQ());
                    if (listCamiones.contains(repository.getCamion(i, position.getVehiclePosition())) == false)
                        listCamiones.add(repository.getCamion(i, position.getVehiclePosition()));
                    tiemposMinimos.add(position.getL());
                    queue.poll();
                }

            }while (pos < (double)(i + 1));
            
            oficinasPorAlmacen.add(listOficinas);
            cargaOficinaAlmacen.add(cargaOficina);
            camionesPorAlmacen.add(listCamiones);
            tiempoMinimoOficinasPorAlmacen.add(tiemposMinimos);
        }

         cost = 0.0;
        /*

        Sd: City sequence of depot d

        DS(p): Travelling time of trip p
        TV(p): Amount of time window violation in trip p
        LV(p): Amount of load violation of vehicle in trip p

        SE ASUME, POR EL MOMENTO, UN CAMION Y UN PATH O TRIP
        
        */

         ds = 0.0;
         tv = 0.0;
         lv = 0.0;

        //DS & TV:
        for (i = 0; i < num_almacenes; i++){

            
            /*
            Triplet:

                Value 0 : List of dates
                Value 1 : total Cost of time
                Value 2 : total quantity 

            */

            Triplet<List<Date>, Double, Integer> values = calcularCostoYTiempoLlegadasRuta(oficinasPorAlmacen.get(i), almacenes.get(i), cargaOficinaAlmacen.get(i),camionesPorAlmacen.get(i),tiempo_salida);
            
            //ds += costoTiempo; //costo parcial calculado en: calcularCostoYTiempoLlegadasRuta
            
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


    private static  Triplet<List<Date>, Double, Integer> calcularCostoYTiempoLlegadasRuta(List<Oficina> oficinas, Oficina almacen, List<Integer> cantidadesPorOficina, List<Camion> camion, Date tiempo_salida) {
        
        double costo = 0.0; //Costo de la ruta actual EN HORAS. CONSTANTE DE ESTA CLASE
        int carga = 0; //Carga de paquetes
        List<Date> tiempoLlegadaPorOficina = new ArrayList<>(); //Tiempos de Llegada por Oficina de oficina i a j, 
        
        /*Se realizará un TSP y se obtendrá el costo de la ruta*/
        /*Se obtendrá estos parámetros por camión*/
        
        for (int i = 0; i < camion.size(); i++){

            List<Oficina> oficinasConAlmacen = new ArrayList<>(); //Lista de Oficinas con almacén como oficina 0, se realizará un TSP.
            oficinasConAlmacen.add(almacen);                      //Almacén como ciudad 0.
            oficinasConAlmacen.addAll(oficinas);

            //Triplet<List<Date>,Double,Integer> outputValues = calcularCostoRutas(listaOficinas, tiempo_salida );
    
        }
 
        return new Triplet<List<Date>,Double,Integer>(tiempoLlegadaPorOficina, costo, carga);
    }

    public static Triplet<List<Date>, Double, Integer>  calcularCostoRutas(List<Oficina> listaOficinas, Date tiempo_salida) {

        double costo = 0.0; //Costo de la ruta actual EN HORAS. CONSTANTE DE ESTA CLASE
        int carga = 0; //Carga de paquetes
        List<Date> tiempoLlegadaPorOficina = new ArrayList<>(); //Tiempos de Llegada por Oficina de oficina i a j, 
 
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

            costo += tiempoLlegadaOficinaDestino;

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
 
        return new Triplet<List<Date>,Double,Integer>(tiempoLlegadaPorOficina, costo, carga);
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
