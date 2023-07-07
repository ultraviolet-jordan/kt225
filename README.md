# kt225

## 225 Emulator Written in Kotlin.
The ambitious Kotlin server project described aims to recreate RuneScape version 225 from 2004.

By reverse engineering the client, we can gain a deep understanding of the game mechanics, internal data structures, 
algorithms, and other crucial elements that make up RuneScape version 225. This knowledge is then used to implement the necessary protocols 
and procedures in the Kotlin server project, replicating the behavior and functionality of the original game.

By analyzing the original client's code and understanding its performance bottlenecks, various techniques can be applied
to optimize the server's codebase. This includes refactoring, algorithm improvements, and reducing computational complexity,
among other strategies. These optimizations aim to enhance the server's responsiveness and reduce processing time,
resulting in faster game cycles and smoother gameplay.

The project's primary focus on optimizing game performance and achieving remarkable game cycle speeds requires meticulous attention to 
memory usage and code optimization. To create an efficient and responsive server implementation that can handle a 
large number of players and complex game interactions.

## Installation Guide

If using the Java client, download a compatible 225 client from the internet. This is not provided this here.

### Requirements
- Java
- Python (If using web client)

### Client Matrix
| HOST OS | Web Client | Java Client |
|---------|------------|-------------|
| Linux   | Yes        | Yes         |
| Windows | No         | Yes         |

### Linux

- _If using the web client, install Websockify 0.9.0_
- ... then use Python to run Websockify.
- Websockify is used to bridge the Web Socket of the browser to the TCP Socket of the game server.
```shell
pip install websockify==0.9.0 // Only 0.9.0 specifically do not use 0.10.0
python -m websockify 0.0.0.0:43595 0.0.0.0:43594 // From Host <-> To Host
```

- _Install Gradle_
```shell
curl -s "https://get.sdkman.io" | bash // Install SDKMAN
sdk install gradle 8.2 // Install Gradle 8.2
```

- _Navigate to the directory of this repository on your local machine..._
- _Run the following tasks in the background._
```shell
gradle http:run // Starts the http server.
gradle game:run // Starts the game server.
```

- Play the game
  - The web client can be accessed at: http://localhost/client
  - or run Java client.

### Windows

- _Navigate to the directory of this repository on your local machine..._
```shell
./gradlew :http:run // Starts the http server.
./gradlew :game:run // Starts the game server.
```

- Play the game
    - Run Java client.

## Miscellaneous
- The web client connects to port 43595 by default.
- The java client connects to port 43594 by default.
- The game server is hosted on port 43594.
- The http server is hosted on port 80.
- Websockify has an issue bridging web socket to tcp socket properly on Windows machines.
- You can also run these applications with an IDE such as IntelliJ.
- If you really want to host the web client on Windows, you can use a container system like Docker and run it on Linux in there.

## Contributing
- Pull requests are welcome.
- Just open an issue if you find anything to fix (excluding systems not in place).