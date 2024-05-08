import greenfoot.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class LivingArmor extends Enemy {
    private int cooldown = 70; // Cooldown to shoot
    private int currentCooldown = 0;
    private int steps;
    private boolean stop;
    private int directionX;
    private int directionY;
    private int decision;
    private int rotation;
    private Player1 player;

    public LivingArmor(int lifes, int speed) {
        super(lifes, speed);
        this.steps = 0;
        this.directionX=0;
        this.directionY=0;
        this.stop = true;
        this.decision=0;
        this.rotation = 90;
        this.player = new Player1(5);
    }

    public void act() {
        if (!isDeath()) {
            if (isHostile()) {
                hostileState();
            } else {
                pacificState();
                lookForPlayer();
            }
        } else {
            killEnemy();
        }
    }

    public void hostileState() {
        Player1 player = findNearestPlayer();
        if (player != null) {
            // Shoot projectiles towards the player
            shootProjectile(player);
        } else {
            changeHostileState(); //Change state if theres no player detected
        }
    }
    
    public void isPlayerNearby(Player1 target) {
        // Calcular la distancia entre el LivingArmor y player
        double distance = Math.hypot(target.getX() - getX(), target.getY() - getY());
        // Devolver verdadero si el jugador está dentro del rango de detección
        if(distance <= 100)
          changeHostileState();// Reducir el rango de detección para los Slimes
    }
    
    public void lookForPlayer()
    {
      List<Player1> playerList = getObjectsInRange(500,Player1.class);
      if(!playerList.isEmpty())
      {
          for (Player1 player : playerList) 
          {
            if(!super.isHostile())
                isPlayerNearby(player);
          }
      }
    }
    
    public int chooseDirection() {
        Random rand = new Random();
        ArrayList<Integer> available = checkOptions();
        return available.get(rand.nextInt(available.size()));        
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
    
        // Check if the LivingArmor's position falls within the collision range
        boolean collideX = enemyPosX + 64 >= minX && enemyPosX - 64 <= maxX;
        boolean collideY = enemyPosY + 64 >= minY && enemyPosY - 64 <= maxY;
        
        // Return true if there's a collision along both X and Y axes
        return collideX && collideY;
    }
    
    public ArrayList<Integer> checkOptions() {
        List<Obstacle> obstacles = getWorld().getObjects(Obstacle.class);
        ArrayList<Integer> options = new ArrayList<>();
    
        // Add all directions initially
        options.add(1); // Right
        options.add(2); // Left
    
        // Check for obstacles and remove directions where collision will occur
        for (Obstacle obstacle : obstacles) {
            if (willCollide(obstacle)) {
                System.out.println("LivingArmor will colide");
                int objPosX = obstacle.getX();
                int objPosY = obstacle.getY();
                int enemyPosX = getX();
                int enemyPosY = getY();
                
                 // Calculate the range of x and y positions for collision
                    int minX = objPosX - 32;
                    int maxX = objPosX + 32;
                    int minY = objPosY - 32;
                    int maxY = objPosY + 32;
                // Determine direction of collision and remove corresponding option
                if (enemyPosX + 64 >= objPosX - 32 && enemyPosX + 64 <= objPosX+32) { // Right
                    options.remove(Integer.valueOf(1)); // Remove the Integer object, not the index
                } else if (enemyPosX - 64 <= objPosX + 32 && enemyPosX - 64 >= objPosX-32) { // Left
                    options.remove(Integer.valueOf(2));
                }
            }
        }
        System.out.println("Available options: "+options);
        return options;
    }
    
    public void setMovement()
    {
        if(this.stop)
            this.stop=false;
        else
            this.stop=true;
    }
    
    public void movePacific(int newDir)
    {
        //Get actual position
        int newX = getX();
        int newY = getY();
        
        //Set the direction acording to the decision that was made
        switch(newDir)
        {
            case 1: //Go right
                this.directionY = 0;
                this.directionX = 1;
                this.rotation=0;
                break;
            case 2: //Go left
                this.directionY = 0;
                this.directionX = -1;
                this.rotation=90;
                break;
        }
        
        //Update to new position
        newX += (this.directionX*super.getSpeed());
        newY += (this.directionY*super.getSpeed());
        
        setLocation(newX,newY);
    }

    public void pacificState() {
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

    private void shootProjectile(Player1 player) {
        if (currentCooldown <= 0) {
            if(player != null){
                int dx=player.getX()-getX();
                int dy=player.getY()-getY();
                double angleRadians = Math.atan2(dy, dx);
                int angleDegrees = (int) Math.toDegrees(angleRadians);
    
            Projectile projectile = new Projectile(angleDegrees); // Crear instancia del proyectil
            getWorld().addObject(projectile, getX(), getY());
        }
     currentCooldown = cooldown; // Reiniciar el cooldown
        } else {
            currentCooldown--;
    }
}
     private Player1 findNearestPlayer() {
        List<Player1> players = getObjectsInRange(800, Player1.class);//Player in range
        if (!players.isEmpty()) {
            return players.get(0); // Devuelve el primer jugador en la lista
        }
        return null; // No se encontraron jugadores cercanos
    }
}

