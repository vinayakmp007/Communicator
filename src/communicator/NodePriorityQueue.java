/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communicator;

import java.util.*;

/**
 *
 * @author vinayak
 */
public class NodePriorityQueue {                             //the class that maintains the queue fro Nodes

    PriorityQueue pqueue;

    public NodePriorityQueue() {
        pqueue = new PriorityQueue<>();
    }

    public void add(Node a) {
        pqueue.add(a);
    }

    public void remove(Node a) {
        pqueue.remove(a);
    }

    public void update(Node a) {
        this.remove(a);
        this.add(a);
    }
}
