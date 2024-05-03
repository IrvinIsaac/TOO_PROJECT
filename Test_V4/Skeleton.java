import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Skeleton extends Enemy {
    private int steps;
    private boolean stop;
    private int directionX;
    private int directionY;
    private int decision;
    private boolean adjust;
    private static final int DEFAULT_DIRECTION = 1;
    private int rotation;
    private int frame = 0;
    private int frameSteps = 0;
    private GreenfootImage idleFront = new GreenfootImage("SkeletonIdleFront.png");
    private GreenfootImage idleBack = new GreenfootImage("SkeletonIdleBack.png");
    private GreenfootImage idleLeft = new GreenfootImage("SkeletonIdleLeft.png");
    private GreenfootImage idleRight = new GreenfootImage("SkeletonIdleRight.png");
    private GreenfootImage walkDown[] = {new GreenfootImage("SkeletonWalkDown1.png"), new GreenfootImage("SkeletonWalkDown2.png"), new GreenfootImage("SkeletonWalkDown3.png"), new GreenfootImage("SkeletonWalkDown4.png"), new GreenfootImage("SkeletonWalkDown5.png"), new GreenfootImage("SkeletonWalkDown6.png"), new GreenfootImage("SkeletonWalkDown7.png"), new GreenfootImage("SkeletonWalkDown8.png")};
    private GreenfootImage walkUp[] = {new GreenfootImage("SkeletonWalkUp1.png"), new GreenfootImage("SkeletonWalkUp2.png"), new GreenfootImage("SkeletonWalkUp3.png"), new GreenfootImage("SkeletonWalkUp4.png"), new GreenfootImage("SkeletonWalkUp5.png"), new GreenfootImage("SkeletonWalkUp6.png"), new GreenfootImage("SkeletonWalkUp7.png"), new GreenfootImage("SkeletonWalkUp8.png")};
    private GreenfootImage walkLeft[] = {new GreenfootImage("SkeletonWalkLeft1.png"), new GreenfootImage("SkeletonWalkLeft2.png"), new GreenfootImage("SkeletonWalkLeft3.png"), new GreenfootImage("SkeletonWalkLeft4.png"), new GreenfootImage("SkeletonWalkLeft5.png"), new GreenfootImage("SkeletonWalkLeft6.png"), new GreenfootImage("SkeletonWalkLeft7.png"), new GreenfootImage("SkeletonWalkLeft8.png")};
    private GreenfootImage walkRight[] = {new GreenfootImage("SkeletonWalkRight1.png"), new GreenfootImage("SkeletonWalkRight2.png"), new GreenfootImage("SkeletonWalkRight3.png"), new GreenfootImage("SkeletonWalkRight4.png"), new GreenfootImage("SkeletonWalkRight5.png"), new GreenfootImage("SkeletonWalkRight6.png"), new GreenfootImage("SkeletonWalkRight7.png"), new GreenfootImage("SkeletonWalkRight8.png")};
    private GreenfootImage actualSprite;

    public Skeleton(int lifes, int speed) {
        super(lifes, speed);
        this.steps = 0;
        this.directionX = 0;
        this.directionY = 0;
        this.stop = true;
        this.decision = 0;
        this.rotation = 90;
        this.adjust = false;
    }

    public void act() {
        if (!super.isDeath()) {
            if (super.isHostile()) {
                hostileState();
            } else if (!this.adjust) {
                pacificState();
                lookForPlayer();
            } else {
                adjustPosition();
            }
            playAnimations();
        } else {
            super.killEnemy();
        }
    }

    public void checkCollision() {
        Actor object = getOneIntersectingObject(null);
        if (object instanceof Player1) {
            ((Player1)object).takeDamage();
        }
    }
    
    public int chooseDirection() {
        Random rand = new Random();
        ArrayList<Integer> available = checkOptions();
        return available.get(rand.nextInt(available.size()));        
    }
    
    public ArrayList<Integer> checkOptions() {
        List<Obstacle> obstacles = getWorld().getObjects(Obstacle.class);
        ArrayList<Integer> options = new ArrayList<>();
    
        // Add all directions initially
        options.add(1); // Up
        options.add(2); // Left
        options.add(3); // Down
        options.add(4); // Right
    
        // Check for obstacles and remove directions where collision will occur
        for (Obstacle obstacle : obstacles) {
            if (willCollide(obstacle)) {
                System.out.println("Skeleton will colide");
                int objPosX = obstacle.getX();
                int objPosY = obstacle.getY();
                int enemyPosX = getX();
                int enemyPosY = getY();
                // Determine direction of collision and remove corresponding option
                if (enemyPosX + 64 >= objPosX - 32 && enemyPosX + 64 <= objPosX+32) { // Right
                    options.remove(Integer.valueOf(4)); // Remove the Integer object, not the index
                } else if (enemyPosX - 64 <= objPosX + 32 && enemyPosX - 64 >= objPosX-32) { // Left
                    options.remove(Integer.valueOf(2));
                } else if (enemyPosY + 64 >= objPosY - 32 && enemyPosY + 64 <= objPosY+32) { // Down
                    options.remove(Integer.valueOf(3));
                } else if (enemyPosY - 64 <= objPosY + 32 && enemyPosY - 64 >= objPosY-32) { // Up
                    options.remove(Integer.valueOf(1));
                }
            }
        }
        System.out.println("Available options: "+options);
        return options;
    }
    
    public boolean willCollide(Obstacle obj) {
        int objPosX = obj.getX();
        int objPosY = obj.getY();
        int enemyPosX = getX();
        int enemyPosY = getY();
        
        // Calculate the range of x and y positions for collision
        int minX = objPosX - 32;
        int maxX = objPosX + 32;
        int minY = objPosY - 32;
        int maxY = objPosY + 32;
    
        // Check if the Skeleton's position falls within the collision range
        boolean collideX = enemyPosX + 64 >= minX && enemyPosX - 64 <= maxX;
        boolean collideY = enemyPosY + 64 >= minY && enemyPosY - 64 <= maxY;
        
        // Return true if there's a collision along both X and Y axes
        return collideX && collideY;
    }


    public void hostileState() {
        Actor objectCollided = getOneIntersectingObject(null);
        if (!(objectCollided instanceof Obstacle)) {
            moveHostile();
        } else {
            changeHostileState();
            this.steps = 51;
            setAdjustDirection();
        }
    }

    public void setMovement() {
        if (this.stop) {
            this.stop = false;
        } else {
            this.stop = true;
        }
    }

    public void pacificState() {
        if (this.steps > 50) {
            this.directionX = 0;
            this.directionY = 0;
            this.decision = chooseDirection();
            this.steps = 0;
            setMovement();
        }
        
        this.steps += 1;

        if (!stop) {
            movePacific(this.decision);
        }
    }

    public void lookForPlayer() {
        List<Player1> playerList = getObjectsInRange(500, Player1.class);
        if (playerList.size() != 0) {
            for (Player1 player : playerList) {
                if (!super.isHostile()) {
                    playerIsOnXY(player);
                }
            }
        }
    }

    public void playerIsOnXY(Player1 target) {
        int dx = Math.abs(getX() - target.getX());
        int dy = Math.abs(getY() - target.getY());
        boolean on = false;
        if (dx <= 20 || dy <= 20) {
            if (dx >= dy) {
                if (getX() > target.getX() && this.rotation == 180) {
                    this.directionX = -1;
                    on = true;
                } else if (getX() < target.getX() && this.rotation == 0) {
                    this.directionX = 1;
                    on = true;
                }
                if (on) this.directionY = 0;
            } else {
                if (getY() > target.getY() && this.rotation == 270) {
                    this.directionY = -1;
                    on = true;
                } else if (getY() < target.getY() && this.rotation == 90) {
                    this.directionY = 1;
                    on = true;
                }
                if (on) this.directionX = 0;
            }
            if (on) changeHostileState();
        }
    }

    public void moveHostile() {
        int newX = getX();
        int newY = getY();
        newX += (this.directionX * super.getSpeed() * 8);
        newY += (this.directionY * super.getSpeed() * 8);
        setLocation(newX, newY);
    }

    public void setAdjustDirection() {
        this.adjust = true;
        if (this.directionX != 0) {
            switch (this.directionX) {
                case 1:
                    this.directionX = -1;
                    this.rotation = 180;
                    break;
                case -1:
                    this.directionX = 1;
                    this.rotation = 0;
                    break;
            }
            this.directionY = 0;
        } else {
            switch (this.directionY) {
                case 1:
                    this.directionY = -1;
                    this.rotation = 270;
                    break;
                case -1:
                    this.decision = 1;
                    this.rotation = 90;
                    break;
            }
            this.directionX = 0;
        }
    }

    public void adjustPosition() {
        int newX = getX();
        int newY = getY();
        Actor o;
        newX += (this.directionX * super.getSpeed());
        newY += (this.directionY * super.getSpeed());
        setLocation(newX, newY);
        if (this.directionX != 0) {
            switch (this.directionX) {
                case 1:
                    o = getOneObjectAtOffset(-64, 0, Obstacle.class);
                    if (o == null) this.adjust = false;
                    break;
                case -1:
                    o = getOneObjectAtOffset(64, 0, Obstacle.class);
                    if (o == null) this.adjust = false;
                    break;
            }
        } else {
            switch (this.directionY) {
                case 1:
                    o = getOneObjectAtOffset(0, -64, Obstacle.class);
                    if (o == null) this.adjust = false;
                    break;
                case -1:
                    o = getOneObjectAtOffset(0, 64, Obstacle.class);
                    if (o == null) this.adjust = false;
                    break;
            }
        }
    }

    public void movePacific(int newDir) {
        int newX = getX();
        int newY = getY();
        switch (newDir) {
            case 1: //Up
                this.directionY = -1;
                this.directionX = 0;
                this.rotation = 270;
                break;
            case 2: //Left
                this.directionY = 0;
                this.directionX = -1;
                this.rotation = 180;
                break;
            case 3://Down
                this.directionY = 1;
                this.directionX = 0;
                this.rotation = 90;
                break;
            case 4://Rigt
                this.directionY = 0;
                this.directionX = 1;
                this.rotation = 0;
                break;
        }
        newX += (this.directionX * super.getSpeed());
        newY += (this.directionY * super.getSpeed());
        setLocation(newX, newY);
    }

    public void playAnimations() {
        if (this.directionY == 0 && this.directionX == 0) {
            setIdleImages();
        } else {
            walkingAnimations();
        }
        setImage(this.actualSprite);
    }

    public void walkingAnimations() {
        if (this.directionY != 0 && this.directionX == 0) {
            switch (this.directionY) {
                case 1:
                    this.actualSprite = this.walkDown[frame];
                    break;
                case -1:
                    this.actualSprite = this.walkUp[frame];
                    break;
            }
        } else {
            switch (this.directionX) {
                case 1:
                    this.actualSprite = this.walkRight[frame];
                    break;
                case -1:
                    this.actualSprite = this.walkLeft[frame];
                    break;
            }
        }
        this.frameSteps += 1;
        if (this.frameSteps > 4) {
            this.frame += 1;
            this.frameSteps = 0;
        }
        if (this.frame == 8) {
            frame = 0;
        }
    }

    public void setIdleImages() {
        switch (this.rotation) {
            case 0:
                this.actualSprite = idleRight;
                break;
            case 90:
                this.actualSprite = idleFront;
                break;
            case 180:
                this.actualSprite = idleLeft;
                break;
            case 270:
                this.actualSprite = idleBack;
                break;
        }
        this.frame = 0;
        this.frameSteps = 0;
    }
}
