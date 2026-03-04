package fileFunctions.imageScramble;

import javax.swing.*;
import java.awt.*;

public class ScrambleProgress extends JFrame {

    private final JProgressBar progressBar;
    private final JLabel label;

    public ScrambleProgress() {

        setTitle("视频混淆处理中");
        setSize(420,120);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        label = new JLabel("正在处理视频，请稍候...", JLabel.CENTER);

        progressBar = new JProgressBar(0,100);
        progressBar.setStringPainted(true);

        add(label, BorderLayout.NORTH);
        add(progressBar, BorderLayout.CENTER);
    }

    public void updateProgress(int percent) {
        SwingUtilities.invokeLater(() -> progressBar.setValue(percent));
    }

    public void finish() {
        SwingUtilities.invokeLater(() -> {
            dispose();
            JOptionPane.showMessageDialog(
                    null,
                    "混淆完成",
                    "完成",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }
}