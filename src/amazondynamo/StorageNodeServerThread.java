/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package amazondynamo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StorageNodeServerThread extends Thread{
    
    private Socket clientSocketSN = null;
    
    public StorageNodeServerThread(Socket clientSocketSN)
    {
      this.clientSocketSN = clientSocketSN;
    }
    
     @Override
     public void run() {
        while(true)
        {
            try {
                ObjectInputStream is = new ObjectInputStream(clientSocketSN.getInputStream()); 
                                
                while (true) 
                {
                    if (parseObject(is.readObject()) == 1)
                        break;
                }
                
                is.close();                
                clientSocketSN.close();
                
            } catch (IOException e) {
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(LoadBalancerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
     }
     
     public int parseObject(Object o) throws IOException
     {
        if(o instanceof Command )
        {
            Command command = (Command)o;
            System.out.println(command);  
            return 0;
        }
        else 
            return 1;
     }  
}
