package pe.edu.pucp.g4algoritmos.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pe.edu.pucp.g4algoritmos.astar.GrafoAStar;
import pe.edu.pucp.g4algoritmos.utilitarios.LoadData;

public class Mapa {

    public static final double limiteXInferior = 0.0; //Coordenadas limite inferior X
    public static final double limiteXSuperior = 0.0; //Coordenadas superior inferior X
    public static final double limiteYInferior = 0.0; //Coordenadas limite inferior Y
    public static final double limiteYSuperior = 0.0; //Coordenadas superior inferior Y
    public static double velocidadCamiones = 60.0; //Velocidad Promedio camiones (KM/H). Se debe de cambiar a 5 velocidades en el futuro.

    public static double duracionMantenimiento = 1.0; //Duración del mantenimiento en Horas

    public Date fechaHoraComienzo;//DateTime de la fecha de comienzo de la simulacion
    public Date fechaHoraActual; //DateTime de la fecha actual de la simulacion
    public Date fechaHoraFin; //DateTime de la fecha FIN de la simulacion

    public static List<Oficina> listaOficinas = new ArrayList<>();
    public static List<Oficina> listaAlmacenes = new ArrayList<>(); 
    public static List<Tramo> listaTramos = new ArrayList<>();
    public static List<Camion> listaCamiones = new ArrayList<>();
    public static List<Bloqueo> listaBloqueos = new ArrayList<>();
    public static List<Pedido> listaPedidos = new ArrayList<>();
    public static List<Entrega> listEntregas = new ArrayList<>();
    public static List<Ruta> listRutas = new ArrayList<>();

    public static GrafoAStar grafoAStar = new GrafoAStar(1);


    public Mapa (){

       		// Creating the LocalDatetime object
		LocalDate currentLocalDate = LocalDate.now();
		
		// Getting system timezone
		ZoneId systemTimeZone = ZoneId.systemDefault();
		
		// converting LocalDateTime to ZonedDateTime with the system timezone
		ZonedDateTime zonedDateTime = currentLocalDate.atStartOfDay(systemTimeZone);
		
		// converting ZonedDateTime to Date using Date.from() and ZonedDateTime.toInstant()
		Date fechaHoraComienzo = AuxiliaryFunctions.getNowTime();
        Date fechaHoraActual = AuxiliaryFunctions.getNowTime();
    }


    public List<Oficina> getListaOficinas() {
        return listaOficinas;
    }


    public List<Tramo> getListaTramos() {
        return listaTramos;
    }

    public static List<Pedido> getListaPedidos(){
        return listaPedidos;
    }

    public static Oficina getOficinaByCodigo(String codigoOficina){
        List<Oficina> listaTotalAlmacenesOficinas = new ArrayList<>(listaOficinas);
        listaTotalAlmacenesOficinas.addAll(listaAlmacenes);
        
        for (Oficina oficina : listaTotalAlmacenesOficinas)
            if (oficina.getCodigo().equals(codigoOficina))
                return oficina;
        
        return null;
        
    }

    public static Camion getCamionByCodigo(String codigoCamion){
        for (Camion camion : listaCamiones)
            if (camion.getCodigo().equals(codigoCamion))
                return camion;
        
        return null;
        
    }

    public static Tramo getTramoByOficinas(String codigoOficinaInicio, String codigoOficinaFin){
        
        for (Tramo tramo : listaTramos)
            if (tramo.getCiudadInicio().getCodigo().equals(codigoOficinaInicio) && tramo.getCiudadFin().getCodigo().equals(codigoOficinaFin))
                return tramo;
        
        return null;
    }

    public static List<Tramo> getTramosByOficinaInicio(String codigoOficina){
        
        List<Tramo> tramos = new ArrayList<>();

        for (Tramo tramo : listaTramos)
            if (tramo.getCiudadInicio().getCodigo().equals(codigoOficina))
                tramos.add(tramo);

        return tramos;
    }

    public static double calcularDistancia(Oficina CiudadInicio, Oficina CiudadFin) {
        
        double x1 = CiudadInicio.getCoordX();
        double y1 = CiudadInicio.getCoordY();
        double x2 = CiudadFin.getCoordX();
        double y2 = CiudadFin.getCoordY();

        //return Math.sqrt(x2-))
        return Math.sqrt(Math.pow((x2-x1),2) + Math.pow((y2-y1),2));

    }


    public static int[] calcularTipoCamionesPorAlmacen(Oficina almacen){

        int[] counter = {0,0,0}; //0: A, 1: B, 
        for (int i = 0; i < listaCamiones.size(); i++){
            if (listaCamiones.get(i).getAlmacen().getCodigo().equals(almacen.getCodigo()))
            {
                if (listaCamiones.get(i).getTipo().getCodigo() == 'A')
                    counter[0]++; 
                else if (listaCamiones.get(i).getTipo().getCodigo() == 'B')
                    counter[1]++; 
                else 
                    counter[2]++; 
            }
        }
        return counter;
    }

    public static List<Camion> getListaCamionesPorAlmacen(Oficina almacen, char tipoCamion){
        List<Camion> camiones = new ArrayList<>();
            for (Camion camion : listaCamiones){
                if (camion.getAlmacen().getCodigo().equals(almacen.getCodigo()) && 
                    camion.getTipo().getCodigo() == tipoCamion &&
                    camion.getEstado() == 1){
                    camiones.add(camion);
                }
            }
        return camiones;
    }

    public static List<Camion> extractListaCamionesPorAlmacen(Oficina almacen, char tipoCamion, int cantidad){
        List<Camion> camiones = new ArrayList<>();
            int cant = cantidad;
            for (Camion camion : listaCamiones){
                if (camion.getAlmacen().getCodigo().equals(almacen.getCodigo()) && 
                    camion.getTipo().getCodigo() == tipoCamion &&
                    camion.getEstado() == 1){
                    
                    camiones.add(camion);
                    camion.setEstado(2);
                    cant--;
                }
                if (cant <= 0)
                    break;
            }
        return camiones;
    }

    public static int enrutarCamiones(List<Camion> listCamiones){

        int counter = 0;
        for (Camion camion : listCamiones){
            camion.setEstado(3);
            counter++;
        }
        return counter;
    }

    public static void inicializarGrafoAstar(){
        grafoAStar = new GrafoAStar(1);
    }

/*
    public static boolean bloquearTramo(Tramo tramo){
        //for (Tramo tramo : listaTramos)
       // if (tramo.getCiudadInicio().getCodigo().equals(codigoOficina))
       //     tramos.add(tramo);
    }
    
*/
    public void setListaTramos(List<Tramo> listaTramos) {
        this.listaTramos = listaTramos;
    }

    public static void setListaCamiones(List<Camion> listaCamion){
        listaCamiones = listaCamion;
    }

    public int ejecutarBloqueos(){
        //Regresa la cantidad de bloqueos realizados a la hora actual
        //Si regresa 0: No se realizo algun bloqueo
        
        if (listaBloqueos.size() == 0)
            return 0;
        int counter = 0;
        for (Bloqueo bloqueo : listaBloqueos)
            counter += bloqueo.tryBloquear();
        
        return counter;
    }

    public int ejecutarDesbloqueos(){

        //Regresa la cantidad de bloqueos realizados a la hora actual
        //Si regresa 0: No se realizo algun bloqueo
        
        if (listaBloqueos.size() == 0)
            return 0;
        int counter = 0;
        for (int i = 0; i < listaBloqueos.size(); i++){
            if (listaBloqueos.get(i).tryDesbloquear() == 1){
                listaBloqueos.remove(i);
                counter++;                
            }
        }
        return counter;
    }

    public static void cargarAlmacenesYOficinas(String ruta) {
        List<Oficina> lista_total = LoadData.leerOficinas(ruta);
        
        listaOficinas.addAll(lista_total);
        listaOficinas.removeIf(x -> x.EsAlmacen());

        listaAlmacenes.addAll(lista_total);
        listaAlmacenes.removeIf(x -> !x.EsAlmacen());
    }

    public static void cargarTramos(String ruta) {
        listaTramos.addAll(LoadData.leerTramos(ruta));
    }

    public static void cargarPedidos(String... rutas) {
        for (String ruta : rutas) {
            List<Pedido> lista_ped = LoadData.leerPedidos(ruta);
            if(lista_ped != null) {
                listaPedidos.addAll(lista_ped);
            }
        }
    }

    public static void cargarBloqueos(String... rutas) {
        for (String ruta : rutas) {
            List<Bloqueo> lista_bloq = LoadData.leerBloqueos(ruta);
            if(lista_bloq != null) {
                listaBloqueos.addAll(lista_bloq);
            }
        }
    }

    public static int tramosSinOficinaInicial(){
        int counter = 0;
        for (Tramo tramo : listaTramos){
            if (tramo.getCiudadInicio() == null )
                counter ++;
        }
        return counter;
    }

    public static int tramosSinOficinaDestino(){
        int counter = 0;
        for (Tramo tramo : listaTramos){
            if (tramo.getCiudadFin() == null )
                counter ++;
        }
        return counter;
    }
}
