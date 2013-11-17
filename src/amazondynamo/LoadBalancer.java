/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package amazondynamo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 *
 * @author laura
 */
public class LoadBalancer {
    
    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;
    
    private static final int port = 2223;
    
    static Vector<StorageNodeMetadata> storageNodes;
    
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
