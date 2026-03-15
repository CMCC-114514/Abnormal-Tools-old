package kk3twt.abnormal.tools.fileFunctions.imageScramble;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

public class PasswordScrambler {

    private static final int BLOCK = 16; // 块大小

    public static BufferedImage scramble(BufferedImage src, long seed) {
        int w = src.getWidth();
        int h = src.getHeight();

        int bx = w / BLOCK;
        int by = h / BLOCK;

        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < bx * by; i++) order.add(i);

        Collections.shuffle(order, new Random(seed));

        BufferedImage dst = new BufferedImage(w, h, src.getType());

        for (int i = 0; i < order.size(); i++) {
            int sx = (i % bx) * BLOCK;
            int sy = (i / bx) * BLOCK;

            int j = order.get(i);
            int dx = (j % bx) * BLOCK;
            int dy = (j / bx) * BLOCK;

            copyBlock(src, dst, sx, sy, dx, dy);
        }
        return dst;
    }

    private static void copyBlock(BufferedImage s, BufferedImage d,
                                  int sx, int sy, int dx, int dy) {
        for (int y = 0; y < BLOCK; y++) {
            for (int x = 0; x < BLOCK; x++) {
                int rgb = s.getRGB(sx + x, sy + y);
                d.setRGB(dx + x, dy + y, rgb);
            }
        }
    }

    public static BufferedImage descramble(BufferedImage src, long seed) {
        int w = src.getWidth();
        int h = src.getHeight();

        int bx = w / BLOCK;
        int by = h / BLOCK;

        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < bx * by; i++) order.add(i);

        Collections.shuffle(order, new Random(seed));

        BufferedImage dst = new BufferedImage(w, h, src.getType());

        for (int i = 0; i < order.size(); i++) {
            int j = order.get(i);

            int sx = (j % bx) * BLOCK;
            int sy = (j / bx) * BLOCK;

            int dx = (i % bx) * BLOCK;
            int dy = (i / bx) * BLOCK;

            copyBlock(src, dst, sx, sy, dx, dy);
        }
        return dst;
    }

    public static long passwordToSeed(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

            // 取前8字节转long
            ByteBuffer buffer = ByteBuffer.wrap(hash);
            return buffer.getLong();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}