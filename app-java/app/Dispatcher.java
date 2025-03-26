package app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Dispatcher implements Runnable {
    private ServerProcessingEngine engine;

    public Dispatcher(ServerProcessingEngine engine) {
        this.engine = engine;
    }
    
    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(0);
            System.out.println("Server listening on port " + serverSocket.getLocalPort());

            while (true) {
                Socket clientSocket = null;
                try {
                    clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    System.err.println("Accept failed: " + e.getMessage());
                    continue;
                }

                engine.spawnWorker(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Couldn't create a server socket: " + e.getMessage());
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.err.println("Couldn't close the server socket: " + e.getMessage());
                }
            }
        }
    }
}
