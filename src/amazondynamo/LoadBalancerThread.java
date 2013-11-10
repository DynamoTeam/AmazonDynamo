/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazondynamo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
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
                ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());        
                parseObject(input.readObject());        
                input.close();
                clientSocket.close();

            }   catch (ClassNotFoundException ex) {
                    Logger.getLogger(LoadBalancerThread.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException e) {
                e.printStackTrace();
            }
        }
     }
     
     public void parseObject(Object o)
     {
        if(o instanceof Command )
        {
            Command command = (Command)o;
            System.out.println(command);    
            
        }
     }    
    
}
