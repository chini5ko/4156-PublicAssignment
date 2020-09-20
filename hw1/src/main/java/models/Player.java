package models;

public class Player {

  private char type;

  private int id;
  
  public Player() {
    this.id = 0;
    this.type = '0';
  }
  
  // initialize player 
  public Player(int id, char type) {
    this.id = id;
    this.type = type;
  }
  
  public char getType() {
    return this.type;
  }
  
  public int getId() { 
    return this.id;
  }
  

}
