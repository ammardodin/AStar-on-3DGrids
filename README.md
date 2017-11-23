## Path Planning & Mount Helena
A* is a widely used algorithm in pathfinding and graph traversal. Due to its pheonemnal performance and accruacy, it is utilized extensively in applications such as maps, vehicle routing, and robotics.

The core of A* lies at the heuristic used to determine the order of node expansion. Heuristics are essentially estimates of the cost of getting to the goal from a particular node. Heuristics take in consideration both the forward cost, and the backward cost.

For more info, see [Wikipedia](https://en.wikipedia.org/A*_search_algorithm)

### Summary
This project implements several A* Modules with different heuristics to determine the best path to reach the peak of [Mount Saint Helena](https://en.wikipedia.org/wiki/Mount_Saint_Helena).

Theoretical worlds are imposed in which the implementations are put to the test. Each of which can be defined by their cost function. More concretely, the worlds can be described by:
```java
 // Exponential of the height difference
 public double getCost(final Point p1, final Point p2) {
     return Math.pow(2.0, (getTile(p2) - getTile(p1)));
 }
 // Bizzaro world cost function
 public double getCost(final Point p1, final Point p2) {
     return 1.0*(getTile(p1) / (getTile(p2) + 1));
 }
 ```

 ### Compiling & Running
 To compile, run the following command in your terminal:
 ```shell
 javac  *.java
 ```

 To run any of the AI modules on a randomly generated map, use the following command:
 ```shell
 java Main <AIModule>
 ```

 To run mutiple AI Modules, use the following command:
 ```shell
java Main <AIModule> <AIModule> ...
``` 

To run with a specific seed to the random map generator, use the following command:
```shell
java Main <AIModule> -seed <number>
```

 ### Implementing your own AI Module
 To implement your own AI Module, you need to write a class that implements the `AIModule` interface. For examples, see the `StupidAI.java` or `AStarExp.java`.

 Once you've implemented your own module, you can use the same commands in the `Compiling & Running` section to test the performance of your AI.

 ### Explanation
 I implemented two AI Modules, namely `AStarDiv` and `AStarExp`. Feel free to check those out. The `Explanation.pdf` contains a detailed explanation and proof of admissability of the heuristics used in those modules.