package kk3twt.abnormal.tools.utils;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.zip.*;

/**
 * 带 Swing GUI 进度条的 ZIP 解压器，作为 SwingWorker 在后台执行解压任务。
 * <p>
 * 功能特性：
 * <ul>
 *     <li>显示模态进度对话框，实时更新解压进度</li>
 *     <li>自动检测 ZIP 文件编码（UTF-8 或 GBK）</li>
 *     <li>防止 Zip Slip 路径穿越攻击</li>
 * </ul>
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

    /**
     * 构造一个解压器。
     *
     * @param filePath 待解压的 ZIP 文件路径
     * @param target   解压目标目录
     */
    public ZipExtractor(Path filePath, Path target) {
        this.filePath = filePath;
        this.target = target;

        this.zipCharset = detectCharset(filePath);

        initProgressUI();
    }

    /** 初始化进度对话框界面 */
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
     * 防止 Zip Slip 攻击：确保解压后的文件路径仍在目标目录内。
     *
     * @param destDir 目标目录
     * @param entry   ZIP 条目
     * @return 安全的目标路径
     * @throws IOException 如果条目试图跳出目标目录
     */
    private static Path resolveZipEntry(Path destDir, ZipEntry entry) throws IOException {
        Path resolvedPath = destDir.resolve(entry.getName()).normalize();

        if (!resolvedPath.startsWith(destDir)) {
            throw new IOException("非法压缩条目: " + entry.getName());
        }

        return resolvedPath;
    }

    /** 预扫描 ZIP 文件，计算所有文件的总字节数（用于进度计算） */
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

    /**
     * 后台执行解压任务。
     *
     * @return null
     * @throws Exception 解压过程中可能出现的异常
     */
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

    /** 根据已解压字节数更新进度百分比 */
    private void updateProgress() {
        if (totalBytes == 0) return;
        int percent = (int) ((extractedBytes * 100) / totalBytes);
        setProgress(percent);
        publish(percent);
    }

    /**
     * 检测 ZIP 文件的字符集编码。
     * <p>
     * 优先尝试 UTF-8，如果失败则回退到 GBK。
     *
     * @param zipPath ZIP 文件路径
     * @return 检测到的字符集
     */
    private static java.nio.charset.Charset detectCharset(Path zipPath) {
        try (ZipFile zip = new ZipFile(zipPath.toFile(), java.nio.charset.StandardCharsets.UTF_8)) {
            return java.nio.charset.StandardCharsets.UTF_8;
        } catch (Exception e) {
            return java.nio.charset.Charset.forName("GBK");
        }
    }

    /**
     * 在事件调度线程中更新进度条数值。
     *
     * @param chunks 进度百分比列表（取最后一个）
     */
    @Override
    protected void process(java.util.List<Integer> chunks) {
        int value = chunks.get(chunks.size() - 1);
        progressBar.setValue(value);
    }

    /** 解压完成后关闭对话框，并显示结果提示。 */
    @Override
    protected void done() {
        try {
            get(); // 检查解压是否成功
            JOptionPane.showMessageDialog(progressDialog, "解压完成");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(progressDialog,
                    "解压失败: " + e.getMessage() + "，请尝试手动解压",
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
        }
        progressDialog.setVisible(false);
    }
}