package com.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.rmiinterface.RMIInterface;



public class ServerStart extends UnicastRemoteObject implements RMIInterface {

	protected ServerStart() throws RemoteException {
		super();
	}

	private static final long serialVersionUID = 1L;

	@Override
	public String helloTo(String name) throws RemoteException {
		System.err.println(name + " is trying to contact!");
		
		return "Server says hello to "+ name;
	}
	//In the main method we bind the server on localhost with the name “MyServer”.
	public static void main(String[] args) {
		try{
			Naming.rebind("//localhost/MyServer", new ServerStart());
			System.err.println("Server Ready");
		} catch(Exception e) {
			System.err.println("Server exception: "+ e.toString());
			e.printStackTrace();
		}
	}
}
