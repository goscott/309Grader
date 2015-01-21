package view.roster;

import javax.swing.JOptionPane;

import model.driver.Debug;
import model.driver.Grader;
import model.roster.GradedItem;
import model.roster.Student;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
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
	private Button asgnButton;
	@FXML
	private Button studentButton;
	@FXML
	private Button removeStudentButton;
	@FXML
	private Button removeAsgnButton;

	private ContextMenu rightClickMenu;

	@FXML
	/**
	 * Initializes the gradebook view
	 */
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
		refresh();

		// create popup menu
		rightClickMenu = new ContextMenu();
		MenuItem addAssignment = new MenuItem("Add Assignment");
		MenuItem dropAssignment = new MenuItem("Drop Assignment");
		MenuItem importAssignment = new MenuItem("Import Assignment");
		MenuItem addStudent = new MenuItem("Add Student");
		MenuItem dropStudent = new MenuItem("Drop Student");
		MenuItem rosterSynch = new MenuItem("Roster Synch");

		addAssignment.setOnAction(new DisplayAddAssignmentPopupEventHandler(
				addAssignment, this));
		addStudent.setOnAction(new DisplayAddStudentPopupEventHandler(
				addStudent, this));

		rightClickMenu.getItems().add(addAssignment);
		rightClickMenu.getItems().add(dropAssignment);
		rightClickMenu.getItems().add(importAssignment);
		rightClickMenu.getItems().add(new SeparatorMenuItem());
		rightClickMenu.getItems().add(addStudent);
		rightClickMenu.getItems().add(dropStudent);
		rightClickMenu.getItems().add(new SeparatorMenuItem());
		rightClickMenu.getItems().add(rosterSynch);

		// show popup menu on right click
		mainTable.addEventFilter(MouseEvent.MOUSE_PRESSED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (event.isSecondaryButtonDown()) {
							rightClickMenu.show(mainTable.getScene()
									.getWindow(), event.getScreenX(), event
									.getScreenY());
							event.consume();
						}
					}
				});
	}

	/**
	 * Checks if a column exists with the given name
	 * @param name the name being checked
	 * @return boolean true if there is already a 
	 * column in the gradebook with that name
	 */
	private boolean columnExists(String name) {
		for (TableColumn<?, ?> col : mainTable.getColumns()) {
			if (checkChildren(col, name))
				return true;
		}
		return false;
	}

	/**
	 * Checks if a column exists with the given name
	 * as a child of a column. Checks recursively.
	 * @param col The column being checked
	 * @param name The name beign searched for
	 * @return boolean true if a column exists with the 
	 * given name in any child column
	 */
	private boolean checkChildren(TableColumn<?, ?> col, String name) {
		if (col.getText().equals(name)) {
			return true;
		}
		for (TableColumn<?, ?> sub : col.getColumns()) {
			if (checkChildren(sub, name)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	/**
	 * Forces a refresh of the gradebook GUI to ensure new
	 * students, assignments, or scores will be accurately
	 * visible at all times.
	 */
	void refresh() {
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
								// commit grade change to the gradebook
								public void handle(
										CellEditEvent<Student, String> t) {
									try {
										double newGrade = Double.parseDouble(t
												.getNewValue());
										if (newGrade < 0 || newGrade > 100) {
											throw new NumberFormatException();
										}
										Grader.addScore(t.getRowValue(), t
												.getTableColumn().getText(),
												newGrade);
									} catch (NumberFormatException ex) {
										Tooltip tip = new Tooltip(
												"Invalid input");
										tip.setAutoFix(true);
										tip.setAutoHide(true);
										tip.show(t.getTableView().getScene()
												.getWindow());
										Debug.log("User Input Error",
												"Entered invalid grade");
									}
									refresh();
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
			// force hard refresh
			mainTable.getColumns().add(0, new TableColumn<Student, String>());
			mainTable.getColumns().remove(0);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	/**
	 * Adds a sub-column to the gradebook
	 * @param item The new GradedItem
	 * @param newColumn the new Column
	 */
	private void addSubColumn(GradedItem item, TableColumn newColumn) {
		for (int ndx = 0; ndx < mainTable.getColumns().size(); ndx++) {
			if (mainTable.getColumns().get(ndx).getText()
					.equals(item.getParent().name())) {
				mainTable.getColumns().get(ndx).getColumns().add(newColumn);
			}
		}
		refresh();
	}
}
