/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zeromq_middleman;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.zeromq.ZMQ;

/**
 *
 * @author dave
 */
public class Zeromq_middleman {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ZMQ.Context context = ZMQ.context(1);
        
        ZMQ.Socket receiver = context.socket(ZMQ.PULL);
        receiver.connect("tcp://localhost:5557");
        
        while(true) {
            byte[] message;
            
            message = receiver.recv(0);
            
            if (message != null) {
                // Need to read first line of message, as that contains the filename.
                // Rest of message is the body
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(message)));
                    String filename = br.readLine();
                    System.out.print("Message Recieved");
                    
                    Zeromq_middleman.writeFile(message, filename);
                    System.out.print("\n");
                }
                catch(Exception e) {}
            }
        }
    }
    
    public static void writeFile(byte[] data, String filename) throws IOException {
        
        OutputStream out = new FileOutputStream("/home/dave/Desktop/zeromq/java/inbox/" + filename);
        
        try {
            out.write(data);
            System.out.print(" - Written to: " + filename);
        }
        catch(FileNotFoundException e) {
            System.out.print(" - Not written");
        }
        finally {
            out.close();
        }
    }
}
