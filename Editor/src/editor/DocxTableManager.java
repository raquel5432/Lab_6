/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;


/**
 *
 * @author ALISSONRAQUELMARTINE
 */


public class DocxTableManager {

    public void crearArchivo(File archivo) throws Exception {
        if (!archivo.exists()) {
            try (XWPFDocument document = new XWPFDocument();
                 FileOutputStream fos = new FileOutputStream(archivo)) {
                document.write(fos);
            }
        }
    }

    public void guardarTabla(File archivo, List<String[]> datos, String[] encabezados) throws Exception {

    try (XWPFDocument document = new XWPFDocument();
         FileOutputStream fos = new FileOutputStream(archivo)) {

        XWPFTable table = document.createTable();

        for (int c = 0; c < encabezados.length; c++) {
            if (c == 0) {
                table.getRow(0).getCell(0).setText(encabezados[0]);
            } else {
                table.getRow(0).addNewTableCell().setText(encabezados[c]);
            }
        }

        for (int r = 0; r < datos.size(); r++) {

            org.apache.poi.xwpf.usermodel.XWPFTableRow row = table.createRow();

            for (int c = 0; c < encabezados.length; c++) {

                String valor = "";

                if (c < datos.get(r).length && datos.get(r)[c] != null) {
                    valor = datos.get(r)[c];
                }

                row.getCell(c).setText(valor);
            }
        }

        document.write(fos);
    }
}

    public List<String[]> leerTabla(File archivo) throws Exception {
        List<String[]> datos = new ArrayList<>();

        if (!archivo.exists()) {
            return datos;
        }

        try (FileInputStream fis = new FileInputStream(archivo);
             XWPFDocument document = new XWPFDocument(fis)) {

            List<XWPFTable> tablas = document.getTables();

            if (tablas.isEmpty()) {
                return datos;
            }

            XWPFTable tabla = tablas.get(0);

            for (int r = 1; r < tabla.getRows().size(); r++) {
                int cols = tabla.getRow(r).getTableCells().size();
                String[] fila = new String[cols];

                for (int c = 0; c < cols; c++) {
                    fila[c] = tabla.getRow(r).getCell(c).getText();
                }

                datos.add(fila);
            }
        }

        return datos;
    }

    public String[] leerEncabezados(File archivo) throws Exception {
        if (!archivo.exists()) {
            return new String[0];
        }

        try (FileInputStream fis = new FileInputStream(archivo);
             XWPFDocument document = new XWPFDocument(fis)) {

            List<XWPFTable> tablas = document.getTables();

            if (tablas.isEmpty()) {
                return new String[0];
            }

            XWPFTable tabla = tablas.get(0);
            int cols = tabla.getRow(0).getTableCells().size();
            String[] encabezados = new String[cols];

            for (int c = 0; c < cols; c++) {
                encabezados[c] = tabla.getRow(0).getCell(c).getText();
            }

            return encabezados;
        }
    }

    public String getInfoArchivo(File archivo) {
        if (archivo == null) {
            return "No hay archivo seleccionado.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Nombre: ").append(archivo.getName()).append("\n");
        sb.append("Ruta: ").append(archivo.getAbsolutePath()).append("\n");
        sb.append("Existe: ").append(archivo.exists() ? "Sí" : "No").append("\n");
        sb.append("Puede leerse: ").append(archivo.canRead() ? "Sí" : "No").append("\n");
        sb.append("Puede escribirse: ").append(archivo.canWrite() ? "Sí" : "No").append("\n");
        sb.append("Tamaño: ").append(archivo.exists() ? archivo.length() + " bytes" : "0 bytes").append("\n");
        return sb.toString();
    }
}