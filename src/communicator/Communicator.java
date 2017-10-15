/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communicator;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.HashMap;

/**
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
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    

    public Communicator(Element elem) throws IOException, InterruptedException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        Nodes = new HashMap<>();
        queue = new NodePriorityQueue();
        ele = elem;
        rcv = new Receiver(elem, Nodes, queue);
        trns = new Transmitter(elem, Nodes, queue);
        t2 = new Thread(trns);

    }

    public boolean containsId(int id) {
        if (Nodes.containsKey(id)) {
            return true;
        }
        return false;
    }

    public Node getNodeWithId(int id) {
        if (containsId(id)) {
            return Nodes.get(id);                                //returns the current node to the caller not the copy 
        }
        return null;
    }

    public NodePriorityQueue getPQ() {
        return queue;
    }

    public Map<Integer, Node> getMap() {
        return Nodes;
    }

    public void start() throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        System.out.println("hello");
        rcv.start();
        t2.start();

    }

    public void requestAll(int id) throws IOException {
        trns.requestAll(id);
    }

    public void requestAll(Node node) throws IOException {
        trns.requestAll(node);
    }

    public Element getElement() {
        return ele;
    }

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
