import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**My Table Model class**/
@SuppressWarnings("serial")
public class MyTableModel extends DefaultTableModel{
	/**Constructor Vector used**/
	public MyTableModel(Vector v1, Vector v2) {
        super(v1, v2);
    }
	
	/**Constructor String used**/
    public MyTableModel(String[][] st1, String[] st2) {
    	super(st1, st2);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
        return false;
    }
}