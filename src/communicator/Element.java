/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communicator;

/**
 *
 * @author vinayak
 */
public class Element {
    int identifier,input_port_no;
    long heartbeat_in_ms;
    public Element(int a,int b,long c){    /*
arguments 
1)idenfifier 
2)input_port 
3)heartbeat interval 
4)*/
        identifier=a;
        input_port_no=b;
        heartbeat_in_ms=c;
        
}
public int getIdentifier(){
    return identifier;
    }
public int getInputPort(){
    return input_port_no;
    }
public long getHeartBeatInterval(){
    return heartbeat_in_ms;
    }
}
