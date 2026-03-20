package kk3twt.abnormal.tools.fileFunctions.musicUnlocker;

import kk3twt.abnormal.tools.utils.AppPath;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 音乐解锁工具类，基于外部程序 um.exe 实现。
 * 支持多种加密音乐格式的解密，将加密文件转换为普通音频文件。
 */
public class Unlocker {

    /** 内置于资源文件夹的 um.exe 路径 */
    private static final String UM_PATH = AppPath.resourcePath("um\\um.exe").toString();

    /** 支持解密的加密音频格式列表（扩展名） */
    public static final String[] DECRYPTED_FORMAT = {
            "QMC0", "QMC2", "QMC3", "QMCFLAC", "QMCOGG", "TKM",
            "BKCMP3", "BKCFLAC",
            "TM0", "TM2", "TM3", "TM6",
            "MFLAC", "MGG", "MFLAC0", "MGG1", "MGGL",
            "OFL_EN",
            "NCM",
            "XM",
            "KWM",
            "KGM", "VPR",
            "X2M", "X3M",
            "MG3D"
    };

    /**
     * 执行解密操作。
     *
     * @param input  输入文件路径（加密音频）
     * @param output 输出文件路径（解密后的音频，通常为 .mp3 或 .flac 等）
     * @throws Exception 如果 um.exe 不存在、执行失败或进程被中断
     */
    public static void decrypt(String input, String output) throws Exception {
        if (!Files.exists(Path.of(UM_PATH))) {
            throw new Exception("没有在计算机上寻找到um，可能需要重新下载依赖");
        }

        ProcessBuilder pb = new ProcessBuilder(
                UM_PATH,
                "-o", output,
                "-i", input
        );

        pb.redirectErrorStream(true); // 合并错误流到标准输出，便于日志查看
        Process process = pb.start();

        // 读取并打印 um 的输出日志（便于调试）
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("um failed with code " + exitCode);
        }
    }

    /**
     * 获取支持的解密格式列表，格式化为易读的字符串。
     *
     * @return 包含所有支持格式的文本，每行一个格式，带点号前缀
     */
    public static String getSupportedFormat() {
        StringBuilder info = new StringBuilder();
        for (String audioFormat : DECRYPTED_FORMAT) {
            info.append("  *").append(".").append(audioFormat).append("\n");
        }
        return info.toString();
    }
}