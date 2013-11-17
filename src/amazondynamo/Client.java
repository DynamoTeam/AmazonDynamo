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
public class Client extends BaseClient{
    
    
    public static BufferedReader bufferReader;
    private static final String FILE_PATH = "/src/amazondynamo/commands";
   
    String host;
    int port;
    
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    void SendPut(String key, Object obj) throws IOException
    {
        Command put = new Command(Command.PUT, key, null, obj);        
        send(put, host, port);
    }
    
    void SendGet(String key) throws IOException
    {
        Command get = new Command(Command.GET, key);        
        send(get, host, port);
    } 
    
    void readLines()
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
            int portLB = 2223;  
            Client client = new Client("localhost", portLB);
            client.readLines();
            
    }   
    
}
