import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
/**GUI for student**/
public class GUI_StudentMain extends JPanel
 implements MouseListener, MouseMotionListener{
	private String id;
	private User_Student student;
	private int notAvailType=-1;	//DB timeblock 테이블 컬럼 생성시 불가능한 스케쥴 구분 용도 변수	
	
	private JTable table_student;
	private BorderLayout layout;
	private JButton btn_enter, btn_cancel;
	private JFrame Fr_table_studentClick;	//학생 일정 추가 프레임
	private JComboBox cmbBx_startTime, cmbBx_endTime, cmbBx_date;
	private JRadioButton rBtn_test, rBtn_lecture, rBtn_unable;
	private int dragStartRow, dragStartCol, dragEndRow, dragEndCol;
	private Enums enums = new Enums();

	private String [] dtm_col = {"시간","월","화","수","목","금","토","일"};	
	private String [][] dtm_data = {{"1A(09:00~09:30)","","","","","","",""},
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

	private String[][] schedule_no = new String[20][20];
	
	/**Constructor**/
	public GUI_StudentMain(String id){
		this.id = id;
		this.student = new User_Student();
		
		/**Set screen properties**/
		setBorder(new TitledBorder("학생 시간표"));
		layout = new BorderLayout();
		setLayout(layout);

		JPanel panel_schedule = new JPanel();
		MyTableModel sche = new MyTableModel(dtm_data, dtm_col);
		table_student = new JTable(sche);		
		table_student.getTableHeader().setReorderingAllowed(false); //이영석 추가 : 테이블 헤더 드래그 불가
		table_student.getTableHeader().setResizingAllowed(false); //이영석 추가 : 테이블 크기 수정 불가
		JScrollPane sp = new JScrollPane(table_student);
		panel_schedule.add("Center",sp);
		
		JButton okJButton = new JButton("확인");
		panel_schedule.add("South", okJButton);

		add(panel_schedule);
		
		sp.setPreferredSize(new java.awt.Dimension(880,500));
		table_student.setColumnSelectionAllowed(true);
		table_student.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_student.setRowHeight(40);
		TableColumn column = table_student.getColumnModel().getColumn(0);
		column.setPreferredWidth(30);		
		table_student.addMouseListener(this);		
		setColumnSize(table_student);
		tableCellCenter(table_student);
		
		initializeTable();
	}

	/**Align cell subjects center**/
	public void tableCellCenter(JTable t)
	{
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		
		TableColumnModel tcm = t.getColumnModel();
		
		for(int i = 0; i<tcm.getColumnCount();i++)
		{
			tcm.getColumn(i).setCellRenderer(dtcr);
		}
	}
	/**Width of left**/
	public void setColumnSize(JTable t)
	{
		t.getColumnModel().getColumn(0).setPreferredWidth(100);
	}
	
	/**Input schedule with <Exam, Class, Impossible Time>**/
	public void table_studentClick(int dayIndex, int sTimeIndex, int eTimeIndex)
	{		
		Fr_table_studentClick = new JFrame("일정 입력");
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel introP = new JPanel();
		JLabel introL = new JLabel("※추가할 일정을 입력하세요※");
		introP.add(introL);
		
		/*****************************************/
		JPanel dateP = new JPanel();
		JLabel dateL = new JLabel("요일");
		String[] dates = {"월","화","수","목","금","토","일"};
		cmbBx_date = new JComboBox(dates);
		cmbBx_date.setSelectedIndex(dayIndex-1);
		cmbBx_date.setMaximumRowCount(3);
		
		dateP.add(dateL);
		dateP.add(cmbBx_date);
		
		JLabel timeL = new JLabel("시간");
		String[] Time = {"09:00","09:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00"};
		cmbBx_startTime  = new JComboBox(Time);
		cmbBx_startTime.setSelectedIndex(sTimeIndex);
		JLabel timewaveL = new JLabel("~");
		cmbBx_endTime = new JComboBox(Time);
		cmbBx_endTime.setSelectedIndex(eTimeIndex+1);
		
		dateP.add(timeL);
		dateP.add(cmbBx_startTime);
		dateP.add(timewaveL);
		dateP.add(cmbBx_endTime);
		
		/*****************************************/
		
		JPanel classCodeP = new JPanel();
		JLabel lb_code = new JLabel("과목코드");
		JTextField tf_code = new JTextField(12);	
		classCodeP.add(lb_code);
		classCodeP.add(tf_code);	
		
		/*****************************************/
		JPanel panel_schedule = new JPanel();	
		rBtn_test = new JRadioButton("시험", true);
		rBtn_lecture = new JRadioButton("수업", false);
		rBtn_unable = new JRadioButton("감독 불가능 시간", false);

		ButtonGroup radioGroup = new ButtonGroup();
		rBtn_test.addActionListener(new studentListener());
		rBtn_lecture.addActionListener(new studentListener());
		rBtn_unable.addActionListener(new studentListener());
		radioGroup.add(rBtn_test);
		radioGroup.add(rBtn_lecture);
		radioGroup.add(rBtn_unable);

		panel_schedule.add(rBtn_test);
		panel_schedule.add(rBtn_lecture);
		if(Info.getType().equals("J"))
			panel_schedule.add(rBtn_unable); //Add unable time for Assistant
		
		/******************************************/
		JPanel buttonP = new JPanel();
		btn_enter = new JButton("입력");
		btn_cancel = new JButton("취소");
		btn_enter.addActionListener(new studentListener());
		btn_cancel.addActionListener(new studentListener());
		
		buttonP.add(btn_enter);
		buttonP.add(btn_cancel);
		
		/******************************************/
		panel.add(introP);
		panel.add(dateP);
		panel.add(classCodeP);
		panel.add(panel_schedule);
		panel.add(buttonP);
		
		Fr_table_studentClick.add(panel);
		Fr_table_studentClick.setSize(450, 200);
		Fr_table_studentClick.setVisible(true);			
	}

	/**Initialize table**/
	public void initializeTable() {
		MyTableModel DTM_Room2 = new MyTableModel(dtm_data, dtm_col);
		table_student.setModel(DTM_Room2);
		table_student.setColumnSelectionAllowed(true);
		table_student.setRowHeight(35);
		TableColumn column = table_student.getColumnModel().getColumn(0);
		column.setPreferredWidth(30);
		table_student.getColumn("시간").setPreferredWidth(95);
		tableCellCenter(table_student);

		table_student.revalidate();
		//table_Room.repaint();
		
		/**이영석 수정 : GUI에 있는 세부 기능 User_Student로 이동**/
		Vector v_schedule = student.getStudentSchedule(id);
		schedule_no = (String[][]) v_schedule.get(0);
		Vector v_scheduleInfos = (Vector) v_schedule.get(1);
		for(int i=0; i<v_scheduleInfos.size(); i++){
			Vector v_scheduleInfo = (Vector) v_scheduleInfos.get(i);
			table_student.setValueAt((String)v_scheduleInfo.get(0), (int)v_scheduleInfo.get(1), (int)v_scheduleInfo.get(2));			
		}
	}
	
	/**Deletion window pops up when schedule is clicked**/
	public void PopUpMenu(MouseEvent ce, final int no){
		JPopupMenu Pmenu;
		Pmenu = new JPopupMenu();
		final JMenuItem deleteSchedule = new JMenuItem("삭제하기");
		Pmenu.add(deleteSchedule);
		Pmenu.show(ce.getComponent(), ce.getX(), ce.getY());
		
		deleteSchedule.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(e.getSource()==deleteSchedule) {
					if(JOptionPane.showConfirmDialog(null,"이 일정을 삭제 하시겠습니까?", "삭제 경고",
							JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)!=JOptionPane.NO_OPTION)
					{
						student.deleteTimeBlock(no);
						initializeTable();
					} 
				}
			}
		});
   }

	/**Mouse drag, click listener**/
	class studentListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			/**Student schedule input button click**/
			if(e.getSource() == btn_enter) {
				student.save();
				
				int start_time = enums.TimeToIndex(cmbBx_startTime.getSelectedItem().toString());
				int end_time = enums.TimeToIndex(cmbBx_endTime.getSelectedItem().toString());
				
				for(int i=start_time; i<end_time; i++) {
					student.createTimeBlock(cmbBx_date.getSelectedItem().toString(), enums.IndexToBlock[i], notAvailType);
				}
				
				Fr_table_studentClick.setVisible(false);
				initializeTable();
			}
			
			else if(e.getSource() == btn_cancel) {
				Fr_table_studentClick.setVisible(false);
			}
			
			else if(e.getSource() == rBtn_test) notAvailType = -1;
			else if(e.getSource() == rBtn_lecture) notAvailType = -2;
			else if(e.getSource() == rBtn_unable) notAvailType = -3;
		}
	}

	/**Schedule exists clicked**/
	public void mouseClicked(MouseEvent me) {
		if(me.getSource()==table_student) {
			if(schedule_no[table_student.getSelectedRow()][table_student.getSelectedColumn()]!=null)
				PopUpMenu(me, Integer.parseInt(schedule_no[table_student.getSelectedRow()][table_student.getSelectedColumn()]));
		}
	}		

	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	
	/**Store the first and the last item coordinate when dragged**/
	public void mousePressed(MouseEvent pe) {
		// TODO Auto-generated method stub
		if(pe.getSource()==table_student) {
			this.dragStartRow = table_student.getSelectedRow();
			this.dragStartCol = table_student.getSelectedColumn();
			System.out.println(dragStartRow+"행,"+dragStartCol+"열");
		}
	}
	public void mouseReleased(MouseEvent re) {
		// TODO Auto-generated method stub
		if(re.getSource()==table_student) {
			this.dragEndRow = table_student.getSelectedRow();
			this.dragEndCol = table_student.getSelectedColumn();

			/**Input only dragged row is different and column is same**/
			if(dragEndRow!=dragStartRow && dragEndCol == dragStartCol){
				/**Confirm scheduled item is dragged**/
				boolean scheduleDuplicate = false;
				for(int i=dragStartRow; i<=dragEndRow; i++){
					if(table_student.getValueAt(i, dragStartCol)!=""){
						scheduleDuplicate = true;
						break;
					}
				}
				/**Ignore the item already scheduled**/
				if(!scheduleDuplicate){
					if(dragStartRow<=dragEndRow)	table_studentClick(dragStartCol, dragStartRow, dragEndRow);					
					/**Reverse start time and end time if dragged from bottom to top**/
					else	table_studentClick(dragStartCol, dragEndRow, dragStartRow);
				}
			}
			System.out.println(dragEndRow+"행,"+dragStartCol+"열");
		}
	}
	/**********************************************/
	@Override
	public void mouseMoved(MouseEvent arg0) {}
	@Override
	public void mouseDragged(MouseEvent arg0) {}
}