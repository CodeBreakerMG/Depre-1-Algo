package pe.edu.pucp.g4algoritmos;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import pe.edu.pucp.g4algoritmos.model.Mapa;
import pe.edu.pucp.g4algoritmos.pso.PSOMain;
import pe.edu.pucp.g4algoritmos.solucion1.PrimeraSolucion;

import pe.edu.pucp.g4algoritmos.solucion2.SegundaSolucion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pe.edu.pucp.g4algoritmos.model.Camion;
import pe.edu.pucp.g4algoritmos.model.Oficina;
import pe.edu.pucp.g4algoritmos.model.TipoCamion;
/*

DEPRE-1-ALGORITMOS
ODIPARPACK SW

CLASE DE JAVA PARA EJECUTAR EL PROYECTO (MAIN)
DESDE ESTE FILE SE TIENE QUE EJECUTAR TO DO.

 */
public class App {
	
    public static void main( String[] args ){

		if (args.length >= 0){
			Solucion2PSO();
			return;
		}

		try{
			double costo = 0.0;
			/*1. Preparacion del archivo OUTPUT:*/
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("_yyyy_MM_dd_HH_mm_ss");  
            LocalDateTime now = LocalDateTime.now();  
            
            String filename = "output\\Solucion_1_SA" + dtf.format(now) + ".txt";
            PrintWriter writer = new PrintWriter(filename, "UTF-8");

            
            writer.println("Planes de Transporte para la atención de Pedidos");
			
			/*2.Función que llama a la primera solucion*/
			PrimeraSolucion sol1Trujillo = new PrimeraSolucion();
			PrimeraSolucion sol1Lima = new PrimeraSolucion();
			PrimeraSolucion sol1Arequipa = new PrimeraSolucion();

			/*3. cargar la data al maestro MAPA*/
			final String sep = File.separator;
			Mapa.cargarAlmacenesYOficinas(System.getProperty("user.dir")+sep+"data"+sep+"inf226.oficinas.txt");
		   
            Mapa.cargarCamiones(System.getProperty("user.dir")+sep+"data"+sep+"inf226.camiones.txt");
			
			Mapa.cargarTramos(System.getProperty("user.dir")+sep+"data"+sep+"inf226.tramos.v.2.0.txt");
			Mapa.cargarBloqueos(20, System.getProperty("user.dir")+sep+"data"+sep+"inf226.bloqueo.05.txt");
			Mapa.cargarPedidos(System.getProperty("user.dir")+sep+"data"+sep+"inf226.ventas202205temp.txt");
			

			/*
			//Pruebas archivos ERICK

			Mapa.setListaCamiones(listCamiones);
			
			Mapa.cargarTramos(System.getProperty("user.dir")+sep+"data"+sep+"Nueva_Data"+sep+"inf226.tramos.v.1.0.txt");
			Mapa.cargarPedidos(System.getProperty("user.dir")+sep+"data"+sep+"Nueva_Data"+sep+"inf226.ventas202203.txt");

			*/
			
			

			Mapa.setTramosToOficinas();
			Mapa.setTramosToAlmacenes();

			writer.println(String.format("Cantidad de oficinas: %4d", Mapa.listaOficinas.size() + Mapa.listaAlmacenes.size()));
			writer.println(String.format("Cantidad de tramos:   %4d", Mapa.listaTramos.size()));
			writer.println(String.format("Cantidad de bloqueos:   %4d", Mapa.listaBloqueos.size()));
			writer.println(String.format("Cantidad de pedidos:  %4d", Mapa.listaPedidos.size()));
			writer.println(String.format("Cantidad de camiones:  %4d", Mapa.listaCamiones.size()));

			/*4. Corremos la solucion: solucion.inicializar(Mapa.getListaPedidos(), alm);*/
			//-> Poner: solucion.inicializar(Mapa.getListaPedidosPorAlmacen(alm), alm) //Ejecutar para cada almacen
			
			costo = costo + sol1Lima.inicializar(Mapa.getListaPedidosPorAlmacen(Mapa.getOficinaByCodigo("150101")), Mapa.getOficinaByCodigo("150101"), writer);
			writer.println("LIMA OK      ==========================================================");
			
			costo = costo + sol1Trujillo.inicializar(Mapa.getListaPedidosPorAlmacen(Mapa.getOficinaByCodigo("130101")), Mapa.getOficinaByCodigo("130101"), writer);
			writer.println("TRUJILLO OK  ==========================================================");
			
			costo = costo + sol1Arequipa.inicializar(Mapa.getListaPedidosPorAlmacen(Mapa.getOficinaByCodigo("040101")), Mapa.getOficinaByCodigo("040101"), writer);
			writer.println("AREQUIPA OK  ==========================================================");
			

			writer.println("==========================================================");
			writer.println("");
			writer.println("");
			writer.println("==========================================================");
			writer.println("El costo total del algoritmo Simulated Annealing : " + costo);
			writer.println("");
			writer.println("");

            writer.close();
            System.out.println("Se ejecutó la primera solución con éxito.");
		   
	   }
		catch (Exception e) {
            e.printStackTrace();
        }
    }

	public static void Solucion2PSO(){

		try{
			/*1. Preparacion del archivo OUTPUT:*/

			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("_yyyy_MM_dd_HH_mm");  
            LocalDateTime now = LocalDateTime.now();  
            
            String filename = "output\\Solucion_2_PSO" + dtf.format(now) + ".txt";
            PrintWriter writer = new PrintWriter(filename, "UTF-8");

            
            writer.println("Solucion de PSO");
			

			/*2. cargar la data al maestro MAPA*/
			final String sep = File.separator;
			Mapa.cargarAlmacenesYOficinas(System.getProperty("user.dir")+sep+"data"+sep+"inf226.oficinas.txt");
		   
            Mapa.cargarCamiones(System.getProperty("user.dir")+sep+"data"+sep+"inf226.camiones.txt");
			
			Mapa.cargarTramos(System.getProperty("user.dir")+sep+"data"+sep+"inf226.tramos.v.2.0.txt");
			Mapa.cargarPedidos(System.getProperty("user.dir")+sep+"data"+sep+"inf226.ventas202202.txt");

			Mapa.setTramosToOficinas();
			Mapa.setTramosToAlmacenes();

			/*
			writer.println(String.format("Cantidad de oficinas: %4d", Mapa.listaOficinas.size() + Mapa.listaAlmacenes.size()));
			writer.println(String.format("Cantidad de tramos:   %4d", Mapa.listaTramos.size()));
			writer.println(String.format("Cantidad de pedidos:  %4d", Mapa.listaPedidos.size()));
			writer.println(String.format("Cantidad de camiones:  %4d", Mapa.listaCamiones.size()));
			*/
			/*Ejecución PSO*/

			SegundaSolucion solPSO = new SegundaSolucion();
			long runTime = solPSO.inicializar(Mapa.listaPedidos, writer);

			
			/*
			writer.println("==========================================================");
			writer.println("");
			writer.println("");
			writer.println("==========================================================");

            writer.close();
			*/
            System.out.println("Se ejecutó la segunda solución con éxito.");
			System.out.println("Tiempo de Ejecución (en segundos): " + runTime);
		   
	   }
		catch (Exception e) {
            e.printStackTrace();
        }
	}
}