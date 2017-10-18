# Communicator
A simple java API for analysing dead and live nodes in a network
* Uses GOSSIP protocol
* Uses HTTP
* Uses JSON to transfer information

## Sample Code

    Element elem = new Element(1234, 8124, 3000);       //creates a element for the communicator
    Communicator comm = new Communicator(elem);
    comm.start();                                       //starts the communicator
    comm.addNode(2000, System.currentTimeMillis(), 8124, "192.168.1.35");  //adds a intial node to the node list of communicator

## Build 
1. Clone this repository.
1. Open terminal and execute **ant compile** in that directory.
1. execute **ant jar** and the jar file will be created.
1. Add the jar files found in **dist** directory and its subdirectories(**lib**) to you project library to use them.

