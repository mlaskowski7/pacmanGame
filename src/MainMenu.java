import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainMenu extends JPanel{
    private Font font;
    private final PacmanWindow pacmanWindow;
    private boolean mainMenuVisible;

    public MainMenu(PacmanWindow pacmanWindow){

        super();
        this.pacmanWindow = pacmanWindow;
        this.mainMenuVisible = true;

        loadFont();

        initMainMenuPanel();

    }

    public void loadFont(){
        try{
            Font robotoMonoFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/RobotoMonoFont/RobotoMono-VariableFont_wght.ttf"));
            font = robotoMonoFont.deriveFont(18f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
        } catch (Exception e){
            System.out.println("Something went wrong while trying to load font - " + e.getMessage());
        }
    }

    public void initMainMenuPanel(){
        setBackground(Color.BLACK);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


        add(Box.createVerticalGlue());
        add(header());
        add(Box.createVerticalStrut(30));
        add(newGameButton());
        add(Box.createVerticalStrut(10));
        add(viewScoresButton());
        add(Box.createVerticalStrut(10));
        add(exitButton());
        add(Box.createVerticalGlue());
    }

    // method creating header box
    public Box header(){
        Box header = Box.createVerticalBox();

        JLabel headerLabel = new JLabel("Pacman Game");
        headerLabel.setFont(font.deriveFont(Font.BOLD,28));
        headerLabel.setForeground(new Color(255, 0, 255));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        changeHeaderColor(headerLabel);

        JLabel authorLabel = new JLabel("By Mateusz Laskowski");
        authorLabel.setFont(font.deriveFont(Font.ITALIC,18));
        authorLabel.setForeground(Color.GRAY);
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel highScoreLabel = new JLabel("Best score: " + getBestScore());
        highScoreLabel.setFont(font.deriveFont(22f));
        highScoreLabel.setForeground(Color.WHITE);
        highScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(headerLabel);
        header.add(authorLabel);
        header.add(highScoreLabel);

        return header;
    }

    private String getBestScore(){
        var usersMap = usersFileManipulation.getUsersMap();
        var sortedUsers = usersMap.entrySet()
                .stream()
                .sorted((o1,o2) -> o2.getValue() - o1.getValue())
                .toList();
        return sortedUsers.get(0).getValue().toString();
    }

    public void changeHeaderColor(JLabel headerLabel){
        var isHeaderYellow = new AtomicBoolean(false);
        var headerColoringThread = new Thread(() -> {

            while(mainMenuVisible){
                try{
                    Thread.sleep(500);
                    if(!isHeaderYellow.get()){
                        headerLabel.setForeground(Color.YELLOW);
                        isHeaderYellow.set(true);
                    } else {
                        headerLabel.setForeground(new Color(255, 0, 255));
                        isHeaderYellow.set(false);
                    }
                    repaint();
                } catch (InterruptedException ex){
                    System.out.println("Header coloring thread was interrupted - " + ex.getMessage());
                }
            }

        });

        headerColoringThread.start();
    }

    public JButton newGameButton(){
        JButton newGameButton = new JButton("New Game");
        newGameButton.setFont(font.deriveFont(18f));
        newGameButton.setForeground(new Color(255,0,255));
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        ActionListener newGameAction = e -> {
            mainMenuVisible = false;
            pacmanWindow.remove(this);
            pacmanWindow.add(new EnterNickname(pacmanWindow,font));
            pacmanWindow.revalidate();
            pacmanWindow.repaint();
        };

        newGameButton.addActionListener(newGameAction);

        return newGameButton;
    }

    public JButton viewScoresButton(){
        JButton viewScoresButton = new JButton("View High Scores");
        viewScoresButton.setFont(font.deriveFont(18f));
        viewScoresButton.setForeground(new Color(255,0,255));
        viewScoresButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        ActionListener viewScoresAction = e -> {
            mainMenuVisible = false;
            pacmanWindow.remove(this);
            pacmanWindow.add(new ViewScores(pacmanWindow, font));
            pacmanWindow.revalidate();
            pacmanWindow.repaint();
        };

        viewScoresButton.addActionListener(viewScoresAction);

        return viewScoresButton;
    }

    public JButton exitButton(){
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(font.deriveFont(18f));
        exitButton.setForeground(Color.BLACK);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        ActionListener exitAction = e -> System.exit(0);

        exitButton.addActionListener(exitAction);

        return exitButton;
    }




}
