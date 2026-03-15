package kk3twt.abnormal.tools.calculators.bmi;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * BMI 计算器的图形用户界面。
 * 该类继承 JFrame，构建一个包含身高体重输入、计算按钮和结果显示区域的窗口。
 */
public class BmiGUI extends JFrame {

    /**
     * 构造 BMI 计算器的主窗口。
     * 初始化窗口属性，添加输入面板和结果面板，并绑定计算按钮的事件。
     */
    public BmiGUI() {
        setTitle("BMI计算器");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // 窗口居中显示
        setLayout(new BorderLayout());

        // 主面板，使用边界布局并设置内边距
        JPanel bmiPanel = new JPanel(new BorderLayout(10, 10));
        bmiPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 输入面板：网格布局 3 行 2 列，用于放置标签、文本框和按钮
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入信息"));

        JTextField weightField = new JTextField(); // 体重输入框
        JTextField heightField = new JTextField(); // 身高输入框

        inputPanel.add(new JLabel("身高（cm）："));
        inputPanel.add(heightField);
        inputPanel.add(new JLabel("体重（kg)："));
        inputPanel.add(weightField);

        JButton calculateButton = new JButton("计算");

        // 添加一个空白标签占位，使按钮位于第三行第二列
        inputPanel.add(new JLabel(""));
        inputPanel.add(calculateButton);

        // 结果面板：使用 JTextArea 显示多行结果，并放入滚动面板
        JTextArea resultArea = new JTextArea(4, 30);
        resultArea.setEditable(false);               // 不允许编辑
        resultArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("计算结果"));

        bmiPanel.add(inputPanel, BorderLayout.NORTH);
        bmiPanel.add(scrollPane, BorderLayout.CENTER);

        // 计算按钮的事件处理
        calculateButton.addActionListener(e -> {
            try {
                // 获取输入并转换单位：身高由厘米转为米
                double height = Double.parseDouble(heightField.getText().trim()) / 100;
                double weight = Double.parseDouble(weightField.getText().trim());

                // 调用 BMI 类的方法进行计算和分类
                double result = Calculators.calculate(weight, height);
                String type = Calculators.returnType(result);

                // 格式化输出结果，包含原始身高（厘米）、身高（米）、体重、BMI 值和体重分类
                String outText = String.format(
                        """
                        身高：%.1f cm / %.2f m
                        体重：%.2f kg

                        BMI：%.2f
                        你的体重%s""", height * 100, height, weight, result, type);

                resultArea.setText(outText);
            } catch (NumberFormatException ex) {

                // 输入非数字时的异常处理
                resultArea.setText("错误：请输入有效的数字！");

            } catch (IllegalArgumentException ex) {

                // 体重或身高不满足正数条件时的异常处理
                resultArea.setText("错误：" + ex.getMessage());
            }
        });

        add(bmiPanel);
    }

    /**
     * 程序入口，在事件分发线程中启动 GUI。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BmiGUI().setVisible(true));
    }
}