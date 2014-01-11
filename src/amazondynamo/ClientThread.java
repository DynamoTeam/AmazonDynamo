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
public class ClientThread extends Thread{
 
  private ObjectInputStream is = null;
  private Socket clientSocket = null;
  

  public ClientThread(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

    @Override
  public void run() {
    
    try {
      System.out.println("de la Storage Node");
      is = new ObjectInputStream(clientSocket.getInputStream());
      
      StorageNodeMetadata m = null;
          try {
              m = (StorageNodeMetadata) is.readObject();
              System.out.println("msg: " + m);
          } catch (ClassNotFoundException ex) {
              Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
          }
      
      is.close();
      clientSocket.close();
    } catch (IOException e) {
    }
  }
    
}
