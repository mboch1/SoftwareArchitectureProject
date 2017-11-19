package com.registry;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RMIInterface extends Remote {
	// enabling object
	public ArrayList<String> getEnablingResult(ArrayList<String> data) throws RemoteException;

	// pay with enabling:
	public void payWithEnabling(double payment) throws RemoteException;

	// warehouse and stock management remote methods:
	public void addItemToWarehouse(String item, double price, double transportCost, int maxItem) throws RemoteException;

	public void updateItemDB(ArrayList<String> items, ArrayList<Double> prices, ArrayList<Double> delivery,
			ArrayList<Integer> quanity, ArrayList<Integer> totalSold, ArrayList<Integer> maxStock,
			ArrayList<Boolean> promotion1, ArrayList<Boolean> promotion2, ArrayList<Boolean> promotion3)
			throws RemoteException;

	ArrayList<String> getItemNames() throws RemoteException;

	ArrayList<Double> getItemPrices() throws RemoteException;

	ArrayList<Double> getTransportCost() throws RemoteException;

	ArrayList<Integer> getItemMaxQty() throws RemoteException;

	ArrayList<Integer> getItemTotalSold() throws RemoteException;

	ArrayList<Integer> getItemNumber() throws RemoteException;

	ArrayList<Boolean> getPromo1() throws RemoteException;

	ArrayList<Boolean> getPromo2() throws RemoteException;

	ArrayList<Boolean> getPromo3() throws RemoteException;
	// user loyalty card remote methods:

	// special offer management methods:

}
