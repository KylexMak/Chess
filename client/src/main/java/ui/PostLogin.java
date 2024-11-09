package ui;

import chess.ChessBoard;
import model.*;

import java.io.IOException;
import java.util.Scanner;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class PostLogin {
    public static void postLoginCommands(int port, AuthData authToken) throws IOException{
        ServerFacade func = new ServerFacade("http://localhost:" + port);
        Scanner input = new Scanner(System.in);
        String command = input.nextLine();
        String[] decodeCommand = new String[0];
        if(command.isEmpty()){
            System.out.println("Please enter a command or press 6 for help");
            postLoginCommands(port, authToken);
        }
        else{
            decodeCommand = command.split(" ");
        }
        evalCommand(decodeCommand, func, port, authToken);
    }

    private static void help(){
        System.out.println(SET_TEXT_COLOR_BLUE + "Options: \n" +
                "\t1.Create game: \"1\", <GAME NAME> \n" +
                "\t2.List games: \"2\" \n" +
                "\t3.Join game: \"3\" <ID> <WHITE|BLACK> \n" +
                "\t4.Observe: \"4\" <ID> \n" +
                "\t5.Logout: \"5\" \n" +
                "\t6.Print this message: \"6\" \n");
    }

    private static void preHelp(){
        System.out.println(SET_TEXT_COLOR_BLUE + "Options: \n" +
                "\t1.Login as existing user: \"1\", <USERNAME> <PASSWORD> \n" +
                "\t2.Register a new user: \"2\", <USERNAME> <PASSWORD> <EMAIL> \n" +
                "\t3.Exit the program: \"3\" \n" +
                "\t4.Print this message: \"4\" \n");
    }

    private static void listGames(ServerFacade func, int port, AuthData authToken) throws RuntimeException{
        try{
            ListGames games = func.listGames(authToken);
            System.out.println(RESET_TEXT_COLOR + "Game list: \n "+ games);
            postLoginCommands(port, authToken);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static void logout(ServerFacade func, int port, AuthData authToken){
        try{
            func.logout(authToken);
            System.out.println("Logout Successful!");
            preHelp();
            Prelogin.preLoginCommands(port);
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private static void createGame(String gameName, ServerFacade func, int port, AuthData authToken) throws IOException{
        try{
            GameName name = new GameName(gameName);
            GameId gameId = func.createGame(authToken, name);
            if(gameId != null){
                System.out.println("Game has been created with id: " + gameId.gameID());
                postLoginCommands(port, authToken);
            }
        }
        catch (Exception e){
            System.out.println(RESET_TEXT_COLOR + "We were not able to create your game. Your game name might be taken. Try another one.");
            postLoginCommands(port, authToken);
        }
    }

    private static void observeGame(String gameId, ServerFacade func, int port, AuthData authToken) throws IOException{
        try{
            int gameID = Integer.parseInt(gameId);
            func.joinGame(authToken, new JoinGameRequest(null, gameID));
            System.out.println(RESET_TEXT_COLOR + authToken.username() + " successfully joined game as an observer!");
            postLoginCommands(port, authToken);
        }
        catch (Exception e){
            System.out.println(RESET_TEXT_COLOR + "Unable to join game as observer.");
        }
    }

    private static void joinGame(String[] params, ServerFacade func, int port, AuthData authToken) throws IOException{
        int gameId = Integer.parseInt(params[1]);
        String playerColor = params[2].toUpperCase();
        try{
            JoinGameRequest join = new JoinGameRequest(playerColor, gameId);
            func.joinGame(authToken, join);
            System.out.println(RESET_TEXT_COLOR + authToken.username() + " successfully joined game " + gameId + " as " + playerColor);
            String board = BoardDrawer.printBoard(new ChessBoard(), join.playerColor());
            System.out.println(board);
        }
        catch (Exception e){
            System.out.println(RESET_TEXT_COLOR + authToken.username() + " unable to join game with game id: " + gameId);
            postLoginCommands(port, authToken);
        }
    }

    private static void unknownCommand(int port, AuthData authToken) throws IOException{
        System.out.println(RESET_TEXT_COLOR +
                "Command not recognized: You may have entered a command in the wrong format or entered too many/few things -- press 6 for help");
        postLoginCommands(port, authToken);
    }

    private static void evalCommand(String[] decodeCommand, ServerFacade func, int port, AuthData authToken) throws IOException{
        if(decodeCommand.length == 1){
            if(decodeCommand[0].equals("2")){
                listGames(func, port, authToken);
            }
            else if (decodeCommand[0].equals("5")) {
                logout(func, port, authToken);
            }
            else if (decodeCommand[0].equals("6")) {
                help();
                postLoginCommands(port, authToken);
            }
            else{
                unknownCommand(port, authToken);
            }
        }
        else if (decodeCommand.length == 2) {
            if(decodeCommand[0].equals("1")){
                createGame(decodeCommand[1], func, port, authToken);
            }
            else if (decodeCommand[0].equals("4")) {
                observeGame(decodeCommand[1], func, port, authToken);
            }
            else{
                unknownCommand(port, authToken);
            }
        } else if (decodeCommand.length == 3) {
            if(decodeCommand[0].equals("3")){
                joinGame(decodeCommand, func, port, authToken);
            }
            else{
                unknownCommand(port, authToken);
            }
        }
        else{
            unknownCommand(port, authToken);
        }
    }
}
