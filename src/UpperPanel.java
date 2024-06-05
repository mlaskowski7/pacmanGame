import javax.swing.*;
import java.awt.*;

public class UpperPanel extends JPanel {
    private Font font;
    private float fontScale;
    private JLabel scoreValue;
    private JLabel highscoreValue;

    UpperPanel(Font font,float fontScale, String currentNickname) {
        this.font = font;
        this.fontScale = fontScale;

        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBackground(Color.BLACK);
        JPanel innerPanel = new JPanel(new GridLayout(2,3,(int)(5*fontScale),(int)(5*fontScale)));
        innerPanel.setBackground(Color.BLACK);

        JLabel userLabel = new JLabel("Your nickname: ");
        userLabel.setFont(font.deriveFont(16f * fontScale));
        userLabel.setForeground(Color.WHITE);
        userLabel.setPreferredSize(new Dimension((int) (150  * fontScale), (int)(20 * fontScale)));
        JLabel userValue = new JLabel(currentNickname);
        userValue.setFont(font.deriveFont(16f * fontScale));
        userValue.setForeground(Color.YELLOW);
        userValue.setPreferredSize(new Dimension((int) (150  * fontScale), (int)(20 * fontScale)));

        JLabel scoreLabel = new JLabel("Score: ");
        scoreLabel.setFont(font.deriveFont(16f * fontScale));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setPreferredSize(new Dimension((int) (150  * fontScale), (int)(20 * fontScale)));
        scoreValue = new JLabel(String.valueOf(0));
        scoreValue.setFont(font.deriveFont(16f * fontScale));
        scoreValue.setForeground(new Color(255,0,255));
        scoreValue.setPreferredSize(new Dimension((int) (150  * fontScale), (int)(20 * fontScale)));

        int highScore = usersFileManipulation.getUsersMap().get(currentNickname);

        JLabel highscoreLabel = new JLabel("High score: ");
        highscoreLabel.setFont(font.deriveFont(16f * fontScale));
        highscoreLabel.setForeground(Color.WHITE);
        highscoreLabel.setPreferredSize(new Dimension((int) (150  * fontScale), (int)(20 * fontScale)));
        highscoreValue = new JLabel(String.valueOf(highScore));
        highscoreValue.setFont(font.deriveFont(16f * fontScale));
        highscoreValue.setForeground(Color.RED);
        highscoreValue.setPreferredSize(new Dimension((int) (150  * fontScale), (int)(20 * fontScale)));

        innerPanel.add(userLabel);
        innerPanel.add(scoreLabel);
        innerPanel.add(highscoreLabel);
        innerPanel.add(userValue);
        innerPanel.add(scoreValue);
        innerPanel.add(highscoreValue);

        add(innerPanel);
    }

    public void setScore(int score) {
        scoreValue.setText(String.valueOf(score));
        repaint();
    }

    public void setHighScore(int score) {
        highscoreValue.setText(String.valueOf(score));
        repaint();
    }
}
