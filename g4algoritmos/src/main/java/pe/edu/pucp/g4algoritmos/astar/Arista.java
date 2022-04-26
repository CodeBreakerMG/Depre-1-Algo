package pe.edu.pucp.g4algoritmos.astar;

import pe.edu.pucp.g4algoritmos.model.Tramo;

public class Arista {

    private Tramo tramo;
    private VertexOficina target;    

    public Arista(Tramo tramo, VertexOficina target) {
        this.tramo = tramo;
        this.target = target;
    }

    public Tramo getTramo() {
        return tramo;
    }

    public void setTramo(Tramo tramo) {
        this.tramo = tramo;
    }

    public VertexOficina getTarget() {
        return target;
    }

    public void setTarget(VertexOficina target) {
        this.target = target;
    }

    public double getCosto(){
        return this.getTramo().getPesoTiempo();
    }




}
