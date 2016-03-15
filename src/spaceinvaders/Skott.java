/*
Här är Skott klassen man skjuter med ALT knappen.  H_Space och V_Space gör 
så att skotten kommer ut vid rätt position
 */
package spaceinvaders;

import javax.swing.ImageIcon;


public class Skott extends Bilder {

    private String skott = "../spacepix/shot.png";
    private final int H_SPACE = 6;
    private final int V_SPACE = 1;

    public Skott() {
    }

    public Skott(int x, int y) {

        ImageIcon ii = new ImageIcon(this.getClass().getResource(skott));
        setImage(ii.getImage());
        setX(x + H_SPACE);
        setY(y - V_SPACE);
    }
}