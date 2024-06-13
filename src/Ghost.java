import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Ghost extends Character {
    private final ImageIcon[] frames1 = {
            new ImageIcon("resources/textures/ghost1/frame1.png"),
            new ImageIcon("resources/textures/ghost1/frame1.png"),
            new ImageIcon("resources/textures/ghost1/frame2.png"),
            new ImageIcon("resources/textures/ghost1/frame2.png"),
            new ImageIcon("resources/textures/ghost1/frame3.png"),
            new ImageIcon("resources/textures/ghost1/frame3.png")
    };
    private final ImageIcon[] frames2 = {
            new ImageIcon("resources/textures/ghost2/frame1.png"),
            new ImageIcon("resources/textures/ghost2/frame1.png"),
            new ImageIcon("resources/textures/ghost2/frame2.png"),
            new ImageIcon("resources/textures/ghost2/frame2.png"),
            new ImageIcon("resources/textures/ghost2/frame3.png"),
            new ImageIcon("resources/textures/ghost2/frame3.png")
    };
    private final ImageIcon[] frames3 = {
            new ImageIcon("resources/textures/ghost3/frame1.png"),
            new ImageIcon("resources/textures/ghost3/frame1.png"),
            new ImageIcon("resources/textures/ghost3/frame2.png"),
            new ImageIcon("resources/textures/ghost3/frame2.png"),
            new ImageIcon("resources/textures/ghost3/frame3.png"),
            new ImageIcon("resources/textures/ghost3/frame3.png"),
    };
    private final ImageIcon[] framesScared = {
            new ImageIcon("resources/textures/ghostScared/frame1.png"),
            new ImageIcon("resources/textures/ghostScared/frame1.png"),
            new ImageIcon("resources/textures/ghostScared/frame2.png"),
            new ImageIcon("resources/textures/ghostScared/frame2.png"),
            new ImageIcon("resources/textures/ghostScared/frame3.png"),
            new ImageIcon("resources/textures/ghostScared/frame3.png"),
    };

    public Ghost(Dimension position, int cell, int type) {

        super(position, cell);
        System.out.println("Creating Ghost");
        this.frames = new ArrayList<>();
        this.movementSpeed = 750;
        this.defaultSpeed = 750;
        changeFramesList(type);

//        conduct animation
        animation();

        setIcon(frames.get(currentFrame));
    }

    private void changeFramesList(int type){
        frames.clear();
        switch (type) {
            case 1:
                for(var icon : frames1){
                    frames.add(icon);
                }
                break;
            case 2:
                for(var icon : frames2){
                    frames.add(icon);
                }
                break;
            case 3:
                for(var icon : frames3){
                    frames.add(icon);
                }
                break;
            case 10:
                for(var icon : framesScared){
                    frames.add(icon);
                }
                break;

        }
    }



    @Override
    public void setState(State state){
        if(state != currentState){
            switch (state){
                case DEAD:
                    changeFramesList(10);
                    isDead = true;
                    isDead = false;
                    animation();
                    break;
                case RIGHT:
                case LEFT:
                case UP:
                case DOWN:
                    changeFramesList((int)(Math.random() * 3 +1));
                    break;
            }
        }
    }
}
