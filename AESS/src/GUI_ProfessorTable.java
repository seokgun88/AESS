import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**교수 수업 시간표 테이블**/
public class GUI_ProfessorTable extends JPanel implements MouseListener{
	private User_Professor prof;
	private String id;
	
	private JTable profTable;
	private BorderLayout layout;
	
	private String [] col = {"시간","월","화","수","목","금","토","일"};
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
	
	public boolean isProfTable; //테스트 케이스 확인용 변수
	
	public GUI_ProfessorTable(String id){
		this.id = id;
		
		isProfTable = true;
		
		setBorder(new TitledBorder("수업 시간표"));
		layout = new BorderLayout();
		setLayout(layout);
		
		JPanel scheP = new JPanel();
		DefaultTableModel sche = new DefaultTableModel(data, col);
		profTable = new JTable(sche);
		
		/******************이영석 추가 : 테이블 헤더 수정 불가*******************/
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
	
	/***************다시 강의목록을 볼려고 할 때 리셋 해주는 함수*******************/
	public void resetProfessorTable(){
		remove(layout.getLayoutComponent(BorderLayout.CENTER));
		revalidate();
		repaint();
		
		isProfTable = true;
		
		setBorder(new TitledBorder("수업 시간표"));		
		JPanel scheP = new JPanel();
		DefaultTableModel sche = new DefaultTableModel(data, col);
		profTable = new JTable(sche);					
		
		/******************이영석 추가 : 테이블 헤더 수정 불가*******************/
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
