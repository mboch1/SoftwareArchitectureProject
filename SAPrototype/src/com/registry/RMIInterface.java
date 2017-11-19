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
			ArrayList<Boolean> promotion1, ArrayList<Boolean> promotion2, ArrayList<Boolean> promotion3,  ArrayList<Integer> x, ArrayList<Integer> y, ArrayList<Double> discount)
			throws RemoteException;

	public ArrayList<String> getItemNames() throws RemoteException;

	public ArrayList<Double> getItemPrices() throws RemoteException;

	public ArrayList<Double> getTransportCost() throws RemoteException;

	public ArrayList<Integer> getItemMaxQty() throws RemoteException;

	public ArrayList<Integer> getItemTotalSold() throws RemoteException;

	public ArrayList<Integer> getItemNumber() throws RemoteException;

	public ArrayList<Boolean> getPromo1() throws RemoteException;
	
	public ArrayList<Integer> getPromo1X() throws RemoteException;
	
	public ArrayList<Integer> getPromo1Y() throws RemoteException;
	
	public ArrayList<Boolean> getPromo2() throws RemoteException;
	
	public ArrayList<Double> getPromo2Discount() throws RemoteException;

	public ArrayList<Boolean> getPromo3() throws RemoteException;
	// user loyalty card remote methods:
	public void setClientDiscountCard(String n, String s, double d, int i) throws RemoteException;
	// special offer management methods:
	public void updateDefaultOptions(int x, int y, double d) throws RemoteException;

	
}
