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

public class Hero extends JPanel{
    private Dimension position;
    private List<BufferedImage> frames;
    private int currentFrame;
    private Timer animClock;
    private int cell;

    public Hero(Dimension position, int cell){
        this.position = position;
        this.cell = cell;
        setBounds(getPosition().width,getPosition().height,cell,cell);
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

        setBackground(Color.BLACK);
    }

    public Dimension getPosition(){
        return position;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(frames.get(currentFrame), 0, 0, getWidth(), getHeight(),  this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(frames.get(currentFrame).getWidth(), frames.get(currentFrame).getWidth());
    }

}
