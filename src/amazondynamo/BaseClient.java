/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazondynamo;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author cristiana
 */
public class BaseClient {
    static Socket clientSocket = null;
    static ObjectOutputStream output = null;
  
     
    public static void initConnection(String host, int port) throws IOException
    {
        clientSocket = new Socket(host, port);
        output = new ObjectOutputStream(clientSocket.getOutputStream());
    }
    
    public static void closeConnection() throws IOException
    {
        output.close();
        clientSocket.close();
    }
    
    void send(Object obj, String host, int port) throws IOException
    {
        initConnection(host, port);
        output.writeObject(obj);
        closeConnection();
    }
}
