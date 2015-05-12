import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class GUI_SetPeriod extends JPanel implements ActionListener, MouseListener {
	int thisYear=2015, thisMonth=2, thisWeek=1, nowRow;
	JTable weekTable;
	JPanel pn_week;
	JLabel lb_year, lb_month, lb_week, lb_yearText, lb_monthText, lb_weekText;
	JPanel pn_bt_left, pn_bt_right;
	JButton bt_left, bt_right, done;
	CalendarModel tm1;
	JScrollPane jsp;
	Connection conn;
	String id;
	User_Admin admin;
 
	public GUI_SetPeriod(Connection conn, String id) {
		this.id = id;
		this.conn = conn;
		info i = new info(id);
		
		if(info.is_A) admin = new User_Admin(id, conn);
		
		setLayout(new BorderLayout(10,10));
		
		bt_left = new JButton("◀");
		bt_left.addActionListener(this);
		bt_right = new JButton("▶");
		bt_right.addActionListener(this);
		pn_bt_left = new JPanel();
		pn_bt_left.add(bt_left);
		pn_bt_left.setPreferredSize(new Dimension(80,20));
		pn_bt_right = new JPanel();
		pn_bt_right.setPreferredSize(new Dimension(80,20));
		pn_bt_right.add(bt_right);
		
		add(pn_bt_left, "West");
		add(pn_bt_right, "East");
		
		tm1 = new CalendarModel(0, 0);
		weekTable = new JTable(tm1);
		weekTable.addMouseListener(this);
		jsp = new JScrollPane(weekTable);
		jsp.setPreferredSize(new java.awt.Dimension(800, 500));
		weekTable.setRowHeight(50);
		tableCellCenter(weekTable);	
		done = new JButton("완료");
		done.addActionListener(this);
		JPanel pn_done = new JPanel();
		pn_done.add(done);
		pn_done.setPreferredSize(new Dimension(80,80));		
		add(jsp, "Center");		
			
		add(pn_done, "South");
		
		if(info.week!=0) {
			thisYear = info.year;
			thisMonth = info.month;
			thisWeek = info.week;
			weekTable.changeSelection(info.week-1, 0, false, false);
		}
		
		lb_year = new JLabel(Integer.toString(thisYear));
		lb_yearText = new JLabel("년");
		lb_month = new JLabel(Integer.toString(thisMonth));
		lb_monthText = new JLabel("월");
		lb_week = new JLabel(Integer.toString(thisWeek));
		lb_weekText = new JLabel("째 주");
		
		pn_week = new JPanel();
		
		pn_week.add(lb_year);
		pn_week.add(lb_yearText);
		pn_week.add(lb_month);
		pn_week.add(lb_monthText);
		pn_week.add(lb_week);
		pn_week.add(lb_weekText);
		
		add(pn_week, "North");
	}
	
	public void tableCellCenter(JTable t) // table 가운데 정렬
	{
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);		
		TableColumnModel tcm = t.getColumnModel();		
		for(int i = 0; i<tcm.getColumnCount();i++)
		{
			tcm.getColumn(i).setCellRenderer(dtcr);
		}
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == bt_left) {
			if(thisMonth>1) thisMonth--;
			else {
				thisYear--;
				thisMonth=12;
			}
			lb_year.setText(Integer.toString(thisYear));
			lb_month.setText(Integer.toString(thisMonth));
			remove(jsp);
			tm1 = new CalendarModel(thisYear,thisMonth);
			weekTable = new JTable(tm1);
			weekTable.addMouseListener(this);
			jsp = new JScrollPane(weekTable);
			jsp.setPreferredSize(new java.awt.Dimension(800, 500));
			weekTable.setRowHeight(50);
			tableCellCenter(weekTable);	
			add(jsp, "Center");
			thisWeek=1;
			lb_week.setText(Integer.toString(thisWeek));
		} 
		else if(e.getSource() == bt_right) 
		{
			if(thisMonth<12) thisMonth++;
			else {
				thisYear++;
				thisMonth=1;
			}
			lb_year.setText(Integer.toString(thisYear));
			lb_month.setText(Integer.toString(thisMonth));
			remove(jsp);
			tm1 = new CalendarModel(thisYear,thisMonth);
			weekTable = new JTable(tm1);
			weekTable.addMouseListener(this);
			jsp = new JScrollPane(weekTable);
			jsp.setPreferredSize(new java.awt.Dimension(800, 500));
			weekTable.setRowHeight(50);
			tableCellCenter(weekTable);	
			add(jsp, "Center");
			thisWeek=1;
			lb_week.setText(Integer.toString(thisWeek));
		}
		else if(e.getSource() == done) 
		{		
			String test;
			int sDate = Integer.parseInt(weekTable.getValueAt(nowRow, 0).toString());
			int eDate = Integer.parseInt(weekTable.getValueAt(nowRow, 6).toString());
			
			if(info.test.equals("중간")) test="M";
			else test="F";
			
			admin.SetExamWeek(info.year, info.season, test, thisMonth, thisWeek, sDate, eDate);
			JOptionPane.showMessageDialog(null, "시험 기간이 변경되었습니다. 지금 이후로 로그인한 사용자에게 반영됩니다.");  
		}
	}
	
	public void mouseClicked(MouseEvent me) {
		this.nowRow = weekTable.getSelectedRow();
		thisWeek = nowRow+1;
		lb_week.setText(Integer.toString(thisWeek));
	};
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}	
