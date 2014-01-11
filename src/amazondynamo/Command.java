/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package amazondynamo;

import java.io.Serializable;

/**
 *
 * @author laura
 */
public class Command implements Serializable{
   public final static int GET = 0;
   public final static int PUT = 1;
   
   public int msgType; 
   public int key; 
   public Context context;
   public Object obj;
   public int clientPort;
   
   public Command(int msgType, int key)
   {
       this.msgType = msgType;
       this.key = key;
   }    
    
   public Command(int msgType, int key, Context context, Object obj)
   {
       this.msgType = msgType;
       this.key = key;
       this.context = context;
       this.obj = obj;
                
   }
   
   public String toString()
   {
       String str = "";
       if (this.msgType == GET){
           str+= "GET " + this.key;
       }
       else{
           str+= "PUT " + this.key + " " + this.obj.toString();
       }
       return str;
   }
}
