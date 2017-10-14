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

    PriorityQueue<Node> pqueue;

    public NodePriorityQueue() {
        PriorityChecker pc = new PriorityChecker();

        pqueue = new PriorityQueue<>(10, pc);
    }

    public NodePriorityQueue(NodePriorityQueue qu) {
        this.pqueue = new PriorityQueue(qu.pqueue);                    //TODO change implmentation 
    }

    public synchronized void add(Node a) {

        pqueue.add(a);

    }

    public synchronized void remove(Node a) {

        pqueue.remove(a);

    }

    public Iterator<Node> iterator() {
        return pqueue.iterator();
    }

    public synchronized void update(Node a) {

        this.remove(a);
        this.add(a);

    }

    public synchronized Node[] toArray() {

        return (Node[]) pqueue.toArray();

    }

    public synchronized int size() {

        return pqueue.size();

    }

    public synchronized boolean hasNext() {

        if (pqueue.size() > 0) {
            return true;
        } else {
            return false;
        }

    }

    public synchronized Node next() {

        if (pqueue.size() <= 0) {
            throw new NullPointerException("No more element present in the queue");
        }
        return pqueue.poll();
    }

}
