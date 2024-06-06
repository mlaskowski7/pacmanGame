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

public class Hero extends Character{
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

    public Hero(Dimension position, int cell){
        super(position, cell);
//        load pacman pngs
        this.frames = new ArrayList<>();
        for(var icon : framesRight){
            frames.add(icon);
        }
//        conduct animation
        animation();
        setIcon(frames.get(currentFrame));
    }

    public void setState(State state){
        if(state != currentState){
            frames.clear();
            switch(state){
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
            currentState = state;
        }

    }

}
