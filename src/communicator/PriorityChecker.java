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
public class PriorityChecker implements Comparator<Node> {

    @Override
    public int compare(Node a, Node b) {                    //comaparotr used for NodepriorityQueue
        if (a.getTimestamp() > b.getTimestamp()) {
            return -1;
        } else {
            return 1;                                       //returns if a is newer than b
        }
    }

}
