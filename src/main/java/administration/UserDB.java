package administration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class UserDB {
	private Collection<User> users;
	private static final String DATABASE = "src/main/java/administration/users.udb";
	
	public UserDB() {
		loadUserDB();
		print();
	}
	
	/**
	 * Loads a list of users into the db.
	 */
	private void loadUserDB() {
		File targetFile;
		BufferedReader reader;
		String line = "";
		String[] tokens;
		int index;
		
		users = new ArrayList<User>();
		
		targetFile = new File(DATABASE);
		
		try {
			
			//check if file exists
			if (!targetFile.exists()) {
				targetFile.createNewFile();
			}
			
			reader = new BufferedReader(new FileReader(targetFile));
			
			while ((line = reader.readLine()) != null) {
				tokens = line.split(",");
				index = 0;
				users.add(new User(tokens[index++], tokens[index++], tokens[index++], tokens[index++], tokens[index++].charAt(0)));
			}
			
			reader.close();
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean addUser(User newUser) {
		
		BufferedWriter writer = null;
		
		//check the db
		if (!users.contains(newUser))
		{
			try {
				writer = new BufferedWriter(new FileWriter(DATABASE, true));
				
				//add the user to the db file
				writer.append(newUser.getfName() + "," + newUser.getlName() + "," + newUser.getId() 
						+ "," + newUser.getPassword() + "," + newUser.getType() + "\n");
				writer.close();
				
				//add the user to the db
				users.add(newUser);
			} 
			
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return true;
		}
		
		return false;
	}
	
	public boolean removeUser(User target) {
		return false;
	}
	
	public void print() {
		
		System.out.println("USER DB:");
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.printf("%-20s%-20s%-20s%-20s%-20s\n\n", "First", "Last", "ID", "Password", "Type");
		
		for (User u : users) {
			System.out.printf("%-20s%-20s%-20s%-20s%-20c\n", u.getfName(), u.getlName(), u.getId(), u.getPassword(), u.getType());
		}
		System.out.println("----------------------------------------------------------------------------------------\n\n");
	}
}
