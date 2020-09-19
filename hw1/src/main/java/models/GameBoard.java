package models;

import com.google.gson.Gson;



public class GameBoard {

  private Player p1;

  private Player p2;

  private boolean gameStarted;

  private int turn;

  private char[][] boardState;

  private int winner;

  private boolean isDraw;
  
  /**
   * Construction for Game board.
  */
  public GameBoard() {
    gameStarted = false;
    turn = 1;
    winner = 0;
    isDraw = false;

    char[][] initBS = { {0, 0, 0}, {0, 0, 0}, {0, 0, 0} }; 
    setBoardState(initBS);

  }
  
  public Player getPlayer1() { 
    return this.p1;
  }
  
  public Player getPlayer2() { 
    return this.p2;
  }
  
  public void setPlayer1(int id, char type) {
    this.p1 = new Player(id, type);
  }
  
  public void setPlayer2(int id, char type) {
    this.p2 = new Player(id, type);
  }
  
  public void setGame(boolean state) {
    gameStarted = state;
  }
  
  public char[][] getBoardState() { 
    return this.boardState;
  }
  
  
  public void setBoardState(char[][] boardState) {
    this.boardState = boardState;
  }
  
  /**
  * Move validity.
  */
  public boolean isMoveValid(Move move) {
    boolean isValid = false;
    if (this.boardState[move.getMoveX()][move.getMoveY()] == 0 
        && this.turn == move.getPlayer().getId()) {
      isValid = true;
    }
    return isValid;
  }
  
  public void setMessage(Message message) {
    message.setMessage(false, 400, "Wrong move");
  }
  
  /**
  * Player move.
  */
  public void playerMoves(Move move, Message message) {
    this.turn = move.getPlayer().getId() == 1 ? 2 : 1;
    message.setMessage(true, 100, "");
    this.boardState[move.getMoveX()][move.getMoveY()] = move.getPlayer().getType();
  }
  
  public String boardJson() {
    Gson gson = new Gson(); 
    return gson.toJson(this);
  } 

}
