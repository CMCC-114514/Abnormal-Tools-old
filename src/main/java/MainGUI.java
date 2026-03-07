import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

// 计算功能
import calculators.dateCalculation.DateCalculatorGUI;
import calculators.factorial.FactorialGUI;
import calculators.geometricCalculation.GeometricCalculatorGUI;
import calculators.unitsConversion.UnitConverterGUI;
import calculators.bmi.BmiGUI;
import calculators.houseLoan.HouseLoanGUI;

// 文件功能
import fileFunctions.imageScramble.ScramblerGUI;
import fileFunctions.formatConversion.formatConversionGUI;
import fileFunctions.musicUnlocker.MusicUnlockerGUI;
import fileFunctions.fileDownloader.FileDownloaderGUI;

// 其他功能
import otherFunctions.base64.Bas64GUI;
import otherFunctions.md5.Md5GUI;
import otherFunctions.randomGenerator.RandomGUI;
import otherFunctions.scoreBoard.ScoreBoard;

// 初始化
import utils.Initializer;

public class MainGUI extends JFrame{
    private static final Dimension BUTTON_SIZE = new Dimension(80, 40);

    public MainGUI(String[] args) {
        setTitle("某科学的工具箱");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 设置中文字体
        Font font = new Font("微软雅黑", Font.PLAIN, 12);
        UIManager.put("Button.font", font);
        UIManager.put("Label.font", font);
        UIManager.put("Menu.font", font);
        UIManager.put("MenuItem.font", font);

        // 设置窗口属性
        setSize(380, 400);
        setLocationRelativeTo(null);      // 使窗口居中显示
        setLayout(new BorderLayout());

        // 创建主面板和各功能面板，根据功能分类
        JPanel mainPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        JPanel calculatorPanel = getCalculatorPanel(args, font);
        JPanel convertorPanel = getFileFunctionPanel(args, font);
        JPanel otherFunctionPanel = getOtherFunctionPanel(args, font);

        // 添加面板
        mainPanel.add(calculatorPanel, BorderLayout.CENTER);
        mainPanel.add(convertorPanel, BorderLayout.CENTER);
        mainPanel.add(otherFunctionPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // 创建菜单栏和底部标签
        JMenuBar menuBar = getMenuBar(font, this);
        JLabel footerLabel = getFooterLabel(font);
        setJMenuBar(menuBar);
        add(footerLabel, BorderLayout.SOUTH);
    }

    // 底部标签
    private static JLabel getFooterLabel(Font font) {
        JLabel footerLabel = new JLabel("<html><center>选择功能开始使用</center></html>", SwingConstants.CENTER);
        footerLabel.setFont(font);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return footerLabel;
    }

    // 菜单栏和关于界面
    private static JMenuBar getMenuBar(Font font, JFrame frame) {
        JMenuBar menuBar = new JMenuBar();
        JMenu aboutMenu = new JMenu("关于");
        aboutMenu.setFont(font);

        JMenuItem aboutItem = new JMenuItem("关于本程序");
        aboutItem.setFont(font);
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(
                frame,
                """
                        某科学的工具箱 v1.6.2
                        
                        爱来自kk3TWT
                        
                        作者不会排版，别问为什么这么丑了
                        """,
                "关于",
                JOptionPane.INFORMATION_MESSAGE
        ));

        aboutMenu.add(aboutItem);
        menuBar.add(aboutMenu);
        return menuBar;
    }

    // 计算功能
    private static JPanel getCalculatorPanel(String[] args, Font font) {
        // 计算功能
        JPanel calculatorPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        calculatorPanel.setBorder(new TitledBorder("计算功能"));

        JButton dateCalculation = new JButton("日期计算");
        JButton unitConversion = new JButton("单位换算");
        JButton geometricCalculation = new JButton("几何计算");
        JButton bmiCalculation = new JButton("BMI计算");
        JButton houseLoanCalculation = new JButton("房贷计算");
        JButton factorialCalculation = new JButton("阶乘计算");

        // 设置按钮字体
        dateCalculation.setFont(font);
        unitConversion.setFont(font);
        geometricCalculation.setFont(font);
        bmiCalculation.setFont(font);
        houseLoanCalculation.setFont(font);
        factorialCalculation.setFont(font);

        // 设置按钮大小
        dateCalculation.setPreferredSize(BUTTON_SIZE);
        unitConversion.setPreferredSize(BUTTON_SIZE);
        geometricCalculation.setPreferredSize(BUTTON_SIZE);
        bmiCalculation.setPreferredSize(BUTTON_SIZE);
        houseLoanCalculation.setPreferredSize(BUTTON_SIZE);
        factorialCalculation.setPreferredSize(BUTTON_SIZE);

        // 按钮监听
        dateCalculation.addActionListener(e -> DateCalculatorGUI.main(args));
        unitConversion.addActionListener(e -> UnitConverterGUI.main(args));
        geometricCalculation.addActionListener(e -> GeometricCalculatorGUI.main(args));
        bmiCalculation.addActionListener(e -> BmiGUI.main(args));
        houseLoanCalculation.addActionListener(e -> HouseLoanGUI.main(args));
        factorialCalculation.addActionListener(e -> FactorialGUI.main(args));

        // 添加按钮到面板
        calculatorPanel.add(dateCalculation);
        calculatorPanel.add(unitConversion);
        calculatorPanel.add(geometricCalculation);
        calculatorPanel.add(bmiCalculation);
        calculatorPanel.add(houseLoanCalculation);
        calculatorPanel.add(factorialCalculation);

        return calculatorPanel;
    }

    // 文件功能
    private static JPanel getFileFunctionPanel(String[] args, Font font) {
        // 文件功能
        JPanel fileFunctionPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        fileFunctionPanel.setBorder(new TitledBorder("文件功能"));

        JButton musicUnlock = new JButton("音乐解锁");
        JButton formatConversion = new JButton("音视频格式转换");
        JButton fileDownload = new JButton("文件下载器");
        JButton imageScramble = new JButton("图片与视频混淆");

        // 设置按钮字体
        musicUnlock.setFont(font);
        formatConversion.setFont(font);
        fileDownload.setFont(font);
        imageScramble.setFont(font);

        // 设置按钮大小
        musicUnlock.setPreferredSize(BUTTON_SIZE);
        formatConversion.setPreferredSize(BUTTON_SIZE);
        fileDownload.setPreferredSize(BUTTON_SIZE);
        formatConversion.setPreferredSize(BUTTON_SIZE);

        // 按钮监听
        musicUnlock.addActionListener(e -> MusicUnlockerGUI.main(args));
        formatConversion.addActionListener(e -> formatConversionGUI.main(args));
        fileDownload.addActionListener(e -> FileDownloaderGUI.main(args));
        imageScramble.addActionListener(e -> ScramblerGUI.main(args));

        // 添加按钮
        fileFunctionPanel.add(musicUnlock);
        fileFunctionPanel.add(formatConversion);
        fileFunctionPanel.add(fileDownload);
        fileFunctionPanel.add(imageScramble);

        return fileFunctionPanel;
    }

    // 其他功能
    private static JPanel getOtherFunctionPanel(String[] args, Font font) {
        // 其他功能
        JPanel otherFunctionPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        otherFunctionPanel.setBorder(new TitledBorder("其他功能"));

        JButton md5 = new JButton("MD5摘要");
        JButton base64 = new JButton("Base64编解码");
        JButton randomNum = new JButton("随机数生成");
        JButton scoreBoard = new JButton("计分板");

        // 设置按钮字体
        md5.setFont(font);
        base64.setFont(font);
        randomNum.setFont(font);
        scoreBoard.setFont(font);

        // 设置按钮大小
        md5.setPreferredSize(BUTTON_SIZE);
        base64.setPreferredSize(BUTTON_SIZE);
        randomNum.setPreferredSize(BUTTON_SIZE);
        scoreBoard.setPreferredSize(BUTTON_SIZE);

        // 按钮监听
        md5.addActionListener(e -> Md5GUI.main(args));
        base64.addActionListener(e -> Bas64GUI.main(args));
        randomNum.addActionListener(e -> RandomGUI.main(args));
        scoreBoard.addActionListener(e -> ScoreBoard.main(args));

        // 添加按钮
        otherFunctionPanel.add(md5);
        otherFunctionPanel.add(base64);
        otherFunctionPanel.add(randomNum);
        otherFunctionPanel.add(scoreBoard);

        return otherFunctionPanel;
    }

    // 程序入口
    public static void main(String[] args) {
        try {
            // 设置系统外观
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            if (Initializer.isInitialized()) {
                SwingUtilities.invokeLater(() -> new MainGUI(args).setVisible(true));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"错误" + e.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
        }
    }
}
