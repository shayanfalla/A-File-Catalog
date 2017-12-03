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

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Server extends UnicastRemoteObject implements MessageInterface {

    private static Database DB;

    public Server() throws RemoteException {
        super();
    }
    
    @Override
    public void unregister(String username){
        DB.unregister(username);
    }
    
    @Override
    public boolean insertFile(String filename, String user, String permissions, String rw){
        return DB.insertFile(filename, user, permissions, rw);
    }

    @Override
    public ArrayList<String[]> list() {
       return DB.getFilelist();   
    }

    @Override
    public String sayHello() {
        return "Welcome to the file catalog!";
    }

    @Override
    public String credentials(String username, String password) {
        if (DB.checkUser(username)) {
            if (DB.checkPass(username, password)) {
                return "You have been logged in.";
            } else {
                return "Your password was incorrect! Try again!";
            }
        } else {
            DB.insertUser(username, password);
        }
        return "That username did not exist.\nAn account has been created with the credentials given.";
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
