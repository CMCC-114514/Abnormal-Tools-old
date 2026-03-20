package kk3twt.abnormal.tools.calculators.unitsConversion;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;

/**
 * 单位换算器图形界面。
 * 提供长度、面积、体积、质量、速度、温度、存储单位、进制和颜色码的换算功能。
 * 使用选项卡切换不同类别，每个类别包含输入数值、选择单位、换算按钮和结果表格。
 */
public class UnitConverterGUI extends JFrame {

    private JPanel mainPanel;
    private JTabbedPane tabbedPane;

    /**
     * 构造函数，初始化窗口并构建界面。
     */
    public UnitConverterGUI() {
        setTitle("单位换算器");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 550);
        setLocationRelativeTo(null);
        setVisible(true);

        initComponents();
        setupLayout();

        setContentPane(mainPanel);
    }

    /**
     * 初始化所有组件：创建主面板、选项卡，并添加各个换算面板。
     */
    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();

        // 添加各种换算的标签页
        tabbedPane.addTab("长度换算", createPanels(Convertors.LENGTH_UNITS));
        tabbedPane.addTab("面积换算", createPanels(Convertors.AREA_UNITS));
        tabbedPane.addTab("体积换算", createPanels(Convertors.VOLUME_UNITS));
        tabbedPane.addTab("质量换算", createPanels(Convertors.MASS_UNITS));
        tabbedPane.addTab("速度换算", createPanels(Convertors.SPEED_UNITS));
        tabbedPane.addTab("温度换算", createPanels(Convertors.TEMPERATURE_UNITS));
        tabbedPane.addTab("存储单位换算", createPanels(Convertors.STORAGE_UNITS));
        tabbedPane.addTab("进制换算", createNumSystemPanel());
        tabbedPane.addTab("颜色码转换", createColorCodePanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // 底部说明标签
        JLabel infoLabel = new JLabel(
                "<html><center>输入数值并选择输入单位，系统将自动计算所有单位的换算结果<br>" +
                        "部分类型的换算支持国际单位、英制单位和市制单位之间的换算</center></html>",
                SwingConstants.CENTER
        );
        infoLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(infoLabel, BorderLayout.SOUTH);
    }

    /**
     * 创建通用单位换算面板（用于长度、面积、体积等有固定单位列表的类别）。
     *
     * @param units 单位名称数组，用于下拉框和表格第一列
     * @return 配置好的 JPanel 对象
     */
    private JPanel createPanels(String[] units) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 输入面板（使用 GridBagLayout 实现紧凑布局）
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new TitledBorder("长度换算输入"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 数值输入标签
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        inputPanel.add(new JLabel("数值:"), gbc);

        // 数值输入框
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        JTextField valueField = new JTextField();
        inputPanel.add(valueField, gbc);

        // 分隔符
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        inputPanel.add(new JLabel(" "), gbc);

        // 单位选择标签
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        inputPanel.add(new JLabel("单位:"), gbc);

        // 单位下拉框
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1;
        JComboBox<String> unitCombo = new JComboBox<>(units);
        inputPanel.add(unitCombo, gbc);

        // 分隔符
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 1;
        inputPanel.add(new JLabel(" "), gbc);

        // 按钮面板（包含换算和清空按钮）
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton convertButton = new JButton("换算");
        JButton clearButton = new JButton("清空");
        buttonPanel.add(convertButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        inputPanel.add(buttonPanel, gbc);

        // 结果表格
        String[] columnNames = {"单位", "换算结果"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable resultTable = new JTable(tableModel);
        resultTable.setRowHeight(25);
        resultTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        resultTable.getColumnModel().getColumn(1).setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBorder(new TitledBorder("长度换算结果"));

        panel.add(inputPanel, BorderLayout.WEST);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 事件处理：换算按钮
        convertButton.addActionListener(e -> {
            try {
                double value = Double.parseDouble(valueField.getText().trim());
                int selectedIndex = unitCombo.getSelectedIndex();
                int unitChoice = selectedIndex + 1; // 内部方法使用 1-based 索引

                double[] results = getDoubleResult(unitChoice, value);

                // 清空表格
                tableModel.setRowCount(0);

                // 添加结果行
                for (int i = 0; i < results.length; i++) {
                    String formattedResult = String.format("%.10f", results[i]);
                    // 去掉末尾多余的零和小数点
                    formattedResult = formattedResult.replaceAll("0*$", "").replaceAll("\\.$", "");

                    Object[] rowData = {
                            units[i],
                            formattedResult
                    };
                    tableModel.addRow(rowData);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "请输入有效的数字！", "错误", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "换算过程中出现错误！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 清空按钮
        clearButton.addActionListener(e -> {
            valueField.setText("");
            tableModel.setRowCount(0);
        });

        // 回车键触发换算
        valueField.addActionListener(e -> convertButton.doClick());

        return panel;
    }

    /**
     * 创建颜色码转换面板。
     *
     * @return 配置好的 JPanel 对象
     */
    private JPanel createColorCodePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new TitledBorder("输入编码"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 编码类型选择
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        inputPanel.add(new JLabel("编码类型："), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        JComboBox<String> codeBox = new JComboBox<>(Convertors.COLOR_CODES);
        inputPanel.add(codeBox, gbc);

        // 分隔符
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        inputPanel.add(new JLabel(" "), gbc);

        // 数值输入标签
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        inputPanel.add(new JLabel("数值："), gbc);

        // 根据所选编码类型动态切换输入面板
        JPanel hexPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        JTextField hexField = new JTextField();
        hexPanel.add(hexField);
        hexPanel.add(new JLabel()); // 占位

        JPanel rgbPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        JTextField rField = new JTextField();
        JTextField gField = new JTextField();
        JTextField bField = new JTextField();
        rgbPanel.add(rField);
        rgbPanel.add(gField);
        rgbPanel.add(bField);

        JPanel cmykPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        JTextField cField = new JTextField();
        JTextField mField = new JTextField();
        JTextField yField = new JTextField();
        JTextField kField = new JTextField();
        cmykPanel.add(cField);
        cmykPanel.add(mField);
        cmykPanel.add(yField);
        cmykPanel.add(kField);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1;
        JPanel[] codePanels = {rgbPanel, cmykPanel, hexPanel};
        inputPanel.add(codePanels[0], gbc);
        final int[] panelIndex = {0, -1};

        // 分隔符
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        inputPanel.add(new JLabel(" "), gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton convertButton = new JButton("换算");
        JButton clearButton = new JButton("清空");
        buttonPanel.add(convertButton);
        buttonPanel.add(clearButton);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 1;
        inputPanel.add(buttonPanel, gbc);

        // 结果表格
        String[] columnNames = {"编码", "换算结果"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable resultTable = new JTable(tableModel);
        resultTable.setRowHeight(25);
        resultTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        resultTable.getColumnModel().getColumn(1).setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBorder(new TitledBorder("颜色码换算结果"));

        panel.add(inputPanel, BorderLayout.WEST);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 下拉框切换事件：更换输入面板
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1;
        codeBox.addActionListener(e -> {
            if (panelIndex[0] != codeBox.getSelectedIndex()) {
                panelIndex[1] = codeBox.getSelectedIndex();
                inputPanel.add(codePanels[panelIndex[1]], gbc);
                inputPanel.remove(codePanels[panelIndex[0]]);
                inputPanel.revalidate();
                inputPanel.repaint();
                panelIndex[0] = panelIndex[1];
            }
        });

        // 换算按钮事件
        convertButton.addActionListener(e -> {
            try {
                String[] results = new String[3];
                switch (codeBox.getSelectedIndex()) {
                    case 0: { // RGB -> CMYK 和 HEX
                        int r = Integer.parseInt(rField.getText().trim());
                        int g = Integer.parseInt(gField.getText().trim());
                        int b = Integer.parseInt(bField.getText().trim());
                        int[] rgb = {r, g, b};
                        String hex = Convertors.RGB2HEX(rgb).toUpperCase();
                        int[] cmyk = Convertors.RGB2CMYK(rgb);

                        results = new String[]{Arrays.toString(rgb), Arrays.toString(cmyk), hex};
                        break;
                    }
                    case 1: { // CMYK -> RGB 和 HEX
                        int c = Integer.parseInt(cField.getText().trim());
                        int m = Integer.parseInt(mField.getText().trim());
                        int y = Integer.parseInt(yField.getText().trim());
                        int k = Integer.parseInt(kField.getText().trim());
                        int[] cmyk = {c, m, y, k};
                        int[] rgb = Convertors.CMYK2RGB(cmyk);
                        String hex = Convertors.RGB2HEX(rgb).toUpperCase();

                        results = new String[]{Arrays.toString(rgb), Arrays.toString(cmyk), hex};
                        break;
                    }
                    case 2: { // HEX -> RGB 和 CMYK
                        String hex = hexField.getText().toUpperCase();
                        int[] rgb = Convertors.HEX2RGB(hex);
                        int[] cmyk = Convertors.RGB2CMYK(rgb);
                        results = new String[]{Arrays.toString(rgb), Arrays.toString(cmyk), "#" + hex};
                    }
                }

                // 清空表格
                tableModel.setRowCount(0);

                // 添加结果行
                for (int i = 0; i < results.length; i++) {
                    Object[] rowData = {
                            Convertors.COLOR_CODES[i],
                            results[i]
                    };
                    tableModel.addRow(rowData);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "请输入有效的数字！", "错误", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "转换过程中出现错误！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 清空按钮：仅清空表格，不清空输入框（可自行决定）
        clearButton.addActionListener(e -> tableModel.setRowCount(0));

        // 回车键触发换算
        hexField.addActionListener(e -> convertButton.doClick());
        bField.addActionListener(e -> convertButton.doClick());
        kField.addActionListener(e -> convertButton.doClick());

        return panel;
    }

    /**
     * 创建进制换算面板。
     *
     * @return 配置好的 JPanel 对象
     */
    private JPanel createNumSystemPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 输入面板
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new TitledBorder("进制换算输入"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 数值输入
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        inputPanel.add(new JLabel("数值:"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        JTextField valueField = new JTextField();
        inputPanel.add(valueField, gbc);

        // 分隔符
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        inputPanel.add(new JLabel(" "), gbc);

        // 单位选择
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        inputPanel.add(new JLabel("单位:"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1;
        JComboBox<String> unitCombo = new JComboBox<>(Convertors.NUM_SYSTEM_UNITS);
        inputPanel.add(unitCombo, gbc);

        // 分隔符
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 1;
        inputPanel.add(new JLabel(" "), gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton convertButton = new JButton("换算");
        JButton clearButton = new JButton("清空");
        buttonPanel.add(convertButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        inputPanel.add(buttonPanel, gbc);

        // 结果表格
        String[] columnNames = {"进制", "换算结果"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable resultTable = new JTable(tableModel);
        resultTable.setRowHeight(25);
        resultTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        resultTable.getColumnModel().getColumn(1).setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBorder(new TitledBorder("进制换算结果"));

        panel.add(inputPanel, BorderLayout.WEST);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 换算按钮事件
        convertButton.addActionListener(e -> {
            try {
                String value = valueField.getText().trim();
                int selectedIndex = unitCombo.getSelectedIndex();
                byte unitChoice = (byte) (selectedIndex + 1);

                String[] results = Convertors.numSystem(unitChoice, value);

                // 清空表格
                tableModel.setRowCount(0);

                // 添加结果
                for (int i = 0; i < results.length; i++) {
                    Object[] rowData = {
                            Convertors.NUM_SYSTEM_UNITS[i],
                            results[i]
                    };
                    tableModel.addRow(rowData);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "请输入有效的数字！", "错误", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "换算过程中出现错误！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 清空按钮
        clearButton.addActionListener(e -> {
            valueField.setText("");
            tableModel.setRowCount(0);
        });

        // 回车键触发换算
        valueField.addActionListener(e -> convertButton.doClick());

        return panel;
    }

    /**
     * 根据当前选中的选项卡，调用对应的换算方法获取 double[] 结果。
     *
     * @param unitChoice 输入单位的索引+1
     * @param value      输入的数值
     * @return 对应所有单位的换算结果数组
     */
    private double[] getDoubleResult(int unitChoice, double value) {
        double[] results = null;
        int tabIndex = tabbedPane.getSelectedIndex();

        switch (tabIndex) {
            case 0 -> results = Convertors.length(unitChoice, value);
            case 1 -> results = Convertors.area(unitChoice, value);
            case 2 -> results = Convertors.volume(unitChoice, value);
            case 3 -> results = Convertors.mass(unitChoice, value);
            case 4 -> results = Convertors.speed(unitChoice, value);
            case 5 -> results = Convertors.temperature(unitChoice, value);
            case 6 -> results = Convertors.storage(unitChoice, value);
        }

        return results;
    }

    /**
     * 设置整个界面的统一字体样式。
     */
    private void setupLayout() {
        Font labelFont = new Font("宋体", Font.PLAIN, 14);
        Font buttonFont = new Font("微软雅黑", Font.PLAIN, 13);
        Font tableFont = new Font("微软雅黑", Font.PLAIN, 12);

        // 遍历选项卡中的所有组件，递归设置字体
        Component[] components = tabbedPane.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                setComponentFont((JPanel) comp, labelFont, buttonFont, tableFont);
            }
        }
    }

    /**
     * 递归设置面板及其子组件的字体。
     *
     * @param panel      目标面板
     * @param labelFont  用于标签、文本框、下拉框的字体
     * @param buttonFont 用于按钮的字体
     * @param tableFont  用于表格的字体
     */
    private void setComponentFont(JPanel panel, Font labelFont, Font buttonFont, Font tableFont) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JLabel) {
                comp.setFont(labelFont);
            } else if (comp instanceof JButton) {
                comp.setFont(buttonFont);
            } else if (comp instanceof JComboBox) {
                comp.setFont(labelFont);
            } else if (comp instanceof JTextField) {
                comp.setFont(labelFont);
            } else if (comp instanceof JTable) {
                comp.setFont(tableFont);
            } else if (comp instanceof JScrollPane) {
                // 处理滚动面板内的表格
                Component view = ((JScrollPane) comp).getViewport().getView();
                if (view instanceof JTable) {
                    view.setFont(tableFont);
                }
            } else if (comp instanceof JPanel) {
                setComponentFont((JPanel) comp, labelFont, buttonFont, tableFont);
            }
        }
    }

    /**
     * 程序入口，在事件调度线程中启动图形界面。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(UnitConverterGUI::new);
    }
}