import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
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
        setLayout(new GridLayout(map.length, map[0].length));

        for(var row : map){
            for(var element : row){
                JLabel label = new JLabel();
                label.setSize(new Dimension(cell,cell));
                if(element == 0){
                    label.setBackground(Color.BLACK);
                } else if(element == 1){
                    label.setBackground(Color.BLACK);
                    var border = BorderFactory.createLineBorder(new Color(255,0,255),3);
                    label.setBorder(border);
                } else if(element == 2){
                    var point = new JLabel();
                    point.setBackground(Color.WHITE);
                    point.setSize(cell/2,cell/2);

                    point.setLocation(new Point(label.getWidth() - point.getWidth()/2, label.getHeight() - point.getHeight()/2));
                    label.add(point);
                }
                add(label);
            }
        }

        hero = new Hero(new Dimension(0,cell*2), cell);
        add(hero);

//        ghosts = new ArrayList<>();
//      for(int i = 0; i < (int)(Math.random()*6) + 1; i++){
//           ghosts.add(new Ghost(new Dimension(map.length/2 * cell + i*cell,map[0].length/2*cell), cell, (int)(Math.random() * 3 +1)));
//       }
//       for(Ghost ghost : ghosts){
//           add(ghost);
//           System.out.println("should add ghost");
//       }

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
                        hero.setState(State.UP);
                        break;
                    case KeyEvent.VK_DOWN:
                        hero.setState(State.DOWN);
                        break;

                    case KeyEvent.VK_LEFT:
                        hero.setState(State.LEFT);
                        break;
                    case KeyEvent.VK_RIGHT:
                        hero.setState(State.RIGHT);
                        break;
                    case KeyEvent.VK_ESCAPE:
                        goBack();
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

                    switch(hero.getCurrentState()){
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

                    for(Ghost ghost : ghosts){
                        int diffX = hero.getPosition().height - ghost.getPosition().height;
                        int diffY = hero.getPosition().width - ghost.getPosition().width;
                        switch (ghost.getCurrentState()){
                            case UP:
                            case DOWN:
                            case LEFT:
                            case RIGHT:
                                if(Math.abs(diffX) > Math.abs(diffY)){
                                    if(map[ghost.getPosition().getSize().height/cell][ghost.getPosition().getSize().width/cell + (diffX / Math.abs(diffX))] != 1){
                                        ghost.setPosition(new Dimension(ghost.getPosition().width + (diffX / Math.abs(diffX)) * cell, ghost.getPosition().height));
                                    } else if(map[ghost.getPosition().getSize().height/cell + (diffY / Math.abs(diffY)) * cell][ghost.getPosition().getSize().width/cell ] != 1){
                                        ghost.setPosition(new Dimension(ghost.getPosition().width, ghost.getPosition().height + (diffY / Math.abs(diffY)) * cell));
                                    }
                                } else{
                                    if(map[ghost.getPosition().getSize().height/cell + (diffY / Math.abs(diffY))][ghost.getPosition().getSize().width/cell ] != 1){
                                        ghost.setPosition(new Dimension(ghost.getPosition().width, ghost.getPosition().height + (diffY / Math.abs(diffY)) * cell));
                                    } else if(map[ghost.getPosition().getSize().height/cell][ghost.getPosition().getSize().width/cell + (diffX / Math.abs(diffX))] != 1){
                                        ghost.setPosition(new Dimension(ghost.getPosition().width + (diffX / Math.abs(diffX)) * cell, ghost.getPosition().height));
                                    }
                                }
                                break;
                            case DEAD:
                        }
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
            goBack();
        });

        return button;
    }

    public void goBack(){
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
            System.out.println("An exception occurred while trying to create new main menu object - " + ex.getMessage());
        }
    }
}
