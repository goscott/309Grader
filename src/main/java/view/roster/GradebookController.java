package view.roster;

import javax.swing.JOptionPane;

import model.driver.Grader;
import model.roster.GradedItem;
import model.roster.Student;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Controls the visual gradebook
 * 
 * @author Gavin Scott
 */
public class GradebookController {
	@FXML
	private TableView<Student> mainTable;
	@FXML
	private BorderPane mainPane;
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
		refreshAssignments();

		// update whenever tab selected
		mainTable.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean oldPropertyValue, Boolean newPropertyValue) {
				if (newPropertyValue) {
					refreshAssignments();
				}
			}
		});
	}

	private boolean columnExists(String name) {
		for (TableColumn<?, ?> col : mainTable.getColumns()) {
			if(checkChildren(col, name))
				return true;
		}
		return false;
	}
	
	private boolean checkChildren(TableColumn<?, ?> col, String name) {
		if(col.getText().equals(name)) {
			return true;
		}
		for(TableColumn<?, ?> sub : col.getColumns()) {
			if(checkChildren(sub, name)) {
				return true;
			}
		}
		return false;
	}

	private void refreshAssignments() {
		if (Grader.getRoster() != null) {
			for (GradedItem item : Grader.getRoster().getAssignments()) {
				if (!columnExists(item.name())) {
					final TableColumn<Student, String> newColumn = new TableColumn<Student, String>(
							item.name());
					newColumn.setMinWidth(100);
					newColumn.setEditable(true);

					newColumn.setCellValueFactory(new Callback() {
						public SimpleStringProperty call(
								CellDataFeatures<Student, Double> param) {
							return new SimpleStringProperty(param.getValue()
									.getAssignmentScore(newColumn.getText())
									.toString());
						}

						public Object call(Object param) {
							return call((CellDataFeatures<Student, Double>) (param));
						}
					});
					newColumn.setCellFactory(TextFieldTableCell
							.<Student> forTableColumn());
					/* When a user types a change */
					newColumn
							.setOnEditCommit(new EventHandler<CellEditEvent<Student, String>>() {
								public void handle(
										CellEditEvent<Student, String> t) {
								}
							});
					if (!item.hasParent()) {
						mainTable.getColumns().add(newColumn);
					} else {
						addSubColumn(item, newColumn);
					}
				}
			}
			mainTable.setItems(Grader.getStudentList());
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
		for (int ndx = 0; ndx < mainTable.getColumns().size(); ndx++) {
			if (mainTable.getColumns().get(ndx).getText()
					.equals(item.getParent().name())) {
				mainTable.getColumns().get(ndx).getColumns().add(newColumn);
			}
		}
	}
}
