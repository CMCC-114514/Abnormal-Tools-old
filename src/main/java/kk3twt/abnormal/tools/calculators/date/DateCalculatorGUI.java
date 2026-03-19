package kk3twt.abnormal.tools.calculators.date;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.Instant;

/**
 * 日期计算器的图形用户界面（GUI）类。
 * 提供四个功能标签页：天数转日期、日期推算、日期间隔、公农历转换。
 * 使用 Swing 组件构建，包含输入字段、按钮和结果展示区域。
 *
 * @author CMCC-114514
 */
public class DateCalculatorGUI extends JFrame {
    private JPanel mainPanel;

    // 天数转日期面板
    private JPanel conversionPanel;
    private JTextField daysField;
    private JButton convertButton;
    private JTextArea conversionResult;

    // 日期推算面板
    private JPanel calculationPanel;
    private JTextField calcYearField;
    private JTextField calcMonthField;
    private JTextField calcDayField;
    private JTextField daysToAddField;
    private JButton calculateButton;
    private JTextArea calculationResult;

    // 日期间隔面板
    private JPanel intervalPanel;
    private JTextField startYearField;
    private JTextField startMonthField;
    private JTextField startDayField;
    private JTextField endYearField;
    private JTextField endMonthField;
    private JTextField endDayField;
    private JButton intervalButton;
    private JTextArea intervalResult;

    // 公农历转换面板
    private JPanel solarToLunarPanel;
    private JTextField solarYearField;
    private JTextField solarMonthField;
    private JTextField solarDayField;
    private JButton toLunarButton;
    private JTextArea lunarResult;

    /**
     * 构造方法，初始化窗口并设置布局。
     */
    public DateCalculatorGUI() {
        setTitle("日期计算器");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 400);
        setLocationRelativeTo(null);
        setVisible(true);

        initComponents();
        setupLayout();

        setContentPane(mainPanel);
    }

    /**
     * 初始化所有组件，创建标签页及面板。
     */
    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();

        // 创建四个功能面板
        createConversionPanel();
        createCalculationPanel();
        createIntervalPanel();
        createSolarToLunarPanel();

        tabbedPane.addTab("天数转日期", conversionPanel);
        tabbedPane.addTab("日期推算", calculationPanel);
        tabbedPane.addTab("日期间隔", intervalPanel);
        tabbedPane.addTab("公农历转换", solarToLunarPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // 添加状态栏提示信息
        JLabel infoLabel = new JLabel(
                "<html><center>计算结果可能有1~3天的误差，计算日期间隔时起始日期必须早于结束日期<br>" +
                        "公农历换算支持范围为1900年~2099年</center></html>",
                SwingConstants.CENTER
        );
        infoLabel.setFont(new Font("宋体", Font.PLAIN, 12));
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(infoLabel, BorderLayout.SOUTH);
    }

    /**
     * 创建“天数转日期”面板
     */
    private void createConversionPanel() {
        conversionPanel = new JPanel(new BorderLayout(10, 10));
        conversionPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入参数"));

        inputPanel.add(new JLabel("天数:"));
        daysField = new JTextField();
        inputPanel.add(daysField);

        convertButton = new JButton("转换");
        inputPanel.add(new JLabel(""));
        inputPanel.add(convertButton);

        // 结果面板
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(new TitledBorder("转换结果"));

        conversionResult = new JTextArea(8, 30);
        conversionResult.setEditable(false);
        conversionResult.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(conversionResult);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        conversionPanel.add(inputPanel, BorderLayout.NORTH);
        conversionPanel.add(resultPanel, BorderLayout.CENTER);

        // 添加事件监听
        convertButton.addActionListener(e -> convertDaysToDate());
    }

    /**
     * 创建“日期推算”面板
     */
    private void createCalculationPanel() {
        calculationPanel = new JPanel(new BorderLayout(10, 10));
        calculationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入参数"));

        inputPanel.add(new JLabel("起始年份:"));
        calcYearField = new JTextField();
        calcYearField.setText("2000");
        inputPanel.add(calcYearField);

        inputPanel.add(new JLabel("起始月份:"));
        calcMonthField = new JTextField();
        calcMonthField.setText("1");
        inputPanel.add(calcMonthField);

        inputPanel.add(new JLabel("起始日期:"));
        calcDayField = new JTextField();
        calcDayField.setText("1");
        inputPanel.add(calcDayField);

        inputPanel.add(new JLabel("要推算的天数:"));
        daysToAddField = new JTextField();
        inputPanel.add(daysToAddField);

        calculateButton = new JButton("推算");
        inputPanel.add(new JLabel(""));
        inputPanel.add(calculateButton);

        // 结果面板
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(new TitledBorder("推算结果"));

        calculationResult = new JTextArea(8, 30);
        calculationResult.setEditable(false);
        calculationResult.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(calculationResult);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        calculationPanel.add(inputPanel, BorderLayout.WEST);
        calculationPanel.add(resultPanel, BorderLayout.CENTER);

        // 添加事件监听
        calculateButton.addActionListener(e -> calculateDate());
    }

    /**
     * 创建“日期间隔”面板
     */
    private void createIntervalPanel() {
        intervalPanel = new JPanel(new BorderLayout(10, 10));
        intervalPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入日期"));

        inputPanel.add(new JLabel("开始年份:"));
        startYearField = new JTextField();
        startYearField.setText("2000");
        inputPanel.add(startYearField);

        inputPanel.add(new JLabel("开始月份:"));
        startMonthField = new JTextField();
        startMonthField.setText("1");
        inputPanel.add(startMonthField);

        inputPanel.add(new JLabel("开始日期:"));
        startDayField = new JTextField();
        startDayField.setText("1");
        inputPanel.add(startDayField);

        inputPanel.add(new JLabel("结束年份:"));
        endYearField = new JTextField();
        endYearField.setText(Instant.now().toString().substring(0,4));
        inputPanel.add(endYearField);

        inputPanel.add(new JLabel("结束月份:"));
        endMonthField = new JTextField();
        endMonthField.setText("1");
        inputPanel.add(endMonthField);

        inputPanel.add(new JLabel("结束日期:"));
        endDayField = new JTextField();
        endDayField.setText("1");
        inputPanel.add(endDayField);

        intervalButton = new JButton("计算间隔");
        inputPanel.add(new JLabel(""));
        inputPanel.add(intervalButton);

        // 结果面板
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(new TitledBorder("间隔计算结果"));

        intervalResult = new JTextArea(6, 30);
        intervalResult.setEditable(false);
        intervalResult.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(intervalResult);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        intervalPanel.add(inputPanel, BorderLayout.WEST);
        intervalPanel.add(resultPanel, BorderLayout.CENTER);

        // 添加事件监听
        intervalButton.addActionListener(e -> calculateInterval());
    }

    /**
     * 创建“公农历转换”面板
     */
    private void createSolarToLunarPanel() {
        solarToLunarPanel = new JPanel(new BorderLayout(10, 10));
        solarToLunarPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入公历日期"));

        inputPanel.add(new JLabel("年份:"));
        solarYearField = new JTextField();
        solarYearField.setText("2000");
        inputPanel.add(solarYearField);

        inputPanel.add(new JLabel("月份:"));
        solarMonthField = new JTextField();
        solarMonthField.setText("1");
        inputPanel.add(solarMonthField);

        inputPanel.add(new JLabel("日期:"));
        solarDayField = new JTextField();
        solarDayField.setText("1");
        inputPanel.add(solarDayField);

        toLunarButton = new JButton("转换");
        inputPanel.add(new JLabel(""));
        inputPanel.add(toLunarButton);

        // 结果面板
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(new TitledBorder("转换结果"));

        lunarResult = new JTextArea(8, 30);
        lunarResult.setEditable(false);
        lunarResult.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(lunarResult);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        solarToLunarPanel.add(inputPanel, BorderLayout.WEST);
        solarToLunarPanel.add(resultPanel, BorderLayout.CENTER);

        // 添加事件监听
        toLunarButton.addActionListener(e -> convertSolarToLunar());
    }

    /**
     * 设置统一组件样式（字体等）
     */
    private void setupLayout() {
        Font buttonFont = new Font("微软雅黑", Font.PLAIN, 13);
        convertButton.setFont(buttonFont);
        calculateButton.setFont(buttonFont);
        intervalButton.setFont(buttonFont);
        toLunarButton.setFont(buttonFont);
    }

    /**
     * 处理“天数转日期”按钮的点击事件
     */
    private void convertDaysToDate() {
        try {
            int days = Integer.parseInt(daysField.getText().trim());

            if (days < 0) {
                conversionResult.setText("错误：天数不能为负数！");
                return;
            }

            Date result = Calculators.Conversion(days, 1);
            conversionResult.setText(String.format("输入天数: %d\n转换结果:\n%d年%d个月%d天",
                    days, result.year, result.month, result.day));

        } catch (NumberFormatException e) {
            conversionResult.setText("错误：请输入有效的数字！");
        } catch (Exception e) {
            conversionResult.setText("错误：" + e.getMessage());
        }
    }

    /**
     * 处理“日期推算”按钮的点击事件
     */
    private void calculateDate() {
        try {
            int year = Integer.parseInt(calcYearField.getText().trim());
            int month = Integer.parseInt(calcMonthField.getText().trim());
            int day = Integer.parseInt(calcDayField.getText().trim());
            int daysToAdd = Integer.parseInt(daysToAddField.getText().trim());

            // 验证日期有效性
            if (month < 1 || month > 12) {
                calculationResult.setText("错误：月份必须在1-12之间！");
                return;
            }

            int maxDays = AuxFunctions.getDayOfMonth(month, year);
            if (day < 1 || day > maxDays) {
                calculationResult.setText(String.format("错误：%d年%d月的日期必须在1-%d之间！", year, month, maxDays));
                return;
            }

            Date startDate = new Date(year, month, day);
            Date result = Calculators.Calculation(startDate, daysToAdd);

            String direction = daysToAdd >= 0 ? "向后" : "向前";
            calculationResult.setText(String.format("起始日期: %d年%d月%d日\n推算天数: %d（%s推算）\n\n推算结果:\n%d年%d月%d日",
                    year, month, day, Math.abs(daysToAdd), direction,
                    result.year, result.month, result.day));

        } catch (NumberFormatException e) {
            calculationResult.setText("错误：请输入有效的数字！");
        } catch (Exception e) {
            calculationResult.setText("错误：" + e.getMessage());
        }
    }

    /**
     * 处理“日期间隔”按钮的点击事件
     */
    private void calculateInterval() {
        try {
            int startYear = Integer.parseInt(startYearField.getText().trim());
            int startMonth = Integer.parseInt(startMonthField.getText().trim());
            int startDay = Integer.parseInt(startDayField.getText().trim());
            int endYear = Integer.parseInt(endYearField.getText().trim());
            int endMonth = Integer.parseInt(endMonthField.getText().trim());
            int endDay = Integer.parseInt(endDayField.getText().trim());

            // 验证日期有效性
            if (startMonth < 1 || startMonth > 12 || endMonth < 1 || endMonth > 12) {
                intervalResult.setText("错误：月份必须在1-12之间！");
                return;
            }

            int startMaxDays = AuxFunctions.getDayOfMonth(startMonth, startYear);
            int endMaxDays = AuxFunctions.getDayOfMonth(endMonth, endYear);

            if (startDay < 1 || startDay > startMaxDays) {
                intervalResult.setText(String.format("错误：开始日期的%d年%d月日期必须在1-%d之间！",
                        startYear, startMonth, startMaxDays));
                return;
            }

            if (endDay < 1 || endDay > endMaxDays) {
                intervalResult.setText(String.format("错误：结束日期的%d年%d月日期必须在1-%d之间！",
                        endYear, endMonth, endMaxDays));
                return;
            }

            Date startDate = new Date(startYear, startMonth, startDay);
            Date endDate = new Date(endYear, endMonth, endDay);

            int interval = Calculators.Interval(startDate, endDate);

            if (interval == -1) {
                intervalResult.setText("错误：开始日期必须在结束日期之前！");
                return;
            }

            intervalResult.setText(String.format("开始日期: %d年%d月%d日\n结束日期: %d年%d月%d日\n\n间隔天数: %d天",
                    startYear, startMonth, startDay, endYear, endMonth, endDay, interval));

        } catch (NumberFormatException e) {
            intervalResult.setText("错误：请输入有效的数字！");
        } catch (Exception e) {
            intervalResult.setText("错误：" + e.getMessage());
        }
    }

    /**
     * 处理“公农历转换”按钮的点击事件
     */
    private void convertSolarToLunar() {
        try {
            int year = Integer.parseInt(solarYearField.getText().trim());
            int month = Integer.parseInt(solarMonthField.getText().trim());
            int day = Integer.parseInt(solarDayField.getText().trim());

            // 验证日期有效性
            if (year < 1900 || year > 2099) {
                calculationResult.setText("错误：年份必须在1900-2099之间！");
                return;
            }

            if (month < 1 || month > 12) {
                calculationResult.setText("错误：月份必须在1-12之间！");
                return;
            }

            int maxDays = AuxFunctions.getDayOfMonth(month, year);
            if (day < 1 || day > maxDays) {
                calculationResult.setText(String.format("错误：%d年%d月的日期必须在1-%d之间！", year, month, maxDays));
                return;
            }

            Date solarDate = new Date(year, month, day);
            LunarDate lunarDate = LunarCalendar.solarToLunar(solarDate);

            lunarResult.setText(String.format("公历日期：%d年%d月%d日\n\n转换结果：\n%s",
                    year, month, day, lunarDate));

        } catch (NumberFormatException e) {
            calculationResult.setText("错误：请输入有效的数字！");
        } catch (Exception e) {
            calculationResult.setText("错误：" + e.getMessage());
        }
    }

    /**
     * 程序入口，启动GUI
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DateCalculatorGUI::new);
    }
}