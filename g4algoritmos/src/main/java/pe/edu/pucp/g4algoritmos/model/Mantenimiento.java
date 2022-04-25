package pe.edu.pucp.g4algoritmos.model;

import java.util.Date;

public class Mantenimiento {
    
    private long id; 
    private String codigoCamion; //Código del camión que afectará
    private Date fechaHoraInicio;   //Fecha y hora del mantenimiento programado
    private int estado ;            //0: No iniciado, 1: en proceso, 2: Completado
    
    public Mantenimiento(String codigoCamion, Date fechaHoraInicio) {
        this.codigoCamion = codigoCamion;
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public Date getFechaHoraFin(){

        return AuxiliaryFunctions.addHoursToJavaUtilDate(this.fechaHoraInicio, Mapa.duracionMantenimiento);
    }

    public void entrarMantenimiento(){

        if (AuxiliaryFunctions.compareDatesByMinutes(fechaHoraInicio, AuxiliaryFunctions.getNowTime()) == 0) {
            this.estado = 1;
            Mapa.getCamionByCodigo(this.codigoCamion).setEstado(2);
        }
    }
    
    public void salirMantenimiento(){

        if (AuxiliaryFunctions.compareDatesByMinutes(getFechaHoraFin(), AuxiliaryFunctions.getNowTime()) > 1 ) {
            this.estado = 2;
            Mapa.getCamionByCodigo(this.codigoCamion).setEstado(1);
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodigoCamion() {
        return codigoCamion;
    }

    public void setCodigoCamion(String codigoCamion) {
        this.codigoCamion = codigoCamion;
    }

    public Date getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(Date fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }
 

    

}

