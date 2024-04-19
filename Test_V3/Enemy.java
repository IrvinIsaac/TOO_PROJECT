import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Enemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Enemy extends Actor
{
    private int lifes;
    private boolean death;
    private boolean hostileState;
    private int speed;
    
    public Enemy(int lifes)
    {
        super();
        this.lifes = lifes;
        this.death = false;
        this.speed = 4;
        this.hostileState = false;
    }
    
    public void takeDamage()
    {
        
    }
    
    public void act()
    {
        // Add your action code here.
    }
}
