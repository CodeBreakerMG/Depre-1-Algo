package pe.edu.pucp.g4algoritmos.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.lang.model.util.ElementScanner6;

import pe.edu.pucp.g4algoritmos.astar.GrafoAStar;
import pe.edu.pucp.g4algoritmos.utilitarios.LoadData;

public class Mapa {

    public static final double limiteXInferior = 0.0; //Coordenadas limite inferior X
    public static final double limiteXSuperior = 0.0; //Coordenadas superior inferior X
    public static final double limiteYInferior = 0.0; //Coordenadas limite inferior Y
    public static final double limiteYSuperior = 0.0; //Coordenadas superior inferior Y
    public static double velocidadCamiones = 60.0; //Velocidad Promedio camiones (KM/H). Se debe de cambiar a 5 velocidades en el futuro.

    public static double velocidadCostaCosta   = 70.0; // Velocidad en km/h de costa a costa
    public static double velocidadCostaSierra  = 50.0; // Velocidad en km/h de costa a sierra
    public static double velocidadSierraSierra = 60.0; // Velocidad en km/h de sierra a sierra
    public static double velocidadSierraSelva  = 55.0; // Velocidad en km/h de sierra a selva
    public static double velocidadSelvaSelva   = 65.0; // Velocidad en km/h de selva a selva
    public static double velocidadCostaSelva   = 60.0; // Velocidad en km/h de costa a selva (not expected)

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

    public static HashMap<Oficina, List<Pedido>> hashMapPedidosAlmacen = new HashMap<>();
    public static HashMap<Oficina, List<Camion>> hashMapCamionesAlmacen = new HashMap<>();

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

    public static void setTramosToOficinas(){
        for (Oficina oficina: listaOficinas){
            oficina.setListaTramos(Mapa.getTramosByOficinaInicio(oficina.getCodigo()));

            //listaAlmacenes
        }
    }

    public static void setTramosToAlmacenes(){
        for (Oficina oficina: listaAlmacenes){
            oficina.setListaTramos(Mapa.getTramosByOficinaInicio(oficina.getCodigo()));

            //listaAlmacenes
        }
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
        
        final double RADIO = 6378.0; // Radio de la tierra en km
        
        double lat1 = CiudadInicio.getCoordY() * Math.PI / 180;
        double lon1 = CiudadInicio.getCoordX() * Math.PI / 180;
        double lat2 = CiudadFin.getCoordY() * Math.PI / 180;
        double lon2 = CiudadFin.getCoordX() * Math.PI / 180;

        double dif_lat = lat2 - lat1;
        double dif_lon = lon2 - lon1;

        double a = Math.pow(Math.sin(dif_lat/2), 2) +
                    Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dif_lon/2), 2);

        double distancia = 2 * RADIO * Math.asin(Math.sqrt(a));
        
        return distancia;
    }


    public static int[] calcularTipoCamionesPorAlmacen(Oficina almacen){

        int[] counter = {0,0,0}; //0: A, 1: B, 
        for (int i = 0; i < listaCamiones.size(); i++){
            if (listaCamiones.get(i).getAlmacen().getCodigo().equals(almacen.getCodigo()))
            {
                if (listaCamiones.get(i).getTipo().getCodigo() == 'A' && listaCamiones.get(i).getEstado() == 1)
                    counter[0]++; 
                if (listaCamiones.get(i).getTipo().getCodigo() == 'B' && listaCamiones.get(i).getEstado() == 1)
                    counter[1]++; 
                if (listaCamiones.get(i).getTipo().getCodigo() == 'C' && listaCamiones.get(i).getEstado() == 1)
                    counter[2]++; 
            }
        }
        return counter;
    }

    public static List<Camion> getListaCamionesPorTipoPorAlmacen(Oficina almacen, char tipoCamion){
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

    
    public static List<Camion> getCamionesPorAlmacen(Oficina almacen){
        List<Camion> camiones = new ArrayList<>();
            for (Camion camion : listaCamiones){
                if (camion.getAlmacen().getCodigo().equals(almacen.getCodigo()) && 
                    camion.getEstado() == 1){
                    camiones.add(camion);
                }
            }
        return camiones;
    }

    public static List<Camion> getListaCamionesDisponibles(){
        List<Camion> camiones = new ArrayList<>();
            for (Camion camion : listaCamiones){
                if (camion.getEstado() == 1){
                    camiones.add(camion);
                }
            }
        return camiones;
    }


    public static List<Camion> getListaCamionesPorAlmacen(Oficina almacen) {
        if(hashMapCamionesAlmacen.get(almacen) != null)
            return hashMapCamionesAlmacen.get(almacen);
        return new ArrayList<Camion>();
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

    public static void resetAstar(){
        for (Oficina oficina : listaOficinas){
            oficina.resetAstar();
        }for (Oficina oficina : listaAlmacenes){
            oficina.resetAstar();
        }
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
        // Carga de pedidos en listaPedidos
        for (String ruta : rutas) {
            List<Pedido> lista_ped = LoadData.leerPedidos(ruta);
            if(lista_ped != null) {
                listaPedidos.addAll(lista_ped);
            }
        }

        // Creación de listas de pedidos para cada almacén
        List<Pedido> lista_pedidos_Trujillo = new ArrayList<>();
        List<Pedido> lista_pedidos_Lima     = new ArrayList<>();
        List<Pedido> lista_pedidos_Arequipa = new ArrayList<>();

        // Repartición de pedidos para cada almacén
        for(Pedido ped : listaPedidos){
            double dist_Trujillo = calcularDistancia(getOficinaByCodigo("130101"), ped.getOficina());
            double dist_Lima     = calcularDistancia(getOficinaByCodigo("150101"), ped.getOficina());
            double dist_Arequipa = calcularDistancia(getOficinaByCodigo("040101"), ped.getOficina());

            if(dist_Trujillo < dist_Lima && dist_Trujillo < dist_Arequipa)
                lista_pedidos_Trujillo.add(ped);
            else if(dist_Lima < dist_Arequipa && dist_Lima < dist_Trujillo)
                lista_pedidos_Lima.add(ped);
            else
                lista_pedidos_Arequipa.add(ped);
        }

        // Guardado de listas de pedidos por almacén en hashmap
        hashMapPedidosAlmacen.put(getOficinaByCodigo("130101"), lista_pedidos_Trujillo);
        hashMapPedidosAlmacen.put(getOficinaByCodigo("150101"), lista_pedidos_Lima);
        hashMapPedidosAlmacen.put(getOficinaByCodigo("040101"), lista_pedidos_Arequipa);
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
    
    public static double getVelocidadByRegiones(char reg1, char reg2){
        if(reg1 == 'C' && reg2 == 'C')
            return velocidadCostaCosta;
        if((reg1 == 'C' && reg2 == 'S') || (reg1 == 'S' && reg2 == 'C'))
            return velocidadCostaSierra;
        if(reg1 == 'S' && reg2 == 'S')
            return velocidadSierraSierra;
        if((reg1 == 'S' && reg2 == 'E') || (reg1 == 'E' && reg2 == 'S'))
            return velocidadSierraSelva;
        if(reg1 == 'E' && reg2 == 'E')
            return velocidadSelvaSelva;
        if((reg1 == 'C' && reg2 == 'E') || (reg1 == 'E' && reg2 == 'C'))
            return velocidadCostaSelva;
        return 60.0;
    }

    public static double getVelocidadByOficinas(Oficina current, Oficina destination){
        char regionInicio = current.getRegion();
        char regionFin = destination.getRegion();

        return getVelocidadByRegiones(regionInicio, regionFin);        
    }

    public static double getVelocidadByCodOficinas(String codOficinaInicio, String codOficinaFin){
        Oficina oficinaInicio = getOficinaByCodigo(codOficinaInicio);
        Oficina oficinaFin = getOficinaByCodigo(codOficinaFin);
        
        if(oficinaInicio != null && oficinaFin != null)
            return getVelocidadByOficinas(oficinaInicio, oficinaFin);
        
        return 60.0;
    }

    public static List<Pedido> getListaPedidosPorAlmacen (Oficina almacen) {
        if(hashMapPedidosAlmacen.get(almacen) != null)
            return hashMapPedidosAlmacen.get(almacen);
        return new ArrayList<Pedido>();
    }

    public static void cargarCamiones(String ruta) {
        listaCamiones = LoadData.leerCamionesYTipos(ruta);

        // Creación de listas de camiones para cada almacén
        List<Camion> lista_camiones_Trujillo = new ArrayList<>();
        List<Camion> lista_camiones_Lima     = new ArrayList<>();
        List<Camion> lista_camiones_Arequipa = new ArrayList<>();

        // Repartición de camiones para cada almacén
        for(Camion camion : listaCamiones){
            if(camion.getAlmacen().equals(getOficinaByCodigo("130101")))
                lista_camiones_Trujillo.add(camion);
            else if(camion.getAlmacen().equals(getOficinaByCodigo("150101")))
                lista_camiones_Lima.add(camion);
            else
                lista_camiones_Arequipa.add(camion);
        }

        // Guardado de listas de camiones por almacén en hashmap
        hashMapCamionesAlmacen.put(getOficinaByCodigo("130101"), lista_camiones_Trujillo);
        hashMapCamionesAlmacen.put(getOficinaByCodigo("150101"), lista_camiones_Lima);
        hashMapCamionesAlmacen.put(getOficinaByCodigo("040101"), lista_camiones_Arequipa);
    }
    
    public static List<Pedido> getListaPedidosPorOficina (Oficina of, List<Pedido> pedidos) {
        List<Pedido> pedTemp = new ArrayList<>();
        for(Pedido p: pedidos){
            if(of.getCodigo().equals(p.getOficina().getCodigo())){
                pedTemp.add(p);
            }
        }
        return pedTemp;
    }
}
