package kk3twt.abnormal.tools.calculators.factorial;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class FactorialGUI extends JFrame {

    private JPanel mainPanel;

    private JPanel combinationPanel;
    private JPanel factorialPanel;

    public FactorialGUI() {
        setTitle("阶乘计算");
        setSize(350,350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        setContentPane(mainPanel);
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();

        createFactorialPanel();
        createCombinationPanel();

        tabbedPane.addTab("阶乘计算", factorialPanel);
        tabbedPane.addTab("排列数计算", combinationPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
    }

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
                int m = Integer.parseInt(mField.getText().trim());
                int n = Integer.parseInt(nField.getText().trim());

                if (m > n) {
                    throw new Exception("m必须小于等于n");
                }

                long permutation = Calculators.Permutation(m, n);
                long combination = Calculators.Combination(m, n);

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
                int m = Integer.parseInt(mField.getText().trim());

                if (m < 0) {
                    throw new Exception("输入的数字必须大等于0");
                }

                long factorial = Calculators.Factorial(m);

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FactorialGUI().setVisible(true));
    }
}
