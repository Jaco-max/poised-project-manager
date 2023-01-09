import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Public Class Client implements Person
 * @author Ockert
 *
 */
public class Client implements Person {
	/**
	 * @param name client name value
	 */
	private String name;
	
	private String surname;
	private String personType;
	private String email;
	private String contactNumber;
	private String address;
	private String projectNumber;
	private static PreparedStatement insertStatement = null;
	private static PreparedStatement updateStatement = null;
	
	/**
	 * This is a constructor for Client, which implements Person.
	 * The Client is the Person making use of the Architects' services
	 * @param name - name of Client
	 * @param surname - Client surname
	 * @param personType - Client is a person
	 * @param email - Client email address
	 * @param contactNumber - Client contact number
	 * @param address - Client physical address
	 * @param projectNumber - Client project number
	 */
	public Client(String name, String surname, String jobTitle,
				  String email, String contactNumber, String address,
				  String projectNumber) {
		this.name = name;
		this.surname = surname;
		this.personType = jobTitle;
		this.email = email;
		this.contactNumber = contactNumber;
		this.address = address;
		this.projectNumber = projectNumber;
	}
	
	/**
	 *  overloaded constructor for Client
	 */
	public Client() {
		
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
		
	}
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
		
	}
	
	public String getPersonType() {
		return personType;
	}
	
	public void setPersonType(String jobTitle) {
		this.personType = jobTitle;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getContactNumber() {
		return contactNumber;
	}
	
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}
	
	/**
	 * This is a toString method to display the client's information neatly
	 * 
	 * @return - displayString which is a String variable of all the 
	 * 			 Client's relevant information
	 */
	//@Override
	public String toStr() {
		String displayString = "Client Name: "
							   + name + "\n"
							   + "Client Surname: "
							   + surname + "\n"
							   + "email address: "
							   + email + "\n"
							   + "Contact Number: "
							   + contactNumber + "\n"
							   + "Client Address: "
							   + address + "\n"
							   + "Project Number: "
							   + projectNumber;
		return displayString;
	}
	
	/**
	 * Read Clients into an arrayList from the database
	 * @return - returns an arrayList of Contractors
	 */
	public static ArrayList <Client> readClients() {
		ArrayList<Client> testList = new ArrayList<Client>();		

		try {
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/poised_db?useSSL=false",
					"otheruser",
					"swordfish");
			Statement statement = connection.createStatement();
			ResultSet results;			
			PreparedStatement searchStatement = null;
			String searchQuery = "SELECT * FROM $tablename";
			String tableName = "client";
			String finalSearchQuery = searchQuery.replace("$tablename", tableName);
			searchStatement = connection.prepareStatement(finalSearchQuery);
			results = searchStatement.executeQuery();
			
			while(results.next()) {	
				Client testClient = new Client();
				testClient.setName(results.getString(1));
				testClient.setSurname(results.getString(2));
				testClient.setPersonType(results.getString(3));
				testClient.setEmail(results.getString(4));
				testClient.setContactNumber(results.getString(5));
				testClient.setAddress(results.getString(6));
				testClient.setProjectNumber(results.getString(7));			
				testList.add(testClient);
			}
	
			results.close();
			statement.close();
			connection.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return(testList);
	}
	
	/**
	 * Takes in a Client object and writes it to the database
	 * @param contractor
	 */
	public static void clientAdd(Client client) {
		try {
			Connection connection = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/poised_db?useSSL=false",
				"otheruser","swordfish");	

			String insertQuery = ("INSERT INTO client VALUES (?, ?, ?, ?, ?, ?, ?)");
			insertStatement = connection.prepareStatement(insertQuery);
			insertStatement.setString(1, client.getName());
			insertStatement.setString(2, client.getSurname());
			insertStatement.setString(3, client.getPersonType());
			insertStatement.setString(4, client.getEmail());
			insertStatement.setString(5, client.getContactNumber());
			insertStatement.setString(6, client.getAddress());
			insertStatement.setString(7, client.getProjectNumber());
			insertStatement.execute();
			
		}catch(SQLException e) {
			System.out.println("Database could not be found");
		}
	}
	
	/**
	 * Uses a Client Name to update information about the contractor
	 * @param contractorName
	 */
	public static void clientToUpdate(String clientName) {
		
		boolean updated = true;

		try {
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/poised_db?useSSL=false",
					"otheruser","swordfish");				
						
			while(updated != false) {
				Scanner input = new Scanner(System.in);
				String newInfo ="";
				String updateQuery="";
				System.out.println("Please select which value "+
								"you would like to update:\n"+
								"1.  Customer email\n" +
								"2.  Customer number\n" +
								"3.  Customer address\n" +
								"4. Done");					
				
				int menuOption = input.nextInt();
						
				//Remove \n buffer created by nextInt
				input.nextLine();
						
				//create menu used to update specific attributes
				switch(menuOption) {
					case 1:
						
						System.out.println("Please enter the new Customer email:");							
						newInfo = input.nextLine();
						updateQuery = ("UPDATE client SET email=(?)  WHERE name=(?)");
						updateStatement = connection.prepareStatement(updateQuery);
						updateStatement.setString(1,newInfo);
						updateStatement.setString(2,clientName);
						updateStatement.execute();
						break;
							
					case 2:
						System.out.println("Please enter the new Customer number:");						
						newInfo = input.nextLine();	
						updateQuery = ("UPDATE projects SET contactnumber=(?)  WHERE name=(?)");
						updateStatement = connection.prepareStatement(updateQuery);
						updateStatement.setString(1,newInfo);
						updateStatement.setString(2,clientName);
						updateStatement.execute();

						break;
							
					case 3:
						System.out.println("Please enter the new Customer address:");						
						newInfo = input.nextLine();							
						updateQuery = ("UPDATE projects SET address=(?)  WHERE name=(?)");
						updateStatement = connection.prepareStatement(updateQuery);
						updateStatement.setString(1,newInfo);
						updateStatement.setString(2,clientName);
						updateStatement.execute();
						break;
							
					
					case 4: System.out.println("Client Updated!");
						updated = false;
						break;
					}
			connection.close();	
			}
				
			}catch(SQLException e) {
				System.out.println("Database could not be found");
			}				
	}

}
