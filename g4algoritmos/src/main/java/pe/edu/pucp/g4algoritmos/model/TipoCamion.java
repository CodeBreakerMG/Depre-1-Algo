package pe.edu.pucp.g4algoritmos.model;

public class TipoCamion {


    private int id;         //Id Tipo Camion
    private char codigo;    //Codigo del tipo camion: A,B ó C
    private long capacidad; //Capacidad del tipo de camion: en paquetes

    public TipoCamion(int id, char codigo, long capacidad) {
        this.id = id;
        this.codigo = codigo;
        this.capacidad = capacidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char getCodigo() {
        return codigo;
    }

    public void setCodigo(char codigo) {
        this.codigo = codigo;
    }

    public long getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(long capacidad) {
        this.capacidad = capacidad;
    }


}
