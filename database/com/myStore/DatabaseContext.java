package com.myStore;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class DatabaseContext {
	private String url = "jdbc:mysql://localhost:3306/eCommerce_JUnitJunkies";
	private static final String USERNAME = "default";
	private static final String PASSWORD = "COVA";
	private Connection con;

	public DatabaseContext() throws SQLException {
		con = DriverManager.getConnection(url, USERNAME, PASSWORD);
	}
	
	// EXIT PROGRAM AFTER CALLING THIS
	public void close() throws SQLException {
		con.close();
	}

	public void runStatement() {
		// set up connections and statements
		Scanner sc = new Scanner(System.in);
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(getUserInput(sc));
			ResultSetMetaData rsmd = rs.getMetaData();
			int columns = rsmd.getColumnCount();

			// iterate over resultset
			// Need to update to fit sql column standards
//			
			// int iteration = 1;
			while (rs.next()) {
				for (int i = 1; i <= columns; i++) {
					String name = rsmd.getColumnName(i);
					String value = rs.getString(i);
					System.out.println(name + ": " + value);
				}
				System.out.println();
				// iteration++;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println("You done goofed");
		}
	}

	private static String getUserInput(Scanner sc) {
		// prompts user for SMART SQL statement
		System.out.println("\nWelcome to the ECommerce JUnit Junkies Application!"
				+ "\n-------------------------------------------------"
				+ "\nPlease write a valid SQL statement to find your info");
		return sc.nextLine();
	}

	public Product getProductFromID(int id) throws SQLException {
		String sql = "SELECT * FROM Products WHERE ProductID = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery(sql);
		pstmt.close();

		return new Product(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getDouble(6),
				rs.getDouble(7), rs.getInt(8), rs.getInt(9), rs.getInt(10));
	}

	public List<Product> getAllProducts() throws SQLException {
		String sql = "SELECT * FROM Products";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		List<Product> products = new ArrayList<Product>();
		
		while (rs.next()) {
			Product currProd = new Product(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5),
					rs.getDouble(6), rs.getDouble(7), rs.getInt(8), rs.getInt(9), rs.getInt(10));
			products.add(currProd);
		}

		stmt.close();
		return products;
	}

	public Order getOrderFromID(int id) throws SQLException {
		String sql = "SELECT * FROM Orders WHERE OrderID = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery(sql);
		pstmt.close();

		return new Order(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getDate(4),
				rs.getString(5), rs.getDate(6), rs.getBoolean(7));
	}

	public List<Order> getAllOrders() throws SQLException {
		String sql = "SELECT * FROM Orders";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		List<Order> orders = new ArrayList<Order>();

		while (rs.next()) {
			Order currOrd = new Order(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getDate(4),
					rs.getString(5), rs.getDate(6), rs.getBoolean(7));
			orders.add(currOrd);
		}

		stmt.close();
		return orders;
	}

	public Category getCategoryFromID(int id) throws SQLException {

		String sql = "SELECT * FROM Categories WHERE CategoryID = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery(sql);
		pstmt.close();
		return new Category(rs.getInt(0), rs.getString(1), rs.getString(2), rs.getInt(3));
	}

	public List<Category> getAllCategories() throws SQLException {
		String sql = "SELECT * FROM Categories";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		List<Category> categories = new ArrayList<Category>();

		while (rs.next()) {
			Category currCat = new Category(rs.getInt(0), rs.getString(1), rs.getString(2), rs.getInt(3));
			categories.add(currCat);
		}

		stmt.close();
		return categories;
	}

	public Customer getCustomerFromID(int id) throws SQLException {
		String sql = "SELECT * FROM Customers WHERE CustomerID = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery(sql);
		pstmt.close();
		return new Customer((rs.getInt(1)), rs.getString(2), rs.getString(3), rs.getString(4),
				rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)
				, rs.getString(10));
	}

	public List<Customer> getAllCustomers() throws SQLException {
		String sql = "SELECT * FROM Customers";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		List<Customer> customers = new ArrayList<Customer>();
		while (rs.next()) {
			Customer currCust = new Customer((rs.getInt(1)), rs.getString(2), rs.getString(3), rs.getString(4),
					rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)
					, rs.getString(10));
			customers.add(currCust);
		}

		stmt.close();
		return customers;

	}

	public void printPricedCustomers(double price) throws SQLException {
		String sql = "SELECT OrderID FROM OrderDetails WHERE SUM(salePrice) > ? GROUP BY OrderNumber";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setDouble(0, price);
		ResultSet rs = pstmt.executeQuery(sql);

		List<Integer> list = new ArrayList<Integer>();
		while (rs.next()) {
			list.add(rs.getInt(1));
		}
		pstmt.close();
		
		
		List<Integer> custList = new ArrayList<Integer>();
		for(Integer i: list) {
			String sql2 = "SELECT CustomerID FROM Orders WHERE OrderID = ?";
			PreparedStatement pstmt2 = con.prepareStatement(sql2);
			pstmt.setInt(0, i);
			ResultSet rs1 = pstmt.executeQuery(sql2);
			
			while(rs.next()) {
				custList.add(rs1.getInt(1));
			}
			pstmt2.close();
		}
		
		
		for(Integer j: custList) {
			String sql3 = "SELECT * FROM Customers WHERE CustomerID = ?";
			PreparedStatement pstmt3 = con.prepareStatement(sql3);
			pstmt.setInt(0, j);
			ResultSet rs2 = pstmt.executeQuery(sql3);
			ResultSetMetaData rsmd2 = rs2.getMetaData();
			int cols = rsmd2.getColumnCount();
			
			
			while(rs2.next()) {
				for (int i = 1; i <= cols; i++) {
					String name = rsmd2.getColumnName(i);
					String value = rs2.getString(i);
					System.out.println(name + ": " + value);
				}
			}
			pstmt3.close();
		}
	}
	
	public List<Customer> getAllCustomersFromZip(int zip) throws SQLException {
		String sql = "SELECT * FROM Customers WHERE zipCode = " + zip;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		List<Customer> customers = new ArrayList<Customer>();
		while (rs.next()) {
			Customer currCust = new Customer((rs.getInt(1)), rs.getString(2), rs.getString(3), rs.getString(4),
					rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)
					, rs.getString(10));
			customers.add(currCust);
		}

		stmt.close();
		return customers;

	}
	
	public List<Order> getOrdersSortedPrice() throws SQLException{
		String sql = "SELECT orderNumber, customerID, shipperID, orderDate, paymentInfo,\r\n" + 
				"			shipDate, shipper, orderStatus, SUM(SalePrice) FROM OrderDetails GROUP BY OrderNumber DESC";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		List<Order> orders = new ArrayList<Order>();
		while (rs.next()) {
			Order currOrd = new Order(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getDate(4),
					rs.getString(5), rs.getDate(6), rs.getBoolean(7));
			orders.add(currOrd);
		}
		stmt.close();
		return orders;
	}
	
	public void deleteOrders(int id) throws SQLException{
		String sql = 	"DELETE FROM OrderDetails WHERE OrderNumber = ?";
		String sql2 =	"DELETE FROM Orders WHERE OrderNumber = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, id);
		PreparedStatement pstmt2 = con.prepareStatement(sql2);
		pstmt2.setInt(1, id);
		int rowsaffected = pstmt.executeUpdate();
		rowsaffected += pstmt2.executeUpdate();

		if(rowsaffected > 0) {
			System.out.println("we did the deleties");
		}	
	}

	public void insertOrder(Order newOrder) throws SQLException {
		String sql = "INSERT  INTO Orders (customerID, shipperID, orderDate,  paymentInfo," + 
				"shipDate, orderStatus)  VALUES (?,?,?,?,?,?)";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, newOrder.getCustomerID());
		pstmt.setInt(2, newOrder.getShipperID());
		pstmt.setDate(3, newOrder.getOrderDate());
		pstmt.setString(4,newOrder.getPaymentInfo());
		pstmt.setDate(5, newOrder.getShipDate());
		pstmt.setBoolean(6, newOrder.getOrderStatus());
		int rowsaffected = pstmt.executeUpdate();
		if(rowsaffected > 0) {
		System.out.println("Table updated");
		}
	}
}
