package controllers;

import io.javalin.Javalin;

import java.io.IOException;
import models.Player;
import models.GameBoard;
import java.util.Queue;
import org.eclipse.jetty.websocket.api.Session;

class PlayGame {

  private static final int PORT_NUMBER = 8080;
  private static GameBoard gameBoard;


  private static Javalin app;

  /** Main method of the application.
   * @param args Command line arguments
   */
  public static void main(final String[] args) {

    app = Javalin.create(config -> {
      config.addStaticFiles("/public");
    }).start(PORT_NUMBER);

    // Test Echo Server
    app.post("/echo", ctx -> {
      ctx.result(ctx.body());
    });
    
    // My code starts here 
    
    app.get("/newgame", ctx -> {
      ctx.redirect("tictactoe.html");
    });
    
    
    
    app.post("/startgame", ctx -> {
      
    
      String clientType = ctx.formParam("type");
      char p1Type = clientType.charAt(0);

      gameBoard = new GameBoard();
      gameBoard.setPlayer1(1, p1Type);
      
      ctx.status(200);
      //System.out.println(gameBoard.getPlayer2().getType());
     
      ctx.result(gameBoard.boardJson());
   
    });
    
    app.get("/joingame", ctx -> {
      System.out.println("player two joined");
      char p2Type = gameBoard.getPlayer1().getType() == 'X' ? 'O' : 'X';
      gameBoard.setPlayer1(2, p2Type);
      sendGameBoardToAllPlayers(gameBoard.boardJson());
      ctx.redirect("/tictactoe.html?p=2");
    });
    
    app.post("/move/:playerId", ctx -> {
      System.out.println("move");
      System.out.println(ctx.formParam("x"));
    });
    
    /**
     * Please add your end points here.
     * 
     * 
     * 
     * 
     * Please add your end points here.
     * 
     * 
     * 
     * 
     * Please add your end points here.
     * 
     * 
     * 
     * 
     * Please add your end points here.
     * 
     */

    // Web sockets - DO NOT DELETE or CHANGE
    app.ws("/gameboard", new UiWebSocket());
  }

  /** Send message to all players.
   * @param gameBoardJson Gameboard JSON
   * @throws IOException Websocket message send IO Exception
   */
  private static void sendGameBoardToAllPlayers(final String gameBoardJson) {
    Queue<Session> sessions = UiWebSocket.getSessions();
    for (Session sessionPlayer : sessions) {
      try {
        sessionPlayer.getRemote().sendString(gameBoardJson);
      } catch (IOException e) {
        // Add logger here
        System.out.println("send error");
      }
    }
  }

  public static void stop() {
    app.stop();
  }
}
