/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ALISSONRAQUELMARTINE
 */
public class VentanaPrincipal extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextArea areaInfo;
    private JTextField txtNombre;
    private JTextField txtEdad;
    private JTextField txtCarrera;

    private File archivoActual;
    private DocxTableManager manager;

    public VentanaPrincipal() {
        manager = new DocxTableManager();

        setTitle("Proyecto DOCX y Tabla");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnCrear = new JButton("Crear DOCX");
        JButton btnAbrir = new JButton("Abrir DOCX");
        JButton btnGuardar = new JButton("Guardar Tabla");
        JButton btnInfo = new JButton("Ver Info Archivo");

        panelSuperior.add(btnCrear);
        panelSuperior.add(btnAbrir);
        panelSuperior.add(btnGuardar);
        panelSuperior.add(btnInfo);

        add(panelSuperior, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{"Nombre", "Edad", "Carrera"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scrollTabla = new JScrollPane(tabla);
        add(scrollTabla, BorderLayout.CENTER);

        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBorder(BorderFactory.createTitledBorder("Información del Archivo"));

        areaInfo = new JTextArea();
        areaInfo.setEditable(false);

        panelDerecho.add(new JScrollPane(areaInfo), BorderLayout.CENTER);
        panelDerecho.setPreferredSize(new java.awt.Dimension(260, 0));

        add(panelDerecho, BorderLayout.EAST);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));

        txtNombre = new JTextField(10);
        txtEdad = new JTextField(5);
        txtCarrera = new JTextField(10);

        JButton btnAgregar = new JButton("Agregar Fila");
        JButton btnEliminar = new JButton("Eliminar Fila");

        panelInferior.add(new JLabel("Nombre:"));
        panelInferior.add(txtNombre);
        panelInferior.add(new JLabel("Edad:"));
        panelInferior.add(txtEdad);
        panelInferior.add(new JLabel("Carrera:"));
        panelInferior.add(txtCarrera);
        panelInferior.add(btnAgregar);
        panelInferior.add(btnEliminar);

        add(panelInferior, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> agregarFila());
        btnEliminar.addActionListener(e -> eliminarFila());
        btnCrear.addActionListener(e -> crearArchivo());
        btnGuardar.addActionListener(e -> guardarArchivo());
        btnAbrir.addActionListener(e -> abrirArchivo());
        btnInfo.addActionListener(e -> mostrarInfoArchivo());
    }

    private void agregarFila() {
        String nombre = txtNombre.getText().trim();
        String edad = txtEdad.getText().trim();
        String carrera = txtCarrera.getText().trim();

        if (nombre.isEmpty() || edad.isEmpty() || carrera.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos.");
            return;
        }

        modelo.addRow(new Object[]{nombre, edad, carrera});

        txtNombre.setText("");
        txtEdad.setText("");
        txtCarrera.setText("");
    }

    private void eliminarFila() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila para eliminar.");
            return;
        }

        modelo.removeRow(fila);
    }

    private void crearArchivo() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Crear archivo DOCX");

        int opcion = chooser.showSaveDialog(this);

        if (opcion == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();

            if (!archivo.getName().toLowerCase().endsWith(".docx")) {
                archivo = new File(archivo.getAbsolutePath() + ".docx");
            }

            try {
                manager.crearArchivo(archivo);
                archivoActual = archivo;
                areaInfo.setText("Archivo DOCX creado correctamente.\n\n" + manager.getInfoArchivo(archivoActual));
                JOptionPane.showMessageDialog(this, "Archivo creado correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al crear archivo: " + ex.getMessage());
            }
        }
    }

    private void guardarArchivo() {
        if (archivoActual == null) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Guardar archivo DOCX");

            int opcion = chooser.showSaveDialog(this);

            if (opcion != JFileChooser.APPROVE_OPTION) {
                return;
            }

            archivoActual = chooser.getSelectedFile();

            if (!archivoActual.getName().toLowerCase().endsWith(".docx")) {
                archivoActual = new File(archivoActual.getAbsolutePath() + ".docx");
            }
        }

        try {
            List<String[]> datos = new ArrayList<>();

            for (int i = 0; i < modelo.getRowCount(); i++) {
                String[] fila = new String[modelo.getColumnCount()];
                for (int j = 0; j < modelo.getColumnCount(); j++) {
                    Object valor = modelo.getValueAt(i, j);
                    fila[j] = valor == null ? "" : valor.toString();
                }
                datos.add(fila);
            }

            String[] encabezados = new String[modelo.getColumnCount()];
            for (int i = 0; i < modelo.getColumnCount(); i++) {
                encabezados[i] = modelo.getColumnName(i);
            }

            manager.guardarTabla(archivoActual, datos, encabezados);

            areaInfo.setText("Tabla guardada en DOCX correctamente.\n\n" + manager.getInfoArchivo(archivoActual));
            JOptionPane.showMessageDialog(this, "Tabla guardada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }

    private void abrirArchivo() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Abrir archivo DOCX");

        int opcion = chooser.showOpenDialog(this);

        if (opcion == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();

            try {
                String[] encabezados = manager.leerEncabezados(archivo);
                List<String[]> datos = manager.leerTabla(archivo);

                modelo.setRowCount(0);
                modelo.setColumnCount(0);

                if (encabezados.length == 0) {
                    modelo.addColumn("Nombre");
                    modelo.addColumn("Edad");
                    modelo.addColumn("Carrera");
                } else {
                    for (String encabezado : encabezados) {
                        modelo.addColumn(encabezado);
                    }
                }

                for (String[] fila : datos) {
                    modelo.addRow(fila);
                }

                archivoActual = archivo;
                areaInfo.setText("Archivo abierto correctamente.\n\n" + manager.getInfoArchivo(archivoActual));
                JOptionPane.showMessageDialog(this, "Archivo cargado correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir archivo: " + ex.getMessage());
            }
        }
    }

    private void mostrarInfoArchivo() {
        if (archivoActual == null) {
            JOptionPane.showMessageDialog(this, "No hay archivo seleccionado.");
            return;
        }

        areaInfo.setText(manager.getInfoArchivo(archivoActual));
    }
}