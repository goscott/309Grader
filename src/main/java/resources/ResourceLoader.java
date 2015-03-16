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
	/** The default green used by the program **/
	public final static Color GREEN = Color.decode("#84F379");
	/** The default blue used by the program **/
	public final static Color BLUE = Color.decode("#74D7E5");
	/** The default yellow used by the program **/
	public final static Color YELLOW = Color.decode("#FFFD7F");
	/** The default orange used by the program **/
	public final static Color ORANGE = Color.decode("#FFBC7F");
	/** The default red used by the program **/
	public final static Color RED = Color.decode("#FE7E84");
	
	/** The red used to indicate an error in user input **/
	public final static javafx.scene.paint.Color ERROR_RED = javafx.scene.paint.Color.web("#FE7E84");
	/** The white used to fill fields to indicate no error **/
	public final static javafx.scene.paint.Color NOERROR_WHITE = javafx.scene.paint.Color.WHITE;

	/** The color of a line in the histogram **/
	public final static javafx.scene.paint.Color LINE_COLOR = javafx.scene.paint.Color.web("#03434B");
	/** The color of a bar in the histogram **/
    public final static javafx.scene.paint.Color BAR_COLOR = javafx.scene.paint.Color.web("#BC7BE9");
    /** The color of the histogram's background **/
    public final static javafx.scene.paint.Color BACKGROUND = javafx.scene.paint.Color.web("#CBEFF4");

	/** The program's icon, wich is displayed in the status bar of most popups **/
	public final static Image ICON = new Image(ResourceLoader.class.getResourceAsStream( "GraderIcon.png" ));
	/** The program's load screen **/
	public final static Image LOAD_SCREEN = new Image(ResourceLoader.class.getResourceAsStream( "GraderLoadScreen.jpg" ));
}
