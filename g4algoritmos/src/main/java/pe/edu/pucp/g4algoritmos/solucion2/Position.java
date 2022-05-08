package pe.edu.pucp.g4algoritmos.solucion2;


import pe.edu.pucp.g4algoritmos.model.Camion;
import pe.edu.pucp.g4algoritmos.model.Oficina;
import java.util.Date;
import java.util.Random;


/*
Position of particle, solution of particle.
Because it's a lot of parameters, we must have it like this
*/

public class Position {
    
    private OficinaPSO oficina; //Oficina en la que se dejará un pedido al menos (No incluye ciudades intermedias)
    private double randomPosition; /*Posición en la que se encuentra en la ruta potencial y de qué almacén viene
                                     Parte entera del numero: almacen del cual saldrá (0....Numero de almacenes-1)
                                     Parte decimal del numero: posicion en la que se encuentra, mas bajo... mas antes se llegará ahí.                             
    */
    private double camionNumber; //Camion que servirá a la oficina. camionNumber = "almacen"."camion del almacen"... "camion del almacen" : parte decimal con solo 2 decimales

    //AQUÍ TAMBIEN QUIZÁ SE PODRÍA AGREGAR QUE CAMION UTILIZARÁ XD

    public Position(OficinaPSO oficina, double randomPosition, double camionNumber) {
        this.oficina = oficina;
        this.randomPosition = randomPosition;
        this.camionNumber = camionNumber;
    }

    public OficinaPSO getOficinaPSO() {
        return oficina;
    }

    public void setOficinaPSO(OficinaPSO oficinaPSO) {
        this.oficina = oficinaPSO;
    }

    public Oficina getOficina() {
        return oficina.getOficina();
    }

    public double getCamionNumber() {
        return camionNumber;
    }

    public int getDepot(){
        return (int)randomPosition; //Devolviendo parte entera
    }

    public int getVehiclePosition() {
        
        double decimalPart = camionNumber % 1;
        int asWhole = (int)decimalPart * 100;
        return asWhole;
    }

    public void setCamionNumber(double camionNumber) {
        this.camionNumber = camionNumber;
    }

    public Date getL(){
        //L: tiempo maximo en la que debe llegar el camion a esta oficina
        return oficina.getL();
    }

    public int getCantidadPedidos(){
        return oficina.getCantidadPedidos();
    }

    public int cantidadPaquetes(){
        return oficina.cantidadPaquetes();
    }

    public double getRandomPosition() {
        return randomPosition;
    }
    public void setRandomPosition(double randomPosition) {
        this.randomPosition = randomPosition;
    }
    
    public double randomize(double min, double max){
        return this.randomPosition =  min + (max-min) * Math.random();
    }

    public double randomize(double numAlmacenes){
        return this.randomPosition =  0 + (numAlmacenes-0) * Math.random();
    }

    public double randomizeCamion(int randomAlmacen, RepositoryPSO repository) {
        Random rand = new Random();
        int min = 0;
        int max = repository.cantidadCamionesPorAlmacen(randomAlmacen) - 1;
        double randomNum = (double) (rand.nextInt((max - min) + 1) + min);

        return  this.camionNumber = randomAlmacen + (randomNum / 100);
    }

    
    @Override
    public String toString() {
        return oficina + ", posicion=" + randomPosition + ", ";
    }

    
}
