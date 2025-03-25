# Client-Server File Retrieval System (POSIX Socket)

## Overview
This repository contains a Java-based file retrieval system designed for indexing and searching large datasets. The system supports full-text search functionality, enabling efficient indexing and querying of documents. It was built using Maven on Ubuntu 24.04.1 LTS and utilizes POSIX sockets for network communication between clients and the server.


## Features

1. Indexing: Efficiently processes and indexes a given dataset.
2. Search: Executes full-text searches with support for basic and advanced queries.
3. Performance: The application can handle large datasets with quick response times for both indexing and searching.
4. Networking: Implements POSIX sockets for client-server communication.

   
## File Structure

The project contains the following Java files:

• ClientAppInterface.java: Defines the user interface for the client-side application.

• ClientProcessingEngine.java: Manages processing and execution of client-side requests.

• Dispatcher.java: Coordinates communication between clients and the server.

• FileRetrievalBenchmark.java: Provides benchmarking functionality to measure retrieval performance.

• FileRetrievalClient.java: Implements client-side logic for requesting file retrieval using POSIX sockets.

• FileRetrievalServer.java: Manages server-side operations, including indexing and searching, utilizing POSIX sockets for communication.

• IndexStore.java: Manages the storage and retrieval of indexed data.

• IndexWorker.java: Handles individual indexing tasks for efficiency.

• ServerAppInterface.java: Defines the server-side application interface.

• ServerProcessingEngine.java: Executes indexing and search operations on the server.


#### Additionally, the project includes:

• pom.xml: The Maven configuration file, which defines the project's dependencies and build process.



## Prerequisites
Before you start, ensure that you have the following installed:

• Java 8 or higher
• Maven
• Ubuntu 24.04.1 LTS (or similar Linux-based OS)



## How to build/compile

To build the Java solution use the following commands:
```
cd app-java
mvn compile
mvn package
```



## How to run application

To run the Java server (after you build the project) use the following command:
```
java -cp target/app-java-1.0-SNAPSHOT.jar csc435.app.FileRetrievalServer <port>
> <list | quit>
```

To run the Java client (after you build the project) use the following command:
```
java -cp target/app-java-1.0-SNAPSHOT.jar csc435.app.FileRetrievalClient
> <connect | get_info | index | search | quit>
```

To run the Java benchmark (after you build the project) use the following command:
```
java -cp target/app-java-1.0-SNAPSHOT.jar csc435.app.FileRetrievalBenchmark <server IP> <server port> <number of clients> [<dataset path>]
```

### Example (2 clients and 1 server)

**Step 1:** start the server:

Server
```
java -cp target/app-java-1.0-SNAPSHOT.jar csc435.app.FileRetrievalServer 12345
>
```

**Step 2:** start the clients and connect them to the server:

Client 1
```
java -cp target/app-java-1.0-SNAPSHOT.jar csc435.app.FileRetrievalClient
> connect 127.0.0.1 12345
Connection successful!
> get_info
Client ID: 1
```

Client 2
```
java -cp target/app-java-1.0-SNAPSHOT.jar csc435.app.FileRetrievalClient
> connect 127.0.0.1 12345
Connection successful!
> get_info
Client ID: 2
```

**Step 3:** list the connected clients on the server:

Server
```
> list
Client ID:IP:PORT
Client 1:127.0.0.1:5746
Client 2:127.0.0.1:9677
```

**Step 4:** index files from the clients:

Client 1
```
> index ../datasets/dataset1_client_server/2_clients/client_1
Completed indexing 68383239 bytes of data
Completed indexing in 2.974 seconds
```

Client 2
```
> index ../datasets/dataset1_client_server/2_clients/client_2
Completed indexing 65864138 bytes of data
Completed indexing in 2.386 seconds
```

**Step 5:** search files from the clients:

Client 1
```
> search the
Search completed in 0.4 seconds
Search results (top 10 out of 0):
> search child-like
Search completed in 2.8 seconds
Search results (top 10 out of 15):
* Client 2:folder7/Document10926.txt:4
* Client 1:folder3/Document10379.txt:3
* Client 2:folder6/Document10866.txt:2
* Client 2:folder8/Document1108.txt:1
* Client 2:folder7/folderD/Document11050.txt:1
* Client 2:folder6/Document10848.txt:1
* Client 2:folder6/Document1082.txt:1
* Client 1:folder4/Document10681.txt:1
* Client 1:folder4/Document10669.txt:1
* Client 1:folder3/Document10387.txt:1
```

Client 2
```
> search distortion AND adaptation
Search completed in 3.27 seconds
Search results (top 10 out of 4):
* Client 2:folder7/folderC/Document10998.txt:6
* Client 1:folder4/Document10516.txt:3
* Client 2:folder8/Document11159.txt:2
* Client 2:folder8/Document11157.txt:2
```

**Step 6:** close and disconnect the clients:

Client 1
```
> quit
```

Client 2
```
> quit
```

**Step 7:** close the server:

Server
```
> quit
```

#### Example (benchmark with 2 clients and 1 server)

**Step 1:** start the server:

Server
```
java -cp target/app-java-1.0-SNAPSHOT.jar csc435.app.FileRetrievalServer 12345
>
```

**Step 2:** start the benchmark:

Benchmark
```
java -cp target/app-java-1.0-SNAPSHOT.jar csc435.app.FileRetrievalBenchmark 127.0.0.1 12345 2 ../datasets/dataset1_client_server/2_clients/client_1 ../datasets/dataset1_client_server/2_clients/client_2
Completed indexing 134247377 bytes of data
Completed indexing in 6.015 seconds
Searching the
Search completed in 0.4 seconds
Search results (top 10 out of 0):
Searching child-like
Search completed in 2.8 seconds
Search results (top 10 out of 15):
* Client 2:folder7/Document10926.txt:4
* Client 1:folder3/Document10379.txt:3
* Client 2:folder6/Document10866.txt:2
* Client 2:folder8/Document1108.txt:1
* Client 2:folder7/folderD/Document11050.txt:1
* Client 2:folder6/Document10848.txt:1
* Client 2:folder6/Document1082.txt:1
* Client 1:folder4/Document10681.txt:1
* Client 1:folder4/Document10669.txt:1
* Client 1:folder3/Document10387.txt:1
Searching distortion AND adaptation
Search completed in 3.27 seconds
Search results (top 10 out of 4):
* Client 2:folder7/folderC/Document10998.txt:6
* Client 1:folder4/Document10516.txt:3
* Client 2:folder8/Document11159.txt:2
* Client 2:folder8/Document11157.txt:2
```

**Step 3:** close the server:

Server
```
> quit
```
