package fileFunctions.imageScramble;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class HilbertScrambler {

    /* ==========================
       Hilbert (Gilbert 2D)
       ========================== */

    public static List<int[]> gilbert2d(int width, int height) {
        List<int[]> coordinates = new ArrayList<>();

        if (width >= height) {
            generate2d(0, 0, width, 0, 0, height, coordinates);
        } else {
            generate2d(0, 0, 0, height, width, 0, coordinates);
        }

        return coordinates;
    }

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
            for (int i = 0; i < w; i++) {
                coords.add(new int[]{x, y});
                x += dax;
                y += day;
            }
            return;
        }

        if (w == 1) {
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
            if ((w2 % 2 != 0) && (w > 2)) {
                ax2 += dax;
                ay2 += day;
            }

            generate2d(x, y, ax2, ay2, bx, by, coords);
            generate2d(x + ax2, y + ay2,
                    ax - ax2, ay - ay2,
                    bx, by, coords);

        } else {

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

    /* ==========================
       加密
       ========================== */

    public static BufferedImage scramble(BufferedImage img) {
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

            dst[newIndex] = src[oldIndex];
        }

        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        out.setRGB(0, 0, w, h, dst, 0, w);
        return out;
    }

    /* ==========================
       解密
       ========================== */

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