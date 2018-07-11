package studio.visualdust.translator;

import java.io.*;

public class Main {
    private final static File dictFile = new File("C:\\Users\\Mr.GZT_Database\\Desktop\\Dictionary.dic");
    private final static String text = "CJY IS A FIRE.CJY IS A FIRE.CJY IS A FIRE.CJY IS A FIRE.CJY IS A FIRE.CJY IS A FIRE.";

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(dictFile));
        String dictionary = bufferedReader.readLine();
        bufferedReader.close();
        Translator t = new Translator("FIRE", dictionary);
        String encryptedText = t.encrypt(text, null);
        System.out.println(encryptedText);
        System.out.println(t.decrypt(encryptedText, null));
    }
}