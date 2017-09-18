# Communicator
A simple java API for analysing dead and live nodes in a network
* Uses HTTP
* Uses JSON to transfer information

## Sample Code

    Element elem = new Element(1234, 8124, 3000);       //creates a element for the communicator
    Communicator comm = new Communicator(elem);
    comm.start();                                       //starts the communicator
    comm.addNode(2000, System.currentTimeMillis(), 8124, "192.168.1.35");  //adds a intial node to the node list of communicator
   

