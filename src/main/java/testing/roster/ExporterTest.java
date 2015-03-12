package testing.roster;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import javafx.scene.control.Button;
import model.driver.Debug;
import model.driver.Grader;
import model.roster.Exporter;
import model.roster.GradedItem;
import model.roster.Roster;
import model.roster.Student;

import org.junit.Test;

public class ExporterTest {

	/**
	 * @author Gavin Scott
	 */
	@Test
	public void testExportRosterToFile() throws IOException {
		Exporter exp = new Exporter();
		Roster rost = new Roster("TEST", "", 1, "", null, null);
		File file = new File(Roster.TEMP_NAME + ".rost");
		Exporter.exportRosterToFile(null, null);
		Exporter.exportRosterToFile(rost, null);
		Exporter.exportRosterToFile(null, file);
		Exporter.exportRosterToFile(rost, file);
		Files.deleteIfExists(FileSystems.getDefault().getPath(Roster.TEMP_NAME + ".rost"));
	}

	/**
	 * @author Gavin Scott
	 */
	@Test
	public void testExportToExcel() {
		Roster roster = new Roster("TEST", "", 1, "", null, null);
		GradedItem item = new GradedItem("Test", "", 100, false);
		roster.addAssignment(item);
		roster.addAssignment(new GradedItem("TestTestTestTestTestTestTest", "", 100, false));
		Student student = new Student("bob", "123", "123", "", false, 100);
		roster.addStudent(new Student("bo", "1234", "123", "", false, 100));
		roster.addStudent(new Student("bobbobbobbobbobbobbob", "12345", "123", "", false, 100));
		roster.addStudent(student);
		roster.addScore(student, item, new Double(30));
		File file = new File(Roster.TEMP_NAME + ".rost");
		
		Exporter.exportRosterToExcel(null, null);
		Exporter.exportRosterToExcel(null, file);
		Exporter.exportRosterToExcel(roster, null);
		Exporter.exportRosterToExcel(roster, file);
	}
}
