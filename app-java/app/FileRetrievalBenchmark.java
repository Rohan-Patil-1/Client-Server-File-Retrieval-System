package app;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

class BenchmarkWorker implements Runnable {
    private ClientProcessingEngine engine;
    private String serverIP;
    private String serverPort;
    private String datasetPath;
    private CountDownLatch latch;

    public BenchmarkWorker(String serverIP, String serverPort, String datasetPath, CountDownLatch latch) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.datasetPath = datasetPath;
        this.latch = latch;
        this.engine = new ClientProcessingEngine();
    }

    @Override
    public void run() {
        try {
            engine.connect(serverIP, serverPort);
            // TO-DO implement indexing 
        } catch (Exception exception) {
            System.err.println("Error in BenchmarkWorker: " + exception.getMessage());
        } finally {
            latch.countDown();
        }
    }

    public void search(ArrayList<String> searchTerms) {
        try {
            // TO-DO implement searching
        } catch (Exception exception) {
            System.err.println("Error in search: " + exception.getMessage());
        }
    }

    public void disconnect() {
        engine.disconnect();
    }
}

public class FileRetrievalBenchmark {
    public static void main(String[] args) throws InterruptedException {
        if (args.length < 4) {
            System.out.println("Usage: java FileRetrievalBenchmark <serverIp> <serverPort> <numClients> <datasetPath1> [<datasetPath2> ...]");
            System.exit(1);
        }

        String serverIp = args[0];
        String serverPort = args[1];
        int numClients = Integer.parseInt(args[2]);
        List<String> datasetPaths = new ArrayList<>();
        for (int i = 3; i < args.length; i++) {
            datasetPaths.add(args[i]);
        }

        long startTime = System.nanoTime();

        CountDownLatch latch = new CountDownLatch(numClients);
        List<BenchmarkWorker> workers = new ArrayList<>();

        for (int i = 0; i < numClients; i++) {
            BenchmarkWorker worker = new BenchmarkWorker(serverIp, serverPort, datasetPaths.get(i % datasetPaths.size()), latch);
            workers.add(worker);
            new Thread(worker).start();
        }

        latch.await();

        long endTime = System.nanoTime();
        double executionTime = (endTime - startTime) / 1e9;

        System.out.printf("Benchmark completed in %.2f seconds%n", executionTime);

        if (!workers.isEmpty()) {
            ArrayList<String> searchTerms = new ArrayList<>(List.of("example", "test"));
            workers.get(0).search(searchTerms);
        }

        for (BenchmarkWorker worker : workers) {
            worker.disconnect();
        }
    }
}
