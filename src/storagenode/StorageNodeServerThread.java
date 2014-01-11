/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storagenode;

import amazondynamo.Command;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import loadbalancer.LoadBalancerThread;

public class StorageNodeServerThread extends Thread{
    
    private Socket clientSocketSN = null;
    StorageNodeServer server;
    
    public StorageNodeServerThread(Socket clientSocketSN, StorageNodeServer server)
    {
      this.clientSocketSN = clientSocketSN;
      this.server = server;
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
            System.out.println("la storage node server thread " + command); 
            if (command.msgType == Command.GET){
                String getResult = null;
                if (server.storageHash.containsKey(command.key))
                       getResult = (String)server.storageHash.get(command.key);
                StorageNodeClient cl = new StorageNodeClient("localhost", command.clientPort);
                cl.send(getResult);
            }
            if (command.msgType == Command.PUT){
                server.storageHash.put(command.key, command.obj);
            }
            
            return 0;
        }
        else if (o instanceof Vector){
            server.storageNodes = (Vector<StorageNodeMetadata>)o;
            System.out.println(server.storageNodes);
            return 0;
        }
        else 
            return 1;
     }  
}
