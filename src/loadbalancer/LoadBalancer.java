/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loadbalancer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import storagenode.StorageNodeMetadata;

/**
 *
 * @author laura
 */
public class LoadBalancer {
    
    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;
    
    private static final int port = 2223;
    
    static Vector<StorageNodeMetadata> storageNodes;
    static int moduloRange=100;
    
    
    public LoadBalancer(int port)
    {
        // Open a load balancer socket on the portNumber 
        try {
          serverSocket = new ServerSocket(port);
        } catch (IOException e) {
          System.out.println(e);
        }
        storageNodes = new Vector<>();
    }

    public static StorageNodeMetadata getNode(int key){
        StorageNodeMetadata nodeMeta;
        
        int hashKey = key % moduloRange;
        
        if (storageNodes.isEmpty())
                return null;
        else
        {
            
            nodeMeta = null;            
            int diff = moduloRange;
            
            for (int i=0; i<storageNodes.size(); i++){
                StorageNodeMetadata node = storageNodes.get(i);
                int nodeId = node.getID();
                if ((nodeId > hashKey) && (nodeId - hashKey < diff))
                {
                    nodeMeta = node;
                    diff = nodeId - hashKey;
                }
            }
            
            if (nodeMeta == null)//when the key is store on the first node
                    {
                        //find the node with the smallest id
                        nodeMeta = storageNodes.get(0);
                        for (int i=1; i<storageNodes.size(); i++)
                            if (storageNodes.get(i).getID()< nodeMeta.getID())
                                nodeMeta = storageNodes.get(i);
                    }
            return nodeMeta;
        }
    }
    
    public static void main(String args[])
    {
        
        LoadBalancer loadBalancer = new LoadBalancer(port);
         
        /*
         * Create a client socket for each connection and pass it to a new client
         * thread.
         */
        while (true) {
          try {
            clientSocket = serverSocket.accept();
            
            (new LoadBalancerThread(clientSocket)).start();
                        
          } catch (IOException e) {
            System.out.println(e);
          }
        }

    }
}
