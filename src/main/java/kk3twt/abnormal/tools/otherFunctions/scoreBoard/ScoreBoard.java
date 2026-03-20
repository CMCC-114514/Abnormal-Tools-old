package kk3twt.abnormal.tools.otherFunctions.scoreBoard;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * 计分板应用程序。
 * 提供一个简单的图形界面，用于记录两个队伍的分数。
 * 每个队伍有加分（+）和减分（-）按钮，底部有重置按钮将两队分数归零。
 */
public class ScoreBoard extends JFrame {

    /** 队伍1的当前分数 */
    private int scoreA = 0;

    /** 队伍2的当前分数 */
    private int scoreB = 0;

    /** 显示队伍1分数的标签 */
    private final JLabel teamA = new JLabel(String.valueOf(scoreA));

    /** 显示队伍2分数的标签 */
    private final JLabel teamB = new JLabel(String.valueOf(scoreB));

    /**
     * 构造计分板窗口，初始化界面组件和事件监听器。
     */
    public ScoreBoard() {
        setTitle("计分板");
        setSize(250, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 创建队伍1的控制面板
        JButton plusA = new JButton("+");
        JButton minusA = new JButton("-");
        JPanel teamAPanel = getScorePanel("队伍1", teamA, plusA, minusA);

        // 创建队伍2的控制面板
        JButton plusB = new JButton("+");
        JButton minusB = new JButton("-");
        JPanel teamBPanel = getScorePanel("队伍2", teamB, plusB, minusB);

        // 为加分/减分按钮添加事件监听，更新分数并刷新标签显示
        plusA.addActionListener(e -> teamA.setText(String.valueOf(++scoreA)));
        plusB.addActionListener(e -> teamB.setText(String.valueOf(++scoreB)));
        minusA.addActionListener(e -> teamA.setText(String.valueOf(--scoreA)));
        minusB.addActionListener(e -> teamB.setText(String.valueOf(--scoreB)));

        // 主面板，垂直排列两个队伍的面板
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(teamAPanel);
        mainPanel.add(teamBPanel);

        // 重置按钮，将两队分数归零
        JButton reset = new JButton("重置");
        reset.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        reset.addActionListener(e -> {
            scoreA = 0;
            scoreB = 0;
            teamA.setText(String.valueOf(scoreA));
            teamB.setText(String.valueOf(scoreB));
        });

        add(mainPanel, BorderLayout.CENTER);
        add(reset, BorderLayout.SOUTH);
    }

    /**
     * 创建一个队伍的面板，包含队伍名称、分数显示及加减按钮。
     *
     * @param name  队伍名称，将作为边框标题
     * @param score 显示分数的标签（外部传入，以便更新）
     * @param plus  加分按钮
     * @param minus 减分按钮
     * @return 组装好的队伍面板
     */
    private JPanel getScorePanel(String name, JLabel score, JButton plus, JButton minus) {
        JPanel scorePanel = new JPanel(new GridLayout(1, 2, 10, 10));

        score.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        score.setHorizontalAlignment(SwingConstants.CENTER);

        // 按钮面板，垂直排列加减按钮
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.add(plus);
        buttonPanel.add(minus);

        scorePanel.add(score);
        scorePanel.add(buttonPanel);
        scorePanel.setBorder(new TitledBorder(name));

        return scorePanel;
    }

    /**
     * 程序入口，在事件分派线程中启动计分板窗口。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ScoreBoard().setVisible(true));
    }
}