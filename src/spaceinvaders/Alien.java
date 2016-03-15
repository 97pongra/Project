/*
Det här är alien karaktären varje alien har en bomb klass.
 */
package spaceinvaders;

import javax.swing.ImageIcon;


public class Alien extends Bilder {

    private Bomb bomb;
    private final String shot = "../spacepix/alien.png";

    public Alien(int x, int y) {
        this.x = x;
        this.y = y;

        bomb = new Bomb(x, y);
        ImageIcon ii = new ImageIcon(this.getClass().getResource(shot));
        setImage(ii.getImage());

    }

    public void act(int direction) {
        this.x += direction;
    }
/*Här kallar vi på Spelplan klassen för att positionera alienen i en 
horisontell riktning*/
    public Bomb getBomb() {
        return bomb;
    }
/*Denna klassen kallas på när en alien ska skjuta ett skott.*/
    public class Bomb extends Bilder {

        private final String bomb = "../spacepix/bomb.png";
        private boolean destroyed;

        public Bomb(int x, int y) {
            setDestroyed(true);
            this.x = x;
            this.y = y;
            ImageIcon ii = new ImageIcon(this.getClass().getResource(bomb));
            setImage(ii.getImage());
        }

        public void setDestroyed(boolean destroyed) {
            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {
            return destroyed;
        }
    }
}