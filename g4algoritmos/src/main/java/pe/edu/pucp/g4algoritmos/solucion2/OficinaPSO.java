package pe.edu.pucp.g4algoritmos.solucion2;

import java.util.List;

import java.util.ArrayList;
import java.util.Date;

import pe.edu.pucp.g4algoritmos.model.Oficina;
import pe.edu.pucp.g4algoritmos.model.Pedido;

public class OficinaPSO {

    private Oficina oficina; //Oficina en la que se dejará un pedido al menos (No incluye ciudades intermedias)
    private List<Pedido> pedidos; //Pedidos de la Oficina
    private Date l;                 //l: tiempoMaximoLlegada
    private Date e;                 //e: tiempoMinimo de salida del camion o enrutamiento para esta ciudad //Ultimo pedido registrado
    private int q;                  //q: cantidad de paquetes A

    public OficinaPSO(Oficina oficina) {
        this.oficina = oficina;
        this.pedidos = new ArrayList<>();
        q = 0;
        l = new Date(9 * (long)10e13);
        e = new Date(1 * (long)10e13);
    }

    public OficinaPSO() {
        this.pedidos = new ArrayList<>();
    }

    public Oficina getOficina() {
        return oficina;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public String getCodigo(){
        return oficina.getCodigo();
    }

    public String getProvincia(){
        return oficina.getProvincia();
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public void addPedido(Pedido pedido){
        this.pedidos.add(pedido);
    }

    public int getCantidadPedidos(){
        return this.pedidos.size();
    }

    public Date getL() {
        return l;
    }

    public void setL(Date l) {
        this.l = l;
    }

    
    public Date getE() {
        return e;
    }

    public void setE(Date e) {
        this.e = e;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public Date tiempoLimiteLlegada() {
        return l;
    }

    public void setTiempoLimiteLlegada(Date l) {
        this.l = l;
    }


    public Date tiempoMinimoSalidaCamion() {
        return e;
    }

    public void setTiempoMinimoSalidaCamion(Date e) {
        this.e = e;
    }


    public int cantidadPaquetes() {
        return q;
    }

    public void setCantidadPaquetes(int q) {
        this.q = q;
    }

    public void addPaquetes(int q){
        this.q += q;
    }

    @Override
    public String toString() {
        return oficina.getProvincia();
    }


    
}
