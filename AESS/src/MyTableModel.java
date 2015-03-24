import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class MyTableModel extends DefaultTableModel{
	public MyTableModel(Vector v1, Vector v2) {
        super(v1, v2);
    }
	
    public MyTableModel(String[][] st1, String[] st2) {
    	super(st1, st2);
	}

	public boolean isCellEditable(int row, int column) {
        return false;
    }
}