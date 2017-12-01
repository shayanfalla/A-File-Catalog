/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaRMI.Server;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
//import javaRMI.Server.Hello;

/**
 *
 * @author Shayan
 */
public class Client {

    private Client() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String host = null;
        
        try {
            Registry registry = LocateRegistry.getRegistry(host);
           //    Hello stub = (Hello) registry.lookup("HelloServer");
           Hello stub = (Hello) registry.lookup("HelloServer");
           String response = stub.sayHello();
           response = response + stub.sayHello();
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

