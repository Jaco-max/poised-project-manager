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
import java.util.Scanner;

/**
 * Public class Contractor implements Person
 * @author Ockert
 *
 */
public class Contractor implements Person {
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
	 * This is a constructor for the Contractor object
	 * @param name - Contractor name
	 * @param surname - Contractor surname
	 * @param personType - type of Person
	 * @param email - Contractor email address
	 * @param contactNumber - Contractor contact number
	 * @param address - Contractor physical address
	 * @param projectNumber - Contractor project number
	 */
	public Contractor(String name, String surname, String jobTitle,
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
	 * Overloaded constructor
	 */
	public Contractor() {
		
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
	 * toString method to display Contractor information neatly
	 * @return - returns a String with all relevant information
	 */
	public String toStr() {
		String displayString = "Contractor Name: "
							   + name + "\n"
							   + "Contractor Surname: "
							   + surname + "\n"
							   + "email address: "
							   + email + "\n"
							   + "Contact Number: "
							   + contactNumber + "\n"
							   + "Contractor Address: "
							   + address + "\n"
							   + "Project Number: "
							   + projectNumber;
		return displayString;
	}
	
	/**
	 * Read Contractors into an arrayList from the database
	 * @return - returns an arrayList of Contractors
	 */
	public static ArrayList <Contractor> readContractors() {
		ArrayList<Contractor> testList = new ArrayList<Contractor>();		

		try {
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/poised_db?useSSL=false",
					"otheruser",
					"swordfish");
			Statement statement = connection.createStatement();
			ResultSet results;			
			PreparedStatement searchStatement = null;
			String searchQuery = "SELECT * FROM $tablename";
			String tableName = "contractor";
			String finalSearchQuery = searchQuery.replace("$tablename", tableName);
			searchStatement = connection.prepareStatement(finalSearchQuery);
			results = searchStatement.executeQuery();
			
			while(results.next()) {	
				Contractor testContractor = new Contractor();
				testContractor.setName(results.getString(1));
				testContractor.setSurname(results.getString(2));
				testContractor.setPersonType(results.getString(3));
				testContractor.setEmail(results.getString(4));
				testContractor.setContactNumber(results.getString(5));
				testContractor.setAddress(results.getString(6));
				testContractor.setProjectNumber(results.getString(7));			
				testList.add(testContractor);
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
	 * Takes in a Contractor object and writes it to the database
	 * @param contractor
	 */
	public static void contractorAdd(Contractor contractor) {
		try {
			Connection connection = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/poised_db?useSSL=false",
				"otheruser","swordfish");	

			String insertQuery = ("INSERT INTO contractor VALUES (?, ?, ?, ?, ?, ?, ?)");
			insertStatement = connection.prepareStatement(insertQuery);
			insertStatement.setString(1, contractor.getName());
			insertStatement.setString(2, contractor.getSurname());
			insertStatement.setString(3, contractor.getPersonType());
			insertStatement.setString(4, contractor.getEmail());
			insertStatement.setString(5, contractor.getContactNumber());
			insertStatement.setString(6, contractor.getAddress());
			insertStatement.setString(7, contractor.getProjectNumber());
			insertStatement.execute();
			
		}catch(SQLException e) {
			System.out.println("Database could not be found");
		}
	}
	
	/**
	 * Uses a contractor Name to update information about the contractor
	 * @param contractorName
	 */
	public static void contractorToUpdate(String contractorName) {
		
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
								"1.  Contractor email\n" +
								"2.  Contractor number\n" +
								"3.  Contractor address\n" +
								"4. Done");					
				
				int menuOption = input.nextInt();
						
				//Remove \n buffer created by nextInt
				input.nextLine();
						
				//create menu used to update specific attributes
				switch(menuOption) {
					case 1:
						
						System.out.println("Please enter the new Contractor email:");							
						newInfo = input.nextLine();
						updateQuery = ("UPDATE contractor SET email=(?)  WHERE name=(?)");
						updateStatement = connection.prepareStatement(updateQuery);
						updateStatement.setString(1,newInfo);
						updateStatement.setString(2,contractorName);
						updateStatement.execute();
						break;
							
					case 2:
						System.out.println("Please enter the new Contractor number:");						
						newInfo = input.nextLine();	
						updateQuery = ("UPDATE contractor SET contactnumber=(?)  WHERE name=(?)");
						updateStatement = connection.prepareStatement(updateQuery);
						updateStatement.setString(1,newInfo);
						updateStatement.setString(2,contractorName);
						updateStatement.execute();

						break;
							
					case 3:
						System.out.println("Please enter the new Contractor address:");						
						newInfo = input.nextLine();							
						updateQuery = ("UPDATE contractor SET address=(?)  WHERE name=(?)");
						updateStatement = connection.prepareStatement(updateQuery);
						updateStatement.setString(1,newInfo);
						updateStatement.setString(2,contractorName);
						updateStatement.execute();
						break;
							
					
					case 4: System.out.println("Contractor Updated!");
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
