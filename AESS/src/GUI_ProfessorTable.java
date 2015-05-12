import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**교수 수업 시간표 테이블**/
public class GUI_ProfessorTable extends JPanel implements MouseListener
{
	JTable profTable;
	BorderLayout layout;
	
	String [] col = {"시간","월","화","수","목","금","토","일"};
	String [][] data = {{"1A","","","","","","",""},
			{"1B","","","","","","",""},
			{"2A","","","","","","",""},
			{"2B","","","","","","",""},
			{"3A","","","","","","",""},
			{"3B","","","","","","",""},
			{"4A","","","","","","",""},
			{"4B","","","","","","",""},
			{"5A","","","","","","",""},
			{"5B","","","","","","",""},
			{"6A","","","","","","",""},
			{"6B","","","","","","",""},
			{"7A","","","","","","",""},
			{"7B","","","","","","",""},
			{"8A","","","","","","",""},
			{"8B","","","","","","",""},
			{"9A","","","","","","",""},
			{"9B","","","","","","",""}};
	
	User_Professor prof;
	String id;
	protected Connection conn;
	String[][] schedule_no = new String[20][20];
	
	public GUI_ProfessorTable(Connection conn, String id)
	{
		this.conn = conn;
		this.id = id;
		
		setBorder(new TitledBorder("수업 시간표"));
		layout = new BorderLayout();
		setLayout(layout);
		
		JPanel scheP = new JPanel();
		DefaultTableModel sche = new DefaultTableModel(data, col);
		profTable = new JTable(sche);		
		JScrollPane sp = new JScrollPane(profTable);
		scheP.add("Center",sp);
		
		add(scheP);
		
		sp.setPreferredSize(new java.awt.Dimension(800, 500));
		profTable.setColumnSelectionAllowed(true);
		profTable.setRowHeight(40);
		TableColumn column = profTable.getColumnModel().getColumn(0);
		column.setPreferredWidth(30);
		
		profTable.addMouseListener(this);
		
		prof=new User_Professor(id, conn);
		prof.InitializeTable(profTable, schedule_no);

	}

	public void mouseClicked(MouseEvent me) {
		int row = profTable.getSelectedRow();
		int column = profTable.getSelectedColumn();
		if(schedule_no[row][column] != null) {
			System.out.println(schedule_no[row][column]);
			remove(layout.getLayoutComponent(BorderLayout.CENTER));
			revalidate();
			repaint();
			GUI_Professor prof_main = new GUI_Professor(conn, id, schedule_no[row][column]);
			add(BorderLayout.CENTER, prof_main);
			this.setBorder(new TitledBorder((String) profTable.getValueAt(row, column) ));
		}
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}
}
