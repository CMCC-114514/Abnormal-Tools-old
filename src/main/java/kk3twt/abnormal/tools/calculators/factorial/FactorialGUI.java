package kk3twt.abnormal.tools.calculators.factorial;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * 阶乘计算GUI类
 * 显示图形用户界面
 */
public class FactorialGUI extends JFrame {

    private JPanel mainPanel;

    private JPanel combinationPanel;
    private JPanel factorialPanel;

    /**
     * 构造函数
     * 设置窗口参数
     */
    public FactorialGUI() {
        setTitle("阶乘计算");
        setSize(350,350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        initComponents();
        setContentPane(mainPanel);
    }

    /**
     * 初始化图形构件
     * 添加标签页
     */
    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();

        createFactorialPanel();
        createCombinationPanel();

        tabbedPane.addTab("阶乘计算", factorialPanel);
        tabbedPane.addTab("排列数计算", combinationPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * 创建排列数面板
     */
    private void createCombinationPanel() {
        combinationPanel = new JPanel(new BorderLayout(10, 10));
        combinationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入数字"));

        JTextField mField = new JTextField();
        JTextField nField = new JTextField();

        inputPanel.add(new JLabel("m："));
        inputPanel.add(mField);
        inputPanel.add(new JLabel("n："));
        inputPanel.add(nField);

        JButton calculateButton = new JButton("计算");
        inputPanel.add(new JLabel(""));
        inputPanel.add(calculateButton);

        // 结果面板
        JTextArea resultArea = new JTextArea(4, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("计算结果"));

        combinationPanel.add(inputPanel, BorderLayout.NORTH);
        combinationPanel.add(scrollPane, BorderLayout.CENTER);

        // 事件处理
        calculateButton.addActionListener(e -> {
            try {
                // 获取输入
                int m = Integer.parseInt(mField.getText().trim());
                int n = Integer.parseInt(nField.getText().trim());

                // m > n 时报错
                if (m > n) {
                    resultArea.setText("错误：m 必须小于或等于 n");
                    return;
                }

                // 排列数与组合数计算结果
                long permutation = Calculators.Permutation(m, n);
                long combination = Calculators.Combination(m, n);

                // 输出结果
                String outText = String.format("""
                        排列数A：%d
                        组合数C：%d
                        """, permutation, combination);
                resultArea.setText(outText);
            } catch (NumberFormatException ex) {
                resultArea.setText("错误：请输入有效的数字！");
            } catch (Exception ex) {
                resultArea.setText("错误：" + ex.getMessage());
            }
        });
    }

    /**
     * 创建阶乘计算面板
     */
    private void createFactorialPanel() {
        factorialPanel = new JPanel(new BorderLayout(10, 10));
        factorialPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入数字"));

        JTextField mField = new JTextField();
        inputPanel.add(new JLabel("一个大等于0的数字："));
        inputPanel.add(mField);

        JButton calculateButton = new JButton("计算");
        inputPanel.add(new JLabel(""));
        inputPanel.add(calculateButton);

        // 结果面板
        JTextArea resultArea = new JTextArea(4, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("计算结果"));

        factorialPanel.add(inputPanel, BorderLayout.NORTH);
        factorialPanel.add(scrollPane, BorderLayout.CENTER);

        // 事件处理
        calculateButton.addActionListener(e -> {
            try {
                // 接收输入
                int m = Integer.parseInt(mField.getText().trim());

                // m < 0 时报错
                if (m < 0) {
                    resultArea.setText("错误：输入的数字必须大于或等于0");
                    return;
                }

                // 计算结果
                long factorial = Calculators.Factorial(m);

                // 输出结果
                String outText = String.format("""
                        %d 的阶乘：%d
                        """, m, factorial);
                resultArea.setText(outText);
            } catch (NumberFormatException ex) {
                resultArea.setText("错误：请输入有效的数字！");
            } catch (Exception ex) {
                resultArea.setText("错误：" + ex.getMessage());
            }
        });
    }

    /**
     * 程序入口
     * @param args 命令行指令
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(FactorialGUI::new);
    }
}
