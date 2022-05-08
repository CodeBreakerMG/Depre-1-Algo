package pe.edu.pucp.g4algoritmos.model;

/*Ejemplo de la clase CAMION*/

public class Camion {

    private long id;    /*Id del camion en la base de datos*/
    private String codigo; //Código del camión
    private TipoCamion tipo;  //Tipo del camion: A, B o C. Devuelve el objeto camion
    private double coordX; //coordX: (Latitud ACTUAL, coordenada x, ejem: -13.51802722)
    private double coordY; //(Longitud ACTUAL, coordenada y, ejem: 73.51802722)
    private int estado; //Estado del camion. 
                /*0: Inactivo
                  1: Activo, en un almacén
                  2: Ocupado, seleccionado para ruta
                  3: Activo, en ruta
                  4: en Mantenimiento, Oficina
                  5: en Mantenimiento, Programado,
                  6: Averiado

                */

    private Oficina almacen; //Almacen en el cual se encuentra o partió inicialmente. 
    
    
    public Camion(long id,String codigo, TipoCamion tipo, double coordX, double coordY,   Oficina almacen ) {
        this.id = id;
        this.codigo = codigo;
        this.tipo = tipo;
        this.coordX = coordX;
        this.coordY = coordY;
        this.estado = 1;
        this.almacen = almacen;
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
    /*  0: Inactivo
        1: Activo, en un almacén
        2: Activo, en ruta
        3: en Mantenimiento, Oficina
        4: en Mantenimiento, Programado,
        5: Averiado
    */
        return estado;
    }


    public void setEstado(int estado) {

    /*  0: Inactivo
        1: Activo, en un almacén
        2: Activo, en ruta
        3: en Mantenimiento, Oficina
        4: en Mantenimiento, Programado,
        5: Averiado
    */ 

        this.estado = estado;
    } 

    public Oficina getAlmacen() {
        return almacen;
    }


    public void setAlmacen(Oficina almacen) {
        this.almacen = almacen;
    }


    public int capacidad(){
        return (int)this.tipo.getCapacidad();
    }


    @Override
    public String toString() {
        return "Camion [almacen=" + almacen.getProvincia() + "id=" + id + ", codigo=" + codigo + ", tipo=" + tipo.getCodigo() + "]\n";
    }

}
