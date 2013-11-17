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
public class StorageNodeServer {
    
    public static ServerSocket serverSocketSN = null;
    public static Socket clientSocketSN = null;    
    
    public StorageNodeServer(int port)
    {
        try {
          serverSocketSN = new ServerSocket(port);
        } catch (IOException e) {
          System.out.println(e);
        }
    }    
    
    public void startAcceptingConnections() throws IOException
    {
       while (true) 
       {
            try {
                
              clientSocketSN  = serverSocketSN.accept();
              (new StorageNodeServerThread(clientSocketSN)).start();

            } catch (IOException e) {
              System.out.println(e);
            }
        }
    }
}
