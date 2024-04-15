import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Bullet here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Bullet extends Actor
{
    private int durability; //Amount of times the bullet can hit objects
    private int range; //How far the bullet can be from the player
    private int speed; //Speed that the bullet will
    private int damage; //Amount of damage that can deal to enemies
    
    public Bullet(int durability,int range,int speed,int damage)
    {
        this.durability = durability;
        this.range = range;
        this.speed = speed;
        this.damage = damage;
    }
    
    public void act()
    {
        // Add your action code here.
    }
}
