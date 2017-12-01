/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaRMI.Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Hello extends Remote{
    public String sayHello() throws RemoteException;
}
