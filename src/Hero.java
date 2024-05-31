import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Hero extends JPanel {
    private Dimension position;
    private List<BufferedImage> frames;
    private int currentFrame;
    private Timer animClock;

    public Hero(Dimension position){
        this.position = position;
        currentFrame = 0;

//        load pacman pngs
        frames = new ArrayList<>();
        try{
            frames.add(ImageIO.read(new File("resources/textures/pacman/pacman1.png")));
            frames.add(ImageIO.read(new File("resources/textures/pacman/pacman2.png")));
            frames.add(ImageIO.read(new File("resources/textures/pacman/pacman3.png")));
            frames.add(ImageIO.read(new File("resources/textures/pacman/pacman4.png")));
            frames.add(ImageIO.read(new File("resources/textures/pacman/pacman5.png")));
            frames.add(ImageIO.read(new File("resources/textures/pacman/pacman6.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        conduct animation
        animClock = new Timer(100, e -> {
            if(currentFrame == frames.size() - 1) {
                currentFrame = 0;
            } else{
                currentFrame++;
            }
            repaint();
        });
        animClock.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(frames.get(currentFrame), position.width, position.height, 25, 25, this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(position.width, position.height);
    }

}
