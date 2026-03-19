package kk3twt.abnormal.tools.otherFunctions.md5;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * MD5 摘要计算工具类。
 * 提供静态方法将输入字符串转换为 MD5 哈希值（32 位十六进制字符串）。
 */
public class MD5 {

    /**
     * 计算给定字符串的 MD5 摘要。
     *
     * @param input 要计算 MD5 的原始字符串
     * @return 32 位小写十六进制表示的 MD5 哈希值
     * @throws Exception 如果算法不可用或字符编码不支持等底层错误
     */
    public static String getMD5(String input) throws Exception {

        // 1. 获取 MD5 算法的 MessageDigest 实例
        MessageDigest md = MessageDigest.getInstance("MD5");

        // 2. 将输入字符串按 UTF-8 编码转换为字节数组，并计算摘要
        byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));

        // 3. 将字节数组转换为十六进制字符串
        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            // 将每个字节与 0xff 进行与运算，确保无符号整数，再格式化为两位十六进制
            hexString.append(String.format("%02x", b & 0xff));
        }

        return hexString.toString();
    }
}