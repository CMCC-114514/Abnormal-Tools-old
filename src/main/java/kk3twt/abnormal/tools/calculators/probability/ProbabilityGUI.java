package kk3twt.abnormal.tools.calculators.probability;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ProbabilityGUI extends JFrame {

    private final JTextArea resultArea = new JTextArea(8, 30);

    private JPanel binomialPanel;
    private JPanel hypergeometryPanel;
    private JPanel poissonPanel;
    private JPanel geometryPanel;

    public ProbabilityGUI() {
        setTitle("概率计算");
        setSize(400, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 创建面板
        setBinomialPanel();
        setHypergeometryPanel();
        setPoissonPanel();
        setGeometryPanel();
        JPanel[] panels = {
                binomialPanel, hypergeometryPanel, poissonPanel, geometryPanel
        };
        int[] panelIndex = {0, -1};

        // 创建结果显示区域
        resultArea.setEditable(false);
        resultArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("计算结果"));

        // 创建下拉框，后续根据下拉框的选项更改输入界面
        JPanel boxPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        String[] probabilityModels = {
                "二项分布", "超几何分布", "泊松分布", "几何分布"
        };
        JComboBox<String> modelBox = new JComboBox<>(probabilityModels);
        modelBox.addActionListener(e -> {
            if (panelIndex[0] != modelBox.getSelectedIndex()) {
                panelIndex[1] = modelBox.getSelectedIndex();
                mainPanel.add(panels[panelIndex[1]], BorderLayout.CENTER);
                mainPanel.remove(panels[panelIndex[0]]);
                mainPanel.revalidate();
                mainPanel.repaint();
                panelIndex[0] = panelIndex[1];
            }
        });
        boxPanel.add(new JLabel("概率模型："));
        boxPanel.add(modelBox);

        mainPanel.add(boxPanel, BorderLayout.NORTH);
        mainPanel.add(binomialPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // 二项分布
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

    // 超几何分布
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

    // 泊松分布
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

    // 几何分布
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProbabilityGUI::new);
    }
}
