package roster;

import java.awt.Label;
import java.io.IOException;

import javax.swing.JOptionPane;

import driver.Grader;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

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
	
	@SuppressWarnings("unchecked")
	@FXML
	void asgnButton(ActionEvent e) {
		String asgn = JOptionPane.showInputDialog("Please input an assignment name");
		
		final TableColumn<Student, Double> newColumn = new TableColumn<Student, Double>(asgn);		
		newColumn.setMinWidth(100);
		newColumn.setEditable(true);
		
		Grader.addAssignment(new GradedItem(newColumn.getText(), "empty"));
		
		 newColumn.setCellValueFactory(new Callback() {
			public SimpleDoubleProperty call(CellDataFeatures<Student, Double> param) {
				return param.getValue().getAssignmentScoreAsProperty(newColumn.getText());
			}
			public Object call(Object param) {
				System.out.println("hit other one");
				return call((CellDataFeatures<Student, Double>)(param));
			}
		});
		
		mainTable.getColumns().add(newColumn);
	}
	
	@FXML
	void studentButton(ActionEvent e) {
		String name = JOptionPane.showInputDialog("Please input a name");
		Grader.addStudent(new Student(name, ""+(name.length()*236 - name.charAt(0))));
		mainTable.setItems(Grader.getStudentList());
	}
}
