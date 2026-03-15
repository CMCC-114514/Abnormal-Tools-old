package kk3twt.abnormal.tools.otherFunctions.base64;

import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class Bas64 {

    public static final String[] PATTERNS = {"编码", "解码"};

    public static String enCode(String originalInput) {
        return Base64.getEncoder().encodeToString(
                originalInput.getBytes(StandardCharsets.UTF_8)
        );
    }

    public static String deCode(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
