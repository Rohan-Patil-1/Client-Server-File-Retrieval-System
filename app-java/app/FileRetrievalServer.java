package csc435.app;
public class FileRetrievalServer
{
    public static void main(String[] args)
    {
        if (args.length < 1) {
            System.out.println("Usage: java FileRetrievalServer <serverPort>");
            return;
        }

        int serverPort;
        try {
            serverPort = Integer.parseInt(args[0]);
            if (serverPort <= 1024) {
                System.out.println("Please provide a non-privileged port number greater than 1024.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number. Please provide a valid integer.");
            return;
        }

        IndexStore store = new IndexStore();
        ServerProcessingEngine engine = new ServerProcessingEngine(store);
        ServerAppInterface appInterface = new ServerAppInterface(engine);
        
        engine.initialize(serverPort);

        appInterface.readCommands();
    }
}
