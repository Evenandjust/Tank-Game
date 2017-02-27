package tankGameVersion3;

/**
 * @author even_and_just
 * @date 3/15/2016
 * @function Members of Tank Game
 */

import java.util.*;

// Bullet class
class Shoot implements Runnable{
    int x;
    int y;
    int direct;
    int speed = 10;
    
    // Status of bullet
    boolean isAlive = true;
    
    public Shoot(int x, int y, int direct){
        this.x = x;
        this.y = y;
        this.direct = direct;
    }
    
    public void run(){
        while(true){
            
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            switch(direct){
            case 0:
                y -= speed;
                break;
            case 1:
                x += speed;
                break;
            case 2:
                y += speed;
                break;
            case 3:
                x -= speed;
                break;
            }
            
            // Determine whether the bullet hits the border of the window
            if(x<0||x>500||y<0||y>430){
                this.isAlive = false;
                break;
            }
        }
    }
}

class Tank{
    // Tank's location
    int x = 0;
    int y = 0;
    
    // Tank's direction, 0---up,1---right,2---down,3---left
    int direct = 0;
    
    // Tank's speed
    int speed = 5;
    
    // Tank's color
    int color;
    
    // Tank's status
    boolean isAlive = true;
    
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public Tank(int x, int y){
        this.x = x;
        this.y = y;
    }
    
}

class MyTank extends Tank{
    
    Shoot s = null;
    Vector<Shoot> ss = new Vector<Shoot>();
    
    
    public MyTank(int x, int y){
        super(x,y);
        
    }
    
    public void shootEnemy(){
        
        
        switch(this.direct){
        case 0:
            s = new Shoot(x+10, y, 0);
            ss.add(s);
            break;
        case 1:    
            s = new Shoot(x+30, y+10, 1);
            ss.add(s);
            break;
        case 2:
            s = new Shoot(x+10, y+30, 2);
            ss.add(s);
            break;
        case 3:
            s = new Shoot(x, y+10, 3);
            ss.add(s);
            break;
        }
        
        Thread t = new Thread(s);
        t.start();
    }
    
    public void moveUp(){
        if(y>0){
            this.y -= speed; 
        }
    }
    public void moveRight(){
        if(x<500-30){
            this.x += speed; 
        }
    }
    public void moveDown(){
        if(y<430-50){
            this.y += speed; 
        }
    }
    public void moveLeft(){
        if(x>0){
            this.x -= speed; 
        }
    }
    
}

class EnemyTank extends Tank implements Runnable{
    
//    int times = 0;
    Vector<Shoot> ss = new Vector<Shoot>();
    
    public EnemyTank(int x, int y){
        super(x,y);
    }
    
    public void run(){
        while(true){
            
            switch(this.direct){
            case 0:
                for(int i=0;i<30;i++){
                    if(y>0){
                        y -= speed;
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 1:
                for(int i=0;i<30;i++){
                    if(x<500-30){
                        x += speed;
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 2:
                for(int i=0;i<30;i++){
                    if(y<430-50){
                        y += speed;
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 3:
                for(int i=0;i<30;i++){
                    if(x>0){
                        x -= speed;
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            
//            this.times ++;
//            
//            if(times %2 == 0){
//                if(isAlive){
//                    
//                    if(ss.size()<2){
//                        Shoot s = null;
//                        
//                        switch(direct){
//                        case 0:
//                            s = new Shoot(x+10,y,0);
//                            ss.add(s);
//                            break;
//                        case 1:
//                            s = new Shoot(x+30,y+10,1);
//                            ss.add(s);
//                            break;
//                        case 2:
//                            s = new Shoot(x+10,y+30,2);
//                            ss.add(s);
//                            break;
//                        case 3:
//                            s = new Shoot(x,y+10,3);
//                            ss.add(s);
//                            break;
//                        }
//                        
//                        // Activate the bullet
//                        Thread t = new Thread(s);
//                        t.start();
//                    }
//                }
//            }
            
            
            this.direct = (int)(Math.random()*4);
            
            // Let the dead enemy tanks exit the thread.
            if(this.isAlive == false){
                break;
            }
            
        }
    }
    
}

class Bomb{
    int x;
    int y;
    int life = 9;
    boolean isAlive = true;
    
    public Bomb(int x,int y){
        this.x = x;
        this.y = y;
    }
    
    public void lifeDown(){
        if(life>0){
            life --;
        }else{
            this.isAlive = false;
        }
    }
    
}





