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

    // 四种分布对应的输入面板
    private JPanel binomialPanel;
    private JPanel hypergeometryPanel;
    private JPanel poissonPanel;
    private JPanel geometryPanel;

    /**
     * 构造函数，初始化窗口并构建界面。
     */
    public ProbabilityGUI() {
        setTitle("概率计算");
        setSize(400, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 创建四个分布的面板
        setBinomialPanel();
        setHypergeometryPanel();
        setPoissonPanel();
        setGeometryPanel();
        JPanel[] panels = {
                binomialPanel, hypergeometryPanel, poissonPanel, geometryPanel
        };
        int[] panelIndex = {0, -1}; // 用于记录当前显示的面板索引和下一个索引

        // 设置结果显示区域
        resultArea.setEditable(false);
        resultArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("计算结果"));

        // 创建下拉框，用于选择分布类型
        JPanel boxPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JComboBox<String> modelBox = getModelBox(panelIndex, mainPanel, panels);
        boxPanel.add(new JLabel("概率模型："));
        boxPanel.add(modelBox);

        // 将各组件添加到主面板
        mainPanel.add(boxPanel, BorderLayout.NORTH);
        mainPanel.add(binomialPanel, BorderLayout.CENTER); // 默认显示二项分布面板
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * 从构造函数中提取出来的方法
     * 用于创建概率模型下拉框
     * 输入面板会随着下拉框的值而改变
     *
     * @param panelIndex 下拉框主体
     * @param mainPanel 下拉框的父组件（主面板）
     * @param panels 输入面板
     * @return 概率模型下拉框
     */
    private static JComboBox<String> getModelBox(int[] panelIndex, JPanel mainPanel, JPanel[] panels) {
        String[] probabilityModels = {
                "二项分布", "超几何分布", "泊松分布", "几何分布"
        };
        JComboBox<String> modelBox = new JComboBox<>(probabilityModels);
        modelBox.addActionListener(e -> {
            // 当下拉框选项改变时，切换中央面板显示的输入面板
            if (panelIndex[0] != modelBox.getSelectedIndex()) {
                panelIndex[1] = modelBox.getSelectedIndex();
                mainPanel.add(panels[panelIndex[1]], BorderLayout.CENTER);
                mainPanel.remove(panels[panelIndex[0]]);
                mainPanel.revalidate();
                mainPanel.repaint();
                panelIndex[0] = panelIndex[1];
            }
        });
        return modelBox;
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
            int lambda = Integer.parseInt(lambdaField.getText().trim());
            int k = Integer.parseInt(rvField.getText().trim());

            double result = Calculators.poisson(k, lambda);

            resultArea.setText(String.format("""
                    泊松分布  X ~ P（%d）
                    P（X = %d）= %.3f
                    """, lambda, k, result));
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
            double p = Double.parseDouble(probabilityField.getText().trim());
            int k = Integer.parseInt(rvField.getText().trim());

            double result = Calculators.geometry(k, p);

            resultArea.setText(String.format("""
                    几何分布  X ~ G（%.2f）
                    P（X = %d）= %.3f
                    """, p, k, result));
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