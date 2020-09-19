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
  
  private int totalPlays;
  
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
        && this.turn == move.getPlayer().getId()
        && totalPlays <= 9) {
      isValid = true;
    }
    return isValid;
  }
  
  /**
  * Set Message.
  */
  public void setMessage(Message message, Move move) {
  
    message.setMessage(false, 400, "bad request");
  
    if (move.getPlayer().getId() != this.turn) {
      message.setMessage(false, 400, "It is not your turn, please wait!");
    } else {
      if (this.boardState[move.getMoveX()][move.getMoveY()] != 0) {
        message.setMessage(false, 400, "Cell is taken :(");
      }
    }
    
    if (this.totalPlays >= 9) {
      this.isDraw = true;
      message.setMessage(false, 400, "game is a draw");
    }  
    
  }
  
  /**
  * Player's move.
  */
  public void playerMoves(Move move, Message message) {
  
    if (this.turn == move.getPlayer().getId() && isMoveValid(move)) {
      this.turn = move.getPlayer().getId() == 1 ? 2 : 1;
      message.setMessage(true, 100, "");
      this.boardState[move.getMoveX()][move.getMoveY()] = move.getPlayer().getType();
      this.totalPlays++;
    
      if (isPlayerAWinner(move.getPlayer().getType(), move.getMoveX(), move.getMoveY())) {
        this.winner = move.getPlayer().getId();
        message.setMessage(true, 100, "Player" + move.getPlayer().getId() + "won :)");
      }
    } else {
      //not valid move 
      this.setMessage(message, move);
    }
    
    if (this.totalPlays >= 9) {
      this.isDraw = true;
      message.setMessage(false, 400, "game is a draw");
    } 
    
  }
  
  
  private boolean isPlayerAWinner(char type, int x, int y) {
  
    boolean isWinner = false;
    // check column 
    for (int i = 0; i <= 2; i++) {
      if (boardState[0][i] == type
          && boardState[1][i] == type
          && boardState[2][i] == type
          && this.isDraw != true) {
        isWinner = true;
        break;   
      }
    }
  
    // check row
    for (int i = 0; i <= 2; i++) {
      if (boardState[i][0] == type
          && boardState[i][1] == type
          && boardState[i][2] == type
          && this.isDraw != true) {
        isWinner = true;
        break;   
      }
    }
  
    //check diagonal    
    if (boardState[0][0] == type
        && boardState[1][1] == type
        && boardState[2][2] == type
        && this.isDraw != true) {
      isWinner = true;
    }
    
    if (boardState[2][0] == type
        && boardState[1][1] == type
        && boardState[0][2] == type
        && this.isDraw != true) {
      isWinner = true;
    }
      
    return isWinner;
  
  }
  
  public String boardJson() {
    Gson gson = new Gson(); 
    return gson.toJson(this);
  } 

}
