/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package amazondynamo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
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
    
    public static BufferedReader bufferReader;
    private static final String FILE_PATH = "/src/amazondynamo/commands";
   
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
    
    static void SendPut(String key, Object obj) throws IOException
    {
        Command put = new Command(Command.PUT, key, null, obj);        
        output.writeObject(put);
    }
    
    static void SendGet(String key) throws IOException
    {
        Command get = new Command(Command.GET, key);        
        output.writeObject(get);
    } 
    
    static void readLines()
    {
        try {
            
            String currentLine;             
            bufferReader = new BufferedReader(new FileReader(System.getProperty("user.dir")+FILE_PATH));
            
            while ((currentLine = bufferReader.readLine()) != null)
            {
                Thread.sleep(2);
                String []tokens = currentLine.split(" ");
                if(tokens[0].compareTo("put") == 0)
                {
                    System.out.println("put");
                    SendPut(tokens[1],  tokens[2]);
                }
                else if(tokens[0].compareTo("get") == 0)
                {
                    System.out.println("get");
                    SendGet(tokens[1]);
                }            
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }    
    
    public static void main(String[] args)
    {
        try { 
            
            readLines();
            initConnection();
            
        } catch (UnknownHostException ex) {
            System.err.println("Don't know about host " + host);
        } catch (IOException ex) {
             System.err.println("Couldn't get I/O for the connection to the host " + host);
        }        
    }
}
