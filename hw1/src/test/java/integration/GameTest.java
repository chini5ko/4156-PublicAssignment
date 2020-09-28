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

 
    // player one make a move -------------------------------
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
    assertEquals(restStatus, 400);
  }
   
  
  /**
  * This is a test case to evaluate the startgame endpoint.
  */
  @Test
  @Order(2)
  public void startGameTest() {
    	
    // Create a POST request to startgame endpoint and get the body
    // Remember to use asString() only once for an endpoint call. Every time you call asString(), 
    //a new request will be sent to the endpoint.  Call it once and then use the data in the object.
    HttpResponse<String> response = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    String responseBody = response.getBody();
        
    // --------------------------- JSONObject Parsing ----------------------------------
        
    System.out.println("Start Game Response: " + responseBody);
        
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);

    // Check if game started after player 1 joins: Game should not start at this point
    assertEquals(false, jsonObject.get("gameStarted"));
        
    // ---------------------------- GSON Parsing -------------------------
        
    // GSON use to parse data to object
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    Player player1 = gameBoard.getPlayer1();
        
    // Check if player type is correct
    assertEquals('X', player1.getType());
        
    System.out.println("Test Start Game");
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
