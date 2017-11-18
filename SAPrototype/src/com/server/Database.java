package com.server;

import java.util.ArrayList;

public class Database {
	
	
	//item features:
	private ArrayList<String> items;
	private ArrayList<Double> prices;
	private ArrayList<Double> delivery;
	private ArrayList<Integer> quanity;
	private ArrayList<Integer> totalSold;
	private ArrayList<Integer> maxStock;
	private ArrayList<Boolean> promotion1;
	private ArrayList<Boolean> promotion2;
	private ArrayList<Boolean> promotion3;
	
	//transactions:
	private ArrayList<Double> transactions;
	
	//promotions settings
	//promotion 1 settings:
	
	//promotion 2 settings: 
	
	//promotion 3 settings:
	
	//users:
	
	
	//on first use populate DB:
	public Database() {
		items = new ArrayList<>();
		prices = new ArrayList<>();
		delivery = new ArrayList<>();
		quanity = new ArrayList<>();
		totalSold = new ArrayList<>();
		maxStock = new ArrayList<>();
		promotion1 = new ArrayList<>();
		promotion2 = new ArrayList<>();
		promotion3 = new ArrayList<>();
		transactions = new ArrayList<>();
		populate();
	}
	
	//read from file dataDB.txt in root folder:
	private void populate() {
		
		
	}
	//getters:
	public ArrayList<String> getItems() {
		return items;
	}
	
	public ArrayList<Double> getPrices(){
		return prices;
	}
	
	public ArrayList<Double> getDelivery(){
		return delivery;
	}
	
	public ArrayList<Integer> getQuanity(){
		return quanity;
	}
	
	public ArrayList<Integer> getTotalSold(){
		return totalSold;
	}
	
	public ArrayList<Integer> getMaxStock(){
		return maxStock;
	}
	
	public ArrayList<Boolean> getPromotion1() {
		return promotion1;
	}
	
	public ArrayList<Boolean> getPromotion2() {
		return promotion2;
	}
	
	public ArrayList<Boolean> getPromotion3() {
		return promotion3;
	}
	
	public ArrayList<Double> getTransactions(){
		return transactions;
	}
	
	//delete:
	public void removeObject(int i) {
		items.remove(i);
		prices.remove(i);
		delivery.remove(i);
		quanity.remove(i);
		totalSold.remove(i);
		maxStock.remove(i);
		promotion1.remove(i);
		promotion2.remove(i);
		promotion3.remove(i);
	}
	
	//setters:
	public void updateObjects(int[] id, String[] item, Double[] price, Double[] transportCost, int[] qty, int[] maxItem, boolean[] prom1, boolean[] prom2, boolean[] prom3) {
		int counter = 0;
		//iterate through the request:
		while(counter<id.length) {
			items.set(id[counter], item[counter]);
			prices.set(id[counter], price[counter]);
			delivery.set(id[counter], transportCost[counter]);
			quanity.set(id[counter], qty[counter]);
			maxStock.set(id[counter], maxItem[counter]);
			promotion1.set(id[counter], prom1[counter]);
			promotion2.set(id[counter], prom2[counter]);
			promotion3.set(id[counter], prom3[counter]);
			counter++;
		}
	}
	//add new
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
		promotion2.add(false);
		promotion3.add(false);
	}
}
