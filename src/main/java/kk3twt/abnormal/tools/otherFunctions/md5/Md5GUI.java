package kk3twt.abnormal.tools.otherFunctions.md5;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * MD5 摘要计算图形用户界面。
 * 提供一个简单的窗口，允许用户输入字符串，点击按钮后显示其 MD5 值。
 */
public class Md5GUI extends JFrame {

    /**
     * 构造 MD5 计算窗口，初始化界面组件并设置事件监听。
     */
    public Md5GUI() {
        setTitle("MD5摘要");
        setSize(450, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setVisible(true);

        // 主面板，使用边界布局，设置边距
        JPanel md5Panel = new JPanel(new BorderLayout(10, 10));
        md5Panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 输入面板：包含文本框和按钮
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入字符"));

        JTextField strField = new JTextField();
        inputPanel.add(strField);

        JButton encryptButton = new JButton("获取MD5");
        inputPanel.add(encryptButton);

        // 结果显示区域（只读文本区）
        JTextArea resultArea = new JTextArea(1, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("获取结果"));

        md5Panel.add(inputPanel, BorderLayout.NORTH);
        md5Panel.add(scrollPane, BorderLayout.CENTER);

        // 为“获取MD5”按钮添加事件监听
        encryptButton.addActionListener(e -> {
            try {
                String str = strField.getText();               // 获取用户输入
                String md5 = MD5.getMD5(str);                  // 调用 MD5 工具类计算
                resultArea.setText(md5);                       // 显示结果
            } catch (NumberFormatException ex) {
                resultArea.setText("错误：请输入有效的内容！");
            } catch (IllegalArgumentException ex) {
                resultArea.setText("错误：" + ex.getMessage());
            } catch (Exception ex) {
                // 其他未预期异常，包装为运行时异常抛出（也可在此记录日志）
                throw new RuntimeException(ex);
            }
        });

        add(md5Panel);
    }

    /**
     * 程序入口，在事件分派线程中启动 GUI。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Md5GUI::new);
    }
}