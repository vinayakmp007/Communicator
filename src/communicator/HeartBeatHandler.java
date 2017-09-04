/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communicator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.util.*;
import java.io.*;
import java.lang.NullPointerException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author vinayak
 */
public class HeartBeatHandler implements HttpHandler,Handler{
   
   Map<String,String> param =new HashMap<>();
   Map<String,String[]> req_headr=new HashMap<>(); 
   InputStream data_inpstream;
   String req_body,from_id_str;
   int req_bodysize,from_id_int;
   JSONObject trailer;
   
   @Override
   public void handle(HttpExchange con) throws IOException{
   
  // param=Handler.queryToMap(con.getRequestURI().getQuery());
   
   data_inpstream=con.getRequestBody();
   
   byte tm,tmbuf[]=new byte[4096];
   char tmp;
  StringBuilder tembuf=new StringBuilder();
   
 while((tm= (byte) data_inpstream.read()) !=-1){
 tmp=(char) tm;
     tembuf.append(tmp);
  
 }
  req_body=tembuf.toString();
  
  if(con.getRequestMethod().contentEquals("POST")){
      System.out.println(req_body);
       try {
           bodyToJSON();
       } catch (ParseException ex) {
           Logger.getLogger(HeartBeatHandler.class.getName()).log(Level.SEVERE, null, ex);
       }
      
  }
  else System.out.println(con.getRequestMethod());
 //  throw new IOException("not post");
   }
    void bodyToJSON() throws ParseException{
      if(req_body.isEmpty())throw new NullPointerException("Request Body is empty");
       JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(req_body);
        from_id_str = ((String) json.get("ID"));
        from_id_int=Integer.parseInt(from_id_str);
        trailer = (JSONObject) json.get("trailer");
        System.out.println(trailer);
        

                                                        //first json object will be the identifier of the sender 
        
        
         
   
   }
    
}
