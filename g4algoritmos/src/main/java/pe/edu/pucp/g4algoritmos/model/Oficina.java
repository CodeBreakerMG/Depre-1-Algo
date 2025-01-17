package pe.edu.pucp.g4algoritmos.model;

import java.util.ArrayList;
import java.util.List;
import org.javatuples.LabelValue;


import pe.edu.pucp.g4algoritmos.astar.AStarModified;
import pe.edu.pucp.g4algoritmos.astar.AStarOficina;
import pe.edu.pucp.g4algoritmos.astar.VertexOficina;

/*
ALMACENES PRINCIPALES:

COSTA-NORTE: 130101,LA LIBERTAD,TRUJILLO,-8.11176389,-79.02868652,COSTA
COSTA-CENTRO: 150101,LIMA,LIMA,-12.04591952,-77.03049615,COSTA
COSTA-SUR: 040101,AREQUIPA,AREQUIPA,-16.39881421,-71.537019649,COSTA

*/

public class Oficina {

    private long id;    //Id en la base de datos
    private String codigo; //Código de la oficina
    private String departamento; //Nombre Departamento (ejem:  AREQUIPA)
    private String provincia; //Nombre Provincia (ejem: CAMANA)
    private char region;  //C: Costa, S: Sierra, E: Selva
    private double coordX; //(Longitud ACTUAL en grados, coordenada x, ejem: 73.51802722)
    private double coordY; //(Latitud  ACTUAL en grados, coordenada y, ejem:-13.51802722)
    private int estado; //Estado de la Oficina
    private int esAlmacen; //0: No es almacen, 1: Es almacén

    /*PARAMETROS A**/
    private List<Tramo> listaTramos; //Aristas desde aqui hasta alla.

    private double g; //Cost from start to here
    private double h; //Cost from here to target, heuristically calculated
    private double f;
    //track de adjacency list (neighbors)

    //it Tracks the previous node in the shortest path
    private Oficina parent;
 


    
    public Oficina(long id, String codigo, String departamento, String provincia, char region, double coordX,
            double coordY, int esAlmacen) {
        this.id = id;
        this.codigo = codigo;
        this.departamento = departamento;
        this.provincia = provincia;
        this.region = region;
        this.coordX = coordX;
        this.coordY = coordY;
        this.estado = 1;
        this.esAlmacen = esAlmacen;

        //Parametros A*
        g = 0;
        f = 0;
        h = 0;
        parent = null;
    }


    public List<Tramo> obtenerTramos(){
        List<Tramo> auxListaAristas = Mapa.getTramosByOficinaInicio(this.getCodigo());
        return auxListaAristas;
    }

    public void calculateTramos(){
        this.listaTramos = Mapa.getTramosByOficinaInicio(this.getCodigo());
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


    public boolean EsAlmacen() {
        return (esAlmacen == 1) ;
    }


    public void setEsAlmacen(int estado) {
        this.estado = estado;
    }

    public void resetAstar(){
        g = 0;
        f = 0;
        h = 0;
        parent = null;
    }

    

    public int getEsAlmacen() {
        return esAlmacen;
    }


    public List<Tramo> getListaTramos() {
        return listaTramos;
    }


    public void setListaTramos(List<Tramo> listaTramos) {
        this.listaTramos = listaTramos;
    }


    public double getG() {
        return g;
    }


    public void setG(double g) {
        this.g = g;
    }


    public double getH() {
        return h;
    }


    public void setH(double h) {
        this.h = h;
    }


    public double getF() {
        return f;
    }


    public void setF(double f) {
        this.f = f;
    }


    public Oficina getParent() {
        return parent;
    }


    public void setParent(Oficina parent) {
        this.parent = parent;
    }


    public List<Tramo> recorridoHasta(Oficina destino){
        
        //Devuelve los tramos a recorrer
        //LabelValue<Double,List<Tramo>> listaYCosto = new LabelValue<Double,List<Tramo>>(label, value)

        

        List<Tramo> tramos;
        Tramo tramo = Mapa.getTramoByOficinas(this.getCodigo(), destino.getCodigo()) ;
        if ( tramo != null && tramo.estaBloqueado() == false){
            tramos = new ArrayList<>();
            tramos.add(tramo);
            return tramos;
        }

        AStarOficina Astar = new AStarOficina(this, destino);
        Astar.run();
        tramos = Astar.getTramosRecorrer();

        /*
        double costo = 0;

        for (Tramo tramo : tramos)
            costo += tramo.getCosto();

        */
        return tramos;
        
    }
    
    public double costoHasta(Oficina destino){
        
        //Devuelve el COSTO del recorridos
        //LabelValue<Double,List<Tramo>> listaYCosto = new LabelValue<Double,List<Tramo>>(label, value)

        

        
        Tramo tramo = Mapa.getTramoByOficinas(this.getCodigo(), destino.getCodigo()) ;
        if ( tramo != null && tramo.estaBloqueado() == false){
            return tramo.getCosto();
        }

        double costo;
        AStarOficina Astar = new AStarOficina(this, destino);
        Astar.run();
        costo = Astar.getCost();

        return costo;

    }

    @Override
    public String toString() {
        return "C: " + provincia  + "-> ";
    }

    
}
