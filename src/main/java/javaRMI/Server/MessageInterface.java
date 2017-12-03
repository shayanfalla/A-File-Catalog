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

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface MessageInterface extends Remote{
    public String sayHello() throws RemoteException;
    
    public String credentials(String username, String password) throws RemoteException;
    
    public ArrayList<String[]> list() throws RemoteException;
    
    public boolean insertFile(String filename, String user, String permissions, String rw) throws RemoteException;

    public void unregister(String username) throws RemoteException;
    
    public boolean modifyFile(String username, String filename, String attribute, String modification) throws RemoteException;
}
