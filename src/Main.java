import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main{
    public static void main(String[] args) throws Exception {
//        Initialize window
        SwingUtilities.invokeLater(() -> new Window("Pacman Game"));
    }
}