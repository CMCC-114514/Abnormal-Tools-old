package kk3twt.abnormal.tools.calculators.probability;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * 概率计算器图形界面。
 * 提供四种概率分布（二项分布、超几何分布、泊松分布、几何分布）的计算。
 * 通过下拉框切换分布类型，输入相应参数后点击“计算”按钮显示结果。
 */
public class ProbabilityGUI extends JFrame {

    /** 显示计算结果的文本域（不可编辑） */
    private final JTextArea resultArea = new JTextArea(8, 30);

    // 不同分布模型对应的输入面板
    private JPanel binomialPanel;
    private JPanel hypergeometryPanel;
    private JPanel poissonPanel;
    private JPanel geometryPanel;
    private JPanel uniformPanel;
    private JPanel exponentialPanel;
    private JPanel normalPanel;

    /**
     * 构造函数，初始化窗口并构建界面。
     */
    public ProbabilityGUI() {
        setTitle("概率计算");
        setSize(400, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        // 创建各个分布模型的输入面板
        setBinomialPanel();
        setHypergeometryPanel();
        setPoissonPanel();
        setGeometryPanel();
        setUniformPanel();
        setExponentialPanel();
        setNormalPanel();
        JPanel[] panels = {
                binomialPanel, hypergeometryPanel, poissonPanel, geometryPanel, 
                uniformPanel, exponentialPanel, normalPanel
        };
        String[] probabilityModels = {
                "二项分布", "超几何分布", "泊松分布", "几何分布", "均匀分布", "指数分布", "正态分布"
        };

        // 设置结果显示区域
        resultArea.setEditable(false);
        resultArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("计算结果"));

        // 创建标签页，选择概率模型
        JTabbedPane tabbedPane = new JTabbedPane();
        for (int i = 0; i < panels.length; i++) {
            panels[i].setBorder(new TitledBorder("输入参数"));
            tabbedPane.add(probabilityModels[i], panels[i]);
        }

        // 将各组件添加到主面板
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    /**
     * 创建二项分布输入面板。
     * 包含实验次数 n、事件发生概率 p、随机变量取值 x 的输入框和计算按钮。
     */
    private void setBinomialPanel() {
        binomialPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField nField = new JTextField();               // 实验次数
        JTextField probabilityField = new JTextField();     // 发生概率
        JTextField rvField = new JTextField();              // 随机变量取值
        JButton calculateButton = new JButton("计算");

        binomialPanel.add(new JLabel("实验次数n："));
        binomialPanel.add(nField);
        binomialPanel.add(new JLabel("事件发生概率p："));
        binomialPanel.add(probabilityField);
        binomialPanel.add(new JLabel("随机变量X取值："));
        binomialPanel.add(rvField);
        binomialPanel.add(new JLabel());
        binomialPanel.add(calculateButton);

        // 计算按钮事件：计算 P(X = x) 和 P(X <= x)
        calculateButton.addActionListener(e -> {
            try {
                int n = Integer.parseInt(nField.getText().trim());
                double p = Double.parseDouble(probabilityField.getText().trim());
                int x = Integer.parseInt(rvField.getText().trim());

                double[] result = Calculators.binomial(n, p);
                double sum = 0;
                for (int i = 0; i < x + 1; i++) {
                    sum += result[i];
                }

                resultArea.setText(String.format("""
                        二项分布  X ~ B（%d, %.2f）
                        P（X = %d）= %.3f
                        P（X <= %d） = %.3f
                        """, n, p, x, result[x], x, sum));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, String.format("""
                    错误：请输入正确的数字
                    error：%s
                    """, ex.getMessage()), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * 创建超几何分布输入面板。
     * 包含物件总数 N、指定种类物件个数 M、抽取物件数 n、随机变量取值 x 的输入框和计算按钮。
     */
    private void setHypergeometryPanel() {
        hypergeometryPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        JTextField NField = new JTextField();               // 物件总数N
        JTextField MField = new JTextField();               // 指定种类物件个数M
        JTextField nField = new JTextField();               // 抽取物件数n
        JTextField rvField = new JTextField();              // 随机变量取值
        JButton calculateButton = new JButton("计算");

        hypergeometryPanel.add(new JLabel("物件总数N："));
        hypergeometryPanel.add(NField);
        hypergeometryPanel.add(new JLabel("指定种类物件个数M："));
        hypergeometryPanel.add(MField);
        hypergeometryPanel.add(new JLabel("抽取物件数n："));
        hypergeometryPanel.add(nField);
        hypergeometryPanel.add(new JLabel("随机变量X取值："));
        hypergeometryPanel.add(rvField);
        hypergeometryPanel.add(new JLabel());
        hypergeometryPanel.add(calculateButton);

        // 计算按钮事件：计算 P(X = x) 和 P(X <= x)
        calculateButton.addActionListener(e -> {
            try {
                int n = Integer.parseInt(nField.getText().trim());
                int M = Integer.parseInt(MField.getText().trim());
                int N = Integer.parseInt(NField.getText().trim());
                int x = Integer.parseInt(rvField.getText().trim());

                double[] result = Calculators.hypergeometry(n, M, N);
                double sum = 0;
                for (int i = 0; i < x + 1; i++) {
                    sum += result[i];
                }

                resultArea.setText(String.format("""
                        超几何分布  X ~ H（%d, %d, %d）
                        P（X = %d）= %.3f
                        P（X <= %d） = %.3f
                        """, n, M, N, x, result[x], x, sum));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, String.format("""
                    错误：请输入正确的数字
                    error：%s
                    """, ex.getMessage()), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * 创建泊松分布输入面板。
     * 包含参数 lambda、随机变量取值 k 的输入框和计算按钮。
     */
    private void setPoissonPanel() {
        poissonPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField lambdaField = new JTextField();          // 参数lambda
        JTextField rvField = new JTextField();              // 随机变量取值
        JButton calculateButton = new JButton("计算");

        poissonPanel.add(new JLabel("参数lambda："));
        poissonPanel.add(lambdaField);
        poissonPanel.add(new JLabel("随机变量X取值："));
        poissonPanel.add(rvField);
        poissonPanel.add(new JLabel());
        poissonPanel.add(calculateButton);

        // 计算按钮事件：计算 P(X = k)
        calculateButton.addActionListener(e -> {
            try {
                double lambda = Double.parseDouble(lambdaField.getText().trim());
                int k = Integer.parseInt(rvField.getText().trim());

                double result = Calculators.poisson(k, lambda);

                resultArea.setText(String.format("""
                        泊松分布  X ~ P（%.2f）
                        P（X <= %d）= %.4f
                        """, lambda, k, result));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, String.format("""
                    错误：请输入正确的数字
                    error：%s
                    """, ex.getMessage()), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * 创建几何分布输入面板。
     * 包含事件发生概率 p、随机变量取值 k 的输入框和计算按钮。
     */
    private void setGeometryPanel() {
        geometryPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField probabilityField = new JTextField();         // 发生概率
        JTextField rvField = new JTextField();                  // 随机变量取值
        JButton calculateButton = new JButton("计算");

        geometryPanel.add(new JLabel("事件发生概率p："));
        geometryPanel.add(probabilityField);
        geometryPanel.add(new JLabel("随机变量X取值："));
        geometryPanel.add(rvField);
        geometryPanel.add(new JLabel());
        geometryPanel.add(calculateButton);

        // 计算按钮事件：计算 P(X = k)
        calculateButton.addActionListener(e -> {
            try {
                double p = Double.parseDouble(probabilityField.getText().trim());
                int k = Integer.parseInt(rvField.getText().trim());

                double result = Calculators.geometry(k, p);

                resultArea.setText(String.format("""
                        几何分布  X ~ G（%.2f）
                        P（X = %d）= %.3f
                        """, p, k, result));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, String.format("""
                    错误：请输入正确的数字
                    error：%s
                    """, ex.getMessage()), "错误", JOptionPane.ERROR_MESSAGE);

            }
        });
    }
    
    /**
     * 创建平均分布输入面板
     * 包含分布上下界a, b、随机变量取值 x 的输入框和计算按钮
     */
    private void setUniformPanel() {
        uniformPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField boundsField = new JTextField();
        JTextField rvField = new JTextField();
        JButton calculateButton = new JButton("计算");

        uniformPanel.add(new JLabel("下界a与上界b（用逗号分开）："));
        uniformPanel.add(boundsField);
        uniformPanel.add(new JLabel("随机变量X取值："));
        uniformPanel.add(rvField);
        uniformPanel.add(new JLabel());
        uniformPanel.add(calculateButton);

        // 计算按钮事件：计算 P(X <= x)
        calculateButton.addActionListener(e -> {
            try {
                String[] bounds = boundsField.getText().trim().split(",");
                double a = Double.parseDouble(bounds[0].trim());
                double b = Double.parseDouble(bounds[1].trim());
                double x = Double.parseDouble(rvField.getText().trim());

                if (a > b) {
                    double t = b;
                    b = a;
                    a = t;
                }

                double result = Calculators.uniform(a, b, x);

                resultArea.setText(String.format("""
                        均匀分布  X ~ U（%.2f, %.2f）
                        P（X <= %.2f）= %.3f
                        """, a, b, x, result));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, String.format("""
                    错误：请输入正确的数字
                    error：%s
                    """, ex.getMessage()), "错误", JOptionPane.ERROR_MESSAGE);

            }
        });
    }

    /**
     * 创建指数分布输入面板
     * 包含参数 lambda 、随机变量取值 x 的输入框和计算按钮
     */
    private void setExponentialPanel() {
        exponentialPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField lambdaField = new JTextField();
        JTextField rvField = new JTextField();
        JButton calculateButton = new JButton("计算");

        exponentialPanel.add(new JLabel("参数lambda："));
        exponentialPanel.add(lambdaField);
        exponentialPanel.add(new JLabel("随机变量X取值："));
        exponentialPanel.add(rvField);
        exponentialPanel.add(new JLabel());
        exponentialPanel.add(calculateButton);

        // 计算按钮事件：计算 P(X <= x)
        calculateButton.addActionListener(e -> {
            try {
                double lambda = Double.parseDouble(lambdaField.getText().trim());
                double x = Double.parseDouble(rvField.getText().trim());

                double result = Calculators.exponential(lambda, x);

                resultArea.setText(String.format("""
                        指数分布 X ~ E（%.2f）
                        P（X <= %.2f） = %.4f
                        """, lambda, x, result));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, String.format("""
                    错误：请输入正确的数字
                    error：%s
                    """, ex.getMessage()), "错误", JOptionPane.ERROR_MESSAGE);

            }
        });
    }

    private void setNormalPanel() {
        normalPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField muField = new JTextField();
        JTextField sigmaField = new JTextField();
        JTextField rvField = new JTextField();
        JButton calculateButton = new JButton("计算");

        normalPanel.add(new JLabel("参数mu："));
        normalPanel.add(muField);
        normalPanel.add(new JLabel("参数sigma："));
        normalPanel.add(sigmaField);
        normalPanel.add(new JLabel("随机变量X取值："));
        normalPanel.add(rvField);
        normalPanel.add(new JLabel());
        normalPanel.add(calculateButton);

        calculateButton.addActionListener(e -> {
            try {
                double mu = Double.parseDouble(muField.getText().trim());
                double sigma = Double.parseDouble(sigmaField.getText().trim());
                double x = Double.parseDouble(rvField.getText().trim());

                if (sigma <= 0) {
                    throw new Exception("sigma的值必须大于零！");
                }

                NormalDistribution normal = new NormalDistribution(mu, sigma);
                double result = normal.calculate(x);

                resultArea.setText(String.format("""
                        正态分布 X ~ N（%.2f, %.2f^2）
                        P（X <= %.2f） = %.4f
                        """, mu, sigma, x, result));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, String.format("""
                    错误：请输入正确的数字
                    error：%s
                    """, ex.getMessage()), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * 程序入口，在事件调度线程中启动图形界面。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProbabilityGUI::new);
    }
}