import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class MainMenu {
    private static int highScore;
    private static Font font;

    public static void setFont(String fontPath) throws Exception {
        try{
            Font robotoMonoFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
            font = robotoMonoFont;
        } catch (Exception e){
            System.out.println("Something went wrong while trying to load font");
            e.printStackTrace();
            throw e;
        }
    }

    public static void setHighScore(int hs){
        highScore = hs;
    }


    // method creating header box
    public static Box header(){
        Box header = Box.createVerticalBox();
        JLabel headerLabel = new JLabel("<html><div style='font-size:36px;  color: #FF00FF; text-align:center;'>PacMan Game</div></html>");
        JLabel authorLabel = new JLabel("<html><div style='font-size:16px; color: gray; text-align:center;'>By Mateusz Laskowski</div></html>");
        headerLabel.setFont(font.deriveFont(Font.BOLD));
        authorLabel.setFont(font);
        headerLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        authorLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        header.add(headerLabel);
        header.add(authorLabel);

        return header;
    }

    public static JButton newGameButton(){
        JButton newGameButton = new JButton("New Game");
        newGameButton.setFont(font.deriveFont(16f));
        newGameButton.setForeground(new Color(0xFF00FF));
        newGameButton.setBackground(Color.BLACK);
        newGameButton.setOpaque(false);
        return newGameButton;
    }

    public static JButton exitButton(){
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(font.deriveFont(16f));
        exitButton.setForeground(new Color(0xFF00FF));
        exitButton.setBackground(Color.BLACK);
        exitButton.setOpaque(false);

        ActionListener exitAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };

        exitButton.addActionListener(exitAction);

        return exitButton;
    }

//    method used to initialize main menu
    public static void init(JFrame window){
        Box headerBox = header();
        JButton newGameButton = newGameButton();
        JButton exitButton = exitButton();
        JPanel menuPanel = new JPanel(new GridBagLayout());

//        Styling for the header box
        GridBagConstraints headerGridConstraints = new GridBagConstraints();
        headerGridConstraints.gridx = 0;
        headerGridConstraints.gridy = 0;
        headerGridConstraints.anchor = GridBagConstraints.CENTER;
        menuPanel.add(headerBox, headerGridConstraints);

//        Styling for new game button
        GridBagConstraints newGameGridConstraints = new GridBagConstraints();
        newGameGridConstraints.gridx = 0;
        newGameGridConstraints.gridy = 1;
        newGameGridConstraints.anchor = GridBagConstraints.CENTER;
        menuPanel.add(newGameButton, newGameGridConstraints);

//        Styling for exit button
        GridBagConstraints exitGridConstraints = new GridBagConstraints();
        exitGridConstraints.gridx = 0;
        exitGridConstraints.gridy = 2;
        exitGridConstraints.anchor = GridBagConstraints.CENTER;
        menuPanel.add(exitButton, exitGridConstraints);

        menuPanel.setBackground(Color.BLACK);
        window.add(menuPanel, BorderLayout.CENTER);
    }
}
