package kk3twt.abnormal.tools.calculators.houseLoan;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * 房贷计算器图形界面。
 * 继承自 JFrame，提供贷款方式选择、贷款期数、金额、年利率的输入，
 * 点击“计算”按钮后，根据所选方式调用 Calculators 类的方法进行计算，
 * 并显示还款总额、每月还款额、总利息等详细信息。
 */
public class HouseLoanGUI extends JFrame {

    /**
     * 构造函数，初始化窗口并构建界面。
     */
    public HouseLoanGUI() {
        setTitle("房贷计算器");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setVisible(true);

        // 主面板，包含输入区域和结果显示区域
        JPanel houseLoanPanel = new JPanel(new BorderLayout(10, 10));
        houseLoanPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 输入面板（使用网格布局）
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(new TitledBorder("输入信息"));

        JComboBox<String> patternCombo = new JComboBox<>(Calculators.PATTERNS);
        JTextField limitField = new JTextField();          // 贷款期数（月）
        JTextField loanAmountField = new JTextField();     // 贷款金额（万元）
        JTextField rateField = new JTextField();           // 贷款年利率（%）

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

        // 结果面板（使用带滚动条的文本域）
        JTextArea resultArea = new JTextArea(8, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("计算结果"));

        houseLoanPanel.add(inputPanel, BorderLayout.NORTH);
        houseLoanPanel.add(scrollPane, BorderLayout.CENTER);

        add(houseLoanPanel);

        // 计算按钮的事件监听
        calculateButton.addActionListener(e -> {
            try {
                int selectedIndex = patternCombo.getSelectedIndex();      // 0:等额本息, 1:等额本金
                int totalMonth = Integer.parseInt(limitField.getText().trim()); // 总月数
                // 将输入的万元转换为元
                double principal = Double.parseDouble(loanAmountField.getText().trim()) * 1e4;
                // 将年利率百分比转换为月利率（如4.9% -> 0.049/12）
                double rate = Double.parseDouble(rateField.getText().trim()) / 1200;

                if (selectedIndex == 0) {
                    // 等额本息
                    double result = Calculators.interest(principal, rate, totalMonth);
                    String outText = formatEqualInstallment(result, totalMonth, principal);
                    resultArea.setText(outText);
                } else if (selectedIndex == 1) {
                    // 等额本金
                    String outText = formatEqualPrincipal(principal, rate, totalMonth);
                    resultArea.setText(outText);
                }
            } catch (NumberFormatException ex) {
                resultArea.setText("错误：请输入有效的数字！");
            } catch (IllegalArgumentException ex) {
                resultArea.setText("错误：" + ex.getMessage());
            }
        });
    }

    /**
     * 格式化等额本金还款方式的输出文本。
     * 计算还款总额、首月还款、末月还款、每月递减额及总利息。
     *
     * @param principal  贷款本金（元）
     * @param rate       月利率
     * @param totalMonth 还款总月数
     * @return 格式化的结果字符串
     */
    private static String formatEqualPrincipal(double principal, double rate, int totalMonth) {
        double[] result = Calculators.capital(principal, rate, totalMonth);
        // 等额本金还款总额为等差数列和： (首月+末月) * 月数 / 2
        double totalAmount = (result[0] + result[result.length - 1]) * result.length / 2;
        double firstMonth = result[0];
        double degressive = result[0] - result[1];          // 每月递减额
        double lastMonth = result[result.length - 1];
        // 总利息公式：本金 × 月利率 × (还款月数 + 1) / 2
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
                principal / 1e4, totalMonth, totalMonth / 12.0,
                totalAmount, firstMonth, lastMonth, degressive, totalInterest);
    }

    /**
     * 格式化等额本息还款方式的输出文本。
     * 计算还款总额和总利息。
     *
     * @param monthlyPayment 每月还款额（元）
     * @param totalMonth     还款总月数
     * @param principal      贷款本金（元）
     * @return 格式化的结果字符串
     */
    private static String formatEqualInstallment(double monthlyPayment, int totalMonth, double principal) {
        double totalAmount = monthlyPayment * totalMonth;          // 还款总额
        double totalInterest = totalAmount - principal;            // 总利息

        return String.format("""
                        贷款金额：%.2f 万元
                        贷款期数：%d 期 （约 %.1f 年）
                        
                        还款总额：%.2f 元
                        每月还款：%.2f 元
                        总利息：%.2f 元
                        """,
                principal / 1e4, totalMonth, totalMonth / 12.0,
                totalAmount, monthlyPayment, totalInterest);
    }

    /**
     * 程序入口，在事件调度线程中启动图形界面。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(HouseLoanGUI::new);
    }
}