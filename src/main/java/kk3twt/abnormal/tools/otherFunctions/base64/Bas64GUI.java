package kk3twt.abnormal.tools.otherFunctions.base64;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Base64 编解码的图形界面。
 * 提供一个窗口，允许用户选择编码或解码模式，输入内容，并显示转换结果。
 */
public class Bas64GUI extends JFrame {

    /**
     * 构造 Base64 编解码窗口，初始化组件并设置事件监听。
     */
    public Bas64GUI() {
        setTitle("Base64编解码");
        setSize(400, 210);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setVisible(true);

        // 主面板
        JPanel base64Panel = new JPanel(new BorderLayout(10, 10));
        base64Panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 输入面板：包含模式选择、输入框和转换按钮
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入内容"));

        JComboBox<String> patternCombo = new JComboBox<>(Bas64.PATTERNS);
        JTextField strField = new JTextField();
        JButton convertButton = new JButton("转换");

        inputPanel.add(new JLabel("转换方式："));
        inputPanel.add(patternCombo);
        inputPanel.add(strField);
        inputPanel.add(convertButton);

        // 结果面板：用于显示转换结果，不可编辑
        JTextArea resultArea = new JTextArea(1, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("转换结果"));

        base64Panel.add(inputPanel, BorderLayout.NORTH);
        base64Panel.add(scrollPane, BorderLayout.CENTER);

        // 转换按钮事件处理
        convertButton.addActionListener(e -> {
            try {
                int selectIndex = patternCombo.getSelectedIndex();
                String str = strField.getText();
                String result;

                if (str.isEmpty()) {
                    resultArea.setText("错误：空内容无法加密或解密");
                } else {
                    switch (selectIndex) {
                        case 0 -> result = Bas64.enCode(str);
                        case 1 -> result = Bas64.deCode(str);
                        default -> result = "错误：不合法的选项";
                    }
                    resultArea.setText(result);
                }
            } catch (NumberFormatException ex) {
                resultArea.setText("错误：请输入有效的内容！");
            } catch (IllegalArgumentException ex) {
                resultArea.setText("错误：" + ex.getMessage());
            }
        });

        add(base64Panel);
    }

    /**
     * 程序入口，在事件调度线程中启动 GUI。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Bas64GUI::new);
    }
}