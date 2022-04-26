package pe.edu.pucp.g4algoritmos.model;

/*Ejemplo de la clase CAMION*/

public class Camion {

    private long id;    //Id del camion en la base de datos
    private String codigo; //Código del camión
    private TipoCamion tipo;  //Tipo del camion: A, B o C. Devuelve el objeto camion
    private double coordX; //coordX: (Latitud ACTUAL, coordenada x, ejem: -13.51802722)
    private double coordY; //(Longitud ACTUAL, coordenada y, ejem: 73.51802722)
    private int estado; //Estado del camion. Código entero que significa algo
    
    
    public Camion(long id, TipoCamion tipo, double coordX, double coordY, int estado) {
        this.id = id;
        this.tipo = tipo;
        this.coordX = coordX;
        this.coordY = coordY;
        this.estado = estado;
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


    public TipoCamion getTipo() {
        return tipo;
    }


    public void setTipo(TipoCamion tipo) {
        this.tipo = tipo;
    }


    public double getCoordX() {
        return coordX;
    }


    public void setCoordX(double coordX) {
        this.coordX = coordX;
    }


    public double getCoordY() {
        return coordY;
    }


    public void setCoordY(double coordY) {
        this.coordY = coordY;
    }


    public int getEstado() {
        return estado;
    }


    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int capacidad(){
        return (int)this.tipo.getCapacidad();
    }

}
