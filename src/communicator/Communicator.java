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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        Communicator b;

        b = new Communicator(new Element(1234, 8124, 30000));
        b.start();
        //Thread.sleep(3*10000);

    }

    public Communicator(Element elem) throws IOException, InterruptedException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        Nodes = new HashMap<>();
        queue = new NodePriorityQueue();
        ele=elem;
        rcv = new Receiver(elem, Nodes, queue);
        trns = new Transmitter(elem, Nodes, queue);
        t2 = new Thread(trns);
        

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
    public Element getElement(){
    return ele;
    }
    
    public void addNode(int id, long timestmp, int port, String ipaddr){
    Nodes.put(id, new Node(id,timestmp,port,ipaddr));
    queue.update(Nodes.get(id));
    
    }
}
