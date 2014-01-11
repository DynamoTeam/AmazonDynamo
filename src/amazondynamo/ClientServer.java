/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazondynamo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 *
 * @author cristiana
 */
public class ClientServer implements Runnable{
    
    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;
    
    private static int port;
    
   int getPort(){
        Random rand = new Random();
        int max = 4000;
        int min = 3000;
        this.port = rand.nextInt((max - min) + 1) + min;
        return port;
    }
    
    public void run(){
        // Open a server socket because the client should act
        // like a server, too
        try {
          serverSocket = new ServerSocket(port);
        } catch (IOException e) {
          System.out.println(e);
        }
       
         
        /*
         * Create a client socket for each connection and pass it to a new client
         * thread.
         */
        while (true) {
          try {
            clientSocket = serverSocket.accept();
            
            (new ClientThread(clientSocket)).start();
                        
          } catch (IOException e) {
            System.out.println(e);
          }
        }

    }
}
