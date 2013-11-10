/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package amazondynamo;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author laura
 */
public class LoadBalancer {
    
    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;
    
    // This server can accept up to maxClientsCount clients' connections.
    private static final int maxClientsCount = 10;
    private static final LoadBalancerThread[] threads = new LoadBalancerThread[maxClientsCount];
    
    
    public LoadBalancer(int port)
    {
        // Open a load balancer socket on the portNumber 
        try {
          serverSocket = new ServerSocket(port);
        } catch (IOException e) {
          System.out.println(e);
        }
        
    }
    
    public static void main(String args[])
    {
        
        LoadBalancer loadBalancer = new LoadBalancer(2223);
         
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
