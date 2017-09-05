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
        PriorityChecker pc=new PriorityChecker();
        
        pqueue = new PriorityQueue<>(10,pc);
    }
    
    public NodePriorityQueue(NodePriorityQueue qu) {
        this.pqueue = new PriorityQueue(qu.pqueue);                    //TODO change implmentation
    }

    

    public void add(Node a) {
        pqueue.add(a);
        
    }

    public void remove(Node a) {
        pqueue.remove(a);
    }
    
    public  Iterator<Node> iterator() {
        return pqueue.iterator();
    }

    public void update(Node a) {
        this.remove(a);
        this.add(a);
    }
    
    public Node[] toArray(){
    
        return (Node[])pqueue.toArray();
    }
    
    public int size(){
    
        return pqueue.size();
    }
}
