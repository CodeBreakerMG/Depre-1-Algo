package pe.edu.pucp.g4algoritmos.utilitarios;

import java.util.ArrayList;
import java.util.List;

import pe.edu.pucp.g4algoritmos.model.Oficina;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException; 

public class LoadData {
    
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
}
