package dao;

import java.io.*;
import java.util.List;
import java.util.StringTokenizer;


public class FileToStr {
    /**
     * Permet la lecture du fichier. Nous avons pris ce code du cours de Java
     */
    private static final int EOF = -1;
    private static final String DELIMITEURS_LIGNES = "\n\r";
    private static final String DELIMITEURS_DONNEES = ";\n\r\t\f";
    private static final String DELIMITEURS_MOTS = " .,;:-+*<>%/='\"()[]{}|!?\n\r\t\f0123456789";

    private FileToStr() {
    }

    private static String read(String fileName) {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileName));
            StringBuilder b = new StringBuilder(in.available());

            for(int c = in.read(); c != -1; c = in.read()) {
                b.append((char)c);
            }

            in.close();
            return b.toString();
        } catch (FileNotFoundException var4) {
            var4.printStackTrace();
            return "";
        } catch (IOException var5) {
            var5.printStackTrace();
            return "";
        }
    }

    public static String[] lireCsv(String fileName) {
        String str = read(fileName);
        StringTokenizer sT = new StringTokenizer(str, "\n\r");
        String[] data = new String[sT.countTokens()];

        for(int cpt = 0; sT.hasMoreTokens(); ++cpt) {
            data[cpt] = sT.nextToken();
        }

        return data;
    }
}


