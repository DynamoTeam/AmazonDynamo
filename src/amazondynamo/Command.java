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
   
   int msgType;
   
  
   String key; 
   Context context;
   Object obj;
   
   public Command(int msgType, String key)
   {
       this.msgType = msgType;
       this.key = key;
   }    
    
   public Command(int msgType, String key, Context context, Object obj)
   {
       this.msgType = msgType;
       this.key = key;
       this.context = context;
       this.obj = obj;
                
   }
   
   public String toString()
   {
       return key + "  " + obj.toString();
   }
}