package model.roster;

import javax.swing.JOptionPane;

import model.driver.Grader;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Controls the visual gradebook
 * 
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
	@FXML
	Button asgnButton;
	@FXML
	Button studentButton;
	@FXML
	Button removeStudentButton;
	@FXML
	Button removeAsgnButton;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@FXML
	protected void initialize() {
		mainTable.setEditable(true);

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
		totalGradeCol
				.setCellValueFactory(new PropertyValueFactory<Student, Double>(
						"totalScore"));

		for (GradedItem item : Grader.getRoster().getAssignments()) {
			final TableColumn<Student, String> newColumn = new TableColumn<Student, String>(
					item.name());
			newColumn.setMinWidth(100);
			newColumn.setEditable(true);

			newColumn.setCellValueFactory(new Callback() {
				public SimpleStringProperty call(
						CellDataFeatures<Student, Double> param) {
					return new SimpleStringProperty(param.getValue().getAssignmentScore(newColumn.getText()).toString());
				}

				public Object call(Object param) {
					return call((CellDataFeatures<Student, Double>) (param));
				}
			});
			newColumn.setCellFactory(TextFieldTableCell.<Student> forTableColumn());
			/* When a user types a change */
			newColumn.setOnEditCommit(new EventHandler<CellEditEvent<Student, String>>() {
				public void handle(CellEditEvent<Student, String> t) {
					System.out.println("typing...");
				}
			});
			if(!item.hasParent()) {
				mainTable.getColumns().add(newColumn);
			}
			else {
				addSubColumn(item, newColumn);
			}
			
		}
		mainTable.setItems(Grader.getStudentList());
	}

	@FXML
	void asgnButton(ActionEvent e) {
		// TODO delete
		System.out.println("*****************");
		for(Student s : Grader.getStudentList()) {
			System.out.print("STUDENT NAME: " + s.getName() + " ");
			for(GradedItem item : Grader.getAssignmentList()) {
				System.out.print(item.name() + ": " + s.getAssignmentScore(item.name()));
			}
			System.out.println();
		}

		System.out.println("*****************");
		
		Stage newStage = new Stage();
		AddAssignmentDialog popup = new AddAssignmentDialog();
		popup.start(newStage);
		popup.setParent(this);
		asgnButton.setDisable(true);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	void addAssignmentColumn(String asgn, String descr, GradedItem asgnParent) {
		final TableColumn<Student, Double> newColumn = new TableColumn<Student, Double>(
				asgn);
		newColumn.setMinWidth(100);
		newColumn.setEditable(true);

		GradedItem item = new GradedItem(asgn, descr, asgnParent);
		Grader.addAssignment(item);

		newColumn.setCellValueFactory(new Callback() {
			public SimpleDoubleProperty call(
					CellDataFeatures<Student, Double> param) {
				return param.getValue().getAssignmentScoreAsProperty(
						newColumn.getText());
			}

			public Object call(Object param) {
				return call((CellDataFeatures<Student, Double>) (param));
			}
		});
		
		if(asgnParent == null) {
			mainTable.getColumns().add(newColumn);
		} else {
			addSubColumn(item, newColumn);
		}
		
	}

	@FXML
	void studentButton(ActionEvent e) {
		String name = JOptionPane.showInputDialog("Please input a name");
		Grader.addStudent(new Student(name, ""
				+ (name.length() * 236 - name.charAt(0))));
		mainTable.setItems(Grader.getStudentList());
	}
	
	private void addSubColumn(GradedItem item, TableColumn newColumn) {
		for(int ndx = 0; ndx < mainTable.getColumns().size(); ndx++) {
			if(mainTable.getColumns().get(ndx).getText().equals(item.getParent().name())) {
				mainTable.getColumns().get(ndx).getColumns().add(newColumn);
			}
		}
	}
}
