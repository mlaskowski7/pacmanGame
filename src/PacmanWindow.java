import javax.swing.*;
import java.awt.*;

public class PacmanWindow extends JFrame{
    private MainMenu mainMenu;
    public PacmanWindow(String title) {
        super(title);
        setSize(500, 525);
        getContentPane().setBackground(Color.BLACK);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new MainMenu(this));

    }

}
