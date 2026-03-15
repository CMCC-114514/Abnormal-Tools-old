package kk3twt.abnormal.tools.fileFunctions.musicUnlocker;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;

public class MusicUnlockerGUI extends JFrame {
    private final JTextField inputField;
    private final JTextField outputField;

    public MusicUnlockerGUI() {
        setTitle("音乐解锁");
        setSize(500, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 主面板
        JPanel ncmDumpPanel = new JPanel(new GridLayout(3, 1, 10,10));
        ncmDumpPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        // 选择文件面板
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        inputField = new JTextField(30);
        JButton inputBtn = new JButton("选择文件或文件夹");
        inputBtn.addActionListener(e -> chooseInputFile());
        inputPanel.add(inputField);
        inputPanel.add(inputBtn);

        // 选择输出位置面板
        JPanel outputPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        outputField = new JTextField(30);
        JButton outputBtn = new JButton("选择输出位置");
        outputBtn.addActionListener(e -> chooseOutputFile());
        outputPanel.add(outputField);
        outputPanel.add(outputBtn);

        // 转换按钮
        JButton convertBtn = new JButton("开始转换");
        convertBtn.addActionListener(e -> convertFile());

        ncmDumpPanel.add(inputPanel, BorderLayout.NORTH);
        ncmDumpPanel.add(outputPanel, BorderLayout.CENTER);
        ncmDumpPanel.add(convertBtn, BorderLayout.SOUTH);
        add(ncmDumpPanel, BorderLayout.CENTER);

        JLabel footerLabel = getFooterLabel(new Font("Microsoft YaHei", Font.PLAIN, 12));
        add(footerLabel, BorderLayout.SOUTH);
    }

    private void chooseInputFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        for (String format : Unlocker.DECRYPTED_FORMAT) {
            chooser.addChoosableFileFilter(
                    new FileNameExtensionFilter(format + " 文件（*."  + format.toLowerCase() + "）", format.toLowerCase()));
        }

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            inputField.setText(chooser.getSelectedFile().getAbsolutePath());
            // 自动生成输出文件名
            Path outputPath = Path.of(inputField.getText()).getParent().resolve("processed");
            outputField.setText(outputPath.toString());
        }
    }

    private void chooseOutputFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(outputField.getText()));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            outputField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void convertFile() {
        String input = inputField.getText();
        String output = outputField.getText();

        if (input.isEmpty() || output.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请选择输入和输出文件");
            return;
        }

        try {
            Unlocker.decrypt(input, output);
            JOptionPane.showMessageDialog(this, "转换成功!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "转换失败: " + e.getMessage());
        }
    }

    private static JLabel getFooterLabel(Font font) {
        JLabel footerLabel = new JLabel("基于um二次开发，仅用于学习和技术研究，转换后请在24小时内删除", SwingConstants.CENTER);
        footerLabel.setFont(font);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return footerLabel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MusicUnlockerGUI().setVisible(true));
    }
}