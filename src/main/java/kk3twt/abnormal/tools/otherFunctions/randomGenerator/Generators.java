package kk3twt.abnormal.tools.otherFunctions.randomGenerator;

import java.util.Random;

public class Generators {
    static Random rd = new Random();

    public static int randomNum(int min, int max) {
        return rd.nextInt(min, max + 1);
    }

    public static String NUM;
    public static String CAPITAL;
    public static String LOWERCASE;
    public static String PUNCTUATIONS;

    public Generators() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            builder.append(i);
        }
        NUM = builder.toString();

        builder = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            builder.append((char) (i + 65));
        }
        CAPITAL = builder.toString();

        builder = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            builder.append((char) (i + 97));
        }
        LOWERCASE = builder.toString();

        int j;
        builder = new StringBuilder();
        for (int i = 0; i < 94; i++) {
            j = i + 33;
            if (j < 48 || (j > 57 && j < 65) || (j > 90 && j < 97) || j > 122) {
                builder.append((char) j);
            }
        }
        PUNCTUATIONS = builder.toString();
    }

    public static String randomStr(int length, String characters) {
        int strLength = characters.length();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append(characters.charAt(rd.nextInt(strLength)));
        }

        return builder.toString();
    }
}
