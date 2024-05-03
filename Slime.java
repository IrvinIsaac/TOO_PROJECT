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
    
    private int countObstacles(boolean[] obstacles, int direction) {
    switch (direction) {
        case 1: return obstacles[0] ? 1 : 0; // Up
        case 2: return obstacles[1] ? 1 : 0; // Left
        case 3: return obstacles[2] ? 1 : 0; // Down
        case 4: return obstacles[3] ? 1 : 0; // Right
        default: return 0;
    }
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
    if (player != null) {
        moveTowardsPlayer(player); // Mover hacia el jugador
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
    
}