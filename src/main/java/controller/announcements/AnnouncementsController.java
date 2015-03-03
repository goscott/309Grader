package controller.announcements;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.announcements.Announcement;
import model.driver.Grader;

/**
 * 
 * @author Jacob
 *
 */
public class AnnouncementsController {
    
    @FXML
    private TableView<Announcement> table; // This is how you reference your fxml objects
    
    @FXML
    private TableColumn<Announcement, String> subject_column;
    
    @FXML
    private TableColumn<Announcement, String> posted_by_column;
    
    @FXML
    private TableColumn<Announcement, String> date_time_column;
    
    @FXML
    private Button add_announcent;
    
    private ObservableList<Announcement> data;
    
    private static AnnouncementsController singleton;
    
    /**
     * Automatically called at startup
     */
    public void initialize() {
    	singleton = this;
    	update();
    }
    
    /**
     * Lets other files refresh the tab
     */
    public static void refresh() {
    	singleton.update();
    }
    
    /**
     * Updates the tab
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void update() { //this gets called automatically at startup
            
        // loads the announcements
        data = FXCollections.observableArrayList();
        for(Announcement ann : Grader.getRoster().getAnnouncements()) {
        	data.add(ann);
        }
        
        //These dictate how the columns get the info from the announcement object
        subject_column.setCellValueFactory(new Callback() {
            public SimpleStringProperty call(CellDataFeatures<Announcement, String> param) {
                return new SimpleStringProperty(param.getValue().getSubject()); //Here is where it happens (Note the call on Announcement.getSubject())
            }

            public Object call(Object param) {
                return call((CellDataFeatures<Announcement, String>) (param));
            }
        });
        
        //These dictate how the columns get the info from the announcement object
        posted_by_column.setCellValueFactory(new Callback() {
            public SimpleStringProperty call(CellDataFeatures<Announcement, String> param) {
                return new SimpleStringProperty(param.getValue().getPostedBy()); //Here is where it happens (Note the call on Announcement.getPostedBy())
            }

            public Object call(Object param) {
                return call((CellDataFeatures<Announcement, String>) (param));
            }
        });
        
        //These dictate how the columns get the info from the announcement object
        date_time_column.setCellValueFactory(new Callback() {
            public SimpleStringProperty call(CellDataFeatures<Announcement, String> param) {
                return new SimpleStringProperty(param.getValue().getDateTime()); //Here is where it happens (Note the call on Announcement.getDateTime())
            }

            public Object call(Object param) {
                return call((CellDataFeatures<Announcement, String>) (param));
            }
        });
        
        //This makes something happen when you double click on a row in the table
        //It should launch an announcement viewer
        table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @SuppressWarnings("static-access")
			@Override 
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                	Stage newStage = new Stage();
                	ViewAnnouncementController popup = new ViewAnnouncementController();
            		popup.setAnnouncement(table.getSelectionModel().getSelectedItem());
            		popup.start(newStage);
                }
            }
        });
        
        table.setItems(data);
    }
    
    /**
     * Launches a new announcement dialog, and then adds the announcement to the list.
     */
    public void addAnnouncement() {
    	Stage newStage = new Stage();
    	AddAnnouncementController popup = new AddAnnouncementController();
		popup.setParent(this);
		popup.start(newStage);
    }
    
    /**
     * Adds an announcement to the table.
     * @param announcement the Announcement to be added to the table.
     */
    void addAnnouncementToList(Announcement announcement) {
    	data.add(announcement);
		table.setItems(data);
    }
}
