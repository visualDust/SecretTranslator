package studio.visualdust.translator;

import studio.visualdust.translator.res.Resource;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import static studio.visualdust.translator.Numbers.map;

public class TranslatorGUI extends JFrame {
    private JPanel panel1;
    private JTextField textFieldInput;
    private JPasswordField passwordField;
    private JRadioButton encryptRadioButton;
    private JRadioButton decryptRadioButton;
    private JButton translateButton;
    private JTextField textFieldResult;
    private JLabel labelEvent;
    private JProgressBar progressBar;
    private JButton pasteButton;
    private JButton copyButton;
    private JProgressBar progressBarRAM;
    private JLabel LabelLength;
    private JLabel LabelRAM;
    private JCheckBox CheckBoxFastTranslate;
    private Clipboard clipboard;

    public TranslatorGUI() {
        add(panel1);
        try {
            setIconImage(new ImageIcon(Resource.class.getResource("ICONIMG.png").toURI().toURL()).getImage());
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
        panel1.setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        translateButton.addActionListener(new PressDownActionListener());
        pasteButton.addActionListener(new PasteActionListener());
        copyButton.addActionListener(new CopyActionListener());
        textFieldInput.getDocument().addDocumentListener(new StringChangeListener());
        passwordField.getDocument().addDocumentListener(new StringChangeListener());
        encryptRadioButton.setSelected(true);
        decryptRadioButton.setSelected(false);
        encryptRadioButton.addActionListener(e -> decryptRadioButton.setSelected(!encryptRadioButton.isSelected()));
        decryptRadioButton.addActionListener(e -> encryptRadioButton.setSelected(!decryptRadioButton.isSelected()));
        progressBar.setBorderPainted(false);
        progressBar.setStringPainted(true);
        progressBarRAM.setBorderPainted(false);
        textFieldResult.setDragEnabled(true);
        CheckBoxFastTranslate.setSelected(true);
        progressBarRAM.setForeground(new Color(187, 137, 0));
        progressBarRAM.setStringPainted(true);
        textFieldInput.setText("This Program was Created By Mr.GZT & PaperCube !!!");
        passwordField.setText("Test");
        TranslateThread startup = new TranslateThread();
        startup.run();
        labelEvent.setText("我不会告诉你密码是\"Test\"的");
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        RuntimeThread runtime = new RuntimeThread();
        runtime.start();
    }

    private boolean isEncryptMode() {
        return encryptRadioButton.isSelected();
    }

    class PressDownActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            TranslateThread tt = new TranslateThread();
            if (textFieldInput.getText().length() != 0 && passwordField.getPassword().length != 0) {
                tt.start();
            } else {
                labelEvent.setForeground(Color.red);
                labelEvent.setText("你没有输入要翻译的句子或密码。");
            }
        }
    }

    class PasteActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                try {
                    textFieldInput.setText((String) clipboard.getData(DataFlavor.stringFlavor));
                } catch (UnsupportedFlavorException | IOException | ClassCastException e1) {
                    e1.printStackTrace();
                }
            } else {
                labelEvent.setForeground(Color.red);
                labelEvent.setText("没有什么可以粘贴的内容。");
            }
        }
    }


    class StringChangeListener implements DocumentChangedListener {
        boolean isclearing = false;

        @Override
        public void anythingChanged(DocumentEvent event) {
            try {
                if (progressBar.getValue() != 0 && !isclearing) {
                    isclearing = true;
                    ClearThread clearer = new ClearThread();
                    clearer.start();
                }
                labelEvent.setText("我现在很闲，你最好给我找点事干......");
                LabelLength.setText("句子长度：" + String.valueOf(textFieldInput.getText().length()));
            } catch (Exception e2) {
                progressBar.setForeground(Color.red);
                labelEvent.setForeground(Color.red);
                labelEvent.setText("糟糕！好像出了点小问题：" + e2.toString());
            }
        }

        class ClearThread extends Thread {
            @Override
            public void run() {
                try {
                    progressBar.setMaximum(250);
                    progressBar.setValue(250);
                    for (int i = progressBar.getValue(); i >= 0; i--) {
                        sleep(1);
                        progressBar.setValue(i);
                    }
                } catch (Exception e2) {
                    progressBar.setForeground(Color.red);
                    labelEvent.setForeground(Color.red);
                    labelEvent.setText("糟糕！好像出了点小问题：" + e2.toString());
                }
                isclearing = false;
            }
        }
    }

    class CopyActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String text = textFieldResult.getText();
            if (!text.isEmpty()) {
                Transferable transferable = new StringSelection(text);
                clipboard.setContents(transferable, null);
                labelEvent.setForeground(Color.BLACK);
                labelEvent.setText("复制成功！");
            } else {
                labelEvent.setForeground(Color.red);
                labelEvent.setText("没有什么可以复制的内容。");
            }
        }
    }

    class RuntimeThread extends Thread {
        Runtime nowruntime = Runtime.getRuntime();
        long usagemin = 1199999999;

        public void run() {
            try {
                while (true) {
                    if (nowruntime.maxMemory() - nowruntime.freeMemory() < usagemin) {
                        usagemin = nowruntime.maxMemory() - nowruntime.freeMemory();
                        progressBarRAM.setMaximum((int) (nowruntime.maxMemory() - usagemin));
                    }
                    LabelRAM.setText("JVM内存占用：" + String.valueOf(nowruntime.maxMemory() - nowruntime.freeMemory()));
                    final int tmpbasic = map((int) (nowruntime.freeMemory()), 0, (int) (nowruntime.maxMemory() - usagemin), 0, 170);
                    progressBarRAM.setForeground(new Color((int) (255 - tmpbasic), (int) (0.8 * tmpbasic), 50));
                    progressBarRAM.setValue((int) (nowruntime.freeMemory()));
                    Thread.sleep(100);
                }
            } catch (Exception e1) {
                progressBar.setForeground(Color.red);
                labelEvent.setForeground(Color.red);
                labelEvent.setText("糟糕！好像出了点小问题：" + e1.toString());
            }
        }
    }

    class TranslateThread extends Thread {
        @Override
        public void run() {
            textFieldResult.setEnabled(false);
            encryptRadioButton.setEnabled(false);
            decryptRadioButton.setEnabled(false);
            passwordField.setEnabled(false);
            progressBar.setForeground(new Color(0, 124, 0));
            labelEvent.setForeground(Color.black);
            translateButton.setEnabled(false);
            pasteButton.setEnabled(false);
            copyButton.setEnabled(false);
            CheckBoxFastTranslate.setEnabled(false);
            LabelLength.setText("句子长度：" + textFieldInput.getText().length());
            textFieldInput.setEnabled(false);
            progressBar.setValue(0);
            try {
                labelEvent.setText("程序正在准备............");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Resource.class.getResourceAsStream("Dictionary.dic"), Charset.forName("UTF-8")));
                String dictionary = bufferedReader.readLine();
                BufferedReader stden = new BufferedReader(new InputStreamReader(Resource.class.getResourceAsStream("EN_STD.dic"), Charset.forName("UTF-8")));
                String standarden = stden.readLine();
                boolean checken = true;
                final String textInput = textFieldInput.getText();
                for (int i = 0; i <= textInput.length() - 1; i++) {
                    boolean tmpche = false;
                    for (int j = 0; j <= standarden.length() - 1; j++) {
                        if (textInput.charAt(i) == standarden.charAt(j)) {
                            tmpche = true;
                            break;
                        }
                    }
                    if (!tmpche) {
                        checken = false;
                        break;
                    }
                }
                if (checken) {
                    dictionary = standarden;
                }
                final Translator t = new Translator(String.valueOf(passwordField.getPassword()), dictionary);
                final String resultStr = isEncryptMode() ? t.encrypt(textInput, null) : t.decrypt(textInput, null);
                labelEvent.setForeground(Color.blue);
                labelEvent.setText("正在玩命翻译中............");
                textFieldResult.setText("");
                progressBar.setValue(0);
                final int resultStrLength = resultStr.length();
                progressBar.setMaximum(resultStrLength);
                if (CheckBoxFastTranslate.isSelected()) {
                    String tmpline = null;
                    for (int i = resultStrLength - 1; i >= 0; i--) {
                        final int tmp = map(i, 0, resultStrLength - 1, 0, 255);
                        progressBar.setForeground(new Color(tmp, 255 - tmp, 100));
                        tmpline = resultStr.charAt(i) + tmpline;
                        textFieldResult.setText(tmpline);
                        Thread.sleep(3L + (long) (Math.random() * 5));
                        progressBar.setValue(resultStrLength - i);
                    }
                }
                textFieldResult.setText(resultStr);
                progressBar.setValue(resultStrLength);
                labelEvent.setForeground(Color.blue);
                labelEvent.setText("翻译完成！");
            } catch (Translator.CharNotFoundException eCNF) {
                progressBar.setForeground(Color.red);
                labelEvent.setForeground(Color.red);
                labelEvent.setText(String.format("句子中的字符 \'%s\' 在字典中找不到。", eCNF.c2));
            } catch (Exception e1) {
                progressBar.setForeground(Color.red);
                labelEvent.setForeground(Color.red);
                labelEvent.setText("糟糕！好像出了点小问题：" + e1.toString());
            }finally {
                translateButton.setEnabled(true);
                pasteButton.setEnabled(true);
                copyButton.setEnabled(true);
                encryptRadioButton.setEnabled(true);
                decryptRadioButton.setEnabled(true);
                textFieldInput.setEnabled(true);
                passwordField.setEnabled(true);
                textFieldResult.setEnabled(true);
                CheckBoxFastTranslate.setEnabled(true);
            }
        }
    }
}

class Numbers {
    public static int map(int value, int fromRangeL, int fromRangeR, int toRangeL, int toRangeR) {
        int fromRangeLength = fromRangeR - fromRangeL;
        int toRangeLength = toRangeR - toRangeL;
        int offset = value - fromRangeL;
        return (int) (fromRangeL + ((double) offset) / fromRangeLength * toRangeLength);
    }
}
