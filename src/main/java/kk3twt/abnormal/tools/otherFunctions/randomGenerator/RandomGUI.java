package kk3twt.abnormal.tools.otherFunctions.randomGenerator;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * 随机数及随机字符串生成器的图形用户界面。
 * 包含两个选项卡：
 * <ul>
 *     <li>“随机数生成”：用户输入最小值和最大值，生成一个随机整数。</li>
 *     <li>“随机字符串生成”：用户指定长度并选择包含的字符类型，生成随机字符串。</li>
 * </ul>
 */
public class RandomGUI extends JFrame {

    /** 主内容面板，采用边界布局 */
    private JPanel mainPanel;

    /** 选项卡面板，包含两个功能面板 */
    private JTabbedPane tabbedPane;

    /** 随机数生成面板 */
    private JPanel numPanel;

    /** 随机字符串生成面板 */
    private JPanel strPanel;

    /**
     * 构造方法，初始化窗口属性、组件及布局。
     */
    public RandomGUI() {
        setTitle("随机数生成");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(350, 350);
        setLocationRelativeTo(null);
        setVisible(true);

        initComponents();
        setupLayout();

        setContentPane(mainPanel);
    }

    /**
     * 初始化主面板、选项卡面板，并创建两个功能面板。
     */
    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();

        createNumPanel();
        createStrPanel();

        tabbedPane.addTab("随机数生成", numPanel);
        tabbedPane.addTab("随机字符串生成", strPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * 创建随机数生成面板。
     * 包含最小值输入框、最大值输入框、生成按钮和结果显示区。
     * 点击“生成”按钮后，调用 Generators.randomNum 并显示结果。
     */
    private void createNumPanel() {
        numPanel = new JPanel(new BorderLayout(10, 10));
        numPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入参数"));

        JTextField minField = new JTextField();
        inputPanel.add(new JLabel("最小值："));
        inputPanel.add(minField);

        JTextField maxField = new JTextField();
        inputPanel.add(new JLabel("最大值："));
        inputPanel.add(maxField);

        JButton generateButton = new JButton("生成");
        inputPanel.add(new Label("")); // 占位
        inputPanel.add(generateButton);

        JTextArea resultArea = new JTextArea(1, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("生成结果"));

        numPanel.add(inputPanel, BorderLayout.NORTH);
        numPanel.add(scrollPane, BorderLayout.CENTER);

        // 为生成按钮添加事件监听
        generateButton.addActionListener(e -> {
            try {
                int min = Integer.parseInt(minField.getText().trim());
                int max = Integer.parseInt(maxField.getText().trim());

                resultArea.setText(Generators.randomNum(min, max) + " ");
            } catch (NumberFormatException ex) {
                resultArea.setText("错误：请输入有效的内容！");
            } catch (IllegalArgumentException ex) {
                resultArea.setText("错误：" + ex.getMessage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * 创建随机字符串生成面板。
     * 包含长度输入框、字符类型复选框、生成按钮和结果显示区。
     * 点击“生成”按钮后，根据选中的字符类型拼接字符集，调用 Generators.randomStr 并显示结果。
     */
    private void createStrPanel() {
        strPanel = new JPanel(new BorderLayout(10, 10));
        strPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 输入面板：长度输入框和生成按钮
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入字符串长度"));
        JTextField lengthField = new JTextField();
        JButton generateButton = new JButton("生成");
        inputPanel.add(lengthField);
        inputPanel.add(generateButton);

        // 字符类型选择面板（复选框）
        JPanel setPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        setPanel.setBorder(new TitledBorder("字符类型"));
        JCheckBox numBox = new JCheckBox("数字");
        JCheckBox capitalBox = new JCheckBox("大写字母");
        JCheckBox lowerBox = new JCheckBox("小写字母");
        JCheckBox punctuationBox = new JCheckBox("标点符号");
        setPanel.add(numBox);
        setPanel.add(capitalBox);
        setPanel.add(lowerBox);
        setPanel.add(punctuationBox);

        // 输出结果区域
        JTextArea resultArea = new JTextArea(1, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("生成结果"));

        strPanel.add(inputPanel, BorderLayout.NORTH);
        strPanel.add(setPanel, BorderLayout.EAST);
        strPanel.add(scrollPane, BorderLayout.CENTER);

        Generators.init();  // 初始化字符集

        // 为生成按钮添加事件监听
        generateButton.addActionListener(e -> {
            try {
                int length = Integer.parseInt(lengthField.getText().trim());

                String characters = "";
                if (numBox.isSelected())
                    characters += Generators.NUM;
                if (capitalBox.isSelected())
                    characters += Generators.CAPITAL;
                if (lowerBox.isSelected())
                    characters += Generators.LOWERCASE;
                if (punctuationBox.isSelected())
                    characters += Generators.PUNCTUATIONS;

                resultArea.setText(Generators.randomStr(length, characters));
            } catch (NumberFormatException ex) {
                resultArea.setText("错误：请输入有效的内容！");
            } catch (IllegalArgumentException ex) {
                resultArea.setText("错误：" + ex.getMessage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * 设置整个窗口的字体样式。
     * 从选项卡面板中获取所有子组件，递归应用字体设置。
     */
    private void setupLayout() {
        // 设置统一的字体
        // Font titleFont = new Font("微软雅黑", Font.BOLD, 16);
        Font labelFont = new Font("微软雅黑", Font.PLAIN, 13);
        Font buttonFont = new Font("微软雅黑", Font.PLAIN, 13);
        Font tableFont = new Font("宋体", Font.PLAIN, 13);

        // 设置所有组件的字体
        Component[] components = tabbedPane.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                setComponentFont((JPanel) comp, labelFont, buttonFont, tableFont);
            }
        }
    }

    /**
     * 递归设置指定面板及其所有子组件的字体。
     *
     * @param panel       要设置字体的面板
     * @param labelFont   标签使用的字体
     * @param buttonFont  按钮使用的字体
     * @param tableFont   表格使用的字体
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
                // 处理滚动面板中的视图组件
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
     * 程序入口，在事件分派线程中启动 GUI。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(RandomGUI::new);
    }
}