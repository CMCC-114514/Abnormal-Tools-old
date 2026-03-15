package kk3twt.abnormal.tools.utils;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.zip.*;

/**
 * 带 Swing GUI 进度条的 ZIP 解压器
 */
public class ZipExtractor extends SwingWorker<Void, Integer> {

    private final Path filePath;
    private final Path target;

    private JDialog progressDialog;
    private JProgressBar progressBar;
    private JLabel progressLabel;

    private final java.nio.charset.Charset zipCharset;

    private long totalBytes = 0;
    private long extractedBytes = 0;

    public ZipExtractor(Path filePath, Path target) {
        this.filePath = filePath;
        this.target = target;

        this.zipCharset = detectCharset(filePath);

        initProgressUI();
    }

    /** 初始化进度条窗口 */
    private void initProgressUI() {
        progressDialog = new JDialog((Frame) null, "正在解压依赖包", true);
        progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        progressDialog.setSize(400, 120);
        progressDialog.setLocationRelativeTo(null);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        progressLabel = new JLabel("准备解压...");

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(progressLabel, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);

        progressDialog.setContentPane(panel);
    }

    /**
     * 防止 Zip Slip 攻击
     */
    private static Path resolveZipEntry(Path destDir, ZipEntry entry) throws IOException {
        Path resolvedPath = destDir.resolve(entry.getName()).normalize();

        if (!resolvedPath.startsWith(destDir)) {
            throw new IOException("非法压缩条目: " + entry.getName());
        }

        return resolvedPath;
    }

    /** 预扫描 ZIP 获取总字节数 */
    private void calculateTotalBytes() throws IOException {
        try (ZipFile zipFile = new ZipFile(filePath.toFile(), zipCharset)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry e = entries.nextElement();
                if (!e.isDirectory() && e.getSize() > 0) {
                    totalBytes += e.getSize();
                }
            }
        }
    }

    @Override
    protected Void doInBackground() throws Exception {
        SwingUtilities.invokeLater(() -> progressDialog.setVisible(true));

        calculateTotalBytes();

        if (!Files.exists(target)) {
            Files.createDirectories(target);
        }

        try (ZipInputStream zis = new ZipInputStream(
                new FileInputStream(filePath.toFile()),
                zipCharset)) {

            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                Path newPath = resolveZipEntry(target, entry);

                progressLabel.setText("正在解压: " + entry.getName());

                if (entry.isDirectory()) {
                    Files.createDirectories(newPath);
                } else {
                    if (newPath.getParent() != null) {
                        Files.createDirectories(newPath.getParent());
                    }

                    try (OutputStream os = Files.newOutputStream(newPath)) {
                        byte[] buffer = new byte[8192];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            os.write(buffer, 0, len);
                            extractedBytes += len;
                            updateProgress();
                        }
                    }
                }

                zis.closeEntry();
            }
        }

        return null;
    }

    /** 更新进度 */
    private void updateProgress() {
        if (totalBytes == 0) return;
        int percent = (int) ((extractedBytes * 100) / totalBytes);
        setProgress(percent);
        publish(percent);
    }

    // 获取文件编码类型
    private static java.nio.charset.Charset detectCharset(Path zipPath) {
        try (ZipFile zip = new ZipFile(zipPath.toFile(), java.nio.charset.StandardCharsets.UTF_8)) {
            return java.nio.charset.StandardCharsets.UTF_8;
        } catch (Exception e) {
            return java.nio.charset.Charset.forName("GBK");
        }
    }

    @Override
    protected void process(java.util.List<Integer> chunks) {
        int value = chunks.get(chunks.size() - 1);
        progressBar.setValue(value);
    }

    @Override
    protected void done() {
        try {
            get(); // 检查解压是否成功
            JOptionPane.showMessageDialog(null, "解压完成");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "解压失败: " + e.getMessage() + "，请尝试手动解压",
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
        }
        progressDialog.setVisible(false);
    }
}
