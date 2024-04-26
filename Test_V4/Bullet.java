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
    private Player1 p; //Instance of Player1 to relationate with Bullet
    private int rotationSpeed;//Constant rotation of the bullet
    
    //Instance of Bullet
    public Bullet(int durability,int range,int speed,int damage,Player1 p)
    {
        this.durability = durability;
        this.range = range;
        this.speed = speed;
        this.damage = damage;
        this.p = p;
        this.rotationSpeed = 5;
    }
    
    //State of the Bullet if the durability is 0 remove the object from the world
    public void state()
    {
        if (this.durability == 0||isAtEdge()){
            getWorld().removeObject(this);
            p.resetShootFlag();
        }
    }
    
    public void dealtDamage()
    {
        Actor enemy = getOneIntersectingObject(Enemy.class);
        if (enemy != null && getWorld().getObjects(Enemy.class).contains(enemy)) {
            ((Enemy) enemy).takeDamage(damage);
            this.durability--;
        }
    }
    
    public void collisionEnvioremnt(Actor skeet)
    {
        //If every Actor object establish a collision durability of the bullet will decrease
        if (intersects(skeet))
        this.durability--;
    }
    
    public void checkRotation() {
        //If the key "left" is down decrement the bullet rotation
        if (Greenfoot.isKeyDown("left")) {
            setRotation(getRotation() - rotationSpeed);
        }
        
        //If the key "right" is down increment the bullet rotation
        if (Greenfoot.isKeyDown("right")) {
            setRotation(getRotation() + rotationSpeed);
        }
    }
    
    public void act()
    {
        //Move to a direction with the actual speed
        move(speed);
        //Distance between Player1 and Bullet, if Distance is major to Bullet's range this one get to "state()"
        int x1 = getX();
        int y1 = getY();
        int x2 = p.getPlayerX();
        int y2 = p.getPlayerY();
        // Calculate the distance within the objects
        double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        if (distance > this.range)
            this.durability=0;
        //Increment or decrement the rotation while the bullet still moving foward
        checkRotation();
        //Check each frame if it has collided with enemy
        dealtDamage();
        //Check state of the bullet with durability and verify if is removed from the world
        state();
    }
    }