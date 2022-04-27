package pe.edu.pucp.g4algoritmos.model;

public class Oficina {

    private long id;    //Id en la base de datos
    private String codigo; //Código de la oficina
    private String departamento; //Nombre Departamento (ejem:  AREQUIPA)
    private String provincia; //Nombre Provincia (ejem: CAMANA)
    private char region;  //C: Costa, S: Sierra, E: Selva
    private double coordX; //coordX: (Latitud ACTUAL, coordenada x, ejem: -13.51802722)
    private double coordY; //(Longitud ACTUAL, coordenada y, ejem: 73.51802722)
    private int estado; //Estado de la Oficina

    
    public Oficina(long id, String codigo, String departamento, String provincia, char region, double coordX,
            double coordY, int estado) {
        this.id = id;
        this.codigo = codigo;
        this.departamento = departamento;
        this.provincia = provincia;
        this.region = region;
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


    public String getDepartamento() {
        return departamento;
    }


    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }


    public String getProvincia() {
        return provincia;
    }


    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }


    public char getRegion() {
        return region;
    }


    public void setRegion(char region) {
        this.region = region;
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
    

    
}
