package models;

public class Move {

  private Player player;

  private int moveX;

  private int moveY;
  
  public Move() { 
  
  }
  
  /**
  * set player's move.
  */
  public void setMove(GameBoard gameBoard, int playerId, int x, int y) { 
  
    if (playerId == 1) {
      this.player = gameBoard.getPlayer1();
    } else {
      this.player = gameBoard.getPlayer2();
    }
    
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
