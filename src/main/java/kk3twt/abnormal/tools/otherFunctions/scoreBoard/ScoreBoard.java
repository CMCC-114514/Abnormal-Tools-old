package kk3twt.abnormal.tools.otherFunctions.scoreBoard;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ScoreBoard extends JFrame {

    private int scoreA = 0;
    private int scoreB = 0;

    private final JLabel teamA = new JLabel(String.valueOf(scoreA));
    private final JLabel teamB = new JLabel(String.valueOf(scoreB));

    public ScoreBoard() {
        setTitle("计分板");
        setSize(250,250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JButton plusA = new JButton("+");
        JButton minusA = new JButton("-");
        JPanel teamAPanel = getScorePanel("队伍1", teamA, plusA, minusA);

        JButton plusB = new JButton("+");
        JButton minusB = new JButton("-");
        JPanel teamBPanel = getScorePanel("队伍2", teamB, plusB, minusB);

        plusA.addActionListener(e -> teamA.setText(String.valueOf(++scoreA)));
        plusB.addActionListener(e -> teamB.setText(String.valueOf(++scoreB)));
        minusA.addActionListener(e -> teamA.setText(String.valueOf(--scoreA)));
        minusB.addActionListener(e -> teamB.setText(String.valueOf(--scoreB)));

        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        mainPanel.add(teamAPanel);
        mainPanel.add(teamBPanel);

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

    private JPanel getScorePanel(String name, JLabel score, JButton plus, JButton minus) {
        JPanel scorePanel = new JPanel(new GridLayout(1,2,10,10));

        score.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        score.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.add(plus);
        buttonPanel.add(minus);

        scorePanel.add(score);
        scorePanel.add(buttonPanel);
        scorePanel.setBorder(new TitledBorder(name));

        return scorePanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ScoreBoard().setVisible(true));
    }
}
