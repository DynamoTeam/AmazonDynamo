/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storagenode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
    StorageNodeMetadata nodeMetadata;

    static final int N = 3; //nb of replicas
    static final int R = 2; //nb of succesful reads
    static final int W = 2; //nb of succesful writes
    HashMap<Integer, ArrayList<NodeClock>> clocks;
    
    public StorageNodeServer(StorageNodeMetadata nodeMetadata)
    {
        storageHash = new HashMap();
        this.nodeMetadata = nodeMetadata;
        clocks = new HashMap<Integer, ArrayList<NodeClock>>();
        try {
          serverSocketSN = new ServerSocket(this.nodeMetadata.getPort());
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
