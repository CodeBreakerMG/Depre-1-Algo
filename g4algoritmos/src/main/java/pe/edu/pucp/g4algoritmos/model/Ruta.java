package pe.edu.pucp.g4algoritmos.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

//Plan de transporte o Ruta

public class Ruta {

    private long id;
    private String codigo;
    private Camion camion;
    
    private double coordX; //coordX: (Latitud ACTUAL, coordenada x, ejem: -13.51802722)
    private double coordY; //(Longitud ACTUAL, coordenada y, ejem: 73.51802722)

    private List<Entrega> listaEntregas = new ArrayList<>();
    private List<Pedido> listaPedidos = new ArrayList<>();
    private List<Tramo>  listaTramos;
    private List<List<Tramo>>  listaTramosPorOficina = new ArrayList<>();
    private List<Oficina> listaOficinas = new ArrayList<>();
    private List<Date> fechaEstimadaLlegadaOficinas = new ArrayList<>();

    private int tramoActual;  //Indice el cual indica el tramo en el que se encuentra. 

    private double costoTotal;	//HORAS. CostoTotal de la ruta, la variable es tiempo, aparentemente
    private double costoAcumulado;	//HORAS. //Costo de la ruta hasta la actualidad

    private double cargaTotal; //Carga Total 
    
    private int estado;	//1: Planificada, 2: En curso, anulada, cancelada, Completada

    private Date fechaHoraInicio; // fecha en la que comenzó el plan de transporte
    private Date fechaHoraCompletado; //ffecha en la que se culminó el plan de transporte

    public Ruta(){

    }

    public Ruta(String codigo, Camion camion, double coordX, double coordY, ArrayList<Entrega> listaEntregas,
        ArrayList<Tramo> listaTramos) {

        this.codigo = codigo;
        this.camion = camion;
        this.coordX = coordX;
        this.coordY = coordY;
        this.listaEntregas = listaEntregas;
        this.listaTramos = listaTramos;
        this.tramoActual = 0;
        this.costoTotal = calcularCostoTotal();
        this.costoAcumulado = 0;
    }

    public Ruta(String codigo, Camion camion, Oficina almacen){
        this.codigo = codigo;
        this.camion = camion;
        this.coordX = almacen.getCoordX();
        this.coordY = almacen.getCoordY();
        this.estado = 1;
        this.camion.setEstado(2);
        this.tramoActual = 0;
        this.costoTotal = 0;
        this.costoAcumulado = 0;
        this.listaTramos = new ArrayList<>();
    }

    public double calcularCostoTotal(){
        this.costoTotal = 0;
        for (Tramo tramo : this.listaTramos)
            this.costoTotal += tramo.getPesoTiempo();
        return this.costoTotal;
    }


    public void setCosto(double c){
        this.costoTotal = c;
    }

    public double getCosto(){
        return this.costoTotal;
    }
    
    public double getCargaTotal() {
        return cargaTotal;
    }

    public void setCargaTotal(double cargaTotal) {
        this.cargaTotal = cargaTotal;
    }

    public double calcularCostoTotal(List<Tramo> tram){
        this.costoTotal = 0;
        for (Tramo tramo : tram)
            this.costoTotal += tramo.getPesoTiempo();
        return this.costoTotal;
    }

    public double calcularCostoAcumulado(){
        this.costoAcumulado = 0;
        for (int i = 0; i < tramoActual; i++)
            this.costoAcumulado += this.listaTramos.get(i).getPesoTiempo();
        return this.costoAcumulado;
    }
    
    public void proceder(){
        costoAcumulado += this.listaTramos.get(this.tramoActual).getPesoTiempo();
        this.tramoActual++;
    }

    public boolean yaTermino(){
        return (tramoActual >= listaTramos.size());
    }

    public List<Pedido> getListaPedidos() {
        return listaPedidos;
    }

    public void setListaPedidos(List<Pedido> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Date getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public List<Date> getFechaEstimadaLlegadaOficinas() {
        return fechaEstimadaLlegadaOficinas;
    }

    public void setFechaEstimadaLlegadaOficinas(List<Date> fechaEstimadaLlegadaOficinas) {
        this.fechaEstimadaLlegadaOficinas = fechaEstimadaLlegadaOficinas;
    }

    public void setFechaHoraInicio(Date fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public Date getFechaHoraCompletado() {
        return fechaHoraCompletado;
    }

    public void setFechaHoraCompletado(Date fechaHoraCompletado) {
        this.fechaHoraCompletado = fechaHoraCompletado;
    }

    public List<Tramo> getListaTramos() {
        return listaTramos;
    }

    public void setListaTramos(List<Tramo> listaTramos) {
        this.listaTramos = listaTramos;
    }

    public List<Oficina> getListaOficinas() {
        return listaOficinas;
    }

    public void setListaOficinas(List<Oficina> listaOficinas) {
        this.listaOficinas = listaOficinas;
    }

    public void addTramos (List<Tramo> tramos){
        this.listaTramos.addAll(tramos);
    }

    public List<List<Tramo>> getListaTramosPorOficina() {
        return listaTramosPorOficina;
    }

    public void setListaTramosPorOficina(List<List<Tramo>> listaTramosPorOficina) {
        this.listaTramosPorOficina = listaTramosPorOficina;
    }

    public void setCamion(Camion c){
        this.camion = c;
    }

    public Camion getCamion(){
        return camion;
    }

    public String toEntry(){
        String output = "";
        
        String oficinas = "";

        for (Oficina o : listaOficinas)
            oficinas = oficinas + o.getCodigo() + " - "+ o.getProvincia() + "\n";

        output = String.valueOf(id) + "," +
                 camion.getCodigo() + "," +
                 //fechaHoraInicio.toString() + "," +
                 //fechaHoraCompletado.toString() + "," +
                 String.valueOf(costoTotal) + "," +
                 String.valueOf(cargaTotal) + "\n\n" +
                 "Oficinas : \n" + oficinas;
        return output;
    }
}
