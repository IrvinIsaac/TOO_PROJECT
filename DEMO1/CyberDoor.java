import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class CyberDoor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CyberDoor extends Obstacle
{
    private boolean open;
    private GreenfootImage door[] = {new GreenfootImage("CYBERENTRYWALL1.png"),new GreenfootImage("CYBERENTRYWALL2.png"),new GreenfootImage("CYBERENTRYWALL3.png"),new GreenfootImage("CYBERENTRYWALL4.png")};
    private GreenfootImage actualSprite;
    private int frame = 0;
    private int steps = 0;
    
     public CyberDoor()
    {
        this.open = true;
        this.actualSprite = door[frame];
    }
    
    public void act()
    {
        //Draw CurrentSprite
        setImage(this.actualSprite);
        if(open)
            openDoor();
    }
    
    public void openDoor()
    {
        if(frame!=4)
        {
            this.actualSprite = door[frame];
            this.steps+=1;
            if(this.steps>10)
            {
                frame++;
                this.steps=0;
            }
        }
    }
    
    
}
