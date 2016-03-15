/*
Pontus project i programering
 */
package spaceinvaders;

import javax.swing.JFrame;

public class Spaceinvaders extends JFrame implements Commons {

    public Spaceinvaders()
    {
        add(new Spelplan());
        setTitle("Space Invaders");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(BOARD_WIDTH, BOARD_HEIGTH);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }
/*Det här är main klassen*/
    public static void main(String[] args) {
        new Spaceinvaders();
    }
}