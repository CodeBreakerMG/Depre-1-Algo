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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class SegundaSolucion {
    
        /*Variables OUTPUT*/ 
        private  List<Date> tiempoDeSalidasZona = new ArrayList<>();
        
        private  List <Ruta> planesDeTransporte = new ArrayList<>(); //Lista de los planes de transporte o RUTAS por camion

        /*Variables INPUT*/ 
        private  List<Pedido> listaPedidos; //Lista inicial de los pedidos
        private  List<OficinaPSO> listaOficinas; //Lista de oficinas que tienen al menos un pedido
        private  List<Oficina> listaAlmacenes; //Lista de oficinas que tienen al menos un pedido
        private  List<List<Camion>> listaCamionesPorAlmacen; //Lista de camiones disponibles de un todos los almacenes

        private  int paquetes; //Cantidad total de paquetes A 
        
        public long inicializar(List<Pedido> pedidos){
            
            System.out.println("Solución PSO: ");
            
            this.listaAlmacenes = Mapa.listaAlmacenes;
            this.listaPedidos = pedidos;
            this.listaCamionesPorAlmacen = new ArrayList<>();

            for (Oficina almacen : listaAlmacenes){
                List<Camion> camiones = Mapa.getCamionesPorAlmacen(almacen);
                this.listaCamionesPorAlmacen.add(camiones) ;
            }

            /*I. Oficinas de todos los pedido*/
            this.listaOficinas = contabilizarOficinas();
            System.out.println(String.format("Cantidad de oficinas a recorrer:  %4d", listaOficinas.size()));

            /*II. Asignación de Rutas*/
            LocalDateTime startTime  = LocalDateTime.now();
            particleSwarmOptimization();
            LocalDateTime endTime = LocalDateTime.now();

            return ChronoUnit.SECONDS.between(startTime, endTime);
            
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
                long min_miliseconds = 0; //en milisegundos
                OficinaPSO oficinaPSO = new OficinaPSO(o);
                for (Pedido p : listaPedidos){
                    if (p.getOficina().getCodigo() == o.getCodigo()){
                        oficinaPSO.addPedido(p);
                        max_miliseconds = p.getFechaHoraLimite().getTime() < max_miliseconds ? p.getFechaHoraLimite().getTime() : max_miliseconds;
                        min_miliseconds = p.getFechaHoraPedido().getTime() > min_miliseconds ? p.getFechaHoraPedido().getTime() : min_miliseconds;
                        oficinaPSO.addPaquetes(p.getCantidadActual());
                    }
                }
                oficinaPSO.setTiempoLimiteLlegada(new Date(max_miliseconds));
                oficinaPSO.setTiempoMinimoSalidaCamion(new Date(min_miliseconds));
                oficinasPSO.add(oficinaPSO);
            }

            return oficinasPSO;
        }

        /*2. Llamar al PSO. Determinar lista de oficinas de los pedidos*/ 
        private void particleSwarmOptimization(){

            Date tiempoSalida = listaOficinas.get(0).tiempoMinimoSalidaCamion();

            for (OficinaPSO oficina : listaOficinas)
                tiempoSalida = AuxiliaryFunctions.minimumDate(oficina.tiempoMinimoSalidaCamion(), tiempoSalida);

            HybridParticleSwarmOptimization pso = new HybridParticleSwarmOptimization(listaOficinas, listaCamionesPorAlmacen, listaAlmacenes, tiempoSalida);
            pso.solve();
            pso.printOficinasXAlmacen();
           // planesDeTransporte = HybridParticleSwarmOptimization


           System.out.println("Se termino el PSO con exito");
        }


}
