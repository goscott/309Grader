package resources;

import java.awt.Color;

import javafx.scene.image.Image;

/**
 * Standardizes coloring and resource loading
 * 
 * @author Gavin Scott
 */
public class ResourceLoader {
	public final static Color GREEN = Color.GREEN;
	public final static Color BLUE = Color.CYAN;
	public final static Color YELLOW = Color.YELLOW;
	public final static Color ORANGE = Color.ORANGE;
	public final static Color RED = Color.RED;
	
	public final static javafx.scene.paint.Color ERROR_RED = javafx.scene.paint.Color.RED;
	public final static javafx.scene.paint.Color NOERROR_WHITE = javafx.scene.paint.Color.WHITE;
	
	public final static Image ICON = new Image(ResourceLoader.class.getResourceAsStream( "GraderIcon.png" ));
	public final static Image LOAD_SCREEN = new Image(ResourceLoader.class.getResourceAsStream( "GraderLoadScreen.jpg" ));
}