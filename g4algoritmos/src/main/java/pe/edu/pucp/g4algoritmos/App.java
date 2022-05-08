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

        Oficina alm1 = Mapa.getOficinaByCodigo("150101");
        Oficina alm2= Mapa.getOficinaByCodigo("130101");
        Oficina alm3= Mapa.getOficinaByCodigo("040101");
        //Oficina alm = Mapa.getOficinaByCodigo("130101");
        TipoCamion tipA = new TipoCamion(1, 'A', 90);
        TipoCamion tipB = new TipoCamion(2, 'B', 45);
        TipoCamion tipC = new TipoCamion(3, 'C', 30);
        Camion camion1 = new Camion(1, "A001", tipA, 0.0, 0.0, alm1);
        Camion camion2 = new Camion(2, "A002", tipA, 0.0, 0.0, alm1);
        Camion camion3 = new Camion(3, "A003", tipA, 0.0, 0.0, alm1);
        Camion camion4 = new Camion(4, "A004", tipA, 0.0, 0.0, alm1);
        Camion camion5 = new Camion(5, "A005", tipA, 0.0, 0.0, alm2);
        Camion camion6 = new Camion(6, "A006", tipA, 0.0, 0.0, alm3);
        Camion camion7 = new Camion(7, "B001", tipB, 0.0, 0.0, alm1);
        Camion camion8 = new Camion(8, "B002", tipB, 0.0, 0.0, alm1);
        Camion camion9 = new Camion(9, "B003", tipB, 0.0, 0.0, alm1);
        Camion camion10 = new Camion(10, "B004", tipB, 0.0, 0.0, alm1);
        Camion camion11 = new Camion(11, "B005", tipB, 0.0, 0.0, alm1);
        Camion camion12 = new Camion(12, "B006", tipB, 0.0, 0.0, alm1);
        Camion camion13 = new Camion(13, "B007", tipB, 0.0, 0.0, alm1);
        Camion camion14 = new Camion(14, "B008", tipB, 0.0, 0.0, alm2);
        Camion camion15 = new Camion(15, "B009", tipB, 0.0, 0.0, alm2);
        Camion camion16 = new Camion(16, "B010", tipB, 0.0, 0.0, alm2);
        Camion camion17 = new Camion(17, "B011", tipB, 0.0, 0.0, alm3);
        Camion camion18 = new Camion(18, "B012", tipB, 0.0, 0.0, alm3);
        Camion camion19 = new Camion(19, "B013", tipB, 0.0, 0.0, alm3);
        Camion camion20 = new Camion(20, "B014", tipB, 0.0, 0.0, alm3);
        Camion camion21 = new Camion(21, "B015", tipB, 0.0, 0.0, alm3);
        Camion camion22 = new Camion(22, "C001", tipC, 0.0, 0.0, alm1);
        Camion camion23 = new Camion(23, "C002", tipC, 0.0, 0.0, alm1);
        Camion camion24 = new Camion(24, "C003", tipC, 0.0, 0.0, alm1);
        Camion camion25 = new Camion(25, "C004", tipC, 0.0, 0.0, alm1);
        Camion camion26 = new Camion(26, "C005", tipC, 0.0, 0.0, alm1);
        Camion camion27 = new Camion(27, "C006", tipC, 0.0, 0.0, alm1);
        Camion camion28 = new Camion(28, "C007", tipC, 0.0, 0.0, alm1);
        Camion camion29 = new Camion(29, "C008", tipC, 0.0, 0.0, alm1);
        Camion camion30 = new Camion(30, "C009", tipC, 0.0, 0.0, alm1);
        Camion camion31 = new Camion(31, "C010", tipC, 0.0, 0.0, alm1);
        Camion camion32 = new Camion(32, "C011", tipC, 0.0, 0.0, alm2);
        Camion camion33 = new Camion(33, "C012", tipC, 0.0, 0.0, alm2);
        Camion camion34 = new Camion(34, "C013", tipC, 0.0, 0.0, alm2);
        Camion camion35 = new Camion(35, "C014", tipC, 0.0, 0.0, alm2);
        Camion camion36 = new Camion(36, "C015", tipC, 0.0, 0.0, alm2);
        Camion camion37 = new Camion(37, "C016", tipC, 0.0, 0.0, alm2);
        Camion camion38 = new Camion(38, "C017", tipC, 0.0, 0.0, alm3);
        Camion camion39 = new Camion(39, "C018", tipC, 0.0, 0.0, alm3);
        Camion camion40 = new Camion(40, "C019", tipC, 0.0, 0.0, alm3);
        Camion camion41 = new Camion(41, "C020", tipC, 0.0, 0.0, alm3);
        Camion camion42 = new Camion(42, "C021", tipC, 0.0, 0.0, alm3);
        Camion camion43 = new Camion(43, "C022", tipC, 0.0, 0.0, alm3);
        Camion camion44 = new Camion(44, "C023", tipC, 0.0, 0.0, alm3);
        Camion camion45 = new Camion(45, "C024", tipC, 0.0, 0.0, alm3);

        List<Camion> listCamiones = new ArrayList<>();
        listCamiones.add(camion1);listCamiones.add(camion2);listCamiones.add(camion3);listCamiones.add(camion4);
        listCamiones.add(camion5);listCamiones.add(camion6);listCamiones.add(camion7);listCamiones.add(camion8);
        listCamiones.add(camion9);listCamiones.add(camion10);listCamiones.add(camion11);listCamiones.add(camion12);
        listCamiones.add(camion13);listCamiones.add(camion14);listCamiones.add(camion15);listCamiones.add(camion16);
        listCamiones.add(camion17);listCamiones.add(camion18);listCamiones.add(camion19);listCamiones.add(camion20);
        listCamiones.add(camion21);listCamiones.add(camion22);listCamiones.add(camion23);listCamiones.add(camion24);
        listCamiones.add(camion25);listCamiones.add(camion26);listCamiones.add(camion27);listCamiones.add(camion28);
        listCamiones.add(camion29);listCamiones.add(camion30);listCamiones.add(camion31);listCamiones.add(camion32);
        listCamiones.add(camion33);listCamiones.add(camion34);listCamiones.add(camion35);listCamiones.add(camion36);
        listCamiones.add(camion37);listCamiones.add(camion38);listCamiones.add(camion39);listCamiones.add(camion40);
        listCamiones.add(camion41);listCamiones.add(camion42);listCamiones.add(camion43);listCamiones.add(camion44);
        listCamiones.add(camion45);

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


        //SegundaSolucion solPSO = new SegundaSolucion();
        //solPSO.inicializar(Mapa.listaPedidos, Mapa.listaAlmacenes);

        //Función que llama al PSO:
        //PSOMain.Main();
    }
}
