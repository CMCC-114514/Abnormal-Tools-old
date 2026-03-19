package kk3twt.abnormal.tools.fileFunctions.formatConversion;

import kk3twt.abnormal.tools.utils.Initializer;

import javax.swing.*;
import java.awt.*;

/**
 * 音视频格式转换的主界面。
 * 使用选项卡面板分别提供图片、音频、视频格式转换功能。
 * 音频与视频转换基于 FFmpeg，图片转换基于 Java 原生 ImageIO。
 */
public class FormatConversionGUI extends JFrame {

    private JPanel mainPanel;
    private JPanel imageConverterPanel;
    private JPanel audioConverterPanel;
    private JPanel videoConverterPanel;

    /**
     * 构造转换器窗口，初始化界面并显示。
     */
    public FormatConversionGUI() {
        setTitle("音视频格式转换");
        setSize(400, 225);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        initComponents();
        setContentPane(mainPanel);
    }

    /**
     * 初始化所有界面组件：创建主面板、选项卡面板，并添加底部提示。
     */
    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();

        createImageConverterPanel();
        createAudioConverterPanel();
        createVideoConverterPanel();

        tabbedPane.addTab("图片格式转换", imageConverterPanel);
        tabbedPane.addTab("音频格式转换", audioConverterPanel);
        tabbedPane.addTab("视频格式转换", videoConverterPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JLabel footerLabel = new JLabel("音频与视频格式转换基于ffmpeg二次开发", SwingUtilities.CENTER);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        mainPanel.add(footerLabel, BorderLayout.SOUTH);
    }

    /**
     * 创建图片转换面板。
     * 包含输入文件选择、输出路径/格式选择、转换按钮。
     */
    private void createImageConverterPanel() {
        imageConverterPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        imageConverterPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 输入文件选择行
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JTextField inputField = new JTextField(30);
        JButton inputButton = new JButton("选择图片");
        inputPanel.add(inputField);
        inputPanel.add(inputButton);

        // 输出文件及格式选择行
        JPanel outputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JTextField outputField = new JTextField(30);
        outputPanel.add(outputField);
        JComboBox<String> formatCombo = new JComboBox<>(ImageConverter.IMAGE_FORMATS);
        outputPanel.add(formatCombo);

        // 选择文件后将输出路径预填为输入文件同名（无扩展名）
        inputButton.addActionListener(e -> ImageConverter.chooseInputFile(inputField, outputField));

        // 转换按钮
        JButton convertBtn = new JButton("开始转换");
        convertBtn.addActionListener(e -> {
            String format = ImageConverter.IMAGE_FORMATS[formatCombo.getSelectedIndex()];
            String input = inputField.getText();
            String output = outputField.getText() + format;

            if (input.isEmpty() || output.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请选择输入和输出文件");
                return;
            }

            try {
                ImageConverter.convert(input, output, format);
                JOptionPane.showMessageDialog(this, "转换成功!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "转换失败：" + ex.getMessage());
            }
        });

        imageConverterPanel.add(inputPanel);
        imageConverterPanel.add(outputPanel);
        imageConverterPanel.add(convertBtn);
    }

    /**
     * 创建音频转换面板。
     * 包含输入文件选择、输出路径/格式选择、转换按钮。
     */
    private void createAudioConverterPanel() {
        audioConverterPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        audioConverterPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JTextField inputField = new JTextField(30);
        JButton inputButton = new JButton("选择音频文件");
        inputPanel.add(inputField);
        inputPanel.add(inputButton);

        JPanel outputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JTextField outputField = new JTextField(30);
        outputPanel.add(outputField);
        JComboBox<String> formatCombo = new JComboBox<>(AudioConverter.AUDIO_FORMATS);
        outputPanel.add(formatCombo);

        inputButton.addActionListener(e -> AudioConverter.chooseInputFile(inputField, outputField));

        JButton convertBtn = new JButton("开始转换");
        convertBtn.addActionListener(e -> {
            String format = AudioConverter.AUDIO_FORMATS[formatCombo.getSelectedIndex()];
            String input = inputField.getText();
            String output = outputField.getText() + format;

            if (input.isEmpty() || output.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请选择输入和输出文件");
                return;
            }

            try {
                AudioConverter.convert(input, output);
                JOptionPane.showMessageDialog(this, "转换成功！");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "转换失败: " + ex.getMessage());
            }
        });

        audioConverterPanel.add(inputPanel);
        audioConverterPanel.add(outputPanel);
        audioConverterPanel.add(convertBtn);
    }

    /**
     * 创建视频转换面板。
     * 包含输入文件选择、输出路径/格式选择、转换按钮。
     */
    private void createVideoConverterPanel() {
        videoConverterPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        videoConverterPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JTextField inputField = new JTextField(30);
        JButton inputButton = new JButton("选择视频文件");
        inputPanel.add(inputField);
        inputPanel.add(inputButton);

        JPanel outputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JTextField outputField = new JTextField(30);
        outputPanel.add(outputField);
        JComboBox<String> formatCombo = new JComboBox<>(VideoConverter.VIDEO_FORMATS);
        outputPanel.add(formatCombo);

        inputButton.addActionListener(e -> VideoConverter.chooseInputFile(inputField, outputField));

        JButton convertBtn = new JButton("开始转换");
        convertBtn.addActionListener(e -> {
            String format = VideoConverter.VIDEO_FORMATS[formatCombo.getSelectedIndex()];
            String input = inputField.getText();
            String output = outputField.getText() + format;

            if (input.isEmpty() || output.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请选择输入和输出文件");
                return;
            }

            try {
                VideoConverter.convert(input, output);
                JOptionPane.showMessageDialog(this, "转换成功！");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "转换失败: " + ex.getMessage());
            }
        });

        videoConverterPanel.add(inputPanel);
        videoConverterPanel.add(outputPanel);
        videoConverterPanel.add(convertBtn);
    }

    /**
     * 程序入口，在事件调度线程中启动 GUI。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        if (Initializer.isInitialized(0, "ffmpeg")) {
            SwingUtilities.invokeLater(FormatConversionGUI::new);
        }
    }
}