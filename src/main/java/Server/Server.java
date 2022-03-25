/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ControlClock.ControlClock;
import ControlClock.ClientThread;
import DB.DB;
/**
 *
 * @author Anastasiya
 */
public class Server {
    
    int port = 3125;
    InetAddress host;
    ControlClock clock = new ControlClock("Tree", 567, 9, 0, 5);
    
    public Server() {
        
        try {
            host = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        try {    
            ServerSocket ss = new ServerSocket(port, 0, host);
            System.out.println("Server started");
            DB db = new DB();
            db.connect();
            clock.set_alarms_from_db(db.GetListAlarms());
            clock.start();
            int count = 0;
            try {
            while (true) {
                Socket cs = ss.accept();
                
                ClientThread cc = new ClientThread(cs, clock, db);
                count++;
                System.out.println("Client " + count + " connect");
            }
            } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            ss.close();
        }
        //  cs.close();
            
        //    ss.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        new Server();
    } 
}
