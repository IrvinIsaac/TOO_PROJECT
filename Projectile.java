import greenfoot.*;

public class Projectile extends Actor {
    private int speed = 4;
    
    public Projectile(int angleDegrees) { //Set the rotation towards the player
        setRotation(angleDegrees);
    }
    
     public void act() {
        move(speed); //Move towards a direction in rotation
        checkCollision();
        checkOutOfBounds();
        collisionEnvioremnt();
    }
    
    public void collisionEnvioremnt()
    {
        //If every Actor object establish a collision durability of the bullet will decrease
        Actor o = getOneIntersectingObject(Obstacle.class);
        if (o != null) {
            getWorld().removeObject(this);
        }
    }
    
    private void checkCollision() {
        Actor actor = getOneIntersectingObject(Player1.class); // Verify collision to player
        if (actor != null) {
            // if projectile collision to player
            Player1 player = (Player1) actor;
            player.takeDamage(); // Apply damage to player 
            getWorld().removeObject(this); // Eliminate projectile to collision
        }
    }
    
    private void checkOutOfBounds() {
        if (isAtEdge()) {
            getWorld().removeObject(this); // Eliminate projectile if is out of bounds
        }
    }
}

