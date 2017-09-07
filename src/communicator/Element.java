/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communicator;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author vinayak
 */
public class Element {                 //this class used by every individual system

    int identifier, input_port_no;                                                //ip might change here
    long heartbeat_in_ms;
    long wait_till_send;
    long wait_till_dead;
    long sending_interval;
    long maximum_time_sync_error;
    int send_threadpoolsize;
    int rcv_threadpoolsize;
    String myipaddr;
    String password,key_file;                       //Required for https connection

    public Element(int a, int b, long c) throws UnknownHostException {
        /*
arguments 
1)idenfifier 
2)input_port 
3)heartbeat interval 
4)*/
        identifier = a;
        input_port_no = b;
        heartbeat_in_ms = c;
        send_threadpoolsize = 5;                   //default threadpool size is 5 
        wait_till_send = 50 * 1000;                  //default value is 30 
        wait_till_dead = 80 * 1000;                  //default value is 50
        rcv_threadpoolsize = 5;   
    }

    public int getIdentifier() {
        return identifier;
    }

    public int getInputPort() {
        return input_port_no;
    }
    public void setPasswordAndKeyFile(String password,String keyfile){
    this.password=password;
    this.key_file=keyfile;
    
    
    }
    public String getPassword(){
    
        return password;
    }
    
    public String getKeyFile(){
    return key_file;
    }

    public int getTransmitterThreadPoolSize() {
        return send_threadpoolsize;
    }

    public void setTransmitterThreadPoolSize(int a) {
        send_threadpoolsize = a;
    }
    public int getReceiverThreadPoolSize() {
        return rcv_threadpoolsize;
    }

    public void setReceiverThreadPoolSize(int a) {
        rcv_threadpoolsize = a;
    }

    public void setWaitTillSendmillis(long a) {
        wait_till_send = a;
    }

    public void setWaitTillDeadmillis(long a) {
        wait_till_dead = a;
    }

    public long getWaitTillSendmillis() {
        return wait_till_send;
    }

    public long getWaitTillDeadmillis() {
        return wait_till_dead;
    }

    public long getHeartBeatInterval() {
        return heartbeat_in_ms;
    }

    public void setSendingIntervalmillis(long a) {
        sending_interval = a;
    }

    public long getSendingIntervalmillis() {
        return sending_interval;
    }

    public String getHostIP() throws UnknownHostException {
        myipaddr = InetAddress.getLocalHost().getHostAddress();
        return myipaddr;
    }
}
