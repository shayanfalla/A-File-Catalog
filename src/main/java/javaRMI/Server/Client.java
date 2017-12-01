/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaRMI.Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 *
 * @author Shayan
 */
public class Client {

    private Client() {
    }

    private static final Scanner input = new Scanner(System.in);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String host = null;

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            MessageInterface stub = (MessageInterface) registry.lookup("HelloServer");
            conversation(stub, input);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private static void conversation(MessageInterface stub, Scanner input) throws RemoteException {
        System.out.println(stub.sayHello());
        System.out.print("\nEnter username: ");
        String username = input.nextLine().toLowerCase();
        System.out.print("\nEnter password: ");
        String password = input.nextLine().toLowerCase();
        System.out.println(stub.credentials(username, password));
    }
}
