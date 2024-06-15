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
    private int currentMap;
    private Hero hero;
    private List<Ghost> ghosts;
    private PacmanWindow pacmanWindow;
    private Font font;
    private String currentNickname;
    private UpperPanel upperPanel;
    private BottomPanel bottomPanel;
    private boolean gameStarted;
    private boolean gamePaused;

    public Game(int cell, int[][] map, int currentMap, PacmanWindow pacmanWindow, Font font){
        this.cell = cell;
        this.map = new int[map.length][map[0].length];
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[i].length; j++){
                this.map[i][j] = map[i][j];
            }
        }
        this.currentMap = currentMap;
        this.pacmanWindow = pacmanWindow;
        this.score = 0;
        this.currentNickname = EnterNickname.getCurrentNickname();
        this.highScore = usersFileManipulation.getUsersMap().get(currentNickname);
        this.font = font;
        this.gamePaused = false;


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
        bottomPanel = (map.length > 10) ? new BottomPanel(font,1.0f, () -> goBack(false), hero.getHealthPoints()) : new BottomPanel(font, 0.6f, () -> goBack(false), hero.getHealthPoints()); ;


        pacmanWindow.add(upperPanel, BorderLayout.NORTH);
        pacmanWindow.add(this, BorderLayout.CENTER);
        pacmanWindow.add(bottomPanel, BorderLayout.SOUTH);

        gameStarted = true;
        startGame();
        conductUpgrades();

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
//                        bottomPanel.setMessage("RIGHT");
                        break;
                    case KeyEvent.VK_DOWN:
                        hero.setState(State.DOWN);
//                        bottomPanel.setMessage("DOWN");
                        break;

                    case KeyEvent.VK_LEFT:
                        hero.setState(State.LEFT);
//                        bottomPanel.setMessage("LEFT");
                        break;
                    case KeyEvent.VK_RIGHT:
                        hero.setState(State.RIGHT);
//                        bottomPanel.setMessage("RIGHT");
                        break;
                    case KeyEvent.VK_ESCAPE:
                        goBack(false);
                        break;
                    case KeyEvent.VK_SPACE:
                        togglePause();
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
                    Thread.sleep(hero.movementSpeed);
                    if(!gamePaused){
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
                    }
//                    check if hero is taking points or upgrades
                    switch (map[hero.getPosition().getSize().height/cell][hero.getPosition().getSize().width/cell]){
                        case 2:
                            eatPoint();
                            break;
                        case 3:
                            increaseHeroSpeed();
                            bottomPanel.setMessage("BOOST");
                            break;
                        case 4:
                            increaseGhostsSpeed();
                            bottomPanel.setMessage("GHOSTS BOOST");
                            break;
                        case 5:
                            rage();
                            bottomPanel.setMessage("RAGE");
                            break;
                        case 6:
                            additionalGhosts();
                            bottomPanel.setMessage("NEW GHOSTS");
                            break;
                        case 7:
                            teleportPacman();
                            bottomPanel.setMessage("TELEPORT");
                            break;
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
                    Thread.sleep(ghosts.get(0).movementSpeed);
                    if(hero.getCurrentState() != State.DEAD && !gamePaused){
                        for(int i = 0; i < ghosts.size(); i++){
                            int diffX = hero.getPosition().height - ghosts.get(i).getPosition().height;
                            int diffY = hero.getPosition().width - ghosts.get(i).getPosition().width;
                            if(Math.abs(diffX) <= cell && Math.abs(diffY) <= cell){
                                if(!hero.isRaged){
                                    hero.kill();
                                    bottomPanel.removeHeart();
                                    bottomPanel.setMessage("KILLED PACMAN");
                                    if(hero.getHealthPoints() <= 0){
                                        goBack(true);
                                    }
                                } else{
                                    remove(ghosts.get(i));
                                    ghosts.remove(i);
                                    bottomPanel.setMessage("KILLED GHOST");
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
                } catch (IndexOutOfBoundsException ex) {

                }
            }
        });

        pacmanMovement.start();
        ghostsMovement.start();

    }

    private void conductUpgrades(){
         var upgradesThread = new Thread(() -> {
            while(gameStarted && !hero.isDead){
                try{
                    Thread.sleep(5000);
                    for(int i = 0; i < ghosts.size(); i++){
                        if((int)(Math.random() * 4) == 0) {
                            map[ghosts.get(i).getPosition().getSize().height / cell][ghosts.get(i).getPosition().getSize().width / cell] = (int) (Math.random() * 5) + 3;
                        }
                    }
                    repaint();

                } catch (InterruptedException ex) {
                    System.out.println("upgrading thread was interrupted - " + ex.getMessage());
                }
            }
        });

         upgradesThread.start();

    }

    private void eatPoint(){
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

    private void increaseHeroSpeed(){
//        increase hero speed for 7 secs
        System.out.println("increase hero speed");
        var boostedHeroSpeedThread = new Thread(() ->{
            try{
                map[hero.getPosition().getSize().height/cell][hero.getPosition().getSize().width/cell] = 0;
                repaint();
                hero.movementSpeed -= 150;
                Thread.sleep(7000);
                hero.movementSpeed += 150;
            } catch (InterruptedException ex){
                System.out.println("boosted speed thread interrupted - " + ex.getMessage());
            }
        });
        if(hero.isDefaultSpeed()){
            boostedHeroSpeedThread.start();
        }
    }

    private void increaseGhostsSpeed(){
//        increase ghost speed for 7 secs
        System.out.println("increase ghosts speed");
        var boostedGhostsSpeedThread = new Thread(() ->{
            try{
                map[hero.getPosition().getSize().height/cell][hero.getPosition().getSize().width/cell] = 0;
                repaint();
                for(int i = 0; i < ghosts.size(); i++){
                    ghosts.get(i).movementSpeed -= 150;
                }
                Thread.sleep(7000);
                for(int i = 0; i < ghosts.size(); i++){
                    ghosts.get(i).movementSpeed += 150;
                }
            } catch (InterruptedException ex){
                System.out.println("boosted speed thread interrupted - " + ex.getMessage());
            }
        });
        if(!ghosts.isEmpty() && ghosts.get(0).isDefaultSpeed()){
            boostedGhostsSpeedThread.start();
        }
    }

    private synchronized void rage(){
        System.out.println("rage");
        var rageThread = new Thread(() -> {
            try{
                map[hero.getPosition().getSize().height/cell][hero.getPosition().getSize().width/cell] = 0;
                hero.isRaged = true;
                hero.movementSpeed -= 150;
                for(int i = 0; i < ghosts.size(); i++){
                    ghosts.get(i).setState(State.DEAD);
                }
                Thread.sleep(7000);
                for(int i = 0; i < ghosts.size(); i++){
                    ghosts.get(i).setState(State.RIGHT);
                }
                hero.movementSpeed += 150;
                hero.isRaged = false;
            } catch (InterruptedException ex){
                System.out.println("rage thread was interrupted - " + ex.getMessage());
            }
        });

        var rageWithoutSpeedThread = new Thread(() -> {
            try{
                map[hero.getPosition().getSize().height/cell][hero.getPosition().getSize().width/cell] = 0;
                hero.isRaged = true;
                for(int i = 0; i < ghosts.size(); i++){
                    ghosts.get(i).setState(State.DEAD);
                }
                Thread.sleep(7000);
                for(int i = 0; i < ghosts.size(); i++){
                    ghosts.get(i).setState(State.RIGHT);
                }
                hero.isRaged = false;
            } catch (InterruptedException ex){
                System.out.println("rage thread was interrupted - " + ex.getMessage());
            }
        });

        if(hero.isDefaultSpeed()){
            rageThread.start();
        } else{
            rageWithoutSpeedThread.start();
        }
    }

    private void additionalGhosts(){
        map[hero.getPosition().getSize().height/cell][hero.getPosition().getSize().width/cell] = 0;
        for(int i = 0; i < (int)(Math.random()*4) + 1; i++){
            ghosts.add(new Ghost(randomPosition(), cell, (int)(Math.random() * 3 +1)));
            add(ghosts.get(ghosts.size() - 1));
        }
    }

    private void teleportPacman(){
        map[hero.getPosition().getSize().height/cell][hero.getPosition().getSize().width/cell] = 0;
        hero.setPosition(randomPosition());
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

    private void togglePause(){
        if(gamePaused){
            gamePaused = false;
        } else{
            gamePaused = true;
        }

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
            if(isOver)
                selectMap.displayGameOver();
            pacmanWindow.revalidate();
            pacmanWindow.repaint();
        } catch (Exception ex) {
            System.out.println("An exception occurred while trying to create new select map object - " + ex.getMessage());
        }
    }

    private boolean canMove(int i, int j){
        if(i >= map.length || j >= map[0].length || i < 0 || j < 0 || map[i][j] == 1 )
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
                } else if(map[i][j] == 3){
                    g.setColor(Color.RED);
                    g.fillRect(j*cell + cell / 2, i*cell + cell / 2, cell/3, cell/3);
                } else if(map[i][j] == 4){
                    g.setColor(Color.GREEN);
                    g.fillRect(j*cell + cell / 2, i*cell + cell / 2, cell/3, cell/3);
                } else if(map[i][j] == 5){
                    g.setColor(Color.CYAN);
                    g.fillRect(j*cell + cell / 2, i*cell + cell / 2, cell/3, cell/3);
                } else if(map[i][j] == 6){
                    g.setColor(Color.BLUE);
                    g.fillRect(j*cell + cell / 2, i*cell + cell / 2, cell/3, cell/3);
                } else if(map[i][j] == 7){
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(j*cell + cell / 2, i*cell + cell / 2, cell/3, cell/3);
                }
            }
        }

    }
}
