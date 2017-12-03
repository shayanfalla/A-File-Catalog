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
    private static String user = null;
    private static boolean loggedIn = false;

    public static void main(String[] args) throws NotBoundException, RemoteException {

        MessageInterface stub = establishConnection();

        do {
            credentials(stub);
            while (loggedIn) {
                System.out.println("Enter command: ('help' for list of commands)");
                String command = input.nextLine().toLowerCase();
                String[] filename = command.split("\\s+");
                switch (filename[0]) {
                    case "unregister": {
                        stub.unregister(user);
                        loggedIn = false;
                        System.out.println("You have been deregistered.\n");
                        break;
                    }
                    case "help": {
                        System.out.println("\nList of commands:\nupload <file> <public/private> <read/write>\n"
                                + "modify <filename> <attribute> <new modification>\nquit\nlist\nlogout\nunregister\n");
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
                    case "modify": {
                        if (filename.length == 4) {
                            if (stub.modifyFile(user, filename[1], filename[2], filename[3])) {
                                System.out.println(filename[1] + " has been modified.");
                                break;
                            }
                        }
                        System.out.println("Invalid or unauthorized command, try again.");
                        break;
                    }
                    case "upload": {
                        if (insertFile(filename, stub)) {
                            break;
                        }
                    }
                    default: {
                        System.out.println("Invalid command, try again.");
                        break;
                    }
                }
            }
        } while (true);
    }

    private static boolean insertFile(String[] filename, MessageInterface stub) throws RemoteException {
        if (filename.length == 4) {
            if ((filename[2].equals("public") || filename[2].equals("private"))
                    && (filename[3].equals("read") || filename[3].equals("write"))) {
                if (stub.insertFile(filename[1], user, filename[2], filename[3])) {
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
            System.out.printf("FILE: %-20sOWNER:%-20sPUBLIC/PRIVATE: %-20sREAD/WRITE: %s", list[0], list[1], list[2], list[3]);
            System.out.println();
        }
    }

    private static MessageInterface establishConnection() throws NotBoundException, RemoteException {
        Registry registry = LocateRegistry.getRegistry(null);
        MessageInterface stub = (MessageInterface) registry.lookup("HelloServer");
        return stub;
    }

    private static void credentials(MessageInterface stub) throws RemoteException {
        System.out.println(stub.sayHello());
        System.out.print("\nEnter username('quit' to exit): ");
        String username = input.nextLine().toLowerCase();
        if (username.equals("quit")) {
            System.out.println("Shutting down...");
            System.exit(0);
        }
        System.out.print("\nEnter password: ");
        String password = input.nextLine().toLowerCase();
        String answer = stub.credentials(username, password);
        System.out.println("\n" + answer + "\n");
        if (answer.equals("Your password was incorrect! Try again!")) {
            return;
        }
        user = username;
        loggedIn = true;
    }
}
