import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) throws Exception {
//        Creating window JFrame
        JFrame window = new JFrame("Pacman");
        window.setSize(1080,1080);
        window.setLayout(new BorderLayout());
        window.getContentPane().setBackground(Color.black);

//        Initialize main menu
        MainMenu.setFont("resources/RobotoMonoFont/RobotoMono-VariableFont_wght.ttf");
        MainMenu.init(window);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}