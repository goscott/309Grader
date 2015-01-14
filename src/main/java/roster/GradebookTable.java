package roster;

import java.awt.Label;
import java.io.IOException;

import driver.Grader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

/**
 * Controls the visual gradebook
 * @author Gavin Scott
 */
public class GradebookTable {
	@FXML
	private TableView<Student> mainTable;
	@FXML
	private TableColumn<Student, String> idCol;
	@FXML
	private TableColumn<Student, Double> totalGradeCol;
	@FXML
	private TableColumn<Student, String> nameCol;
	@FXML
	private ScrollPane scrollPane;

	private ObservableList<Student> data = FXCollections
			.observableArrayList();/*
					new Student("Gavin", "11111"), 
					new Student("Michael", "22222"), 
					new Student("Frank", "33333"),
					new Student("Jacob", "44444"),
					new Student("Shelli", "55555"),
					new Student("Mason", "66666"));*/

	@FXML
	protected void initialize() {		
		mainTable.setEditable(false);
		
		nameCol.setMinWidth(100);
		nameCol.setEditable(false);
		nameCol.setCellValueFactory(new PropertyValueFactory<Student, String>(
				"name"));
		
		idCol.setMinWidth(100);
		idCol.setEditable(false);
		idCol.setCellValueFactory(new PropertyValueFactory<Student, String>(
				"id"));
		
		totalGradeCol.setEditable(false);
		totalGradeCol.setMinWidth(100);
		totalGradeCol.setCellValueFactory(new PropertyValueFactory<Student, Double>(
				"totalScore"));
		
		mainTable.setItems(Grader.getStudentList());
	}
	
	@FXML
	void asgnButton(ActionEvent e) {
		TableColumn<Student, String> temp = new TableColumn<Student, String>("Test");
		temp.setMinWidth(100);
		temp.setEditable(true);
		mainTable.getColumns().addAll(temp);
	}
	
	@FXML
	void studentButton(ActionEvent e) {
		Grader.addStudent(new Student("New Guy", "12345"));
		mainTable.setItems(Grader.getStudentList());
	}
}
