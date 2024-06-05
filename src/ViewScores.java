import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ViewScores extends JPanel {
    private PacmanWindow pacmanWindow;
    private Font font;

    public ViewScores(PacmanWindow pacmanWindow, Font font) {
        super();
        this.pacmanWindow = pacmanWindow;
        this.font = font;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setSize(new Dimension(pacmanWindow.getWidth()/2, pacmanWindow.getHeight()/2));


        add(headerLabel(), BorderLayout.NORTH);
        add(highScoresPanel(), BorderLayout.CENTER);
        add(backButton(),BorderLayout.SOUTH);

    }

    public JLabel headerLabel(){
        JLabel header = new JLabel("High Scores");
        header.setFont(font.deriveFont(Font.BOLD,28f));
        header.setAlignmentX(CENTER_ALIGNMENT);
        header.setForeground(new Color(255, 0, 255));

        return header;
    }

    public JScrollPane highScoresPanel(){
        var usersMap = usersFileManipulation.getUsersMap();
//        sort users
        var sortedUsers = usersMap.entrySet()
                .stream()
                .sorted((o1,o2) -> o2.getValue() - o1.getValue())
                .toList();

        var panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(CENTER_ALIGNMENT);
        panel.setBackground(Color.BLACK);

        sortedUsers.forEach( entry -> {
            JLabel userLabel = new JLabel(entry.getKey() + ": " + entry.getValue());
            userLabel.setFont(font);
            userLabel.setAlignmentX(LEFT_ALIGNMENT);
            userLabel.setForeground(Color.WHITE);

            panel.add(userLabel);
            panel.add(Box.createVerticalStrut(5));
        });

        var scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);

        return scrollPane;
    }

    public JButton backButton(){
        JButton backButton = new JButton("Back");
        backButton.setFont(font.deriveFont(Font.BOLD));
        backButton.setPreferredSize(new Dimension(500,20));
        backButton.setForeground(Color.BLACK);
        backButton.setAlignmentX(CENTER_ALIGNMENT);

        ActionListener backAction = e -> {
            try{
                pacmanWindow.remove(this);
                pacmanWindow.add(new MainMenu(pacmanWindow));
                pacmanWindow.revalidate();
                pacmanWindow.repaint();
            } catch (Exception ex) {
                System.out.println("An exception occurred while trying to create new main menu object");
                ex.printStackTrace();
            }
        };
        backButton.addActionListener(backAction);

        return backButton;
    }
}
