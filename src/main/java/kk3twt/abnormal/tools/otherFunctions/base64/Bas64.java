package kk3twt.abnormal.tools.otherFunctions.base64;

import java.util.Base64;
import java.nio.charset.StandardCharsets;

/**
 * Base64 编解码工具类。
 * 提供基于 UTF-8 字符集的 Base64 编码和解码功能。
 * 使用 Java 8 引入的 java.util.Base64 工具。
 */
public class Bas64 {

    /**
     * 界面中可选择的转换模式：编码、解码。
     */
    public static final String[] PATTERNS = {"编码", "解码"};

    /**
     * 将原始字符串进行 Base64 编码。
     *
     * @param originalInput 原始字符串（UTF-8 编码）
     * @return Base64 编码后的字符串
     */
    public static String enCode(String originalInput) {
        return Base64.getEncoder().encodeToString(
                originalInput.getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * 将 Base64 编码字符串解码为原始字符串。
     *
     * @param encodedString Base64 编码的字符串
     * @return 解码后的原始字符串（UTF-8 编码）
     * @throws IllegalArgumentException 如果输入字符串不是合法的 Base64 格式
     */
    public static String deCode(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}