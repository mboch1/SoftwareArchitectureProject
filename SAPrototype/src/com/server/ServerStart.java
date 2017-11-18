package com.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;

import com.registry.RMIInterface;

public class ServerStart extends UnicastRemoteObject implements RMIInterface {

	protected ServerStart() throws RemoteException {
		super();
	}

	private static final long serialVersionUID = 1L;
	private static Database db;
	
	//In the main method we bind the server on localhost with the name “MyServer”.
	public static void main(String[] args) {
		try{
			Naming.rebind("//localhost/MyServer", new ServerStart());
			System.err.println("Server Ready");
			db = new Database();
			System.err.println("Database Connected");
			
		} catch(Exception e) {
			System.err.println("Server exception: "+ e.toString());
			e.printStackTrace();
		}
	}
	@Override
	public ArrayList<String> getEnablingResult(ArrayList<String> data) throws RemoteException {
		
		ArrayList<String> result = new ArrayList<>();
		System.out.println("Enabling processing...");
		
		Random rd = new Random();
		int i = rd.nextInt(100);
		
		if(i>14) {		
			double total = Double.parseDouble(data.get(1))*1.2;
			result.add("Client ID: "+data.get(0)+"\nFull name: "+data.get(2)+" "+data.get(3)+"\n has been accepted for Enabling payment \nTotal to pay: "+Double.toString(total)+" includes 20% interest");
			result.add("accepted");
			result.add(Double.toString(total));
			return result;
		}
		else {
			result.add("Client ID: "+data.get(0)+"\nFull name: "+data.get(2)+" "+data.get(3)+"\n has been rejected for Enabling payment.");
			result.add("rejected");
			return result;
		}
	}
	@Override
	public void payWithEnabling(double toPayment) throws RemoteException {
		//REGISTER PAYMENT IN THE DATABASE
		System.out.println("received transaction: "+toPayment);
		db.addTransaction(toPayment);
	}
	@Override
	public void addItemToWarehouse(String item, double price, double transportCost, int maxItem) throws RemoteException {
		//ADD ITEM TO DB
		db.addItem(item, price, transportCost, maxItem);
	}
	
}
