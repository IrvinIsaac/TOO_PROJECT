import greenfoot.*;

public abstract class Enemy extends Actor
{
    private int lifes;
    private boolean death;
    private boolean hostileState;
    private int speed;
    
    protected Enemy(int lifes,int speed)
    {
        super();
        this.lifes = lifes;
        this.death = false;
        this.speed = speed;
        this.hostileState = false;
    }
    
    public void takeDamage(int damage)
    {
        this.lifes -= damage;
        if(this.lifes<=0)
        {
            this.death = true;
            this.lifes = 0;
        }
    }
    
    public int getSpeed()
    {
        return this.speed;
    }
    
    //Checks if the enemy is in a hostile mode
    public boolean isHostile()
    {
        return this.hostileState;
    }
    
    public void changeHostileState()
    {
        if(this.hostileState)
            this.hostileState = false;
        else
            this.hostileState = true;
    }
    
    //Checks if the enemy is death
    public boolean isDeath()
    {
        return this.death;
    }
    
    public void killEnemy()
    {
        getWorld().removeObject(this);
    }
    //Checks if it does exist in current world
    public abstract void hostileState();
    public abstract void pacificState();
}
