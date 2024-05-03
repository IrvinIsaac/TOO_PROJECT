import greenfoot.*;

public class CyberWorld1 extends World
{
    private int tileSize = 64;
    private int worldLength = 13;
    private int worldHeight = 10;
    
    private Enemy enemyTest;
    private Enemy enemyTest2 = new Slime(1,1);
    private Enemy enemyTest3 = new Slime(1,1);
    private Enemy enemyTest4 = new Slime(1,1);
    private Player1 test;
    
    private String[] stage =
    {
      "LWWWWWDWLWWWL",
      "LFFFFFFFLFFFL",
      "LFFFFFFFLFFFL",
      "LFFFFFFLLFFFL",
      "LFFFFFFFFFFFL",
      "LFFFFFFFFFFFL",
      "LFFFFFFFFFFFL",
      "LFFFFFFFFFFFL",
      "LFFFFFFFFFFFL",
      "LWWWWWWWWWWWL"
    };
    
    public CyberWorld1()
    {    
        super(832, 640, 1); 
        constructWorld(); // Corrected method name
        enemyTest = new Skeleton(1, 1);
        addObject(enemyTest, 500, 300);
        test = new Player1(5);
        addObject(test, 400, 250);
        addObject(enemyTest2,200,200);
        addObject(enemyTest3,300,500);
        addObject(enemyTest4,400,300);
    }
    
    public void addTile(char o, int x, int y)
    {
        Obstacle tile = null;
        
        switch(o)
        {
            case 'W':
                tile = new Cyberwall();
                break;
            case 'L':
                tile = new CyberLatWall();
                break;
            case 'D':
                tile = new CyberDoor();
                break;
        }
        
        
        if(tile != null)
        {
            addObject(tile, x * tileSize + tileSize / 2, y * tileSize + tileSize / 2);
            System.out.println("Tile: "+ o);
            System.out.println("Xpos: "+ tile.getX());
            System.out.println("Ypos: " + tile.getY());
        }
            
    }
    
    public void constructWorld() // Corrected method name
    {
        for(int i = 0; i < worldLength; i++)
        {
            for(int j = 0; j < worldHeight; j++)
            {
                addTile(stage[j].charAt(i), i, j);
            }
        }
    }
}
