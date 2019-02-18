# Breakthrough AI Agent
###Introduction
This project is a university project in Artificial Intelligence. The goal is to make an agent with iterative deepening 
alpha beta pruning algorithm to make decisions in moving players in breakthrough.

###Requirements
* Java 10.x.x
* Maven (optional)

Note that the original project had an ant build setup. However since maven is more suitable to us
for dependancies we added maven later for unit testing.

Install Maven instructions:
https://maven.apache.org/install.html

###How to run
1. Open terminal
2. Navigate to the root directory of this project
3. run the following in terminal to build.
    ```
        ant build
    ```
4. run the following in terminal to run.
    ```
        ant run
    ```
    The service should now be running on localhost port 4001
5. Open either kiosk.jar or gamecontroller-gui.jar and follow instructions from there to play.