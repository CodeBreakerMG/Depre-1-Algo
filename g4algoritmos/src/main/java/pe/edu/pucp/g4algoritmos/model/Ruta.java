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
    private List<Tramo> listaTramos = new ArrayList<>();
    private List<Oficina> listaOficinas = new ArrayList<>();

    private int tramoActual;  //Indice el cual indica el tramo en el que se encuentra. 

    private double costoTotal;	//HORAS. CostoTotal de la ruta, la variable es tiempo, aparentemente
    private double costoAcumulado;	//HORAS. //Costo de la ruta hasta la actualidad

    
    private int estado;	//Planificada, En curso, anulada, cancelada, Completada

    private Date fechaHoraInicio; // fecha en la que comenzó el plan de transporte
    private Date fechaHoraCompletado; //ffecha en la que se culminó el plan de transporte

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

    public double calcularCostoTotal(){
        this.costoTotal = 0;
        for (Tramo tramo : this.listaTramos)
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


}
