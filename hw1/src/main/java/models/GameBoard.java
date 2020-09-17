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
   * @param type1 player type
   * @param type2 player type
  */
  public GameBoard(char type1, char type2){
    this.p1 = new Player(1, type1);
    this.p2 = new Player(2, type2);
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
  
  public char[][] getBoardState() { 
    return this.boardState;
  }
  
  public Player getPlayer2() { 
    return this.p2;
  }
  
  public void setBoardState(char[][] boardState) {
    this.boardState = boardState;
  }
  
  public String boardJson() {
    Gson gson = new Gson(); 
    return gson.toJson(this);
  } 

}
