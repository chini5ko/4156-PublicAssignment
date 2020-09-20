package models;

import com.google.gson.Gson;


public class Message {

  private boolean moveValidity;

  private int code;

  private String message;
  
  /**
  * Message.
  */
  public Message() {
    moveValidity = true;
    code = 100;
    message = "";
  }
  
  /**
  * Construction for Game board, and a setter.
  */
  public void setMessage(boolean mv, int code, String message) {
    this.moveValidity = mv;
    this.code = code;
    this.message = message;
  }
  
  // return the message in json 
  public String messageJson() {
    Gson gson = new Gson(); 
    return gson.toJson(this);
  } 

}
