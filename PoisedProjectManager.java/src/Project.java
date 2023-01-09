import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


/**
 * Public class Project
 * @author Ockert
 *
 */
public class Project {
	private String customerName;
	private String projectNumber;
	private String projectName;
	private String buildingType;
	private String projectAddress;
	private String projectERF;
	private String deadline;
	private String totalFee;
	private String totalPaid;
	private String finalised;
	private static PreparedStatement insertStatement = null;
	private static PreparedStatement updateStatement = null;
	private static PreparedStatement deleteStatement = null;
	private static PreparedStatement searchStatement = null;
	private static ResultSet results;
	
	/**
	 * This is a constructor for the Project class
	 * @param customerName - name of customer
	 * @param projectNumber - unique number assigned to each project
	 * @param projectName - name of project
	 * @param buildingType - type of building the project
	 * @param projectAddress - address of the project building
	 * @param projectERF - ERF number of the project building
	 * @param deadline - deadline for the project
	 * @param totalFee - total fee for the project after completion
	 * @param totalPaid - total amount paid by the client
	 * @param finalised - shows whether the project is finalised or not
	 */
	public Project(String customerName, String projectNumber, String projectName, 
				   String buildingType, String projectAddress,
			       String projectERF, String deadline, 
			       String totalFee, String totalPaid, String finalised) {
		this.customerName = customerName;
		this.projectNumber = projectNumber;
		this.projectName = projectName;
		this.buildingType = buildingType;
		this.projectAddress = projectAddress;
		this.projectERF = projectERF;
		this.deadline = deadline;
		this.totalFee = totalFee;
		this.totalPaid = totalPaid;
		this.finalised = finalised;
	}
	
	/**
	 * Overloaded constructor
	 */
	public Project() {
		
	}
	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getProjectNumber() {
		return projectNumber;
	}
	
	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}
		
	public String getProjectName() {
		return projectName;
	}	
	
	public void setProjectName(String newProjectName) {
		projectName = newProjectName;
	}
	
	public String getBuildingType() {
		return buildingType;
	}
	
	public void setBuildingType(String buildingType) {
		this.buildingType = buildingType;
	}
	
	public String getProjectAddress() {
		return projectAddress;
	}
	
	public void setProjectAddress(String projectAddress) {
		this.projectAddress = projectAddress;
	}
	
	public String getProjectERF() {
		return projectERF;
	}
	
	public void setProjectERF(String projectERF) {
		this.projectERF = projectERF;
	}
	
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String newDeadline) {
		deadline = newDeadline;
	}
	
	public String getTotalFee() {
		return totalFee;
	}
	
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	
	public String getTotalPaid() {
		return totalPaid;
	}
	
	public void setTotalPaid(String newTotalPaid) {
		totalPaid = newTotalPaid;
	}
	
	public String getFinalised() {
		return finalised;
	}
	
	public void setFinalised(String newFinalised) {
		finalised = newFinalised;
	}
	
	/**
	 *ToString method to display a project neatly
	 * @return - returns a String with all the relevant Project information
	 */
	public String toStr() {
		
		String displayStr = "";
		displayStr = "Customer Name: " + customerName +"\n"
				   + "Project Number: " + projectNumber + "\n"
				   + "Project Name: " + projectName + "\n"
				   + "Building type: " + buildingType + "\n"
				   + "Project Address: " + projectAddress +  "\n"
				   + "Project ERF Number : " + projectERF + "\n"
				   + "Project Deadline: " + deadline + "\n"				   			
				   + "Total Fee: R" + totalFee + "\n"
				   + "Total paid to date: R" + totalPaid + "\n";
		return displayStr;
	}
	
	/**
	 * method used to read contents from the database into an
	   arraylist of project objects 
	 * @return - returns an arrayList of project objects
	 */
	public static ArrayList <Project> readProjects() {
		ArrayList<Project> testList = new ArrayList<Project>();		
		Calendar testDueDate = Calendar.getInstance(); 
		String testDueDateArr [];
			
		try {
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/poised_db?useSSL=false",
					"otheruser",
					"swordfish");
			Statement statement = connection.createStatement();
			ResultSet results;
			int rowsAffected;
				
			PreparedStatement searchStatement = null;
			String searchQuery = "SELECT * FROM $tablename";
			String tableName = "projects";
			String finalSearchQuery = searchQuery.replace("$tablename", tableName);
			searchStatement = connection.prepareStatement(finalSearchQuery);
			results = searchStatement.executeQuery();
				
			while(results.next()) {	
				Project testProject = new Project();
				testProject.setCustomerName(results.getString(1));
				testProject.setProjectNumber(results.getString(2));
				testProject.setProjectName(results.getString(3));
				testProject.setBuildingType(results.getString(4));
				testProject.setProjectAddress(results.getString(5));
				testProject.setProjectERF(results.getString(6));					
				testProject.setDeadline(results.getString(7));
				testProject.setTotalFee(results.getString(8));
				testProject.setTotalPaid(results.getString(9));
				testProject.setFinalised(results.getString(10));					
				testList.add(testProject);
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
	 * Creates a Calendar object from the database String
	 * to be compared with today's date
	 * @param dateString
	 * @return
	 */
	public static Calendar createDate(String dateString) {
		String dateArr[] = dateString.split("_");
		Calendar projectDate  = Calendar.getInstance();
		int day = Integer.parseInt(dateArr[2]);
		int month = Integer.parseInt(dateArr[1]);
		int year = Integer.parseInt(dateArr[0]);
		projectDate.set(month, day, year);
		
		return(projectDate);
		
	}
	
	/**
	 * Method used to update a project object in the database
	 * @param ProjectNumber - project number of the project to be updated
	 */
	public static void projectToUpdate(String projectNumber) {
		
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
								"1.  Customer Name\n" +
								"2.  Project Number\n" +
								"3.  Project Name\n" +
								"4.  Building Type\n" +
								"5.  Project Address\n" +
								"6.  Project ERF Number\n" +
								"7.  Project Deadline\n" +
								"8.  Total Project Fee\n" +
								"9.  Total Fee Paid\n" +
								"10. Done");					
				
				int menuOption = input.nextInt();
						
				//Remove \n buffer created by nextInt
				input.nextLine();
						
				//create menu used to update specific attributes
				switch(menuOption) {
					case 1:
						
						System.out.println("Please enter the new Customer Name:");							
						newInfo = input.nextLine();
						updateQuery = ("UPDATE projects SET customername=(?)  WHERE projectnum=(?)");
						updateStatement = connection.prepareStatement(updateQuery);
						updateStatement.setString(1,newInfo);
						updateStatement.setString(2,projectNumber);
						updateStatement.execute();
						break;
							
					case 2:
						System.out.println("Please enter the new Project Number:");						
						newInfo = input.nextLine();	
						updateQuery = ("UPDATE projects SET projectnum=(?)  WHERE projectnum=(?)");
						updateStatement = connection.prepareStatement(updateQuery);
						updateStatement.setString(1,newInfo);
						updateStatement.setString(2,projectNumber);
						updateStatement.execute();

						break;
							
					case 3:
						System.out.println("Please enter the new Project Name:");						
						newInfo = input.nextLine();							
						updateQuery = ("UPDATE projects SET projectname=(?)  WHERE projectnum=(?)");
						updateStatement = connection.prepareStatement(updateQuery);
						updateStatement.setString(1,newInfo);
						updateStatement.setString(2,projectNumber);
						updateStatement.execute();
						break;
							
					case 4:
						System.out.println("Please enter the new Building Type:");
						
						newInfo = input.nextLine();
						updateQuery = ("UPDATE projects SET buildingtype=(?)  WHERE projectnum=(?)");
						updateStatement = connection.prepareStatement(updateQuery);
						updateStatement.setString(1,newInfo);
						updateStatement.setString(2,projectNumber);
						updateStatement.execute();
						break;
					case 5:
						System.out.println("Please enter the new Project Address:");
						newInfo = input.nextLine();
						updateQuery = ("UPDATE projects SET projectaddress=(?)  WHERE projectnum=(?)");
						updateStatement = connection.prepareStatement(updateQuery);
						updateStatement.setString(1,newInfo);
						updateStatement.setString(2,projectNumber);
						updateStatement.execute();
						break;
					case 6:
						System.out.println("Please enter the new Project ERF Number:");
						newInfo = input.nextLine();
						updateQuery = ("UPDATE projects SET projectERF=(?)  WHERE projectnum=(?)");
						updateStatement = connection.prepareStatement(updateQuery);
						updateStatement.setString(1,newInfo);
						updateStatement.setString(2,projectNumber);
						updateStatement.execute();
						break;
							
					case 7:
						System.out.println("Please enter the new Project Deadline:(YYYY MM DD)");
							
						String newInfoDate[];
						String newDueDateStr = "";					
						newInfo = input.nextLine();
						newInfoDate = newInfo.split(" ");							
						newDueDateStr = (newInfoDate[0] + "_" + newInfoDate[1] + "_"  +newInfoDate[2]);
						updateQuery = ("UPDATE projects SET projectdeadline=(?)  WHERE projectnum=(?)");
						updateStatement = connection.prepareStatement(updateQuery);
						updateStatement.setString(1,newDueDateStr);
						updateStatement.setString(2,projectNumber);
						updateStatement.execute();													
						break;
					case 8:
						System.out.println("Please enter the new Project Fee:");						
						newInfo = input.nextLine();
						updateQuery = ("UPDATE projects SET totalfee=(?)  WHERE projectnum=(?)");
						updateStatement = connection.prepareStatement(updateQuery);
						updateStatement.setString(1,newInfo);
						updateStatement.setString(2,projectNumber);
						updateStatement.execute();
						break;
					case 9:
						System.out.println("Please enter the new Fee Paid:");						
						newInfo = input.nextLine();
						updateQuery = ("UPDATE projects SET totalpaid=(?)  WHERE projectnum=(?)");
						updateStatement = connection.prepareStatement(updateQuery);
						updateStatement.setString(1,newInfo);
						updateStatement.setString(2,projectNumber);
						updateStatement.execute();
						break;
					case 10: System.out.println("Project Updated!");
						updated = false;
						break;
					}
			connection.close();	
			}
				
			}catch(SQLException e) {
				System.out.println("Database could not be found");
			}		
			
	}
	
	/**
	 * method used to finalise a project and write to finalised.txt
	 * @param projectNumber - project number of the project to be updated 
	 */
	public static void finaliseProject(String projectNumber) {
		//read existing lists from textfiles
		ArrayList<Project> projectList = readProjects();
		ArrayList<Client> clientList = Client.readClients();
				
		//create blank list used to write updated list to textfile
		Project finalisedProject = new Project();
				
		double totalPaid = 0.0;
		double totalFee = 0.0;
		String customerContact = "";
				
				
		for(int i = 0; i < projectList.size(); i ++) {
					
			/* change values in project object that is finalised
			 * and add to finalisedProjectList
			*/
			if (projectList.get(i).getProjectNumber().contains(projectNumber)){
						
				projectList.get(i).setFinalised("true");
				//finalisedProjectList.add(projectList.get(i));
				finalisedProject = projectList.get(i);
				totalPaid = Double.parseDouble(projectList.get(i).getTotalPaid());
				totalFee = Double.parseDouble(projectList.get(i).getTotalFee());
						
				//check if project is fully paid
				if (totalFee == totalPaid) {
					System.out.println("Project has been paid in full");
					}
						
				//print receipt as project is not fully paid
				else {
					for(int v = 0; v < clientList.size(); v ++) {
						if (clientList.get(v).getProjectNumber()
							.contains(finalisedProject.getProjectNumber().toString())) {
							customerContact = clientList.get(v).getContactNumber();
						}								
					}
							
					double totalOwed = totalFee - totalPaid;
					System.out.println("Amount still owed : R" + totalOwed + 
										 "\n" +
										"Customer Contact: " + customerContact);
					}				
				}														
		}
		
		try {
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/poised_db?useSSL=false",
					"otheruser","swordfish");
			String updateQuery = ("UPDATE projects SET completed=(?) WHERE projectnum=(?)");				
			updateStatement = connection.prepareStatement(updateQuery);
			updateStatement.setString(1, "true");
			updateStatement.setString(2, projectNumber);
			updateStatement.execute();
			connection.close();
			
		}catch(SQLException e) {
			System.out.println("Database could not be found");
		
		}		
	}
	
	/**
	 * Method takes in a Project object and adds it to the database
	 * @param project
	 */
	public static void projectAdd(Project project) {
		try {
			Connection connection = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/poised_db?useSSL=false",
				"otheruser","swordfish");	
			String insertQuery = ("INSERT INTO projects VALUES (?, ?, ?, ?, ?, "
								  + "?, ?, ?, ?, ?)");
			insertStatement = connection.prepareStatement(insertQuery);
			insertStatement.setString(1, project.getCustomerName());
			insertStatement.setString(2, project.getProjectNumber());
			insertStatement.setString(3, project.getProjectName());
			insertStatement.setString(4, project.getBuildingType());
			insertStatement.setString(5, project.getProjectAddress());
			insertStatement.setString(6, project.getProjectERF());
			insertStatement.setString(7, project.getDeadline());
			insertStatement.setString(8, project.getTotalFee());
			insertStatement.setString(9, project.getTotalPaid());
			insertStatement.setString(10, project.getFinalised());
			insertStatement.execute();						
		}catch(SQLException e) {
			System.out.println("Database could not be found");
		}
	}
	
	/**
	 * Method checks which projects are complete and displays them
	 */
	public static void showIncompleteProjects() {
		ArrayList<Project> projects = new ArrayList<Project>();
		projects  = Project.readProjects();
		int numFinalised = 0;
		
		for (int i = 0; i < projects.size(); i++) {
			if (projects.get(i).getFinalised().toString().equalsIgnoreCase("false")) {
				numFinalised ++;
				System.out.println(projects.get(i).toStr());
			}
		}
		
		if(numFinalised == 0) {
			System.out.println("All projects are finalised");
		}
	}
		
	
	
}
