package studio.visualdust.product;
import studio.visualdust.product.translator.TranslatorGUI;

import javax.swing.*;

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
            for(int i=400;i>=350;i--)
            {
                Thread.sleep(5);
                tGUI.setSize(650+i, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        tGUI.setSize(900, 250);
    }
}
