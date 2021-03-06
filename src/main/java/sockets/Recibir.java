package sockets;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import frontend.VtnEdicion;
import static frontend.VtnPrincipal.leerArchivo;

public class Recibir {

    private final DataInputStream in;
    private javax.swing.JFrame parent;
    private File directorioProyecto;
    private final String SEPARADOR = System.getProperty("file.separator");

    public Recibir(DataInputStream in, javax.swing.JFrame parent) {
        this.in = in;
        this.parent = parent;
    }

    public void recibirArchivo(String contenidoArchivo) throws IOException {
        JFileChooser fileChosser = new JFileChooser();
        fileChosser.setDialogTitle("Selecciona el directorio para guardar el .json");
        fileChosser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChosser.setAcceptAllFileFilterUsed(false);

        if (fileChosser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File directorio = fileChosser.getSelectedFile();
            this.directorioProyecto = directorio;
            boolean guardado = guardarArchivo(directorio, contenidoArchivo, "repitencia.json");
            guardarArchivo(directorio, "", "reportes.def");
            guardarArchivo(directorio, "", "reporte.html");
            String contenidoCopy = directorio.getAbsolutePath() + SEPARADOR + "repitencia.json,";
            contenidoCopy += directorio.getAbsolutePath() + SEPARADOR + "reportes.def,";
            contenidoCopy += directorio.getAbsolutePath() + SEPARADOR + "reporte.html";
            guardarArchivo(directorio, contenidoCopy, "proyecto.copy");
            abrirProyecto(new File(directorio.getAbsoluteFile() + SEPARADOR + "proyecto.copy"));
            if (guardado) {
                JOptionPane.showMessageDialog(null, "Archivo guardado");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo guardar el archivo");
            }
        }
    }

    private void abrirProyecto(File archivoCopy) {
        String estructura = leerArchivo(archivoCopy);
        String[] archivos = estructura.split(",");
        File archivoJson = new File(archivos[0].replace(" ", ""));
        File archivoDef = new File(archivos[1].replace(" ", ""));
        File archivoHtml = new File(archivos[2].replace(" ", ""));
        VtnEdicion vtnEdicion = new VtnEdicion(archivoJson, archivoDef, archivoHtml);
        vtnEdicion.setVisible(true);
        parent.dispose();
    }

    private boolean guardarArchivo(File archivo, String contenido, String nombre) {
        boolean guardado = false;
        FileWriter fw;
        try {
            fw = new FileWriter(archivo + SEPARADOR + nombre);
            fw.write(contenido);
            fw.close();
            guardado = true;
        } catch (IOException ex) {

        }

        return guardado;
    }

    public File getDirectorioProyecto() {
        return this.directorioProyecto;
    }
}
