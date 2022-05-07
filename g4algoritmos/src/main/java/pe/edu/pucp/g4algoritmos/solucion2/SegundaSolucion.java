package pe.edu.pucp.g4algoritmos.solucion2;

import pe.edu.pucp.g4algoritmos.model.*;
import pe.edu.pucp.g4algoritmos.pso.ParticleSwarmOptimization;
import pe.edu.pucp.g4algoritmos.utilitarios.Stats;


import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;


public class SegundaSolucion {
    
        /*Variables OUTPUT*/ 
        public static List<Date> tiempoDeSalidasZona = new ArrayList<>();
        
        public static List <Ruta> planesDeTransporte = new ArrayList<>(); //Lista de los planes de transporte o RUTAS por camion

        /*Variables INPUT*/ 
        public static List<Pedido> listaPedidos = new ArrayList<>(); //Lista inicial de los pedidos
        public static List<OficinaPSO> listaOficinas = new ArrayList<>(); //Lista de oficinas que tienen al menos un pedido
        public static List<Camion> listaCamiones = new ArrayList<>(); //Lista de camiones disponibles de un ALMACEN

        public static int paquetes; //Cantidad total de paquetes A 
        
        public void inicializar(List<Pedido> pedidos){
            
            System.out.println("Solución PSO: ");
        
            listaPedidos = pedidos;

            /*I. Oficinas de todos los pedido*/
            listaOficinas = contabilizarOficinas();
            System.out.println(String.format("Cantidad de oficinas a recorrer:  %4d", listaOficinas.size()));

            /*II. Asignación de Rutas*/
            particleSwarmOptimization();
        }

        /*1. Determinar lista de oficinas de los pedidos*/ 
        private List<OficinaPSO> contabilizarOficinas() {
            List<OficinaPSO> oficinasPSO = new ArrayList<>();
            List<Oficina> oficinas = new ArrayList<>();

            for (Pedido p : listaPedidos){
                if(oficinas.contains(p.getOficina()) == false) {
                    oficinas.add(p.getOficina());
                }
            }

            for (Oficina o: oficinas){
                long max_miliseconds = 9 * (long)10e13; //en milisegundos
                OficinaPSO oficinaPSO = new OficinaPSO(o);
                for (Pedido p : listaPedidos){
                    if (p.getOficina().getCodigo() == o.getCodigo()){
                        oficinaPSO.addPedido(p);
                        max_miliseconds = p.getFechaHoraLimite().getTime() < max_miliseconds ? p.getFechaHoraLimite().getTime() : max_miliseconds;
                        oficinaPSO.addPaquetes(p.getCantidadActual());
                    }
                }
                oficinaPSO.setL(new Date(max_miliseconds));
            }

            return oficinasPSO;
        }

        private void particleSwarmOptimization(){

        }


}
