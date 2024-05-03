import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Slime extends Enemy
{
    private int steps;
    private boolean stop;
    private int directionX;
    private int directionY;
    private int decision;
    private int rotation;
    private Player1 player;
    //Animation atributs
    private int framesteps = 0;
    private int frame = 0;
    private boolean flag = false;
    private GreenfootImage idle[] = {new GreenfootImage("SlimeIdle1.png"),new GreenfootImage("SlimeIdle2.png"),new GreenfootImage("SlimeIdle3.png"),new GreenfootImage("SlimeIdle4.png"),new GreenfootImage("SlimeIdle5.png"),new GreenfootImage("SlimeIdle4.png")};
    private GreenfootImage attack[] = {new GreenfootImage("SlimeAttack1.png"),new GreenfootImage("SlimeAttack2.png"),new GreenfootImage("SlimeAttack3.png"),new GreenfootImage("SlimeAttack4.png"),new GreenfootImage("SlimeAttack5.png"),new GreenfootImage("SlimeAttack6.png")};
    private GreenfootImage actualSprite;
    public Slime(int lifes, int speed)
    {
        super(lifes,speed);
        this.steps = 0;
        this.directionX=0;
        this.directionY=0;
        this.stop = true;
        this.decision=0;
        this.rotation = 90;
        this.player = new Player1(5);
    }
    public void act()
    {
        //System.out.println("Hostile mode: " + super.isHostile());
        //System.out.println("Enemy pos: "+getX()+","+getY());
        // Add your action code here.
        if(!isDeath())
        {
            if(isHostile())
                hostileState();
            else
                pacificState();
                lookForPlayer();
            
            playAnimations();
        }
        else
        {
            //Remove enemy from world
            killEnemy();
        }
    }
    
    public void isPlayerNearby(Player1 target) {
        // Calcular la distancia entre el Slime y el jugador
        double distance = Math.hypot(target.getX() - getX(), target.getY() - getY());
        // Devolver verdadero si el jugador está dentro del rango de detección
        if(distance <= 120)
          changeHostileState();// Reducir el rango de detección para los Slimes
    }
    
    public void lookForPlayer()
    {
      List<Player1> playerList =  getObjectsInRange(500,Player1.class);
      if(playerList.size()!=0)
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
    
    public ArrayList<Integer> checkOptions() {
        List<Obstacle> obstacles = getWorld().getObjects(Obstacle.class);
        ArrayList<Integer> options = new ArrayList<>();
    
        // Add all directions initially
        options.add(1); // Up
        options.add(2); // Down
        options.add(3); // Left
        options.add(4); // Right
    
        // Check for obstacles and remove directions where collision will occur
        for (Obstacle obstacle : obstacles) {
            if (willCollide(obstacle)) {
                System.out.println("Skeleton will colide");
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

    public void moveHostile()
{
    // Mover en la dirección actual hacia el objetivo (Player1)
    int newX = getX() + (directionX * getSpeed());
    int newY = getY() + (directionY * getSpeed());
    setLocation(newX, newY);
}
    
private Player1 findNearestPlayer() {
    // Obtener una lista de jugadores en un rango
    List<Player1> players = getObjectsInRange(500, Player1.class);
    if (!players.isEmpty()) {
        // Encontrar el jugador más cercano (puede ser solo uno)
        return players.get(0); // Devolver el primer jugador en la lista
    }
    return null; // No se encontraron jugadores cercanos
}

private void moveTowardsPlayer(Player1 player) {
    // Calcular la dirección hacia el jugador
    int dx = player.getX() - getX();
    int dy = player.getY() - getY();
    
    // Normalizar la dirección (convertir a una longitud de 1)
    double magnitude = Math.sqrt(dx * dx + dy * dy);
    if (magnitude > 0) {
        int moveX = (int) Math.round(dx / magnitude); // Dirección X normalizada
        int moveY = (int) Math.round(dy / magnitude); // Dirección Y normalizada
        
        // Establecer la dirección para moverse hacia el jugador
        directionX = moveX;
        directionY = moveY;
        
        // Actualizar la rotación (opcional)
        updateRotation(moveX, moveY);
        
        // Mover al Slime en la dirección calculada
        int newX = getX() + (directionX * getSpeed());
        int newY = getY() + (directionY * getSpeed());
        setLocation(newX, newY);
    }
}

private void updateRotation(int moveX, int moveY) {
    if (moveX == 0 && moveY == -1) {
        rotation = 270; // Arriba
    } else if (moveX == 0 && moveY == 1) {
        rotation = 90; // Abajo
    } else if (moveX == -1 && moveY == 0) {
        rotation = 180; // Izquierda
    } else if (moveX == 1 && moveY == 0) {
        rotation = 0; // Derecha
    }
}

public void hostileState()
{
    // Verificar si el jugador está cerca
    Player1 player = findNearestPlayer();
    List<Obstacle> obstacles = getIntersectingObjects(Obstacle.class);
    if (player != null&&obstacles.isEmpty()) {
        moveTowardsPlayer(player); // Mover hacia el jugador
    }
    else
    {
        changeHostileState();
    }
}

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
    
    //Animations
    public void attackAnimation()
    {
        actualSprite = attack[frame];
        
        if(framesteps > 10)
        {
            frame++;
            framesteps=0;
        }
        else
        {
            framesteps++;
        }
        
        if(frame==6)
            frame=0;
    }
    
    public void idleAnimation()
    {
        actualSprite = idle[frame];
        
        if(framesteps > 4)
        {
            frame++;
            framesteps=0;
        }
        else
        {
            framesteps++;
        }
        
        if(frame==6)
            frame=0;
    }
    
    public void playAnimations()
    {
        setImage(actualSprite);
        
        if(!super.isHostile())
        {
            idleAnimation();
        }
        else
        {
            attackAnimation();
        }
    }
    
    public void resetAnimation()
    {
        frame = 0;
        framesteps = 0;
    }
}