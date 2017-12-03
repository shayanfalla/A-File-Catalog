/*
 * Copyright (C) 2017 Shayan Fallahian shayanf@kth.se
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package javaRMI.Server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws NotBoundException, RemoteException {

        boolean session = true;
        boolean loggedIn = false;
        MessageInterface stub = establishConnection();

        System.out.println(stub.sayHello());

        do {
            credentials(stub);
            loggedIn = true;
            while (loggedIn) {
                System.out.println("Enter command: ('help' for list of commands)");
                String command = input.nextLine().toLowerCase();
                String[] filename = command.split("\\s+");
                switch (filename[0]) {
                    case "help": {
                        System.out.println("upload <file> <public/private> <read/write>\nquit\nlist\nlogout");
                        break;
                    }
                    case "quit": {
                        System.out.println("Shutting down...");
                        return;
                    }
                    case "list": {
                        listFiles(stub);
                        break;
                    }
                    case "logout": {
                        System.out.println("Logging out.");
                        loggedIn = false;
                        break;

                    }
                    case "upload": {
                        if (insertFile(filename, stub)) {
                            break;
                        }
                    }
                    default: {
                        System.out.println("Invalid command. Try again.");
                        break;
                    }

                }

            }
        } while (session);
    }

    private static boolean insertFile(String[] filename, MessageInterface stub) throws RemoteException {
        if (filename.length == 4) {
            if ((filename[2].equals("public") || filename[2].equals("private"))
                    && (filename[3].equals("read") || filename[3].equals("write"))) {
                if (stub.insertFile(filename[1], "me", filename[2], filename[3])) {
                    System.out.println("File added.");
                    return true;
                }
            }
        }
        return false;
    }

    private static void listFiles(MessageInterface stub) throws RemoteException {
        ArrayList<String[]> temp = stub.list();
        for (int i = 0; i < temp.size(); i++) {
            String[] list = temp.get(i);
            System.out.print("File " + (i + 1) + ": ");
            for (int j = 0; j < list.length; j++) {
                System.out.print(" " + list[j]);
            }
            System.out.println();
        }
    }

    private static MessageInterface establishConnection() throws NotBoundException, RemoteException {
        Registry registry = LocateRegistry.getRegistry(null);
        MessageInterface stub = (MessageInterface) registry.lookup("HelloServer");
        return stub;
    }

    private static void credentials(MessageInterface stub) throws RemoteException {
        System.out.print("\nEnter username: ");
        String username = input.nextLine().toLowerCase();
        System.out.print("\nEnter password: ");
        String password = input.nextLine().toLowerCase();
        System.out.println("\n" + stub.credentials(username, password) + "\n");
    }
}
