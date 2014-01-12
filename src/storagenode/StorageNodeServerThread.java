/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storagenode;

import amazondynamo.Command;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
                if (!(e instanceof java.io.EOFException))
                    e.printStackTrace();
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
                System.out.println(server.clocks.get(command.key));
                StorageNodeClient cl = new StorageNodeClient("localhost", command.clientPort);
                cl.send(getResult);
            }
            if (command.msgType == Command.PUT){
                server.storageHash.put(command.key, command.obj);
                
                int nb = 0;
                int idaux;
                for (int i=0;i<server.storageNodes.size();i++){
                    if (server.storageNodes.get(i).getID() == server.nodeMetadata.getID()){
                        if (server.clocks.containsKey(command.key)){
                            //update the vector clock and send to the others
                            System.out.println("before put " + server.clocks.get(command.key));
                            for (int j=0;j<server.clocks.get(command.key).size();j++)
                                if (server.clocks.get(command.key).get(i).nodeId == server.nodeMetadata.getID()){
                                    server.clocks.get(command.key).get(i).clock = System.currentTimeMillis();
                                    break;
                                }
                            
                            System.out.println("after put " + server.clocks.get(command.key));
                        }
                        else{
                            ArrayList<NodeClock> vec = new ArrayList<NodeClock>();
                            while(nb < server.N){
                                idaux = i+nb;
                                idaux = idaux % server.storageNodes.size();
                                vec.add(new NodeClock(server.storageNodes.get(idaux).getID()));
                                nb++;
                            }
                            
                            server.clocks.put(command.key, vec);
                        }    
                        

                        break;
                    }


                } 
                
                //send the vector clock to the other replicas
                nb = 1;
                for (int i=0;i<server.storageNodes.size();i++){
                    if (server.storageNodes.get(i).getID() == server.nodeMetadata.getID()){
                        while (nb < server.N){
                            idaux = i+nb;
                            idaux = idaux % server.storageNodes.size();
                            StorageNodeClient cl = new StorageNodeClient("localhost", server.storageNodes.get(idaux).getPort());
                            ReplicationCommand rc = new ReplicationCommand(command.key, command.obj,
                                    server.clocks.get(command.key));
                            cl.send(rc);
                            System.out.println("clocks sent to " + server.storageNodes.get(idaux).getID() );
                            nb++;
                        }                        

                        break;
                    }
                } 
            }
            
            return 0;
        }
        else if (o instanceof Vector){
            server.storageNodes = (Vector<StorageNodeMetadata>)o;
            
            //sort server.storageNodes
            Collections.sort(server.storageNodes,new Comparator<StorageNodeMetadata>(){
                     public int compare(StorageNodeMetadata s1,StorageNodeMetadata s2){
                           return ((Integer)s1.getID()).compareTo((Integer)s2.getID());
                               
                     }});
            System.out.println(server.storageNodes);
             
            return 0;
        }
        else if (o instanceof ReplicationCommand){
            ReplicationCommand rc = (ReplicationCommand)o;
            System.out.println("received from the others " + rc);
            
            if (!server.clocks.containsKey(rc.key)){
                server.storageHash.put(rc.key, rc.obj);
                server.clocks.put(rc.key, rc.clocks);
            }
            else{
                //check if the local version is old
                //if yes, update it
                int smaller = 0;
                int bigger = 0;
                for (int i=0; i<rc.clocks.size();i++){
                    if (server.clocks.get(rc.key).get(i).clock < rc.clocks.get(i).clock)
                        smaller++;
                    else 
                        bigger++;
                }
                if (bigger == 0)
                    server.clocks.put(rc.key, rc.clocks);
                    
            }
            
            return 0;
        }
                
        else 
            return 1;
     }  
}
