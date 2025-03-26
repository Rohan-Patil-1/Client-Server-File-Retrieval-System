package app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerProcessingEngine {
    private IndexStore store;
    private Thread dispatcherThread;
    private List<Thread> workerThreads;
    private final List<String> clientInfoList;
    private ExecutorService executorService;
    private volatile boolean running = true;
    private int clientId = 1;

    public ServerProcessingEngine(IndexStore store) {
        this.store = store;
        this.workerThreads = new ArrayList<>();
        this.clientInfoList = new ArrayList<>();
        this.executorService = Executors.newCachedThreadPool();
    }

    public void initialize(int serverPort) {
        dispatcherThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
                // TO-DO implement initializing of port and getting Client ID
                }
            } catch (IOException e) {
                System.err.println("Error initializing server: " + e.getMessage());
            }
        });
        dispatcherThread.start();
    }


    public void spawnWorker(Socket clientSocket) {
	  // TO-DO implement worker for each new client
    }

    public void shutdown() {
	   // TO-DO implement shutdown of server
    }
    System.out.println("Server shut down.");
}

    public ArrayList<String> getConnectedClients() {
        return new ArrayList<>(clientInfoList);
    }
}
