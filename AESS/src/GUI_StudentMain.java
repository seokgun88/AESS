import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class GUI_StudentMain extends JPanel // 학생 메인화면이 될 GUI
 implements MouseListener, MouseMotionListener
{
	JTable studTable;
	BorderLayout layout;
	private JButton exitJButton;
	String time1;
	JButton enterB, cancelB;
	JFrame Fr_studTableClick;
	JComboBox timeC1, timeC2, dateC;
	JRadioButton testB, lectB, unabB;
	int notAvailType=-1;
	
	int dragStartRow, dragStartCol, dragEndRow;
	Enums enums = new Enums();
	String id;

	String [] dtm_col = {"시간","월","화","수","목","금","토","일"};	
	String [][] dtm_data = {{"1A(09:00~09:30)","","","","","","",""},
			{"1B(09:30~10:00)","","","","","","",""},
			{"2A(10:00~10:30)","","","","","","",""},
			{"2B(10:30~11:00)","","","","","","",""},
			{"3A(11:00~11:30)","","","","","","",""},
			{"3B(11:30~12:00)","","","","","","",""},
			{"4A(12:00~12:30)","","","","","","",""},
			{"4B(12:30~01:00)","","","","","","",""},
			{"5A(01:00~01:30)","","","","","","",""},
			{"5B(01:30~02:00)","","","","","","",""},
			{"6A(02:00~02:30)","","","","","","",""},
			{"6B(02:30~03:00)","","","","","","",""},
			{"7A(03:00~03:30)","","","","","","",""},
			{"7B(03:30~04:00)","","","","","","",""},
			{"8A(04:00~04:30)","","","","","","",""},
			{"8B(04:30~05:00)","","","","","","",""},
			{"9A(05:00~05:30)","","","","","","",""},
			{"9B(05:30~06:00)","","","","","","",""}};

	User_Student student;
	protected Connection conn;
	String[][] schedule_no = new String[20][20];
	
	public GUI_StudentMain(Connection conn, String id)
	{
		this.conn = conn;
		this.id = id;
		this.student = new User_Student(id, conn);
		
		setBorder(new TitledBorder("학생 시간표"));
		layout = new BorderLayout();
		setLayout(layout);

		JPanel scheP = new JPanel();
		MyTableModel sche = new MyTableModel(dtm_data, dtm_col);
		studTable = new JTable(sche);		
		JScrollPane sp = new JScrollPane(studTable);
		scheP.add("Center",sp);
		
		JButton okJButton = new JButton("확인");
		scheP.add("South", okJButton);

		add(scheP);
		
		sp.setPreferredSize(new java.awt.Dimension(880,500));
		studTable.setColumnSelectionAllowed(true);
		studTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		studTable.setRowHeight(40);
		TableColumn column = studTable.getColumnModel().getColumn(0);
		column.setPreferredWidth(30);
		
		studTable.addMouseListener(this);
		
		setColumnSize(studTable);
		tableCellCenter(studTable);
		InitializeTable();

	}

	public void tableCellCenter(JTable t) // 셀 내용 가운데 정렬
	{
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		
		TableColumnModel tcm = t.getColumnModel();
		
		for(int i = 0; i<tcm.getColumnCount();i++)
		{
			tcm.getColumn(i).setCellRenderer(dtcr);
		}
	}
	public void setColumnSize(JTable t) // 젤 왼쪽 가로크기
	{
		t.getColumnModel().getColumn(0).setPreferredWidth(100);
	}
	
	public void studTableClick(int dayIndex, int sTimeIndex, int eTimeIndex) // <시험, 수업, 불가능한 시간대> 일정 입력
	{
		
		Fr_studTableClick = new JFrame("일정 입력");
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel introP = new JPanel();
		JLabel introL = new JLabel("※추가할 일정을 입력하세요※");
		introP.add(introL);
		
		/*****************************************/
		JPanel dateP = new JPanel();
		JLabel dateL = new JLabel("요일");
		String[] dates = {"월","화","수","목","금","토","일"};
		dateC = new JComboBox(dates);
		dateC.setSelectedIndex(dayIndex-1);
		dateC.setMaximumRowCount(3);
		
		dateP.add(dateL);
		dateP.add(dateC);
		
		JLabel timeL = new JLabel("시간");
		String[] Time = {"9:00","9:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00"};
		timeC1  = new JComboBox(Time);
		timeC1.setSelectedIndex(sTimeIndex);
		JLabel timewaveL = new JLabel("~");
		timeC2 = new JComboBox(Time);
		timeC2.setSelectedIndex(eTimeIndex+1);
		
		dateP.add(timeL);
		dateP.add(timeC1);
		dateP.add(timewaveL);
		dateP.add(timeC2);
		
		/*****************************************/
		
		JPanel classCodeP = new JPanel();
		JLabel lb_code = new JLabel("과목코드");
		JTextField tf_code = new JTextField(12);	
		classCodeP.add(lb_code);
		classCodeP.add(tf_code);	
		
		/*****************************************/
		JPanel scheP = new JPanel();	
		testB = new JRadioButton("시험", true);
		lectB = new JRadioButton("수업", false);
		unabB = new JRadioButton("감독 불가능 시간", false);

		ButtonGroup radioGroup = new ButtonGroup();
		testB.addActionListener(new studentListener());
		lectB.addActionListener(new studentListener());
		unabB.addActionListener(new studentListener());
		radioGroup.add(testB);
		radioGroup.add(lectB);
		radioGroup.add(unabB);

		scheP.add(testB);
		scheP.add(lectB);
		if(info.is_J) scheP.add(unabB);
		
		/******************************************/
		JPanel buttonP = new JPanel();
		enterB = new JButton("입력");
		cancelB = new JButton("취소");
		enterB.addActionListener(new studentListener());
		cancelB.addActionListener(new studentListener());
		
		buttonP.add(enterB);
		buttonP.add(cancelB);
		
		/******************************************/
		panel.add(introP);
		panel.add(dateP);
		panel.add(classCodeP);
		panel.add(scheP);
		panel.add(buttonP);
		
		Fr_studTableClick.add(panel);
		Fr_studTableClick.setSize(450, 200);
		Fr_studTableClick.setVisible(true);	
		
	}
	
	public void InitializeTable() {
		Statement blockState;
		ResultSet blockResult;
	
		int row;
		int col;
		int i =0;
		
		MyTableModel DTM_Room2 = new MyTableModel(dtm_data, dtm_col);
		studTable.setModel(DTM_Room2);
		studTable.setColumnSelectionAllowed(true);
		studTable.setRowHeight(35);
		TableColumn column = studTable.getColumnModel().getColumn(0);
		column.setPreferredWidth(30);
		studTable.getColumn("시간").setPreferredWidth(95);
		tableCellCenter(studTable);

		studTable.revalidate();
		//table_Room.repaint();
		schedule_no = new String[20][20];
		
		try {
			blockState=conn.createStatement();
			blockResult = blockState.executeQuery("select * from timeblock where user_id='" +id+ "' and isAvailable='F'");
			while(blockResult.next()) {
				row = enums.BlockToIndex(blockResult.getString("time"));
				col = enums.DayToIndex(blockResult.getString("day"));
				int type = blockResult.getInt("scheduleNo");
				schedule_no[row][col] = blockResult.getString("no");
				if(type==-1) studTable.setValueAt("시험", row, col);
				else if(type==-2) studTable.setValueAt("수업", row, col);
				else if(type==-3) studTable.setValueAt("감독 불가", row, col);
			}
			
			blockState=conn.createStatement();
			blockResult = blockState.executeQuery("select s.name, day, time from timeblock t, courseRelation c, schedule s where c.user_id='" +id+ "' and c.no=s.lecture_id and s.stype='C' and t.scheduleNo=s.no and t.isAvailable='F'");
			while(blockResult.next()) {
				row = enums.BlockToIndex(blockResult.getString("time"));
				col = enums.DayToIndex(blockResult.getString("day"));
				studTable.setValueAt(blockResult.getString("name"), row, col);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void PopUpMenu(MouseEvent ce, final int no){
		JPopupMenu Pmenu;
		Pmenu = new JPopupMenu();
		final JMenuItem modifySchedule = new JMenuItem("Block No. "+no);
		Pmenu.add(modifySchedule);
		modifySchedule.setEnabled(false);
		final JMenuItem deleteSchedule = new JMenuItem("삭제하기");
		Pmenu.add(deleteSchedule);
		Pmenu.show(ce.getComponent(), ce.getX(), ce.getY());
		deleteSchedule.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(e.getSource()==deleteSchedule) {
					if(JOptionPane.showConfirmDialog(null,"이 일정을 삭제 하시겠습니까?", "삭제 경고",
							JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)!=JOptionPane.NO_OPTION)
					{
						student.DeleteTimeBlock(no);
						InitializeTable();
					} 
				}
			}
		});
   }

	class studentListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == enterB) {
				student.Save();
				
				int start_time = enums.TimeToIndex(timeC1.getSelectedItem().toString());
				int end_time = enums.TimeToIndex(timeC2.getSelectedItem().toString());
				int i;
				
				for(i=start_time; i<end_time; i++) {
					student.CreateTimeBlock(dateC.getSelectedItem().toString(), enums.IndexToBlock[i], notAvailType);
				}
				
				Fr_studTableClick.setVisible(false);
				InitializeTable();
			}
			
			else if(e.getSource() == cancelB) {
				Fr_studTableClick.setVisible(false);
			}
			
			else if(e.getSource() == testB) notAvailType = -1;
			else if(e.getSource() == lectB) notAvailType = -2;
			else if(e.getSource() == unabB) notAvailType = -3;
		}
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		if(me.getSource()==studTable) {
			if(schedule_no[studTable.getSelectedRow()][studTable.getSelectedColumn()]!=null)
				PopUpMenu(me, Integer.parseInt(schedule_no[studTable.getSelectedRow()][studTable.getSelectedColumn()]));
		}
	}
		

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent pe) {
		// TODO Auto-generated method stub
		if(pe.getSource()==studTable) {
			this.dragStartRow = studTable.getSelectedRow();
			this.dragStartCol = studTable.getSelectedColumn();
			System.out.println(dragStartRow+"행,"+dragStartCol+"열");
		}
	}

	@Override
	public void mouseReleased(MouseEvent re) {
		// TODO Auto-generated method stub
		if(re.getSource()==studTable) {
			this.dragEndRow = studTable.getSelectedRow();
			if(dragEndRow!=dragStartRow) studTableClick(dragStartCol, dragStartRow, dragEndRow);
			System.out.println(dragEndRow+"행,"+dragStartCol+"열");
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {}

	@Override
	public void mouseDragged(MouseEvent arg0) {}
}
