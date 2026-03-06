/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
/**
 *
 * @author Alejandro R
 */
public class GUI extends JFrame {

    private Editor logica;
    private JTextPane areaTexto;
    private FormatoTexto formato;  // clase del compañero

    // Controles de la barra
    private JComboBox<String> cmbFuente;
    private JComboBox<Integer> cmbTamanio;
    private JButton btnColor;
    private Color colorElegido = Color.BLACK;

    // Colores usados (los cuadraditos de la imagen)
    private Color[] coloresUsados = {
        Color.BLACK, Color.WHITE, Color.RED, new Color(139,69,19),
        Color.ORANGE, Color.GRAY, Color.YELLOW, Color.BLUE
    };

    public GUI() {
        logica = new Editor();

        setTitle("Editor de Texto");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(crearBarraHerramientas(), BorderLayout.NORTH);
        add(crearAreaTexto(),         BorderLayout.CENTER);  // areaTexto se crea aqui
        add(crearBarraEstado(),       BorderLayout.SOUTH);
        setJMenuBar(crearMenu());

        formato = new FormatoTexto(areaTexto);  // se crea despues de areaTexto
    }

    // BARRA DE HERRAMIENTAS
    private JPanel crearBarraHerramientas() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Fuente
        panel.add(new JLabel("Fuente:"));
        String[] fuentes = {"Arial", "Times New Roman", "Courier New",
                            "Georgia", "Verdana", "Lucida Calligraphy"};
        cmbFuente = new JComboBox<>(fuentes);
        cmbFuente.addActionListener(e -> cambiarFuente());
        panel.add(cmbFuente);

        // Tamaño
        panel.add(new JLabel("Tamaño:"));
        Integer[] tamanios = {8, 12, 16, 24, 32, 42, 48, 64, 92, 144, 190, 240, 300};
        cmbTamanio = new JComboBox<>(tamanios);
        cmbTamanio.setSelectedItem(16);
        cmbTamanio.addActionListener(e -> cambiarTamanio());
        panel.add(cmbTamanio);

        panel.add(new JSeparator(SwingConstants.VERTICAL));

        // Color
        panel.add(new JLabel("Color:"));
        btnColor = new JButton("  A  ");
        btnColor.setForeground(colorElegido);
        btnColor.addActionListener(e -> elegirColor());
        panel.add(btnColor);

        // Negrita, Cursiva, Subrayado
        JButton btnB = new JButton("B");
        btnB.setFont(new Font("Arial", Font.BOLD, 14));
        btnB.addActionListener(e -> aplicarNegrita());
        panel.add(btnB);

        JButton btnI = new JButton("I");
        btnI.setFont(new Font("Arial", Font.ITALIC, 14));
        btnI.addActionListener(e -> aplicarCursiva());
        panel.add(btnI);

        JButton btnU = new JButton("U");
        btnU.addActionListener(e -> aplicarSubrayado());
        panel.add(btnU);

        panel.add(new JSeparator(SwingConstants.VERTICAL));

        // Colores predefinidos
        panel.add(new JLabel("Colores utilizados:"));
        for (Color c : coloresUsados) {
            JButton btn = new JButton();
            btn.setBackground(c);
            btn.setPreferredSize(new Dimension(20, 20));
            btn.addActionListener(e -> {
                colorElegido = c;
                btnColor.setForeground(c);
                aplicarColor();
            });
            panel.add(btn);
        }

        return panel;
    }

    // AREA DE TEXTO
    private JScrollPane crearAreaTexto() {
        areaTexto = new JTextPane();
        areaTexto.setEditorKit(logica.getRtfKit());
        areaTexto.setDocument(logica.getDocumento());
        areaTexto.setFont(new Font("Arial", Font.PLAIN, 16));
        return new JScrollPane(areaTexto);
    }

    // BARRA DE ESTADO
    private JLabel crearBarraEstado() {
        JLabel lbl = new JLabel("  Listo");
        lbl.setBorder(BorderFactory.createEtchedBorder());
        return lbl;
    }

    // MENU
    private JMenuBar crearMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu archivo = new JMenu("Archivo");

        JMenuItem nuevo = new JMenuItem("Nuevo");
        nuevo.addActionListener(e -> nuevo());
        archivo.add(nuevo);

        JMenuItem abrir = new JMenuItem("Abrir");
        abrir.addActionListener(e -> abrir());
        archivo.add(abrir);

        JMenuItem guardar = new JMenuItem("Guardar");
        guardar.addActionListener(e -> guardar());
        archivo.add(guardar);

        archivo.addSeparator();

        JMenuItem salir = new JMenuItem("Salir");
        salir.addActionListener(e -> System.exit(0));
        archivo.add(salir);

        menuBar.add(archivo);

        JMenu insertar = new JMenu("Insertar");

        JMenuItem tabla = new JMenuItem("Tabla");
        tabla.addActionListener(e -> insertarTabla());
        insertar.add(tabla);

        menuBar.add(insertar);

        return menuBar;
    }

    // ACCIONES DE FORMATO - ahora usan la clase del compañero
    private void cambiarFuente() {
        String fuente = (String) cmbFuente.getSelectedItem();
        formato.cambiarFuente(fuente);
    }

    private void cambiarTamanio() {
        Integer tam = (Integer) cmbTamanio.getSelectedItem();
        formato.cambiarTamano(tam);
    }

    private void elegirColor() {
        Color c = JColorChooser.showDialog(this, "Elegir color", colorElegido);
        if (c != null) {
            colorElegido = c;
            btnColor.setForeground(c);
            aplicarColor();
        }
    }

    private void aplicarColor() {
        formato.cambiarColor(colorElegido);
    }

    private void aplicarNegrita() {
        formato.ponerNegrita();
    }

    private void aplicarCursiva() {
        formato.ponerCursiva();
    }

    private void aplicarSubrayado() {
        formato.ponerSubrayado();
    }

    // ACCIONES DE ARCHIVO - siguen siendo tuyas
    private void nuevo() {
        areaTexto.setText("");
    }

    private void abrir() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                logica.abrir(fc.getSelectedFile());
                areaTexto.setDocument(logica.getDocumento());
                setTitle("Editor - " + fc.getSelectedFile().getName());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir: " + ex.getMessage());
            }
        }
    }

    private void guardar() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File f = fc.getSelectedFile();
                if (!f.getName().endsWith(".rtf")) f = new File(f.getPath() + ".rtf");
                logica.guardar(f);
                JOptionPane.showMessageDialog(this, "Guardado: " + f.getName());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
            }
        }
    }

    private void insertarTabla() {
        String filasStr = JOptionPane.showInputDialog(this, "Número de filas:", "3");
        String colsStr  = JOptionPane.showInputDialog(this, "Número de columnas:", "3");

        if (filasStr == null || colsStr == null) return;

        int filas = Integer.parseInt(filasStr);
        int cols  = Integer.parseInt(colsStr);

        String[] columnas = new String[cols];
        for (int i = 0; i < cols; i++) columnas[i] = "Col " + (i + 1);

        Object[][] datos = new Object[filas][cols];

        JTable tabla = new JTable(new DefaultTableModel(datos, columnas));
        tabla.setGridColor(Color.BLACK);
        tabla.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(400, 150));

        int resultado = JOptionPane.showConfirmDialog(this, scroll,
            "Ingrese datos en la tabla", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            areaTexto.insertComponent(new JScrollPane(tabla));
        }
    }
}