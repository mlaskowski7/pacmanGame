import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class Game extends JPanel{
    private int cell;
    private int score;
    private int highScore;
    private int[][] map;
    private Hero hero;
    private List<Ghost> ghosts;
    private PacmanWindow pacmanWindow;
    private Font font;
    private String currentNickname;
    private UpperPanel upperPanel;
    private JButton backButton;
    private boolean gameStarted;

    public Game(int cell, int[][] map, PacmanWindow pacmanWindow, Font font){
        this.cell = cell;
        this.map = map;
        this.pacmanWindow = pacmanWindow;
        this.score = 0;
        this.currentNickname = EnterNickname.getCurrentNickname();
        this.highScore = usersFileManipulation.getUsersMap().get(currentNickname);
        this.font = font;


        pacmanWindow.setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setLayout(null);

        hero = new Hero(new Dimension(0,cell*2), cell);
        add(hero);

        upperPanel = (map.length > 10) ? new UpperPanel(font,1.0f, currentNickname) : new UpperPanel(font, 0.6f, currentNickname);
        backButton = backButton();


        pacmanWindow.add(upperPanel, BorderLayout.NORTH);
        pacmanWindow.add(this, BorderLayout.CENTER);
        pacmanWindow.add(backButton, BorderLayout.SOUTH);

        gameStarted = true;
        startGame();

        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()){
                    case KeyEvent.VK_UP:
                        hero.setDirection(Hero.Direction.UP);
                        break;
                    case KeyEvent.VK_DOWN:
                        hero.setDirection(Hero.Direction.DOWN);
                        break;

                    case KeyEvent.VK_LEFT:
                        hero.setDirection(Hero.Direction.LEFT);
                        break;
                    case KeyEvent.VK_RIGHT:
                        hero.setDirection(Hero.Direction.RIGHT);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}

        });

    }

    public void startGame(){
        Thread pacmanMovement = new Thread(() -> {
            while(gameStarted){
                try{
                    Thread.sleep(350);

                    switch(hero.getDirection()){
                        case UP:
                            if(map[hero.getPosition().getSize().height/cell - 1][hero.getPosition().getSize().width/cell] != 1){
                                hero.setPosition(new Dimension(hero.getPosition().width, hero.getPosition().height - cell));
                            }
                            break;
                        case DOWN:
                            if(map[hero.getPosition().getSize().height/cell + 1][hero.getPosition().getSize().width/cell] != 1){
                                hero.setPosition(new Dimension(hero.getPosition().width, hero.getPosition().height + cell));
                            }
                            break;
                        case LEFT:
                            if(map[hero.getPosition().getSize().height/cell][hero.getPosition().getSize().width/cell - 1] != 1){
                                hero.setPosition(new Dimension(hero.getPosition().width - cell, hero.getPosition().height));
                            }
                            break;
                        case RIGHT:
                            if(map[hero.getPosition().getSize().height/cell][hero.getPosition().getSize().width/cell + 1] != 1){
                                hero.setPosition(new Dimension(hero.getPosition().width + cell, hero.getPosition().height));
                            }
                            break;
                    }

                    if(map[hero.getPosition().getSize().height/cell][hero.getPosition().getSize().width/cell] == 2){
                        map[hero.getPosition().getSize().height/cell][hero.getPosition().getSize().width/cell] = 0;
                        score += 1;
                        upperPanel.setScore(score);
                        if(score > highScore){
                            highScore = score;
                            upperPanel.setHighScore(highScore);
                            usersFileManipulation.updateHighScore(currentNickname, highScore);
                        }
                        repaint();
                    }

                    hero.repaint();
                } catch (InterruptedException ex) {
                    System.out.println("Game thread was interrupted - " + ex.getMessage());
                }


            }
        });

        pacmanMovement.start();

    }

    public JButton backButton(){
        var button = new JButton("Back");
        button.setBackground(Color.BLACK);
        button.setSize(pacmanWindow.getWidth(),7);
        button.addActionListener(e -> {
            try{
                gameStarted = false;
                hero.setIsDead(true);
                pacmanWindow.remove(upperPanel);
                pacmanWindow.remove(this);
                pacmanWindow.remove(backButton);
                pacmanWindow.setSize(500,525);
                pacmanWindow.add(new SelectMap(pacmanWindow, font));
                pacmanWindow.revalidate();
                pacmanWindow.repaint();
            } catch (Exception ex) {
                System.out.println("An exception occurred while trying to create new main menu object");
                ex.printStackTrace();
            }
        });

        return button;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[i].length; j++){
                if(map[i][j] == 1){
                    g.setColor(new Color(48, 25, 52));
                    g.drawRect(j*cell, i*cell, cell, cell);
                } else if(map[i][j] == 0){
                    g.setColor(Color.BLACK);
                    g.fillRect(j*cell, i*cell, cell, cell);
                } else if(map[i][j] == 2){
                    g.setColor(Color.WHITE);
                    g.fillRect(j*cell + cell / 2, i*cell + cell / 2, cell/10, cell/10);
                }
            }
        }

    }
}
