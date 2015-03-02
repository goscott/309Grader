package model.server;

import java.util.ArrayList;

import model.driver.Debug;
import model.roster.Roster;

/**
 * A temporary version of the PolyLearn server
 * @author Gavin Scott
 */
public class PeopleSoftServer {
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
		ArrayList<String> list = new ArrayList<String>();
		list.add("00000");
		list.add("11111");
		list.add("22222");
		list.add("33333");
		list.add("44444");
		list.add("55555");
		list.add("66666");
		list.add("77777");
		list.add("88888");
		list.add("99999");
		list.add("54321");
		list.add("12345");
		list.add("67890");
		list.add("01234");
		list.add("23456");
		list.add("34567");
		list.add("45678");
		list.add("56789");
		list.add("01010");
		list.add("10101");
		list.add("02020");
		list.add("20202");
		list.add("03030");
		list.add("30303");
		list.add("04040");
		list.add("40404");
		list.add("05050");
		list.add("50505");
		return list;
	}
}
