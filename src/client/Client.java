/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import amazondynamo.Command;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
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
    
    private static ServerSocket serverSocket = null;
    static int ownServerPort;
    public static int storageNodePort = 0;
    public static final Object countLock = new Object();
    
       
    //TODO : send key to load balancer
    // delete host and port and set them 
    
    void SendPut(int key, Object obj) throws IOException
    {
        Command put = new Command(Command.PUT, key, null, obj);   
        put.clientPort = ownServerPort;
        send(put, hostLB, portLB);
    }
    
    void SendGet(int key) throws IOException
    {
        Command get = new Command(Command.GET, key);      
        get.clientPort = ownServerPort;
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
                
                synchronized(countLock) {
                    try {
                        countLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
                System.out.println("storageNodePort= " + storageNodePort);
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
            ClientServer clServer = new ClientServer();
            Thread t = new Thread(clServer);
            t.start();
            ownServerPort = clServer.getPort();
            client.readLines();
    }   
    
}
