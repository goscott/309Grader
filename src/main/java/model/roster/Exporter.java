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
import java.io.IOException;
import java.util.List;

import controller.mainpage.Alert;
import model.driver.Debug;

/**
 * Allows for easy exportation of gradebooks to excel documents
 * 
 * @author Gavin Scott
 */
public class Exporter {
	/** folder containing exported files **/
	private static String exportFolderName = "Exports";
	/** export filetype **/
	private static String fileType = ".xls";
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
	public static void exportRoster(Roster roster) {
		// don't save null rosters
		if (roster == null) {
			return;
		}
		// check export directory exists
		File dir = new File(exportFolderName);
		if (!dir.exists()) {
			dir.mkdir();
		}
		String fileName = roster.courseName() + '-'
				+ String.format("%02d", roster.getSection());
		File file = new File(exportFolderName + '/' + fileName + fileType);
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
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (WriteException ex) {
			ex.printStackTrace();
		}
		Alert.show(fileName + " exported to " + exportFolderName + '/' + fileName + fileType);
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

		// print student names
		int row = 1;
		sheet.addCell(new Label(0, 0, nameLabel, times));
		sheet.addCell(new Label(1, 0, idLabel, times));
		for (Student student : students) {
			sheet.addCell(new Label(0, row, student.getName(), times));
			sheet.addCell(new Label(1, row++, student.getId(), times));
		}

		// print grades for each assignment
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
	}
}
