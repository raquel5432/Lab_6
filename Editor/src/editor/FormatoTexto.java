package editor;

import javax.swing.JTextPane;
import javax.swing.text.StyleConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import java.awt.Color;

public class FormatoTexto {
    private JTextPane panel;
    private StyledDocument docx;

    public FormatoTexto(JTextPane panel) {
        this.panel = panel;
        this.docx=panel.getStyledDocument();
    }
    
    public void cambiarFuente(String fuente){
        int inicio=panel.getSelectionStart();
        int fin=panel.getSelectionEnd();
        SimpleAttributeSet atributos=new SimpleAttributeSet();
        StyleConstants.setFontFamily(atributos, fuente);
        docx.setCharacterAttributes(inicio, fin, atributos, false);
    }
    
    public void cambiarTamano(int tamano){
        int inicio=panel.getSelectionStart();
        int fin=panel.getSelectionEnd();
        SimpleAttributeSet atributos=new SimpleAttributeSet();
        StyleConstants.setFontSize(atributos, tamano);
        docx.setCharacterAttributes(inicio, fin, atributos, false);
    }
    
    public void cambiarColor(Color color){
        int inicio=panel.getSelectionStart();
        int fin=panel.getSelectionEnd();
        SimpleAttributeSet atributos=new SimpleAttributeSet();
        StyleConstants.setForeground(atributos, color);
        docx.setCharacterAttributes(inicio, fin, atributos, false);
    }
    
    public void ponerNegrita(){
        int inicio=panel.getSelectionStart();
        int fin=panel.getSelectionEnd();
        SimpleAttributeSet atributos=new SimpleAttributeSet();
        StyleConstants.setBold(atributos, true);
        docx.setCharacterAttributes(inicio, fin, atributos, false);
    }
    
    public void ponerCursiva(){
        int inicio=panel.getSelectionStart();
        int fin=panel.getSelectionEnd();
        SimpleAttributeSet atributos=new SimpleAttributeSet();
        StyleConstants.setItalic(atributos, true);
        docx.setCharacterAttributes(inicio, fin, atributos, false);
    }
    
    public void ponerSubrayado(){
        int inicio=panel.getSelectionStart();
        int fin=panel.getSelectionEnd();
        SimpleAttributeSet atributos=new SimpleAttributeSet();
        StyleConstants.setUnderline(atributos, true);
        docx.setCharacterAttributes(inicio, fin, atributos, false);
    }
    
}
