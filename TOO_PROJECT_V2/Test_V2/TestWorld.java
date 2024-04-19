import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class TestWorld extends World
{
    public TestWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1920/2, 1080/2, 1); 
        //Instance of Player1 in the world
        addObject(new Player1(5), 10, 10);
    }
    
    public void act()
    {
        
    }
}