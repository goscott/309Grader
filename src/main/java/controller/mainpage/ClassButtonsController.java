package controller.mainpage;

import java.io.File;
import java.io.FilenameFilter;

import model.administration.PermissionKeys;
import model.driver.Debug;
import model.driver.Grader;
import model.roster.Roster;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

public class ClassButtonsController {
	private FlowPane buttonSetUp;
	private static ClassButtonsController singleton;

	public ClassButtonsController(FlowPane buttons) {
		singleton = this;
		buttonSetUp = buttons;
		// buttonSetUp.setTranslateX(25);
		// buttonSetUp.setTranslateY(25);

		buttonSetUp.setVgap(50);
		buttonSetUp.setHgap(50);
		buttonSetUp.setMaxSize(100000, 1000000);
		buttonSetUp.autosize();
		refreshButtons();
	}

	public static void refresh() {
		singleton.refreshButtons();
	}

	public void refreshButtons() {
		buttonSetUp.getChildren().clear();
		File dir = new File("Rosters");
		if (!dir.exists()) {
			dir.mkdir();
		}
		File[] rosters = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				Roster temp = null;
				try {
					temp = Roster.load("Rosters/" + name);
				} catch (Exception ex) {
					Debug.log("ERROR", "Roster could not load");
				}
				return temp != null
						&& temp.current()
						&& name.endsWith(".rost")
						&& (temp.getInstructorId().equals(
								Grader.getUser().getId()) || temp
								.getStudentByID(Grader.getUser().getId()) != null)
						&& !temp.courseName().equals(Debug.DEFAULT_NAME)
						&& !name.contains(Roster.TEMP_NAME);
			}

		});
		Button button;

		for (int i = 0; i < rosters.length; i++) {
			String name = rosters[i].getName();
			name = name.substring(0, name.length() - 5);
			button = new Button(name);
			button.setOnAction(new ClassButtonEventHandler());
			buttonSetUp.getChildren().add(button);
		}

		if (Grader.getUser().getPermissions()
				.contains(PermissionKeys.ADD_CLASS)) {
			button = new Button("Add Class");
			button.setOnAction(new AddClassButtonEventHandler(this));
			buttonSetUp.getChildren().add(button);
		}

	}
}
