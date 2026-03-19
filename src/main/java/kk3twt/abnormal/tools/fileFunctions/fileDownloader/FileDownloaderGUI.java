package kk3twt.abnormal.tools.fileFunctions.fileDownloader;

import kk3twt.abnormal.tools.utils.Downloader;
import kk3twt.abnormal.tools.utils.ZipExtractor;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件下载器图形界面。
 * 提供输入下载链接、自动或手动指定文件名、显示下载进度、
 * 暂停/继续下载以及下载完成后自动解压ZIP文件的功能。
 * 下载文件默认保存到用户主目录下的“Downloads”文件夹。
 */
public class FileDownloaderGUI extends JFrame {

    /** 图形界面各组件 */
    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JTextField urlField = new JTextField();
    private final JTextField fileNameField = new JTextField();
    private final JButton downloadButton;
    private final JCheckBox needExtract;

    /** 当前执行下载任务的SwingWorker对象 */
    private Downloader downloader;

    /** 标记用户是否手动编辑过文件名（若为true，则停止自动更新文件名） */
    private boolean userEditedFileName = false;

    /** 标记当前文件名修改是否由程序自动触发（用于区分用户编辑） */
    private boolean programmaticChange = false;

    /**
     * 构造下载器窗口，初始化各组件、布局和事件监听器。
     */
    public FileDownloaderGUI() {
        setTitle("文件下载器");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        // 主面板，使用边界布局，留出边距
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        // 输入面板，放置链接、文件名、按钮和复选框
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入下载链接和文件名"));

        inputPanel.add(new JLabel("下载链接："));
        inputPanel.add(urlField);
        inputPanel.add(new JLabel("文件名："));
        inputPanel.add(fileNameField);

        downloadButton = new JButton("开始");
        inputPanel.add(downloadButton);

        needExtract = new JCheckBox("解压zip格式压缩包");
        inputPanel.add(needExtract);

        // 监听URL文本框变化，自动检测并填写文件名（仅当用户未手动编辑时）
        urlField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { autoDetectFileName(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { autoDetectFileName(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { autoDetectFileName(); }
        });

        // 监听文件名文本框编辑，标记用户是否手动修改过
        fileNameField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { markEdit(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { markEdit(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { markEdit(); }

            private void markEdit() {
                if (!programmaticChange) {
                    userEditedFileName = true;   // 非程序自动修改，视为用户编辑
                }
            }
        });

        // 下载按钮的动作：切换下载/暂停/继续状态
        final boolean[] isDownloading = {false};   // 标记当前是否正在下载（用于按钮文字切换）
        downloadButton.addActionListener(e -> {
            if (!isDownloading[0]) {
                startDownload();                   // 开始下载
                isDownloading[0] = true;
                downloadButton.setText("暂停");
            } else {
                pause();                            // 暂停下载
                isDownloading[0] = false;
                downloadButton.setText("继续");
            }
        });

        // 进度条设置
        progressBar.setStringPainted(true);
        JLabel footerLabel = new JLabel("默认下载到“下载”文件夹", SwingConstants.CENTER);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // 将各组件添加到主面板
        setLayout(new BorderLayout(10, 10));
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(progressBar, BorderLayout.CENTER);
        mainPanel.add(footerLabel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * 从URL中自动检测文件名并设置到文件名文本框。
     * 首先尝试从HTTP响应头Content-Disposition中提取filename，
     * 若失败则从URL路径的最后一段获取。
     * 仅在用户未手动编辑文件名时生效。
     * 此方法会在新线程中执行HTTP请求，避免阻塞EDT。
     */
    private void autoDetectFileName() {
        if (userEditedFileName) return;          // 用户已手动编辑，不再自动更新

        String url = urlField.getText().trim();
        if (url.isEmpty()) return;

        new Thread(() -> {
            String name = getFileNameFromHTTP(url);   // 尝试从HTTP头获取

            if (name == null || name.isEmpty()) {
                // 回退：取URL中最后一个斜杠后的内容
                int idx = url.lastIndexOf('/');
                if (idx != -1 && idx < url.length() - 1) {
                    name = url.substring(idx + 1);
                }
            }

            if (name != null && !name.isEmpty()) {
                final String finalName = name;
                SwingUtilities.invokeLater(() -> {
                    programmaticChange = true;          // 标记为程序修改
                    fileNameField.setText(finalName);
                    programmaticChange = false;
                });
            }
        }).start();
    }

    /**
     * 通过HTTP HEAD请求获取服务器返回的文件名（Content-Disposition头）。
     *
     * @param urlStr 下载链接
     * @return 提取到的文件名，若不存在或发生异常则返回null
     */
    private String getFileNameFromHTTP(String urlStr) {
        try {
            java.net.URL url = new java.net.URL(urlStr);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            String disposition = conn.getHeaderField("Content-Disposition");
            if (disposition != null) {
                String[] parts = disposition.split(";");
                for (String part : parts) {
                    part = part.trim();
                    if (part.startsWith("filename=")) {
                        // 去掉 "filename=" 前缀和可能的引号
                        return part.substring(9).replace("\"", "");
                    }
                }
            }
        } catch (Exception ignored) {
            // 忽略所有异常（超时、协议错误等），返回null
        }
        return null;
    }

    /**
     * 开始下载任务。
     * 根据URL和文件名构造下载路径，创建Downloader实例，注册进度监听，
     * 并在下载完成后处理结果（显示消息、自动解压等）。
     */
    private void startDownload() {
        String fileName = fileNameField.getText();
        Path target = Paths.get(System.getProperty("user.home"), "Downloads").resolve(fileName);
        String url = urlField.getText();

        downloader = new Downloader(target, url) {
            @Override
            protected void done() {
                try {
                    get();   // 获取结果，若下载被取消或异常则抛出相应异常
                    downloadButton.setText("开始");
                    JOptionPane.showMessageDialog(null, "下载完成！");

                    // 如果勾选了自动解压且文件是ZIP格式，则调用解压工具
                    String fileName = fileNameField.getText();
                    if (needExtract.isSelected() && downloader.isDone() &&
                            (fileName.endsWith(".zip") || fileName.endsWith(".ZIP"))) {
                        String folderName = fileName.substring(0, fileName.lastIndexOf('.'));
                        Path zipFile = Paths.get(System.getProperty("user.home"), "Downloads").resolve(fileName);
                        ZipExtractor extractor = new ZipExtractor(zipFile, zipFile.getParent().resolve(folderName));
                        extractor.execute();   // 异步解压
                    }
                } catch (java.util.concurrent.CancellationException e) {
                    // 用户主动暂停
                    JOptionPane.showMessageDialog(null, "下载已暂停");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            null,
                            "下载失败：" + e.getMessage(),
                            "错误",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        // 监听Downloader的progress属性变更，更新进度条
        downloader.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                progressBar.setValue((Integer) evt.getNewValue());
            }
        });

        downloader.execute();   // 启动下载线程
    }

    /**
     * 暂停当前下载任务（如果存在且未完成）。
     * 通过调用Downloader的cancel(true)实现中断。
     */
    private void pause() {
        if (downloader != null && !downloader.isDone()) {
            downloader.cancel(true);
        }
    }

    /**
     * 程序入口，在事件调度线程中启动GUI。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileDownloaderGUI::new);
    }
}