/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storagenode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author laura
 */
public class StorageNodeServer {
    
    public static ServerSocket serverSocketSN = null;
    public static Socket clientSocketSN = null; 
    HashMap storageHash;
    Vector<StorageNodeMetadata>  storageNodes;
    
    public StorageNodeServer(int port)
    {
        storageHash = new HashMap();
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
              (new StorageNodeServerThread(clientSocketSN, this)).start();

            } catch (IOException e) {
              System.out.println(e);
            }
        }
    }
}
