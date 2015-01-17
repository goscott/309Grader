package view.mainpage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.administration.User;
import model.administration.UserDB;
import model.administration.UserTypes;
import model.administration.tests.PermissionsTester;
import model.driver.Driver;
import model.driver.Grader;

public class MainPageController {
	@FXML
    private AnchorPane classTab;
	@FXML
    private AnchorPane gradebookTab;
	@FXML
    private AnchorPane graphsTab;
	@FXML
    private AnchorPane historyTab;
	@FXML
    private AnchorPane predictionsTab;
	@FXML
    private AnchorPane announcmentsTab;
	
	public void initialize() {
		// gives it some initial data
		Grader.get();
		//Driver.test();
		
		// loads tab contents
		try {
			BorderPane gradebookPage = (BorderPane) FXMLLoader.load(getClass().getResource("../roster/gradebook_screen.fxml"));
			gradebookTab.getChildren().add(gradebookPage);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    //launches the permissions editor
    public void permissions() {
        System.out.println("Launching permissions editor");
        UserDB users = new UserDB();
        
        try {
            Stage stage = new Stage();
            
            //BorderPane page = (BorderPane) FXMLLoader.load(getClass().getResource("PermissionsEditor.fxml"));
            Scene scene = new Scene((Parent) FXMLLoader.load(getClass().getResource("../administration/permissions_editor.fxml")));
            
            ListView<String> view = (ListView<String>) scene.lookup("#user_list");
            ObservableList<String> list = FXCollections.observableArrayList();
            
            for (User target : users.getUsers()) {
                list.add(String.format("%-40s%51s", target.getId(), UserTypes.fullName(target.getType())));
            }
            
            view.setItems(list);
            
            stage.setScene(scene);
            stage.setTitle("Edit User Permissions");
            stage.setResizable(false);
            
            stage.show();
            
        } catch (Exception ex) {
            Logger.getLogger(PermissionsTester.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("ERR");
        }
    }

}
