package pe.edu.pucp.g4algoritmos.utilitarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pe.edu.pucp.g4algoritmos.model.Bloqueo;
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
import java.time.Year; 

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
            
            // Lectura de líneas y agregación de pedidos a la lista
            String line = "";
            BufferedReader br = new BufferedReader(new FileReader(ruta));
            
            int contadorPedidos = 0;

            while ((line = br.readLine()) != null) {  
                String[] linea_pedido = String.join("-", line.split("[,( =>  )]")).split("-+");
                
                // Si la oficina con el código provisto no existe, se omite la carga del pedido.
                String codOficina = linea_pedido[3].trim();
                if(Mapa.getOficinaByCodigo(codOficina) == null)
                    continue;
                
                String codigoPedido = year_month + String.format("%04d", contadorPedidos);
                String codCliente = linea_pedido[5].trim();
                Date fecha_hora_pedido = fechaHoraStrToDate(year, month, linea_pedido[0], linea_pedido[1]);
                Date fecha_hora_limite = calcularFechaHoraLimite(fecha_hora_pedido, codOficina);
                
                contadorPedidos++;

                // Creación del Pedido y sus entregas unitarias
                int cantidadTotal = Integer.valueOf(linea_pedido[4]);

                for(int i=1; i<=cantidadTotal; i++){
                    // Creación del pedido unitario                    
                    String codPedidoUnit = codigoPedido + String.format("%04d", i);
                    int cantUnit = 1;

                    Pedido pedidoUnit = new Pedido(codigoPedido, cantUnit, fecha_hora_pedido,
                                            fecha_hora_limite, codOficina, codCliente, codPedidoUnit);

                    // Agregación del pedido en la lista de pedidos
                    listaPedidos.add(pedidoUnit);                
                }                
            }
            br.close();
        }
        catch (IOException ex) {  
            ex.printStackTrace();  
        }

        return listaPedidos;
    }

    public static Date calcularFechaHoraLimite(Date fecha_hora_pedido, String codOficina){
        Oficina oficina = Mapa.getOficinaByCodigo(codOficina);
        
        if(oficina == null) return new Date();

        switch (oficina.getRegion()) {
            case 'C':
                return new Date(fecha_hora_pedido.getTime() + 1*DAY);
            case 'S':
                return new Date(fecha_hora_pedido.getTime() + 2*DAY);
            case 'E':
                return new Date(fecha_hora_pedido.getTime() + 3*DAY);
        }
        return new Date();
    }

    public static Date fechaHoraStrToDate(String year, String month, String day, String time){
        // year = "yyyy" ; month == "MM" ; day = "dd" ; time = "HH:mm"        
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return sf.parse(year+"-"+month+"-"+day+" "+time);
        }
        catch(ParseException ex) {
            System.out.println(ex.getMessage());
        }
        return new Date();
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

        // Lectura de líneas y agregación de tramos a la lista
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

    public static List<Bloqueo> leerBloqueos(String ruta) {
        // Verificación de haber cargado el archivo de tramos previamente
        if(Mapa.listaTramos.size() == 0){
            System.out.println("Debe cargar los datos del archivo de tramos al Mapa antes de cargar los bloqueos.");
            JOptionPane.showMessageDialog(null,
                "Debe cargar los datos del archivo de tramos al Mapa antes de cargar los bloqueos.",
                "Advertencia: Cargar tramos primero",
                JOptionPane.WARNING_MESSAGE);
            return null;
        }

        // Lectura de líneas y agregación de bloqueos a la lista
        List<Bloqueo> listaBloqueos = new ArrayList<Bloqueo>();        
        try {    
            String line = "";
            BufferedReader br = new BufferedReader(new FileReader(ruta));
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            
            while ((line = br.readLine()) != null) {  

                String[] linea_bloqueo = line.split("( +=> +)|,|;|==");

                String cod_oficina_inicio = linea_bloqueo[0];
                String cod_oficina_fin    = linea_bloqueo[1];

                Tramo tramo = Mapa.getTramoByOficinas(cod_oficina_inicio, cod_oficina_fin);
                if(tramo == null) continue;

                String mes_inicio = linea_bloqueo[2].substring(0, 2);
                String dia_inicio = linea_bloqueo[2].substring(2, 4);
                String mes_fin    = linea_bloqueo[4].substring(0, 2);
                String dia_fin    = linea_bloqueo[4].substring(2, 4);
                                
                int anio_inicio = Year.now().getValue();
                int anio_fin = Integer.valueOf(mes_inicio) > Integer.valueOf(mes_fin) ? (anio_inicio + 1) : anio_inicio;
                
                String hora_inicio = linea_bloqueo[3];
                String hora_fin = linea_bloqueo[5];

                try{
                    Date fecha_hora_inicio = sf.parse(String.valueOf(anio_inicio)+"-"+mes_inicio+"-"+dia_inicio+" "+hora_inicio);
                    Date fecha_hora_fin    = sf.parse(String.valueOf(anio_fin)+"-"+mes_fin+"-"+dia_fin+" "+hora_fin);
                    
                    listaBloqueos.add(new Bloqueo(cod_oficina_inicio, cod_oficina_fin, fecha_hora_inicio, fecha_hora_fin));
                }
                catch(ParseException ex){
                    System.out.println(ex.getMessage());
                }                
            }

            br.close();
        }
        catch (IOException ex) {  
            ex.printStackTrace();  
        }

        return listaBloqueos;
    }
}
