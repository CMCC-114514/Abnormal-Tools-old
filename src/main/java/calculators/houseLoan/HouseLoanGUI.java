package calculators.houseLoan;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class HouseLoanGUI extends JFrame {
    public HouseLoanGUI() {
        setTitle("房贷计算器");
        setSize(400,450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel houseLoanPanel = new JPanel(new BorderLayout(10, 10));
        houseLoanPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20,20));

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(new TitledBorder("输入信息"));

        JComboBox<String> patternCombo = new JComboBox<>(Calculators.PATTERNS);
        JTextField limitField = new JTextField();
        JTextField loanAmountField = new JTextField();
        JTextField rateField = new JTextField();

        inputPanel.add(new JLabel("贷款方式："));
        inputPanel.add(patternCombo);
        inputPanel.add(new JLabel("贷款期数："));
        inputPanel.add(limitField);
        inputPanel.add(new JLabel("贷款金额（万元）："));
        inputPanel.add(loanAmountField);
        inputPanel.add(new JLabel("贷款年利率（%）："));
        inputPanel.add(rateField);

        JButton calculateButton = new JButton("计算");
        inputPanel.add(new JLabel(""));
        inputPanel.add(calculateButton);

        // 结果面板
        JTextArea resultArea = new JTextArea(8, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("计算结果"));

        houseLoanPanel.add(inputPanel, BorderLayout.NORTH);
        houseLoanPanel.add(scrollPane, BorderLayout.CENTER);

        // 事件处理
        calculateButton.addActionListener(e -> {
            try {
                int selectedIndex = patternCombo.getSelectedIndex();
                int totalMonth = Integer.parseInt(limitField.getText().trim());
                double principal = Double.parseDouble(loanAmountField.getText().trim()) * 1e4;
                double rate = Double.parseDouble(rateField.getText().trim()) / 1200;

                if (selectedIndex == 0) {
                    double result = Calculators.interest(principal, rate, totalMonth);
                    String outText = getString(result, totalMonth, principal);

                    resultArea.setText(outText);

                } else if (selectedIndex == 1) {
                    String outText = getString(principal, rate, totalMonth);

                    resultArea.setText(outText);
                }
            } catch (NumberFormatException ex) {
                resultArea.setText("错误：请输入有效的数字！");
            } catch (IllegalArgumentException ex) {
                resultArea.setText("错误：" + ex.getMessage());
            }
        });

        add(houseLoanPanel);
    }

    // 等额本金输出结果
    private static String getString(double principal, double rate, int totalMonth) {
        double[] result = Calculators.capital(principal, rate, totalMonth);
        double totalAmount = (result[0] + result[result.length - 1]) * result.length / 2;
        double firstMonth = result[0];
        double degressive = result[0] - result[1];
        double lastMonth = result[result.length - 1];
        double totalInterest = principal * rate * (result.length + 1) / 2;

        return String.format("""
                贷款金额：%.2f 万元
                贷款期数：%d 期 （约 %.1f 年）
                
                还款总额：%.2f 元
                首月还款：%.2f 元
                最后一月还款：%.2f 元
                每月递减：%.2f 元
                总利息：%.2f 元
                """,
                principal /1e4, totalMonth, totalMonth /12.0, totalAmount, firstMonth, lastMonth, degressive, totalInterest);
    }

    // 等额本息输出结果
    private static String getString(double result, int totalMonth, double principal) {
        double totalAmount = result * totalMonth;
        double totalInterest = totalAmount - principal;

        return String.format("""
                贷款金额：%.2f 万元
                贷款期数：%d 期 （约 %.1f 年）
                
                还款总额：%.2f 元
                每月还款：%.2f 元
                总利息：%.2f 元
                """
                , principal /1e4, totalMonth, totalMonth /12.0, totalAmount, result, totalInterest);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HouseLoanGUI().setVisible(true));
    }
}
