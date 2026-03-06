/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Alejandro R
 */
public class GUI extends JFrame {

    private FormatoTexto      formato;
    private DocxTableManager  manager;

    private JTextPane          areaTexto;
    private JComboBox<String>  cmbFuente;
    private JComboBox<Integer> cmbTamanio;
    private JButton            btnColor;
    private JLabel             barraEstado;
    private Color              colorElegido = Color.BLACK;
    private File               archivoActivo = null;

    private final Color[] coloresUsados = {
        Color.BLACK, Color.WHITE, Color.RED, new Color(139, 69, 19),
        Color.ORANGE, Color.GRAY, Color.YELLOW, Color.BLUE
    };

    public GUI() {
        manager = new DocxTableManager();

        setTitle("Editor de Texto (.docx)");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(crearBarraHerramientas(), BorderLayout.NORTH);
        add(crearAreaTexto(),         BorderLayout.CENTER);
        add(crearBarraEstado(),       BorderLayout.SOUTH);
        setJMenuBar(crearMenu());

        formato = new FormatoTexto(areaTexto);
    }
    
    private JPanel crearBarraHerramientas() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panel.add(new JLabel("Fuente:"));
        String[] fuentes = {"Arial", "Times New Roman", "Courier New",
                            "Georgia", "Verdana", "Lucida Calligraphy"};
        cmbFuente = new JComboBox<>(fuentes);
        cmbFuente.addActionListener(e -> formato.cambiarFuente((String) cmbFuente.getSelectedItem()));
        panel.add(cmbFuente);

        panel.add(new JLabel("Tamaño:"));
        Integer[] tamanios = {8, 12, 16, 24, 32, 42, 48, 64, 92, 144, 190, 240, 300};
        cmbTamanio = new JComboBox<>(tamanios);
        cmbTamanio.setSelectedItem(16);
        cmbTamanio.addActionListener(e -> {
            Integer tam = (Integer) cmbTamanio.getSelectedItem();
            if (tam != null) formato.cambiarTamano(tam);
        });
        panel.add(cmbTamanio);

        panel.add(new JSeparator(SwingConstants.VERTICAL));

        panel.add(new JLabel("Color:"));
        btnColor = new JButton("  A  ");
        btnColor.setForeground(colorElegido);
        btnColor.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Elegir color", colorElegido);
            if (c != null) {
                colorElegido = c;
                btnColor.setForeground(c);
                formato.cambiarColor(c);
            }
        });
        panel.add(btnColor);

        JButton btnB = new JButton("B");
        btnB.setFont(new Font("Arial", Font.BOLD, 14));
        btnB.addActionListener(e -> formato.ponerNegrita());
        panel.add(btnB);

        JButton btnI = new JButton("I");
        btnI.setFont(new Font("Arial", Font.ITALIC, 14));
        btnI.addActionListener(e -> formato.ponerCursiva());
        panel.add(btnI);

        JButton btnU = new JButton("U");
        btnU.addActionListener(e -> formato.ponerSubrayado());
        panel.add(btnU);

        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(new JLabel("Colores utilizados:"));

        for (Color c : coloresUsados) {
            JButton btn = new JButton();
            btn.setBackground(c);
            btn.setPreferredSize(new Dimension(20, 20));
            final Color cf = c;
            btn.addActionListener(e -> {
                colorElegido = cf;
                btnColor.setForeground(cf);
                formato.cambiarColor(cf);
            });
            panel.add(btn);
        }

        return panel;
    }
    
    private JScrollPane crearAreaTexto() {
        areaTexto = new JTextPane();
        areaTexto.setFont(new Font("Arial", Font.PLAIN, 16));
        return new JScrollPane(areaTexto);
    }
    
    private JLabel crearBarraEstado() {
        barraEstado = new JLabel("  Listo");
        barraEstado.setBorder(BorderFactory.createEtchedBorder());
        return barraEstado;
    }
    
    private JMenuBar crearMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu archivo = new JMenu("Archivo");
        agregarItem(archivo, "Nuevo",   e -> accionNuevo());
        agregarItem(archivo, "Abrir",   e -> accionAbrir());
        agregarItem(archivo, "Guardar", e -> accionGuardar());
        archivo.addSeparator();
        agregarItem(archivo, "Salir",   e -> System.exit(0));
        menuBar.add(archivo);

        JMenu insertar = new JMenu("Insertar");
        agregarItem(insertar, "Tabla",  e -> accionInsertarTabla());
        menuBar.add(insertar);

        return menuBar;
    }

    private void agregarItem(JMenu menu, String texto, java.awt.event.ActionListener al) {
        JMenuItem item = new JMenuItem(texto);
        item.addActionListener(al);
        menu.add(item);
    }

    private void accionNuevo() {
        areaTexto.setText("");
        archivoActivo = null;
        setTitle("Editor de Texto (.docx)");
        barraEstado.setText("  Nuevo documento");
    }

    private void accionAbrir() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Abrir archivo .docx");
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        try {
            String texto = manager.leerTexto(fc.getSelectedFile());
            areaTexto.setText(texto);
            archivoActivo = fc.getSelectedFile();
            setTitle("Editor - " + archivoActivo.getName());
            barraEstado.setText("  Abierto: " + archivoActivo.getName());
        } catch (Exception ex) {
            mostrarError("Error al abrir: " + ex.getMessage());
        }
    }

    private void accionGuardar() {
        if (archivoActivo == null) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Guardar como .docx");
            if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
            archivoActivo = fc.getSelectedFile();
            if (!archivoActivo.getName().toLowerCase().endsWith(".docx")) {
                archivoActivo = new File(archivoActivo.getAbsolutePath() + ".docx");
            }
        }

        try {
            manager.guardarTexto(archivoActivo, areaTexto.getText());
            barraEstado.setText("  Guardado: " + archivoActivo.getName());
            JOptionPane.showMessageDialog(this, "Guardado correctamente.");
        } catch (Exception ex) {
            mostrarError("Error al guardar: " + ex.getMessage());
        }
    }

    private void accionInsertarTabla() {
        String filasStr = JOptionPane.showInputDialog(this, "Número de filas:", "3");
        String colsStr  = JOptionPane.showInputDialog(this, "Número de columnas:", "3");
        if (filasStr == null || colsStr == null) return;

        int filas, cols;
        try {
            filas = Integer.parseInt(filasStr.trim());
            cols  = Integer.parseInt(colsStr.trim());
        } catch (NumberFormatException ex) {
            mostrarError("Ingresa números válidos.");
            return;
        }

        String[] columnas = new String[cols];
        for (int i = 0; i < cols; i++) columnas[i] = "Col " + (i + 1);

        DefaultTableModel modeloTabla = new DefaultTableModel(new Object[filas][cols], columnas);
        JTable jTabla = new JTable(modeloTabla);
        jTabla.setGridColor(Color.BLACK);
        jTabla.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(jTabla);
        scroll.setPreferredSize(new Dimension(500, 180));

        int resultado = JOptionPane.showConfirmDialog(
            this, scroll, "Ingrese datos en la tabla", JOptionPane.OK_CANCEL_OPTION
        );
        if (resultado != JOptionPane.OK_OPTION) return;

        if (archivoActivo == null) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Guardar tabla en .docx");
            if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
            archivoActivo = fc.getSelectedFile();
            if (!archivoActivo.getName().toLowerCase().endsWith(".docx")) {
                archivoActivo = new File(archivoActivo.getAbsolutePath() + ".docx");
            }
        }

        List<String[]> listaFilas = new ArrayList<>();
        for (int r = 0; r < filas; r++) {
            String[] fila = new String[cols];
            for (int c = 0; c < cols; c++) {
                Object val = modeloTabla.getValueAt(r, c);
                fila[c] = val == null ? "" : val.toString();
            }
            listaFilas.add(fila);
        }

        try {
            manager.guardarTabla(archivoActivo, listaFilas, columnas);
            areaTexto.insertComponent(new JScrollPane(new JTable(modeloTabla)));
            barraEstado.setText("  Tabla guardada en: " + archivoActivo.getName());
            JOptionPane.showMessageDialog(this, "Tabla guardada correctamente.");
        } catch (Exception ex) {
            mostrarError("Error al guardar tabla: " + ex.getMessage());
        }
    }
    
    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}