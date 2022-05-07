package pe.edu.pucp.g4algoritmos.model;

import java.util.Date;

public class Pedido {

    private long id;    //Id del pedido
    private String codigo; //Código del pedido (total)
    private int cantidadTotal; //Cantidad de Paquetes Total
    private int cantidadActual; //Cantidad de Paquetes Actual. Si llega a 0, se entrego.
    private Date fechaHoraPedido; // Fecha en la que se realizo el pedido
    private Date fechaHoraLimite; //Fecha y hora limite en la que se entregará el pedido...
    private Date fechaHoraCompletado; //Fecha y hora en la que se completo el pedido
    private Oficina oficina;    //Oficina de Destino
    
    private int estado; /*  0: No iniciado
                            1: En Proceso
                            2: Completado/Entregado
                            3: Postergado (delayed)
                            4: Anulado
                            5: Cancelado    
                        */

    private String codPedidoUnitario; // Código del pedido unitario (entrega de un paquete)

    //Para carga con archivo txt
    public Pedido(String codigo,  int cantidadTotal, Date fechaHoraPedido, Date fechaHoraLimite, String codOficina, String codCliente, String codPedidoUnitario) {
        this.codigo = codigo;
        this.cantidadTotal = cantidadTotal;
        this.fechaHoraPedido = fechaHoraPedido;
        this.fechaHoraLimite = fechaHoraLimite;
        this.oficina = Mapa.getOficinaByCodigo(codOficina);
        this.estado = 0;
        this.codPedidoUnitario = codPedidoUnitario;
    }

    


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getCodigo() {
        return codigo;
    }


    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }


    public int getCantidadTotal() {
        return cantidadTotal;
    }


    public void setCantidadTotal(int cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }


    public int getCantidadActual() {
        return cantidadActual;
    }


    public void setCantidadActual(int cantidadActual) {
        this.cantidadActual = cantidadActual;
    }


    public Date getFechaHoraPedido() {
        return fechaHoraPedido;
    }


    public void setFechaHoraPedido(Date fechaHoraPedido) {
        this.fechaHoraPedido = fechaHoraPedido;
    }


    public Date getFechaHoraLimite() {
        return fechaHoraLimite;
    }


    public void setFechaHoraLimite(Date fechaHoraLimite) {
        this.fechaHoraLimite = fechaHoraLimite;
    }


    public Date getFechaHoraCompletado() {
        return fechaHoraCompletado;
    }


    public void setFechaHoraCompletado(Date fechaHoraCompletado) {
        this.fechaHoraCompletado = fechaHoraCompletado;
    }


    public Oficina getOficina() {
        return oficina;
    }


    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }


    public int getEstado() {
        return estado;
    }


    public void setEstado(int estado) {
        this.estado = estado;
    } 


    public String getCodPedidoUnitario() {
        return codPedidoUnitario;
    }


    public void setCodPedidoUnitario(String codPedidoUnitario) {
        this.codPedidoUnitario = codPedidoUnitario;
    }
                        
    
}
