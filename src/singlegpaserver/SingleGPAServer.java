/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package singlegpaserver;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jamesostmann
 */
public class SingleGPAServer {
    
    private static final HashMap<String,Double>GPA_TABLE;
    DataInputStream from = null;
    DataOutputStream to = null;
    ServerSocket server = null;
    
    static {
        GPA_TABLE = new HashMap<>();
        initTable();
    }
    //private static int port = 9876;

    public SingleGPAServer(int port) {
        
        
        try {
        
            server = new ServerSocket(port);
            System.out.println("Starting server on port " + port);
            
            Socket socket = server.accept();
            System.out.println("Accepted connection from Client");
            
            from = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            to = new DataOutputStream(socket.getOutputStream());
            
            String clientMessage = from.readUTF();
            
            System.out.println("Recieved message from client:  " + clientMessage);
            
            while(notFinished(clientMessage)) {
                String reply = calculateGPA(clientMessage);
                
            }
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(SingleGPAServer.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    
    private static void initTable() {
        GPA_TABLE.put("A",4.0);
        GPA_TABLE.put("A-",3.7);
        GPA_TABLE.put("B+",3.3);
        GPA_TABLE.put("B",3.0);
        GPA_TABLE.put("B-",2.7);
        GPA_TABLE.put("C+",2.3);
        GPA_TABLE.put("C",2.0);
        GPA_TABLE.put("C-",1.7);
        GPA_TABLE.put("D+",1.3);
        GPA_TABLE.put("D",1.0);
        GPA_TABLE.put("F",0.0);
    }
    
    private boolean notFinished(String clientMessage) {
        return !clientMessage.trim().equals("stop"); 
    }
    
    private String calculateGPA(String clientMessage) {
        
    }
    
    private String getSum(String message) {
        int length = Character.getNumericValue(message.charAt(0));
        int sum = 0;
        
        for(int i = 0; i < length; i++) {
            sum += Character.getNumericValue(message.charAt(i + 1));
        }
        
        return String.valueOf(sum);
    }
    
    public static void main(String args[]) throws IOException, ClassNotFoundException {
        server = new ServerSocket(port);
        while (true) {
            System.out.println("Waiting for the client....");
            
            // Accept new connection
            Socket socket = server.accept();
            
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();
            
            
            System.out.println("Message Received: " + message);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            String reply = "Done";
            if (!message.equalsIgnoreCase("exit")){
                reply = getSum(message);
            }
            oos.writeObject("Hi Client your sum is..." + reply);
            ois.close();
            oos.close();
            socket.close();
            
            if (message.equalsIgnoreCase("exit")) {
                break;
            }
        }
        
        System.out.println ("Shutting down Socket server!!");
        server.close();
        
    }
    
}


