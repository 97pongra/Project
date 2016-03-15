/*
Här har vi den största klassen som gör så att spelet kan fungera.
 */
package spaceinvaders;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class Spelplan extends JPanel implements Runnable, Commons { 

    private Dimension d;
    private ArrayList aliens;
    private Spelare spelare;
    private Skott skott;

    private int alienX = 150;
    private int alienY = 5;
    private int direction = -1;
    private int deaths = 0;

    private boolean ingame = true;
    private final String expl = "../spacepix/explosion.png";
    private final String alienpix = "../spacepix/alien.png";
    private String message = "Game Over";

    private Thread animator;

    public Spelplan() 
    {

        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGTH);
        setBackground(Color.black);

        gameInit();
        setDoubleBuffered(true);
    }

    public void addNotify() {
        super.addNotify();
        gameInit();
    }

    public void gameInit() {

        aliens = new ArrayList();

        ImageIcon ii = new ImageIcon(this.getClass().getResource(alienpix));

        for (int i=0; i < 4; i++) {
            for (int j=0; j < 6; j++) {
                Alien alien = new Alien(alienX + 18*j, alienY + 18*i);
                alien.setImage(ii.getImage());
                aliens.add(alien);
            }
        }

        spelare = new Spelare();
        skott = new Skott();
/*
Här sätter vi ut 24 stycken Aliens vi bestämmer att dom ska vara 12X12px 
stora och att det ska vara 6 px mellan dom, 
Vi skapar också spelaren och skott objecten
*/
        if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
        }
    }

    public void drawAliens(Graphics g) 
    {
        Iterator it = aliens.iterator();

        while (it.hasNext()) {
            Alien alien = (Alien) it.next();

            if (alien.isVisible()) {
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            if (alien.isDying()) {
                alien.die();
            }
        }
    }

    public void drawSpelare(Graphics g) {

        if (spelare.isVisible()) {
            g.drawImage(spelare.getImage(), spelare.getX(), spelare.getY(), this);
        }

        if (spelare.isDying()) {
            spelare.die();
            ingame = false;
        }
    }

    public void drawSkott(Graphics g) {
        if (skott.isVisible())
            g.drawImage(skott.getImage(), skott.getX(), skott.getY(), this);
    }

    public void drawBombing(Graphics g) {

        Iterator i3 = aliens.iterator();

        while (i3.hasNext()) {
            Alien a = (Alien) i3.next();

            Alien.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {
                g.drawImage(b.getImage(), b.getX(), b.getY(), this); 
            }
        }
    }
/*
Här skapas små object som används för att rita ut spelare,alien och spelarens skott.
drawBombing ritar dock ut bomberna som aliens skjuter.
*/
    public void paint(Graphics g)
    {
      super.paint(g);

      g.setColor(Color.black);
      g.fillRect(0, 0, d.width, d.height);
      g.setColor(Color.green);   

      if (ingame) {

        g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
        drawAliens(g);
        drawSpelare(g);
        drawSkott(g);
        drawBombing(g);
      }

      Toolkit.getDefaultToolkit().sync();
      g.dispose();
    }

    public void gameOver()
    {

        Graphics g = this.getGraphics();

        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGTH);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH/2 - 30, BOARD_WIDTH-100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH/2 - 30, BOARD_WIDTH-100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message))/2, 
            BOARD_WIDTH/2);
    }
/*
Det är här vi betämmer NÄR allting ska ritas ut.
Här ritar vi upp bakgrunden och hur den ska reagera på när de nya objekten
skapas, Vi gör även en fin GAME OVER skärm.    
*/
    public void animationCycle()  {

        if (deaths == NUMBER_OF_ALIENS_TO_DESTROY) {
            ingame = false;
            message = "Game won!";
        }
/*Om vi förstört alla aliens vinner vi spelet */
        // player

        spelare.act();

        // shot
        if (skott.isVisible()) {
            Iterator it = aliens.iterator();
            int shotX = skott.getX();
            int shotY = skott.getY();

            while (it.hasNext()) {
                Alien alien = (Alien) it.next();
                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && skott.isVisible()) {
                    if (shotX >= (alienX) && 
                        shotX <= (alienX + ALIEN_WIDTH) &&
                        shotY >= (alienY) &&
                        shotY <= (alienY+ALIEN_HEIGHT) ) {
                            ImageIcon ii = 
                                new ImageIcon(getClass().getResource(expl));
                            alien.setImage(ii.getImage());
                            alien.setDying(true);
                            deaths++;
                            skott.die();
                        }
                }
            }
/*Om vi skjuter en alien så flaggas det skeppet som att det är förstört räknar 
ut att ett till alien skepp förstörts och slutar sicka ut bomber från
det skeppet och gör det osynligt
*/
            int y = skott.getY();
            y -= 4;
            if (y < 0)
                skott.die();
            else skott.setY(y);
        }

        // aliens

         Iterator it1 = aliens.iterator();

         while (it1.hasNext()) {
             Alien a1 = (Alien) it1.next();
             int x = a1.getX();

             if (x  >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {
                 direction = -1;
                 Iterator i1 = aliens.iterator();
                 while (i1.hasNext()) {
                     Alien a2 = (Alien) i1.next();
                     a2.setY(a2.getY() + GO_DOWN);
                 }
             }
/*
Om aliens kommer längst till höger så åker dom ett snäpp ner och 
byter riktning
*/
            if (x <= BORDER_LEFT && direction != 1) {
                direction = 1;

                Iterator i2 = aliens.iterator();
                while (i2.hasNext()) {
                    Alien a = (Alien)i2.next();
                    a.setY(a.getY() + GO_DOWN);
                }
            }
        }


        Iterator it = aliens.iterator();

        while (it.hasNext()) {
            Alien alien = (Alien) it.next();
            if (alien.isVisible()) {

                int y = alien.getY();

                if (y > GROUND - ALIEN_HEIGHT) {
                    ingame = false;
                    message = "Invasion!";
                }

                alien.act(direction);
            }
        }
/*Här flyttar vi på aliens och om dom når botten så börjar invasionen.*/
        // bombs

        Iterator i3 = aliens.iterator();
        Random generator = new Random();

        while (i3.hasNext()) {
            int shot = generator.nextInt(15);
            Alien a = (Alien) i3.next();
            Alien.Bomb b = a.getBomb();
            if (shot == CHANCE && a.isVisible() && b.isDestroyed()) {

                b.setDestroyed(false);
                b.setX(a.getX());
                b.setY(a.getY());   
            }
/*
Här bestämms det om en bomb slåpps eller inte från en alien, skeppet får inte 
vara flaggat som förstört eller ha en tidigare släppt bomb fortfarande på 
skärmen och om detta är sant så är det upp till CHANCE om det skjuter eller inte
*/
            int bombX = b.getX();
            int bombY = b.getY();
            int spelareX = spelare.getX();
            int spelareY = spelare.getY();

            if (spelare.isVisible() && !b.isDestroyed()) {
                if ( bombX >= (spelareX) && 
                    bombX <= (spelareX+PLAYER_WIDTH) &&
                    bombY >= (spelareY) && 
                    bombY <= (spelareY+PLAYER_HEIGHT) ) {
                        ImageIcon ii = 
                            new ImageIcon(this.getClass().getResource(expl));
                        spelare.setImage(ii.getImage());
                        spelare.setDying(true);
                        b.setDestroyed(true);;
                    }
            }

            if (!b.isDestroyed()) {
                b.setY(b.getY() + 1);   
                if (b.getY() >= GROUND - BOMB_HEIGHT) {
                    b.setDestroyed(true);
                }
            }
        }
    }
/*
Om en bomb inte är förstörd så åker den mot marken, om den når botten förstörs 
den och en ny bomb kan skjutas från alien.
*/
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (ingame) {
            repaint();
            animationCycle();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) 
                sleep = 2;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
            beforeTime = System.currentTimeMillis();
        }
        gameOver();
    }

    private class TAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            spelare.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) {

          spelare.keyPressed(e);

          int x = spelare.getX();
          int y = spelare.getY();

          if (ingame)
          {
            if (e.isAltDown()) {
                if (!skott.isVisible())
                    skott = new Skott(x, y);
            }
          }
        }
    }
}