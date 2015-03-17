package model.server;

import java.util.ArrayList;
import model.driver.Debug;
import model.roster.Roster;
import model.roster.Student;

/**
 * A temporary version of the PolyLearn server
 * 
 * @author Gavin Scott
 */
public class PeopleSoftServer {
	/** The list of students stored in the peoplesoft server **/
	private static ArrayList<Student> list;

	/** Populates the server with dummy data **/
	public static void initialize() {
		list = new ArrayList<Student>();
		list.add(new Student("user0", "00000","19403278", "Softwhere Engeineering", false, 4));
		list.add(new Student("user1", "11111", "09132432", "Softwhere Engeineering", false, 4));
		list.add(new Student("user2", "22222", "14235514", "Softwhere Engeineering", false, 4));
		list.add(new Student("user3", "33333", "32432544", "Softwhere Engeineering", false, 4));
		list.add(new Student("user4", "44444", "19403278", "Softwhere Engeineering", false, 4));
		list.add(new Student("user5", "55555", "132451532", "Softwhere Engeineering", false, 4));
		list.add(new Student("user6", "66666", "13245132", "Softwhere Engeineering", false, 4));
		list.add(new Student("user7", "77777", "1942278", "Softwhere Engeineering", false, 4));
		list.add(new Student("user8", "88888", "1324532", "Softwhere Engeineering", false, 4));
		list.add(new Student("user9", "99999", "123413242", "Softwhere Engeineering", false, 4));
		list.add(new Student("Gavin Scott", "54321", "123413254", "Softwhere Engeineering", false, 4));
		list.add(new Student("Shelli Crispen", "12345", "1234213", "Softwhere Engeineering", false, 4));
		list.add(new Student("Frank Poole", "67890", "12345678", "Softwhere Engeineering", false, 4));
		list.add(new Student("Michael Lenz", "01234", "1412424523", "Softwhere Engeineering", false, 4));
		list.add(new Student("Mason Stevenson", "23456", "132453", "Softwhere Engeineering", false, 4));
		list.add(new Student("user10", "34567", "132141325", "Softwhere Engeineering", false, 4));
		list.add(new Student("user11", "45678", "213412943", "Softwhere Engeineering", false, 4));
		list.add(new Student("user12", "56789", "213590", "Softwhere Engeineering", false, 4));
		list.add(new Student("user13", "01010", "3170720", "Softwhere Engeineering", false, 4));
		list.add(new Student("user14", "10101", "324872", "Softwhere Engeineering", false, 4));
		list.add(new Student("user15", "02020", "2134iu5", "Softwhere Engeineering", false, 4));
		list.add(new Student("user16", "20202", "123412134", "Softwhere Engeineering", false, 4));
		list.add(new Student("user17", "03030", "132123495", "Softwhere Engeineering", false, 4));
		list.add(new Student("user18", "30303", "12534124", "Softwhere Engeineering", false, 4));
		list.add(new Student("user19", "04040", "1234125", "Softwhere Engeineering", false, 4));
		list.add(new Student("user20", "40404", "123413254", "Softwhere Engeineering", false, 4));
		list.add(new Student("admin", "05050", "12355432", "Softwhere Engeineering", false, 4));
		list.add(new Student("admin2", "50505", "4576578", "Softwhere Engeineering", false, 4));
		list.add(new Student("user21", "21549", "532473", "Softwhere Engeineering", false, 4));
		list.add(new Student("user22", "34718", "34634578", "Softwhere Engeineering", false, 4));
		list.add(new Student("user23", "87123", "2345745", "Softwhere Engeineering", false, 4));
		list.add(new Student("user24", "98012", "43676585", "Softwhere Engeineering", false, 4));
		list.add(new Student("user25", "01968", "89765334", "Softwhere Engeineering", false, 4));
	}
	
	/**
	 * Commits a finalized roster to polylearn
	 */
	public static void commitRoster(Roster roster) {
		// doesn't do anything
		Debug.log("PolyLearn", roster.courseName() + " sent to polylearn");
	}

	/**
	 * Gets all possible IDs for Cal Poly users (empl ids)
	 */
	public static ArrayList<String> getAllEmplIds() {
		ArrayList<String> ret = new ArrayList<String>();
		for(Student student : list) {
			ret.add(student.getId());
		}
		return ret;
	}
	
	/**
	 * Gets all the students stored in the server
	 */
	public static ArrayList<Student> getStudents() {
		return list;
	}
	
	/**
	 * Gets the server's list of all students associated with a roster
	 * (currently dummy data)
	 */
	public static ArrayList<Student> getAssociatedStudents(Roster roster) {
		ArrayList<Student> retList = new ArrayList<Student>();
		for(int ndx = 0; ndx < 10; ndx++) {
			retList.add(list.get(ndx));
		}
		return retList;
	}
}
