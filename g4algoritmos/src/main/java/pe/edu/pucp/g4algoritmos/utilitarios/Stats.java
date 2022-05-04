package pe.edu.pucp.g4algoritmos.utilitarios;

import java.util.Collection;

public class Stats {
    
    

    public static double suma(double [] arr) {
        if(arr.length == 0) return Double.NaN;
        double suma = 0.0;
        for(double x : arr){
            suma += x;
        }
        return suma;
    }

    public static double media(double [] arr) {
        if(arr.length == 0) return Double.NaN;
        return suma(arr) / arr.length;
    }

    public static double varianza(double [] arr) {
        if(arr.length == 0) return Double.NaN;
        double media = media(arr);
        double suma = 0.0;
        for(double x : arr){
            suma += (x - media) * (x - media);
        }
        return suma / arr.length;
    }

    public static double desvest(double [] arr) {
        if(arr.length == 0) return Double.NaN;
        return Math.sqrt(varianza(arr));
    }

    public static double desvest(Collection<Integer> collection){
        Integer[] arrInt = collection.toArray(new Integer[collection.size()]);
        double[] arrDouble = new double[arrInt.length];
        for(int i=0; i<arrInt.length; i++)
            arrDouble[i] = Double.valueOf(arrInt[i]);
        return desvest(arrDouble);
    }
}
