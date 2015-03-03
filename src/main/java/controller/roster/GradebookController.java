package controller.roster;

import java.awt.Color;
import java.util.ArrayList;

import controller.administration.UserLoginController;
import model.administration.PermissionKeys;
import model.curve.Grade;
import model.driver.Grader;
import model.roster.GradedItem;
import model.roster.Roster;
import model.roster.Student;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * Controls the visual gradebook
 * 
 * @author Gavin Scott
 */
public class GradebookController {
	/** The main gradebook table **/
	@FXML
	private TableView<Student> mainTable;
	/** The column showing the students' ids **/
	@FXML
	private TableColumn<Student, String> idCol;
	/** The column showing the students' total grades **/
	@FXML
	private TableColumn<Student, Double> totalGradeCol;
	/** The column showing the students' names **/
	@FXML
	private TableColumn<Student, String> nameCol;
	/** The scrollpane that holds the TableView **/
	@FXML
	private ScrollPane scrollPane;

	/** The right-click menu **/
	private ContextMenu rightClickMenu;
	/** An ArrayList holding the names of the columns that are expanded **/
	private ArrayList<String> expanded;
	/** The instance of the controller **/
	private static GradebookController singleton;
	/** checks if accessing the current roster **/
	private boolean current = true;
	/** The roster, if not current **/
	private Roster roster = null;

	public static GradebookController getController() {
		return singleton;
	}

	@FXML
	/**
	 * Initializes the gradebook view
	 */
	protected void initialize() {
		singleton = this;
		mainTable.setEditable(true);
		expanded = new ArrayList<String>();
		for (String item : getRoster().getAssignmentNameList()) {
			setAssignmentExpansion(item, true);
		}
		fullRefresh();

		// create right-click menu
		rightClickMenu = new ContextMenu();
		MenuItem expandCollapse = new MenuItem("Expand/Collapse Columns");
		expandCollapse.setOnAction(new DisplayExpandCollapsePopupEventHandler(
				expandCollapse, this));
		// students don't get these options
		if (current
				&& Grader.getUser().getPermissions()
						.contains(PermissionKeys.EDIT_GRADEBOOK)) {
			MenuItem addAssignment = new MenuItem("Add Assignment");
			MenuItem dropAssignment = new MenuItem("Drop Assignment");
			MenuItem addStudent = new MenuItem("Add Student");
			MenuItem dropStudent = new MenuItem("Drop Student");

			addAssignment
					.setOnAction(new DisplayAddAssignmentPopupEventHandler(
							addAssignment, this));
			dropAssignment
					.setOnAction(new DisplayDropAssignmentPopupEventHandler(
							dropAssignment, this));
			addStudent.setOnAction(new DisplayAddStudentPopupEventHandler(
					addStudent, this));
			dropStudent.setOnAction(new DisplayDropStudentPopupEventHandler(
					dropStudent, this));

			rightClickMenu.getItems().addAll(expandCollapse,
					new SeparatorMenuItem(), addAssignment, dropAssignment,
					new SeparatorMenuItem(), addStudent, dropStudent);
		} else {
			rightClickMenu.getItems().addAll(expandCollapse);
		}

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
	 * Supresses unnecessary warnings
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	/**
	 * Adds the far-right columns (Total percentage and Grade) to the gradebook.
	 */
	private void endColumns() {
		TableColumn<Student, Double> percentCol = new TableColumn<Student, Double>(
				"Percentage");
		percentCol.setEditable(false);
		percentCol.setMinWidth(100);
		percentCol
				.setCellValueFactory(new PropertyValueFactory<Student, Double>(
						"totalPercentage"));
		mainTable.getColumns().add(percentCol);

		TableColumn<Student, Grade> gradeCol = new TableColumn<Student, Grade>(
				"Grade");
		gradeCol.setMinWidth(100);
		gradeCol.setEditable(false);

		gradeCol.setCellFactory(new Callback<TableColumn<Student, Grade>, TableCell<Student, Grade>>() {
			@Override
			public TableCell<Student, Grade> call(
					TableColumn<Student, Grade> param) {
				TableCell cell = new TableCell() {
					@Override
					public void updateItem(Object item, boolean empty) {
						if (item != null) {
							setText(item.toString());
							Grade grade = getRoster().getCurve().getGrade(
									item.toString());
							Color col = grade.getColor();
							setStyle("-fx-background-color: rgb("
									+ col.getRed() + ", " + col.getGreen()
									+ ", " + col.getBlue() + ")");
						}
					}
				};
				cell.setAlignment(Pos.CENTER);

				return cell;
			}
		});

		gradeCol.setCellValueFactory(new Callback() {
			public SimpleStringProperty call(
					CellDataFeatures<Student, Grade> param) {
				if (param.getValue().getGrade() != null) {
					return new SimpleStringProperty(param.getValue().getGrade()
							.getName());
				}
				return new SimpleStringProperty("");
			}

			public Object call(Object param) {
				return call((CellDataFeatures<Student, Grade>) (param));
			}
		});

		mainTable.getColumns().add(gradeCol);
	}

	/**
	 * Adds the far-left student columns to the gradebook
	 */
	@SuppressWarnings("unchecked")
	private void studentColumns() {
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
		mainTable.getColumns().addAll(idCol, totalGradeCol);
	}

	/**
	 * Checks if a column exists with the given name
	 * 
	 * @param name
	 *            the name being checked
	 * @return boolean true if there is already a column in the gradebook with
	 *         that name
	 */
	private boolean columnExists(String name) {
		for (TableColumn<?, ?> col : mainTable.getColumns()) {
			if (checkChildren(col, name))
				return true;
		}
		return false;
	}

	/**
	 * Sets which columns are visible
	 * 
	 * @param asgnNames
	 *            the names of the assignments that will now be visible
	 */
	void setExpanded(ArrayList<String> asgnNames) {
		expanded = asgnNames;
	}

	/**
	 * Gets which assignments are currently visible in the gradebook
	 */
	ArrayList<String> getExpanded() {
		return expanded;
	}

	/**
	 * Supresses unnecessary warnings
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	/**
	 * Forces a refresh of the gradebook GUI to ensure new
	 * students, assignments, or scores will be accurately
	 * visible at all times.
	 */
	void refresh() {
		if (getRoster() != null) {
			int endNdx = mainTable.getColumns().size() - 2;
			mainTable.getColumns().remove(endNdx, endNdx + 2);
			totalGradeCol
					.setCellValueFactory(new PropertyValueFactory<Student, Double>(
							"totalScore"));
			for (GradedItem item : getRoster().getAssignments()) {
				if (!columnExists(item.name())
						&& (item.getParent() == null || expanded.contains(item
								.getParent().name()))) {
					TableColumn<Student, String> newColumn = new TableColumn<Student, String>(
							item.name());
					newColumn.setMinWidth(100);
					newColumn.setEditable(item.hasChildren());

					newColumn.setCellValueFactory(new Callback() {
						public SimpleStringProperty call(
								CellDataFeatures<Student, String> param) {
							if (param.getValue().getAssignmentScore(
									newColumn.getText()) == null) {
								return new SimpleStringProperty("");
							}
							return new SimpleStringProperty(param.getValue()
									.getAssignmentScore(newColumn.getText())
									+ "");
						}

						public Object call(Object param) {
							return call((CellDataFeatures<Student, String>) (param));
						}
					});
					newColumn.setCellFactory(TextFieldTableCell
							.<Student> forTableColumn());
					/* When a user types a change */
					newColumn.setOnEditCommit(new CellEditEventHandler(this));

					if (!item.hasParent()) {
						mainTable.getColumns().add(newColumn);
					} else {
						addSubColumn(item, newColumn);
					}
				}
			}

			endColumns();

			mainTable.setItems(getRoster().getStudentList());
			// force hard refresh
			mainTable.getColumns().add(0, new TableColumn<Student, String>());
			mainTable.getColumns().remove(0);
		}
	}

	ObservableList<TableColumn<Student, ?>> getTopLevelColumns() {
		return mainTable.getColumns();
	}

	/**
	 * Expands or collapses a column
	 * 
	 * @param asgnName
	 *            The name of the column
	 * @param expand
	 *            Whether to expand or collapse (true expands)
	 */
	void setAssignmentExpansion(String asgnName, boolean expand) {
		if (expand) {
			open(asgnName);
			// fullRefresh();
		}
		if (!expand) {
			close(asgnName);
			// fullRefresh();
		}
	}

	/**
	 * Recursively expands a column and its children
	 * 
	 * @param asgnName
	 */
	private void open(String asgnName) {
		if (!expanded.contains(asgnName)) {
			expanded.add(asgnName);
			GradedItem item = getRoster().getAssignment(asgnName);
			for (GradedItem child : item.getChildren()) {
				open(child.name());
			}
		}
	}

	/**
	 * Recursively collapses a column an its children
	 * 
	 * @param asgnName
	 *            The name of the column
	 */
	private void close(String asgnName) {
		if (expanded.contains(asgnName)) {
			expanded.remove(asgnName);
			GradedItem item = getRoster().getAssignment(asgnName);
			for (GradedItem child : item.getChildren()) {
				close(child.name());
			}
		}
	}

	/**
	 * Completely refreshes the gradebook
	 */
	public void fullRefresh() {
		if (mainTable.getColumns().size() > 0) {
			for (int i = mainTable.getColumns().size() - 1; i > 0; i--) {
				mainTable.getColumns().remove(i);
			}
		}
		studentColumns();
		endColumns();
		refresh();
	}

	/**
	 * Supresses unnecessary warnings
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	/**
	 * Adds a sub-column to the gradebook
	 * @param item The new GradedItem
	 * @param newColumn the new Column
	 */
	private void addSubColumn(GradedItem item, TableColumn newColumn) {
		TableColumn parentCol = findColumn(item.getParent().name());
		parentCol.getColumns().add(newColumn);
	}

	/**
	 * Finds a column with the given name. If no such column exists, returns
	 * null
	 * 
	 * @param name
	 *            The desired name
	 * @return TableColumn the colummn if it exists, or null if it does not.
	 */
	private TableColumn<?, ?> findColumn(String name) {
		for (TableColumn<?, ?> topColumn : mainTable.getColumns()) {
			if (checkChildren(topColumn, name)) {
				return findSubColumn(topColumn, name);
			}
		}
		return null;
	}

	/**
	 * Finds a column with the given name under the given column. If no such
	 * column exists, returns null
	 * 
	 * @param name
	 *            The desired name
	 * @return TableColumn the colummn if it exists, or null if it does not.
	 */
	private TableColumn<?, ?> findSubColumn(TableColumn<?, ?> col, String name) {
		if (col.getText().equals(name)) {
			return col;
		}
		for (TableColumn<?, ?> sub : col.getColumns()) {
			TableColumn<?, ?> temp = findSubColumn(sub, name);
			if (temp != null)
				return temp;
		}
		return null;
	}

	/**
	 * Checks if a column exists with the given name as a child of a column.
	 * Checks recursively.
	 * 
	 * @param col
	 *            The column being checked
	 * @param name
	 *            The name beign searched for
	 * @return boolean true if a column exists with the given name in any child
	 *         column
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

	/**
	 * Gets the roster being shown
	 */
	private Roster getRoster() {
		if (current) {
			return Grader.getRoster();
		}
		return roster;
	}

	public void showRoster(Roster roster) {
		current = false;
		this.roster = roster;
	}
}
