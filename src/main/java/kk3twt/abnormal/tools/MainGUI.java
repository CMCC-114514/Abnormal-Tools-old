package kk3twt.abnormal.tools;

import javax.swing.*;
import java.awt.*;

// 计算功能
import kk3twt.abnormal.tools.calculators.date.DateCalculatorGUI;
import kk3twt.abnormal.tools.calculators.factorial.FactorialGUI;
import kk3twt.abnormal.tools.calculators.geometry.GeometricCalculatorGUI;
import kk3twt.abnormal.tools.calculators.probability.ProbabilityGUI;
import kk3twt.abnormal.tools.calculators.unitsConversion.UnitConverterGUI;
import kk3twt.abnormal.tools.calculators.bmi.BmiGUI;
import kk3twt.abnormal.tools.calculators.houseLoan.HouseLoanGUI;

// 文件功能
import kk3twt.abnormal.tools.fileFunctions.imageScramble.ScramblerGUI;
import kk3twt.abnormal.tools.fileFunctions.formatConversion.formatConversionGUI;
import kk3twt.abnormal.tools.fileFunctions.musicUnlocker.MusicUnlockerGUI;
import kk3twt.abnormal.tools.fileFunctions.fileDownloader.FileDownloaderGUI;

// 其他功能
import kk3twt.abnormal.tools.otherFunctions.base64.Bas64GUI;
import kk3twt.abnormal.tools.otherFunctions.loliconImage.LoliconGUI;
import kk3twt.abnormal.tools.otherFunctions.md5.Md5GUI;
import kk3twt.abnormal.tools.otherFunctions.randomGenerator.RandomGUI;
import kk3twt.abnormal.tools.otherFunctions.scoreBoard.ScoreBoard;

public class MainGUI extends JFrame{
    private static final Dimension BUTTON_SIZE = new Dimension(80, 40);

    private JPanel calculatorPanel;
    private JPanel fileFunctionPanel;
    private JPanel otherFunctionPanel;
    private JPanel aboutPanel;

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
        setSize(430, 300);
        setLocationRelativeTo(null);      // 使窗口居中显示
        setLayout(new BorderLayout());
        setVisible(true);

        // 创建标签页
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(font);

        // 创建主面板和各功能面板，根据功能分类
        getCalculatorPanel(args, font);
        getFileFunctionPanel(args, font);
        getOtherFunctionPanel(args, font);
        getAboutPanel(font);

        // 添加面板
        tabbedPane.addTab("计算功能", calculatorPanel);
        tabbedPane.addTab("文件功能", fileFunctionPanel);
        tabbedPane.addTab("其他功能", otherFunctionPanel);
        tabbedPane.addTab("关于", aboutPanel);
        add(tabbedPane);

        // 创建底部标签
        JLabel footerLabel = getFooterLabel(font);
        add(footerLabel, BorderLayout.SOUTH);
    }

    // 底部标签
    private JLabel getFooterLabel(Font font) {
        JLabel footerLabel = new JLabel("<html><center>选择功能开始使用</center></html>", SwingConstants.CENTER);
        footerLabel.setFont(font);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return footerLabel;
    }

    // 关于界面
    private void getAboutPanel(Font font) {
        aboutPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        aboutPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JButton aboutButton = new JButton("关于本程序");
        aboutButton.setFont(font);
        aboutButton.addActionListener(e -> JOptionPane.showMessageDialog(
                null,
                """
                        某科学的工具箱

                        爱来自kk3TWT
                        """,
                "关于本程序",
                JOptionPane.INFORMATION_MESSAGE
        ));

        JButton updateButton = new JButton("更新日志");
        updateButton.setFont(font);
        updateButton.addActionListener(e -> JOptionPane.showMessageDialog(
                null,
                """
                        Abnormal-Tools v1.7.0
                        
                        新增：
                        1. 计算功能 -> 概率计算：根据概率模型和相关参数进行离散型随机变量的概率计算
                        2. 其他功能 -> Pixiv 图片搜索：根据 pid、tag 等参数进行图片检索，Lolicon API 提供支持
                        
                        修正：
                        1. 删除了菜单栏，添加了“关于”标签页
                        2. 现在可以在软件内查看更新日志了
                        3. 修复了启动jar时报错“错误定位AppHome失败”的问题
                        4. 修复了“视频混淆”不能调用依赖中的ffmpeg的问题
                        5. 将依赖文件夹名称由/lib 改为了/resources
                        6. QWQ
                        """,
                "更新日志",
                JOptionPane.INFORMATION_MESSAGE
        ));

        JButton qwq = new JButton("QWQ");
        qwq.setFont(font);
        qwq.addActionListener(e -> JOptionPane.showMessageDialog(
                null,
                """
                        QWQ TAT AWA TWT
                        
                        都看到这里了，来Q群：904976878 玩呗
                        
                        [某群聊的空间移动]是由<某科学管理组>和 Test 为中心并基于<某康康的聊天群>的神奇群聊.
                        以Tencent QQ 为据点为群友提供安全稳定的涩图环境，
                        在以邀请制为核心的制度下没有阴间人和广告等困扰的优良涩图氛围，
                        管理组积极负责24小时全天在线解决问题.
                        群内巨佬云集, 不乏有「嘉心糖」,「二刺螈」,「音游人」,「程序猿」,「男酮」,「原农粥撸幻批」等等重量级选手.
                        """,
                "QWQ",
                JOptionPane.QUESTION_MESSAGE
        ));

        aboutPanel.add(aboutButton);
        aboutPanel.add(updateButton);
        aboutPanel.add(qwq);
    }

    // 计算功能
    private void getCalculatorPanel(String[] args, Font font) {
        // 计算功能
        calculatorPanel = new JPanel(new GridLayout(3, 3, 20, 20));
        calculatorPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton dateCalculation = new JButton("日期计算");
        JButton unitConversion = new JButton("单位换算");
        JButton geometricCalculation = new JButton("几何计算");
        JButton bmiCalculation = new JButton("BMI计算");
        JButton houseLoanCalculation = new JButton("房贷计算");
        JButton factorialCalculation = new JButton("阶乘计算");
        JButton probabilityCalculation = new JButton("概率计算");

        // 设置按钮字体
        dateCalculation.setFont(font);
        unitConversion.setFont(font);
        geometricCalculation.setFont(font);
        bmiCalculation.setFont(font);
        houseLoanCalculation.setFont(font);
        factorialCalculation.setFont(font);
        probabilityCalculation.setFont(font);

        // 设置按钮大小
        dateCalculation.setPreferredSize(BUTTON_SIZE);
        unitConversion.setPreferredSize(BUTTON_SIZE);
        geometricCalculation.setPreferredSize(BUTTON_SIZE);
        bmiCalculation.setPreferredSize(BUTTON_SIZE);
        houseLoanCalculation.setPreferredSize(BUTTON_SIZE);
        factorialCalculation.setPreferredSize(BUTTON_SIZE);
        probabilityCalculation.setPreferredSize(BUTTON_SIZE);

        // 按钮监听
        dateCalculation.addActionListener(e -> DateCalculatorGUI.main(args));
        unitConversion.addActionListener(e -> UnitConverterGUI.main(args));
        geometricCalculation.addActionListener(e -> GeometricCalculatorGUI.main(args));
        bmiCalculation.addActionListener(e -> BmiGUI.main(args));
        houseLoanCalculation.addActionListener(e -> HouseLoanGUI.main(args));
        factorialCalculation.addActionListener(e -> FactorialGUI.main(args));
        probabilityCalculation.addActionListener(e -> ProbabilityGUI.main(args));

        // 添加按钮到面板
        calculatorPanel.add(dateCalculation);
        calculatorPanel.add(unitConversion);
        calculatorPanel.add(geometricCalculation);
        calculatorPanel.add(bmiCalculation);
        calculatorPanel.add(houseLoanCalculation);
        calculatorPanel.add(factorialCalculation);
        calculatorPanel.add(probabilityCalculation);
    }

    // 文件功能
    private void getFileFunctionPanel(String[] args, Font font) {
        // 文件功能
        fileFunctionPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        fileFunctionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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
    }

    // 其他功能
    private void getOtherFunctionPanel(String[] args, Font font) {
        // 其他功能
        otherFunctionPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        otherFunctionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton md5 = new JButton("MD5摘要");
        JButton base64 = new JButton("Base64编解码");
        JButton randomNum = new JButton("随机数生成");
        JButton scoreBoard = new JButton("计分板");
        JButton loliconImage = new JButton("Pixiv图片搜索");

        // 设置按钮字体
        md5.setFont(font);
        base64.setFont(font);
        randomNum.setFont(font);
        scoreBoard.setFont(font);
        loliconImage.setFont(font);

        // 设置按钮大小
        md5.setPreferredSize(BUTTON_SIZE);
        base64.setPreferredSize(BUTTON_SIZE);
        randomNum.setPreferredSize(BUTTON_SIZE);
        scoreBoard.setPreferredSize(BUTTON_SIZE);
        loliconImage.setPreferredSize(BUTTON_SIZE);

        // 按钮监听
        md5.addActionListener(e -> Md5GUI.main(args));
        base64.addActionListener(e -> Bas64GUI.main(args));
        randomNum.addActionListener(e -> RandomGUI.main(args));
        scoreBoard.addActionListener(e -> ScoreBoard.main(args));
        loliconImage.addActionListener(e -> LoliconGUI.main(args));

        // 添加按钮
        otherFunctionPanel.add(md5);
        otherFunctionPanel.add(base64);
        otherFunctionPanel.add(randomNum);
        otherFunctionPanel.add(scoreBoard);
        otherFunctionPanel.add(loliconImage);
    }

    // 程序入口
    public static void main(String[] args) {
        try {
            // 设置系统外观
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.invokeLater(() -> new MainGUI(args).setVisible(true));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"错误" + e.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
        }
    }
}
