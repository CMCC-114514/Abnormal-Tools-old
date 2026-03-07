package fileFunctions.formatConversion;

import utils.AppPath;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class VideoConverter {
    private VideoConverter(){}

    private static final String FFMPEG_PATH_SYSTEM = System.getenv("ffmpeg");
    private static final String FFMPEG_PATH = AppPath.resourcePath("ffmpeg\\ffmpeg.exe").toString();

    public static final String[] VIDEO_FORMATS = {
            "MP4", "MKV", "WEBM", "MOV", "AVI", "FLV", "TS"
    };

    public static void convert(String input, String output) throws Exception {
        String path = Files.exists(Path.of(FFMPEG_PATH_SYSTEM)) ? "ffmpeg" : FFMPEG_PATH;

        if (!Files.exists(Path.of(path))) {
            throw new Exception("没有在计算机上寻找到ffmpeg，可能需要重新下载依赖");
        }

        ProcessBuilder pb = new ProcessBuilder(
                path,
                "-y",                 // 覆盖输出
                "-i", input,          // 输入文件
                "-qscale 0",          // 维持原视频文件质量
                output
        );

        pb.redirectErrorStream(true);
        Process process = pb.start();

        // 打印 ffmpeg 日志（非常重要）
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("FFmpeg出现问题，错误码：" + exitCode);
        }
    }

    public static void chooseInputFile(JTextField inputField, JTextField outputField) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        for (String format : VIDEO_FORMATS) {
            chooser.addChoosableFileFilter(
                    new FileNameExtensionFilter(format + " 文件（*."  + format.toLowerCase() + "）", format.toLowerCase()));
        }

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            inputField.setText(chooser.getSelectedFile().getAbsolutePath());

            String inputPath = inputField.getText();
            int dot = inputPath.lastIndexOf('.');
            String base = (dot > 0) ? inputPath.substring(0, dot + 1) : inputPath + ".";
            outputField.setText(base);
        }
    }
}
