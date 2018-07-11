package studio.visualdust.translator;
import studio.visualdust.translator.res.Resource;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TranslatorLauncher {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        TranslatorGUI tGUI = new TranslatorGUI();
        tGUI.setTitle("Translator By Mr.GZT & PaperCube");
        tGUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //ImageIcon windico = new ImageIcon("ICONIMG.png");
        //tGUI.setIconImage(windico.getImage());
        //tGUI.setIconImage(windico.getImage());
        tGUI.setLocation(50,50);
        tGUI.setVisible(true);
        tGUI.setSize(950,300);
        try {
            Thread.sleep(300);
            for(int i=300;i>=250;i--)
            {
                Thread.sleep(5);
                tGUI.setSize(650+i, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tGUI.setSize(900, 250);
    }
}
