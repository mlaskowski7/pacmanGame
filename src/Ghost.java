import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Ghost extends Character {
    private final ImageIcon[] frames1 = {
            new ImageIcon("resources/textures/ghost1/frame1.png"),
            new ImageIcon("resources/textures/ghost1/frame2.png"),
            new ImageIcon("resources/textures/ghost1/frame3.png")
    };
    private final ImageIcon[] frames2 = {
            new ImageIcon("resources/textures/ghost2/frame1.png"),
            new ImageIcon("resources/textures/ghost2/frame2.png"),
            new ImageIcon("resources/textures/ghost2/frame3.png")
    };
    private final ImageIcon[] frames3 = {
            new ImageIcon("resources/textures/ghost3/frame1.png"),
            new ImageIcon("resources/textures/ghost3/frame2.png"),
            new ImageIcon("resources/textures/ghost3/frame3.png")
    };

    public Ghost(Dimension position, int cell, int type) {

        super(position, cell);
        System.out.println("Creating Ghost");
        this.frames = new ArrayList<>();
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
        }

//        conduct animation
        animation();

        setIcon(frames.get(currentFrame));
    }

    public void setState(State state){

    }
}
