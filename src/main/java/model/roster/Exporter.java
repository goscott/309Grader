package model.roster;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import model.HarmlessException;
import model.driver.Debug;

/**
 * Allows for easy exportation of gradebooks to excel documents
 * 
 * @author Gavin Scott
 */
public class Exporter {
	/** name of the column containing student names **/
	private static String nameLabel = "Student Name";
	/** name of the column containing student ids **/
	private static String idLabel = "Student ID";

	/**
	 * Exports a roster to an excel document. All assignments are printed,
	 * including sub-assignments
	 */
	/*@
		requires(
			roster != null
		);
		ensures(
			// the excel file has been created
		);
	@*/
	public static void exportRosterToExcel(Roster roster, File file) {
		// don't save null rosters
		if (roster == null || file == null) {
			return;
		}
		String fileName = roster.courseName() + '-'
				+ String.format("%02d", roster.getSection());
		Debug.log("Exporting to excel", "Exporting " + fileName);
		// populate document
		WorkbookSettings settings = new WorkbookSettings();
		WritableWorkbook workbook = null;
		try {
			workbook = Workbook.createWorkbook(file, settings);

			workbook.createSheet(fileName, 0);
			WritableSheet sheet = workbook.getSheet(0);
			printRoster(roster, sheet);

			workbook.write();
			workbook.close();
			throw new HarmlessException();
		} catch (Exception ex) {
			Debug.log("Error", "Could not export to excel");
		}
	}

	/**
	 * Adds the data from the roster to the excel sheet
	 */
	/*@
		requires(
			roster != null
				&&
			sheet != null
		);
	@*/
	private static void printRoster(Roster roster, WritableSheet sheet)
			throws RowsExceededException, WriteException {
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		WritableCellFormat times = new WritableCellFormat(times10pt);
		int minColWidth = 10;
		List<Student> students = roster.getStudents();
		// format name cells
		int maxLength = nameLabel.length();
		for (Student student : students) {
			maxLength = maxLength > student.getName().length() ? maxLength
					: student.getName().length();
		}
		sheet.setColumnView(0, maxLength + 5);
		sheet.setColumnView(1, idLabel.length() + 5);
		int row = 1;
		sheet.addCell(new Label(0, 0, nameLabel, times));
		sheet.addCell(new Label(1, 0, idLabel, times));
		for (Student student : students) {
			sheet.addCell(new Label(0, row, student.getName(), times));
			sheet.addCell(new Label(1, row++, student.getId(), times));
		}
		int column = 2;
		for (GradedItem item : roster.getAssignments()) {
			sheet.addCell(new Label(column, 0, item.name(), times));
			sheet.setColumnView(column,
					item.name().length() > minColWidth ? item.name().length()
							: minColWidth);
			row = 1;
			for (Student student : students) {
				if (item.getStudentScore(student) != null) {
					sheet.addCell(new Number(column, row++, item
							.getStudentScore(student), times));
				}
			}
			column++;
		}
		sheet.addCell(new Label(column, 0, "Percentage", times));
		sheet.setColumnView(column, minColWidth);
		row = 1;
		for (Student student : students) {
				sheet.addCell(new Number(column, row++, student.getTotalPercentage(), times));
		}
		column++;
		sheet.addCell(new Label(column, 0, "Grade", times));
		sheet.setColumnView(column, minColWidth);
		row = 1;
		for (Student student : students) {
				sheet.addCell(new Label(column, row++, student.getGrade().getName(), times));
		}
	}

	/**
	 * Exports a roster as a .rost to the provided file
	 */
	/*@
		requires(
			roster != null
				&&
			file != null
		);
	@*/
	public static void exportRosterToFile(Roster roster, File file) {
		try {
			if(roster == null || file == null) {
				throw new NullPointerException();
			}
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(file));
			out.writeObject(roster);
			out.close();
		} catch (Exception ex) {
			Debug.log("SAVE ERROR", "failed to save Roster.");
		}
	}
}
