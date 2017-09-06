/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communicator;

import java.io.IOException;
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

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        Communicator b;

        b = new Communicator(new Element(1234, 8124, 30000));
        b.start();
        //Thread.sleep(3*10000);

    }

    public Communicator(Element elem) throws IOException, InterruptedException {
        Nodes = new HashMap<>();
        queue = new NodePriorityQueue();

        rcv = new Receiver(elem, Nodes, queue);
        trns = new Transmitter(elem, Nodes, queue);
        Thread t = new Thread(trns);
        t.start();

    }

    public NodePriorityQueue getPQ() {
        return queue;
    }

    public Map<Integer, Node> getMap() {
        return Nodes;
    }

    public void start() {
        System.out.println("hello");
    }
}
