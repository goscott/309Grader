package controller.roster;

import java.io.IOException;
import java.util.ArrayList;

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
		ArrayList<String> children = new ArrayList<String>();

		for (String name : parent.getExpanded()) {
			if (!children.contains(name)) {
				GradedItem temp = Grader.getAssignment(name);
				TreeItem<String> item = new TreeItem<String>(name);
				root.getChildren().add(item);
				for(GradedItem child : temp.getChildren()) {
					children.add(child.name());
					TreeItem<String> childItem = new TreeItem<String>(child.name());
					item.getChildren().add(childItem);
				}
			}
		}
		tree.setRoot(root);
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

	public void setParent(MenuItem callingItem, GradebookController contr) {
		this.callingItem = callingItem;
		this.parent = contr;
		this.callingItem.setDisable(true);
	}

	@FXML
	private void handleButton(ActionEvent e) {
		refreshButton.setDisable(true);
		if (parent != null)
			parent.fullRefresh();
	}
}