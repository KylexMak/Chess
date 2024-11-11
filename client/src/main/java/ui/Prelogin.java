package ui;

import model.AuthData;
import model.Login;
import model.UserData;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Prelogin {
    public static void preLoginCommands(int port) throws IOException{
        ServerFacade func = new ServerFacade("http://localhost:" + port);
        Scanner input = new Scanner(System.in);
        String command = input.nextLine();
        String[] decodeCommand = new String[0];
        if(command.isEmpty()){
            System.out.println("Please enter a command or press 4 for help \n");
            preLoginCommands(port);
        }
        else{
            decodeCommand = command.split(" ");
        }
        evalCommand(decodeCommand, func, port);
    }

    private static void help(){
        System.out.println(SET_TEXT_COLOR_BLUE + "Options: \n" +
                "\t1.Login as existing user: \"1\", <USERNAME> <PASSWORD> \n" +
                "\t2.Register a new user: \"2\", <USERNAME> <PASSWORD> <EMAIL> \n" +
                "\t3.Exit the program: \"3\" \n" +
                "\t4.Print this message: \"4\" \n");
    }

    private static void postHelp(){
        System.out.println("""
                Options:\s
                \t1.Create game: "1", <GAME NAME>\s
                \t2.List games: "2"\s
                \t3.Join game: "3" <ID> <WHITE|BLACK>\s
                \t4.Observe: "4" <ID>\s
                \t5.Logout: "5"\s
                \t6.Print this message: "6"\s
                """);
    }

    private static void quit(){
        System.out.println(RESET_TEXT_COLOR + "Come again! Bye!");
    }

    private static void login(String[] decodeCommand, ServerFacade func, int port) throws IOException {
        String username = decodeCommand[1];
        String password = decodeCommand[2];
        try{
            AuthData auth = func.login(new Login(username, password));
            if(auth != null){
                System.out.println(RESET_TEXT_COLOR + "Login successful! \n");
                postHelp();
                PostLogin.postLoginCommands(port, auth);
            }
        } catch (Exception e) {
            System.out.println(RESET_TEXT_COLOR + "We were unable to log you in. Make sure your username and password are correct. \n");
            preLoginCommands(port);
        }
    }

    private static void register(String[] decodeCommand, ServerFacade func, int port) throws IOException{
        String username = decodeCommand[1];
        String password = decodeCommand[2];
        String email = decodeCommand[3];
        try {
            AuthData auth = func.register(new UserData(username, password, email));
            if(auth != null){
                System.out.println(RESET_TEXT_COLOR + "Registered successfully! \n" +
                        username + " is logged in!\n");
                postHelp();
                PostLogin.postLoginCommands(port, auth);
            }
            else{
                throw new IOException("Could not register");
            }
        }
        catch (Exception e){
            System.out.println(RESET_TEXT_COLOR + "Unable to register user. Username may be already taken. \n");
            preLoginCommands(port);
        }
    }

    private static void unknownCommand(int port) throws IOException{
        System.out.println(RESET_TEXT_COLOR +
                "Command not recognized: You may have entered a command in the wrong format or entered too many/few things -- press 4 for help \n");
        preLoginCommands(port);
    }

    private static void evalCommand(String[] decodeCommand, ServerFacade func, int port) throws IOException{
        if(decodeCommand.length == 1){
            if(Objects.equals(decodeCommand[0], "3")){
                quit();
            }
            else if(Objects.equals(decodeCommand[0], "4")){
                help();
                preLoginCommands(port);
            }
            else{
                unknownCommand(port);
            }
        }
        else if (decodeCommand.length == 3 && decodeCommand[0].equals("1")) {
            login(decodeCommand, func, port);
        }
        else if (decodeCommand.length == 4 && decodeCommand[0].equals("2")){
            register(decodeCommand, func, port);
        }
        else{
            unknownCommand(port);
        }
    }
}
