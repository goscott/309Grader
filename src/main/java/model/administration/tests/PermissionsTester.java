package model.administration.tests;

import java.util.logging.Level;
import java.util.logging.Logger;

import model.administration.User;
import model.administration.UserDB;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * 
 * @author Mason Stevenson
 *
 */
public class PermissionsTester extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(PermissionsTester.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage primaryStage) {
        UserDB users = new UserDB();
        
        try {
            //BorderPane page = (BorderPane) FXMLLoader.load(getClass().getResource("PermissionsEditor.fxml"));
            Scene scene = new Scene((Parent) FXMLLoader.load(getClass().getResource("../../../view/administration/permissions_editor.fxml")));
            
            ListView<String> view = (ListView<String>) scene.lookup("#user_list");
            ObservableList<String> list = FXCollections.observableArrayList();
            
            for (User target : users.getUsers()) {
                list.add(target.getId());
            }
            
            view.setItems(list);
            
            primaryStage.setScene(scene);
            primaryStage.setTitle("Edit User Permissions");
            
            primaryStage.show();
            
        } catch (Exception ex) {
            Logger.getLogger(PermissionsTester.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("ERR");
        }
    }
}