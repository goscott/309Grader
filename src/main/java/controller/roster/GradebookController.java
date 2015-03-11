package controller.roster;
 
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.javafx.scene.control.skin.VirtualFlow;

import controller.GraderPopup;
import controller.graph.Histogram;
import model.administration.PermissionKeys;
import model.announcements.Announcement;
import model.curve.Grade;
import model.driver.Debug;
import model.driver.Grader;
import model.roster.GradedItem;
import model.roster.PredictionMath;
import model.roster.Roster;
import model.roster.Student;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
	@FXML
	private VBox expander;
	@FXML
	private SplitPane splitPane;
	@FXML
	private TreeView<String> tree;

	/** The right-click menu **/
	// private ContextMenu rightClickMenu;
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
	public static final int COLUMN_WIDTH = 125;

	public static GradebookController getController() {
		return singleton;
	}

	public static GradebookController getControllerTwo() {
		return singletonTwo;
	}
	
	public TableView getMainTable() {
	    return mainTable;
	}
	
	public TableView getStatsTable() {
	    return stats_table;
	}

	@FXML
	/**
	 * Initializes the gradebook view
	 */
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
		
		/*
		mainTable.getColumns().addListener(new ListChangeListener<TableColumn<?,?>>() {  
		     @Override  
		     public void onChanged(Change<? extends TableColumn<?,?>> change) {  
		          // table columns changed, perhaps due to reordering...  
		         populateStatsTable();
		     }  
		}); */
	} 

	/**
	 * Makes the right-click menu
	 */
	private ContextMenu makeContextMenu() {
		ContextMenu rightClickMenu = new ContextMenu();
		MenuItem predictionToggle = new MenuItem("Toggle Prediction Mode");
		MenuItem expandCollapse = new MenuItem("Expand/Collapse Columns");
		//expandCollapse.setOnAction(GraderPopup.getPopupHandler("expandCollapseDialog", expandCollapse));
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
					GradebookController.get().fullRefresh();
				} else {
					Roster rost = Roster.load("Rosters/" + Roster.TEMP_NAME + ".rost");
					Grader.setCurrentRoster(rost);
				}
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
			MenuItem makePrediction = new MenuItem("Make Prediction");
			
			ref.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					fullRefresh();
				}
			});
			
			makePrediction.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    PredictionMath.getPrediction(Grader.getRoster(), mainTable.getSelectionModel().getSelectedItem(), Grader.getRoster().getCurve().getGrade("A"));
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
			gradeCol.setCellFactory(new Callback<TableColumn<Student, String>, TableCell<Student, String>>() {
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
			});
		} else {
			gradeCol.setCellFactory(TextFieldTableCell
				.<Student> forTableColumn());
		}
		/* When a user types a change */
		//newColumn.setOnEditCommit(new CellEditEventHandler(this));
		gradeCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student,String>>() {
			public void handle(CellEditEvent<Student, String> event) {
				if(Grader.getCurve().getGrade(event.getNewValue()) != null) {
					 Roster rost = Roster.load("Rosters/" + Roster.TEMP_NAME + ".rost");
					 Grader.setCurrentRoster(rost);
					 HashMap<GradedItem, Double> map = PredictionMath.getPrediction(Grader.getRoster(),
							 mainTable.getSelectionModel().getSelectedItem(),
							 Grader.getRoster().getCurve().getGrade(event.getNewValue()));
					 for(GradedItem item : map.keySet()) {
						 Grader.addScore(event.getRowValue(), item.name(),
									map.get(item));
					 }
				}
				fullRefresh();
			}
		});

		mainTable.getColumns().add(gradeCol);
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
					newColumn.setMinWidth(COLUMN_WIDTH);
					newColumn.setEditable(item.isLeaf());

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
			
			populateStatsTable();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public void populateStatsTable() {
	    Debug.log("GradebookController", "Populating Stats Table");
	    TableColumn targetColumn;
	    TableColumn<AggregateInfo, String> newColumn, titleColumn, bufferColumn2, bufferColumn3, bufferColumn4;
	    
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
	            getPointValues(),
	            getDataPoints(), 
	            getRanges(),
	            getMeans(),
	            getDeviations(),
	            getMedians(),
	            getMax(),
	            getMin());
	    stats_table.setItems(data);
	}
	
	private AggregateInfo getPointValues() {
	    AggregateInfo pointValues;
	    
	    pointValues = new AggregateInfo("Maximum Point Value");
        
        for (GradedItem item : Grader.getAssignmentList()) {
            pointValues.addCell(item.name(), item.maxScore());
        }
        
        pointValues.addCell("Percentage", Grader.getMaxPoints());
        
        return pointValues;
	}
	
	private AggregateInfo getDataPoints() {
	    AggregateInfo dataPoints;
	    dataPoints = new AggregateInfo("# of Data Points");
	    
	    for (GradedItem item : Grader.getAssignmentList()) {
	        dataPoints.addCell(item.name(), item.getNumGraded());
	    }
	    
	    dataPoints.addCell("Percentage", Grader.getStudentList().size());
	    
	    return dataPoints;
	}
	
	private AggregateInfo getRanges() {
        AggregateInfo range;
        range = new AggregateInfo("Range");
        
        for (GradedItem item : Grader.getAssignmentList()) {
            range.addCell(item.name(), item.getMax() - item.getMin());
        }
        
        range.addCell("Percentage", "Need Method");
        
        return range;
    }
	
	private AggregateInfo getMeans() {
        AggregateInfo mean;
        mean = new AggregateInfo("Mean");
        
        for (GradedItem item : Grader.getAssignmentList()) {
            mean.addCell(item.name(), item.getMean());
        }
        
        mean.addCell("Percentage", Grader.getRoster().getPercentAverage());
        
        return mean;
    }
	
	private AggregateInfo getMedians() {
        AggregateInfo median;
        median = new AggregateInfo("Median");
        
        for (GradedItem item : Grader.getAssignmentList()) {
            median.addCell(item.name(), item.getMedian());
        }
        
        median.addCell("Percentage", "Need Method");
        
        return median;
    }
	
	private AggregateInfo getDeviations() {
        AggregateInfo standardDeviation;
        standardDeviation = new AggregateInfo("Standard Deviation");
        
        for (GradedItem item : Grader.getAssignmentList()) {
            standardDeviation.addCell(item.name(), item.getStandardDeviation()); 
        }
        
        standardDeviation.addCell("Percentage", "Need Method");
        
        return standardDeviation;
    }
	
	private AggregateInfo getMin() {
        AggregateInfo mins;
        mins = new AggregateInfo("Minimum Score");
        
        for (GradedItem item : Grader.getAssignmentList()) {
            mins.addCell(item.name(), item.getMin()); 
        }
        
        mins.addCell("Percentage", "Need Method");
        
        return mins;
    }
	
	private AggregateInfo getMax() {
        AggregateInfo max;
        max = new AggregateInfo("Maximum Score");
        
        for (GradedItem item : Grader.getAssignmentList()) {
            max.addCell(item.name(), item.getMax()); 
        }
        
        max.addCell("Percentage", "Need Method");
        
        return max;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    private void dfsAddCollumns(TableColumn targetColumn) {
	    TableColumn<AggregateInfo, String> newColumn;
	    
	    //leaf
	    if (targetColumn.getColumns().isEmpty()) {
	        newColumn = new TableColumn<AggregateInfo, String>(targetColumn.getText());
            newColumn.setMinWidth(COLUMN_WIDTH);
            
            newColumn.setCellValueFactory(new Callback() {
                public SimpleStringProperty call(CellDataFeatures<AggregateInfo, String> param) {
                    return new SimpleStringProperty(param.getValue().getCell(newColumn.getText()));
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
		} else {
			close(asgnName);
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

	public static GradebookController get() {
		return singleton;
	}
	
	void populateTree() {
		TreeItem<String> root = new TreeItem<String>("All Columns");
		root.setExpanded(true);

		for (TableColumn<?, ?> topColumn : getTopLevelColumns()) {
			TreeItem<String> item = makeTreeItem(topColumn.getText());
			item.setExpanded(getExpanded().contains(topColumn.getText()));
			root.getChildren().add(item);
		}
		tree.setRoot(root);
	}
	
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
	
	private void handleTreeEvent() {
		for (TreeItem<String> item : tree.getRoot().getChildren()) {
			mirrorInGradebook(item);
		}
		fullRefresh();
	}
	
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
