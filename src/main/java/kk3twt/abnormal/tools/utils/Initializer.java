package kk3twt.abnormal.tools.utils;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

/**
 * 初始化对话框，负责依赖的下载与解压。
 * <p>
 * 根据 {@code needDownload} 参数决定行为：
 * <ul>
 *     <li>若需要下载，则先启动 {@link Downloader} 下载指定依赖的 ZIP 文件，完成后自动转为解压</li>
 *     <li>若不需要下载，则直接启动 {@link ZipExtractor} 解压已存在的依赖包</li>
 * </ul>
 * 解压完成后会自动删除 ZIP 文件。
 */
public class Initializer extends JDialog {
    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JLabel statusLabel = new JLabel("", SwingConstants.CENTER);
    private final int downloadMode;
    private boolean needDownload;

    // 依赖的下载地址（实际使用时请替换为真实 URL）
    private final String[] dependencyUrls = {
            "https://github.com/CMCC-114514/Abnormal_Dependencies/releases/download/v0.0.3/ffmpeg.zip",
            "https://github.com/CMCC-114514/Abnormal_Dependencies/releases/download/v0.0.3/um.zip"
    };

    // 依赖名称，用于生成 ZIP 文件名
    private final String[] dependencyNames = {"ffmpeg", "um"};

    /**
     * 构造初始化对话框。
     *
     * @param downloadMode 下载模式索引（对应 dependencyUrls 和 dependencyNames 数组）
     * @param needDownload 是否需要先下载；若为 false 则直接解压已存在的 ZIP
     */
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

    /** 根据 needDownload 启动相应的任务（下载或直接解压） */
    private void startTask() {
        if (needDownload) {
            startDownload();
        } else {
            startExtract();
        }
    }

    /** 启动下载任务，下载完成后自动切换为解压任务 */
    private void startDownload() {
        Path target = AppPath.libDir().resolve(dependencyNames[downloadMode] + ".zip");
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

    /** 启动解压任务，解压完成后删除 ZIP 文件并关闭对话框 */
    private void startExtract() {
        Path libZip = AppPath.resourcePath(dependencyNames[downloadMode] + ".zip");
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