package kk3twt.abnormal.tools.otherFunctions.randomGenerator;

import java.util.Random;

/**
 * 随机数及随机字符串生成工具类。
 * 提供生成指定范围内的随机整数、从指定字符集合中生成随机字符串的功能。
 * 内部维护了数字、大写字母、小写字母、标点符号的字符集常量。
 */
public class Generators {

    /** 随机数生成器实例，供所有静态方法共用 */
    static Random rd = new Random();

    /**
     * 生成指定闭区间 [min, max] 内的随机整数。
     *
     * @param min 最小值（包含）
     * @param max 最大值（包含）
     * @return 生成的随机整数
     */
    public static int randomNum(int min, int max) {
        return rd.nextInt(min, max + 1);
    }

    /** 数字字符集（0-9） */
    public static String NUM;

    /** 大写字母字符集（A-Z） */
    public static String CAPITAL;

    /** 小写字母字符集（a-z） */
    public static String LOWERCASE;

    /** 标点符号字符集（包含可打印的 ASCII 标点符号） */
    public static String PUNCTUATIONS;

    /**
     * 初始化字符集
     */
    public static void init() {
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
            // 跳过数字、大写字母、小写字母，其余视为标点符号
            if (j < 48 || (j > 57 && j < 65) || (j > 90 && j < 97) || j > 122) {
                builder.append((char) j);
            }
        }
        PUNCTUATIONS = builder.toString();
    }

    /**
     * 从给定的字符集合中随机生成长度为 length 的字符串。
     *
     * @param length      生成的字符串长度
     * @param characters  可用的字符集合
     * @return 生成的随机字符串
     */
    public static String randomStr(int length, String characters) {
        int strLength = characters.length();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append(characters.charAt(rd.nextInt(strLength)));
        }

        return builder.toString();
    }
}