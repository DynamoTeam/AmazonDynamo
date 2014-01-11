/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

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
public class ClientThread extends Thread{
 
  private ObjectInputStream is = null;
  private Socket clientSocket = null;
  

  public ClientThread(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

    @Override
  public void run() {
    
    try {
        is = new ObjectInputStream(clientSocket.getInputStream());

        
        try {
            Object o = is.readObject();
            if (o instanceof StorageNodeMetadata) {
                StorageNodeMetadata m = null;
                m = (StorageNodeMetadata) o;

                synchronized(Client.countLock) {
                    Client.storageNodePort = m.getPort();
                    Client.countLock.notify();
                }
            }
            if (o instanceof String){
                String s = (String) o;
                synchronized(Client.countLock) {
                    Client.getResult = s;
                    Client.countLock.notify();
                }
                
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        is.close();
      clientSocket.close();
    } catch (IOException e) {
    }
  }
    
}
