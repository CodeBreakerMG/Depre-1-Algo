package pe.edu.pucp.g4algoritmos.model;

import java.util.Date;

public class Bloqueo {
    
    private Tramo tramo;
    private Date fechaHoraInicio;
    private Date fechaHoraFin;
    
    //Para la carga a través de archivos
    public Bloqueo(String codOficinaInicio, String codOficinaFin, Date fechaHoraInicio, Date fechaHoraFin) {

        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;

        this.tramo = getTramo(codOficinaInicio, codOficinaFin);
    }

    public Tramo getTramo(String codOficinaInicio, String codOficinaFin) {
        
        return Mapa.getTramoByOficinas(codOficinaInicio, codOficinaFin);

    }

    public Tramo getTramo() {
        return tramo;
    }
    public void setTramo(Tramo tramo) {
        this.tramo = tramo;
    }
    public Date getFechaHoraInicio() {
        return fechaHoraInicio;
    }
    public void setFechaHoraInicio(Date fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }
    public Date getFechaHoraFin() {
        return fechaHoraFin;
    }
    public void setFechaHoraFin(Date fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }


}
