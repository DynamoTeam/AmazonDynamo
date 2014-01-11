/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package loadbalancer;

import amazondynamo.Command;
import client.BaseClient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import storagenode.StorageNodeMetadata;

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
            } catch (InterruptedException ex) {
                Logger.getLogger(LoadBalancerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
     }
     
     public int parseObject(Object o) throws IOException, InterruptedException
     {
        if(o instanceof Command )
        {
            Command command = (Command)o;
            System.out.println("load balancer thread " + command);    
            System.out.println(LoadBalancer.getNode(command.key));
            
            BaseClient client = new BaseClient();
            client.send(LoadBalancer.getNode(command.key), 
                    "localhost", command.clientPort);
            return 0;
        }
        else if(o instanceof StorageNodeMetadata )
        {
            StorageNodeMetadata metadata = (StorageNodeMetadata)o;
            System.out.println(metadata);
            LoadBalancer.storageNodes.add(metadata);
           
            Thread.sleep(100);
            System.out.println(LoadBalancer.storageNodes);
            for (int i=0; i<LoadBalancer.storageNodes.size(); i++){
                BaseClient client = new BaseClient();
                client.send(LoadBalancer.storageNodes, 
                        "localhost", LoadBalancer.storageNodes.get(i).getPort());
                
            }
            return 0;
        }
        else 
            return 1;
     }    
    
}
