import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Game extends JPanel {
    private int cell;
    private int[][] map;
    private Hero hero;
    private List<Ghost> ghosts;

    public Game(int cell, int[][] map){
        this.cell = cell;
        this.map = map;
        setBackground(Color.BLACK);
        setLayout(null);

        hero = new Hero(new Dimension(0,cell*2), cell);
        add(hero);
        hero.requestFocusInWindow();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[i].length; j++){
                if(map[i][j] == 1){
                    g.setColor(new Color(48, 25, 52));
                    g.drawRect(j*cell, i*cell, cell, cell);
                } else if(map[i][j] == 0){
                    g.setColor(Color.BLACK);
                    g.fillRect(j*cell, i*cell, cell, cell);
                } else if(map[i][j] == 2){
                    g.setColor(Color.WHITE);
                    g.fillRect(j*cell + cell / 2, i*cell + cell / 2, cell/10, cell/10);
                }
            }
        }

    }
}
