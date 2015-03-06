package resources;

import java.awt.Color;

import javafx.scene.image.Image;

/**
 * Standardizes coloring and resource loading
 * 
 * @author Gavin Scott 
 * @author Shelli Crispen
 */
public class ResourceLoader {
	public final static Color GREEN = Color.decode("#84F379");
	public final static Color BLUE = Color.decode("#74D7E5");
	public final static Color YELLOW = Color.decode("#FFFD7F");
	public final static Color ORANGE = Color.decode("#FFBC7F");
	public final static Color RED = Color.decode("#FE7E84");
	
	public final static javafx.scene.paint.Color ERROR_RED = javafx.scene.paint.Color.web("#FE7E84");
	public final static javafx.scene.paint.Color NOERROR_WHITE = javafx.scene.paint.Color.WHITE;
	//Line color and bar color
	//back ground color for the slidy thingy 
	public final static javafx.scene.paint.Color LINE_COLOR = javafx.scene.paint.Color.web("#03434B");
    public final static javafx.scene.paint.Color BAR_COLOR = javafx.scene.paint.Color.web("#BC7BE9");
    public final static javafx.scene.paint.Color BACKGROUND = javafx.scene.paint.Color.web("#CBEFF4");

	
	public final static Image ICON = new Image(ResourceLoader.class.getResourceAsStream( "GraderIcon.png" ));
	public final static Image LOAD_SCREEN = new Image(ResourceLoader.class.getResourceAsStream( "GraderLoadScreen.jpg" ));

}
