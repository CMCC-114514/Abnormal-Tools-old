package kk3twt.abnormal.tools.otherFunctions.loliconImage;

import javax.swing.*;
import java.awt.*;

public class LoliconGUI extends JFrame {
    public LoliconGUI() {
        setTitle("Pixiv图片搜索");
        setSize(430, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        JPanel mainPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField tagField = new JTextField();
        JLabel tagLabel = new JLabel("输入标签（多标签用空格分开）：");
        mainPanel.add(tagLabel);
        mainPanel.add(tagField);

        JTextField pidField = new JTextField();
        JLabel pidLabel = new JLabel("非pid检索模式不可用");
        mainPanel.add(pidLabel);
        mainPanel.add(pidField);
        pidField.setEditable(false);

        JCheckBox pidMode = new JCheckBox("pid检索");
        JCheckBox randomMode = new JCheckBox("随机涩图");
        JCheckBox r18Mode = new JCheckBox("R18模式");
        mainPanel.add(pidMode);
        mainPanel.add(randomMode);
        mainPanel.add(r18Mode);

        JButton searchButton = new JButton("来点涩图");
        mainPanel.add(searchButton);

        pidMode.addActionListener(e -> {
            tagLabel.setText(pidMode.isSelected() ? "pid检索模式下不可用" : "输入标签（多标签用空格分开）：");
            tagField.setEditable(!pidMode.isSelected());
            pidLabel.setText(pidMode.isSelected() ? "输入pid：" : "非pid检索模式不可用");
            pidField.setEditable(pidMode.isSelected());

            randomMode.setEnabled(!pidMode.isSelected());
        });

        randomMode.addActionListener(e -> {
            tagLabel.setText(randomMode.isSelected() ? "随机模式下不可用" : "输入标签（多标签用空格分开）：");
            tagField.setEditable(!randomMode.isSelected());

            pidField.setEditable(!randomMode.isSelected() && pidMode.isSelected());
            pidMode.setEnabled(!randomMode.isSelected());
        });

        searchButton.addActionListener(e -> {
            boolean random = randomMode.isSelected();
            boolean usePid = pidMode.isSelected();
            int pid = pidField.isEditable() ? Integer.parseInt(pidField.getText().trim()) : 0;
            int r18 = r18Mode.isSelected() ? 1 : 0;
            String[] tags = tagField.getText().split(" ");

            try {
                new ImageThumbnail(random, usePid, pid, r18, tags);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        add(mainPanel, BorderLayout.CENTER);

        JLabel footerLabel = new JLabel("<html><center>使用 Lolicon API 下载涩图<br>" + "仅供学习使用，图片请在24小时内删除</center></html>", SwingConstants.CENTER);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(footerLabel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoliconGUI::new);
    }
}
