/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ALISSONRAQUELMARTINE
 */
public class ArchivoManager {

    public void crearArchivo(File archivo) throws IOException {
        if (!archivo.exists()) {
            archivo.createNewFile();
        }
    }

    public void guardarTabla(File archivo, List<String[]> datos) throws IOException {
        try (FileWriter writer = new FileWriter(archivo)) {
            for (String[] fila : datos) {
                writer.write(String.join(";", fila));
                writer.write("\n");
            }
        }
    }

    public List<String[]> leerTabla(File archivo) throws IOException {
        List<String[]> datos = new ArrayList<>();

        if (!archivo.exists()) {
            return datos;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;

            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                datos.add(partes);
            }
        }

        return datos;
    }

    public String getInfoArchivo(File archivo) {
        if (archivo == null) {
            return "No hay archivo seleccionado.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Nombre: ").append(archivo.getName()).append("\n");
        sb.append("Ruta: ").append(archivo.getAbsolutePath()).append("\n");
        sb.append("Existe: ").append(archivo.exists() ? "Sí" : "No").append("\n");
        sb.append("Se puede leer: ").append(archivo.canRead() ? "Sí" : "No").append("\n");
        sb.append("Se puede escribir: ").append(archivo.canWrite() ? "Sí" : "No").append("\n");
        sb.append("Tamaño: ").append(archivo.exists() ? archivo.length() + " bytes" : "0 bytes").append("\n");

        return sb.toString();
    }
}
