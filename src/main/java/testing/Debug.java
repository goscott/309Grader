package testing;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A class that offers standardized testing output and saved output logs
 * 
 * @author Gavin Scott
 *
 */
public class Debug {
	/** Controls whether log entries are printed to the console **/
	private final static boolean loggerPrint = true;
	/** Controls whether log entries are recorded in a log **/
	private final static boolean loggerRecord = true;
	/** The file name of the standard log **/
	private final static String fileName = "testlog.txt";

	/**
	 * Logs a message. Outputs it to the screen, and saves it to the output log
	 * 
	 * @param category
	 *            Disaplays a category before the message
	 * @param msg
	 *            The message
	 */
	public static void log(String category, String msg) {
		if (loggerPrint)
			System.out.println("    " + category.toUpperCase() + "   " + msg);
		if (loggerRecord && category.toLowerCase().contains("fail")) {
			try (PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(fileName, true)))) {
				out.println("    " + category.toUpperCase() + "   " + msg + '\n');
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Outputs to the log, but does not indent
	 * @param header
	 */
	public static void logHeader(String header) {
		if (loggerPrint)
			System.out.println(header + "...");
		if (loggerRecord)
			try (PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(fileName, true)))) {
				out.println(header + "...\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * Logs a message with no category
	 * 
	 * @param msg
	 *            The message
	 */
	public static void log(String msg) {
		if (loggerPrint)
			System.out.println("    " + msg);
		if (loggerRecord)
			try (PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(fileName, true)))) {
				out.println("    " + msg + '\n');
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/**
	 * Wipes the old log files
	 */
	public static void initialize() {
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
			writer.println("     GRADER TOOL TEST OUTPUT\n");
			writer.println("----------------------------------\n");
			writer.println("  Logged at " + dateFormat.format(cal.getTime()));
			writer.println("----------------------------------\n");
			writer.println("\n");
			writer.close();
		}
	}
}
