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

## Getting Started
- To start the web server using Gradle, you can run the run task:
```shell
./gradlew :http:run
```

- To start the game server using Gradle, you can run the run task:
```shell
./gradlew :game:run
```