package ui;

import model.AuthData;
import model.ListGames;
import server.Server;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

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

    private static void listGames(ServerFacade func, int port, AuthData authToken) throws RuntimeException{
        try{
            ListGames games = func.listGames(authToken);
            System.out.println(games);
            postLoginCommands(port, authToken);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static void logout(ServerFacade func, int port, AuthData authToken){
        try{
            func.logout(authToken);
            Prelogin.preLoginCommands(port);
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
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
        }
        else if (decodeCommand.length == 2) {
            if(decodeCommand[0].equals("1")){
                //createGame();
            }
            else if (decodeCommand[0].equals("4")) {

            }
        }
    }
}
