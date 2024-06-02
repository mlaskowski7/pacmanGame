import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JFrame {
    private int highScore;
    private Font font;

    public MainMenu(JFrame window) throws Exception {
        try{
            Font robotoMonoFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/RobotoMonoFont/RobotoMono-VariableFont_wght.ttf"));
            font = robotoMonoFont.deriveFont(18f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
        } catch (Exception e){
            System.out.println("Something went wrong while trying to load font");
            e.printStackTrace();
            throw e;
        }
        Box headerBox = header();
        JPanel menuPanel = new JPanel(new GridBagLayout());
        JButton newGameButton = newGameButton(window, menuPanel);
        JButton exitButton = exitButton();

//        Styling for the header box
        GridBagConstraints headerGridConstraints = new GridBagConstraints();
        headerGridConstraints.gridx = 0;
        headerGridConstraints.gridy = 0;
        headerGridConstraints.anchor = GridBagConstraints.WEST;
        headerGridConstraints.insets = new Insets(0, 0, 15, 0);
        menuPanel.add(headerBox, headerGridConstraints);

//        Styling for new game button
        GridBagConstraints newGameGridConstraints = new GridBagConstraints();
        newGameGridConstraints.gridx = 0;
        newGameGridConstraints.gridy = 1;
        newGameGridConstraints.anchor = GridBagConstraints.WEST;
        menuPanel.add(newGameButton, newGameGridConstraints);

//        Styling for exit button
        GridBagConstraints exitGridConstraints = new GridBagConstraints();
        exitGridConstraints.gridx = 0;
        exitGridConstraints.gridy = 2;
        exitGridConstraints.anchor = GridBagConstraints.WEST;
        menuPanel.add(exitButton, exitGridConstraints);

        menuPanel.setBackground(Color.BLACK);
        window.add(menuPanel, BorderLayout.CENTER);
    }

    public void setHighScore(int hs){
        highScore = hs;
    }


    // method creating header box
    public Box header(){
        Box header = Box.createVerticalBox();
        JLabel headerLabel = new JLabel("<html><div style='font-size:36px;  color: #FF00FF; text-align:center;'>PacMan Game</div></html>");
        JLabel authorLabel = new JLabel("<html><div style='font-size:16px; color: gray; text-align:center;'>By Mateusz Laskowski</div></html>");
        JLabel highScoreLabel = new JLabel("<html><div style='font-size:16px; color: white; text-align:center;'>Your high Score: " + highScore + "</div></html>");
        headerLabel.setFont(font.deriveFont(Font.BOLD));
        authorLabel.setFont(font);
        highScoreLabel.setFont(font);
        headerLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        authorLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        highScoreLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        header.add(headerLabel);
        header.add(authorLabel);
        header.add(highScoreLabel);

        return header;
    }

    public JButton newGameButton(JFrame window, JPanel menuPanel){
        JButton newGameButton = new JButton("New Game");
        newGameButton.setFont(font.deriveFont(18f));
        newGameButton.setForeground(new Color(0xFF00FF));

        ActionListener newGameAction = e -> {
            SelectMap selectMap = new SelectMap(font, window);
            window.remove(menuPanel);
            window.add(selectMap);
            window.revalidate();
            window.repaint();
        };

        newGameButton.addActionListener(newGameAction);

        return newGameButton;
    }

    public JButton exitButton(){
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(font.deriveFont(18f));
        exitButton.setForeground(new Color(0xFF00FF));

        ActionListener exitAction = e -> System.exit(0);

        exitButton.addActionListener(exitAction);

        return exitButton;
    }


}
