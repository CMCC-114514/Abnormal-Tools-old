package kk3twt.abnormal.tools.fileFunctions.musicUnlocker;

import kk3twt.abnormal.tools.utils.Initializer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;

/**
 * 音乐解锁工具的图形界面。
 * 允许用户选择加密音频文件或文件夹，指定输出位置，并调用 Unlocker 进行解密。
 * 界面包含输入文件选择、输出目录选择和转换按钮。
 */
public class MusicUnlockerGUI extends JFrame {

    /** 输入文件/文件夹路径文本框 */
    private final JTextField inputField;

    /** 输出文件/目录路径文本框 */
    private final JTextField outputField;

    /**
     * 构造音乐解锁窗口，初始化所有组件并显示。
     */
    public MusicUnlockerGUI() {
        setTitle("音乐解锁");
        setSize(400, 225);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setVisible(true);

        // 主面板
        JPanel ncmDumpPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        ncmDumpPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 输入文件选择面板
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        inputField = new JTextField(30);
        JButton inputBtn = new JButton("选择文件或文件夹");
        inputBtn.addActionListener(e -> chooseInputFile());
        inputPanel.add(inputField);
        inputPanel.add(inputBtn);

        // 输出位置选择面板
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

    /**
     * 打开文件选择器，允许用户选择单个加密文件或包含加密文件的文件夹。
     * 选择后自动将路径填入输入文本框，并在输出文本框预置同级的 "processed" 文件夹。
     */
    private void chooseInputFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        for (String format : Unlocker.DECRYPTED_FORMAT) {
            chooser.addChoosableFileFilter(
                    new FileNameExtensionFilter(format + " 文件（*." + format.toLowerCase() + "）", format.toLowerCase()));
        }

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            inputField.setText(chooser.getSelectedFile().getAbsolutePath());
            // 自动生成输出路径：输入文件所在目录下的 processed 文件夹
            Path outputPath = Path.of(inputField.getText()).getParent().resolve("processed");
            outputField.setText(outputPath.toString());
        }
    }

    /**
     * 打开文件保存对话框，让用户选择输出位置（可以是文件或目录）。
     * 默认为当前输出文本框中的路径。
     */
    private void chooseOutputFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(outputField.getText()));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            outputField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    /**
     * 执行解密操作。
     * 检查输入输出路径是否为空，调用 Unlocker.decrypt 进行解密，
     * 并弹出结果提示框。
     */
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

    /**
     * 创建底部提示标签，包含免责声明。
     *
     * @param font 标签使用的字体
     * @return 配置好的 JLabel
     */
    private static JLabel getFooterLabel(Font font) {
        JLabel footerLabel = new JLabel("<html><center>基于um二次开发，仅用于学习和技术研究，<br>转换后请在24小时内删除<center><html>", SwingConstants.CENTER);
        footerLabel.setFont(font);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return footerLabel;
    }

    /**
     * 程序入口，在事件调度线程中启动 GUI。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        if (Initializer.isInitialized(1, "um")) {
            SwingUtilities.invokeLater(MusicUnlockerGUI::new);
        }
    }
}