/**
 * @author even_and_just
 * @date 3/14/2016
 * @function Tank Game Simplified Version
 */

package tankGame;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;

public class TankGame3 extends JFrame{
    MyPanel mp;

    public static void main(String[] args) {
        TankGame3 tg = new TankGame3();

    }
    public TankGame3(){
        mp = new MyPanel();
        this.add(mp);
        
        // Start the mp thread
        Thread t = new Thread(mp);
        t.start();
        
        this.addKeyListener(mp);
        
        this.setSize(500,430);
        this.setLocation(400, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

}

class MyPanel extends JPanel implements KeyListener,Runnable{
    // Define my tank
    MyTank mytank = null;
    
    // Define enemy tanks
    Vector<EnemyTank> ets = new Vector<EnemyTank>();
    int etSize = 3;
    
    // Define bomb groups
    Vector<Bomb> bms = new Vector<Bomb>();
    
    Image img1 = null;
    Image img2 = null;
    Image img3 = null;
    
    // Constructor function
    public MyPanel(){
        mytank = new MyTank(300,100);  // Original location of my tank
        
        // Initialize enmey tanks
        for(int i=0;i<etSize;i++){
            
            // Create an enemy tank object
            EnemyTank et = new EnemyTank((i+1)*50,0);
            et.setColor(0);
            et.setDirect(2);
            
            // Add an enemy tank
            ets.add(et);
            
            // Start the enemy tanks
            Thread t = new Thread(et);
            t.start();
            
            // Give the enemy tank one bullet
            Shoot s = new Shoot(et.x+10,et.y+30,2);
            et.ss.add(s);
            
            Thread t2 = new Thread(s);
            t2.start();
        }
        
        // Initialize 3 images for bomb effect
        try{
            img1 = ImageIO.read(new File("/bomb1.png"));
            img2 = ImageIO.read(new File("/bomb1.png"));
            img3 = ImageIO.read(new File("/bomb1.png"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void paint(Graphics g){
        super.paint(g);
        
        g.fillRect(0, 0, 500, 430);
        
        // Draw my tank
        if(mytank.isAlive==true){
            this.drawTank(mytank.getX(),mytank.getY(),g,this.mytank.direct,1);
        }
        
        // Draw each bullet in ss
        for(int i=0;i<this.mytank.ss.size();i++){
            
            Shoot myShoot = mytank.ss.get(i);
            
            // Draw bullet, only 1
            if(myShoot != null && myShoot.isAlive == true){
                g.draw3DRect(myShoot.x, myShoot.y, 1, 1, false);
            }
            
            // Remove the bullet if it is dead
            if(myShoot.isAlive == false){
                // It's better to use remove(myShoot) rather than remove(i).
                mytank.ss.remove(myShoot);
            }
            
        }
        
        // Draw bombs
        for(int i=0;i<bms.size();i++){
            Bomb bm = bms.get(i);
            if(bm.life>6){
                g.drawImage(img1, bm.x, bm.y, 30, 30, this);
            }if(bm.life>3){
                g.drawImage(img1, bm.x, bm.y, 30, 30, this);
            }else{
                g.drawImage(img1, bm.x, bm.y, 30, 30, this);
            }
            bm.lifeDown();
            if(bm.life==0){
                bms.remove(bm);
            }
        }
        
        
        // Draw enemy tanks
        for(int i=0;i<ets.size();i++){
            EnemyTank et = ets.get(i);
            if(et.isAlive){
                this.drawTank(et.getX(), et.getY(), g, et.getDirect(), 0);
                for(int j=0;j<et.ss.size();j++){
                    Shoot enemyShoot = et.ss.get(j);
                    if(enemyShoot.isAlive){
                        g.draw3DRect(enemyShoot.x, enemyShoot.y, 1, 1, false);
                    }else{
                        et.ss.remove(enemyShoot);
                    }
                }
            }
        }
        
    }
    
    // Determine whether mytank is hit by enemy tanks
    public void hitMine(){
        for(int i=0;i<ets.size();i++){
            EnemyTank et = ets.get(i);
            
            for(int j=0;j<et.ss.size();j++){
                Shoot enemyShoot = et.ss.get(j);
                if(enemyShoot.isAlive){
                    this.hitTank(enemyShoot, mytank);
                }
            }
        }
    }
    
    // Determine whether my bullet does hit the enemy tanks
    public void hitEnemyTank(){
        for(int i=0;i<mytank.ss.size();i++){
            
            Shoot myShoot = mytank.ss.get(i);
            if(myShoot.isAlive){
                
                for(int j=0;j<ets.size();j++){
                    EnemyTank et = ets.get(j);
                    if(et.isAlive){
                        this.hitTank(myShoot, et);
                    }
                }
            }
        }
    }
    
    // Determine whether the bullet hit the enemy tank
    public void hitTank(Shoot s, Tank et){
        
        // Determine the direction of the tank
        switch(et.direct){
        case 0:
        case 2:
            if(s.x>=et.x && s.x<=et.x+20 && s.y>=et.y && s.y<=et.y+30){
                s.isAlive = false;
                et.isAlive = false;
                Bomb bm = new Bomb(et.x,et.y);
                bms.add(bm);
            }
        case 1:
        case 3:
            if(s.x>=et.x && s.x<=et.x+30 && s.y>=et.y && s.y<=et.y+20){
                s.isAlive = false;
                et.isAlive = false;
                Bomb bm = new Bomb(et.x,et.y);
                bms.add(bm);
            }
        }
        
        
        
    }
    
    public void drawTank(int x, int y, Graphics g, int direct, int type){
        // Determine the type of tank
        switch(type){
        case 0:
            g.setColor(Color.CYAN);
            break;
        case 1:
            g.setColor(Color.YELLOW);
            break;
        }
        
        // Determine the direction
        switch(direct){
        case 0:
            // Draw my tank
            g.fill3DRect(x, y, 5, 30, false);
            g.fill3DRect(x+5, y+5, 10, 20, false);
            g.fill3DRect(x+15, y, 5, 30, false);
            g.fillOval(x+6, y+11, 8, 8);
            g.drawLine(x+10, y, x+10, y+15);
            break;
        case 1:
            g.fill3DRect(x, y, 30, 5, false);
            g.fill3DRect(x+5, y+5, 20, 10, false);
            g.fill3DRect(x, y+15, 30, 5, false);
            g.fillOval(x+11, y+6, 8, 8);
            g.drawLine(x+15, y+10, x+30, y+10);
            break;
        case 2:
            g.fill3DRect(x, y, 5, 30, false);
            g.fill3DRect(x+5, y+5, 10, 20, false);
            g.fill3DRect(x+15, y, 5, 30, false);
            g.fillOval(x+6, y+11, 8, 8);
            g.drawLine(x+10, y+15, x+10, y+30);
            break;
        case 3:
            g.fill3DRect(x, y, 30, 5, false);
            g.fill3DRect(x+5, y+5, 20, 10, false);
            g.fill3DRect(x, y+15, 30, 5, false);
            g.fillOval(x+11, y+6, 8, 8);
            g.drawLine(x, y+10, x+15, y+10);
            break;
        }
    }

    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        if(e.getKeyCode()==KeyEvent.VK_UP){
            this.mytank.setDirect(0);
            this.mytank.moveUp();
        }else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            this.mytank.setDirect(1);
            this.mytank.moveRight();
        }else if(e.getKeyCode()==KeyEvent.VK_DOWN){
            this.mytank.setDirect(2);
            this.mytank.moveDown();
        }else if(e.getKeyCode()==KeyEvent.VK_LEFT){
            this.mytank.setDirect(3);
            this.mytank.moveLeft();
        }
        
        // Shoot
        if(e.getKeyCode()==KeyEvent.VK_SPACE){
            
            // Control the frequency of shooting
            if(this.mytank.ss.size()<5){
                this.mytank.shootEnemy();
            }
        }
        this.repaint();
    }

    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    public void run() {
        while(true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            this.hitEnemyTank();
            this.hitMine();
            
            // Determine whether add new bullet to enemy tanks.
            for(int i=0;i<ets.size();i++){
                EnemyTank et = ets.get(i);
                if(et.isAlive){
                    
                    if(et.ss.size()<2){
                        Shoot s = null;
                        
                        switch(et.direct){
                        case 0:
                            s = new Shoot(et.x+10,et.y,0);
                            et.ss.add(s);
                            break;
                        case 1:
                            s = new Shoot(et.x+30,et.y+10,1);
                            et.ss.add(s);
                            break;
                        case 2:
                            s = new Shoot(et.x+10,et.y+30,2);
                            et.ss.add(s);
                            break;
                        case 3:
                            s = new Shoot(et.x,et.y+10,3);
                            et.ss.add(s);
                            break;
                        }
                        
                        // Activate the bullet
                        Thread t = new Thread(s);
                        t.start();
                    }
                }
            }
            
            this.repaint();
        }
        
    }
}



















