package pe.edu.pucp.g4algoritmos.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Mapa {

    public static final double limiteXInferior = 0.0; //Coordenadas limite inferior X
    public static final double limiteXSuperior = 0.0; //Coordenadas superior inferior X
    public static final double limiteYInferior = 0.0; //Coordenadas limite inferior Y
    public static final double limiteYSuperior = 0.0; //Coordenadas superior inferior Y
    public static final double velocidadCamiones = 60.0; //Velocidad Promedio camiones (KM/H)

    public static double duracionMantenimiento = 1.0; //Duración del mantenimiento en Horas

    public Date fechaHoraComienzo;//DateTime de la fecha de comienzo de la simulacion
    public Date fechaHoraActual; //DateTime de la fecha actual de la simulacion
    public Date fechaHoraFin; //DateTime de la fecha FIN de la simulacion

    public static List<Oficina> listaOficinas = new ArrayList<>();
    public static List<Tramo> listaTramos = new ArrayList<>();
    public static List<Camion> listaCamiones = new ArrayList<>();


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

    public void setListaOficinas(List<Oficina> listaOficinas) {
        this.listaOficinas = listaOficinas;
    }

    public List<Tramo> getListaTramos() {
        return listaTramos;
    }

    public static Oficina getOficinaByCodigo(String codigoOficina){
        for (Oficina oficina : listaOficinas)
            if (oficina.getCodigo()  == codigoOficina)
                return oficina;
        
        return null;
        
    }

    public static Camion getCamionByCodigo(String codigoCamion){
        for (Camion camion : listaCamiones)
            if (camion.getCodigo()  == codigoCamion)
                return camion;
        
        return null;
        
    }

    public static Tramo getTramoByOficinas(String codigoOficinaInicio, String codigoOficinaFin){
        
        for (Tramo tramo : listaTramos)
            if (tramo.getCiudadInicio().getCodigo() == codigoOficinaInicio && tramo.getCiudadFin().getCodigo() == codigoOficinaFin)
                return tramo;
        
        return null;
    }

    public static List<Tramo> getTramosByOficinaInicio(String codigoOficina){
        
        List<Tramo> tramos = new ArrayList<>();

        for (Tramo tramo : listaTramos)
            if (tramo.getCiudadInicio().getCodigo() == codigoOficina)
                tramos.add(tramo);

        return tramos;
    }

    

    public void setListaTramos(List<Tramo> listaTramos) {
        this.listaTramos = listaTramos;
    }

 
    
}
