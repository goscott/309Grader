package controller.roster;

import java.io.IOException;

import controller.GraderPopup;
import model.driver.Debug;
import model.driver.Grader;
import model.roster.GradedItem;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ExpandCollapseController {
	@FXML
	private Button refreshButton;
	@FXML
	private TreeView<String> tree;

	private static GradebookController parent;
	private static MenuItem callingItem;

	public void initialize() {
		refreshButton.setDisable(true);
		tree.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				// populateTree();
				refreshButton.setDisable(false);
			}
		});
		populateTree();
	}

	private void populateTree() {
		TreeItem<String> root = new TreeItem<String>("All Columns");
		root.setExpanded(true);

		for (TableColumn<?, ?> topColumn : parent.getTopLevelColumns()) {
			TreeItem<String> item = makeTreeItem(topColumn.getText());
			item.setExpanded(parent.getExpanded().contains(topColumn.getText()));
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
				childItem.setExpanded(parent.getExpanded().contains(
						child.name()));
				item.getChildren().add(childItem);
			}
		}
		return item;
	}

	public void start(Stage stage) {
		try {
			BorderPane page = (BorderPane) FXMLLoader
					.load(getClass().getResource(
							"../../view/roster/expandCollapseDialog.fxml"));
			Scene popup = new Scene(page);
			stage.setTitle("Expand/Collapse Columns");
			stage.setScene(popup);
			stage.setResizable(false);
			GraderPopup.setIcon(stage);
			stage.show();
		} catch (IOException ex) {
			Debug.log("IO ERROR", "Could not load file to start popup");
			ex.printStackTrace();
		}

		stage.setOnHiding(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				callingItem.setDisable(false);
			}
		});
	}

	public void setParent(MenuItem callItem, GradebookController contr) {
		callingItem = callItem;
		parent = contr;
		callingItem.setDisable(true);
	}

	@FXML
	private void handleButton(ActionEvent e) {
		refreshButton.setDisable(true);
		for (TreeItem<String> item : tree.getRoot().getChildren()) {
			mirrorInGradebook(item);
		}
		if (parent != null)
			parent.fullRefresh();
		
		// TODO Have it refresh so it doesn't have to close
		// closes the window on click
		((Stage) refreshButton.getScene().getWindow()).hide();
	}

	private void mirrorInGradebook(TreeItem<String> item) {
		parent.setAssignmentExpansion(item.getValue(), item.isExpanded());
		if (item.isExpanded()) {
			for (TreeItem<String> child : item.getChildren()) {
				mirrorInGradebook(child);
			}
		}
	}
}