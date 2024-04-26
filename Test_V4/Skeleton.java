import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

/**
 * Write a description of class Skeleton here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Skeleton extends Enemy
{
    private int steps;
    private boolean stop;
    private int directionX;
    private int directionY;
    private int decision;
    //For Handling the animations and images
    private int rotation;
    private int frame = 0;
    private int frameSteps = 0;
    private GreenfootImage idleFront = new GreenfootImage("SkeletonIdleFront.png");
    private GreenfootImage idleBack = new GreenfootImage("SkeletonIdleBack.png");
    private GreenfootImage idleLeft = new GreenfootImage("SkeletonIdleLeft.png");
    private GreenfootImage idleRight = new GreenfootImage("SkeletonIdleRight.png");
    private GreenfootImage walkDown[] = {new GreenfootImage("SkeletonWalkDown1.png"),new GreenfootImage("SkeletonWalkDown2.png"),new GreenfootImage("SkeletonWalkDown3.png"), new GreenfootImage("SkeletonWalkDown4.png"),new GreenfootImage("SkeletonWalkDown5.png"),new GreenfootImage("SkeletonWalkDown6.png"),new GreenfootImage("SkeletonWalkDown7.png"),new GreenfootImage("SkeletonWalkDown8.png")};
    private GreenfootImage walkUp[] = {new GreenfootImage("SkeletonWalkUp1.png"),new GreenfootImage("SkeletonWalkUp2.png"),new GreenfootImage("SkeletonWalkUp3.png"), new GreenfootImage("SkeletonWalkUp4.png"),new GreenfootImage("SkeletonWalkUp5.png"),new GreenfootImage("SkeletonWalkUp6.png"),new GreenfootImage("SkeletonWalkUp7.png"),new GreenfootImage("SkeletonWalkUp8.png")};
    private GreenfootImage walkLeft[] = {new GreenfootImage("SkeletonWalkLeft1.png"),new GreenfootImage("SkeletonWalkLeft2.png"),new GreenfootImage("SkeletonWalkLeft3.png"), new GreenfootImage("SkeletonWalkLeft4.png"),new GreenfootImage("SkeletonWalkLeft5.png"),new GreenfootImage("SkeletonWalkLeft6.png"),new GreenfootImage("SkeletonWalkLeft7.png"),new GreenfootImage("SkeletonWalkLeft8.png")};
    private GreenfootImage walkRight[] = {new GreenfootImage("SkeletonWalkRight1.png"),new GreenfootImage("SkeletonWalkRight2.png"),new GreenfootImage("SkeletonWalkRight3.png"), new GreenfootImage("SkeletonWalkRight4.png"),new GreenfootImage("SkeletonWalkRight5.png"),new GreenfootImage("SkeletonWalkRight6.png"),new GreenfootImage("SkeletonWalkRight7.png"),new GreenfootImage("SkeletonWalkRight8.png")};
    private GreenfootImage actualSprite;
    
    
    public Skeleton(int lifes, int speed)
    {
        super(lifes,speed);
        this.steps = 0;
        this.directionX=0;
        this.directionY=0;
        this.stop = true;
        this.decision=0;
        this.rotation = 90;
    }
    
    public void act()
    {
        //System.out.println("Hostile mode: " + super.isHostile());
        //System.out.println("Enemy pos: "+getX()+","+getY());
        // Add your action code here.
        if(!super.isDeath())
        {
            if(super.isHostile())
                hostileState();
            else
                pacificState();
                lookForPlayer();
            //Play animations
            playAnimations();
        }
        else
        {
            //Remove enemy from world
            super.killEnemy();
        }
    }
    
    public void checkCollision()
    {
        Actor object = getOneIntersectingObject(null);
        if(object instanceof Player1)
        {
            //System.out.println("Damage player1");
            ((Player1)object).takeDamage();
        }
    }
    
    //This method will change direction in case of an obstacle detected
    public int chooseDirection() {
        Random rand = new Random();
        boolean[] obstacles = {false, false, false, false}; // Flags for obstacles in up, left, down, right directions
        ArrayList<Integer> available = new ArrayList<>(); // Existing directions to go
        int xPos = getX();
        int yPos = getY();
        int width = getWorld().getWidth();
        int height = getWorld().getHeight();
    
        // Check for obstacles and add available directions
        if (yPos - 50 > 0) {
            available.add(1); // Up
        } else {
            obstacles[0] = true;
        }
    
        if (xPos - 50 > 0) {
            available.add(2); // Left
        } else {
            obstacles[1] = true;
        }
    
        if (yPos + 50 < height) {
            available.add(3); // Down
        } else {
            obstacles[2] = true;
        }
    
        if (xPos + 50 < width) {
            available.add(4); // Right
        } else {
            obstacles[3] = true;
        }
    
        // Calculate weights based on the number of available directions
        int totalAvailable = available.size();
        double[] weights = new double[totalAvailable];
        for (int i = 0; i < totalAvailable; i++) {
            // Assign higher weights to directions with fewer obstacles
            weights[i] = 1.0 / (1.0 + countObstacles(obstacles, available.get(i)));
        }
    
        // Normalize weights
        double sumWeights = 0;
        for (double weight : weights) {
            sumWeights += weight;
        }
        for (int i = 0; i < totalAvailable; i++) {
            weights[i] /= sumWeights;
        }
    
        // Randomly choose a direction based on the weights
        double randomValue = rand.nextDouble();
        double cumulativeProbability = 0;
        for (int i = 0; i < totalAvailable; i++) {
            cumulativeProbability += weights[i];
            if (randomValue <= cumulativeProbability) {
                return available.get(i);
            }
        }
    
        // Fallback to randomly choose if something goes wrong
        return available.get(rand.nextInt(totalAvailable));
    }

// Helper method to count obstacles in a given direction
    private int countObstacles(boolean[] obstacles, int direction) {
    switch (direction) {
        case 1: return obstacles[0] ? 1 : 0; // Up
        case 2: return obstacles[1] ? 1 : 0; // Left
        case 3: return obstacles[2] ? 1 : 0; // Down
        case 4: return obstacles[3] ? 1 : 0; // Right
        default: return 0;
    }
}
    
    public void hostileState()
    {
        int height = getWorld().getHeight();
        int width = getWorld().getWidth();
        Actor objectCollided = getOneIntersectingObject(null);
        
        //System.out.println("Collided with edge? "+isAtEdge());
        if((!(getX() - 50 < 0 || getX() + 50 > width) && !(getY() - 50 < 0 || getY() + 50 > height)) && !(objectCollided instanceof Obstacle))
        {
            moveHostile();
        }
        else//Enemy has collided with an obstacle or edge, deactivate hostile state
        {
            this.directionX=0;
            this.directionY=0;
            changeHostileState();
            this.steps = 51;
        }
    }
    
    public void setMovement()
    {
        if(this.stop)
            this.stop=false;
        else
            this.stop=true;
    }
    
    //Action to do whe in pacific 
    public void pacificState()
    {
        //Stop for only oneSecond
        if(this.steps>50)
        {
            this.directionX=0;
            this.directionY=0;
            this.decision=chooseDirection();
            this.steps=0;
            setMovement();
        }
        
        //Move only for one second
        if(!stop)
            movePacific(this.decision);
        
        this.steps+=1;
    }
    
    //Check if player 1 is visible on area
    public void lookForPlayer()
    {
      List<Player1> playerList =  getObjectsInRange(500,Player1.class);
      if(playerList.size()!=0)
      {
          for (Player1 player : playerList) 
          {
            if(!super.isHostile())
                playerIsOnXY(player);
          }
      }
    }
    
    public void playerIsOnXY(Player1 target)
    {
        //System.out.println("Player pos: "+target.getX()+","+target.getY());
        //Checks if the player is Horizontal
        // Calculate the horizontal and vertical distance between the skeleton and the player
        int dx = Math.abs(getX() - target.getX());
        int dy = Math.abs(getY() - target.getY());
        //System.out.println("Differences dx, dy: "+dx+","+dy);
        // Check if the player is within a certain range horizontally or vertically
        if (dx <= 20 || dy <= 20) {
            // If the player is within the range, determine the direction to move towards the player
            if (dx >= dy) {
                // Player is horizontally aligned
                if (getX() > target.getX())
                    this.directionX = -1; // Player is to the left
                else
                    this.directionX = 1;  // Player is to the right
                this.directionY = 0;
            } else {
                // Player is vertically aligned
                if (getY() > target.getY())
                    this.directionY = -1; // Player is above
                else
                    this.directionY = 1;  // Player is below
                this.directionX = 0;
            }
            // Activate hostile state since the player is within range
            changeHostileState();
        }
    }
    
    //Movement acting of skeleton when being hostileState
    public void moveHostile()
    {
        //Get actual position
        int newX = getX();
        int newY = getY();
        
        //Update to new position
        newX += (this.directionX*super.getSpeed()*8);
        newY += (this.directionY*super.getSpeed()*8);
        
        setLocation(newX,newY);
    }
    
    //Movement acting of skeleton when being pacificState
    public void movePacific(int newDir)
    {
        //Get actual position
        int newX = getX();
        int newY = getY();
        
        //Set the direction acording to the decision that was made
        switch(newDir)
        {
            case 1: //Go up
                this.directionY = -1;
                this.directionX = 0;
                this.rotation=270;
                break;
            case 2: //Go down
                this.directionY = 0;
                this.directionX = -1;
                this.rotation=90;
                break;
            case 3: //Go left
                this.directionY = 1;
                this.directionX = 0;
                this.rotation=180;
                break;
            case 4: //Go right
                this.directionY = 0;
                this.directionX = 1;
                this.rotation=0;
                break;
        }
        
        //Update to new position
        newX += (this.directionX*super.getSpeed());
        newY += (this.directionY*super.getSpeed());
        
        setLocation(newX,newY);
    }
    
    public void playAnimations()
    {
     //Get the image for actual sprite
     if(this.directionY==0&&this.directionX==0)
     {
         setIdleImages();
     }
     else
     {
         walkingAnimations();
     }
     //Set actualsprite on actor
     setImage(this.actualSprite);
    }
    public void walkingAnimations()
    {
        //Play animations for vertical movement
        if(this.directionY!=0&&this.directionX==0)
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
        this.frameSteps+=1;
        if(this.frameSteps>4) //This defines how long a frame is going to play
        {
            
            this.frame += 1;
            this.frameSteps=0;
        }
      
        //Restart Walking Animation
        if(this.frame == 8)
        {
            frame = 0;
        }
    }
    
    public void setIdleImages()
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
        this.frame = 0;
        this.frameSteps = 0;
    }
}
