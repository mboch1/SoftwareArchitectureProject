package com.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.registry.RMIInterface;

public class ServerStart extends UnicastRemoteObject implements RMIInterface {

	protected ServerStart() throws RemoteException {
		super();
	}

	private static final long serialVersionUID = 1L;
	private static Database db;

	// In the main method we bind the server on localhost with the name “MyServer”.
	public static void main(String[] args) {
		try {
			Naming.rebind("//localhost/MyServer", new ServerStart());
			System.err.println("Server Ready");
			db = new Database();
			System.err.println("Database Connected");
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
		// task scheduler for automatic db checks:
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				for (int i = 0; i < 25; i++) {
					db.makePurchase();
				}
				System.out.println("New purchase were made!");
				db.checkStock();
				System.out.println("Stock automatic check routine completed!");
			}
		}, 20000, 10000);

	}

	@Override
	public ArrayList<String> getEnablingResult(ArrayList<String> data) throws RemoteException {

		ArrayList<String> result = new ArrayList<>();
		System.out.println("Enabling processing...");

		Random rd = new Random();
		int i = rd.nextInt(100);

		if (i > 14) {
			double total = Double.parseDouble(data.get(1)) * 1.2;
			result.add("Client ID: " + data.get(0) + "\nFull name: " + data.get(2) + " " + data.get(3)
					+ "\n has been accepted for Enabling payment \nTotal to pay: " + Double.toString(total)
					+ " includes 20% interest");
			result.add("accepted");
			result.add(Double.toString(total));
			return result;
		} else {
			result.add("Client ID: " + data.get(0) + "\nFull name: " + data.get(2) + " " + data.get(3)
					+ "\n has been rejected for Enabling payment.");
			result.add("rejected");
			return result;
		}
	}

	@Override
	public void payWithEnabling(double toPayment) throws RemoteException {
		// REGISTER PAYMENT IN THE DATABASE
		System.out.println("received transaction: " + toPayment);
		db.addTransaction(toPayment);
	}

	@Override
	public void addItemToWarehouse(String item, double price, double transportCost, int maxItem)
			throws RemoteException {
		// ADD ITEM TO DB
		System.out.println("Adding item" + item);
		db.addItem(item, price, transportCost, maxItem);
		System.out.println("Item added!");
	}

	@Override
	public ArrayList<String> getItemNames() throws RemoteException {
		// pack to export:
		ArrayList<String> names = new ArrayList<>();
		names.addAll(db.getItems());
		return names;
	}

	@Override
	public ArrayList<Double> getItemPrices() throws RemoteException {
		// pack to export:
		ArrayList<Double> prices = new ArrayList<>();
		prices.addAll(db.getPrices());
		return prices;
	}

	@Override
	public ArrayList<Double> getTransportCost() throws RemoteException {
		// pack to export:
		ArrayList<Double> tCost = new ArrayList<>();
		tCost.addAll(db.getDelivery());
		return tCost;
	}

	@Override
	public ArrayList<Integer> getItemNumber() throws RemoteException {
		// pack to export:
		ArrayList<Integer> tNum = new ArrayList<>();
		tNum.addAll(db.getQuanity());
		return tNum;
	}

	@Override
	public ArrayList<Integer> getItemTotalSold() throws RemoteException {
		// pack to export:
		ArrayList<Integer> tSold = new ArrayList<>();
		tSold.addAll(db.getTotalSold());
		return tSold;
	}

	@Override
	public ArrayList<Integer> getItemMaxQty() throws RemoteException {
		// pack to export:
		ArrayList<Integer> tStock = new ArrayList<>();
		tStock.addAll(db.getMaxStock());
		return tStock;
	}

	@Override
	public ArrayList<Boolean> getPromo1() throws RemoteException {
		// pack to export:
		ArrayList<Boolean> tP1 = new ArrayList<>();
		tP1.addAll(db.getPromotion1());
		return tP1;
	}

	@Override
	public ArrayList<Boolean> getPromo2() throws RemoteException {
		// pack to export:
		ArrayList<Boolean> tP2 = new ArrayList<>();
		tP2.addAll(db.getPromotion2());
		return tP2;
	}

	@Override
	public ArrayList<Boolean> getPromo3() throws RemoteException {
		// pack to export:
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
	public String[] getUserIDs() throws RemoteException {

		ArrayList<Integer> uid = new ArrayList<>();
		uid.addAll(db.getUserCardID());
		int i = uid.size();

		String[] users = new String[i];
		for (int j = 0; j < i; j++) {
			users[j] = uid.get(j).toString();
		}

		return users;
	}

	@Override
	public void updateItemDB(ArrayList<String> items, ArrayList<Double> prices, ArrayList<Double> delivery,
			ArrayList<Integer> quanity, ArrayList<Integer> totalSold, ArrayList<Integer> maxStock,
			ArrayList<Boolean> promotion1, ArrayList<Boolean> promotion2, ArrayList<Boolean> promotion3,
			ArrayList<Integer> xUpdate, ArrayList<Integer> yUpdate, ArrayList<Double> discountUpdate)
			throws RemoteException {
		db.updateDB(items, prices, delivery, quanity, totalSold, maxStock, promotion1, promotion2, promotion3, xUpdate,
				yUpdate, discountUpdate);
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

	@Override
	public String getShopReport() throws RemoteException {
		String reportCreated = "Report on stock performance.\n";
		// get data from db to analyse it
		ArrayList<String> names = db.getItems();
		ArrayList<Integer> totalSold = db.getTotalSold();
		ArrayList<Double> prices = db.getPrices();
		ArrayList<Integer> maxStock = db.getMaxStock();
		ArrayList<Integer> onStock = db.getQuanity();
		ArrayList<Double> transactions = db.getTransactions();
		// user analysis:
		ArrayList<Integer> userCardID = db.getUserCardID();
		ArrayList<ArrayList<Double>> purchaseHistory = db.getPurchaseHistory();

		// for quantity min max alg
		int min = 2000000000;
		int bestMin = 0;
		int max = 0;
		int bestMax = 0;

		// find the best and worst selling items by quantity:
		for (int i = 0; i < names.size(); i++) {
			if (totalSold.get(i) > max) {
				max = totalSold.get(i);
				bestMax = i;
			}

			if (totalSold.get(i) < min) {
				min = totalSold.get(i);
				bestMin = i;
			}
		}
		reportCreated = reportCreated + "Best item sold in quantity is: " + names.get(bestMax) + " sold over: "
				+ totalSold.get(bestMax) + "\n";
		reportCreated = reportCreated + "Worst item sold in quantity is: " + names.get(bestMin) + " sold over: "
				+ totalSold.get(bestMin) + "\n \n";

		// for income min max alg
		double incMin = 99999999999999.0;
		int incBestMin = 0;
		double incMax = 0;
		int incBestMax = 0;

		// find the best selling item income-wise: total * value:
		for (int i = 0; i < names.size(); i++) {
			if ((double) totalSold.get(i) * prices.get(i) > incMax) {
				incMax = (double) totalSold.get(i) * prices.get(i);
				incBestMax = i;
			}
			if ((double) totalSold.get(i) * prices.get(i) < incMin) {
				incMin = (double) totalSold.get(i) * prices.get(i);
				incBestMin = i;
			}
		}
		reportCreated = reportCreated + "Best item sold in value is: " + names.get(incBestMax) + " sold over: "
				+ totalSold.get(bestMax) + " for: £" + Double.toString(Math.round(incMax)) + "\n";
		reportCreated = reportCreated + "Worst item sold in value is: " + names.get(incBestMin) + " sold over: "
				+ totalSold.get(bestMin) + " for: £" + Double.toString(Math.round(incMin)) + "\n \n";

		// for max and min number of items on stock (stock / max stock)
		double maxItemStock = 0.0;
		int maxPointer = 0;
		double minItemStock = 1.0;
		int minPointer = 0;

		// find the item which is most and least on stock by %
		for (int i = 0; i < names.size(); i++) {
			if (onStock.get(i) > 0) {
				if ((double) onStock.get(i) / (double) maxStock.get(i) > maxItemStock) {
					maxItemStock = (double) onStock.get(i) / (double) maxStock.get(i);
					maxPointer = i;
				}
				if ((double) onStock.get(i) / (double) maxStock.get(i) < minItemStock) {
					minItemStock = (double) onStock.get(i) / (double) maxStock.get(i);
					minPointer = i;
				}
			}
			// no item on stock, becomes automatically new min:
			else {
				minItemStock = 0.0;
				minPointer = i;
				reportCreated = reportCreated + "Item not on stock in: " + names.get(minPointer) + " "
						+ Double.toString(Math.round(minItemStock * 100)) + "[%]\n";
			}

		}
		reportCreated = reportCreated + "Item low on stock in [%]: " + names.get(minPointer) + " "
				+ Double.toString(Math.round(minItemStock * 100)) + "[%]\n";
		reportCreated = reportCreated + "Item mostly stocked in [%]: " + names.get(maxPointer) + " "
				+ Double.toString(Math.round(maxItemStock * 100)) + "[%]\n \n";

		// count total transactions
		double totalTransactions = 0.0;

		// transactions total value:
		for (int i = 0; i < transactions.size(); i++) {
			if (transactions.size() > 0) {
				totalTransactions = totalTransactions + transactions.get(i);
			}
		}
		reportCreated = reportCreated + "Total transactions made by Enabling value: £"
				+ Double.toString(Math.round(totalTransactions)) + "\n";

		// count total sales:
		double totalSum = 0.0;
		double totalAllClientsSum = 0.0;
		// total user sales value:
		for (int i = 0; i < purchaseHistory.size(); i++) {
			for (Double totalPurchased : purchaseHistory.get(i)) {
				totalSum += totalPurchased;
			}
			totalAllClientsSum += totalSum;
		}
		reportCreated = reportCreated + "Total user sales value: £" + Double.toString(Math.round(totalAllClientsSum))
				+ "\n";

		// find best client:
		double totalPurchasedMax = 0.0;
		int userPointer = 0;

		for (int i = 0; i < purchaseHistory.size(); i++) {

			double totalPurchased = 0.0;

			for (int j = 0; j < purchaseHistory.get(i).size(); j++) {
				totalPurchased += purchaseHistory.get(i).get(j);
			}
			if (totalPurchased > totalPurchasedMax) {
				totalPurchasedMax = totalPurchased;
				userPointer = i;
			}
		}
		reportCreated = reportCreated + "Best shop client is: " + userCardID.get(userPointer)
				+ " Made shopping for total: £" + Double.toString(Math.round(totalPurchasedMax)) + "\n";

		return reportCreated;
	}

	@Override
	public ArrayList<String> getUserData(int index) throws RemoteException {
		ArrayList<String> userData = new ArrayList<>();
		userData.add(db.getUserDiscount(index).toString());
		userData.add(db.getUserName(index));
		userData.add(db.getUserSurname(index));
		return userData;
	}

}
