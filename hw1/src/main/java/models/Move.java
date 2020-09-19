package models;

public class Move {

  private Player player;

  private int moveX;

  private int moveY;
  
  public Move() { 
  
  }
  
  /**
  * set player Move.
  */
  public void setMove(Player player, int x, int y) { 
    this.player = player;
    this.moveX = x;
    this.moveY = y;
  }
  
  public int getMoveX() {
    return this.moveX;
  }

  public int getMoveY() {
    return this.moveY;
  }
  
  public Player getPlayer() {
    return this.player;
  }
}
