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
                to.writeUTF("Done");
                
                clientMessage = from.readUTF();
                System.out.println("Recieved message from client:  " + clientMessage);
            }
            
            socket.close();
            to.close();
            from.close();
            
            System.out.println("Server closed");
            
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
        
        String[] grades = clientMessage.split(",");
        for(String g: grades) {
            System.out.println(g);
        }
        
        return "Done";
    }
    
    private String getSum(String message) {
        int length = Character.getNumericValue(message.charAt(0));
        int sum = 0;
        
        for(int i = 0; i < length; i++) {
            sum += Character.getNumericValue(message.charAt(i + 1));
        }
        
        return String.valueOf(sum);
    }   
}