package csc435.app;

import java.util.ArrayList;
import java.util.Scanner;

public class ServerAppInterface {
    private ServerProcessingEngine engine;

    public ServerAppInterface(ServerProcessingEngine engine) {
        this.engine = engine;
    }

    public void readCommands() {
        Scanner sc = new Scanner(System.in);
        String command;
        
        while (true) {
            System.out.print("> ");

            command = sc.nextLine();

            if (command.equals("quit")) {
                // TO-DO implement quitting
            }

            if (command.startsWith("list")) {
                // TO-DO implement Listing Client ID
            }

            System.out.println("unrecognized command!");
        }
    }
}
