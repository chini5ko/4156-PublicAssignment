package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import controllers.PlayGame;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import models.GameBoard;
import models.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


@TestMethodOrder(OrderAnnotation.class) 
public class GameTest {
	
  /**
    * Runs only once before the testing starts.
  */
  @BeforeAll
  public static void init() {
    // Start Server
    PlayGame.main(null);
    System.out.println("Before All");
  }
	
  /**
  * This method starts a new game before every test run. It will run every time before a test.
  */
  @BeforeEach
  public void startNewGame() {
    // Test if server is running. 
    HttpResponse<String> response = Unirest.get("http://localhost:8080/").asString();
    int restStatus = response.getStatus();
    assertEquals(restStatus, 200);
    //System.out.println("Before Each: " + restStatus);
  }
	
  /**
  * A player cannot make a move until both players have joined the game.
  */
  @Test
  @Order(1)
  public void playerCannotMoveUntilBothPlayersHaveJoined() {
    	
    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    int restStatus = response.getStatus();
        
    // Check assert statement (New Game has started)
    assertEquals(restStatus, 200);
    
    response = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    String responseBody = response.getBody();
    
    // --------------------------- CHECKING GAMEBOARD ----------------------------------
        
    // System.out.println("Start Game Response: " + responseBody);
        
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);

    // Check if game started after player 1 joins: Game should not start at this point
    assertEquals(false, jsonObject.get("gameStarted"));
    assertEquals(1, jsonObject.get("turn"));
    assertEquals(0, jsonObject.get("winner"));
    assertEquals(false, jsonObject.get("isDraw"));

    // ---------------------------- CHECKING GAMEBOARD -------------------------
        
    // GSON use to parse data to object
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    Player player1 = gameBoard.getPlayer1();
        
    // Check if player type is correct
    assertEquals('X', player1.getType());

 
    // player 1 make a move -------------------------------
    response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    responseBody = response.getBody();
    //System.out.println("Player makes a move: " + responseBody);
    restStatus = response.getStatus();
    
    // check JSON
    JSONObject messageJson = new JSONObject(responseBody);
    assertEquals(400, messageJson.get("code"));
    assertEquals(false, messageJson.get("moveValidity"));
    assertEquals("Player 2 has not joined", messageJson.get("message"));
    
    // check HTTP request 
    //assertEquals(restStatus, 400);
  }
   
  /**
  * After game has started Player 1 always makes the first move..
  */
  @Test
  @Order(2)
  public void player1MakesFirstMove() {
    	
    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    int restStatus = response.getStatus();
        
    // Check assert statement (New Game has started)
    assertEquals(restStatus, 200);
    
    response = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    String responseBody = response.getBody();
    
    // --------------------------- CHECKING INIT GAMEBOARD ----------------------------------
        
    // Parse the response to JSON object
    JSONObject gameBoardJson = new JSONObject(responseBody);

    // Check if game started after player 1 joins: Game should not start at this point
    assertEquals(false, gameBoardJson.get("gameStarted"));
    assertEquals(1, gameBoardJson.get("turn"));
    assertEquals(0, gameBoardJson.get("winner"));
    assertEquals(false, gameBoardJson.get("isDraw"));

    // ---------------------------- CHECKING GAMEBOARD -------------------------
        
    // GSON use to parse data to object
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(gameBoardJson.toString(), GameBoard.class);
    Player player1 = gameBoard.getPlayer1();
        
    // Check if player type is correct
    assertEquals('X', player1.getType());
    
    
    // GAME STARTED 
    
    // Player 2 joins 
    response = Unirest.get("http://localhost:8080/joingame").asString();
    restStatus = response.getStatus();
    assertEquals(restStatus, 200);
    
    response = Unirest.get("http://localhost:8080/getGameBoard").asString();
    responseBody = response.getBody();
    gameBoardJson = new JSONObject(responseBody);
    gameBoard = gson.fromJson(gameBoardJson.toString(), GameBoard.class);
    player1 = gameBoard.getPlayer1();
    
    // Check if game started after player 1 joins: Game should not start at this point
    assertEquals(true, gameBoardJson.get("gameStarted"));
    assertEquals(1, gameBoardJson.get("turn"));
    assertEquals(0, gameBoardJson.get("winner"));
    assertEquals(false, gameBoardJson.get("isDraw"));
    assertEquals('X', player1.getType());


    // PLAYER 2 MAKES a Move before player 1 
    response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=0").asString();
    responseBody = response.getBody();
    restStatus = response.getStatus();
    
    // check JSON
    JSONObject messageJson = new JSONObject(responseBody);
    assertEquals(400, messageJson.get("code"));
    assertEquals(false, messageJson.get("moveValidity"));
    assertEquals("It is not your turn, please wait!", messageJson.get("message"));
    
    // PLAYER 1 makes its first move 
    response = Unirest.post("http://localhost:8080/move/1").body("x=1&y=0").asString();
    responseBody = response.getBody();
    //restStatus = response.getStatus(;
    
    // check JSON
    messageJson = new JSONObject(responseBody);
    assertEquals(100, messageJson.get("code"));
    assertEquals(true, messageJson.get("moveValidity"));
  }
  
    
  /**
  * This will run every time after a test has finished.
  */
  @AfterEach
  public void finishGame() {
    System.out.println("Finished Testing");
  }
    
  /**
   * This method runs only once after all the test cases have been executed.
  */
  @AfterAll
  public static void close() {
    // Stop Server
    PlayGame.stop();
    System.out.println("After All test are exec");
  }
}
