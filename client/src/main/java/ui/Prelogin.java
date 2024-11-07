package ui;

public class Prelogin {
    public static void preLoginCommands(int port){
        ServerFacade func = new ServerFacade("http://localhost:" + port);
    }
}
