package controllers;

import io.javalin.Javalin;
import java.io.IOException;
import java.util.Queue;
import models.GameBoard;
import models.Message;
import models.Move;
import org.eclipse.jetty.websocket.api.Session;

class PlayGame {

  private static final int PORT_NUMBER = 8080;
  private static GameBoard gameBoard;
  private static Message message;
  private static Move move;

  private static Javalin app;

  /** Main method of the application.
   * @param args Command line arguments
   */
  public static void main(final String[] args) {
    move = new Move();
    message = new Message();

    app = Javalin.create(config -> {
      config.addStaticFiles("/public");
    }).start(PORT_NUMBER);

    // Test Echo Server
    app.post("/echo", ctx -> {
      ctx.result(ctx.body());
    });
    
    // Hector's code 
    
    // player 1 join the game 
    app.get("/newgame", ctx -> {
      ctx.redirect("tictactoe.html");
    });
    
    // player 1 starts the game
    app.post("/startgame", ctx -> {
      
      //set user info 
      String clientType = ctx.formParam("type");
      char p1Type = clientType.charAt(0);

      // start game 
      gameBoard = new GameBoard();
      gameBoard.setPlayer1(1, p1Type);
      
      // responds 
      ctx.status(200);
      ctx.result(gameBoard.boardJson());
   
    });
    
    // player 2 join the game 
    app.get("/joingame", ctx -> {
      //player info 
      char p2Type = gameBoard.getPlayer1().getType() == 'X' ? 'O' : 'X';
      gameBoard.setPlayer2(2, p2Type);
      
      //start game 
      gameBoard.setGame(true);      
      
      // server 
      sendGameBoardToAllPlayers(gameBoard.boardJson());   
      
      // respond 
      ctx.redirect("/tictactoe.html?p=2");
    });
    
    app.post("/move/:playerId", ctx -> {
      // set move 
      int playerId = Integer.parseInt(ctx.pathParam("playerId"));
      int moveX = Integer.parseInt(ctx.formParam("x"));
      int moveY = Integer.parseInt(ctx.formParam("y")); 
      
      // initialize move 
      move.setMove(gameBoard, playerId, moveX, moveY);

      // load player's Move
      gameBoard.playerMoves(move, message);
    
      // responds 
      ctx.result(message.messageJson());
      sendGameBoardToAllPlayers(gameBoard.boardJson());   
    });

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
