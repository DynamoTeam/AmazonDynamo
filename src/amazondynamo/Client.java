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
    static String hostLB = "localhost";
    static int portLB = 2223;    
    
    //TODO : send key to load balancer
    // delete host and port and set them 
    
    void SendPut(int key, Object obj) throws IOException
    {
        Command put = new Command(Command.PUT, key, null, obj);        
        send(put, hostLB, portLB);
    }
    
    void SendGet(int key) throws IOException
    {
        Command get = new Command(Command.GET, key);        
        send(get, hostLB, portLB);
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
                    SendPut(Integer.parseInt(tokens[1]),  tokens[2]);
                }
                else if(tokens[0].compareTo("get") == 0)
                {
                    System.out.println("get");
                    SendGet(Integer.parseInt(tokens[1]));
                } 
                
                //TODO
                //primeste de la LB
                //se conecteaza la Nod
                //
            }
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }    
    
    public static void main(String[] args)
    {
            Client client = new Client();
            client.readLines();
    }   
    
}
