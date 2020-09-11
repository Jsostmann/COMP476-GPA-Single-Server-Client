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
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jamesostmann
 */
public class SingleGPAClient {
    
    DataInputStream fromServer = null;
    DataOutputStream toServer = null;
    Socket socket = null;
   
    public static void main(String[] args) {
        new SingleGPAClient(9876,new String[] {"4, A-, 3, A, 3, C+, 4, B, 3, 2.88, 46"});
    }
    public SingleGPAClient(int port,String[] messages) {
        try {
            socket = new Socket(InetAddress.getLocalHost(),port);
            fromServer = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            toServer = new DataOutputStream(socket.getOutputStream());
            System.out.println(socket.getInetAddress());
            
            for(int i = 0; i < messages.length; i++) {
                System.out.println("Sending data to server...");
                toServer.writeUTF(messages[i]);
                String answer = fromServer.readUTF();
                System.out.println("Recieved message from Server: " + answer);

            }
            System.out.println("Sending stop to server...");
            toServer.writeUTF("stop");
            String answer = fromServer.readUTF();
            System.out.println("Recieved stop confirmation from server: " + answer);
            
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(SingleGPAClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SingleGPAClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
}
