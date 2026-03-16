package kk3twt.abnormal.tools.utils;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Initializer extends JDialog {
    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JLabel statusLabel = new JLabel("", SwingConstants.CENTER);
    private final int downloadMode;
    private boolean needDownload;

    private final String[] dependencyUrls = {"1", "2"};
    private final String[] dependencyNames = {"ffmpeg", "um"};

    public Initializer(int downloadMode, boolean needDownload) {
        setTitle(needDownload ? "下载依赖" : "解压依赖");
        setSize(400, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true); // 模态阻塞

        progressBar.setStringPainted(true);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel.setText(needDownload ? "正在下载依赖：" + dependencyNames[downloadMode] : "正在解压依赖，请稍候");

        setLayout(new BorderLayout(10, 10));
        add(statusLabel, BorderLayout.SOUTH);
        add(progressBar, BorderLayout.CENTER);

        this.downloadMode = downloadMode;
        this.needDownload = needDownload;
        startTask();
    }

    private void startTask() {
        if (needDownload) {
            startDownload();
        } else {
            startExtract();
        }
    }

    private void startDownload() {
        Path target = AppPath.appHome().resolve(dependencyNames[downloadMode] + ".zip");
        String url = dependencyUrls[downloadMode];
        Downloader downloader = new Downloader(target, url) {
            @Override
            protected void done() {
                try {
                    get(); // 检查下载是否成功
                    // 下载成功，切换为解压模式
                    SwingUtilities.invokeLater(() -> {
                        needDownload = false;
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
        Path libZip = AppPath.resourcePath("resources.zip");
        ZipExtractor extractor = new ZipExtractor(libZip, AppPath.libDir()) {
            @Override
            protected void done() {
                try {
                    get(); // 检查解压是否成功

                    if (!libZip.toFile().delete()) {
                        JOptionPane.showMessageDialog(null,
                                "删除依赖包失败，请尝试手动删除",
                                "错误",
                                JOptionPane.ERROR_MESSAGE);
                    }

                    JOptionPane.showMessageDialog(null, "依赖下载完成");

                    dispose();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(Initializer.this,
                            "解压失败: " + e.getMessage() + "，请尝试手动解压依赖",
                            "错误",
                            JOptionPane.ERROR_MESSAGE);
                    dispose();
                }
            }
        };
        extractor.execute();
    }
}
