package kk3twt.abnormal.tools.otherFunctions.loliconImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageThumbnail extends JFrame {

    public ImageThumbnail(boolean random, boolean usePid, int pid, int r18, String[] tags) throws Exception {
        setTitle("涩图搜索结果");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 图片链接与信息
        ImageInfo imageInfo = new ImageInfo(random, usePid, pid, r18, tags);
        String url = imageInfo.getUrl();
        String[] data = imageInfo.getData();
        String[] imageTags = imageInfo.getTags();

        // 在窗口左侧显示图片信息与标签
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

        JPanel imageData = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // 设置公共属性
        gbc.gridx = 0; // 只有一列
        gbc.fill = GridBagConstraints.BOTH; // 填充整个单元格
        gbc.weightx = 1.0; // 水平扩展
        gbc.weighty = 1.0; // 垂直扩展权重，每行相同

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

        // 在窗口中心显示图片
        JLabel imageLabel = new JLabel("少女祈祷中...", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(800, 600));
        mainPanel.add(imageLabel, BorderLayout.CENTER);

        add(mainPanel);

        // 使用SwingWorker后台加载图片
        ImageLoader loader = new ImageLoader(url, imageLabel, this);
        loader.execute();

        // 保存原图
        saveButton.addActionListener(e -> saveOriginalImage(loader.originalImage));
    }

    private void saveOriginalImage(BufferedImage originalImage) {
        if (originalImage == null) {
            JOptionPane.showMessageDialog(this, "没有可保存的原始图片", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("保存原图");

        // 设置默认文件名
        Date date = new Date(); // 返回保存时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        fileChooser.setSelectedFile(new File("Test小姐的馈赠_" + formatter.format(date) + ".png"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // 确保文件扩展名为.png（简单处理，可根据需要改进）
            String path = fileToSave.getAbsolutePath();
            if (!path.toLowerCase().endsWith(".png")) {
                fileToSave = new File(path + ".png");
            }

            try {
                // 使用ImageIO写入PNG格式
                boolean success = ImageIO.write(originalImage, "png", fileToSave);
                if (success) {
                    JOptionPane.showMessageDialog(this, "图片保存成功: " + fileToSave.getName(),
                            "成功", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "保存失败（不支持的格式）",
                            "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "保存出错: " + ex.getMessage(),
                        "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}