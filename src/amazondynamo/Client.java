/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package amazondynamo;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author laura
 */
public class Client {
    
    static String host = "localhost";
    static int port = 2222;
    private static Socket clientSocket = null;
    private static ObjectOutputStream output = null;
    
    public static void initConnection() throws IOException
    {
        clientSocket = new Socket(host, port);
        output = new ObjectOutputStream(clientSocket.getOutputStream());
    }
    
    public static void closeConnection() throws IOException
    {
        output.close();
        clientSocket.close();
    }
    
    public void SendPut(String key, Object obj) throws IOException
    {
        Command put = new Command(Command.PUT, key, null, obj);        
        output.writeObject(put);
    }
    
    public void SendGet(String key) throws IOException
    {
        Command get = new Command(Command.GET, key);        
        output.writeObject(get);
    }
    
    public static void main(String[] args)
    {
        try {            
            
            initConnection();
            
            
        } catch (UnknownHostException ex) {
            System.err.println("Don't know about host " + host);
        } catch (IOException ex) {
             System.err.println("Couldn't get I/O for the connection to the host " + host);
        }
        
        
        
    }
}
