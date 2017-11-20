package com.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import javax.mail.*;
import javax.mail.internet.*;
import com.sun.mail.smtp.SMTPTransport;

public class Database {

	// item features:
	private ArrayList<String> items;
	private ArrayList<Double> prices;
	private ArrayList<Double> delivery;
	private ArrayList<Integer> quanity;
	private ArrayList<Integer> totalSold;
	private ArrayList<Integer> maxStock;
	private ArrayList<Boolean> promotion1;
	private ArrayList<Integer> x;
	private ArrayList<Integer> y;
	private ArrayList<Boolean> promotion2;
	private ArrayList<Double> discount;
	private ArrayList<Boolean> promotion3;
	// other
	private ArrayList<String> name;
	private ArrayList<Integer> stock;
	private ArrayList<Integer> max;
	private ArrayList<String> toSend;
	private ArrayList<Integer> id;
	// stock watch
	double stockWatch = 0.1;
	// promotions default values
	private int defaultX = 0;
	private int defaultY = 0;
	private double defaultDiscount = 0.0;
	// enabling transaction history:
	private ArrayList<Double> transactions;
	// users:
	private ArrayList<String> userName;
	private ArrayList<String> userSurname;
	private ArrayList<Double> userDiscount;
	private ArrayList<Integer> cardNumber;
	private ArrayList<ArrayList<Double>> userPurchaseHistory;

	public Database() {
		// for users data:
		userName = new ArrayList<>();
		userSurname = new ArrayList<>();
		userDiscount = new ArrayList<>();
		cardNumber = new ArrayList<>();
		userPurchaseHistory = new ArrayList<>();
		// for processing:
		stock = new ArrayList<>();
		name = new ArrayList<>();
		max = new ArrayList<>();
		id = new ArrayList<>();
		toSend = new ArrayList<>();
		// for database:
		items = new ArrayList<>();
		prices = new ArrayList<>();
		delivery = new ArrayList<>();
		quanity = new ArrayList<>();
		totalSold = new ArrayList<>();
		maxStock = new ArrayList<>();
		promotion1 = new ArrayList<>();
		x = new ArrayList<>();
		y = new ArrayList<>();
		promotion2 = new ArrayList<>();
		discount = new ArrayList<>();
		promotion3 = new ArrayList<>();
		// external transactions
		transactions = new ArrayList<>();

		// one time read/generate data for db to populate it:
		populate();
		generateClients();
	}

	protected void makePurchase() {
		Random rd = new Random();
		//total shopping
		double price = 0.0;

		for (int i = 0; i < 5; i++) {

			int t = rd.nextInt(prices.size());
			if (quanity.get(t) > 0) {
				// if object is available, buy it and reduce qty by 1
				quanity.set(t, quanity.get(t) - 1);
				totalSold.set(t, totalSold.get(t) + 1);
				//paid delivery:
				if(promotion3.get(t)==false) {
					price = price + prices.get(rd.nextInt(prices.size()))+delivery.get(t);
				}
				//free delivery: 
				else {
					price = price + prices.get(rd.nextInt(prices.size()));
				}
			}

		}
		//apply user discount:
		int u = rd.nextInt(userDiscount.size());
		price = price * userDiscount.get(u);
		
		if (price != 0.0) {
			userPurchaseHistory.get(u).add(price);
		}
	}

	// use to populate db with clients and history of shopping
	private void generateClients() {
		Random rd = new Random();
		for (int z = 0; z < 100; z++) {
			// 24 characters:
			String characters = "ABCDEFGHIJKLMNOPRSTUVXYZ";
			// generate name
			String name = "";
			for (int i = 0; i < 10; i++) {
				name = name + characters.charAt(rd.nextInt(characters.length()));
			}
			userName.add(name);
			name = "";
			// generate surname
			String surname = "";
			for (int i = 0; i < 10; i++) {
				surname = surname + characters.charAt(rd.nextInt(characters.length()));
			}
			userSurname.add(surname);
			surname = "";
			// generate existing discount
			userDiscount.add((double) rd.nextInt(50));
			// generate card number
			cardNumber.add(rd.nextInt(666666));
			// generate purchase history
			ArrayList<Double> d = new ArrayList<>();
			for (int j = 0; j < rd.nextInt(100); j++) {
				d.add((double) rd.nextInt(20) + 1);
			}
			userPurchaseHistory.add(d);
		}
	}

	// read from file dataDB.txt in root folder:
	private void populate() {

		final String filename = "D:\\WorkSpace\\SoftwareArchitectureProject\\SAPrototype\\dataDB.txt";

		String line = null;
		try {

			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, "\t");
				items.add(st.nextToken());
				prices.add(Double.parseDouble(st.nextToken()));
				delivery.add(Double.parseDouble(st.nextToken()));
				quanity.add(Integer.parseInt(st.nextToken()));
				totalSold.add(Integer.parseInt(st.nextToken()));
				maxStock.add(Integer.parseInt(st.nextToken()));
				promotion1.add(Boolean.parseBoolean(st.nextToken()));
				x.add(defaultX);
				y.add(defaultY);
				promotion2.add(Boolean.parseBoolean(st.nextToken()));
				discount.add(defaultDiscount);
				promotion3.add(Boolean.parseBoolean(st.nextToken()));
			}
			System.out.println("Database Populated");
			System.out.println(items);
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + filename + "'");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	// getters:
	public ArrayList<String> getItems() {
		return items;
	}

	public ArrayList<Double> getPrices() {
		return prices;
	}

	public ArrayList<Double> getDelivery() {
		return delivery;
	}

	public ArrayList<Integer> getQuanity() {
		return quanity;
	}

	public ArrayList<Integer> getTotalSold() {
		return totalSold;
	}

	public ArrayList<Integer> getMaxStock() {
		return maxStock;
	}

	public ArrayList<Boolean> getPromotion1() {
		return promotion1;
	}

	public ArrayList<Integer> getPromotion1X() {
		return x;
	}

	public ArrayList<Integer> getPromotion1Y() {
		return y;
	}

	public ArrayList<Boolean> getPromotion2() {
		return promotion2;
	}

	public ArrayList<Double> getPromotion2Discount() {
		return discount;
	}

	public ArrayList<Boolean> getPromotion3() {
		return promotion3;
	}

	public ArrayList<Double> getTransactions() {
		return transactions;
	}

	public ArrayList<Integer> getUserCardID(){
		return cardNumber;
	}
	
	public String getUserName(int i){
		return userName.get(i);
	}
	
	public String getUserSurname(int i){
		return userSurname.get(i);
	}
	
	public Double getUserDiscount(int i){
		return userDiscount.get(i);
	}
	
	public ArrayList<ArrayList<Double>> getPurchaseHistory(){
		return userPurchaseHistory;
	}
	
	
	public double getStockWatch() {
		return stockWatch;
	}

	// setters:
	public void addNewClient(String n, String s, double d, int i) {	
		userName.add(n);
		userSurname.add(s);
		userDiscount.add(d);
		cardNumber.add(i);
		userPurchaseHistory.add(new ArrayList<Double>());
	}

	public void setStockReplenish() {
		for (int i = 0; i < id.size(); i++) {
			quanity.set(id.get(i), maxStock.get(id.get(i)));
		}
		System.out.println("Stock replaced!");
	}

	public void setDefaults(int dX, int dY, double dDiscount) {
		defaultX = dX;
		defaultY = dY;
		defaultDiscount = dDiscount;
	}

	// delete:
	public void removeObject(int i) {
		items.remove(i);
		prices.remove(i);
		delivery.remove(i);
		quanity.remove(i);
		totalSold.remove(i);
		maxStock.remove(i);
		promotion1.remove(i);
		x.remove(i);
		y.remove(i);
		promotion2.remove(i);
		discount.remove(i);
		promotion3.remove(i);
	}

	// update all:
	public void updateDB(ArrayList<String> itemsUpdate, ArrayList<Double> pricesUpdate,
			ArrayList<Double> deliveryUpdate, ArrayList<Integer> quanityUpdate, ArrayList<Integer> totalSoldUpdate,
			ArrayList<Integer> maxStockUpdate, ArrayList<Boolean> promotion1Update, ArrayList<Boolean> promotion2Update,
			ArrayList<Boolean> promotion3Update, ArrayList<Integer> xUpdate, ArrayList<Integer> yUpdate,
			ArrayList<Double> discountUpdate) {
		// remove old entries:
		if (items.size() > 0) {
			items.removeAll(items);
			prices.removeAll(prices);
			delivery.removeAll(delivery);
			quanity.removeAll(quanity);
			totalSold.removeAll(totalSold);
			maxStock.removeAll(maxStock);
			promotion1.removeAll(promotion1);
			x.removeAll(x);
			y.removeAll(y);
			promotion2.removeAll(promotion2);
			discount.removeAll(discount);
			promotion3.removeAll(promotion3);
		}
		// add new data from user:
		items.addAll(itemsUpdate);
		prices.addAll(pricesUpdate);
		delivery.addAll(deliveryUpdate);
		quanity.addAll(quanityUpdate);
		totalSold.addAll(totalSoldUpdate);
		maxStock.addAll(maxStockUpdate);
		promotion1.addAll(promotion1Update);
		x.addAll(xUpdate);
		y.addAll(yUpdate);
		promotion2.addAll(promotion2Update);
		discount.addAll(discountUpdate);
		promotion3.addAll(promotion3Update);
		// check stock:
		checkStock();

	}

	// add new
	public void addTransaction(double transaction) {
		System.out.println("Enabling transaction adding to the system...");
		System.out.println(transaction);
		transactions.add(transaction);
	}

	public void addItem(String item, double price, double transportCost, int maxItem) {
		items.add(item);
		prices.add(price);
		delivery.add(transportCost);
		quanity.add(0);
		totalSold.add(0);
		maxStock.add(maxItem);
		promotion1.add(false);
		x.add(defaultX);
		y.add(defaultY);
		promotion2.add(false);
		discount.add(defaultDiscount);
		promotion3.add(false);
		// check stock:
		checkStock();
	}

	protected void checkStock() {
		// first empty old arrays
		if (stock.size() > 0) {
			stock.removeAll(stock);
			max.removeAll(max);
			name.removeAll(name);
			toSend.removeAll(toSend);
			id.removeAll(id);
		}
		// get new data
		stock.addAll(this.getQuanity());
		max.addAll(this.getMaxStock());
		name.addAll(this.getItems());

		// check stock if any item is below set threshold add it to the list
		for (int i = 0; i < name.size(); i++) {
			if (stock.get(i) < max.get(i)) {
				if (max.get(i) * stockWatch > stock.get(i)) {
					toSend.add(name.get(i));
					id.add(i);
				}
			}
		}
		// once finished checkup routine send email:
		if (toSend.size() > 0) {
			sendEmail();
		}
	}

	private void sendEmail() {
		// Recipient's email ID needs to be mentioned.
		String to = "michalbochenek2@gmail.com";
		// Sender's email ID needs to be mentioned
		String from = "michalbochenek2@gmail.com";

		// Get system properties
		Properties properties = System.getProperties();
		// Setup mail server
		properties.setProperty("mail.smtp.from", from);
		properties.setProperty("mail.smtp.host", "smtp.gmail.com");
		properties.setProperty("mail.protocol.port", "465");
		properties.put("mail.smtp.auth", true);
		properties.put("mail.transport.protocol", "TLS");
		properties.setProperty("mail.user", "michalbochenek2@gmail.com");
		properties.setProperty("mail.password", "Redguard7890");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.socketFactory.fallback", "false");
		properties.put("mail.smtp.starttls.enable", "true");

		// Get the default Session object.
		Session mailSession = Session.getDefaultInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("michalbochenek2@gmail.com", "Redguard7890");
			}

		});

		// prepare message text:
		String stockDataFinal = "List of items ordered: \n";
		String stockData = "";
		for (int i = 0; i < toSend.size(); i++) {
			stockData = stockData + "" + toSend.get(i) + "\n";
		}
		// final message
		stockDataFinal = stockDataFinal + stockData;

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(mailSession);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));
			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			// Set Subject: header field
			message.setSubject("Low Stock - Automatic Order");
			// Now set the actual message
			message.setText(stockDataFinal);
			// Send message

			SMTPTransport tr = (SMTPTransport) mailSession.getTransport("smtp");
			tr.connect();
			tr.sendMessage(message, message.getAllRecipients());

			System.out.println("Sent message successfully...");
			// replenish stock in db:
			setStockReplenish();

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
