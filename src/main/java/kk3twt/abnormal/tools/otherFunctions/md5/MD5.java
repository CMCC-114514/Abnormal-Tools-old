package kk3twt.abnormal.tools.otherFunctions.md5;

import java.security.MessageDigest;

public class MD5 {
    public static String getMD5(String input) throws Exception {

        // 创建MessageDigest实例
        MessageDigest md = MessageDigest.getInstance("MD5");

        // 计算摘要（返回字节数组）
        byte[] digest = md.digest(input.getBytes("UTF-8"));

        // 将字节数组转换为十六进制字符串
        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            // 确保每字节显示为2位十六进制（不足补0）
            hexString.append(String.format("%02x", b & 0xff));
        }

        return hexString.toString();
    }
}
