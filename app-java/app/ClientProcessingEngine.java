package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class IndexResult {
    public double executionTime;
    public long totalBytesRead;

    public IndexResult(double executionTime, long totalBytesRead) {
        this.executionTime = executionTime;
        this.totalBytesRead = totalBytesRead;
    }
}

class DocPathFreqPair {
    public String documentPath;
    public long wordFrequency;

    public DocPathFreqPair(String documentPath, long wordFrequency) {
        this.documentPath = documentPath;
        this.wordFrequency = wordFrequency;
    }
    @Override
    public String toString() {
        return documentPath + ", " + wordFrequency;
}
}

class SearchResult {
    public double executionTime;
    public ArrayList<DocPathFreqPair> documentFrequencies;

    public SearchResult(double executionTime, ArrayList<DocPathFreqPair> documentFrequencies) {
        this.executionTime = executionTime;
        this.documentFrequencies = documentFrequencies;
    }

    public SearchResult() {
        this.documentFrequencies = new ArrayList<>();
    }
}

public class ClientProcessingEngine {
    private Socket clientSocket;
    private int clientId;

    private static int clientIdCounter = 1; 
    private Map<String, Map<String, Integer>> invertedIndex = new HashMap<>();
    private PrintWriter out;
    private BufferedReader in;
    private List<String> connectedClients;

    public ClientProcessingEngine() {
        this.connectedClients = new ArrayList<>();
    }

    
    public void setupStreams(BufferedReader input, PrintWriter output) {
        this.in = input;
        this.out = output;
    }


    public IndexResult indexFiles(String folderPath) {
    long startTime = System.nanoTime();
    long totalBytesRead = 0;

    File folder = new File(folderPath);
    if (!folder.exists() || !folder.isDirectory()) {
        System.err.println("ERROR: Invalid folder path: " + folderPath);
        return null;
    }

    File[] files = folder.listFiles();
    if (files == null || files.length == 0) {
        System.err.println("WARNING: No files found in directory: " + folderPath);
        return null;
    }

    // TO-DO implement indexing

    double executionTime = (System.nanoTime() - startTime) / 1e9;
    return new IndexResult(executionTime, totalBytesRead);
}

    public SearchResult searchFiles(ArrayList<String> terms) {
        // TO-DO implement searching

        double executionTime = (System.nanoTime() - startTime) / 1e9;
        searchResult.executionTime = executionTime;
        searchResult.documentFrequencies = new ArrayList<>(topResults);

        System.out.printf("Search completed in %.4f seconds\n", executionTime);
        System.out.printf("Search results (top %d out of %d):\n", Math.min(10, frequencyMap.size()), frequencyMap.size());

        for (DocPathFreqPair result : topResults) {
            String formattedPath = result.documentPath.replace("/mnt/shared/dataset1_client_server/1_client/client_", "Client ");
            System.out.printf("* %s:%d\n", formattedPath, result.wordFrequency);
        }

        return searchResult;
    }

    public void sendCommand(String command) {
        out.println(command);
        System.out.println(" Sent command: " + command);
    }

    
    public String receiveResponse() {
        try {
            return in.readLine();
        } catch (IOException e) {
            System.err.println("Error reading from server: " + e.getMessage());
            return null;
        }
    }
    

    public long getInfo() {
    // TO-DO implement get info for Client ID
    return 0;
    }

    public void connect(String host, String port) {
        // TO-DO implement connecting to server
    }

    public void disconnect() {
        // TO-DO implement disconnecting from server
    }
}
