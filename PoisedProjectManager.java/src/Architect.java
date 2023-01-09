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
 * Public class Architect implements Person
 * @author Ockert
 *
 */
public class Architect implements Person{
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
	 * This is a constructor for the Architect object
	 * @param name - Architect name
	 * @param surname - Architect surname
	 * @param personType - Type of Person Object
	 * @param email - Architect email address
	 * @param contactNumber - Architect contact number
	 * @param address - Architect physical address
	 * @param projectNumber - Architect project number
	 */
	public Architect(String name, String surname, String jobTitle,
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
	 * overloaded constructor
	 */
	public Architect() {
		
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
	
	public String getJobTitle() {
		return personType;
	}
	
	public void setJobTitle(String jobTitle) {
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
	 * toString method to display Architect information neatly
	 * @return - returns a String with all relevant information
	 */
	public String toStr() {
		String displayString = "Architect Name: "
							   + name + "\n"
							   + "Architect Surname: "
							   + surname + "\n"
							   + "Job Title: "
							   + personType + "\n"
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
	 * Read Architects into an arrayList from the database
	 * @return - returns an arrayList of Contractors
	 */
	public static ArrayList <Architect> readArchitects() {
		ArrayList<Architect> testList = new ArrayList<Architect>();		

		try {
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/poised_db?useSSL=false",
					"otheruser",
					"swordfish");
			Statement statement = connection.createStatement();
			ResultSet results;			
			PreparedStatement searchStatement = null;
			String searchQuery = "SELECT * FROM $tablename";
			String tableName = "architect";
			String finalSearchQuery = searchQuery.replace("$tablename", tableName);
			searchStatement = connection.prepareStatement(finalSearchQuery);
			results = searchStatement.executeQuery();
			
			while(results.next()) {	
				Architect testArchitect = new Architect();
				testArchitect.setName(results.getString(1));
				testArchitect.setSurname(results.getString(2));
				testArchitect.setJobTitle(results.getString(3));
				testArchitect.setEmail(results.getString(4));
				testArchitect.setContactNumber(results.getString(5));
				testArchitect.setAddress(results.getString(6));
				testArchitect.setProjectNumber(results.getString(7));			
				testList.add(testArchitect);
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
	 * Takes in an Architect  object and writes it to the database
	 * @param contractor
	 */
	public static void architectAdd(Architect architect) {
		try {
			Connection connection = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/poised_db?useSSL=false",
				"otheruser","swordfish");	

			String insertQuery = ("INSERT INTO architect VALUES (?, ?, ?, ?, ?, ?, ?)");
			insertStatement = connection.prepareStatement(insertQuery);
			insertStatement.setString(1, architect.getName());
			insertStatement.setString(2, architect.getSurname());
			insertStatement.setString(3, architect.getJobTitle());
			insertStatement.setString(4, architect.getEmail());
			insertStatement.setString(5, architect.getContactNumber());
			insertStatement.setString(6, architect.getAddress());
			insertStatement.setString(7, architect.getProjectNumber());
			insertStatement.execute();
			
		}catch(SQLException e) {
			System.out.println("Database could not be found");
		}
	}
	
	/**
	 * Uses an Architect Name to update information about the contractor
	 * @param contractorName
	 */
	public static void architectToUpdate(String architectName) {
		
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
								"1.  Architect email\n" +
								"2.  Architect number\n" +
								"3.  Architect address\n" +
								"4. Done");					
				
				int menuOption = input.nextInt();
						
				//Remove \n buffer created by nextInt
				input.nextLine();
						
				//create menu used to update specific attributes
				switch(menuOption) {
					case 1:
						
						System.out.println("Please enter the new Architect email:");							
						newInfo = input.nextLine();
						updateQuery = ("UPDATE architect SET email=(?)  WHERE name=(?)");
						updateStatement = connection.prepareStatement(updateQuery);
						updateStatement.setString(1,newInfo);
						updateStatement.setString(2,architectName);
						updateStatement.execute();
						break;
							
					case 2:
						System.out.println("Please enter the new Architect number:");						
						newInfo = input.nextLine();	
						updateQuery = ("UPDATE architect SET contactnumber=(?)  WHERE name=(?)");
						updateStatement = connection.prepareStatement(updateQuery);
						updateStatement.setString(1,newInfo);
						updateStatement.setString(2,architectName);
						updateStatement.execute();

						break;
							
					case 3:
						System.out.println("Please enter the new Architect address:");						
						newInfo = input.nextLine();							
						updateQuery = ("UPDATE architect SET address=(?)  WHERE name=(?)");
						updateStatement = connection.prepareStatement(updateQuery);
						updateStatement.setString(1,newInfo);
						updateStatement.setString(2,architectName);
						updateStatement.execute();
						break;
							
					
					case 4: System.out.println("Architect Updated!");
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
