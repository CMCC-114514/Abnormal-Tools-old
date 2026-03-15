package kk3twt.abnormal.tools.fileFunctions.formatConversion;

import javax.swing.*;
import java.awt.*;

public class formatConversionGUI extends JFrame {

    private JPanel mainPanel;
    private JPanel imageConverterPanel;
    private JPanel audioConverterPanel;
    private JPanel videoConverterPanel;

    public formatConversionGUI() {
        setTitle("音视频格式转换");
        setSize(400, 225);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        initComponents();
        setContentPane(mainPanel);
    }

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

    private void createImageConverterPanel() {
        // 主面板参数
        imageConverterPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        imageConverterPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15,15));

        // 选择文件与输出位置
        JPanel inputPanel = new JPanel(new GridLayout(1,2,10,10));
        JTextField inputField = new JTextField(30);
        JButton inputButton = new JButton("选择图片");
        inputPanel.add(inputField);
        inputPanel.add(inputButton);

        JPanel outputPanel = new JPanel(new GridLayout(1,2,10,10));
        JTextField outputField = new JTextField(30);
        outputPanel.add(outputField);

        inputButton.addActionListener(e -> ImageConverter.chooseInputFile(inputField, outputField));

        // 选择目标格式
        JComboBox<String> formatCombo = new JComboBox<>(ImageConverter.IMAGE_FORMATS);
        outputPanel.add(formatCombo);

        // 转换过程
        JButton convertButton = new JButton("开始转换");
        convertButton.addActionListener(e -> {
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
        imageConverterPanel.add(convertButton);
    }

    private void createAudioConverterPanel() {
        // 主面板参数
        audioConverterPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        audioConverterPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        // 选择文件与输出位置
        JPanel inputPanel = new JPanel(new GridLayout(1,2,10,10));
        JTextField inputField = new JTextField(30);
        JButton inputButton = new JButton("选择音频文件");
        inputPanel.add(inputField);
        inputPanel.add(inputButton);

        JPanel outputPanel = new JPanel(new GridLayout(1,2,10,10));
        JTextField outputField = new JTextField(30);
        outputPanel.add(outputField);

        inputButton.addActionListener(e -> AudioConverter.chooseInputFile(inputField, outputField));

        // 选择目标格式
        JComboBox<String> formatCombo = new JComboBox<>(AudioConverter.AUDIO_FORMATS);
        outputPanel.add(formatCombo);

        // 转换按钮
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

    private void createVideoConverterPanel() {
        // 主面板参数
        videoConverterPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        videoConverterPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        // 选择文件与输出位置
        JPanel inputPanel = new JPanel(new GridLayout(1,2,10,10));
        JTextField inputField = new JTextField(30);
        JButton inputButton = new JButton("选择音频文件");
        inputPanel.add(inputField);
        inputPanel.add(inputButton);

        JPanel outputPanel = new JPanel(new GridLayout(1,2,10,10));
        JTextField outputField = new JTextField(30);
        outputPanel.add(outputField);

        inputButton.addActionListener(e -> VideoConverter.chooseInputFile(inputField, outputField));

        // 选择目标格式
        JComboBox<String> formatCombo = new JComboBox<>(VideoConverter.VIDEO_FORMATS);
        outputPanel.add(formatCombo);

        // 转换按钮
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(formatConversionGUI::new);
    }
}
