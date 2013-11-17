/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazondynamo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cristiana
 */
public class LoadBalancerThread extends Thread{
    private Socket clientSocket = null;
    
    public LoadBalancerThread(Socket clientSocket)
    {
      this.clientSocket = clientSocket;
    }
    
     @Override
     public void run() {
        while(true){
            try {
                // Create input and output streams for this client.
                ObjectInputStream is = new ObjectInputStream(clientSocket.getInputStream()); 
                
                
                while (true) {
                    if (parseObject(is.readObject()) == 1)
                        break;
                }
                
                is.close();
                
                clientSocket.close();
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
            System.out.println(LoadBalancer.getNode(command.key));
            
            BaseClient client = new BaseClient();
            client.send(LoadBalancer.getNode(command.key), 
                    clientSocket.getInetAddress().getHostAddress(), clientSocket.getPort());
            return 0;
        }
        else if(o instanceof StorageNodeMetadata )
        {
            StorageNodeMetadata metadata = (StorageNodeMetadata)o;
            System.out.println(metadata);
            LoadBalancer.storageNodes.add(metadata);
           
            System.out.println(LoadBalancer.storageNodes);
            return 0;
        }
        else 
            return 1;
     }    
    
}
