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

import model.roster.GradedItem;
import model.roster.Roster;

/**
 * A class that offers standardized debugging output and saved output/error logs
 * 
 * @author Gavin Scott
 *
 */
public class Debug {
	/** Controls whether log entries are printed to the console **/
	public static boolean loggerPrint = true;
	/** Controls whether log entries are recorded in a log **/
	public static boolean loggerRecord = true;
	/** The file name of the standard log **/
	private final static String fileName = "log.txt";
	/** The file name of the error log **/
	private final static String errorFileName = "error_log.txt";
	/** The default roster name **/
	public final static String DEFAULT_NAME = "AUTO-POPULATED";

	/**
	 * Logs a message. Outputs it to the screen, and saves it to the output log
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
	 */
	public static void log(String msg) {
		if (loggerPrint)
			System.out.println(msg);
		if (loggerRecord)
			try (PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(fileName, true)))) {
				out.println(msg + '\n');
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/**
	 * Initializes the program with some data
	 */
	public static void autoPopulate() {
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();		
		start.set(2015, 01, 01);
        end.set(2015, 04, 01);
        Roster roster = new Roster(DEFAULT_NAME, "", 1,
                "", start, end);
        
		Grader.addRoster(roster);
		Grader.setCurrentRoster(roster);
		
		Grader.addAssignment(new GradedItem("Test", "sfds", 100, false));
		GradedItem test2 = new GradedItem("Midterms", "sfds", 150, false);
		GradedItem test3 = new GradedItem("Midterm 1", "sfds", 150, test2,
				false);
		Grader.addAssignment(test2);
		Grader.addAssignment(test3);
		Grader.addAssignment(new GradedItem("Q1", "wefgr", 75, test3, false));
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
