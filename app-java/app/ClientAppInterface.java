package app;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientAppInterface {
    private ClientProcessingEngine engine;

    public ClientAppInterface(ClientProcessingEngine engine) {
        this.engine = engine;
    }

    public void readCommands() {
        Scanner sc = new Scanner(System.in);
        String command;

        while (true) {
            System.out.print("> ");

            command = sc.nextLine();

            if (command.equals("quit")) {
                // TO-DO implement for "Quit"
            }

            if (command.startsWith("connect")) {
                // TO-DO implement for "Connect"
            }

            if (command.startsWith("index")) {
                // TO-DO implement for "Index"
            }

	        if (command.startsWith("search")) {
                // TO-DO implement for "Search"
            }

            if (command.equals("get_info")) {
                // TO-DO implement for "get_info"
            }

            System.out.println("unrecognized command!");
        }

        sc.close();
    }
}
