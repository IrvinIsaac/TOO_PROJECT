import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class TestWorld extends World
{
    private Enemy enemyTest;
    private Enemy enemyTest2;
    private Player1 test;
    
    public TestWorld()
    { 
        super(800, 600, 1); 
        test = new Player1(5);
        addObject(test, 400, 250);
        enemyTest = new Skeleton(1, 1);
        addObject(enemyTest, 500, 300);
        addObject(enemyTest, 500-200, 300-100);
    }
    
    public void act()
    {
        
    }
    
}