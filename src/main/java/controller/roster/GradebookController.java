package controller.roster;
 
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import controller.Alert;
import controller.GraderPopup;
import model.administration.PermissionKeys;
import model.curve.Grade;
import model.driver.Debug;
import model.driver.Grader;
import model.roster.GradedItem;
import model.roster.PredictionMath;
import model.roster.Roster;
import model.roster.Student;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
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
	/** The stat table **/
	@SuppressWarnings("rawtypes")
	@FXML
	private TableView stats_table;
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
	/** The vbox that holds the expand tree **/
	@FXML
	private VBox expander;
	/** The split pane holdign the gradebook and the tree **/
	@FXML
	private SplitPane splitPane;
	/** The expand/collapse tree **/
	@FXML
	private TreeView<String> tree;

	/** An ArrayList holding the names of the columns that are expanded **/
	private ArrayList<String> expanded;
	/** The instance of the controller **/
	private static GradebookController singleton;
	/** Like singleton, but one better. (used in history controller) **/
	private static GradebookController singletonTwo;
	/** checks if accessing the current roster **/
	private boolean current = true;
	/** The roster, if not current **/
	private Roster roster = null;
   /** Whether or not the gradebook is in prediction mode **/
    public static boolean predictionMode = false;
    /** whether or not changes have been made **/
    public static boolean edited;
    /** The width of a column **/
	public static final int COLUMN_WIDTH = 125;

	/** Gets the history gradebook **/
	public static GradebookController getControllerTwo() {
		return singletonTwo;
	}
	
	/**
	 * Gets the gradebook table
	 */
	@SuppressWarnings("rawtypes")
	public TableView getMainTable() {
	    return mainTable;
	}
	
	/**
	 * Gets the stats table
	 */
	@SuppressWarnings("rawtypes")
	public TableView getStatsTable() {
	    return stats_table;
	}

	/**
	 * Initializes the gradebook view
	 */
	@FXML
	public void initialize() {
		Debug.log("GradebookController", "controller init");

		if (singleton == null) {
			singleton = this;
		}
		singletonTwo = this;

		mainTable.setEditable(true);
		expanded = new ArrayList<String>();
		for (String item : getRoster().getAssignmentNameList()) {
			setAssignmentExpansion(item, true);
		}
		fullRefresh();

		mainTable.addEventFilter(MouseEvent.MOUSE_PRESSED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (event.isSecondaryButtonDown()) {
							makeContextMenu().show(
									mainTable.getScene().getWindow(),
									event.getScreenX(), event.getScreenY());
							event.consume();
						}
					}
				});
		
		tree.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				handleTreeEvent();
			}
		});
		populateTree();
	} 

	/**
	 * Makes the right-click menu
	 */
	private ContextMenu makeContextMenu() {
		ContextMenu rightClickMenu = new ContextMenu();
		MenuItem predictionToggle = new MenuItem("Toggle Prediction Mode");
		MenuItem expandCollapse = new MenuItem("Expand/Collapse Columns");
		expandCollapse.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                expander.setMaxWidth(250);
                expander.setMinWidth(250);
            }
        });
		predictionToggle.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				predictionMode = !predictionMode;
				if(predictionMode) {
					Roster.saveTemp(Grader.getRoster());
				} else {
					Roster rost = Roster.load("Rosters/" + Roster.TEMP_NAME + ".rost");
					Grader.setCurrentRoster(rost);
				}
				GradebookController.get().fullRefresh();
			}
		});
		// students don't get these options
		if (current
				&& Grader.getUser().getPermissions()
						.contains(PermissionKeys.EDIT_GRADEBOOK)) {
			MenuItem addAssignment = new MenuItem("Add Assignment");
			MenuItem dropAssignment = new MenuItem("Drop Assignment");
			MenuItem addStudent = new MenuItem("Add Student");
			MenuItem dropStudent = new MenuItem("Drop Student");
			MenuItem ref = new MenuItem("Refresh");
			
			ref.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					fullRefresh();
				}
			});
			
			addAssignment.setOnAction(GraderPopup.getPopupHandler("addAssignmentDialog", addAssignment));
			dropAssignment.setOnAction(GraderPopup.getPopupHandler("dropAssignment", dropAssignment));
			addStudent.setOnAction(GraderPopup.getPopupHandler("AddStudent",	addStudent));
			dropStudent.setOnAction(GraderPopup.getPopupHandler("DropStudent", dropStudent));

			rightClickMenu.getItems().addAll(ref,
					new SeparatorMenuItem(), addAssignment, dropAssignment,
					new SeparatorMenuItem(), addStudent, dropStudent, new SeparatorMenuItem());
		}
		rightClickMenu.getItems().addAll(predictionToggle, expandCollapse);
		return rightClickMenu;
	}
	
	/**
	 * Collapses the expand/collapse tree (hides it)
	 */
	public void collapseTree() {
        expander.setMaxWidth(0);
        expander.setMinWidth(0);
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
		percentCol.setMinWidth(COLUMN_WIDTH);
		percentCol
				.setCellValueFactory(new PropertyValueFactory<Student, Double>(
						"totalPercentage"));
		mainTable.getColumns().add(percentCol);
		TableColumn<Student, String> gradeCol = new TableColumn<Student, String>(
				"Grade");
		gradeCol.setMinWidth(COLUMN_WIDTH);
		gradeCol.setEditable(predictionMode);
		gradeCol.setCellValueFactory(new Callback() {
			public SimpleStringProperty call(
					CellDataFeatures<Student, String> param) {
				return new SimpleStringProperty(param.getValue()
						.getGrade().getName()
						+ "");
			}
			public Object call(Object param) {
				return call((CellDataFeatures<Student, String>) (param));
			}
		});
		if(!predictionMode) {
			gradeCol.setCellFactory(getGradeColumnCellFactory());
		} else {
			gradeCol.setCellFactory(TextFieldTableCell
				.<Student> forTableColumn());
		}
		gradeCol.setOnEditCommit(getGradeColumnEditHandler());
		mainTable.getColumns().add(gradeCol);
	}

	/**
	 * Creates the event handler that handles the editing of final grades
	 * in prediction mode
	 */
	private EventHandler<CellEditEvent<Student, String>> getGradeColumnEditHandler() {
		return new EventHandler<TableColumn.CellEditEvent<Student,String>>() {
			public void handle(CellEditEvent<Student, String> event) {
				if(Grader.getCurve().getGrade(event.getNewValue()) != null) {
					 Roster rost = Roster.load("Rosters/" + Roster.TEMP_NAME + ".rost");
					 Grader.setCurrentRoster(rost);
					 HashMap<GradedItem, Double> map = PredictionMath.getPrediction(
							 mainTable.getSelectionModel().getSelectedItem(),
							 Grader.getRoster().getCurve().getGrade(event.getNewValue()));
					 if(map != null) {
						 for(GradedItem item : map.keySet()) {
							 Grader.addScore(event.getRowValue(), item.name(),
										map.get(item));
						 }
					 } else {
						 showImpossibleGradeWarning(mainTable.getSelectionModel().getSelectedItem(),
								 event.getNewValue());
					 }
				}
				fullRefresh();
			}
		};
	}

	/**
	 * Creates the cell factory that colors the cells in the grade column
	 * based on their grade (A's are green, B's are blue, etc.)
	 */
	private Callback<TableColumn<Student, String>, TableCell<Student, String>> getGradeColumnCellFactory() {
		return new Callback<TableColumn<Student, String>, TableCell<Student, String>>() {
			@Override
			public TableCell<Student, String> call(
					TableColumn<Student, String> param) {
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
		};
	}

	/**
	 * Informs the user that no prediction can be made for a requested grade
	 */
	private void showImpossibleGradeWarning(Student student, String attempted) {
		ArrayList<Grade> list =  Grader.getCurve().getGrades();
		Collections.reverse(list);
		for(Grade grade : list) {
			HashMap<GradedItem, Double> map = PredictionMath.getPrediction(student, grade);
			if(map != null) {
				Alert.showWarningMessage("Impossible Grade", 
						student.getName() + " cannot receive a " + attempted + ". The highest they can" + 
						" receive is a " + grade.getName());
				return;
			}
		}
	}
	
	/**
	 * Adds the far-left student columns to the gradebook
	 */
	@SuppressWarnings("unchecked")
	private void studentColumns() {
		nameCol.setMinWidth(COLUMN_WIDTH);
		nameCol.setEditable(false);
		nameCol.setCellValueFactory(new PropertyValueFactory<Student, String>(
				"name"));

		idCol.setMinWidth(COLUMN_WIDTH);
		idCol.setEditable(false);
		idCol.setCellValueFactory(new PropertyValueFactory<Student, String>(
				"id"));

		totalGradeCol.setEditable(false);
		totalGradeCol.setMinWidth(COLUMN_WIDTH);
		totalGradeCol
				.setCellValueFactory(new PropertyValueFactory<Student, Double>(
						"totalScore"));
		mainTable.getColumns().addAll(idCol, totalGradeCol);
	}

	/**
	 * Checks if a column exists with the given name
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
					TableColumn<Student, String> newColumn = new TableColumn<Student, String>();
					Label label = new Label(item.name());
					label.setTooltip(new Tooltip("Max Score: " + item.maxScore()));
					newColumn.setGraphic(label);
					
					newColumn.setMinWidth(COLUMN_WIDTH);
					newColumn.setEditable(item.isLeaf());

					newColumn = setAssignmentCellFactories(newColumn);
					
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
			
			populateStatsTable();
		}
	}
	
	/**
	 * Sets the cell factories for a tablecolumn and returns
	 * the updated column
	 */
	@SuppressWarnings("unchecked")
	private TableColumn<Student, String> setAssignmentCellFactories(
			TableColumn<Student, String> newColumn) {
		newColumn.setCellValueFactory(new Callback() {
			public SimpleStringProperty call(
					CellDataFeatures<Student, String> param) {
				if (param.getValue().getAssignmentScore(
						getColumnTitle(newColumn)) == null) {
					return new SimpleStringProperty("");
				}
				return new SimpleStringProperty(param.getValue()
						.getAssignmentScore(getColumnTitle(newColumn))
						+ "");
			}

			public Object call(Object param) {
				return call((CellDataFeatures<Student, String>) (param));
			}
		});
		newColumn.setCellFactory(TextFieldTableCell
				.<Student> forTableColumn());
		return newColumn;
	}

	/**
	 * Gets the column title from it's graphic object
	 */
	@SuppressWarnings("rawtypes")
	private String getColumnTitle(TableColumn column) {
		if(column.getGraphic() != null) {
			return ((Label)(column.getGraphic())).getText();
		}
		return column.getText();
	}
	
	/**
	 * Populate the stats table
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
    public void populateStatsTable() {
	    Debug.log("GradebookController", "Populating Stats Table");
	    TableColumn targetColumn;
	    TableColumn<AggregateInfo, String> newColumn, titleColumn, 
	    	bufferColumn2, bufferColumn3, bufferColumn4;
	    ObservableList<AggregateInfo> data;
	    stats_table.getColumns().clear();
	    
	    titleColumn = new TableColumn<AggregateInfo, String>("");
	    bufferColumn2 = new TableColumn("");
	    bufferColumn3 = new TableColumn("");
	    bufferColumn4 = new TableColumn("");
	    
	    titleColumn.setCellValueFactory(new Callback() {
            public SimpleStringProperty call(CellDataFeatures<AggregateInfo, String> param) {
                return new SimpleStringProperty(param.getValue().getTitle());
            }
            public Object call(Object param) {
                return call((CellDataFeatures<AggregateInfo, String>) (param));
            }
        });
	    
	    titleColumn.setMinWidth(COLUMN_WIDTH);
	    bufferColumn2.setMinWidth(COLUMN_WIDTH);
	    bufferColumn3.setMinWidth(COLUMN_WIDTH);
	    bufferColumn4.setMinWidth(COLUMN_WIDTH);
	    
	    stats_table.getColumns().add(titleColumn);
	    stats_table.getColumns().add(bufferColumn2);
	    stats_table.getColumns().add(bufferColumn3);
	    
	    for (int index = 3; index < mainTable.getColumns().size() - 1; index++) {
	        targetColumn = mainTable.getColumns().get(index);
	        dfsAddCollumns(targetColumn);
	    }
	    stats_table.getColumns().add(bufferColumn4);
	    stats_table.getItems().clear();
	    data = FXCollections.observableArrayList(
	            getPointValues(), getDataPoints(), 
	            getRanges(), getMeans(),
	            getDeviations(), getMedians(),
	            getMax(), getMin());
	    stats_table.setItems(data);
	}
	
	/**
	 * Gets the point values for the assignments (for the stats table)
	 */
	private AggregateInfo getPointValues() {
	    AggregateInfo pointValues;
	    pointValues = new AggregateInfo("Maximum Point Value");
        
        for (GradedItem item : Grader.getAssignmentList()) {
            pointValues.addCell(item.name(), item.maxScore());
        }
        pointValues.addCell("Percentage", Grader.getMaxPoints());
        return pointValues;
	}
	
	/**
	 * Gets the data points for the assignments (for the stats table)
	 */
	private AggregateInfo getDataPoints() {
	    AggregateInfo dataPoints;
	    dataPoints = new AggregateInfo("# of Data Points");
	    
	    for (GradedItem item : Grader.getAssignmentList()) {
	        dataPoints.addCell(item.name(), item.getNumGraded());
	    }
	    dataPoints.addCell("Percentage", Grader.getStudentList().size());
	    
	    return dataPoints;
	}
	
	/**
	 * Gets the ranges for the assignments (for the stats table)
	 */
	private AggregateInfo getRanges() {
        AggregateInfo range;
        range = new AggregateInfo("Range");
        
        for (GradedItem item : Grader.getAssignmentList()) {
            range.addCell(item.name(), item.getMax() - item.getMin());
        }
        
        range.addCell("Percentage", "Need Method");
        
        return range;
    }
	
	/**
	 * Gets the means for the assignments (for the stats table)
	 */
	private AggregateInfo getMeans() {
        AggregateInfo mean;
        mean = new AggregateInfo("Mean");
        
        for (GradedItem item : Grader.getAssignmentList()) {
            mean.addCell(item.name(), item.getMean());
        }
        
        mean.addCell("Percentage", Grader.getRoster().getPercentAverage());
        
        return mean;
    }
	
	/**
	 * Gets the medians for the assignments (for the stats table)
	 */
	private AggregateInfo getMedians() {
        AggregateInfo median;
        median = new AggregateInfo("Median");
        
        for (GradedItem item : Grader.getAssignmentList()) {
            median.addCell(item.name(), item.getMedian());
        }
        
        median.addCell("Percentage", "Need Method");
        
        return median;
    }
	
	/**
	 * Gets the standard deviations for the assignments (for the stats table)
	 */
	private AggregateInfo getDeviations() {
        AggregateInfo standardDeviation;
        standardDeviation = new AggregateInfo("Standard Deviation");
        
        for (GradedItem item : Grader.getAssignmentList()) {
            standardDeviation.addCell(item.name(), item.getStandardDeviation()); 
        }
        
        standardDeviation.addCell("Percentage", "Need Method");
        
        return standardDeviation;
    }
	
	/**
	 * Gets the min values for the assignments (for the stats table)
	 */
	private AggregateInfo getMin() {
        AggregateInfo mins;
        mins = new AggregateInfo("Minimum Score");
        
        for (GradedItem item : Grader.getAssignmentList()) {
            mins.addCell(item.name(), item.getMin()); 
        }
        
        mins.addCell("Percentage", "Need Method");
        
        return mins;
    }
	
	/**
	 * Gets the max values for the assignments (for the stats table)
	 */
	private AggregateInfo getMax() {
        AggregateInfo max;
        max = new AggregateInfo("Maximum Score");
        
        for (GradedItem item : Grader.getAssignmentList()) {
            max.addCell(item.name(), item.getMax()); 
        }
        
        max.addCell("Percentage", "Need Method");
        
        return max;
    }
	
	/**
	 * Adds the assignment columns to the stats table
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
    private void dfsAddCollumns(TableColumn targetColumn) {
	    TableColumn<AggregateInfo, String> newColumn;
	    
	    //leaf
	    if (targetColumn.getColumns().isEmpty()) {
	        newColumn = new TableColumn<AggregateInfo, String>(getColumnTitle(targetColumn));
            newColumn.setMinWidth(COLUMN_WIDTH);
            
            newColumn.setCellValueFactory(new Callback() {
                public SimpleStringProperty call(CellDataFeatures<AggregateInfo, String> param) {
                    return new SimpleStringProperty(param.getValue().getCell(getColumnTitle(newColumn)));
                }

                public Object call(Object param) {
                    return call((CellDataFeatures<AggregateInfo, String>) (param));
                }
            });
            
            stats_table.getColumns().add(newColumn);
	        return;
	    }
	    
	    else {
	        for (TableColumn child : ((ObservableList<TableColumn>)targetColumn.getColumns())) {
	            dfsAddCollumns(child);
	        }
	    }
	}
	ObservableList<TableColumn<Student, ?>> getTopLevelColumns() {
		return mainTable.getColumns();
	}

	/**
	 * Expands or collapses a column with the given name
	 */
	void setAssignmentExpansion(String asgnName, boolean expand) {
		if (expand) {
			open(asgnName);
		} else {
			close(asgnName);
		}
	}

	/**
	 * Recursively expands a column and its children
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
		populateTree();
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
		if (getColumnTitle(col).equals(name)) {
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
	 */
	private boolean checkChildren(TableColumn<?, ?> col, String name) {
		if (getColumnTitle(col).equals(name)) {
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
		Debug.log("GradebookController", "returning history roster");
		return roster;
	}

	/**
	 * Sets the currently shown roster
	 */
	public void showRoster(Roster roster) {
		current = false;
		this.roster = roster;
	}

	/**
	 * Gets the singleton gradebook controller
	 */
	public static GradebookController get() {
		return singleton;
	}
	
	/**
	 * Populates the expand/collapse tree to match the assignments
	 */
	void populateTree() {
		TreeItem<String> root = new TreeItem<String>("All Columns");
		root.setExpanded(true);

		for (TableColumn<?, ?> topColumn : getTopLevelColumns()) {
			TreeItem<String> item = makeTreeItem(getColumnTitle(topColumn));
			item.setExpanded(getExpanded().contains(getColumnTitle(topColumn)));
			root.getChildren().add(item);
		}
		tree.setRoot(root);
	}
	
	/**
	 * Creates an item (representing an assignment) for
	 * the expand/collapse tree
	 */
	private TreeItem<String> makeTreeItem(String name) {
		GradedItem temp = Grader.getAssignment(name);
		TreeItem<String> item = new TreeItem<String>(name);
		if (temp != null) {
			for (GradedItem child : temp.getChildren()) {
				TreeItem<String> childItem = makeTreeItem(child.name());
				childItem.setExpanded(getExpanded().contains(
						child.name()));
				item.getChildren().add(childItem);
			}
		}
		return item;
	}
	
	/**
	 * Handles the user expanding or collapsing a column
	 */
	private void handleTreeEvent() {
		for (TreeItem<String> item : tree.getRoot().getChildren()) {
			mirrorInGradebook(item);
		}
		fullRefresh();
	}
	
	/**
	 * Expands or collapses a column in the gradebook to reflect
	 * changes in the tree
	 */
	private void mirrorInGradebook(TreeItem<String> item) {
		setAssignmentExpansion(item.getValue(), item.isExpanded());
		if (item.isExpanded()) {
			for (TreeItem<String> child : item.getChildren()) {
				mirrorInGradebook(child);
			}
		}
	}
	
	/**
	 * Makes the gradebook non-editable
	 */
	public void deactivateCellEditing() {
	    mainTable.setEditable(false);
	}
}
