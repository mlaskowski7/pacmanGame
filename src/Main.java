import javax.swing.*;

public class Main{
    public static void main(String[] args) throws Exception {
//        Initialize window
        SwingUtilities.invokeLater(() -> new PacmanWindow("Pacman Game"));
    }
}