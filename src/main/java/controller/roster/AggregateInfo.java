package controller.roster;

import java.util.ArrayList;
import java.util.HashMap;

public class AggregateInfo {
    private String title;
    private HashMap<String, String> cells;
    
    public AggregateInfo(String newTitle) {
        title = newTitle;
        cells = new HashMap<String, String>();
    }
    
    public void addCell(String column, int cell) {
        cells.put(column, cell + "");
    }
    
    public void addCell(String column, double cell) {
        cells.put(column, String.format("%.2f", cell));
    }
    
    public void addCell(String column, String cell) {
        cells.put(column, cell);
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getCell(String column) {
        if (column.equals("")) {
            return "";
        }
        
        if (!cells.containsKey(column)) {
            return "";
        }
        
        return cells.get(column);
    }
}
