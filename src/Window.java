import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Window extends JFrame{
    private MainMenu mainMenu;
    public Window(String title) {
        super(title);
        setSize(500, 525);
        getContentPane().setBackground(Color.BLACK);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new MainMenu(this));

    }

}
