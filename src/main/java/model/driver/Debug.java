package model.driver;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import controller.roster.AddAssignmentDialogController;
import model.roster.GradedItem;
import model.roster.Roster;
import model.roster.ScoreNode;
import model.roster.Student;

/**
 * A class that offers standardized debugging
 * output and saved output/error logs
 * 
 * @author Gavin Scott
 *
 */
public class Debug {
	private final static boolean loggerPrint = true;
	private final static boolean loggerRecord = true;
	private final static String fileName = "log.txt";
	private final static String errorFileName = "error_log.txt";

	/**
	 * Logs a message. Outputs it to the screen, and saves it to the
	 * output log
	 * 
	 * @param category Disaplays a category before the message
	 * @param msg The message
	 */
	public static void log(String category, String msg) {
		if (loggerPrint)
			System.out.println(category.toUpperCase() + " : " + msg);
		if (loggerRecord) {
			try (PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(fileName, true)))) {
				out.println(category.toUpperCase() + " : " + msg + '\n');
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (category.toLowerCase().contains("error")) {
				try (PrintWriter out = new PrintWriter(new BufferedWriter(
						new FileWriter(fileName, true)))) {
					out.println(category.toUpperCase() + " : " + msg + '\n');
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Logs a message with no category
	 * 
	 * @param msg The message
	 */
	public static void log(String msg) {
		if (loggerPrint)
			System.out.println(msg);
		if (loggerRecord)
			try (PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter("log.txt", true)))) {
				out.println(msg + '\n');
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/**
	 * Initializes the program with some data
	 */
	public static void autoPopulate() {
		Roster roster = new Roster("CPE 309", "Winter 2015", "Gene Fisher");
		Grader.addRoster(roster);
		Grader.setCurrentRoster(roster);
		Grader.addAssignment(new GradedItem("Test", "sfds", 100));
		GradedItem test2 = new GradedItem("Midterms", "sfds", 150);
		GradedItem test3 = new GradedItem("Midterm 1", "sfds", 150, test2);
		GradedItem test4 = new GradedItem("Midterm 2", "sfds", 150, test2);
		GradedItem test5 = new GradedItem("Midterm 3", "sfds", 150, test2);
		Grader.addAssignment(test2);
		Grader.addAssignment(test3);
		Grader.addAssignment(test4);
		Grader.addAssignment(test5);
		Grader.addAssignment(new GradedItem("Q1", "wefgr", 75, test3));
		Grader.addAssignment(new GradedItem("Q2", "wefgr", 75, test3));

		Student bob = new Student("Bob", "123");
		Student bill = new Student("Bill", "321");

		Grader.addStudent(new Student("Bob", "123"));
		Grader.addStudent(new Student("Bill", "321"));
		Grader.addScore(bob, "Midterm 1", 90);
		Grader.addScore(bob, "Midterm 2", 86);
		Grader.addScore(bob, "Midterm 3", 95);
		Grader.addScore(bill, "Midterm 1", 54);
		Grader.addScore(bill, "Midterm 2", 67);
		Grader.addScore(bill, "Midterm 3", 59);
	}

	/**
	 * Wipes the old log files
	 */
	public static void newFile() {
		if (loggerRecord) {
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(fileName, "UTF-8");
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			writer.println("----------------------------------\n");
			writer.println("     GRADER TOOL LOG OUTPUT\n");
			writer.println("----------------------------------\n");
			writer.println("  Logged at " + dateFormat.format(cal.getTime()));
			writer.println("----------------------------------\n");
			writer.println("\n");
			writer.close();
			newErrorFile();
		}
	}

	/**
	 * Wipes the old error file
	 */
	private static void newErrorFile() {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(errorFileName, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		writer.println("----------------------------------\n");
		writer.println("   GRADER TOOL ERROR LOG OUTPUT\n");
		writer.println("----------------------------------\n");
		writer.println("  Logged at " + dateFormat.format(cal.getTime()));
		writer.println("----------------------------------\n");
		writer.println("\n");
		writer.close();
	}
}
