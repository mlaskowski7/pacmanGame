import javax.swing.*;
import javax.swing.border.Border;
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
    private JPanel bottomPanel;
    private boolean gameStarted;

    public Game(int cell, int[][] map, PacmanWindow pacmanWindow, Font font){
        this.cell = cell;
        this.map = new int[map.length][map[0].length];
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[i].length; j++){
                this.map[i][j] = map[i][j];
            }
        }
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

        ghosts = new ArrayList<>();
        for(int i = 0; i < (int)(Math.random()*6) + 1; i++){
            ghosts.add(new Ghost(randomPosition(), cell, (int)(Math.random() * 3 +1)));
        }
        for(Ghost ghost : ghosts){
            add(ghost);
            System.out.println("should add ghost");
        }

        upperPanel = (map.length > 10) ? new UpperPanel(font,1.0f, currentNickname) : new UpperPanel(font, 0.6f, currentNickname);
        bottomPanel = bottomPanel();


        pacmanWindow.add(upperPanel, BorderLayout.NORTH);
        pacmanWindow.add(this, BorderLayout.CENTER);
        pacmanWindow.add(bottomPanel, BorderLayout.SOUTH);

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
                        goBack(false);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}

        });

    }

    private void startGame(){
        Thread pacmanMovement = new Thread(() -> {
            while(gameStarted){
                try{
                    Thread.sleep(350);

                    switch(hero.getCurrentState()){
                        case UP:
                            if(canMove(hero.getPosition().getSize().height/cell - 1, hero.getPosition().getSize().width/cell)){
                                hero.setPosition(new Dimension(hero.getPosition().width, hero.getPosition().height - cell));
                            }
                            break;
                        case DOWN:
                            if(canMove(hero.getPosition().getSize().height/cell + 1, hero.getPosition().getSize().width/cell)){
                                hero.setPosition(new Dimension(hero.getPosition().width, hero.getPosition().height + cell));
                            }
                            break;
                        case LEFT:
                            if(canMove(hero.getPosition().getSize().height/cell, hero.getPosition().getSize().width/cell - 1)){
                                hero.setPosition(new Dimension(hero.getPosition().width - cell, hero.getPosition().height));
                            }
                            break;
                        case RIGHT:
                            if(canMove(hero.getPosition().getSize().height/cell, hero.getPosition().getSize().width/cell + 1)){
                                hero.setPosition(new Dimension(hero.getPosition().width + cell, hero.getPosition().height));
                            }
                            break;
                        case DEAD:
                            Thread.sleep(500);
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

        var ghostsMovement = new Thread(() -> {
            while(gameStarted){
                try{
                    Thread.sleep(750);
                    if(hero.getCurrentState() != State.DEAD){
                        for(int i = 0; i < ghosts.size(); i++){
                            int diffX = hero.getPosition().height - ghosts.get(i).getPosition().height;
                            int diffY = hero.getPosition().width - ghosts.get(i).getPosition().width;
                            if(Math.abs(diffX) <= cell && Math.abs(diffY) <= cell){
                                System.out.println("collision with ghost detected");
                                hero.kill();
                                changeBottomPanel();
                                if(hero.getHealthPoints() <= 0){
                                    goBack(true);
                                }


                            }
                            switch (ghosts.get(i).getCurrentState()){
                                case UP:
                                case DOWN:
                                case LEFT:
                                case RIGHT:
                                    if(hero.getCurrentState() != State.DEAD){
                                        if(Math.abs(diffX) > Math.abs(diffY)){
                                            if(diffX != 0 && canMove(ghosts.get(i).getPosition().height/cell + (diffX / Math.abs(diffX)),ghosts.get(i).getPosition().width/cell)){
                                                var potentialPosition = new Dimension(ghosts.get(i).getPosition().width, ghosts.get(i).getPosition().height + (diffX / Math.abs(diffX)) * cell);
                                                if(!checkIfStacked(potentialPosition))
                                                    ghosts.get(i).setPosition(potentialPosition);
                                            } else if(diffY != 0 && canMove(ghosts.get(i).getPosition().height/cell,ghosts.get(i).getPosition().width/cell + (diffY / Math.abs(diffY)))){
                                                var potentialPosition = new Dimension(ghosts.get(i).getPosition().width  + (diffY / Math.abs(diffY) * cell), ghosts.get(i).getPosition().height);
                                                if(!checkIfStacked(potentialPosition))
                                                    ghosts.get(i).setPosition(potentialPosition);
                                            }
                                        } else{
                                            if(diffY != 0 && canMove(ghosts.get(i).getPosition().height/cell,ghosts.get(i).getPosition().width/cell + (diffY / Math.abs(diffY)))){
                                                var potentialPosition = new Dimension(ghosts.get(i).getPosition().width  + (diffY / Math.abs(diffY) * cell), ghosts.get(i).getPosition().height);
                                                if(!checkIfStacked(potentialPosition))
                                                    ghosts.get(i).setPosition(potentialPosition);
                                            } else if(diffX != 0 && canMove(ghosts.get(i).getPosition().height/cell + (diffX / Math.abs(diffX)),ghosts.get(i).getPosition().width/cell)){
                                                var potentialPosition = new Dimension(ghosts.get(i).getPosition().width, ghosts.get(i).getPosition().height + (diffX / Math.abs(diffX)) * cell);
                                                if(!checkIfStacked(potentialPosition))
                                                    ghosts.get(i).setPosition(potentialPosition);
                                            }
                                        }
                                    }

                                    break;
                                case DEAD:
                            }
                        }
                    } else{
                        for(int i = 0; i < ghosts.size(); i++){
                            ghosts.get(i).setPosition(randomPosition());
                        }
                    }
                } catch (InterruptedException ex) {
                    System.out.println("Ghosts thread was interrupted - " + ex.getMessage());
                }
            }
        });

        pacmanMovement.start();
        ghostsMovement.start();

    }

    private Dimension randomPosition(){
        var positionX = (int) (Math.random() * map.length);
        var positionY = (int) (Math.random() * map[0].length);
        while(!canMove(positionX, positionY)){
            positionX = (int) (Math.random() * map.length);
            positionY = (int) (Math.random() * map[0].length);
        }
        return new Dimension(positionY*cell, positionX*cell);
    }

    private JPanel bottomPanel(){
        var panel = new JPanel(new GridLayout(0,4));
        var button = backButton();
        panel.setBackground(new Color(54, 2, 54));
        panel.add(button);
        System.out.println("bottom panel() -> health: " + hero.getHealthPoints());
        for(int i = 0; i < hero.getHealthPoints(); i++){
            var heartLabel = new JLabel();
//            heart texture was generated by DALL-E model
            var heartIcon = new ImageIcon("resources/textures/heart.png");
            heartLabel.setIcon(heartIcon);
            panel.add(heartLabel);
        }

        return panel;
    }

    private void changeBottomPanel(){
        bottomPanel.removeAll();
        var button = backButton();
        bottomPanel.add(button);
        System.out.println("bottom panel() -> health: " + hero.getHealthPoints());
        for(int i = 0; i < hero.getHealthPoints(); i++){
            var heartLabel = new JLabel();
//            heart texture was generated by DALL-E model
            var heartIcon = new ImageIcon("resources/textures/heart.png");
            heartLabel.setIcon(heartIcon);
            bottomPanel.add(heartLabel);
        }

        bottomPanel.revalidate();
    }


    private JButton backButton(){
        var button = new JButton("Back");
        button.setBackground(Color.BLACK);
        button.setSize(pacmanWindow.getWidth(),7);
        button.addActionListener(e -> {
            goBack(false);
        });

        return button;
    }

    private void goBack(boolean isOver){
        try{
            gameStarted = false;
            hero.setIsDead(true);
            pacmanWindow.remove(upperPanel);
            pacmanWindow.remove(this);
            pacmanWindow.remove(bottomPanel);
            pacmanWindow.setSize(500,525);
            var selectMap = new SelectMap(pacmanWindow, font);
            pacmanWindow.add(selectMap);
            selectMap.displayGameOver();
            pacmanWindow.revalidate();
            pacmanWindow.repaint();
        } catch (Exception ex) {
            System.out.println("An exception occurred while trying to create new main menu object - " + ex.getMessage());
        }
    }

    private boolean canMove(int i, int j){
        if(map[i][j] == 1 || i >= map.length || j >= map[0].length || i < 0 || j < 0)
            return false;
        else
            return true;
    }

    private boolean checkIfStacked(Dimension position){
        boolean stacked = false;
        for(int i = 0; i < ghosts.size(); i++){
            if(position.width == ghosts.get(i).getPosition().width && ghosts.get(i).getPosition().height == position.height){
                stacked = true;
                break;
            }
        }
        return stacked;
    }


    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[i].length; j++){
                if(map[i][j] == 1){
                    g.setColor(new Color(54, 2, 54));
                    g.fillRect(j*cell, i*cell, cell, cell);
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
