import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Hero extends JLabel{

    public enum Direction{
        LEFT, RIGHT, UP, DOWN
    }

    private Dimension position;
    private final ImageIcon[] framesRight = {
            new ImageIcon("resources/textures/pacman/pacman1.png"),
            new ImageIcon("resources/textures/pacman/pacman2.png"),
            new ImageIcon("resources/textures/pacman/pacman3.png"),
            new ImageIcon("resources/textures/pacman/pacman4.png"),
            new ImageIcon("resources/textures/pacman/pacman5.png"),
            new ImageIcon("resources/textures/pacman/pacman6.png"),

    };
    private final ImageIcon[] framesLeft = {
            new ImageIcon("resources/textures/pacman/backwards/pacman1.png"),
            new ImageIcon("resources/textures/pacman/backwards/pacman2.png"),
            new ImageIcon("resources/textures/pacman/backwards/pacman3.png"),
            new ImageIcon("resources/textures/pacman/backwards/pacman4.png"),
            new ImageIcon("resources/textures/pacman/backwards/pacman5.png"),
            new ImageIcon("resources/textures/pacman/backwards/pacman6.png"),

    };
    private final ImageIcon[] framesDown = {
            new ImageIcon("resources/textures/pacman/downwards/pacman1.png"),
            new ImageIcon("resources/textures/pacman/downwards/pacman2.png"),
            new ImageIcon("resources/textures/pacman/downwards/pacman3.png"),
            new ImageIcon("resources/textures/pacman/downwards/pacman4.png"),
            new ImageIcon("resources/textures/pacman/downwards/pacman5.png"),
            new ImageIcon("resources/textures/pacman/downwards/pacman6.png"),

    };
    private final ImageIcon[] framesUp = {
            new ImageIcon("resources/textures/pacman/upwards/pacman1.png"),
            new ImageIcon("resources/textures/pacman/upwards/pacman2.png"),
            new ImageIcon("resources/textures/pacman/upwards/pacman3.png"),
            new ImageIcon("resources/textures/pacman/upwards/pacman4.png"),
            new ImageIcon("resources/textures/pacman/upwards/pacman5.png"),
            new ImageIcon("resources/textures/pacman/upwards/pacman6.png"),

    };
    private List<ImageIcon> frames;
    private int currentFrame;
    private Direction currentDirection;
    private Timer animClock;
    private int cell;
    private boolean isDead;

    public Hero(Dimension position, int cell){
        super();
        this.position = position;
        this.cell = cell;
        isDead = false;
        setBounds(getPosition().width,getPosition().height,cell,cell);
        currentFrame = 0;
        currentDirection = Direction.RIGHT;

//        load pacman pngs
        frames = new ArrayList<>();
        for(var icon : framesRight){
            frames.add(icon);
        }

//        conduct animation
        animation();

        super.setIcon(frames.get(currentFrame));

        setBackground(Color.BLACK);
    }

    public void animation(){
        Thread animation = new Thread(() -> {
            while(!isDead){
                try{
                    Thread.sleep(75);
                    if(currentFrame == frames.size() - 1) {
                        currentFrame = 0;
                    } else{
                        currentFrame++;
                    }
                    super.setIcon(frames.get(currentFrame));
                    repaint();
                } catch(InterruptedException ex){
                    System.out.println("Animation thread was interrupted - " + ex.getMessage());
                }
            }
        });

        animation.start();
    }

    public Dimension getPosition(){
        return position;
    }

    public Direction getDirection(){
        return currentDirection;
    }

    public void setIsDead(boolean isDead) {
        this.isDead = isDead;
    }

    public void setDirection(Direction direction){
        if(direction != currentDirection){
            frames.clear();
            switch(direction){
                case LEFT:
                    for(var icon : framesLeft){
                        frames.add(icon);
                    }
                    break;
                case RIGHT:
                    for(var icon : framesRight){
                        frames.add(icon);
                    }
                    break;
                case UP:
                    for(var icon : framesUp){
                        frames.add(icon);
                    }
                    break;
                case DOWN:
                    for(var icon : framesDown){
                        frames.add(icon);
                    }
                    break;

            }
            currentDirection = direction;
        }

    }
    public void setPosition(Dimension position){
        this.position = position;
        setBounds(getPosition().width,getPosition().height,cell,cell);
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(frames.get(currentFrame).getIconWidth(), frames.get(currentFrame).getIconHeight());
    }

}
