package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexWorker implements Runnable {
    private final IndexStore store;
    private final Socket clientSocket;
    private int clientId;

    private static int clientIdCounter = 1;
    public IndexWorker(IndexStore store, Socket clientSocket) {
        this.store = store;
        this.clientSocket = clientSocket;
	synchronized (IndexWorker.class) {
            this.clientId = clientIdCounter++;
	}
    }

    public static synchronized int getClientId() {
	return clientIdCounter;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            out.println("Client ID: " + clientId);
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] parts = inputLine.split(" ", 2);
                if (parts.length < 2) {
                    out.println("ERROR: Invalid command format");
                    continue;
                }
                CommandHandler handler = getCommandHandler(parts[0]);
                if (handler == null) {
                    out.println("ERROR: Unknown command");
                    continue;
                }
                handler.handle(parts[1], in, out);
            }
        } catch (IOException e) {
            System.err.println("Error in IndexWorker: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

    private CommandHandler getCommandHandler(String command) {
        Map<String, CommandHandler> commandMap = new HashMap<>();
        commandMap.put("INDEX", (params, in, out) -> {
            try {
                handleIndexRequest(params, in, out);
            } catch (IOException e) {
                out.println("ERROR: " + e.getMessage());
            }
        });
        commandMap.put("SEARCH", (params, in, out) -> handleSearchRequest(params, out));
        commandMap.put("QUIT", (params, in, out) -> out.println("QUIT REPLY"));

        return commandMap.getOrDefault(command, null);
    }

    private void handleIndexRequest(String documentPath, BufferedReader in, PrintWriter out) throws IOException {
        if (documentPath == null || documentPath.isEmpty()) {
            out.println("ERROR: Invalid document path");
            return;
        }

        long documentNumber = store.putDocument(documentPath);
        if (documentNumber == 0) {
            out.println("ERROR: Failed to index document");
            return;
        }

	String[] searchTerms = documentPath.trim().split("\\s+(AND\\s+)?");

        if (searchTerms.length == 0) {

        out.println("ERROR: Invalid search terms");

        return;

        }


        Map<Long, Long> aggregatedFrequencies = new HashMap<>();
        for (String term : searchTerms) {
        if (!term.equalsIgnoreCase("AND")) {
            List<DocFreqPair> termResults = store.lookupIndex(term);
            for (DocFreqPair pair : termResults) {
                aggregatedFrequencies.merge(pair.documentNumber, pair.wordFrequency, Long::sum);
            }
        }
        }

	Map<Long, Long> filteredResults = new HashMap<>();
        for (Long docId : aggregatedFrequencies.keySet()) {
        boolean containsAllTerms = true;
        for (String term : searchTerms) {
            if (!term.equalsIgnoreCase("AND")) {
                List<DocFreqPair> termResults = store.lookupIndex(term);
                if (!termResults.stream().anyMatch(pair -> pair.documentNumber == docId)) {
                    containsAllTerms = false;
                    break;
                }
            }
        }
        if (containsAllTerms) {
            filteredResults.put(docId, aggregatedFrequencies.get(docId));
        }
        }

        HashMap<String, Long> wordFrequencies = new HashMap<>();
        String line;
        while ((line = in.readLine()) != null && !line.equals("END")) {
            String[] parts = line.split(" ");
            if (parts.length >= 2) {
                try {
                    wordFrequencies.put(parts[0], Long.valueOf(parts[1]));
                } catch (NumberFormatException e) {
                    out.println("ERROR: Invalid word frequency value");
                    return;
                }
            } else {
                out.println("ERROR: Invalid word frequency format");
                return;
            }
        }

        store.updateIndex(documentNumber, wordFrequencies);
        out.println("INDEX REPLY " + documentNumber);
    }

    private void handleSearchRequest(String terms, PrintWriter out) {
        if (terms == null || terms.trim().isEmpty()) {
            out.println("ERROR: Invalid search terms");
            return;
        }

        String[] searchTerms = terms.trim().split("\\s+");
        if (searchTerms.length == 0) {
            out.println("ERROR: Invalid search terms");
            return;
        }

        Map<Long, Long> aggregatedFrequencies = new HashMap<>();
        for (String term : searchTerms) {
            List<DocFreqPair> termResults = store.lookupIndex(term);
            for (DocFreqPair pair : termResults) {
                aggregatedFrequencies.merge(pair.documentNumber, pair.wordFrequency, Long::sum);
            }
        }

        List<Map.Entry<Long, Long>> sortedEntries = new ArrayList<>(aggregatedFrequencies.entrySet());
        sortedEntries.sort(Map.Entry.<Long, Long>comparingByValue().reversed());

        out.println("SEARCH REPLY");
        int resultsLimit = Math.min(10, sortedEntries.size());
        for (int i = 0; i < resultsLimit; i++) {
            Map.Entry<Long, Long> entry = sortedEntries.get(i);
            String documentPath = store.getDocument(entry.getKey());
            if (documentPath.isEmpty()) {
                out.println("ERROR: Failed to retrieve document path");
                return;
            }
            out.println(documentPath + " " + entry.getValue());
        }
        out.println("END");
    }

    @FunctionalInterface
    private interface CommandHandler {
        void handle(String params, BufferedReader in, PrintWriter out);
    }
}
