import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

public class Player1 extends Actor
{
    
    private int lifes; //
    private int speed;
    private int directionX;
    private int directionY;
    private boolean death;
    //For handling all the weapon use stuff
    private Weapon currentWeapon;
    private ArrayList<Weapon> weaponInventory;
    private int shootDirX;
    private int shootDirY;
    private boolean shootFlag;
    
    
    public Player1(int lifes)
    {
        super();
        this.lifes = lifes;
        this.speed = 3;
        this.directionX = 0;
        this.directionY = 0;
        this.death = false;
        this.shootFlag = false;
        this.currentWeapon = new Weapon(5,1,new Bullet(5,200,5,1,this));
        this.weaponInventory = new ArrayList<Weapon>(); 
    }
    
    public int getPlayerX() //To get acces of player x position through another object 
    {
        return getX();
    }
    
    public int getPlayerY() //To get acces of player y position through another object 
    {
        return getY();
    }
    
    public boolean isDead()
    {
        return this.death;
    }
    
    //Void methods
    
    public void takeDamage()
    {
        if(this.lifes>0)
        {
            this.lifes--;
        }
        else
        {
            this.death = true;
        }
    }
    
    public void resetShootFlag() {
        shootFlag = false;
    }
    
    public void shoot()
    {
        if(Greenfoot.isKeyDown("k") && !shootFlag)
        {
            //Shoot bullet when press down
             this.currentWeapon.shootBullet(this);
             shootFlag = true;
        }
    }
    
    public void checkInput()
    {
        //TopDown Movement
        //Check Horizontal Movement
        if(Greenfoot.isKeyDown("a"))
        {
             this.directionX = -1;
             this.shootDirX = -1; //Player is pointig left
             setRotation(180);
             //System.out.println("left");
        }
        else if(Greenfoot.isKeyDown("d"))
        {
            this.directionX = 1;
            this.shootDirX = 1; //Player is pointing right
            setRotation(0);
            //System.out.println("right");
        }
        else
        {
            this.directionX = 0;
        }
        
        //Check Vertical Movement
        if(Greenfoot.isKeyDown("w"))
        {
             this.directionY = -1;
             this.shootDirY = -1; //Player is pointing up
             setRotation(270);
             //System.out.println("up");
        }
        else if(Greenfoot.isKeyDown("s"))
        {
            this.directionY = 1;
            this.shootDirY = 1; //Player is pointing down
            setRotation(90);
            //System.out.println("down");
        }
        else
        {
            this.directionY = 0;
        }
    }
    
    public void topDownMovement()
    {
        if(this.directionY!=0||this.directionX!=0)//Move only if at least one direction is detected
        {
            int newX = getX() + (speed * directionX);
            int newY = getY() + (speed * directionY);
            setLocation(newX, newY);
        }
    }
    
    public void act()
    {
        this.checkInput();
        this.topDownMovement();
        this.shoot();
    }
}
