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
Map<Integer,Node> Nodes;
NodePriorityQueue queue;
Receiver a;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Communicator b=new Communicator();
        b.start();
        
    }
 public Communicator() throws IOException{
 Nodes =new HashMap<>();
 queue =new NodePriorityQueue();
 a=new Receiver(8124,Nodes,queue);
 }   
 public void start(){
   
 }
}
