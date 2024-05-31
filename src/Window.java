import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    public Window(String title) {
        super(title);
        setSize(500, 525);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
