/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communicator;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

/**
 * Communicator can be used to detect live and dead nodes in a distributed network.It uses gossip protocol to communicate between
 * nodes.
 *
 * @author vinayak
 */
public class Communicator {

    Map<Integer, Node> Nodes;
    NodePriorityQueue queue;
    Receiver rcv;
    Transmitter trns;
    Element ele;
    Thread t2;

    ;
 /**
  * Creates a communicator object
  * @param elem contains all the details about the current node we are going to initialise.
  * @throws IOException
  * @throws InterruptedException 
  */   
    

    public Communicator(Element elem) throws IOException, InterruptedException{
        Nodes = new HashMap<>();
        queue = new NodePriorityQueue();
        ele = elem;
        rcv = new Receiver(elem, Nodes, queue);
        trns = new Transmitter(elem, Nodes, queue);
        t2 = new Thread(trns);

    }
/**
 * Returns whether the current node list contains the given node
 * @param id Identifier for the node
 * @return true if the node list contains the given node 
 */
    public boolean containsId(int id) {
        if (Nodes.containsKey(id)) {
            return true;
        }
        return false;
    }
/**
 * Returns Node object of the give id if it exists.else a null pointer is returned
 * @param id
 * @return Node or null
 */
    public Node getNodeWithId(int id) {
        if (containsId(id)) {
            return Nodes.get(id);                                //returns the current node to the caller not the copy 
        }
        return null;
    }
/**
 * Returns the Priority queue used by the Communicator object.
 * @return NodeProiorityQueue used by the object
 */
    public NodePriorityQueue getPQ() {
        return queue;
    }
/**
 * Returns Node Map used by the Communicator object.
 * @return Map<Integer,Node> used by the communicator object internally.
 */
    public Map<Integer, Node> getMap() {
        return Nodes;
    }
/**
 * Starts The Communicator services.
 * the Communicator object will start to send heartbeats and receive heartbeats from other nodes.
 * @throws IOException

 */
    public void start() throws IOException {
        System.out.println("hello");
        rcv.start();
        t2.start();

    }
/**
 * This function requests the remote node for all entries in its node table
 * the given remote nodes identifier should be present in the current nodes node table
 * @param id identifier for the remote node
 * @throws IOException 
 */
    public void requestAll(int id) throws IOException {
        trns.requestAll(id);
    }
/**
 *  This function requests the remote node for all entries in its node table
 * @param node node object of the remote node
 * @throws IOException 
 */
    public void requestAll(Node node) throws IOException {
        trns.requestAll(node);
    }
/**
 * Returns the element object used by the Communicator object
 * @return Element used by the communicator object
 */
    public Element getElement() {
        return ele;
    }
/**
 * Adds the node object to Communicators objects node table.
 * Atleast one node should be present inside the commnicator to make a requestAll 
 * @param id identifier of the node
 * @param timestmp last known timestamp of the node
 * @param port port used by the node for incoming connections
 * @param ipaddr Ipv4 address of the node
 * @throws Exception 
 */
    public synchronized void addNode(int id, long timestmp, int port, String ipaddr) throws Exception {
        if (ele.getIdentifier() != id) {  //checks whether this elemnt is being added to itself
            if (!Nodes.containsKey(id)) {
                Nodes.put(id, new Node(id, timestmp, port, ipaddr));
                queue.update(Nodes.get(id));
            }

        }
        // else throw new Exception("Cant add itself to the Node list.Try another identifier\nThe identifier "+id+ "is the current node");
    }
}
