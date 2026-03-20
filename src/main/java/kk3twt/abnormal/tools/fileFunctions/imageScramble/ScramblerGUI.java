package kk3twt.abnormal.tools.fileFunctions.imageScramble;

import kk3twt.abnormal.tools.utils.Initializer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图片与视频混淆工具的主界面。
 * 提供两个选项卡：图片混淆、视频混淆。
 * 图片混淆支持两种算法：密码混淆（块重排）和希尔伯特曲线混淆。
 * 视频混淆支持相同的算法，并显示处理进度。
 */
public class ScramblerGUI extends JFrame {

    /** 当前处理完成后输出文件的路径（由文件选择器自动生成） */
    private String outputPath;

    private JPanel mainPanel;
    private JPanel imageScramblePanel;
    private JPanel videoScramblePanel;

    /**
     * 构造主窗口，初始化界面并显示。
     */
    public ScramblerGUI() {
        setTitle("图片与视频混淆");
        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        initComponents();
        setContentPane(mainPanel);
    }

    /**
     * 初始化主面板，创建选项卡并添加底部提示。
     */
    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();

        createImageScramblePanel();
        createVideoScramblePanel();

        tabbedPane.addTab("图片混淆", imageScramblePanel);
        tabbedPane.addTab("视频混淆", videoScramblePanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JLabel footerLabel = new JLabel("图片混淆仅支持PNG格式，视频混淆仅支持MP4格式", SwingUtilities.CENTER);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        mainPanel.add(footerLabel, BorderLayout.SOUTH);
    }

    /**
     * 创建图片混淆选项卡的面板。
     * 包含文件选择、算法选择、密码输入、混淆/解混淆按钮。
     */
    private void createImageScramblePanel() {
        imageScramblePanel = new JPanel(new BorderLayout(10, 10));
        imageScramblePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 选择文件面板
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JTextField inputField = new JTextField(30);
        JButton inputBtn = new JButton("选择图片文件");
        inputBtn.addActionListener(e -> chooseImgFile(inputField));
        inputPanel.add(inputField);
        inputPanel.add(inputBtn);

        // 算法选择及密码输入面板
        JPanel typePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        String[] type = {"密码混淆", "希尔伯特曲线混淆"};
        JComboBox<String> typeBox = new JComboBox<>(type);
        JTextField passwordField = new JTextField(30);
        typePanel.add(new JLabel("选择混淆类型："));
        typePanel.add(typeBox);
        JLabel passwordLabel = new JLabel("输入密码：");
        typePanel.add(passwordLabel);
        typePanel.add(passwordField);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton scrambleButton = new JButton("混淆");
        JButton descrambleButton = new JButton("解混淆");
        buttonPanel.add(scrambleButton);
        buttonPanel.add(descrambleButton);

        // 算法切换时更新密码框的可用性及提示
        typeBox.addActionListener(e -> {
            if (typeBox.getSelectedIndex() == 0) {
                passwordLabel.setText("输入密码：");
                passwordField.setEditable(true);
            } else {
                passwordLabel.setText("该算法不需要输入密码");
                passwordField.setEditable(false);
            }
        });

        scrambleButton.addActionListener(e -> {
            try {
                imgScramble(typeBox.getSelectedIndex(), inputField.getText().trim(), passwordField.getText().trim());
                JOptionPane.showMessageDialog(this, "完成");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "错误：" + ex.getMessage());
            }
        });

        descrambleButton.addActionListener(e -> {
            try {
                imgDescramble(typeBox.getSelectedIndex(), inputField.getText().trim(), passwordField.getText().trim());
                JOptionPane.showMessageDialog(this, "完成");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "错误：" + ex.getMessage());
            }
        });

        imageScramblePanel.add(inputPanel, BorderLayout.NORTH);
        imageScramblePanel.add(typePanel, BorderLayout.CENTER);
        imageScramblePanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * 创建视频混淆选项卡的面板。
     * 结构与图片混淆类似，但处理时调用视频相关方法。
     */
    private void createVideoScramblePanel() {
        videoScramblePanel = new JPanel(new BorderLayout(10, 10));
        videoScramblePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 选择文件面板
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JTextField inputField = new JTextField(30);
        JButton inputBtn = new JButton("选择视频文件");
        inputBtn.addActionListener(e -> chooseVideoFile(inputField));
        inputPanel.add(inputField);
        inputPanel.add(inputBtn);

        // 算法选择及密码输入面板
        JPanel typePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        String[] type = {"密码混淆", "希尔伯特曲线混淆"};
        JComboBox<String> typeBox = new JComboBox<>(type);
        JTextField passwordField = new JTextField(30);
        typePanel.add(new JLabel("选择混淆类型："));
        typePanel.add(typeBox);
        JLabel passwordLabel = new JLabel("输入密码：");
        typePanel.add(passwordLabel);
        typePanel.add(passwordField);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton scrambleButton = new JButton("混淆");
        JButton descrambleButton = new JButton("解混淆");
        buttonPanel.add(scrambleButton);
        buttonPanel.add(descrambleButton);

        typeBox.addActionListener(e -> {
            if (typeBox.getSelectedIndex() == 0) {
                passwordLabel.setText("输入密码：");
                passwordField.setEditable(true);
            } else {
                passwordLabel.setText("该算法不需要输入密码");
                passwordField.setEditable(false);
            }
        });

        scrambleButton.addActionListener(e -> {
            try {
                videoScramble(typeBox.getSelectedIndex(), inputField.getText().trim(), passwordField.getText().trim());
                JOptionPane.showMessageDialog(this, "完成");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "错误：" + ex.getMessage());
            }
        });

        descrambleButton.addActionListener(e -> {
            try {
                videoDescramble(typeBox.getSelectedIndex(), inputField.getText().trim(), passwordField.getText().trim());
                JOptionPane.showMessageDialog(this, "完成");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "错误：" + ex.getMessage());
            }
        });

        videoScramblePanel.add(inputPanel, BorderLayout.NORTH);
        videoScramblePanel.add(typePanel, BorderLayout.CENTER);
        videoScramblePanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * 打开文件选择器选择图片文件（PNG），并自动生成输出路径。
     *
     * @param inputField 用于显示所选文件路径的文本框
     */
    private void chooseImgFile(JTextField inputField) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.addChoosableFileFilter(
                new FileNameExtensionFilter("PNG 文件（*.png）", "png")
        );

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            inputField.setText(chooser.getSelectedFile().getAbsolutePath());

            String inputPath = inputField.getText();
            int dot = inputPath.lastIndexOf('.');
            String base = (dot > 0) ? inputPath.substring(0, dot) : "none";
            outputPath = base + "_processed.png";
        }
    }

    /**
     * 打开文件选择器选择视频文件（MP4），并自动生成输出路径。
     *
     * @param inputField 用于显示所选文件路径的文本框
     */
    private void chooseVideoFile(JTextField inputField) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.addChoosableFileFilter(
                new FileNameExtensionFilter("MP4 文件（*.mp4）", "mp4")
        );

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            inputField.setText(chooser.getSelectedFile().getAbsolutePath());

            String inputPath = inputField.getText();
            int dot = inputPath.lastIndexOf('.');
            String base = (dot > 0) ? inputPath.substring(0, dot) : "none";
            outputPath = base + "_processed.mp4";
        }
    }

    /**
     * 执行图片混淆操作。
     *
     * @param type     算法类型：0=密码混淆，1=希尔伯特混淆
     * @param imgPath  输入图片路径
     * @param password 密码（type=0时有效）
     * @throws IOException 图片读取或写入失败时抛出
     */
    private void imgScramble(int type, String imgPath, String password) throws IOException {
        BufferedImage src = ImageIO.read(new File(imgPath));

        BufferedImage out;
        if (type == 0) {
            long seed = PasswordScrambler.passwordToSeed(password);
            out = PasswordScrambler.scramble(src, seed);
        } else {
            out = HilbertScrambler.scramble(src);
        }

        ImageIO.write(out, "PNG", new File(outputPath));
    }

    /**
     * 执行图片解混淆操作。
     *
     * @param type     算法类型：0=密码混淆，1=希尔伯特混淆
     * @param imgPath  输入图片路径（已混淆）
     * @param password 密码（type=0时有效）
     * @throws IOException 图片读取或写入失败时抛出
     */
    private void imgDescramble(int type, String imgPath, String password) throws IOException {
        BufferedImage src = ImageIO.read(new File(imgPath));

        BufferedImage out;
        if (type == 0) {
            long seed = PasswordScrambler.passwordToSeed(password);
            out = PasswordScrambler.descramble(src, seed);
        } else {
            out = HilbertScrambler.descramble(src);
        }

        ImageIO.write(out, "PNG", new File(outputPath));
    }

    /**
     * 执行视频混淆操作（异步，通过进度对话框显示）。
     *
     * @param type       算法类型：0=密码混淆，1=希尔伯特混淆
     * @param videoPath  输入视频路径
     * @param password   密码（type=0时有效）
     * @throws Exception 视频处理过程中可能抛出的异常
     */
    private void videoScramble(int type, String videoPath, String password) throws Exception {
        int[] videoInfo = VideoScrambler.probeResolution(videoPath);
        long seed = 0;

        if (type == 0) {
            seed = PasswordScrambler.passwordToSeed(password);
        }

        ScrambleProgress progress = new ScrambleProgress();

        progress.start(videoPath,
                outputPath,
                videoInfo[0],
                videoInfo[1],
                videoInfo[2],
                seed,
                type,
                true);
    }

    /**
     * 执行视频解混淆操作（异步，通过进度对话框显示）。
     *
     * @param type       算法类型：0=密码混淆，1=希尔伯特混淆
     * @param videoPath  输入视频路径（已混淆）
     * @param password   密码（type=0时有效）
     * @throws Exception 视频处理过程中可能抛出的异常
     */
    private void videoDescramble(int type, String videoPath, String password) throws Exception {
        int[] videoInfo = VideoScrambler.probeResolution(videoPath);
        long seed = 0;

        if (type == 0) {
            seed = PasswordScrambler.passwordToSeed(password);
        }

        ScrambleProgress progress = new ScrambleProgress();

        progress.start(videoPath,
                outputPath,
                videoInfo[0],
                videoInfo[1],
                videoInfo[2],
                seed,
                type,
                false);
    }

    /**
     * 程序入口，在事件调度线程中启动 GUI。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        if (Initializer.isInitialized(0, "ffmpeg")) {
            SwingUtilities.invokeLater(ScramblerGUI::new);
        }
    }
}