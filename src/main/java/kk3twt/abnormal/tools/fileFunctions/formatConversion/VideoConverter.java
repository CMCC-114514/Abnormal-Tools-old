package kk3twt.abnormal.tools.fileFunctions.formatConversion;

import kk3twt.abnormal.tools.utils.AppPath;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 视频格式转换工具类，基于 FFmpeg 实现。
 * 提供视频格式列表、转换方法以及文件选择辅助方法。
 */
public class VideoConverter {
    private VideoConverter() {} // 私有构造，防止实例化

    /** 从环境变量中获取的 FFmpeg 路径（可能为 null） */
    private static final String FFMPEG_PATH_SYSTEM = System.getenv("ffmpeg");

    /** 内置于资源文件夹的 FFmpeg 路径（用于环境变量不存在时） */
    private static final String FFMPEG_PATH = AppPath.resourcePath("ffmpeg\\ffmpeg.exe").toString();

    /** 支持的视频输出格式列表（扩展名） */
    public static final String[] VIDEO_FORMATS = {
            "MP4", "MKV", "WEBM", "MOV", "AVI", "FLV", "TS"
    };

    /**
     * 执行视频格式转换。
     *
     * @param input  输入文件路径
     * @param output 输出文件路径（应包含目标扩展名）
     * @throws Exception 如果 FFmpeg 未找到、转换过程失败或被中断
     */
    public static void convert(String input, String output) throws Exception {

        // 判定计算机是否已经配置ffmpeg为系统变量，优先使用系统自带
        boolean envFileExists = FFMPEG_PATH_SYSTEM != null && Files.exists(Path.of(FFMPEG_PATH_SYSTEM));
        String ffmpeg = envFileExists ? FFMPEG_PATH_SYSTEM : FFMPEG_PATH;

        ProcessBuilder pb = new ProcessBuilder(
                ffmpeg,
                "-y",                 // 覆盖输出文件
                "-i", input,          // 指定输入文件
                "-qscale 0",          // 尽量保持原视频质量（针对某些编码器）
                output
        );

        pb.redirectErrorStream(true); // 将错误流合并到标准输出，便于日志查看
        Process process = pb.start();

        // 读取并打印 FFmpeg 输出日志（便于调试）
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

    /**
     * 弹出文件选择对话框，让用户选择视频输入文件。
     * 选中后自动将输入路径填入 inputField，并在 outputField 中预填同路径无扩展名的基本名称（用于拼接输出格式）。
     *
     * @param inputField  用于显示所选输入文件路径的文本框
     * @param outputField 用于显示输出基本路径的文本框（不含扩展名）
     */
    public static void chooseInputFile(JTextField inputField, JTextField outputField) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        for (String format : VIDEO_FORMATS) {
            chooser.addChoosableFileFilter(
                    new FileNameExtensionFilter(format + " 文件（*." + format.toLowerCase() + "）", format.toLowerCase()));
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