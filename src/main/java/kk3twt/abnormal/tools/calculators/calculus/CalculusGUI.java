package kk3twt.abnormal.tools.calculators.calculus;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * 微积分图形界面，目前提供定积分计算功能。
 * <p>
 * 支持四种函数类型：
 * <ul>
 *     <li>一次函数 y = m*x + n</li>
 *     <li>二次函数 y = a*x² + b*x + c</li>
 *     <li>三角函数 y = A*sin(ω*x + φ) 或 A*cos(ω*x + φ)</li>
 *     <li>指数/对数函数 y = C*e^(m*x + n) + k 或 C*ln(m*x + n) + k</li>
 * </ul>
 * 用户可选择函数类型、输入参数、积分区间和精度，计算定积分结果。
 */
public class CalculusGUI extends JFrame {

    private JPanel[] funcPanels;
    private final JComboBox<String> functionType;
    private final JTextArea resultArea;
    private final JButton calculateButton = new JButton("计算");
    private final JCheckBox isCos = new JCheckBox("余弦函数");
    private final JCheckBox isLn = new JCheckBox("对数函数");
    private final JTextField rangeField = new JTextField();
    private final JTextField tolField = new JTextField();

    /**
     * 构造函数，初始化窗口并构建界面。
     * 创建主面板，添加各个组件，并为下拉框添加切换面板的监听器。
     */
    public CalculusGUI() {
        setTitle("定积分计算");
        setSize(450, 425);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 计算类型（微分或积分）和函数类型
        JPanel typePanel = new JPanel(new GridLayout(1, 2, 10, 10));
        functionType = new JComboBox<>(Integral.FUNCTION_TYPE);
        typePanel.add(new JLabel("选择函数类型："));
        typePanel.add(functionType);
        mainPanel.add(typePanel, BorderLayout.NORTH);

        // 配置输入面板（精度、积分区间、函数参数）
        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        JPanel rangeAndTolPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        rangeAndTolPanel.add(new JLabel("精度（10^-x）："));
        rangeAndTolPanel.add(tolField);
        rangeAndTolPanel.add(new JLabel("积分区间："));
        rangeAndTolPanel.add(rangeField);
        inputPanel.add(rangeAndTolPanel);
        setFuncPanels();
        inputPanel.add(funcPanels[0]);
        mainPanel.add(inputPanel);

        // 配置选项面板（三角/对数切换）
        JPanel optionPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        isCos.setEnabled(false);
        optionPanel.add(isCos);
        isLn.setEnabled(false);
        optionPanel.add(isLn);
        mainPanel.add(optionPanel, BorderLayout.WEST);
        mainPanel.add(calculateButton, BorderLayout.EAST);

        // 结果面板（带滚动条的文本域）
        resultArea = new JTextArea(8, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("计算结果"));
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        // 下拉框切换时动态更换函数输入面板，并启用/禁用三角/对数的复选框
        int[] panelIndex = {0, -1};
        functionType.addActionListener(e -> {
            if (panelIndex[0] != functionType.getSelectedIndex()) {
                panelIndex[1] = functionType.getSelectedIndex();
                inputPanel.add(funcPanels[panelIndex[1]], BorderLayout.CENTER);
                inputPanel.remove(funcPanels[panelIndex[0]]);
                inputPanel.revalidate();
                inputPanel.repaint();
                panelIndex[0] = panelIndex[1];
            }

            isCos.setEnabled(panelIndex[1] == 2);
            isLn.setEnabled(panelIndex[1] == 3);
        });

        add(mainPanel);
    }

    /**
     * 构建四个函数类型的输入面板，并为计算按钮添加监听器。
     * <p>
     * 每个面板使用 GridBagLayout 实现灵活的组件布局。
     * 监听器根据当前选择的函数类型，读取对应输入框的值，构建 Integral 对象，
     * 然后进行积分计算并显示结果。
     */
    private void setFuncPanels() {
        // 一次函数输入面板：y = m*x + n
        JPanel linearPanel = new JPanel(new GridBagLayout());
        JTextField mField = new JTextField();
        JTextField nField = new JTextField();
        GridBagConstraints gbc = new GridBagConstraints();
        resetGbc(gbc);
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        linearPanel.add(new JLabel("y="), gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        linearPanel.add(mField, gbc);
        gbc.gridx = 3;
        gbc.gridwidth = 2;
        linearPanel.add(new JLabel("*x+"), gbc);
        gbc.gridx = 5;
        gbc.gridwidth = 1;
        linearPanel.add(nField, gbc);

        // 二次函数输入面板：y = a*x² + b*x + c
        JPanel quadraticPanel = new JPanel(new GridBagLayout());
        JTextField aField = new JTextField();
        JTextField bField = new JTextField();
        JTextField cField = new JTextField();
        resetGbc(gbc);
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        quadraticPanel.add(new JLabel("y="), gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        quadraticPanel.add(aField, gbc);
        gbc.gridx = 3;
        gbc.gridwidth = 2;
        quadraticPanel.add(new JLabel("*x^2+"), gbc);
        gbc.gridx = 5;
        gbc.gridwidth = 1;
        quadraticPanel.add(bField, gbc);
        gbc.gridx = 6;
        gbc.gridwidth = 2;
        quadraticPanel.add(new JLabel("*x+"), gbc);
        gbc.gridx = 8;
        gbc.gridwidth = 1;
        quadraticPanel.add(cField, gbc);

        // 三角函数输入面板：y = A*sin(ω*x + φ) 或 A*cos(ω*x + φ)
        JPanel sinePanel = new JPanel(new GridBagLayout());
        JTextField AField = new JTextField();
        JTextField omegaField = new JTextField();
        JTextField phiField = new JTextField();
        resetGbc(gbc);
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        sinePanel.add(new JLabel("y="), gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        sinePanel.add(AField, gbc);
        gbc.gridx = 3;
        gbc.gridwidth = 2;
        JLabel sineLabel = new JLabel("*sin(");
        sinePanel.add(sineLabel, gbc);
        gbc.gridx = 5;
        gbc.gridwidth = 1;
        sinePanel.add(omegaField, gbc);
        gbc.gridx = 6;
        gbc.gridwidth = 2;
        sinePanel.add(new JLabel("*x+"), gbc);
        gbc.gridx = 8;
        gbc.gridwidth = 1;
        sinePanel.add(phiField, gbc);
        gbc.gridx = 9;
        gbc.gridwidth = 2;
        sinePanel.add(new JLabel(")"), gbc);
        // 切换 sin/cos 时更新标签文本
        isCos.addActionListener(e -> sineLabel.setText(isCos.isSelected() ? "*cos(" : "*sin("));

        // 指数/对数函数输入面板：
        // 指数模式：y = C*e^(m*x + n) + k
        // 对数模式：y = C*ln(m*x + n) + k
        JPanel expPanel = new JPanel(new GridBagLayout());
        JTextField CField = new JTextField();
        JTextField meField = new JTextField();
        JTextField neField = new JTextField();
        JTextField kField = new JTextField();
        resetGbc(gbc);
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        expPanel.add(new JLabel("y="), gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        expPanel.add(CField, gbc);
        gbc.gridx = 3;
        gbc.gridwidth = 2;
        JLabel expLabel = new JLabel("*e^(");
        expPanel.add(expLabel, gbc);
        gbc.gridx = 5;
        gbc.gridwidth = 1;
        expPanel.add(meField, gbc);
        gbc.gridx = 6;
        gbc.gridwidth = 2;
        expPanel.add(new JLabel("*x+"), gbc);
        gbc.gridx = 8;
        gbc.gridwidth = 1;
        expPanel.add(neField, gbc);
        gbc.gridx = 9;
        gbc.gridwidth = 2;
        expPanel.add(new JLabel(")"), gbc);
        gbc.gridx = 11;
        gbc.gridwidth = 2;
        expPanel.add(new JLabel("+"), gbc);
        gbc.gridx = 13;
        gbc.gridwidth = 1;
        expPanel.add(kField, gbc);
        // 切换 e^ / ln 时更新标签文本
        isLn.addActionListener(e -> expLabel.setText(isLn.isSelected() ? "*ln(" : "*e^("));

        funcPanels = new JPanel[]{linearPanel, quadraticPanel, sinePanel, expPanel};

        // 计算按钮的监听器：根据当前选择的函数类型，读取对应输入框的值，
        // 构造 Integral 对象，进行积分计算并显示结果。
        calculateButton.addActionListener(e -> {
            try {
                Integral integral = switch (functionType.getSelectedIndex()) {
                    case 0 -> // 一次函数
                            new Integral(Double.parseDouble(mField.getText().trim()),
                                    Double.parseDouble(nField.getText().trim()));
                    case 1 -> // 二次函数
                            new Integral(Double.parseDouble(aField.getText().trim()),
                                    Double.parseDouble(bField.getText().trim()),
                                    Double.parseDouble(cField.getText().trim()));
                    case 2 -> // 三角函数
                            new Integral(Double.parseDouble(AField.getText().trim()),
                                    Double.parseDouble(omegaField.getText().trim()),
                                    Double.parseDouble(phiField.getText().trim()),
                                    isCos.isSelected());
                    case 3 -> // 指数/对数函数
                            new Integral(Double.parseDouble(CField.getText().trim()),
                                    Double.parseDouble(meField.getText().trim()),
                                    Double.parseDouble(neField.getText().trim()),
                                    Double.parseDouble(kField.getText().trim()),
                                    isLn.isSelected());
                    default -> null;
                };

                double tolerance = Math.pow(10, Double.parseDouble(tolField.getText().trim()));
                int maxDepth = 50;

                // 解析积分区间（格式：a,b）
                if (rangeField.getText().contains("，"))
                    throw new NumberFormatException("请使用英文逗号分隔区间");
                String[] range = rangeField.getText().trim().split(",");
                double a = Double.parseDouble(range[0]);
                double b = Double.parseDouble(range[1]);

                // 对数函数时，检查真数取值范围（m*x + n > 0）
                if (isLn.isSelected()) {
                    double antilogarithm = -Double.parseDouble(neField.getText().trim()) /
                            Double.parseDouble(meField.getText().trim());
                    if (a <= antilogarithm || b <= antilogarithm)
                        throw new NumberFormatException("真数的取值范围必须为正实数");
                }

                if (integral != null) {
                    resultArea.setText(String.format("""
                                    结果：%.4f
                                    积分区间：（%s, %s）
                                    计算精度为10^-%s
                                    """,
                            integral.calculate(a, b, functionType.getSelectedIndex(), tolerance, maxDepth),
                            range[0], range[1], tolField.getText().trim()));
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        String.format("""
                        错误：请输入正确的数字！
                        %s
                        """, ex.getMessage()), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * 重置 GridBagConstraints 的通用设置。
     * <p>
     * 设置 gridy = 0，fill = BOTH，权重为 1.0，使组件可伸缩。
     *
     * @param gbc 要重置的 GridBagConstraints 对象
     */
    private void resetGbc(GridBagConstraints gbc) {
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
    }

    /**
     * 程序入口，在事件分发线程中启动 GUI。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(CalculusGUI::new);
    }
}