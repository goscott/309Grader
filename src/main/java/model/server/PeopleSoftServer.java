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
	private static ArrayList<Student> list;

	public static void initialize() {
		list = new ArrayList<Student>();
		list.add(new Student("user0", "00000"));
		list.add(new Student("user1", "11111"));
		list.add(new Student("user2", "22222"));
		list.add(new Student("user3", "33333"));
		list.add(new Student("user4", "44444"));
		list.add(new Student("user5", "55555"));
		list.add(new Student("user6", "66666"));
		list.add(new Student("user7", "77777"));
		list.add(new Student("user8", "88888"));
		list.add(new Student("user9", "99999"));
		list.add(new Student("Gavin Scott", "54321"));
		list.add(new Student("Shelli Crispen", "12345"));
		list.add(new Student("Frank Poole", "67890"));
		list.add(new Student("Michael Lenz", "01234"));
		list.add(new Student("Mason Stevenson", "23456"));
		list.add(new Student("user10", "34567"));
		list.add(new Student("user11", "45678"));
		list.add(new Student("user12", "56789"));
		list.add(new Student("user13", "01010"));
		list.add(new Student("user14", "10101"));
		list.add(new Student("user15", "02020"));
		list.add(new Student("user16", "20202"));
		list.add(new Student("user17", "03030"));
		list.add(new Student("user18", "30303"));
		list.add(new Student("user19", "04040"));
		list.add(new Student("user20", "40404"));
		list.add(new Student("admin", "05050"));
		list.add(new Student("admin2", "50505"));
		list.add(new Student("user21", "21549"));
		list.add(new Student("user22", "34718"));
		list.add(new Student("user23", "87123"));
		list.add(new Student("user24", "98012"));
		list.add(new Student("user25", "01968"));
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
	
	public static ArrayList<Student> getStudents() {
		return list;
	}
}
