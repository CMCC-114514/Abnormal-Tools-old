package fileFunctions.fileDownloader;

import utils.Downloader;
import utils.ZipExtractor;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDownloaderGUI extends JFrame {

    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JTextField urlField = new JTextField();
    private final JTextField fileNameField = new JTextField();
    private final JCheckBox needExtract;
    private Downloader downloader;

    private boolean userEditedFileName = false;
    private boolean programmaticChange = false;

    public FileDownloaderGUI() {
        setTitle("文件下载器");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10,10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15,15,10,15));

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入下载链接和文件名"));

        inputPanel.add(new JLabel("下载链接："));
        inputPanel.add(urlField);
        inputPanel.add(new JLabel("文件名："));
        inputPanel.add(fileNameField);

        JButton downloadButton = new JButton("开始");
        inputPanel.add(downloadButton);

        needExtract = new JCheckBox("解压zip格式压缩包");
        inputPanel.add(needExtract);

        // 自动生成文件名
        urlField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { autoDetectFileName(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { autoDetectFileName(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { autoDetectFileName(); }
        });

        fileNameField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { markEdit(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { markEdit(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { markEdit(); }

            private void markEdit() {
                if (!programmaticChange) {
                    userEditedFileName = true;
                }
            }
        });

        // 下载按钮
        final boolean[] isDownloading = {false};
        downloadButton.addActionListener(e -> {
            if (!isDownloading[0]) {
                startDownload();
                isDownloading[0] = !isDownloading[0];
                downloadButton.setText("暂停");
            } else {
                pause();
                isDownloading[0] = !isDownloading[0];
                downloadButton.setText("继续");
            }
        });

        // 下载进度条
        progressBar.setStringPainted(true);
        JLabel footerLabel = new JLabel("默认下载到“下载”文件夹", SwingConstants.CENTER);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        setLayout(new BorderLayout(10, 10));
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(progressBar, BorderLayout.CENTER);
        mainPanel.add(footerLabel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void autoDetectFileName() {

        if (userEditedFileName) return;

        String url = urlField.getText().trim();
        if (url.isEmpty()) return;

        new Thread(() -> {

            String name = getFileNameFromHTTP(url);

            if (name == null || name.isEmpty()) {
                int idx = url.lastIndexOf('/');
                if (idx != -1 && idx < url.length() - 1) {
                    name = url.substring(idx + 1);
                }
            }

            if (name != null && !name.isEmpty()) {
                final String finalName = name;

                SwingUtilities.invokeLater(() -> {
                    programmaticChange = true;
                    fileNameField.setText(finalName);
                    programmaticChange = false;
                });
            }

        }).start();
    }

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
                        return part.substring(9).replace("\"", "");
                    }

                }
            }

        } catch (Exception ignored) {}

        return null;
    }

    private void startDownload() {
        String fileName = fileNameField.getText();
        Path target = Paths.get(System.getProperty("user.home"), "Downloads").resolve(fileName);
        String url = urlField.getText();

        downloader = new Downloader(target, url) {
            @Override
            protected void done() {

                try {
                    get(); // 完成 or 异常 or 取消

                    JOptionPane.showMessageDialog(null, "下载完成！");

                    // 自动解压压缩包
                    String fileName = fileNameField.getText();
                    if (needExtract.isSelected() && downloader.isDone() && (fileName.endsWith(".zip") || fileName.endsWith(".ZIP"))) {
                        Path zipFile = Paths.get(System.getProperty("user.home"), "Downloads").resolve(fileName);
                        ZipExtractor extractor = new ZipExtractor(zipFile, zipFile.getParent().resolve(fileName.substring(0, fileName.lastIndexOf('.')))) {
                            @Override
                            protected void done() {
                                try {
                                    get(); // 检查解压是否成功
                                    JOptionPane.showMessageDialog(null, "解压完成");
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null,
                                            "解压失败: " + e.getMessage() + "请手动解压",
                                            "错误",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        };
                        extractor.execute();
                    }

                } catch (java.util.concurrent.CancellationException e) {
                    // 用户暂停
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

        downloader.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                progressBar.setValue((Integer) evt.getNewValue());
            }
        });

        downloader.execute();
    }

    private void pause() {
        if (downloader != null && !downloader.isDone()) {
            downloader.cancel(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FileDownloaderGUI().setVisible(true));
    }
}
