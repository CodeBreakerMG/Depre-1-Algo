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
import pe.edu.pucp.g4algoritmos.model.Pedido;
import pe.edu.pucp.g4algoritmos.model.Ruta;
import pe.edu.pucp.g4algoritmos.model.Tramo;

public class ConstantesPSO {
 
  /*  
    public final static int NUM_PARTICLES = 100;
    public final static int MAX_ITERATIONS = 1000;
    
*/
    public final static int NUM_PARTICLES = 10;
    public final static int MAX_ITERATIONS = 100;

    public final static double w = 0.3; //Inertia Weight 
    public final static double c1 = 1.49; //Cognitive Weight / Local
    public final static double c2 = 1.49; //Social Weight / Global

    /*Pensalization Constants*/
    public final static double theta = 500.5;
    public final static double lambda = 50000.0;
    public static final double MILI_HORAS = 3.6e-6; //MILI to hours 
    public static final int MAX_NUM_CAMIONES = 3;

    //Variable global:
    
    public static double costoParcial;
    public static double cargaParcial;
    public static int counterIterations = 0;
    
    public static double f(double[] data){
        /*The parameter for this function is an array of two parameters because we are working on 2 dimensions*/

        /*

        Cost function f:  
        f(x,y) = exp(-x*x-y*y)*sin(x) 
        
        */
        return Math.exp(-data[0]*data[0]-data[1]*data[1])*Math.sin(data[0]);
        
        
        //P.D: it is a 2 dimensional function, a picture can be found on this proyect

        
    }

    public static double f(List<Position> positions, int num_almacenes, RepositoryPSO repository){

        /*Funcion Fitness basado en el paper */

        /*RETURNS: Total cost of solution (in unit of time: HOURS * other stuff) (R(phi))*/

        /*Variables definitions*/
        int i,j,k;                                                   //int values to traverse lists.
        List<List<Position>> positionsByDepot = new ArrayList<>(); //DEPOT == almacen

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

            /*Obtaining costs through function, can use A* */
            Triplet<List<Date>, Double, List<Integer>> outputValues = calcularCostoYTiempoLlegadasRuta(positionsByDepot.get(i), i, repository);

            /*2.2 DS Calculation*/
            ds += outputValues.getValue1(); //costo parcial calculado en: calcularCostoYTiempoLlegadasRuta
             
            /*2.3 LV Calculation*/
            List<Integer> cantidadesAsignadasCamion = outputValues.getValue2();
            List<Camion> camionesSeleccionados = camionesSeleccionadosAlmacen(positionsByDepot.get(i), i, repository);

            j = 0;
            for (Camion c : camionesSeleccionados){
                int capacidadCamion = c.capacidad();
                int asignadosCamion = cantidadesAsignadasCamion.get(j);
                if (capacidadCamion < asignadosCamion)
                    lv += Math.abs(asignadosCamion - capacidadCamion);
                //lse
                 //   System.out.println("No hay violacion");
                j++;
            }

            /*if (lv <= 0.0 && i == num_almacenes - 1){
                System.out.println("No hay violacion CARGA ");
            }*/

            /*2.4 TV Calculation*/
            List<Date> tiempoLlegadaPorOficina = outputValues.getValue0();
            for (j = 0; j < positionsByDepot.get(i).size(); j++){
                
                Date tiempoMaximoLlegadaOficina = positionsByDepot.get(i).get(j).getL();
                Date tiempoActualDeLlegada = tiempoLlegadaPorOficina.get(j);
                
                if (AuxiliaryFunctions.compareDates(tiempoMaximoLlegadaOficina, tiempoActualDeLlegada) > 0 ){
                    tv += Math.abs(tiempoActualDeLlegada.getTime() - tiempoMaximoLlegadaOficina.getTime()) * MILI_HORAS;
                }

                /*if (tv <= 0.0 && i == num_almacenes - 1){
                    System.out.println("No hay violacion FECHA YEEE ");
                }*/
    
            }
           
        }
        

        return (ds + theta * tv + lambda * lv);
    }


 
    private static Triplet<List<Date>, Double, List<Integer>> calcularCostoYTiempoLlegadasRuta(List<Position> positions, int indexAlmacen, RepositoryPSO repository) {
        
        double costo = 0.0; //Costo de la ruta actual EN HORAS. CONSTANTE DE ESTA CLASE
        List<Integer> cargaPorCamion = new ArrayList<>(); //Carga de paquetes por camion
        List<Date> tiempoLlegadaPorOficina = new ArrayList<>(); //Tiempos de Llegada por Oficina de oficina i a j, para un camion

        OficinaPSO almacen = new OficinaPSO(repository.getAlmacen(indexAlmacen));

        /*1. Asignar Oficinas por Camion*/
        List<List<OficinaPSO>> oficinasPorCamion = new ArrayList<>();

        /*1.1 Contabilizacion de camiones del almacen*/
        List<Integer> camionesIndex = new ArrayList<>(); //Camion INDEX del cual se operara (indice en Position)
        for (Position p: positions){
            
            if (camionesIndex.contains(p.getVehiclePosition()) == false){
                camionesIndex.add(p.getVehiclePosition());
            }
        }

        /*1.2 Asignación*/
        for (int indexCamion : camionesIndex){
            List<OficinaPSO> oficinas = new ArrayList<>();
            for (Position p: positions){
                if (p.getVehiclePosition() == indexCamion){
                    oficinas.add(p.getOficinaPSO());
                }
            }    
            oficinasPorCamion.add(oficinas);
            
        } 
        
        /*2. Enrutamiento de cada camion*/
        for (int i = 0; i < camionesIndex.size(); i++){

            List<OficinaPSO> oficinasConAlmacen = new ArrayList<>(); //Lista de Oficinas con almacén como oficina 0, se realizará un TSP.
            oficinasConAlmacen.add(almacen);                      //Almacén como ciudad 0.
            oficinasConAlmacen.addAll(oficinasPorCamion.get(i));

            Triplet<List<Date>,Double,Integer> outputValues = calcularCostoRutas(oficinasConAlmacen);

            tiempoLlegadaPorOficina.addAll(outputValues.getValue0());
            costo += outputValues.getValue1();
            cargaPorCamion.add(outputValues.getValue2());

    
        }
 
        return new Triplet<List<Date>,Double,List<Integer>>(tiempoLlegadaPorOficina, costo, cargaPorCamion);

    }

    public static Triplet<List<Date>, Double, Integer>  calcularCostoRutas(List<OficinaPSO> listaOficinas) {

        double costo = 0.0; //Costo de la ruta actual EN HORAS. CONSTANTE DE ESTA CLASE
        int carga = 0; //Carga de paquetes
        List<Date> tiempoLlegadaPorOficina = new ArrayList<>(); //Tiempos de Llegada por Oficina de oficina i a j, 
        Date tiempo_salida = AuxiliaryFunctions.setDateWithTime("2010-12-31 00:00:00");

        for (OficinaPSO o : listaOficinas)
            tiempo_salida = AuxiliaryFunctions.maximumDate(tiempo_salida, o.tiempoMinimoSalidaCamion());

        for (int index = 0; index < listaOficinas.size(); ++index){
            
            double tiempoLlegadaOficinaDestino = 0.0;
            //Mismo Loop utilizado que en SingleTour.calcularTramos
            OficinaPSO fromOficina = listaOficinas.get(index);
            OficinaPSO destinationOficina = null;

            /*CALCULO DE LA CARGA*/
            carga += fromOficina.cantidadPaquetes();

            /*CALCULO DEL COSTO (TIEMPO)*/
            if(index + 1 < listaOficinas.size())
                destinationOficina = listaOficinas.get(index + 1);
            else
                destinationOficina = listaOficinas.get(0);
            
            //CON A*:
            //tiempoLlegadaOficinaDestino += fromOficina.costToExact(destinationOficina);

            //Sin A*:
            tiempoLlegadaOficinaDestino += fromOficina.costToApprox(destinationOficina);

            costo += tiempoLlegadaOficinaDestino;

            if (index == 0){
                Date tiempoParcial = tiempo_salida;
                tiempoParcial = AuxiliaryFunctions.addHoursToJavaUtilDate(tiempoParcial, tiempoLlegadaOficinaDestino);
                tiempoLlegadaPorOficina.add(tiempoParcial);
            }
            else {
                Date tiempoParcial = AuxiliaryFunctions.addHoursToJavaUtilDate(tiempoLlegadaPorOficina.get(index - 1), tiempoLlegadaOficinaDestino);
                if(index + 1 < listaOficinas.size())
                    tiempoLlegadaPorOficina.add(tiempoParcial);
            }
            
        }
        
        return new Triplet<List<Date>,Double,Integer>(tiempoLlegadaPorOficina, costo, carga);
    }

    public static List<Camion> camionesSeleccionadosAlmacen(List<Position> positions, int indexAlmacen, RepositoryPSO repository){
        List<Camion> allCamionesAlmacen = repository.getCamionesPorAlmacen(indexAlmacen);
        List<Camion> camiones = new ArrayList<>();

        for (Position p : positions){
            Camion camionPos = allCamionesAlmacen.get(p.getVehiclePosition());
            if (camiones.contains(camionPos) == false)
                camiones.add(camionPos);
        }
        return camiones;
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

    public static List<Ruta> generarPlanesDeTransporte(RepositoryPSO repository, List<Position> positions){
        List<Ruta> listRutas = new ArrayList<>();

        List<List<Position>> positionsByDepot = new ArrayList<>();
        
        for (int i = 0; i < repository.getNumAlmacenes(); i++){
            List<Position> listPositions = new ArrayList<>();
            for (Position p : positions){
                if ( i <= p.getDepot()  && p.getDepot() < i + 1){
                    listPositions.add(p);
                }
            }
            positionsByDepot.add(listPositions);
        }

        
        for (int i = 0; i < repository.getNumAlmacenes(); i++){

            /*Obtaining costs through function, can use A* */

            /*Por camion: */

            List<List<OficinaPSO>> oficinasPorCamion = new ArrayList<>();
            List<Integer> listaDeCamionesInteger = new ArrayList<>(); //Camion INDEX del cual se operara (indice en Position)

            /*1.1 Contabilizacion de camiones del almacen*/
            
            for (Position p: positions){
                
                if (listaDeCamionesInteger.contains(p.getVehiclePosition()) == false){
                    listaDeCamionesInteger.add(p.getVehiclePosition());
                }
            }
    
            /*1.2 Asignación*/
            for (int indexCamion : listaDeCamionesInteger){
                List<OficinaPSO> oficinas = new ArrayList<>();
                for (Position p: positions){
                    if (p.getVehiclePosition() == indexCamion){
                        oficinas.add(p.getOficinaPSO());
                    }
                }    
                oficinasPorCamion.add(oficinas);
            } 

            for (int j = 0; j < listaDeCamionesInteger.size();j++){
                
                Triplet<List<Date>, Double, List<List<Tramo>>> outputValues = calcularRutasPlanDeTransporte(oficinasPorCamion.get(j), repository.getAlmacen(i));
                Ruta ruta = new Ruta();
                
                ruta.setFechaEstimadaLlegadaOficinas(outputValues.getValue0());
                
                ruta.setListaTramosPorOficina(outputValues.getValue2());
                ruta.setCamion(repository.getCamion(i, listaDeCamionesInteger.get(j)));
                
                
                List<Oficina> oficinas = new ArrayList<>();
                List<Pedido> pedidos = new ArrayList<>();
                oficinas.add(repository.getAlmacen(i));
                for (OficinaPSO ofi : oficinasPorCamion.get(j)){
                    oficinas.add(ofi.getOficina());
                    pedidos.addAll(ofi.getPedidos());
                }
                ruta.setListaOficinas(oficinas);
                ruta.setListaPedidos(pedidos);
                ruta.setEstado(1);
                Date tiempo_salida = AuxiliaryFunctions.setDateWithTime("2010-12-31 00:00:00");

                double carga = 0;
                for (OficinaPSO o : oficinasPorCamion.get(j)){
                    tiempo_salida = AuxiliaryFunctions.maximumDate(tiempo_salida, o.tiempoMinimoSalidaCamion());
                    carga+= o.getQ();
                }
                ruta.setFechaHoraInicio(tiempo_salida);
                ruta.setCargaTotal(carga);
                ruta.setCosto(outputValues.getValue1());



                listRutas.add(ruta);
            }
        
        }

        return listRutas;
    }

       public static Triplet<List<Date>, Double, List<List<Tramo>> >  calcularRutasPlanDeTransporte(List<OficinaPSO> listaOficinas, Oficina almacen) {

        double costo = 0.0; //Costo de la ruta actual EN HORAS. CONSTANTE DE ESTA CLASE
        
        List<List<Tramo>> listaTramosPorOficina = new ArrayList<>();
        List<Date> tiempoLlegadaPorOficina = new ArrayList<>(); //Tiempos de Llegada por Oficina de oficina i a j, 
        Date tiempo_salida = AuxiliaryFunctions.setDateWithTime("2010-12-31 00:00:00");

        for (OficinaPSO o : listaOficinas)
            tiempo_salida = AuxiliaryFunctions.maximumDate(tiempo_salida, o.tiempoMinimoSalidaCamion());

        listaOficinas.add(0, new OficinaPSO(almacen)); //Almacén como ciudad 0.

        for (int index = 0; index < listaOficinas.size(); ++index){
            
            double tiempoLlegadaOficinaDestino = 0.0;
            //Mismo Loop utilizado que en SingleTour.calcularTramos
            OficinaPSO fromOficina = listaOficinas.get(index);
            OficinaPSO destinationOficina = null;

            
            /*CALCULO DEL COSTO (TIEMPO) y tramos*/
            if(index + 1 < listaOficinas.size())
                destinationOficina = listaOficinas.get(index + 1);
            else
                destinationOficina = listaOficinas.get(0);
            
            //CON A*:
            List<Tramo> tramos = fromOficina.getTramosTo(destinationOficina);
            for (Tramo t: tramos)
                tiempoLlegadaOficinaDestino += t.getCosto();
 

            costo += tiempoLlegadaOficinaDestino;

            if (index == 0){
                Date tiempoParcial = tiempo_salida;
                tiempoParcial = AuxiliaryFunctions.addHoursToJavaUtilDate(tiempoParcial, tiempoLlegadaOficinaDestino);
                tiempoLlegadaPorOficina.add(tiempoParcial);
            }
            else {
                Date tiempoParcial = AuxiliaryFunctions.addHoursToJavaUtilDate(tiempoLlegadaPorOficina.get(index - 1), tiempoLlegadaOficinaDestino);
                if(index + 1 < listaOficinas.size())
                    tiempoLlegadaPorOficina.add(tiempoParcial);
            }

            listaTramosPorOficina.add(tramos);
            
        }
        
        return new Triplet<List<Date>,Double,List<List<Tramo>>>(tiempoLlegadaPorOficina, costo, listaTramosPorOficina);
    }
    
}
