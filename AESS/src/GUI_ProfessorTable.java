import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**���� ���� �ð�ǥ ���̺�**/
public class GUI_ProfessorTable extends JPanel implements MouseListener{
	private User_Professor prof;
	private String id;
	
	private JTable profTable;
	private BorderLayout layout;
	
	private String [] col = {"�ð�","��","ȭ","��","��","��","��","��"};
	private String [][] data = {{"1A","","","","","","",""},
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
	
	private String[][] schedule_no = new String[20][20];
	
	public boolean isProfTable; //�׽�Ʈ ���̽� Ȯ�ο� ����
	
	public GUI_ProfessorTable(String id){
		this.id = id;
		
		isProfTable = true;
		
		setBorder(new TitledBorder("���� �ð�ǥ"));
		layout = new BorderLayout();
		setLayout(layout);
		
		JPanel scheP = new JPanel();
		DefaultTableModel sche = new DefaultTableModel(data, col);
		profTable = new JTable(sche);
		
		/******************�̿��� �߰� : ���̺� ��� ���� �Ұ�*******************/
		profTable.getTableHeader().setReorderingAllowed(false);
		profTable.getTableHeader().setResizingAllowed(false);
		
		JScrollPane sp = new JScrollPane(profTable);
		scheP.add("Center",sp);
		
		add(scheP);
		
		sp.setPreferredSize(new java.awt.Dimension(800, 500));
		profTable.setColumnSelectionAllowed(true);
		profTable.setRowHeight(40);
		TableColumn column = profTable.getColumnModel().getColumn(0);
		column.setPreferredWidth(30);
		
		profTable.addMouseListener(this);
		
		prof=new User_Professor(id);
		prof.InitializeTable(profTable, schedule_no);
	}
	
	/***************�ٽ� ���Ǹ���� ������ �� �� ���� ���ִ� �Լ�*******************/
	public void resetProfessorTable(){
		remove(layout.getLayoutComponent(BorderLayout.CENTER));
		revalidate();
		repaint();
		
		isProfTable = true;
		
		setBorder(new TitledBorder("���� �ð�ǥ"));		
		JPanel scheP = new JPanel();
		DefaultTableModel sche = new DefaultTableModel(data, col);
		profTable = new JTable(sche);					
		
		/******************�̿��� �߰� : ���̺� ��� ���� �Ұ�*******************/
		profTable.getTableHeader().setReorderingAllowed(false);
		profTable.getTableHeader().setResizingAllowed(false);
		
		profTable.setColumnSelectionAllowed(true);
		profTable.setRowHeight(40);
		JScrollPane sp = new JScrollPane(profTable);
		sp.setPreferredSize(new java.awt.Dimension(800, 500));	
		scheP.add("Center",sp);
		add(scheP);
		profTable.addMouseListener(this);
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
			
			isProfTable = false;
			
			GUI_Professor prof_main = new GUI_Professor(id, schedule_no[row][column]);
			add(BorderLayout.CENTER, prof_main);
			this.setBorder(new TitledBorder((String) profTable.getValueAt(row, column) ));
		}
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
