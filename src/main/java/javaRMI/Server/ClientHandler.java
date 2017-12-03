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
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ClientHandler extends UnicastRemoteObject implements ServerInterface {

    private static Database DB;
    ArrayList<ClientInterface> Clientlist = new ArrayList<ClientInterface>();

    public ClientHandler() throws RemoteException {
        DB = new Database();
    }

    @Override
    public void storeClient(ClientInterface remoteNode) {
        Clientlist.add(remoteNode);
    }

    @Override
    public void removeClient(ClientInterface remoteNode) {
        Clientlist.add(remoteNode);
    }

    public void broadMsg(String msg) {
        for (ClientInterface ci : Clientlist) {
            try {
                ci.recvMsg(msg);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public synchronized boolean modifyFile(String username, String filename, String attribute, String modification) {
        ArrayList<String[]> bigList = list();
        for (int i = 0; i < bigList.size(); i++) {
            String[] list = bigList.get(i);
            if (filename.equals(list[0]) && !(list[1].equals(username))) {
                if (list[2].equals("private") && !(list[1].equals(username))) {
                    return false;
                }
                if (list[2].equals("public") && list[3].equals("read")) {
                    return false;
                }
            }
        }
        switch (attribute) {
            case "filename": {
                DB.modifyFile(filename, attribute, modification);
                broadMsg(filename + " has changed name to " + modification + ".");
                return true;
            }
            case "permission": {
                switch (modification) {
                    case "public": {
                        DB.modifyFile(filename, attribute, modification);
                        broadMsg(filename + "has been set public.");
                        return true;
                    }
                    case "private": {
                        DB.modifyFile(filename, attribute, modification);
                        broadMsg(filename + "has been set private");
                        return true;
                    }
                }
            }
            case "readwrite": {
                switch (modification) {
                    case "read": {
                        DB.modifyFile(filename, attribute, modification);
                        broadMsg(filename + "has been set to read only.");
                        return true;
                    }
                    case "write": {
                        DB.modifyFile(filename, attribute, modification);
                        broadMsg(filename + "has been set to write.");
                        return true;
                    }
                }
            }
            default:
                return false;
        }
    }

    @Override
    public void unregister(String username) {
        DB.unregister(username);
    }

    @Override
    public boolean insertFile(String filename, String user, String permissions, String rw) {
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
}
