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
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jamesostmann
 */
public class SingleGPAServer {
    
    private static final HashMap<String,Double>GPA_TABLE;
    private DataInputStream fromClient = null;
    private DataOutputStream toClient = null;
    private ServerSocket server = null;
    
    static {
        GPA_TABLE = new HashMap<>();
        initTable();
    }

    
    public static void main(String[] args) {
        new SingleGPAServer(9876);
    }
    
    public SingleGPAServer(int port) {
      
        try {
        
            server = new ServerSocket(port);
            System.out.println("Starting server on port " + port);
            
            Socket socket = server.accept();
            System.out.println("Accepted connection from Client");
            
            fromClient = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            toClient = new DataOutputStream(socket.getOutputStream());
            
            String clientMessage = fromClient.readUTF();
            
            System.out.println("Recieved message from client:  " + clientMessage);
            
            while(notFinished(clientMessage)) {
                
                String reply = calculateGPA(clientMessage);
                toClient.writeUTF(reply);
                
                clientMessage = fromClient.readUTF();
                System.out.println("Recieved message from client:  " + clientMessage);
            }
            
            socket.close();
            server.close();
            toClient.close();
            fromClient.close();
            
            System.out.println("Server and Socket closed");
            
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
        return !clientMessage.trim().equals("Ok"); 
    }
    
    private String calculateGPA(String clientMessage) {
        
        String[] message = clientMessage.split(", ");
        int numGrades = Character.getNumericValue(message[0].charAt(0));
        int i;
        int semesterCredits = 0;
        double semesterTotal = 0;
        
        for (i = 1; i < numGrades * 2; i+=2) {
            semesterCredits += Integer.parseInt(message[i+1]);
            semesterTotal += (GPA_TABLE.get(message[i]) * Integer.parseInt(message[i+1]));
        }
        
        DecimalFormat df = new DecimalFormat("#.##");
        String semesterGPA = df.format(semesterTotal / semesterCredits);
        
        double prevGPA = Double.parseDouble(message[i++]);
        int prevCreditHrs = Integer.parseInt(message[i]);
        double cumulativeGPANum = ((prevGPA * prevCreditHrs) + semesterTotal) / (prevCreditHrs += semesterCredits);
        String cumulativeGPA = df.format(cumulativeGPANum);
        
        return semesterGPA + ", " + cumulativeGPA + ", " + String.valueOf(prevCreditHrs);
        
        /*
        int real = 0;
        
        for(i = 0; i < numGrades; i++) {
            System.out.println(grades[real + 1] + " " + grades[real+2]);
            real+=2;
        }
        */
    }   
}