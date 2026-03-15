package kk3twt.abnormal.tools.otherFunctions.md5;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class Md5GUI extends JFrame {
    public Md5GUI() {
        setTitle("MD5摘要");
        setSize(450, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel md5Panel = new JPanel(new BorderLayout(10, 10));
        md5Panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("输入字符"));

        JTextField strField = new JTextField();

        inputPanel.add(strField);

        JButton encryptButton = new JButton("获取MD5");
        inputPanel.add(encryptButton);

        // 结果面板
        JTextArea resultArea = new JTextArea(1, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder("获取结果"));

        md5Panel.add(inputPanel, BorderLayout.NORTH);
        md5Panel.add(scrollPane, BorderLayout.CENTER);

        // 事件处理
        encryptButton.addActionListener(e -> {
            try {
                String str = strField.getText();
                String md5 = MD5.getMD5(str);

                resultArea.setText(md5);
            } catch (NumberFormatException ex) {
                resultArea.setText("错误：请输入有效的内容！");
            } catch (IllegalArgumentException ex) {
                resultArea.setText("错误：" + ex.getMessage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        add(md5Panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Md5GUI().setVisible(true));
    }
}
