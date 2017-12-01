/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaRMI.Server;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements MessageInterface {
    
    private static Database DB;

    public Server() throws RemoteException {
        super();
    }

    @Override
    public String sayHello() {
        return "Welcome!";
    }

    @Override
    public String credentials(String username, String password) {
        if(DB.checkUser(username)){
            return "You already exist";
        } else DB.insertUser(username, password);
        return "You have been added";
    }

    public static void main(String args[]) throws RemoteException {
         DB = new Database();
        try {
            //Bind the remote object to the registry
            LocateRegistry.createRegistry(1099);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("HelloServer", new Server());
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
