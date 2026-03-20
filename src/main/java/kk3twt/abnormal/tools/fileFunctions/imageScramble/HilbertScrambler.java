package kk3twt.abnormal.tools.fileFunctions.imageScramble;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于希尔伯特曲线（实际为吉尔伯特二维曲线）的图片混淆/解混淆工具。
 * 通过将像素按照二维空间填充曲线的顺序重新排列，实现图像的置乱。
 * 该类提供静态方法直接对 BufferedImage 进行混淆或还原。
 */
public class HilbertScrambler {

    /**
     * 生成覆盖指定宽度和高度的吉尔伯特二维曲线坐标序列。
     * 曲线从 (0,0) 开始，遍历所有像素恰好一次。
     *
     * @param width  图像宽度
     * @param height 图像高度
     * @return 坐标列表，每个元素为 int[]{x, y}
     */
    public static List<int[]> gilbert2d(int width, int height) {
        List<int[]> coordinates = new ArrayList<>();

        if (width >= height) {
            generate2d(0, 0, width, 0, 0, height, coordinates);
        } else {
            generate2d(0, 0, 0, height, width, 0, coordinates);
        }

        return coordinates;
    }

    /**
     * 递归生成吉尔伯特曲线的内部方法。
     *
     * @param x      当前起始点 x 坐标
     * @param y      当前起始点 y 坐标
     * @param ax     a 向量 x 分量
     * @param ay     a 向量 y 分量
     * @param bx     b 向量 x 分量
     * @param by     b 向量 y 分量
     * @param coords 存储生成的坐标的列表
     */
    private static void generate2d(int x, int y,
                                   int ax, int ay,
                                   int bx, int by,
                                   List<int[]> coords) {

        int w = Math.abs(ax + ay);
        int h = Math.abs(bx + by);

        int dax = Integer.signum(ax);
        int day = Integer.signum(ay);
        int dbx = Integer.signum(bx);
        int dby = Integer.signum(by);

        if (h == 1) {
            // 只有一行，沿 a 方向逐个添加
            for (int i = 0; i < w; i++) {
                coords.add(new int[]{x, y});
                x += dax;
                y += day;
            }
            return;
        }

        if (w == 1) {
            // 只有一列，沿 b 方向逐个添加
            for (int i = 0; i < h; i++) {
                coords.add(new int[]{x, y});
                x += dbx;
                y += dby;
            }
            return;
        }

        int ax2 = ax / 2;
        int ay2 = ay / 2;
        int bx2 = bx / 2;
        int by2 = by / 2;

        int w2 = Math.abs(ax2 + ay2);
        int h2 = Math.abs(bx2 + by2);

        if (2 * w > 3 * h) {
            // 水平分割
            if ((w2 % 2 != 0) && (w > 2)) {
                ax2 += dax;
                ay2 += day;
            }

            generate2d(x, y, ax2, ay2, bx, by, coords);
            generate2d(x + ax2, y + ay2,
                    ax - ax2, ay - ay2,
                    bx, by, coords);

        } else {
            // 垂直分割
            if ((h2 % 2 != 0) && (h > 2)) {
                bx2 += dbx;
                by2 += dby;
            }

            generate2d(x, y, bx2, by2, ax2, ay2, coords);
            generate2d(x + bx2, y + by2,
                    ax, ay,
                    bx - bx2, by - by2, coords);

            generate2d(
                    x + (ax - dax) + (bx2 - dbx),
                    y + (ay - day) + (by2 - dby),
                    -bx2, -by2,
                    -(ax - ax2), -(ay - ay2),
                    coords
            );
        }
    }

    /**
     * 对图像进行希尔伯特曲线混淆。
     * 利用黄金比例产生偏移量，将像素从原曲线位置移动到新曲线位置。
     *
     * @param img 原始图像（建议使用 ARGB 类型）
     * @return 混淆后的新图像
     */
    public static BufferedImage scramble(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        int N = w * h;

        int[] src = img.getRGB(0, 0, w, h, null, 0, w);
        int[] dst = new int[N];

        List<int[]> curve = gilbert2d(w, h);
        // 使用黄金比例生成偏移量
        int offset = (int) Math.round(((Math.sqrt(5) - 1) / 2.0) * N);

        for (int i = 0; i < N; i++) {
            int[] oldPos = curve.get(i);
            int[] newPos = curve.get((i + offset) % N);

            int oldIndex = oldPos[0] + oldPos[1] * w;
            int newIndex = newPos[0] + newPos[1] * w;

            dst[newIndex] = src[oldIndex];
        }

        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        out.setRGB(0, 0, w, h, dst, 0, w);
        return out;
    }

    /**
     * 对已混淆的图像进行还原（解混淆）。
     * 使用与 {@link #scramble(BufferedImage)} 相同的偏移量逆向操作。
     *
     * @param img 已混淆的图像
     * @return 还原后的图像
     */
    public static BufferedImage descramble(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        int N = w * h;

        int[] src = img.getRGB(0, 0, w, h, null, 0, w);
        int[] dst = new int[N];

        List<int[]> curve = gilbert2d(w, h);
        int offset = (int) Math.round(((Math.sqrt(5) - 1) / 2.0) * N);

        for (int i = 0; i < N; i++) {
            int[] oldPos = curve.get(i);
            int[] newPos = curve.get((i + offset) % N);

            int oldIndex = oldPos[0] + oldPos[1] * w;
            int newIndex = newPos[0] + newPos[1] * w;

            dst[oldIndex] = src[newIndex];
        }

        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        out.setRGB(0, 0, w, h, dst, 0, w);
        return out;
    }
}