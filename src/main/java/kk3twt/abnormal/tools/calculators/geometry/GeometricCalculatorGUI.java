package kk3twt.abnormal.tools.calculators.geometry;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class GeometricCalculatorGUI extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;

    // 各个计算面板
    private JPanel rectanglePanel;
    private JPanel circlePanel;
    private JPanel trianglePanel;
    private JPanel parallelogramPanel;
    private JPanel trapezoidPanel;
    private JPanel conePanel;
    private JPanel spherePanel;
    private JPanel cuboidPanel;
    private JPanel cylinderPanel;

    public GeometricCalculatorGUI() {
        setTitle("几何计算器");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 400);
        setLocationRelativeTo(null);

        initComponents();
        setupLayout();

        setContentPane(mainPanel);
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();

        // 创建各个功能面板
        createRectanglePanel();
        createCirclePanel();
        createTrianglePanel();
        createParallelogramPanel();
        createTrapezoidPanel();
        createConePanel();
        createSpherePanel();
        createCuboidPanel();
        createCylinderPanel();

        // 添加标签页
        tabbedPane.addTab("矩形", rectanglePanel);
        tabbedPane.addTab("圆形", circlePanel);
        tabbedPane.addTab("三角形", trianglePanel);
        tabbedPane.addTab("平行四边形", parallelogramPanel);
        tabbedPane.addTab("梯形", trapezoidPanel);
        tabbedPane.addTab("圆锥体", conePanel);
        tabbedPane.addTab("球体", spherePanel);
        tabbedPane.addTab("长方体", cuboidPanel);
        tabbedPane.addTab("圆柱体", cylinderPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // 添加状态栏
        JLabel infoLabel = new JLabel(
                "<html><center>输入数值进行计算，所有的输入都必须为整数</center></html>",
                SwingConstants.CENTER
        );
        infoLabel.setFont(new Font("宋体", Font.PLAIN, 12));
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(infoLabel, BorderLayout.SOUTH);
    }

    private void createRectanglePanel() {
        rectanglePanel = new JPanel(new BorderLayout(10, 10));
        rectanglePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入矩形参数"));

        JTextField lengthField = new JTextField();
        JTextField widthField = new JTextField();

        inputPanel.add(new JLabel("长度:"));
        inputPanel.add(lengthField);
        inputPanel.add(new JLabel("宽度:"));
        inputPanel.add(widthField);

        JButton calculateButton = new JButton("计算");
        inputPanel.add(new JLabel(""));
        inputPanel.add(calculateButton);

        // 结果面板
        JTextArea resultArea = new JTextArea(8, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("计算结果"));

        rectanglePanel.add(inputPanel, BorderLayout.NORTH);
        rectanglePanel.add(scrollPane, BorderLayout.CENTER);

        // 事件处理
        calculateButton.addActionListener(e -> {
            try {
                double length = Double.parseDouble(lengthField.getText().trim());
                double width = Double.parseDouble(widthField.getText().trim());

                double[] results = Calculators.rectangle(length, width);

                resultArea.setText(String.format(
                        """
                                矩形参数:
                                长度 = %.2f
                                宽度 = %.2f
                                
                                计算结果:
                                面积 = %.4f
                                周长 = %.4f
                                对角线长度 = %.4f""",
                        length, width, results[0], results[1], results[2]
                ));
            } catch (NumberFormatException ex) {
                resultArea.setText("错误：请输入有效的数字！");
            } catch (IllegalArgumentException ex) {
                resultArea.setText("错误：" + ex.getMessage());
            }
        });
    }

    private void createCirclePanel() {
        circlePanel = new JPanel(new BorderLayout(10, 10));
        circlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入圆形参数"));

        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"已知半径", "已知周长", "已知面积"});
        JTextField valueField = new JTextField();

        inputPanel.add(new JLabel("已知条件:"));
        inputPanel.add(typeCombo);
        inputPanel.add(new JLabel("数值:"));
        inputPanel.add(valueField);

        JButton calculateButton = new JButton("计算");
        inputPanel.add(new JLabel(""));
        inputPanel.add(calculateButton);

        // 结果面板
        JTextArea resultArea = new JTextArea(8, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("计算结果"));

        circlePanel.add(inputPanel, BorderLayout.NORTH);
        circlePanel.add(scrollPane, BorderLayout.CENTER);

        // 事件处理
        calculateButton.addActionListener(e -> {
            try {
                byte choice = (byte)(typeCombo.getSelectedIndex() + 1);
                double value = Double.parseDouble(valueField.getText().trim());

                double[] results = Calculators.circle(choice, value);

                String knownWhat = "";
                switch (choice) {
                    case 1 -> knownWhat = String.format("半径 = %.2f", value);
                    case 2 -> knownWhat = String.format("周长 = %.2f", value);
                    case 3 -> knownWhat = String.format("面积 = %.2f", value);
                }

                resultArea.setText(String.format(
                        """
                                已知条件:
                                %s
                                
                                计算结果:
                                半径 = %.4f
                                周长 = %.4f
                                面积 = %.4f""",
                        knownWhat, results[0], results[1], results[2]
                ));
            } catch (NumberFormatException ex) {
                resultArea.setText("错误：请输入有效的数字！");
            } catch (IllegalArgumentException ex) {
                resultArea.setText("错误：" + ex.getMessage());
            }
        });
    }

    private void createTrianglePanel() {
        trianglePanel = new JPanel(new BorderLayout(10, 10));
        trianglePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入三角形参数"));

        JTextField baseField = new JTextField();
        JTextField heightField = new JTextField();

        inputPanel.add(new JLabel("底边长:"));
        inputPanel.add(baseField);
        inputPanel.add(new JLabel("高:"));
        inputPanel.add(heightField);

        JButton calculateButton = new JButton("计算");
        inputPanel.add(new JLabel(""));
        inputPanel.add(calculateButton);

        // 结果面板
        JTextArea resultArea = new JTextArea(6, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("计算结果"));

        trianglePanel.add(inputPanel, BorderLayout.NORTH);
        trianglePanel.add(scrollPane, BorderLayout.CENTER);

        // 事件处理
        calculateButton.addActionListener(e -> {
            try {
                double base = Double.parseDouble(baseField.getText().trim());
                double height = Double.parseDouble(heightField.getText().trim());

                double area = Calculators.triangle(base, height);

                resultArea.setText(String.format(
                        "三角形参数:\n底边长 = %.2f\n高 = %.2f\n\n计算结果:\n面积 = %.4f",
                        base, height, area
                ));
            } catch (NumberFormatException ex) {
                resultArea.setText("错误：请输入有效的数字！");
            } catch (IllegalArgumentException ex) {
                resultArea.setText("错误：" + ex.getMessage());
            }
        });
    }

    private void createParallelogramPanel() {
        parallelogramPanel = new JPanel(new BorderLayout(10, 10));
        parallelogramPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入平行四边形参数"));

        JTextField baseField = new JTextField();
        JTextField heightField = new JTextField();

        inputPanel.add(new JLabel("底边长:"));
        inputPanel.add(baseField);
        inputPanel.add(new JLabel("高:"));
        inputPanel.add(heightField);

        JButton calculateButton = new JButton("计算");
        inputPanel.add(new JLabel(""));
        inputPanel.add(calculateButton);

        // 结果面板
        JTextArea resultArea = new JTextArea(6, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("计算结果"));

        parallelogramPanel.add(inputPanel, BorderLayout.NORTH);
        parallelogramPanel.add(scrollPane, BorderLayout.CENTER);

        // 事件处理
        calculateButton.addActionListener(e -> {
            try {
                double base = Double.parseDouble(baseField.getText().trim());
                double height = Double.parseDouble(heightField.getText().trim());

                double area = Calculators.rhomboid(base, height);

                resultArea.setText(String.format(
                        "平行四边形参数:\n底边长 = %.2f\n高 = %.2f\n\n计算结果:\n面积 = %.4f",
                        base, height, area
                ));
            } catch (NumberFormatException ex) {
                resultArea.setText("错误：请输入有效的数字！");
            } catch (IllegalArgumentException ex) {
                resultArea.setText("错误：" + ex.getMessage());
            }
        });
    }

    private void createTrapezoidPanel() {
        trapezoidPanel = new JPanel(new BorderLayout(10, 10));
        trapezoidPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入梯形参数"));

        JTextField topField = new JTextField();
        JTextField bottomField = new JTextField();
        JTextField heightField = new JTextField();

        inputPanel.add(new JLabel("上底:"));
        inputPanel.add(topField);
        inputPanel.add(new JLabel("下底:"));
        inputPanel.add(bottomField);
        inputPanel.add(new JLabel("高:"));
        inputPanel.add(heightField);

        JButton calculateButton = new JButton("计算");
        inputPanel.add(new JLabel(""));
        inputPanel.add(calculateButton);

        // 结果面板
        JTextArea resultArea = new JTextArea(6, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("计算结果"));

        trapezoidPanel.add(inputPanel, BorderLayout.NORTH);
        trapezoidPanel.add(scrollPane, BorderLayout.CENTER);

        // 事件处理
        calculateButton.addActionListener(e -> {
            try {
                double top = Double.parseDouble(topField.getText().trim());
                double bottom = Double.parseDouble(bottomField.getText().trim());
                double height = Double.parseDouble(heightField.getText().trim());

                double area = Calculators.trapezoid(top, bottom, height);

                resultArea.setText(String.format(
                        "梯形参数:\n上底 = %.2f\n下底 = %.2f\n高 = %.2f\n\n计算结果:\n面积 = %.4f",
                        top, bottom, height, area
                ));
            } catch (NumberFormatException ex) {
                resultArea.setText("错误：请输入有效的数字！");
            } catch (IllegalArgumentException ex) {
                resultArea.setText("错误：" + ex.getMessage());
            }
        });
    }

    private void createConePanel() {
        conePanel = new JPanel(new BorderLayout(10, 10));
        conePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入圆锥体参数"));

        JTextField radiusField = new JTextField();
        JTextField heightField = new JTextField();

        inputPanel.add(new JLabel("底面半径:"));
        inputPanel.add(radiusField);
        inputPanel.add(new JLabel("高:"));
        inputPanel.add(heightField);

        JButton calculateButton = new JButton("计算");
        inputPanel.add(new JLabel(""));
        inputPanel.add(calculateButton);

        // 结果面板
        JTextArea resultArea = new JTextArea(8, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("计算结果"));

        conePanel.add(inputPanel, BorderLayout.NORTH);
        conePanel.add(scrollPane, BorderLayout.CENTER);

        // 事件处理
        calculateButton.addActionListener(e -> {
            try {
                double radius = Double.parseDouble(radiusField.getText().trim());
                double height = Double.parseDouble(heightField.getText().trim());

                double[] results = Calculators.cone(radius, height);

                resultArea.setText(String.format(
                        """
                                圆锥体参数:
                                底面半径 = %.2f
                                高 = %.2f
                                
                                计算结果:
                                表面积 = %.4f
                                体积 = %.4f""",
                        radius, height, results[0], results[1]
                ));
            } catch (NumberFormatException ex) {
                resultArea.setText("错误：请输入有效的数字！");
            } catch (IllegalArgumentException ex) {
                resultArea.setText("错误：" + ex.getMessage());
            }
        });
    }

    private void createSpherePanel() {
        spherePanel = new JPanel(new BorderLayout(10, 10));
        spherePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入球体参数"));

        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"已知半径", "已知表面积", "已知体积"});
        JTextField valueField = new JTextField();

        inputPanel.add(new JLabel("已知条件:"));
        inputPanel.add(typeCombo);
        inputPanel.add(new JLabel("数值:"));
        inputPanel.add(valueField);

        JButton calculateButton = new JButton("计算");
        inputPanel.add(new JLabel(""));
        inputPanel.add(calculateButton);

        // 结果面板
        JTextArea resultArea = new JTextArea(8, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("计算结果"));

        spherePanel.add(inputPanel, BorderLayout.NORTH);
        spherePanel.add(scrollPane, BorderLayout.CENTER);

        // 事件处理
        calculateButton.addActionListener(e -> {
            try {
                byte choice = (byte)(typeCombo.getSelectedIndex() + 1);
                double value = Double.parseDouble(valueField.getText().trim());

                double[] results = Calculators.sphere(choice, value);

                String knownWhat = "";
                switch (choice) {
                    case 1 -> knownWhat = String.format("半径 = %.2f", value);
                    case 2 -> knownWhat = String.format("表面积 = %.2f", value);
                    case 3 -> knownWhat = String.format("体积 = %.2f", value);
                }

                resultArea.setText(String.format(
                        """
                                已知条件:
                                %s
                                
                                计算结果:
                                半径 = %.4f
                                表面积 = %.4f
                                体积 = %.4f""",
                        knownWhat, results[0], results[1], results[2]
                ));
            } catch (NumberFormatException ex) {
                resultArea.setText("错误：请输入有效的数字！");
            } catch (IllegalArgumentException ex) {
                resultArea.setText("错误：" + ex.getMessage());
            }
        });
    }

    private void createCuboidPanel() {
        cuboidPanel = new JPanel(new BorderLayout(10, 10));
        cuboidPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入长方体参数"));

        JTextField lengthField = new JTextField();
        JTextField widthField = new JTextField();
        JTextField heightField = new JTextField();

        inputPanel.add(new JLabel("长:"));
        inputPanel.add(lengthField);
        inputPanel.add(new JLabel("宽:"));
        inputPanel.add(widthField);
        inputPanel.add(new JLabel("高:"));
        inputPanel.add(heightField);

        JButton calculateButton = new JButton("计算");
        inputPanel.add(new JLabel(""));
        inputPanel.add(calculateButton);

        // 结果面板
        JTextArea resultArea = new JTextArea(8, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("计算结果"));

        cuboidPanel.add(inputPanel, BorderLayout.NORTH);
        cuboidPanel.add(scrollPane, BorderLayout.CENTER);

        // 事件处理
        calculateButton.addActionListener(e -> {
            try {
                double length = Double.parseDouble(lengthField.getText().trim());
                double width = Double.parseDouble(widthField.getText().trim());
                double height = Double.parseDouble(heightField.getText().trim());

                double[] results = Calculators.cuboid(length, width, height);

                resultArea.setText(String.format(
                        """
                                长方体参数:
                                长 = %.2f
                                宽 = %.2f
                                高 = %.2f
                                
                                计算结果:
                                对角线长度 = %.4f
                                表面积 = %.4f
                                体积 = %.4f""",
                        length, width, height, results[0], results[1], results[2]
                ));
            } catch (NumberFormatException ex) {
                resultArea.setText("错误：请输入有效的数字！");
            } catch (IllegalArgumentException ex) {
                resultArea.setText("错误：" + ex.getMessage());
            }
        });
    }

    private void createCylinderPanel() {
        cylinderPanel = new JPanel(new BorderLayout(10, 10));
        cylinderPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入圆柱体参数"));

        JTextField radiusField = new JTextField();
        JTextField heightField = new JTextField();

        inputPanel.add(new JLabel("底面半径:"));
        inputPanel.add(radiusField);
        inputPanel.add(new JLabel("高:"));
        inputPanel.add(heightField);

        JButton calculateButton = new JButton("计算");
        inputPanel.add(new JLabel(""));
        inputPanel.add(calculateButton);

        // 结果面板
        JTextArea resultArea = new JTextArea(8, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("计算结果"));

        cylinderPanel.add(inputPanel, BorderLayout.NORTH);
        cylinderPanel.add(scrollPane, BorderLayout.CENTER);

        // 事件处理
        calculateButton.addActionListener(e -> {
            try {
                double radius = Double.parseDouble(radiusField.getText().trim());
                double height = Double.parseDouble(heightField.getText().trim());

                double[] results = Calculators.cylinder(radius, height);

                resultArea.setText(String.format(
                        """
                                圆柱体参数:
                                底面半径 = %.2f
                                高 = %.2f
                                
                                计算结果:
                                表面积 = %.4f
                                体积 = %.4f""",
                        radius, height, results[0], results[1]
                ));
            } catch (NumberFormatException ex) {
                resultArea.setText("错误：请输入有效的数字！");
            } catch (IllegalArgumentException ex) {
                resultArea.setText("错误：" + ex.getMessage());
            }
        });
    }

    private void setupLayout() {
        // 设置统一的字体
        // Font titleFont = new Font("微软雅黑", Font.BOLD, 16);
        Font labelFont = new Font("宋体", Font.PLAIN, 14);
        Font buttonFont = new Font("微软雅黑", Font.PLAIN, 14);

        // 设置所有组件的字体
        Component[] components = tabbedPane.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                setComponentFont((JPanel) comp, labelFont, buttonFont);
            }
        }
    }

    private void setComponentFont(JPanel panel, Font labelFont, Font buttonFont) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JLabel) {
                comp.setFont(labelFont);
            } else if (comp instanceof JButton) {
                comp.setFont(buttonFont);
            } else if (comp instanceof JComboBox) {
                comp.setFont(labelFont);
            } else if (comp instanceof JTextField) {
                comp.setFont(labelFont);
            } else if (comp instanceof JTextArea) {
                comp.setFont(labelFont);
            } else if (comp instanceof JPanel) {
                setComponentFont((JPanel) comp, labelFont, buttonFont);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GeometricCalculatorGUI gui = new GeometricCalculatorGUI();
            gui.setVisible(true);
        });
    }
}