package pe.edu.pucp.g4algoritmos.solucion2;


import java.util.Random;

import pe.edu.pucp.g4algoritmos.model.Oficina;


/*
Position of particle, solution of particle.
Because it's a lot of parameters, we must have it like this
*/

public class Position {
    
    private Oficina oficina; //Oficina en la que se dejará un pedido al menos (No incluye ciudades intermedias)
    private double randomPosition; /*Posición en la que se encuentra en la ruta potencial y de qué almacén viene
                                     Parte entera del numero: almacen del cual saldrá (0....Numero de almacenes-1)
                                     Parte decimal del numero: posicion en la que se encuentra, mas bajo... mas antes se llegará ahí.                             

    */
    public Position(Oficina oficina, double randomPosition) {
        this.oficina = oficina;
        this.randomPosition = randomPosition;
    }
    public Oficina getOficina() {
        return oficina;
    }
    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
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
    @Override
    public String toString() {
        return oficina + ", posicion=" + randomPosition + ", ";
    }

    
}
