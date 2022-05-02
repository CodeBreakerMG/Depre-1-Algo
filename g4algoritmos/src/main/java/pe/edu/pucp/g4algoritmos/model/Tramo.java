package pe.edu.pucp.g4algoritmos.model;

public class Tramo {
    
    private long id; 
    private Oficina CiudadInicio; //Objeto Ciudad Inicio
    private Oficina CiudadFin;   //Objeto Ciudad Fin
    private double distancia;   //Distancia del recorrido de un camino a otro (SIRVE PARA EL CALCULO DEL COSTO)
    private double pesoRegion; //VALOR ENTERO QUE INDICA DE QUE REGION A QUE REGION ESTAMOS TRASLADANDONOS: costa-costa, costa-sierra, costa-selva, sierra-selva
                            //Si viajamos de costa a sierra, un dia mas de viaje. Si viajamos de costa a selva, 2 dias mas de viaje.
    private double pesoTiempo; //Tiempo total de recorrido del tramo.	 
    
    private int estado; /*  0: Bloqueado
                            1: Disponible 
                        */ 


    public Tramo(Oficina ciudadInicio, Oficina ciudadFin, double distancia, double pesoRegion, double pesoTiempo) {
        CiudadInicio = ciudadInicio;
        CiudadFin = ciudadFin;
        this.distancia = distancia;
        this.pesoRegion = pesoRegion;
        this.pesoTiempo = pesoTiempo;
        this.estado = 1; //activo, o disponible
    }

    //Constructor para con la carga de archivos.
    public Tramo(String codOficinaInicio, String codOficinaFin){

        CiudadInicio = Mapa.getOficinaByCodigo(codOficinaInicio);
        CiudadInicio = Mapa.getOficinaByCodigo(codOficinaFin);

        distancia = calcularDistancia();
        pesoRegion = calcularPesoRegion();
        pesoTiempo = (distancia/ Mapa.velocidadCamiones) + pesoRegion;

    }

    private double calcularPesoRegion() {
        return 0;
    }

    private double calcularDistancia() {
        
        double x1 = CiudadInicio.getCoordX();
        double y1 = CiudadInicio.getCoordY();
        double x2 = CiudadFin.getCoordX();
        double y2 = CiudadFin.getCoordY();

        //return Math.sqrt(x2-))
        return Math.sqrt(Math.pow((x2-x1),2) + Math.pow((y2-y1),2));

    }

    public double getCosto(){
        return pesoTiempo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Oficina getCiudadInicio() {
        return CiudadInicio;
    }

    public void setCiudadInicio(Oficina ciudadInicio) {
        CiudadInicio = ciudadInicio;
    }

    public Oficina getCiudadFin() {
        return CiudadFin;
    }

    public void setCiudadFin(Oficina ciudadFin) {
        CiudadFin = ciudadFin;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double getPesoRegion() {
        return pesoRegion;
    }

    public void setPesoRegion(double pesoRegion) {
        this.pesoRegion = pesoRegion;
    }

    public double getPesoTiempo() {
        return pesoTiempo;
    }

    public void setPesoTiempo(double pesoTiempo) {
        this.pesoTiempo = pesoTiempo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public void bloquear(){
        estado = 0;
    }

    public void desbloquear(){
        estado = 1;
    }

    public boolean estaBloqueado(){
        return (estado == 0);
    }

}
