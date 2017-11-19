package com.client;

import java.awt.EventQueue;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.registry.RMIInterface;
import com.server.Database;

import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

public class ClientRun extends JFrame {

	private static final long serialVersionUID = 1L;
	private static com.registry.RMIInterface lookUp;

	private JPanel contentPane;
	private JTextField textFieldUserID;
	private JTextField textFieldTotal;
	private JTextField textFieldFirstName;
	private JTextField textFieldSurname;
	private JTable table;
	private JScrollPane scrollPane;
	private JPanel pMain;
	private JPanel pEnabling;
	private JPanel pStockReport;
	private JPanel pLoyaltyEditor;
	private JPanel pShopReport;
	private JPanel pSpecialOffer;
	private JTextField textFieldX;
	private JTextField textFieldY;
	private JTextField textFieldDisc;
	private JTextField textFieldClientName;
	private JTextField textFieldClientSurname;
	private JTextField textFieldClientID;
	private JTextField textFieldAddress2;
	private JTextField textFieldCity;
	private JTextField textFieldContact;
	private JTextField textFieldLoyaltyDiscount;
	private JTextField textFieldItemName;
	private JTextField textFieldQty;
	private JTextField textFieldPrice;
	private JTextField textFieldDelivery;
	private JTextArea textAreaResult;
	private JButton btnSend;
	private JButton btnAcceptCredit;
	// for enabling:
	private double toPayment = 0;
	private int defaultX = 0;
	private int defaultY = 0;
	private int defaultDiscount = 0;

	private ArrayList<String> toServer = new ArrayList<>();
	private ArrayList<String> fromServer = new ArrayList<>();
	// for remote object database:
	private static Database db;
	// for stock status:
	private String[] header = { "ID", "Name", "Price", "In Stock", "Sold Total", "Delivery Cost", "Max Stock",
			"X for Y", "X value", "Y value", "Discount", "Discount value", "Loyalty Discount" };
	private String[][] data = new String[500][13];
	private ArrayList<String> items = new ArrayList<>();
	private ArrayList<Double> prices = new ArrayList<>();
	private ArrayList<Double> delivery = new ArrayList<>();
	private ArrayList<Integer> quanity = new ArrayList<>();
	private ArrayList<Integer> totalSold = new ArrayList<>();
	private ArrayList<Integer> maxStock = new ArrayList<>();
	private ArrayList<Boolean> promotion1 = new ArrayList<>();
	private ArrayList<Integer> promotion1X = new ArrayList<>();
	private ArrayList<Integer> promotion1Y = new ArrayList<>();
	private ArrayList<Boolean> promotion2 = new ArrayList<>();
	private ArrayList<Double> discount = new ArrayList<>();
	private ArrayList<Boolean> promotion3 = new ArrayList<>();

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		// connect to service broker:
		lookUp = (RMIInterface) Naming.lookup("//localhost/MyServer");
		// start gui thread:
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientRun frame = new ClientRun();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientRun() {
		setBackground(Color.LIGHT_GRAY);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1024, 768);
		// create top panel menu
		createMenu();

		// create main jpanel for card layout
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));

		createMainPage();

		createLoyaltyEditor();

		createEnabling();

		createStockReport();

		createShopReport();

		createSpecialOffer();

	}

	// create menu bar:
	private void createMenu() {

		// menu bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// menu option
		JMenu mnProgram = new JMenu("Program");
		menuBar.add(mnProgram);
		// menu item
		JMenuItem mntmSettings = new JMenuItem("Settings");
		mnProgram.add(mntmSettings);
		mntmSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cl = (CardLayout) (contentPane.getLayout());
				cl.show(contentPane, "MainPage");
			}
		});
		// menu item
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mnProgram.add(mntmQuit);

		// menu option
		JMenu mnExternal = new JMenu("External");
		menuBar.add(mnExternal);
		// menu item
		JMenuItem mntmEnablingInquiry = new JMenuItem("Enabling Inquiry");
		mntmEnablingInquiry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cl = (CardLayout) (contentPane.getLayout());
				cl.show(contentPane, "Enabling");
				// ensure that the button is reset
				btnAcceptCredit.setEnabled(false);
			}
		});
		mnExternal.add(mntmEnablingInquiry);

		// menu option
		JMenu mnWarehouse = new JMenu("Warehouse");
		menuBar.add(mnWarehouse);
		// menu item
		JMenuItem mntmStockReport = new JMenuItem("Stock Report");
		mntmStockReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cl = (CardLayout) (contentPane.getLayout());
				cl.show(contentPane, "StockReport");
			}
		});
		mnWarehouse.add(mntmStockReport);

		// menu option
		JMenu mnShopManager = new JMenu("Shop Manager");
		menuBar.add(mnShopManager);
		// menu item
		JMenuItem mntmShopReport = new JMenuItem("Shop Report");
		mntmShopReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cl = (CardLayout) (contentPane.getLayout());
				cl.show(contentPane, "ShopReport");
			}
		});
		mnShopManager.add(mntmShopReport);
		// menu item
		JMenuItem mntmSpecialOfferEditor = new JMenuItem("Special Offer Editor");
		mntmSpecialOfferEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cl = (CardLayout) (contentPane.getLayout());
				cl.show(contentPane, "SpecialOffer");
			}
		});
		mnShopManager.add(mntmSpecialOfferEditor);
		// menu item
		JMenuItem mntmLoyaltyCard = new JMenuItem("Loyalty Card");
		mntmLoyaltyCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cl = (CardLayout) (contentPane.getLayout());
				cl.show(contentPane, "LoyaltyCard");
			}
		});
		mnShopManager.add(mntmLoyaltyCard);
	}

	// create cards:
	// partially working (base functionality)
	private void createSpecialOffer() {
		pSpecialOffer = new JPanel();
		pSpecialOffer.setLayout(null);
		pSpecialOffer.setBackground(Color.WHITE);
		contentPane.add(pSpecialOffer, "SpecialOffer");

		JLabel lblSpecialOfferEditor = new JLabel("Special Offer Editor");
		lblSpecialOfferEditor.setBounds(10, 11, 140, 14);
		pSpecialOffer.add(lblSpecialOfferEditor);

		JLabel lblSelectItemFrom = new JLabel("Select Item From The List");
		lblSelectItemFrom.setBounds(10, 48, 210, 14);
		pSpecialOffer.add(lblSelectItemFrom);

		JComboBox comboBoxItems = new JComboBox();
		comboBoxItems.setMaximumRowCount(5000);
		comboBoxItems.setBounds(10, 73, 210, 22);
		pSpecialOffer.add(comboBoxItems);

		JLabel lblSetPromotionType = new JLabel("Set Promotion Type");
		lblSetPromotionType.setBounds(230, 48, 140, 14);
		pSpecialOffer.add(lblSetPromotionType);

		JCheckBox chckbxXForY = new JCheckBox("X in price of Y");
		chckbxXForY.setBounds(240, 73, 120, 23);
		pSpecialOffer.add(chckbxXForY);

		textFieldX = new JTextField();
		textFieldX.setColumns(10);
		textFieldX.setBounds(386, 74, 30, 20);
		pSpecialOffer.add(textFieldX);

		JLabel lblXQuanity = new JLabel("X quanity");
		lblXQuanity.setBounds(380, 48, 46, 14);
		pSpecialOffer.add(lblXQuanity);

		JLabel lblYQuanity = new JLabel("Y quanity");
		lblYQuanity.setBounds(454, 48, 46, 14);
		pSpecialOffer.add(lblYQuanity);

		textFieldY = new JTextField();
		textFieldY.setColumns(10);
		textFieldY.setBounds(464, 74, 30, 20);
		pSpecialOffer.add(textFieldY);

		JCheckBox chckbxDiscount = new JCheckBox("Discount");
		chckbxDiscount.setBounds(240, 128, 120, 23);
		pSpecialOffer.add(chckbxDiscount);

		textFieldDisc = new JTextField();
		textFieldDisc.setColumns(10);
		textFieldDisc.setBounds(386, 129, 30, 20);
		pSpecialOffer.add(textFieldDisc);

		JLabel lblSetDiscount = new JLabel("Discount %");
		lblSetDiscount.setBounds(380, 106, 114, 14);
		pSpecialOffer.add(lblSetDiscount);

		JButton btnSetPromotion = new JButton("Change Settings");
		btnSetPromotion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int x = Integer.parseInt(textFieldX.getText());
				int y = Integer.parseInt(textFieldY.getText());
				double d = Double.parseDouble(textFieldDisc.getText());
				try {
					lookUp.updateDefaultOptions(x, y, d);
					textFieldX.setText("");
					textFieldY.setText("");
					textFieldDisc.setText("");
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		btnSetPromotion.setBounds(230, 327, 130, 23);
		pSpecialOffer.add(btnSetPromotion);

		JCheckBox chckbxFreeDelivery = new JCheckBox("FreeDelivery");
		chckbxFreeDelivery.setBounds(240, 179, 120, 23);
		pSpecialOffer.add(chckbxFreeDelivery);

		JCheckBox chckbxBuyGet = new JCheckBox("Buy 1 get 1 free");
		chckbxBuyGet.setBounds(240, 230, 120, 23);
		pSpecialOffer.add(chckbxBuyGet);

		JCheckBox chckbxLoyaltyDisc = new JCheckBox("Loyalty Discount Applies");
		chckbxLoyaltyDisc.setBounds(240, 277, 176, 23);
		pSpecialOffer.add(chckbxLoyaltyDisc);

	}

	// working, base functionality
	private void createLoyaltyEditor() {
		pLoyaltyEditor = new JPanel();
		pLoyaltyEditor.setBackground(Color.WHITE);
		contentPane.add(pLoyaltyEditor, "LoyaltyCard");
		pLoyaltyEditor.setLayout(null);

		JLabel lblLoyaltyEditor = new JLabel("New Client Loyalty Card");
		lblLoyaltyEditor.setBounds(10, 11, 287, 25);
		pLoyaltyEditor.add(lblLoyaltyEditor);

		JLabel lblClientName = new JLabel("Client Name:");
		lblClientName.setBounds(10, 47, 140, 14);
		pLoyaltyEditor.add(lblClientName);

		textFieldClientName = new JTextField();
		textFieldClientName.setBounds(10, 72, 230, 20);
		pLoyaltyEditor.add(textFieldClientName);
		textFieldClientName.setColumns(10);

		JLabel lblClientSurname = new JLabel("Client Surname:");
		lblClientSurname.setBounds(10, 103, 140, 14);
		pLoyaltyEditor.add(lblClientSurname);

		textFieldClientSurname = new JTextField();
		textFieldClientSurname.setBounds(10, 128, 230, 20);
		pLoyaltyEditor.add(textFieldClientSurname);
		textFieldClientSurname.setColumns(10);

		JLabel lblID = new JLabel("Client ID");
		lblID.setBounds(10, 158, 140, 14);
		pLoyaltyEditor.add(lblID);

		textFieldClientID = new JTextField();
		textFieldClientID.setBounds(10, 183, 230, 20);
		pLoyaltyEditor.add(textFieldClientID);
		textFieldClientID.setColumns(10);

		JLabel lblAddress = new JLabel("Address");
		lblAddress.setBounds(10, 215, 140, 14);
		pLoyaltyEditor.add(lblAddress);

		textFieldAddress2 = new JTextField();
		textFieldAddress2.setBounds(10, 240, 230, 20);
		textFieldAddress2.setEnabled(false);
		pLoyaltyEditor.add(textFieldAddress2);
		textFieldAddress2.setColumns(10);

		JLabel lblCity = new JLabel("City");
		lblCity.setBounds(10, 271, 140, 14);
		pLoyaltyEditor.add(lblCity);

		textFieldCity = new JTextField();
		textFieldCity.setBounds(10, 296, 230, 20);
		textFieldCity.setEnabled(false);
		pLoyaltyEditor.add(textFieldCity);
		textFieldCity.setColumns(10);

		JLabel lblPhone = new JLabel("Contact Number");
		lblPhone.setBounds(10, 331, 140, 14);
		pLoyaltyEditor.add(lblPhone);

		textFieldContact = new JTextField();
		textFieldContact.setBounds(10, 356, 230, 20);
		textFieldContact.setEnabled(false);
		pLoyaltyEditor.add(textFieldContact);
		textFieldContact.setColumns(10);

		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String n = textFieldClientName.getText();
					String s = textFieldClientSurname.getText();
					double d = Double.parseDouble(textFieldLoyaltyDiscount.getText());
					int i = Integer.parseInt(textFieldClientID.getText());
					lookUp.setClientDiscountCard(n, s, d, i);

					textFieldClientName.setText("");
					textFieldClientSurname.setText("");
					textFieldLoyaltyDiscount.setText("");
					textFieldClientID.setText("");
					
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		btnSubmit.setBounds(100, 398, 140, 23);
		pLoyaltyEditor.add(btnSubmit);

		JButton btnSetDiscount = new JButton("Set Discount");

		btnSetDiscount.setBounds(437, 398, 140, 23);
		pLoyaltyEditor.add(btnSetDiscount);

		JLabel lblSetLoyaltyDiscount = new JLabel("Set Loyalty Discount[%]:");
		lblSetLoyaltyDiscount.setBounds(437, 331, 140, 14);
		pLoyaltyEditor.add(lblSetLoyaltyDiscount);

		textFieldLoyaltyDiscount = new JTextField();
		textFieldLoyaltyDiscount.setBounds(437, 356, 50, 20);
		pLoyaltyEditor.add(textFieldLoyaltyDiscount);
		textFieldLoyaltyDiscount.setColumns(10);
	}

	// working:
	private void createEnabling() {

		pEnabling = new JPanel();
		pEnabling.setBackground(Color.WHITE);
		contentPane.add(pEnabling, "Enabling");
		pEnabling.setLayout(null);

		JLabel lblUserId = new JLabel("Client ID (or Loyalty Card ID)");
		lblUserId.setBounds(10, 60, 210, 14);
		pEnabling.add(lblUserId);

		textFieldUserID = new JTextField();
		textFieldUserID.setBounds(10, 85, 150, 20);
		pEnabling.add(textFieldUserID);
		textFieldUserID.setColumns(10);

		JLabel lblTotalCharge = new JLabel("Total Charge");
		lblTotalCharge.setBounds(10, 116, 150, 14);
		pEnabling.add(lblTotalCharge);

		textFieldTotal = new JTextField();
		textFieldTotal.setBounds(10, 141, 150, 20);
		pEnabling.add(textFieldTotal);
		textFieldTotal.setColumns(10);

		JLabel lblEnablingInquiryForm = new JLabel("Enabling Inquiry Form");
		lblEnablingInquiryForm.setBounds(10, 11, 210, 38);
		pEnabling.add(lblEnablingInquiryForm);

		textAreaResult = new JTextArea();
		textAreaResult.setEditable(false);
		textAreaResult.setBackground(Color.LIGHT_GRAY);
		textAreaResult.setBounds(281, 83, 581, 500);
		pEnabling.add(textAreaResult);

		textFieldFirstName = new JTextField();
		textFieldFirstName.setBounds(10, 201, 150, 20);
		pEnabling.add(textFieldFirstName);
		textFieldFirstName.setColumns(10);

		JLabel lblName = new JLabel("First Name");
		lblName.setBounds(10, 172, 150, 14);
		pEnabling.add(lblName);

		JLabel lblSurname = new JLabel("Surname");
		lblSurname.setBounds(10, 232, 46, 14);
		pEnabling.add(lblSurname);

		textFieldSurname = new JTextField();
		textFieldSurname.setBounds(10, 257, 150, 20);
		pEnabling.add(textFieldSurname);
		textFieldSurname.setColumns(10);

		JLabel lblInquiryResults = new JLabel("Inquiry Results:");
		lblInquiryResults.setBounds(281, 60, 128, 14);
		pEnabling.add(lblInquiryResults);

		btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// get form data and put it on array list
				toServer.add(textFieldUserID.getText());
				toServer.add(textFieldTotal.getText());
				toServer.add(textFieldFirstName.getText());
				toServer.add(textFieldSurname.getText());

				try {
					// try to get response from server:
					fromServer.addAll(lookUp.getEnablingResult(toServer));
					// process response:
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (fromServer != null) {
					textAreaResult.setText(fromServer.get(0));
					if (fromServer.get(1).contains("accepted")) {
						btnAcceptCredit.setEnabled(true);
						toPayment = Double.parseDouble(fromServer.get(2));
						toServer.removeAll(toServer);
						fromServer.removeAll(fromServer);
					} else if (fromServer.get(1).contains("rejected")) {
						toServer.removeAll(toServer);
						fromServer.removeAll(fromServer);
					} else {
						toServer.removeAll(toServer);
						fromServer.removeAll(fromServer);
					}
				}
			}
		});
		btnSend.setBounds(71, 288, 89, 23);
		pEnabling.add(btnSend);

		btnAcceptCredit = new JButton("Accept Credit");
		btnAcceptCredit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					btnAcceptCredit.setEnabled(false);
					lookUp.payWithEnabling(toPayment);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnAcceptCredit.setEnabled(false);
		btnAcceptCredit.setBounds(742, 594, 120, 23);
		pEnabling.add(btnAcceptCredit);
	}

	// working:
	private void updateTableModel() {
		try {
			// PREPARE DATA TO INSERT:
			if (items.size() > 0) {
				items.removeAll(items);
				prices.removeAll(prices);
				delivery.removeAll(delivery);
				quanity.removeAll(quanity);
				totalSold.removeAll(totalSold);
				maxStock.removeAll(maxStock);
				promotion1.removeAll(promotion1);
				promotion1X.removeAll(promotion1X);
				promotion1Y.removeAll(promotion1Y);
				promotion2.removeAll(promotion2);
				discount.removeAll(discount);
				promotion3.removeAll(promotion3);
			}

			items.addAll(lookUp.getItemNames());
			prices.addAll(lookUp.getItemPrices());
			delivery.addAll(lookUp.getTransportCost());
			quanity.addAll(lookUp.getItemNumber());
			totalSold.addAll(lookUp.getItemTotalSold());
			maxStock.addAll(lookUp.getItemMaxQty());
			promotion1.addAll(lookUp.getPromo1());
			promotion1X.addAll(lookUp.getPromo1X());
			promotion1Y.addAll(lookUp.getPromo1Y());
			promotion2.addAll(lookUp.getPromo2());
			discount.addAll(lookUp.getPromo2Discount());
			promotion3.addAll(lookUp.getPromo3());

			int i = items.size();

			for (int j = 0; j < i; j++) {
				data[j][0] = Integer.toString(j);
				data[j][1] = items.get(j);
				data[j][2] = Double.toString(prices.get(j));
				data[j][3] = Integer.toString(quanity.get(j));
				data[j][4] = Integer.toString(totalSold.get(j));
				data[j][5] = Double.toString(delivery.get(j));
				data[j][6] = Integer.toString(maxStock.get(j));

				if (promotion1.get(j) == true) {
					data[j][7] = "true";
				} else {
					data[j][7] = "false";
				}

				data[j][8] = Integer.toString(promotion1X.get(j));
				data[j][9] = Integer.toString(promotion1Y.get(j));

				if (promotion2.get(j) == true) {
					data[j][10] = "true";
				} else {
					data[j][10] = "false";
				}

				data[j][11] = Double.toString(discount.get(j));

				if (promotion3.get(j) == true) {
					data[j][12] = "true";
				} else {
					data[j][12] = "false";
				}
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// working
	private void createStockReport() {
		pStockReport = new JPanel();
		pStockReport.setBackground(Color.WHITE);
		contentPane.add(pStockReport, "StockReport");
		pStockReport.setLayout(null);
		// prepare data to be used first time:
		updateTableModel();
		// create table with default model:
		table = new JTable(data, header);
		table.setCellSelectionEnabled(true);
		table.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setPreferredScrollableViewportSize(new Dimension(968, 680));
		table.setFillsViewportHeight(true);
		table.setBounds(10, 50, 968, 680);
		table.doLayout();
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(table.getBounds());
		pStockReport.add(scrollPane);

		JLabel lblStockReport = new JLabel("Stock Report");
		lblStockReport.setBounds(10, 11, 74, 14);
		pStockReport.add(lblStockReport);

		// data from database:
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateTableModel();
				table.revalidate();
				table.repaint();
			}
		});

		btnUpdate.setBounds(94, 7, 85, 23);
		pStockReport.add(btnUpdate);

		JLabel lblAddItem = new JLabel("Add New Item");
		lblAddItem.setBounds(186, 11, 85, 14);
		pStockReport.add(lblAddItem);

		JButton btnAdd = new JButton("Add Item");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String item = textFieldItemName.getText();
				double price = Double.parseDouble(textFieldPrice.getText());
				double transportCost = Double.parseDouble(textFieldDelivery.getText());
				int maxItem = Integer.parseInt(textFieldQty.getText());

				try {
					lookUp.addItemToWarehouse(item, price, transportCost, maxItem);
					textFieldItemName.setText("");
					textFieldPrice.setText("");
					textFieldDelivery.setText("");
					textFieldQty.setText("");

				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				updateTableModel();
				table.validate();
				table.repaint();
			}
		});
		btnAdd.setBounds(272, 7, 85, 23);
		pStockReport.add(btnAdd);

		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(361, 11, 45, 14);
		pStockReport.add(lblName);

		textFieldItemName = new JTextField();
		textFieldItemName.setBounds(406, 11, 104, 20);
		pStockReport.add(textFieldItemName);

		JLabel lblQty = new JLabel("Max Qty:");
		lblQty.setBounds(512, 11, 50, 14);
		pStockReport.add(lblQty);

		textFieldQty = new JTextField();
		textFieldQty.setBounds(563, 11, 50, 20);
		pStockReport.add(textFieldQty);

		JLabel lblPrice = new JLabel("Price:");
		lblPrice.setBounds(614, 11, 45, 14);
		pStockReport.add(lblPrice);

		textFieldPrice = new JTextField();
		textFieldPrice.setBounds(665, 11, 50, 20);
		pStockReport.add(textFieldPrice);

		JLabel lblDel = new JLabel("Delivery:");
		lblDel.setBounds(716, 11, 50, 14);
		pStockReport.add(lblDel);

		textFieldDelivery = new JTextField();
		textFieldDelivery.setBounds(767, 11, 50, 20);
		pStockReport.add(textFieldDelivery);

		// send data to database:
		JButton btnSend = new JButton("Send Changes");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<String> item = new ArrayList<>();
				ArrayList<Double> pr = new ArrayList<>();
				ArrayList<Double> del = new ArrayList<>();
				ArrayList<Integer> qty = new ArrayList<>();
				ArrayList<Integer> ts = new ArrayList<>();
				ArrayList<Integer> maxSt = new ArrayList<>();
				ArrayList<Boolean> p1 = new ArrayList<>();
				ArrayList<Integer> x = new ArrayList<>();
				ArrayList<Integer> y = new ArrayList<>();
				ArrayList<Boolean> p2 = new ArrayList<>();
				ArrayList<Double> disc = new ArrayList<>();
				ArrayList<Boolean> p3 = new ArrayList<>();

				if (items.size() > 0) {
					// if table ID is not null do:
					for (int i = 0; i < items.size(); i++) {
						item.add(table.getValueAt(i, 1).toString());
						pr.add(Double.parseDouble(table.getValueAt(i, 2).toString()));
						qty.add(Integer.parseInt(table.getValueAt(i, 3).toString()));
						ts.add(Integer.parseInt(table.getValueAt(i, 4).toString()));
						del.add(Double.parseDouble(table.getValueAt(i, 5).toString()));
						maxSt.add(Integer.parseInt(table.getValueAt(i, 6).toString()));
						p1.add(Boolean.parseBoolean(table.getValueAt(i, 7).toString()));
						x.add(Integer.parseInt(table.getValueAt(i, 8).toString()));
						y.add(Integer.parseInt(table.getValueAt(i, 9).toString()));
						p2.add(Boolean.parseBoolean(table.getValueAt(i, 10).toString()));
						disc.add(Double.parseDouble(table.getValueAt(i, 11).toString()));
						p3.add(Boolean.parseBoolean(table.getValueAt(i, 12).toString()));
					}

					// send to server:
					try {
						lookUp.updateItemDB(item, pr, del, qty, ts, maxSt, p1, p2, p3, x, y, disc);
						updateTableModel();
						table.validate();
						table.repaint();
					} catch (RemoteException e) {
						e.printStackTrace();
					}

				}
			}
		});

		btnSend.setBounds(820, 7, 90, 23);
		pStockReport.add(btnSend);

	}

	// to do: - GENERATE CLIENTS "accounting DB" - then create report based on data
	private void createShopReport() {
		pShopReport = new JPanel();
		pShopReport.setLayout(null);
		pShopReport.setBackground(Color.WHITE);
		contentPane.add(pShopReport, "ShopReport");

		JLabel lblShopReport = new JLabel("Shop Report");
		lblShopReport.setBackground(Color.WHITE);
		lblShopReport.setBounds(20, 11, 352, 14);
		pShopReport.add(lblShopReport);

		JTextArea textAreaShopReport = new JTextArea();
		textAreaShopReport.setEditable(false);
		textAreaShopReport.setBounds(20, 36, 968, 650);
		pShopReport.add(textAreaShopReport);
	}

	// working:
	private void createMainPage() {
		pMain = new JPanel();
		pMain.setLayout(null);
		pMain.setBackground(Color.WHITE);
		contentPane.add(pMain, "MainPage");

		JLabel lblWelcome = new JLabel("Welcome to the prototype, please select option from the toolbar above.");
		lblWelcome.setBackground(Color.WHITE);
		lblWelcome.setBounds(20, 11, 500, 14);
		pMain.add(lblWelcome);
	}
}
