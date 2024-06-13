import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class Character extends JLabel {
    Dimension position;
    List<ImageIcon> frames;
    int currentFrame;
    State currentState;
    int cell;
    boolean isDead;

    public Character(Dimension position, int cell) {
        super();
        this.position = position;
        this.cell = cell;
        isDead = false;
        setBounds((int)position.getWidth(), (int)position.getHeight(), cell, cell);
        currentFrame = 0;
        currentState = State.RIGHT;

        setBackground(Color.BLACK);

    }

    public synchronized State getCurrentState() {
        return currentState;
    }

    public void setIsDead(boolean isDead) {
        this.isDead = isDead;
    }

    public Dimension getPosition() {
        return position;
    }

    public void setPosition(Dimension position){
        this.position = position;
        setBounds((int)position.getWidth(),(int)position.getHeight(),cell,cell);
        repaint();
    }

    public abstract void setState(State state);

    public void animation(){
        Thread animation = new Thread(() -> {
            while(!isDead){
                try{
                    if(currentState != State.DEAD)
                        Thread.sleep(75);
                    else
                        Thread.sleep(180);
                    if(currentFrame == frames.size() - 1) {
                        currentFrame = 0;
                    } else{
                        currentFrame++;
                    }
                    setIcon(frames.get(currentFrame));
                    repaint();
                } catch(InterruptedException ex){
                    System.out.println("Animation thread was interrupted - " + ex.getMessage());
                }
            }
        });

        animation.start();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(frames.get(currentFrame).getIconWidth(), frames.get(currentFrame).getIconHeight());
    }

}
