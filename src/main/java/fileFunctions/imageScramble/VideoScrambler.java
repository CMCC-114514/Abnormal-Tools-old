package fileFunctions.imageScramble;

import java.awt.image.BufferedImage;
import java.io.*;

public class VideoScrambler {

    private final int width;
    private final int height;
    private final int frameSize;
    private final long seed;

    public VideoScrambler(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        this.frameSize = width * height * 4; // RGBA
        System.out.println("frameSize：" + frameSize);
    }

    /* =========================
       混淆一帧
       ========================= */
    public BufferedImage scrambleFrame(BufferedImage src, int choose) {
        BufferedImage result = null;
        switch (choose) {
            case 1 -> result = PasswordScrambler.scramble(src, seed);
            case 2 -> result = HilbertScrambler.scramble(src);
        }
        return result;
    }

    /* =========================
       解混淆一帧
       ========================= */
    public BufferedImage descrambleFrame(BufferedImage src, int choose) {
        BufferedImage result = null;
        switch (choose) {
            case 1 -> result = PasswordScrambler.descramble(src, seed);
            case 2 -> result = HilbertScrambler.descramble(src);
        }
        return result;
    }

    /* =========================
       从raw RGBA读取一帧
       ========================= */
    public BufferedImage readFrame(InputStream in) throws IOException {
        byte[] buf = new byte[frameSize];
        int read = 0;

        while (read < frameSize) {
            int r = in.read(buf, read, frameSize - read);
            if (r == -1) {
                if (read == 0) return null;
                throw new EOFException("Incomplete frame");
            }
            read += r;
        }

        BufferedImage img =
                new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        int[] pixels = new int[width * height];

        int p = 0;
        for (int i = 0; i < pixels.length; i++) {
            int r = buf[p++] & 0xFF;
            int g = buf[p++] & 0xFF;
            int b = buf[p++] & 0xFF;
            int a = buf[p++] & 0xFF;

            pixels[i] = (a << 24) | (r << 16) | (g << 8) | b;
        }

        img.setRGB(0, 0, width, height, pixels, 0, width);
        return img;
    }

    /* =========================
       写入raw RGBA一帧
       ========================= */
    public void writeFrame(OutputStream out, BufferedImage img) throws IOException {
        int[] pixels = img.getRGB(0, 0, width, height, null, 0, width);

        byte[] buf = new byte[frameSize];
        int p = 0;

        for (int argb : pixels) {
            buf[p++] = (byte) ((argb >> 16) & 0xFF); // R
            buf[p++] = (byte) ((argb >> 8) & 0xFF);  // G
            buf[p++] = (byte) (argb & 0xFF);         // B
            buf[p++] = (byte) ((argb >> 24) & 0xFF); // A
        }

        out.write(buf);
    }

    /* =========================
       统计视频帧数
       ========================= */
    public static long probeFrameCount(String input) throws Exception {
        Process p = new ProcessBuilder(
                "ffprobe",
                "-v", "error",
                "-select_streams", "v:0",
                "-count_packets",
                "-show_entries", "stream=nb_read_packets",
                "-of", "csv=p=0",
                input
        ).start();

        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            return Long.parseLong(br.readLine().trim());
        }
    }

    // 获取视频帧率和分辨率
    public static int[] probeResolution(String input) throws Exception {
        Process p = new ProcessBuilder(
                "ffprobe",
                "-v", "error",
                "-select_streams", "v:0",
                "-show_entries", "stream=width,height,r_frame_rate",
                "-of", "csv=p=0",
                input
        ).start();

        try (BufferedReader br =
                     new BufferedReader(
                             new InputStreamReader(p.getInputStream()))) {

            String line = br.readLine();   // 例如：854,480,24/1

            if (line == null)
                throw new RuntimeException("ffprobe returned no data");

            String[] parts = line.split(",");

            int width = Integer.parseInt(parts[0]);
            int height = Integer.parseInt(parts[1]);

            String[] fpsParts = parts[2].split("/");

            long fps =
                    Math.round(Double.parseDouble(fpsParts[0]) /
                            Double.parseDouble(fpsParts[1]));

            return new int[]{width, height, Math.toIntExact(fps)};
        }
    }

    // 视频混淆
    public static void scramble(String input, String output,
                                int w, int h, int fps,
                                long seed,
                                int choose) throws Exception {

        long totalFrames = probeFrameCount(input);

        Process ffmpegDecode = new ProcessBuilder(
                "ffmpeg",
                "-i",input,
                "-f","rawvideo",
                "-pix_fmt","rgba",
                "-"
        )
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start();

        Process ffmpegEncode = new ProcessBuilder(
                "ffmpeg",
                "-y",
                "-f","rawvideo",
                "-pix_fmt","rgba",
                "-s", w+"x"+h,
                "-r", String.valueOf(fps),
                "-i","-",
                "-c:v","libx264",
                "-pix_fmt","yuv420p",   // 必须
                "-profile:v","high",
                "-level","4.1",
                "-movflags","+faststart",
                "-crf","18",
                "-preset","fast",
                output
        )
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start();

        VideoScrambler scrambler = new VideoScrambler(w,h,seed);

        InputStream in = ffmpegDecode.getInputStream();
        OutputStream out = ffmpegEncode.getOutputStream();

        while (true) {
            var frame = scrambler.readFrame(in);
            if (frame == null) break;

            var scrambled = scrambler.scrambleFrame(frame, choose);
            scrambler.writeFrame(out, scrambled);
        }

        out.flush();
        out.close();

        in.close();

        ffmpegDecode.waitFor();
        ffmpegEncode.waitFor();
    }

    // 视频解混淆
    public static void descramble(String input, String output,
                                  int w, int h, int fps,
                                  long seed,
                                  int choose) throws Exception {

        Process ffmpegDecode = new ProcessBuilder(
                "ffmpeg",
                "-i", input,
                "-f", "rawvideo",
                "-pix_fmt", "rgba",
                "-"
        )
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start();

        Process ffmpegEncode = new ProcessBuilder(
                "ffmpeg",
                "-y",
                "-f","rawvideo",
                "-pix_fmt","rgba",
                "-s", w+"x"+h,
                "-r", String.valueOf(fps),
                "-i","-",
                "-c:v","libx264",
                "-pix_fmt","yuv420p",   // 必须
                "-profile:v","high",
                "-level","4.1",
                "-movflags","+faststart",
                "-crf","18",
                "-preset","fast",
                output
        )
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start();

        VideoScrambler scrambler = new VideoScrambler(w, h, seed);

        InputStream in = ffmpegDecode.getInputStream();
        OutputStream out = ffmpegEncode.getOutputStream();

        while (true) {
            var frame = scrambler.readFrame(in);
            if (frame == null) break;

            var descrambled = scrambler.descrambleFrame(frame, choose);
            scrambler.writeFrame(out, descrambled);
        }

        out.flush();
        out.close();

        in.close();

        ffmpegDecode.waitFor();
        ffmpegEncode.waitFor();
    }
}