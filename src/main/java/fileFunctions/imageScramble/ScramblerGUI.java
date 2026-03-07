package fileFunctions.imageScramble;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScramblerGUI extends JFrame {
    private String outputPath;

    private JPanel mainPanel;

    private JPanel imageScramblePanel;
    private JPanel videoScramblePanel;

    public ScramblerGUI() {
        setTitle("图片与视频混淆");
        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        initComponents();
        setContentPane(mainPanel);
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();

        // code to create panels
        createImageScramblePanel();
        createVideoScramblePanel();

        tabbedPane.addTab("图片混淆", imageScramblePanel);
        tabbedPane.addTab("视频混淆", videoScramblePanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JLabel footerLabel = new JLabel("图片混淆仅支持PNG格式，视频混淆需要时间，请耐心等待", SwingUtilities.CENTER);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        mainPanel.add(footerLabel, BorderLayout.SOUTH);
    }

    private void createImageScramblePanel() {
        imageScramblePanel = new JPanel(new BorderLayout(10, 10));
        imageScramblePanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        // 选择文件面板
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JTextField inputField = new JTextField(30);
        JButton inputBtn = new JButton("选择图片文件");
        inputBtn.addActionListener(e -> chooseImgFile(inputField));
        inputPanel.add(inputField);
        inputPanel.add(inputBtn);

        // 选择混淆类型
        JPanel typePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        String[] type = {"密码混淆", "希尔伯特曲线混淆"};
        JComboBox<String> typeBox = new JComboBox<>(type);
        JTextField passwordField = new JTextField(30);
        typePanel.add(new JLabel("选择混淆类型："));
        typePanel.add(typeBox);
        JLabel passwordLabel = new JLabel("输入密码：");
        typePanel.add(passwordLabel);
        typePanel.add(passwordField);

        // 混淆按钮与解混淆按钮
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton scrambleButton = new JButton("混淆");
        JButton descrambleButton = new JButton("解混淆");
        buttonPanel.add(scrambleButton);
        buttonPanel.add(descrambleButton);

        // 事件监听
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

    private void createVideoScramblePanel() {
        videoScramblePanel = new JPanel(new BorderLayout(10, 10));
        videoScramblePanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        // 选择文件面板
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JTextField inputField = new JTextField(30);
        JButton inputBtn = new JButton("选择视频文件");
        inputBtn.addActionListener(e -> chooseVideoFile(inputField));
        inputPanel.add(inputField);
        inputPanel.add(inputBtn);

        // 选择混淆类型
        JPanel typePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        String[] type = {"密码混淆", "希尔伯特曲线混淆"};
        JComboBox<String> typeBox = new JComboBox<>(type);
        JTextField passwordField = new JTextField(30);
        typePanel.add(new JLabel("选择混淆类型："));
        typePanel.add(typeBox);
        JLabel passwordLabel = new JLabel("输入密码：");
        typePanel.add(passwordLabel);
        typePanel.add(passwordField);

        // 混淆按钮与解混淆按钮
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton scrambleButton = new JButton("混淆");
        JButton descrambleButton = new JButton("解混淆");
        buttonPanel.add(scrambleButton);
        buttonPanel.add(descrambleButton);

        // 事件监听
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

    private void videoScramble(int type, String videoPath, String password) throws Exception {
        int[] videoInfo = VideoScrambler.probeResolution(videoPath);
        long seed = 0;

        if (type == 0) {
            seed = PasswordScrambler.passwordToSeed(password);
        }

        VideoScrambler.scramble(videoPath,
                outputPath,
                videoInfo[0],
                videoInfo[1],
                videoInfo[2],
                seed,
                type);
    }

    private void videoDescramble(int type, String videoPath, String password) throws Exception {
        int[] videoInfo = VideoScrambler.probeResolution(videoPath);
        long seed = 0;

        if (type == 0) {
            seed = PasswordScrambler.passwordToSeed(password);
        }

        VideoScrambler.descramble(videoPath,
                outputPath,
                videoInfo[0],
                videoInfo[1],
                videoInfo[2],
                seed,
                type);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ScramblerGUI::new);
    }
}
