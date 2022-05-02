package pe.edu.pucp.g4algoritmos.utilitarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pe.edu.pucp.g4algoritmos.model.Mapa;
import pe.edu.pucp.g4algoritmos.model.Oficina;
import pe.edu.pucp.g4algoritmos.model.Pedido;
import pe.edu.pucp.g4algoritmos.model.Tramo;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat; 

public class LoadData {

    public static final long DAY = 24*3600*1000;
    
    /* leerOficinas(String ruta) retorna una lista de todas las oficinas
       que se encuentran en el archivo de oficinas de la ruta especificada */
    public static List<Oficina> leerOficinas(String ruta) {
        List<Oficina> listaOficinas = new ArrayList<Oficina>();

        try {
            // Lectura de líneas y agregación de oficinas a la lista
            String line = "";
            BufferedReader br = new BufferedReader(new FileReader(ruta));
            while ((line = br.readLine()) != null) {  
                String[] oficina = line.split(",");
                listaOficinas.add(
                    new Oficina(
                        Long.parseLong(oficina[0]),
                        oficina[0],
                        oficina[1],
                        oficina[2],
                        oficina[5].toUpperCase().equals("COSTA") ? 'C' : 
                            (oficina[5].toUpperCase().equals("SIERRA") ? 'S' : 
                            (oficina[5].toUpperCase().equals("SELVA") ? 'E' : 'N')),
                        Double.parseDouble(oficina[4]), // longitud
                        Double.parseDouble(oficina[3]), // latitud
                        ((oficina[1].toUpperCase().equals("LIMA")           && oficina[2].toUpperCase().equals("LIMA"))
                         || (oficina[1].toUpperCase().equals("AREQUIPA")    && oficina[2].toUpperCase().equals("AREQUIPA"))
                         || (oficina[1].toUpperCase().equals("LA LIBERTAD") && oficina[2].toUpperCase().equals("TRUJILLO"))) ? 1 : 0
                    )
                );
            }
            br.close();
        }   
        catch (IOException ex) {  
            ex.printStackTrace();  
        }  
        return listaOficinas;
    } 

    /* leerOficinas() retorna una lista de todas las oficinas
       que se encuentran en el archivo de oficinas que se abrirá
       utilizando un cuadro de diálogo de apertura de archivos   */
    public static List<Oficina> leerOficinas() {
        List<Oficina> listaOficinas = new ArrayList<Oficina>();

        // Selección del archivo a leer
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"))); 
        
        // Validación de selección de archivo
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            // Confirmación de carga de archivo (0=yes, 1=no, 2=cancel)
            int confirmation = JOptionPane.showConfirmDialog(null,
                            "Se utilizará el archivo en la ubicación \""
                            + selectedFile.getAbsolutePath()
                            + "\" para cargar las oficinas.\n¿Desea continuar?");
            
            if(confirmation == JOptionPane.OK_OPTION) {
                try {
                    // Lectura de líneas y agregación de oficinas a la lista
                    String line = "";
                    BufferedReader br = new BufferedReader(new FileReader(selectedFile.getAbsolutePath()));  
                    while ((line = br.readLine()) != null) {  
                        String[] oficina = line.split(",");
                        listaOficinas.add(
                            new Oficina(
                                Long.parseLong(oficina[0]),
                                oficina[0],
                                oficina[1],
                                oficina[2],
                                oficina[5].toUpperCase().equals("COSTA") ? 'C' : 
                                    (oficina[5].toUpperCase().equals("SIERRA") ? 'S' : 
                                    (oficina[5].toUpperCase().equals("SELVA") ? 'E' : 'N')),
                                Double.parseDouble(oficina[4]), // longitud
                                Double.parseDouble(oficina[3]), // latitud
                                ((oficina[1].toUpperCase().equals("LIMA")           && oficina[2].toUpperCase().equals("LIMA"))
                                 || (oficina[1].toUpperCase().equals("AREQUIPA")    && oficina[2].toUpperCase().equals("AREQUIPA"))
                                 || (oficina[1].toUpperCase().equals("LA LIBERTAD") && oficina[2].toUpperCase().equals("TRUJILLO"))) ? 1 : 0
                            )
                        );
                    } 
                    br.close(); 
                }   
                catch (IOException ex) {  
                    ex.printStackTrace();  
                }
            }
        }
        return listaOficinas;
    }    

    /* leerPedidos(String ruta) retorna una lista de todos los pedidos
       que se encuentran en un archivo de ventas especificado por la ruta */
    public static List<Pedido> leerPedidos(String ruta) {
        // Verificación de haber cargado el archivo de oficinas previamente
        if(Mapa.listaAlmacenes.size() == 0 && Mapa.listaOficinas.size() == 0){
            System.out.println("Debe cargar los datos del archivo de oficinas al Mapa antes de cargar los pedidos.");
            JOptionPane.showMessageDialog(null,
                "Debe cargar los datos del archivo de oficinas al Mapa antes de cargar los pedidos.",
                "Advertencia: Cargar oficinas primero",
                JOptionPane.WARNING_MESSAGE);
            return null;
        }

        List<Pedido> listaPedidos = new ArrayList<Pedido>();        
        try {
            // Lectura de año y mes del nombre del archivo (tipo ventas202203.txt)
            Pattern pattern = Pattern.compile("[1-9][0-9][0-9][0-9][0-1][1-9]");
            Matcher matcher = pattern.matcher(ruta);
            String year_month = matcher.find() ? matcher.group(0) : "000000";
            String year = year_month.substring(0, 4);
            String month = year_month.substring(4, 6);
            
            // Lectura de líneas y agregación de oficinas a la lista
            String line = "";
            BufferedReader br = new BufferedReader(new FileReader(ruta));
            
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            int counter = 0;

            while ((line = br.readLine()) != null) {  
                String[] linea_pedido = String.join("-", line.split("[,( =>  )]")).split("-+");
                
                Date fecha_hora_pedido = new Date();
                try{
                    fecha_hora_pedido = sf.parse(year+"-"+month+"-"+linea_pedido[0]+" "+linea_pedido[1]);
                }
                catch(ParseException ex){
                    System.out.println(ex.getMessage());
                }

                // Si la oficina con el código provisto no existe, se omite la carga del pedido.
                if(Mapa.getOficinaByCodigo(linea_pedido[3].trim()) == null)
                    continue;

                // Creación del objeto Pedido
                Pedido pedido = new Pedido(
                    year_month + String.format("%04d", ++counter),   // codigo será tipo yyyyMM01
                    Integer.valueOf(linea_pedido[4]),                       // cantidad
                    fecha_hora_pedido,                                      // fecha y hora de pedido
                    new Date(),                                             // fecha límite (temporal)
                    linea_pedido[3]                                         // código de oficina
                    );

                try {
                    // Asignación de fecha límite de entrega según región
                    switch (pedido.getOficina().getRegion()) { // Se asume que 1 día de entrega = 24 horas
                        case 'C': pedido.setFechaHoraLimite(new Date(pedido.getFechaHoraPedido().getTime()+1*DAY)); break;
                        case 'S': pedido.setFechaHoraLimite(new Date(pedido.getFechaHoraPedido().getTime()+2*DAY)); break;
                        case 'E': pedido.setFechaHoraLimite(new Date(pedido.getFechaHoraPedido().getTime()+3*DAY)); break;
                    }
                    
                    // Agregación del pedido en la lista de pedidos
                    listaPedidos.add(pedido);
                }
                catch(NullPointerException ex){
                    System.out.println("Oficina con código "+linea_pedido[3]+" no se encuentra en el Mapa.");
                }
            }
            br.close();
        }
        catch (IOException ex) {  
            ex.printStackTrace();  
        }

        return listaPedidos;
    }

    /* leerTramos(String ruta) retorna una lista de todos los tramos
       que se encuentran en el archivo de tramos especificado por la ruta */
    public static List<Tramo> leerTramos(String ruta) {
        // Verificación de haber cargado el archivo de oficinas previamente
        if(Mapa.listaAlmacenes.size() == 0 && Mapa.listaOficinas.size() == 0){
            System.out.println("Debe cargar los datos del archivo de oficinas al Mapa antes de cargar los tramos.");
            JOptionPane.showMessageDialog(null,
                "Debe cargar los datos del archivo de oficinas al Mapa antes de cargar los tramos.",
                "Advertencia: Cargar oficinas primero",
                JOptionPane.WARNING_MESSAGE);
            return null;
        }

        // Lectura de líneas y agregación de oficinas a la lista
        List<Tramo> listaTramos = new ArrayList<Tramo>();        
        try {    
            String line = "";
            BufferedReader br = new BufferedReader(new FileReader(ruta));
            
            while ((line = br.readLine()) != null) {  
                String[] linea_tramo = line.split("=>");
                if(Mapa.getOficinaByCodigo(linea_tramo[0].trim()) != null
                    && Mapa.getOficinaByCodigo(linea_tramo[1].trim()) != null) { 
                    listaTramos.add(new Tramo(linea_tramo[0].trim(), linea_tramo[1].trim()));
                }
            }

            br.close();
        }
        catch (IOException ex) {  
            ex.printStackTrace();  
        }

        return listaTramos;
    }
}
