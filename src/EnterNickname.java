import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class EnterNickname extends JPanel {
    private Font font;
    private final Window window;
    private static String currentNickname;

    public EnterNickname(Window window, Font font){

        super();
        this.window = window;
        this.font = font;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);

        add(Box.createVerticalGlue());
        add(createHeaderLabel());
        add(Box.createVerticalStrut(5));
        add(createSubHeaderLabel());
        add(Box.createVerticalStrut(15));
        add(createNicknameField());
        add(Box.createVerticalGlue());

    }

    public static String getCurrentNickname() {
        return currentNickname;
    }

    public JLabel createHeaderLabel(){
        var header = new JLabel("Enter your nickname:");
        header.setFont(font.deriveFont(Font.BOLD,28f));
        header.setAlignmentX(CENTER_ALIGNMENT);
        header.setForeground(new Color(255,0,255));

        return header;
    }

    public JLabel createSubHeaderLabel(){
        var subHeader = new JLabel("Confirm by pressing enter");
        subHeader.setFont(font.deriveFont(Font.BOLD,20f));
        subHeader.setAlignmentX(CENTER_ALIGNMENT);
        subHeader.setForeground(Color.GRAY);

        return subHeader;
    }

    public JTextField createNicknameField(){
        var nicknameField = new JTextField();
        nicknameField.setMaximumSize(new Dimension(200,50));
        var nicknamePanel = this;
        nicknameField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    System.out.println("enter clicked - user typed in his nickname");
                    currentNickname = nicknameField.getText();
                    usersFileManipulation.uploadUser(nicknameField.getText());
                    window.remove(nicknamePanel);
                    window.add(new SelectMap(window,font));
                    window.revalidate();
                    window.repaint();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        return nicknameField;
    }


}
