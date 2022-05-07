package pe.edu.pucp.g4algoritmos;

import java.io.File;

import pe.edu.pucp.g4algoritmos.astar.AStarMain;
import pe.edu.pucp.g4algoritmos.model.Mapa;
import pe.edu.pucp.g4algoritmos.pso.PSOMain;
import pe.edu.pucp.g4algoritmos.solucion1.PrimeraSolucion;
import pe.edu.pucp.g4algoritmos.solucion2.SegundaSolucion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pe.edu.pucp.g4algoritmos.model.Camion;
import pe.edu.pucp.g4algoritmos.model.Mapa;
import pe.edu.pucp.g4algoritmos.model.Oficina;
import pe.edu.pucp.g4algoritmos.model.TipoCamion;
/*

DEPRE-1-ALGORITMOS
ODIPARPACK SW

CLASE DE JAVA PARA EJECUTAR EL PROYECTO (MAIN)
DESDE ESTE FILE SE TIENE QUE EJECUTAR TO DO.

 */
public class App 
{
    public static void main( String[] args )
    {

       // AStarMain.Main();


        //Función que llama a la primera solucion
        PrimeraSolucion sol1Trujillo = new PrimeraSolucion();
        PrimeraSolucion sol1Lima = new PrimeraSolucion();
        PrimeraSolucion sol1Arequipa = new PrimeraSolucion();

        //Primero, cargar la data al maestro MAPA
        final String sep = File.separator;
        Mapa.cargarAlmacenesYOficinas(System.getProperty("user.dir")+sep+"data"+sep+"inf226.oficinas.txt");
       //Mapa.cargarAlmacenesYOficinas(System.getProperty("user.dir")+sep+"data"+sep+"Nueva_Data"+sep+"inf226.oficinasv2.txt");

        Oficina alm = Mapa.getOficinaByCodigo("150101");
        //Oficina alm = Mapa.getOficinaByCodigo("130101");
        TipoCamion tipA = new TipoCamion(1, 'A', 90);
        TipoCamion tipB = new TipoCamion(2, 'B', 45);
        TipoCamion tipC = new TipoCamion(3, 'C', 30);
        Camion camion1 = new Camion(1, "A001", tipA, 0.0, 0.0, alm);
        Camion camion2 = new Camion(2, "A002", tipA, 0.0, 0.0, alm);
        Camion camion3 = new Camion(3, "A003", tipA, 0.0, 0.0, alm);
        Camion camion4 = new Camion(4, "A004", tipA, 0.0, 0.0, alm);
        Camion camion5 = new Camion(5, "A005", tipB, 0.0, 0.0, alm);
        Camion camion6 = new Camion(6, "A006", tipB, 0.0, 0.0, alm);
        Camion camion7 = new Camion(7, "A007", tipB, 0.0, 0.0, alm);
        Camion camion8 = new Camion(8, "A008", tipB, 0.0, 0.0, alm);
        Camion camion9 = new Camion(9, "A009", tipB, 0.0, 0.0, alm);
        Camion camion10 = new Camion(10, "A010", tipB, 0.0, 0.0, alm);
        Camion camion11 = new Camion(11, "A011", tipB, 0.0, 0.0, alm);
        Camion camion12 = new Camion(12, "A012", tipC, 0.0, 0.0, alm);
        Camion camion13 = new Camion(13, "A013", tipC, 0.0, 0.0, alm);
        Camion camion14 = new Camion(14, "A014", tipC, 0.0, 0.0, alm);
        Camion camion15 = new Camion(15, "A015", tipC, 0.0, 0.0, alm);
        Camion camion16 = new Camion(16, "A016", tipC, 0.0, 0.0, alm);
        Camion camion17 = new Camion(17, "A017", tipC, 0.0, 0.0, alm);
        Camion camion18 = new Camion(18, "A018", tipC, 0.0, 0.0, alm);
        Camion camion19 = new Camion(19, "A019", tipC, 0.0, 0.0, alm);
        Camion camion20 = new Camion(20, "A020", tipC, 0.0, 0.0, alm);

        List<Camion> listCamiones = new ArrayList<>();
        listCamiones.add(camion1);listCamiones.add(camion2);listCamiones.add(camion3);listCamiones.add(camion4);
        listCamiones.add(camion5);listCamiones.add(camion6);listCamiones.add(camion7);listCamiones.add(camion8);
        listCamiones.add(camion9);listCamiones.add(camion10);listCamiones.add(camion11);listCamiones.add(camion12);
        listCamiones.add(camion13);listCamiones.add(camion14);listCamiones.add(camion15);listCamiones.add(camion16);
        listCamiones.add(camion17);listCamiones.add(camion18);listCamiones.add(camion19);listCamiones.add(camion20);

        Mapa.setListaCamiones(listCamiones);
        
        Mapa.cargarTramos(System.getProperty("user.dir")+sep+"data"+sep+"inf226.tramos.v.2.0.txt");
        Mapa.cargarPedidos(System.getProperty("user.dir")+sep+"data"+sep+"inf226.ventas202202.txt");
        

        /*
        //Pruebas archivos ERICK

        Mapa.setListaCamiones(listCamiones);
        
        Mapa.cargarTramos(System.getProperty("user.dir")+sep+"data"+sep+"Nueva_Data"+sep+"inf226.tramos.v.1.0.txt");
        Mapa.cargarPedidos(System.getProperty("user.dir")+sep+"data"+sep+"Nueva_Data"+sep+"inf226.ventas202203.txt");

        */
        
        

        Mapa.setTramosToOficinas();
        Mapa.setTramosToAlmacenes();

        System.out.println(String.format("Cantidad de oficinas: %4d", Mapa.listaOficinas.size() + Mapa.listaAlmacenes.size()));
        System.out.println(String.format("Cantidad de tramos:   %4d", Mapa.listaTramos.size()));
        System.out.println(String.format("Cantidad de pedidos:  %4d", Mapa.listaPedidos.size()));
        System.out.println(String.format("Cantidad de camiones:  %4d", Mapa.listaCamiones.size()));

        //solucion.inicializar(Mapa.getListaPedidos(), alm);
        //-> Poner: solucion.inicializar(Mapa.getListaPedidosPorAlmacen(alm), alm) //Ejecutar para cada almacen
        
        sol1Lima.inicializar(Mapa.getListaPedidosPorAlmacen(Mapa.getOficinaByCodigo("150101")), Mapa.getOficinaByCodigo("150101"));
        System.out.println("LIMA OK      ==========================================================");
        
        sol1Trujillo.inicializar(Mapa.getListaPedidosPorAlmacen(Mapa.getOficinaByCodigo("130101")), Mapa.getOficinaByCodigo("130101"));
        System.out.println("TRUJILLO OK  ==========================================================");
        
        sol1Arequipa.inicializar(Mapa.getListaPedidosPorAlmacen(Mapa.getOficinaByCodigo("040101")), Mapa.getOficinaByCodigo("040101"));
        System.out.println("AREQUIPA OK  ==========================================================");
        

        System.out.println("==========================================================");
        System.out.println("");
        System.out.println("");
        System.out.println("==========================================================");


        SegundaSolucion solPSO = new SegundaSolucion();
        solPSO.inicializar(Mapa.listaPedidos, Mapa.listaAlmacenes);

        //Función que llama al PSO:
        //PSOMain.Main();
    }
}
