/*
Här är spelar klassen den kontroleras med pil tangenterna Höger och vänster
*/
package spaceinvaders;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;


public class Spelare extends Bilder implements Commons{

    private final int START_Y = 280; 
    private final int START_X = 270;
/*Här är start kordinaterna för spelaren*/
    private final String spelare = "../spacepix/player.png";
    private int width;

    public Spelare() {

        ImageIcon ii = new ImageIcon(this.getClass().getResource(spelare));

        width = ii.getImage().getWidth(null); 

        setImage(ii.getImage());
        setX(START_X);
        setY(START_Y);
    }

    public void act() {
        x += dx;
        if (x <= 2) 
            x = 2;
        if (x >= BOARD_WIDTH - 2*width) 
            x = BOARD_WIDTH - 2*width;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT)
        {
            dx = -2;
        }

        if (key == KeyEvent.VK_RIGHT)
        {
            dx = 2;
        }
    }
/*När vi trycker på vänster pil så sätts värdet till -2 och act metoden kallas
  och spelaren rör sig åt vänster*/
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT)
        {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT)
        {
            dx = 0;
        }
/*Om vi släper upp vänster eller höger pil tangent så sätts dx till 0 och
spelaren stannar*/  
    }
}