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
	private static Database db ;
	
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
		System.out.println("Adding item"+item);
		db.addItem(item, price, transportCost, maxItem);
		System.out.println("Item added!");
	}

	@Override
	public ArrayList<String> getItemNames() throws RemoteException {
		//pack to export:
		ArrayList<String> names = new ArrayList<>();
		names.addAll(db.getItems());
		return names;
	}
	
	@Override
	public ArrayList<Double> getItemPrices() throws RemoteException {
		//pack to export:
		ArrayList<Double> prices = new ArrayList<>();
		prices.addAll(db.getPrices());
		return prices;
	}
	
	@Override
	public ArrayList<Double> getTransportCost() throws RemoteException {
		//pack to export:
		ArrayList<Double> tCost = new ArrayList<>();
		tCost.addAll(db.getDelivery());
		return tCost;
	}
	
	@Override
	public ArrayList<Integer> getItemNumber() throws RemoteException {
		//pack to export:
		ArrayList<Integer> tNum = new ArrayList<>();
		tNum.addAll(db.getQuanity());
		return tNum;
	}
	
	@Override
	public ArrayList<Integer> getItemTotalSold() throws RemoteException {
		//pack to export:
		ArrayList<Integer> tSold = new ArrayList<>();
		tSold.addAll(db.getTotalSold());
		return tSold;
	}
	
	@Override
	public ArrayList<Integer> getItemMaxQty() throws RemoteException {
		//pack to export:
		ArrayList<Integer> tStock = new ArrayList<>();
		tStock.addAll(db.getMaxStock());
		return tStock;
	}
	
	@Override
	public ArrayList<Boolean> getPromo1() throws RemoteException {
		//pack to export:
		ArrayList<Boolean> tP1 = new ArrayList<>();
		tP1.addAll(db.getPromotion1());
		return tP1;
	}
	
	@Override
	public ArrayList<Boolean> getPromo2() throws RemoteException {
		//pack to export:
		ArrayList<Boolean> tP2 = new ArrayList<>();
		tP2.addAll(db.getPromotion2());
		return tP2;
	}
	
	@Override
	public ArrayList<Boolean> getPromo3() throws RemoteException {
		//pack to export:
		ArrayList<Boolean> tP3 = new ArrayList<>();
		tP3.addAll(db.getPromotion3());
		return tP3;
	}
	
	@Override
	public ArrayList<Integer> getPromo1X() throws RemoteException {
		ArrayList<Integer> gP1X = new ArrayList<>();
		gP1X.addAll(db.getPromotion1X());
		return gP1X;
	}

	@Override
	public ArrayList<Integer> getPromo1Y() throws RemoteException {
		ArrayList<Integer> gP1Y = new ArrayList<>();
		gP1Y.addAll(db.getPromotion1Y());
		return gP1Y;
	}

	@Override
	public ArrayList<Double> getPromo2Discount() throws RemoteException {
		ArrayList<Double> dsc = new ArrayList<>();
		dsc.addAll(db.getPromotion2Discount());
		return dsc;
	}

	@Override
	public void updateItemDB(ArrayList<String> items, ArrayList<Double> prices, ArrayList<Double> delivery,
			ArrayList<Integer> quanity, ArrayList<Integer> totalSold, ArrayList<Integer> maxStock,
			ArrayList<Boolean> promotion1, ArrayList<Boolean> promotion2, ArrayList<Boolean> promotion3,  ArrayList<Integer> xUpdate, ArrayList<Integer> yUpdate, ArrayList<Double> discountUpdate)
			throws RemoteException {
			db.updateDB(items, prices, delivery, quanity, totalSold, maxStock, promotion1, promotion2, promotion3, xUpdate, yUpdate, discountUpdate);
			System.out.println("Database Updated!");
	}

	@Override
	public void updateDefaultOptions(int x, int y, double d) throws RemoteException {
		db.setDefaults(x, y, d);
		System.out.println("Default settings for promotions changed.");
	}

	@Override
	public void setClientDiscountCard(String n, String s, double d, int i) throws RemoteException {
		db.addNewClient(n, s, d, i);
		System.out.println("New Client loyalty card added!");
	}

}
