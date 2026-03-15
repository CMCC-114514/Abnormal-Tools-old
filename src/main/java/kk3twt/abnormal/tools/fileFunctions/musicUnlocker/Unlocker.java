package kk3twt.abnormal.tools.fileFunctions.musicUnlocker;

import kk3twt.abnormal.tools.utils.AppPath;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class Unlocker {
    private static final String UM_PATH = AppPath.resourcePath("um\\um.exe").toString();

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

    public static void decrypt(String input, String output) throws Exception {
        if (!Files.exists(Path.of(UM_PATH))) {
            throw new Exception("没有在计算机上寻找到um，可能需要重新下载依赖");
        }

        ProcessBuilder pb = new ProcessBuilder(
                UM_PATH,
                "-o", output,
                "-i", input
        );

        pb.redirectErrorStream(true);
        Process process = pb.start();

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

    public static String getSupportedFormat() {
        StringBuilder info = new StringBuilder();
        for (String audioFormat : DECRYPTED_FORMAT) {
            info.append("  *").append(".").append(audioFormat).append("\n");
        }
        return info.toString();
    }
}
