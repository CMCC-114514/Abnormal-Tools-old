package kk3twt.abnormal.tools.fileFunctions.imageScramble;

import kk3twt.abnormal.tools.utils.AppPath;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 视频混淆/解混淆的核心处理类。
 * 基于 ffmpeg 进行视频解码/编码，逐帧处理图像，并处理音频流。
 * 支持两种混淆算法：密码混淆（块重排）和希尔伯特曲线混淆。
 */
public class VideoScrambler {

    private final int width;
    private final int height;
    private final int frameSize;   // 每帧 RGBA 数据的字节数
    private final long seed;

    /** ffmpeg 可执行文件路径（优先使用环境变量，否则使用内嵌资源） */
    private static final String FFMPEG = Files.exists(Path.of(System.getenv("ffmpeg"))) ? "ffmpeg" : AppPath.resourcePath("ffmpeg\\ffmpeg.exe").toString();

    /** ffprobe 可执行文件路径 */
    private static final String FFPROBE = Files.exists(Path.of(System.getenv("ffmpeg"))) ? "ffprobe" : AppPath.resourcePath("ffmpeg\\ffprobe.exe").toString();

    /**
     * 构造视频混淆器实例。
     *
     * @param width  视频宽度
     * @param height 视频高度
     * @param seed   随机种子（用于密码混淆）
     */
    public VideoScrambler(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        this.frameSize = width * height * 4; // RGBA 每像素4字节
    }

    /**
     * 对单帧图像进行混淆。
     *
     * @param src    原始帧
     * @param choose 算法类型：0=密码混淆，1=希尔伯特混淆
     * @return 混淆后的帧
     */
    public BufferedImage scrambleFrame(BufferedImage src, int choose) {
        BufferedImage result = null;
        switch (choose) {
            case 0 -> result = PasswordScrambler.scramble(src, seed);
            case 1 -> result = HilbertScrambler.scramble(src);
        }
        return result;
    }

    /**
     * 对单帧图像进行解混淆。
     *
     * @param src    已混淆的帧
     * @param choose 算法类型：0=密码混淆，1=希尔伯特混淆
     * @return 还原后的帧
     */
    public BufferedImage descrambleFrame(BufferedImage src, int choose) {
        BufferedImage result = null;
        switch (choose) {
            case 0 -> result = PasswordScrambler.descramble(src, seed);
            case 1 -> result = HilbertScrambler.descramble(src);
        }
        return result;
    }

    /**
     * 从输入流中读取一帧 raw RGBA 数据，转换为 BufferedImage。
     *
     * @param in 输入流（来自 ffmpeg 的管道）
     * @return 图像帧，若流结束则返回 null
     * @throws IOException 读取失败或不完整时抛出
     */
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

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

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

    /**
     * 将 BufferedImage 帧写入输出流（raw RGBA 格式）。
     *
     * @param out 输出流（供 ffmpeg 编码）
     * @param img 图像帧
     * @throws IOException 写入失败时抛出
     */
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

    /**
     * 统计视频的总帧数（通过 ffprobe 读取包计数）。
     *
     * @param input 视频文件路径
     * @return 总帧数
     * @throws Exception ffprobe 执行失败或输出解析错误
     */
    public static long probeFrameCount(String input) throws Exception {
        Process p = new ProcessBuilder(
                FFPROBE,
                "-v", "error",
                "-select_streams", "v:0",
                "-count_packets",
                "-show_entries", "stream=nb_read_packets",
                "-of", "csv=p=0",
                input
        ).start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            return Long.parseLong(br.readLine().trim());
        }
    }

    /**
     * 获取视频的分辨率和帧率。
     *
     * @param input 视频文件路径
     * @return int 数组，[width, height, fps]
     * @throws Exception ffprobe 执行失败或输出解析错误
     */
    public static int[] probeResolution(String input) throws Exception {
        Process p = new ProcessBuilder(
                FFPROBE,
                "-v", "error",
                "-select_streams", "v:0",
                "-show_entries", "stream=width,height,r_frame_rate",
                "-of", "csv=p=0",
                input
        ).start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line = br.readLine(); // 例如：854,480,24/1

            if (line == null)
                throw new RuntimeException("ffprobe returned no data");

            String[] parts = line.split(",");

            int width = Integer.parseInt(parts[0]);
            int height = Integer.parseInt(parts[1]);

            String[] fpsParts = parts[2].split("/");

            long fps = Math.round(Double.parseDouble(fpsParts[0]) / Double.parseDouble(fpsParts[1]));

            return new int[]{width, height, Math.toIntExact(fps)};
        }
    }

    /**
     * 从视频中提取音频，保存为临时 AAC 文件。
     *
     * @param videoPath 视频路径
     * @return 临时音频文件
     * @throws Exception ffmpeg 执行失败
     */
    public static File extractAudio(String videoPath) throws Exception {
        File audio = File.createTempFile("audio_", ".aac");

        Process extract = new ProcessBuilder(
                FFMPEG,
                "-i", videoPath,
                "-vn",
                "-c:a", "aac",
                "-b:a", "128k",
                "-y",
                audio.getAbsolutePath()
        ).redirectError(ProcessBuilder.Redirect.INHERIT).start();

        int exitCode = extract.waitFor();
        if (exitCode != 0) {
            throw new IOException("音频提取失败：error = " + exitCode);
        }
        return audio;
    }

    /**
     * 将处理后的视频与之前提取的音频合并。
     *
     * @param processedVideo 已处理的无音频视频文件路径
     * @param audio          音频文件
     * @param output         最终输出视频路径
     * @throws Exception ffmpeg 执行失败
     */
    public static void mergeAudio(String processedVideo, File audio, String output) throws Exception {
        Process merge = new ProcessBuilder(
                FFMPEG,
                "-i", processedVideo,
                "-i", audio.getAbsolutePath(),
                "-c", "copy",
                "-map", "0:v:0",
                "-map", "1:a:0",
                "-y",
                output
        ).redirectError(ProcessBuilder.Redirect.INHERIT).start();

        int exitCode = merge.waitFor();
        if (exitCode != 0) {
            throw new IOException("音频合并失败：error = " + exitCode);
        }
    }

    /**
     * 对视频进行混淆。
     *
     * @param origin   原始视频路径
     * @param output   输出视频路径
     * @param w        视频宽度
     * @param h        视频高度
     * @param fps      视频帧率
     * @param seed     随机种子（密码混淆用）
     * @param choose   算法类型：0=密码混淆，1=希尔伯特混淆
     * @param callback 进度回调，每处理一帧调用一次
     * @throws Exception 处理过程中可能抛出的异常
     */
    public static void scramble(String origin, String output,
                                int w, int h, int fps,
                                long seed,
                                int choose,
                                ProgressCallback callback) throws Exception {

        String shuffled = "shuffled.mp4";
        File audio = extractAudio(origin);

        Process ffmpegDecode = new ProcessBuilder(
                FFMPEG,
                "-i", origin,
                "-f", "rawvideo",
                "-pix_fmt", "rgba",
                "-"
        ).redirectError(ProcessBuilder.Redirect.INHERIT).start();

        Process ffmpegEncode = new ProcessBuilder(
                FFMPEG,
                "-y",
                "-f", "rawvideo",
                "-pix_fmt", "rgba",
                "-s", w + "x" + h,
                "-r", String.valueOf(fps),
                "-i", "-",
                "-c:v", "libx264",
                "-pix_fmt", "yuv420p",
                "-profile:v", "high",
                "-level", "4.1",
                "-movflags", "+faststart",
                "-crf", "18",
                "-preset", "fast",
                shuffled
        ).redirectError(ProcessBuilder.Redirect.INHERIT).start();

        VideoScrambler scrambler = new VideoScrambler(w, h, seed);

        InputStream in = ffmpegDecode.getInputStream();
        OutputStream out = ffmpegEncode.getOutputStream();

        long processed = 0;

        while (true) {
            var frame = scrambler.readFrame(in);
            if (frame == null) break;

            var scrambled = scrambler.scrambleFrame(frame, choose);
            scrambler.writeFrame(out, scrambled);

            processed++;

            if (callback != null) {
                callback.onProgress(processed);
            }
        }

        out.flush();
        out.close();
        in.close();

        ffmpegDecode.waitFor();
        ffmpegEncode.waitFor();

        mergeAudio(shuffled, audio, output);

        Files.delete(Path.of(shuffled));
        Files.delete(audio.toPath());
    }

    /**
     * 对视频进行解混淆（还原）。
     *
     * @param origin   已混淆的视频路径
     * @param output   输出视频路径
     * @param w        视频宽度
     * @param h        视频高度
     * @param fps      视频帧率
     * @param seed     随机种子（需与混淆时相同）
     * @param choose   算法类型：0=密码混淆，1=希尔伯特混淆
     * @param callback 进度回调，每处理一帧调用一次
     * @throws Exception 处理过程中可能抛出的异常
     */
    public static void descramble(String origin, String output,
                                  int w, int h, int fps,
                                  long seed,
                                  int choose,
                                  ProgressCallback callback) throws Exception {

        String shuffled = "shuffled.mp4";
        File audio = extractAudio(origin);

        Process ffmpegDecode = new ProcessBuilder(
                FFMPEG,
                "-i", origin,
                "-f", "rawvideo",
                "-pix_fmt", "rgba",
                "-"
        ).redirectError(ProcessBuilder.Redirect.INHERIT).start();

        Process ffmpegEncode = new ProcessBuilder(
                FFMPEG,
                "-y",
                "-f", "rawvideo",
                "-pix_fmt", "rgba",
                "-s", w + "x" + h,
                "-r", String.valueOf(fps),
                "-i", "-",
                "-c:v", "libx264",
                "-pix_fmt", "yuv420p",
                "-profile:v", "high",
                "-level", "4.1",
                "-movflags", "+faststart",
                "-crf", "18",
                "-preset", "fast",
                shuffled
        ).redirectError(ProcessBuilder.Redirect.INHERIT).start();

        VideoScrambler scrambler = new VideoScrambler(w, h, seed);

        InputStream in = ffmpegDecode.getInputStream();
        OutputStream out = ffmpegEncode.getOutputStream();

        long processed = 0;

        while (true) {
            var frame = scrambler.readFrame(in);
            if (frame == null) break;

            var descrambled = scrambler.descrambleFrame(frame, choose);
            scrambler.writeFrame(out, descrambled);

            processed++;

            if (callback != null) {
                callback.onProgress(processed);
            }
        }

        out.flush();
        out.close();
        in.close();

        ffmpegDecode.waitFor();
        ffmpegEncode.waitFor();

        mergeAudio(shuffled, audio, output);

        Files.delete(Path.of(shuffled));
        Files.delete(audio.toPath());
    }

    /**
     * 进度回调接口，用于实时更新处理进度。
     */
    public interface ProgressCallback {
        /**
         * 当处理完一帧时调用。
         *
         * @param processedFrames 已处理的帧数
         */
        void onProgress(long processedFrames);
    }
}