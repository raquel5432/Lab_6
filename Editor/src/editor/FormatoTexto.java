package editor;

import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.Color;

public class FormatoTexto {

    private JTextPane panel;
    private StyledDocument docx;

    private SimpleAttributeSet atributosActivos = new SimpleAttributeSet();

    public FormatoTexto(JTextPane panel) {
        this.panel = panel;
        this.docx = panel.getStyledDocument();
    }

    private void aplicar(SimpleAttributeSet atributos) {
        int inicio = panel.getSelectionStart();
        int fin    = panel.getSelectionEnd();

        if (inicio != fin) {
            docx.setCharacterAttributes(inicio, fin - inicio, atributos, false);
        }

        atributosActivos.addAttributes(atributos);
        panel.setCharacterAttributes(atributosActivos, false);
    }


    public void cambiarFuente(String fuente) {
        SimpleAttributeSet a = new SimpleAttributeSet();
        StyleConstants.setFontFamily(a, fuente);
        aplicar(a);
    }

    public void cambiarTamano(int tamano) {
        SimpleAttributeSet a = new SimpleAttributeSet();
        StyleConstants.setFontSize(a, tamano);
        aplicar(a);
    }

    public void cambiarColor(Color color) {
        SimpleAttributeSet a = new SimpleAttributeSet();
        StyleConstants.setForeground(a, color);
        aplicar(a);
    }

    public void ponerNegrita() {
        boolean negritaActual = StyleConstants.isBold(atributosActivos);
        SimpleAttributeSet a = new SimpleAttributeSet();
        StyleConstants.setBold(a, !negritaActual);
        aplicar(a);
    }

    public void ponerCursiva() {
        boolean cursivaActual = StyleConstants.isItalic(atributosActivos);
        SimpleAttributeSet a = new SimpleAttributeSet();
        StyleConstants.setItalic(a, !cursivaActual);
        aplicar(a);
    }

    public void ponerSubrayado() {
        boolean subActual = StyleConstants.isUnderline(atributosActivos);
        SimpleAttributeSet a = new SimpleAttributeSet();
        StyleConstants.setUnderline(a, !subActual);
        aplicar(a);
    }
}
