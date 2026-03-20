package kk3twt.abnormal.tools.otherFunctions.loliconImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 显示搜索结果图片的窗口。
 * 包含图片信息、标签列表、缩略图预览以及保存原图的功能。
 */
public class ImageThumbnail extends JFrame {

    /**
     * 构造搜索结果窗口。
     *
     * @param random  随机模式标志
     * @param usePid  是否使用 PID 模式
     * @param pid     PID 值（usePid 为 true 时有效）
     * @param r18     R18 模式标志
     * @param tags    标签数组
     * @throws Exception 获取图片信息或加载图片时可能抛出的异常
     */
    public ImageThumbnail(boolean random, boolean usePid, int pid, int r18, String[] tags) throws Exception {
        setTitle("涩图搜索结果");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 获取图片信息
        ImageInfo imageInfo = new ImageInfo(random, usePid, pid, r18, tags);
        String url = imageInfo.getUrl();
        String[] data = imageInfo.getData();
        String[] imageTags = imageInfo.getTags();

        // 左侧信息面板（图片信息和标签）
        JPanel infoPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        infoPanel.setBorder(new TitledBorder("图片信息"));
        for (String datum : data) {
            infoPanel.add(new JLabel(datum));
        }

        JPanel tagPanel = new JPanel(new GridLayout(imageTags.length, 1));
        tagPanel.setBorder(new TitledBorder("标签"));
        for (String imageTag : imageTags) {
            tagPanel.add(new JLabel("#" + imageTag));
        }

        JButton saveButton = new JButton("下载原图");

        // 使用 GridBagLayout 布局左侧内容
        JPanel imageData = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        gbc.gridy = 0;
        gbc.gridheight = 4;
        imageData.add(infoPanel, gbc);

        gbc.gridy = 4;
        gbc.gridheight = 6;
        imageData.add(tagPanel, gbc);

        gbc.gridy = 10;
        gbc.gridheight = 1;
        imageData.add(saveButton, gbc);
        mainPanel.add(imageData, BorderLayout.EAST);

        // 中心图片显示区域
        JLabel imageLabel = new JLabel("少女祈祷中...", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(800, 600));
        mainPanel.add(imageLabel, BorderLayout.CENTER);

        add(mainPanel);

        // 后台加载图片
        ImageLoader loader = new ImageLoader(url, imageLabel, this);
        loader.execute();

        // 保存原图按钮事件
        saveButton.addActionListener(e -> saveOriginalImage(loader.originalImage));
    }

    /**
     * 保存原始图片到本地文件。
     * 弹出文件保存对话框，默认文件名为 "Test小姐的馈赠_时间戳.png"。
     *
     * @param originalImage 要保存的原始图片（未缩放）
     */
    private void saveOriginalImage(BufferedImage originalImage) {
        if (originalImage == null) {
            JOptionPane.showMessageDialog(this, "没有可保存的原始图片", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("保存原图");

        // 设置默认文件名（包含时间戳）
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        fileChooser.setSelectedFile(new File("Test小姐的馈赠_" + formatter.format(date) + ".png"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // 确保文件扩展名为 .png
            String path = fileToSave.getAbsolutePath();
            if (!path.toLowerCase().endsWith(".png")) {
                fileToSave = new File(path + ".png");
            }

            try {
                boolean success = ImageIO.write(originalImage, "png", fileToSave);
                if (success) {
                    JOptionPane.showMessageDialog(this, "图片保存成功: " + fileToSave.getName(),
                            "成功", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "保存失败（不支持的格式）",
                            "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "保存出错: " + ex.getMessage(),
                        "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}