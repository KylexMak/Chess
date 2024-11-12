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
            if(games.games().isEmpty()){
                System.out.println(RESET_TEXT_COLOR + "Game list: \n " + "\tNone \n");
            }
            else{
                System.out.println(RESET_TEXT_COLOR + "Game list: \n " + games);
            }
            postLoginCommands(port, authToken);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static void logout(ServerFacade func, int port, AuthData authToken){
        try{
            func.logout(authToken);
            System.out.println("Logout Successful!\n");
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
                System.out.println("Game has been created!\n");
                postLoginCommands(port, authToken);
            }
        }
        catch (Exception e){
            System.out.println(RESET_TEXT_COLOR + "We were not able to create your game. Your game name might be taken. Try another one.\n");
            postLoginCommands(port, authToken);
        }
    }

    private static void observeGame(String gameId, ServerFacade func, int port, AuthData authToken) throws IOException{
        try{
            int gameID = Integer.parseInt(gameId);
            GameData game = func.joinGame(authToken, new JoinGameRequest(null, gameID, true));
            System.out.println(RESET_TEXT_COLOR + authToken.username() + " successfully joined game as an observer!\n");
            String board = BoardDrawer.printBoard(game.game().getBoard(), "WHITE");
            System.out.println(board);
            Scanner input = new Scanner(System.in);
            String command;
            String[] decodeCommand;
            observeOptions();
            while(true){
                command = input.nextLine();;
                if(command.isEmpty()){
                    System.out.println("Please enter a command or press 2 for help\n");
                }
                decodeCommand = command.split(" ");
                if(decodeCommand[0].equals("1")){
                    System.out.println("You have left the game \n");
                    break;
                }
                evalObserve(decodeCommand);
            }
            help();
            postLoginCommands(port, authToken);
        }
        catch (Exception e){
            System.out.println(RESET_TEXT_COLOR + "Unable to join game as observer. \n");
            postLoginCommands(port, authToken);
        }
    }

    private static void joinGame(String[] params, ServerFacade func, int port, AuthData authToken) throws IOException{
        int gameId = Integer.parseInt(params[1]);
        String playerColor = params[2].toUpperCase();
        try{
            ListGames games = func.listGames(authToken);
            GameData game = games.games().get(gameId);
            JoinGameRequest join = new JoinGameRequest(playerColor, game.gameID(), false);
            game = func.joinGame(authToken, join);
            System.out.println(RESET_TEXT_COLOR + authToken.username() + " successfully joined game " + gameId + " as " + playerColor + "\n");
            String board = BoardDrawer.printBoard(game.game().getBoard(), join.playerColor());
            System.out.println(board);
            Scanner input = new Scanner(System.in);
            String command;
            String[] decodeCommand;
            joinOptions();
            while(true){
                command = input.nextLine();;
                if(command.isEmpty()){
                    System.out.println("Please enter a command or press 3 for help \n");
                }
                decodeCommand = command.split(" ");
                if(decodeCommand[0].equals("2")){
                    System.out.println("You have resigned \n");
                    break;
                }
                evalJoin(decodeCommand, board);
            }
            help();
            postLoginCommands(port,authToken);
        }
        catch (Exception e){
            System.out.println(RESET_TEXT_COLOR + authToken.username() + " unable to join game with game id\n");
            postLoginCommands(port, authToken);
        }
    }

    private static void evalJoin(String[] decodeCommand, String board){
        if(decodeCommand[0].equals("1")){
            System.out.println(board);
        }
        else if (decodeCommand[0].equals("3")) {
            joinOptions();
        }
    }

    private static void evalObserve(String[] decodeCommand){
        if(decodeCommand[0].equals("2")){
            observeOptions();
        }
    }

    private static void joinOptions(){
        System.out.println(SET_TEXT_COLOR_BLUE + "Options:\n" +
                "\t1.Redraw board \"1\" \n" +
                "\t2.Resign \"2\" \n" +
                "\t3.Print out these options again. \"3\" \n");
    }

    private static void observeOptions(){
        System.out.println(SET_TEXT_COLOR_BLUE + "Options:\n" +
                "\t1.Leave game \"1\" \n" +
                "\t2.Print out these options again. \"2\" \n");
    }

    private static void unknownCommand(int port, AuthData authToken) throws IOException{
        System.out.println(RESET_TEXT_COLOR +
                "Command not recognized: You may have entered a command in the wrong format or entered too many/few things -- press 6 for help\n");
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
