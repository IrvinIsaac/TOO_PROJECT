import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Weapon extends Actor
{
    private Bullet bullet; //Type of bullet that will shoot
    private int ammo;//Amount of amo that weapon will have
    private int weaponType; //1-Normal pistol(unlimited ammo), 2-(limited ammo)
    
    public Weapon(int ammo, int weaponType, Bullet type)
    {
        super();
        this.bullet = type; 
        this.ammo = ammo;
        this.weaponType = weaponType;
    }
    
    //Get Player position so the world object can spawn the bullet in that position (x and y positions will be stored in an array)
    public void shootBullet(Player1 p)
    {
          if(this.ammo!=0)
          {
            //System.out.println(this.ammo);
            //Instance of an object Bullet each time Player1 Shoot
            Bullet bullet = new Bullet(5,200,4,1,p);
            //Get the world the player is currently in
            World world = p.getWorld();
            int xSpawn = p.getPlayerX();
            int ySpawn = p.getPlayerY();
            //Spawn the bullet on player position
            world.addObject(bullet, xSpawn, ySpawn);
            //Shoot the Bullet in the direction of Player1
            bullet.setRotation(p.getRot());
          }
    }
    
    public void decreaseAmmo()
    {
        if(this.weaponType!=1)
        {
            this.ammo--;
        }
    }
    
    public void increaseAmmo()
    {
         if(this.weaponType!=1)
        {
            this.ammo++;
        }
    }
}
