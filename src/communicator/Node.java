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
public class Node {
int identifier;
long milli_timestamp;
int input_port;
public int getIdentifier(){
return identifier;
}
public long getTimestamp(){
return milli_timestamp;
}
public void setIdentifier(int i){
identifier=i;
}
public void setTimestamp(long i){
milli_timestamp=i;
}
public void setInputPort(int i){
input_port=i;
}
public int getInputPort(){
return input_port;
}
public Node(int id,long timestmp,int port){
identifier=id;
milli_timestamp=timestmp;
input_port=port;

}

}
