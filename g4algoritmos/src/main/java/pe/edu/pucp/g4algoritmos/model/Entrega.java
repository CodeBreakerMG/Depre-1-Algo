package pe.edu.pucp.g4algoritmos.model;

import java.util.Date;

public class Entrega {

    private long id;    //Id del pedido
    private String codigo; //Código de la entrega
    private Pedido pedido;  //Pedido referido
    private int cantidad; //Cantidad de paquetes de la entrega. No puede ser mayor que la cantidad actual del pedido

    private Date fechaHoraSalida; //fecha en la que salió del almacén
    private Date fechaHoraEntrega; //fecha en la que llegará a su destino
    
    private int estado; /*  0: No iniciado
                            1: En Proceso
                            2: Completado/Entregado
                            3: Postergado (delayed)
                            4: Anulado
                            5: Cancelado    
                        */

    public Entrega(String codigo, Pedido pedido, int cantidad, Date fechaHoraSalida, Date fechaHoraEntrega) {
        this.codigo = codigo;
        this.pedido = pedido;
        this.cantidad = cantidad;
        this.fechaHoraSalida = fechaHoraSalida;
        this.fechaHoraEntrega = fechaHoraEntrega;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(Date fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public Date getFechaHoraEntrega() {
        return fechaHoraEntrega;
    }

    public void setFechaHoraEntrega(Date fechaHoraEntrega) {
        this.fechaHoraEntrega = fechaHoraEntrega;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    
}
