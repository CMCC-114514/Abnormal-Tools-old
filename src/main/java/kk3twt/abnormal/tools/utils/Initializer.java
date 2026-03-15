package kk3twt.abnormal.tools.utils;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Initializer extends JDialog {
    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JLabel statusLabel = new JLabel("", SwingConstants.CENTER);
    private boolean downloadMode;

    /**
     * 创建初始化对话框
     * @param downloadMode true：需要下载依赖；false：只需解压已存在的lib.zip
     */
    public Initializer(boolean downloadMode) {
        setTitle(downloadMode ? "下载依赖" : "解压依赖");
        setSize(400, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true); // 模态阻塞

        progressBar.setStringPainted(true);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel.setText(downloadMode ? "初次启动，需要下载相关依赖" : "正在解压依赖，请稍候");

        setLayout(new BorderLayout(10, 10));
        add(statusLabel, BorderLayout.SOUTH);
        add(progressBar, BorderLayout.CENTER);

        this.downloadMode = downloadMode;
        startTask();
    }

    private void startTask() {
        if (downloadMode) {
            startDownload();
        } else {
            startExtract();
        }
    }

    private void startDownload() {
        Path target = AppPath.appHome().resolve("lib.zip");
        String url = "https://github.com/CMCC-114514/Abnormal_Dependencies/releases/download/lib/lib.zip";

        Downloader downloader = new Downloader(target, url) {
            @Override
            protected void done() {
                try {
                    get(); // 检查下载是否成功
                    // 下载成功，切换为解压模式
                    SwingUtilities.invokeLater(() -> {
                        downloadMode = false;
                        setTitle("解压依赖");
                        statusLabel.setText("正在解压依赖，请稍候");
                        progressBar.setValue(0);
                        progressBar.setIndeterminate(false); // 恢复确定进度模式（解压可能不支持进度）
                        startExtract();
                    });
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(Initializer.this,
                            "下载失败: " + e.getMessage(),
                            "错误",
                            JOptionPane.ERROR_MESSAGE);
                    dispose();
                }
            }
        };

        downloader.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                progressBar.setValue((Integer) evt.getNewValue());
            }
        });

        downloader.execute();
    }

    private void startExtract() {
        Path libZip = AppPath.appHome().resolve("lib.zip");
        ZipExtractor extractor = new ZipExtractor(libZip, AppPath.appHome()) {
            @Override
            protected void done() {
                try {
                    get(); // 检查解压是否成功
                    // 解压成功，写入初始化标记文件，并删除依赖包
                    Path initFile = AppPath.resourcePath("isInitialized");
                    Files.write(initFile, "1".getBytes(), StandardOpenOption.CREATE_NEW);

                    if (!libZip.toFile().delete()) {
                        JOptionPane.showMessageDialog(null,
                                "删除依赖包失败，请尝试手动删除",
                                "错误",
                                JOptionPane.ERROR_MESSAGE);
                    }

                    JOptionPane.showMessageDialog(null, "初始化完成，请重新启动程序");

                    System.exit(0);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(Initializer.this,
                            "解压失败: " + e.getMessage() + "，请尝试重新启动程序",
                            "错误",
                            JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                }
            }
        };
        extractor.execute();
    }

    /**
     * 检查并执行初始化（若需要）
     * @return true 初始化已完成或无需初始化；false 初始化失败或未完成
     */
    public static boolean isInitialized() {
        Path initFile = AppPath.resourcePath("isInitialized");
        if (Files.exists(initFile)) {
            return true;
        }

        // 需要初始化，显示模态对话框阻塞直到完成
        try {
            if (SwingUtilities.isEventDispatchThread()) {
                runInitialization();
            } else {
                SwingUtilities.invokeAndWait(Initializer::runInitialization);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    String.format("初始化过程出错: %s\n请手动处理依赖。", e.getMessage()),
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
        }

        return Files.exists(initFile);
    }

    private static void runInitialization() {
        Path libZip = AppPath.appHome().resolve("lib.zip");
        try {
            if (!Files.exists(libZip)) {
                new Initializer(true).setVisible(true);
            } else {
                new Initializer(false).setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "初始化对话框显示失败: " + e.getMessage(),
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // 保留main方法以便独立测试（可选）
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Initializer(true).setVisible(true));
    }
}