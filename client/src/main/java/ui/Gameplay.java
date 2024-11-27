package ui;

import model.AuthData;
import websocket.NotificationHandler;
import websocket.WebsocketClient;

import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class Gameplay {
    public static WebsocketClient ws;
    public static NotificationHandler handler = new NotificationHandler();

    public static void gameplayCommands(int port, AuthData auth, int gameId) throws Exception {
        String url = "http://localhost:" + port;
        ServerFacade func = new ServerFacade(url);
        ws = new WebsocketClient(url, auth.authToken(), gameId, handler);
        Scanner input = new Scanner(System.in);
        String command = input.nextLine();
        String[] decodeCommand = new String[0];
        if(command.isEmpty()){
            System.out.println("Please enter a command or press 6 for help");
            gameplayCommands(port, auth, gameId);
        }
        else{
            decodeCommand = command.split(" ");
        }
        evalCommand(decodeCommand, func, port, auth, gameId);
    }

    private static void gameHelp(){
        System.out.println(SET_TEXT_COLOR_BLUE + "Options: \n" +
                "\t1.Redraw board: \"1\" \n" +
                "\t2.Move: \"3\", <START POSITION> <END POSITION> <PROMOPIECE(only if applicable)> \n" +
                "\t3.Highlight possible moves: \"3\" <POSITION>\n" +
                "\t4.Resign: \"4\" \n" +
                "\t5.Leave: \"5\" \n" +
                "\t6.Print this message: \"6\" \n");
    }

    private static void evalCommand(String[] decodeCommand, ServerFacade func, int port, AuthData auth, int gameId) throws Exception {
        if(decodeCommand.length == 1){
            switch(decodeCommand[0]){
                case "1" -> {
                    ws.redrawGame(auth, gameId);
                    gameplayCommands(port, auth, gameId);
                }
                case "4" -> {
                    ws.resignGame(auth, gameId);
                    gameplayCommands(port, auth, gameId);
                }
                case "5"-> {
                    ws.leaveGame(auth, gameId);
                    gameplayCommands(port, auth, gameId);
                }
                case "6" -> {
                    gameHelp();
                    gameplayCommands(port, auth, gameId);
                }
            }
        }
    }
}
