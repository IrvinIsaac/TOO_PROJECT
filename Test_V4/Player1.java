import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import java.util.List;

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
    private int rotation ;
    private int shootDirX;
    private int shootDirY;
    private boolean shootFlag;
    //For Handling the animations
    private int frame = 0;
    private int steps = 0;
    private GreenfootImage idleFront = new GreenfootImage("Player1IdleFront.png");
    private GreenfootImage idleBack = new GreenfootImage("Player1IdleBack.png");
    private GreenfootImage idleLeft = new GreenfootImage("Player1IdleLeft.png");
    private GreenfootImage idleRight = new GreenfootImage("Player1IdleRight.png");
    private GreenfootImage pointFront = new GreenfootImage("Player1PointFront.png");
    private GreenfootImage pointBack = new GreenfootImage("Player1PointBack.png");
    private GreenfootImage pointLeft = new GreenfootImage("Player1PointLeft.png");
    private GreenfootImage pointRight = new GreenfootImage("Player1PointRight.png");
    private GreenfootImage walkDown[] = {new GreenfootImage("frontWalk1.png"),new GreenfootImage("frontWalk2.png"),new GreenfootImage("frontWalk3.png"), new GreenfootImage("frontWalk4.png"),new GreenfootImage("frontWalk5.png"),new GreenfootImage("frontWalk6.png"),new GreenfootImage("frontWalk7.png"),new GreenfootImage("frontWalk8.png")};
    private GreenfootImage walkUp[] = {new GreenfootImage("backWalk1.png"),new GreenfootImage("backWalk2.png"),new GreenfootImage("backWalk3.png"), new GreenfootImage("backWalk4.png"),new GreenfootImage("backWalk5.png"),new GreenfootImage("backWalk6.png"),new GreenfootImage("backWalk7.png"),new GreenfootImage("backWalk8.png")};
    private GreenfootImage walkLeft[] = {new GreenfootImage("leftWalk1.png"),new GreenfootImage("leftWalk2.png"),new GreenfootImage("leftWalk3.png"), new GreenfootImage("leftWalk4.png"),new GreenfootImage("leftWalk5.png"),new GreenfootImage("leftWalk6.png"),new GreenfootImage("leftWalk7.png"),new GreenfootImage("leftWalk8.png")};
    private GreenfootImage walkRight[] = {new GreenfootImage("rightWalk1.png"),new GreenfootImage("rightWalk2.png"),new GreenfootImage("rightWalk3.png"), new GreenfootImage("rightWalk4.png"),new GreenfootImage("rightWalk5.png"),new GreenfootImage("rightWalk6.png"),new GreenfootImage("rightWalk7.png"),new GreenfootImage("rightWalk8.png")};
    private GreenfootImage actualSprite;
    private int width;
    private int height;
    
    
    public Player1(int lifes)
    {
        super();
        this.lifes = lifes;
        this.speed = 2;
        this.directionX = 0;
        this.directionY = 0;
        this.death = false;
        this.shootFlag = false;
        this.currentWeapon = new Weapon(5,1,new Bullet(5,200,5,1,this));
        this.weaponInventory = new ArrayList<Weapon>();
        this.rotation = 90;
        this.actualSprite = idleFront;
        this.width = this.actualSprite.getWidth() + 16;
        this.height = this.actualSprite.getHeight() + 16;
    }
    
    public boolean checkTileObstacleCollision()
    {
        Actor o = null;
        //getOneObjectAtOffset(int dx, int dy, Class<?> cls)
        if(Greenfoot.isKeyDown("a")&&Greenfoot.isKeyDown("w"))
            o = getOneObjectAtOffset(-16,-16,Obstacle.class);
        else if(Greenfoot.isKeyDown("d")&&Greenfoot.isKeyDown("w"))
            o = getOneObjectAtOffset(16,-16,Obstacle.class);
        else if(Greenfoot.isKeyDown("a")&&Greenfoot.isKeyDown("s"))
            o = getOneObjectAtOffset(-16,16,Obstacle.class);
        else if(Greenfoot.isKeyDown("d")&&Greenfoot.isKeyDown("s"))
            o = getOneObjectAtOffset(16,16,Obstacle.class);
        else if(Greenfoot.isKeyDown("a"))
            o = getOneObjectAtOffset(-16,0,Obstacle.class);
        else if(Greenfoot.isKeyDown("d"))
            o = getOneObjectAtOffset(16,0,Obstacle.class);
        else if(Greenfoot.isKeyDown("s"))
            o = getOneObjectAtOffset(0,16, Obstacle.class);
        else if(Greenfoot.isKeyDown("w"))
            o = getOneObjectAtOffset(0,-16, Obstacle.class);
            
        if(o!=null)
            return true;
        else
            return false;
    }
    
    public void setDir0(Obstacle obj)
    {
        int objPosX = obj.getX();
        int objPosY = obj.getY();
        int posX = getX();
        int posY = getY();
        int minX = objPosX - 32;
        int maxX = objPosX + 32;
        int minY = objPosY - 32;
        int maxY = objPosY + 32;

        if(posX + 64 >= minX&&posX + 64 <= maxX) //Will collide to right
            this.directionX=0;
        else if(posX - 64 <= maxX&&posX - 64 >= minX) //Will collide to left
            this.directionX=0;   
        else if(posY - 64 <= maxY&&posY - 64 >= minY) //Will collide to up
            this.directionY=0;
        else if(posY + 64 >= minY&&posY + 64 <= maxY) //Will collide to down
            this.directionY=0;
    }
    
    public boolean willCollide(Obstacle obj) {
        int objPosX = obj.getX();
        int objPosY = obj.getY();
        int posX = getX();
        int posY = getY();
        
        // Calculate the range of x and y positions for collision
        int minX = objPosX - 32;
        int maxX = objPosX + 32;
        int minY = objPosY - 32;
        int maxY = objPosY + 32;
    
        // Check if the Player's position falls within the collision range
        boolean collideX = posX + 64 >= minX && posX - 64 <= maxX;
        boolean collideY = posY + 64 >= minY && posY - 64 <= maxY;
        
        // Return true if there's a collision along both X and Y axes
        return collideX && collideY;
    }
    
    
    public int getRot()
    {
        return this.rotation;
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
        if(Greenfoot.isKeyDown("k"))
        {
            this.directionY=this.directionX=0;
        }
        if(Greenfoot.isKeyDown("k") && !shootFlag)
        {
            //Shoot bullet when press down
             this.currentWeapon.shootBullet(this);
             shootFlag = true;
        }
    }
    
    public int getShootDirX()
    {
        return shootDirX;
    }
    
    public int getShootDirY()
    {
        return shootDirY;
    }
    
    public void checkInput()
    {
        //TopDown Movement
        //Check Horizontal Movement
        if(Greenfoot.isKeyDown("a")&&!checkTileObstacleCollision())
        {
             this.directionX = -1;
             this.shootDirX = -1; //Player is pointig left
             this.rotation=180;
             //System.out.println("left");
        }
        else if(Greenfoot.isKeyDown("d")&&!checkTileObstacleCollision())
        {
            this.directionX = 1;
            this.shootDirX = 1; //Player is pointing right
            this.rotation=0;
            //System.out.println("right");
        }
        else
        {
            this.directionX = 0;
        }
        
        //Check Vertical Movement
        if(Greenfoot.isKeyDown("w")&&!checkTileObstacleCollision())
        {
             this.directionY = -1;
             this.shootDirY = -1; //Player is pointing up
             this.rotation=270;
             //System.out.println("up");
        }
        else if(Greenfoot.isKeyDown("s")&&!checkTileObstacleCollision())
        {
            this.directionY = 1;
            this.shootDirY = 1; //Player is pointing down
            this.rotation=90;
            //System.out.println("down");
        }
        else
        {
            this.directionY = 0;
        }
        
        //CheckShooting input
        this.shoot();
    }
    
    //Playing animations methods
    
    public void walkingAnimations()
    {
        //Play animations for vertical movement
        if(Greenfoot.isKeyDown("w")||Greenfoot.isKeyDown("s"))
        {
            switch(this.directionY)
            {
                case 1:
                    this.actualSprite = this.walkDown[frame];
                    break;
                case -1:
                    this.actualSprite = this.walkUp[frame];
                    break;
            }
        }
        else //PlayAnimations for Horizontal movement
        {
            switch(this.directionX)
            {
                case 1:
                    this.actualSprite=this.walkRight[frame];
                    break;
                case -1:
                    this.actualSprite=this.walkLeft[frame];
                    break;
            }
        }
        
        //Change frame
        this.steps+=1;
        if(this.steps>4) //This defines how long a frame is going to play
        {
            
            this.frame += 1;
            this.steps=0;
        }
      
        //Restart Walking Animation
        if(this.frame == 8)
        {
            frame = 0;
        }
    }
    
    public void setIdle()
    {
        this.frame = 0;
        
        if(Greenfoot.isKeyDown("k"))//Get Aiming images
        {
            switch(this.rotation)
            {
                case 0:
                    this.actualSprite=pointRight;
                    break;
                case 90:
                    this.actualSprite=pointFront;
                    break;
                case 180:
                    this.actualSprite=pointLeft;
                    break;
                case 270:
                    this.actualSprite=pointBack;
                    break;
            }
        }
        else //Get idleImages
        {
            switch(this.rotation)
            {
                case 0:
                    this.actualSprite=idleRight;
                    break;
                case 90:
                    this.actualSprite=idleFront;
                    break;
                case 180:
                    this.actualSprite=idleLeft;
                    break;
                case 270:
                    this.actualSprite=idleBack;
                    break;
            }
        }
    }
    
    public void playAnimations()
    {
        //Draw Current sprite
        this.actualSprite.scale(this.width,this.height);
        setImage(this.actualSprite);
        if(this.directionY!=0||this.directionX!=0)
        {
            walkingAnimations();
        }
        else
        {
            setIdle();
        }
    }
    
    public void topDownMovement()
    {
        //PlayAnimations
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
        this.playAnimations();
    }
}
