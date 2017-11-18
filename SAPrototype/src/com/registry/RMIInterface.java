package com.registry;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RMIInterface extends Remote {
	//enabling object
	public ArrayList<String> getEnablingResult(ArrayList<String> data) throws RemoteException;
	//pay with enabling:
	public void payWithEnabling(double payment) throws RemoteException;
	
	//warehouse:
	public void addItemToWarehouse(String item, double price, double transportCost, int maxItem) throws RemoteException;
	
}
