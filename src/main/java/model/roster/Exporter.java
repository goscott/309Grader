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

import model.driver.Grader;

/**
 * Allows for easy exportation of gradebooks
 * to excel documents
 * @author Gavin Scott
 */
public class Exporter {

	/**
	 * Exports a roster to an excel document. All assignments are
	 * printed, including sub-assignments
	 */
	public static void exportRoster(Roster roster) {
		String fileName = roster.courseName() + "-"
				+ String.format("%02d", roster.getSection());
		File file = new File(fileName + ".xls");

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
	}

	/**
	 * Adds the data from the roster to the excel sheet
	 */
	private static void printRoster(Roster roster, WritableSheet sheet)
			throws RowsExceededException, WriteException {
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		WritableCellFormat times = new WritableCellFormat(times10pt);
		int minColWidth = 10;
		String nameLabel = "Student Name";
		String idLabel = "Student ID";
		List<Student> students = Grader.getStudentList();

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
		for (GradedItem item : Grader.getAssignmentList()) {
			sheet.addCell(new Label(column, 0, item.name(), times));
			sheet.setColumnView(column,
					item.name().length() > minColWidth ? item.name().length()
							: minColWidth);
			row = 1;
			for (Student student : students) {
				sheet.addCell(new Number(column, row++, item
						.getStudentGrade(student), times));
			}
			column++;
		}
	}
}
