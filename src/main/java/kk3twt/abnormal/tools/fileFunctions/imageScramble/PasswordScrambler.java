package kk3twt.abnormal.tools.fileFunctions.imageScramble;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

/**
 * 基于密码的图片块混淆工具。
 * 将图像划分为固定大小的块（16x16），根据种子打乱块的位置。
 * 提供混淆、解混淆及密码转种子的方法。
 */
public class PasswordScrambler {

    /** 块大小（边长） */
    private static final int BLOCK = 16;

    /**
     * 对图像进行块混淆。
     *
     * @param src  原始图像
     * @param seed 随机种子，决定块的重排顺序
     * @return 混淆后的新图像
     */
    public static BufferedImage scramble(BufferedImage src, long seed) {
        int w = src.getWidth();
        int h = src.getHeight();

        int bx = w / BLOCK;      // 水平方向块数
        int by = h / BLOCK;      // 垂直方向块数

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

    /**
     * 复制一个块（16x16）从源图像到目标图像。
     *
     * @param s  源图像
     * @param d  目标图像
     * @param sx 源块左上角 x 坐标
     * @param sy 源块左上角 y 坐标
     * @param dx 目标块左上角 x 坐标
     * @param dy 目标块左上角 y 坐标
     */
    private static void copyBlock(BufferedImage s, BufferedImage d,
                                  int sx, int sy, int dx, int dy) {
        for (int y = 0; y < BLOCK; y++) {
            for (int x = 0; x < BLOCK; x++) {
                int rgb = s.getRGB(sx + x, sy + y);
                d.setRGB(dx + x, dy + y, rgb);
            }
        }
    }

    /**
     * 对已混淆的图像进行还原。
     *
     * @param src  已混淆的图像
     * @param seed 与混淆时相同的种子
     * @return 还原后的图像
     */
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

    /**
     * 将密码字符串转换为 long 型种子。
     * 使用 SHA-256 哈希，取前 8 个字节作为种子。
     *
     * @param password 用户输入的密码
     * @return 用于随机数生成的种子
     */
    public static long passwordToSeed(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

            // 取前8字节转 long
            ByteBuffer buffer = ByteBuffer.wrap(hash);
            return buffer.getLong();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}