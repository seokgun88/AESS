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

/**�л� GUI**/
public class GUI_StudentMain extends JPanel
 implements MouseListener, MouseMotionListener{
	JTable table_student;
	BorderLayout layout;
	JButton btn_enter, btn_cancel;
	JFrame Fr_table_studentClick;	//�л� ���� �߰� ������
	JComboBox cmbBx_startTime, cmbBx_endTime, cmbBx_date;
	JRadioButton rBtn_test, rBtn_lecture, rBtn_unable;
	int dragStartRow, dragStartCol, dragEndRow, dragEndCol;
	Enums enums = new Enums();	

	int notAvailType=-1;	//DB timeblock ���̺� �÷� ������ �Ұ����� ������ ���� �뵵 ����	
	
	protected Connection conn;
	String id;
	User_Student student;

	String [] dtm_col = {"�ð�","��","ȭ","��","��","��","��","��"};	
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

	String[][] schedule_no = new String[20][20];
	
	public GUI_StudentMain(Connection conn, String id)
	{
		this.conn = conn;
		this.id = id;
		this.student = new User_Student(id, conn);
		
		/**ȭ�� ���� ��ҵ� ����**/
		setBorder(new TitledBorder("�л� �ð�ǥ"));
		layout = new BorderLayout();
		setLayout(layout);

		JPanel panel_schedule = new JPanel();
		MyTableModel sche = new MyTableModel(dtm_data, dtm_col);
		table_student = new JTable(sche);		
		JScrollPane sp = new JScrollPane(table_student);
		panel_schedule.add("Center",sp);
		
		JButton okJButton = new JButton("Ȯ��");
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
		
		InitializeTable();
	}

	public void tableCellCenter(JTable t) // �� ���� ��� ����
	{
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		
		TableColumnModel tcm = t.getColumnModel();
		
		for(int i = 0; i<tcm.getColumnCount();i++)
		{
			tcm.getColumn(i).setCellRenderer(dtcr);
		}
	}
	public void setColumnSize(JTable t) // �� ���� ����ũ��
	{
		t.getColumnModel().getColumn(0).setPreferredWidth(100);
	}
	
	public void table_studentClick(int dayIndex, int sTimeIndex, int eTimeIndex) // <����, ����, �Ұ����� �ð���> ���� �Է�
	{		
		Fr_table_studentClick = new JFrame("���� �Է�");
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel introP = new JPanel();
		JLabel introL = new JLabel("���߰��� ������ �Է��ϼ����");
		introP.add(introL);
		
		/*****************************************/
		JPanel dateP = new JPanel();
		JLabel dateL = new JLabel("����");
		String[] dates = {"��","ȭ","��","��","��","��","��"};
		cmbBx_date = new JComboBox(dates);
		cmbBx_date.setSelectedIndex(dayIndex-1);
		cmbBx_date.setMaximumRowCount(3);
		
		dateP.add(dateL);
		dateP.add(cmbBx_date);
		
		JLabel timeL = new JLabel("�ð�");
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
		JLabel lb_code = new JLabel("�����ڵ�");
		JTextField tf_code = new JTextField(12);	
		classCodeP.add(lb_code);
		classCodeP.add(tf_code);	
		
		/*****************************************/
		JPanel panel_schedule = new JPanel();	
		rBtn_test = new JRadioButton("����", true);
		rBtn_lecture = new JRadioButton("����", false);
		rBtn_unable = new JRadioButton("���� �Ұ��� �ð�", false);

		ButtonGroup radioGroup = new ButtonGroup();
		rBtn_test.addActionListener(new studentListener());
		rBtn_lecture.addActionListener(new studentListener());
		rBtn_unable.addActionListener(new studentListener());
		radioGroup.add(rBtn_test);
		radioGroup.add(rBtn_lecture);
		radioGroup.add(rBtn_unable);

		panel_schedule.add(rBtn_test);
		panel_schedule.add(rBtn_lecture);
		if(info.is_J) panel_schedule.add(rBtn_unable);	//������ ���� �Ұ����� �ð� ��ư �߰�
		
		/******************************************/
		JPanel buttonP = new JPanel();
		btn_enter = new JButton("�Է�");
		btn_cancel = new JButton("���");
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
	
	public void InitializeTable() {
		Statement blockState;
		ResultSet blockResult;
	
		int row;
		int col;
		int i =0;
		
		MyTableModel DTM_Room2 = new MyTableModel(dtm_data, dtm_col);
		table_student.setModel(DTM_Room2);
		table_student.setColumnSelectionAllowed(true);
		table_student.setRowHeight(35);
		TableColumn column = table_student.getColumnModel().getColumn(0);
		column.setPreferredWidth(30);
		table_student.getColumn("�ð�").setPreferredWidth(95);
		tableCellCenter(table_student);

		table_student.revalidate();
		//table_Room.repaint();
		schedule_no = new String[20][20];
		
		try {
			blockState=conn.createStatement();
			/**�̹� �ִ� ������ �ҷ�����**/
			blockResult = blockState.executeQuery("select * from timeblock where user_id='" +id+ "' and isAvailable='F'");
			while(blockResult.next()) {
				row = enums.BlockToIndex(blockResult.getString("time"));
				col = enums.DayToIndex(blockResult.getString("day"));
				schedule_no[row][col] = blockResult.getString("no");
				
				/**scheduleNo ���� ���� ����, ����, �����Ұ��� �ν�**/
				int type = blockResult.getInt("scheduleNo");
				if(type==-1) table_student.setValueAt("����", row, col);
				else if(type==-2) table_student.setValueAt("����", row, col);
				else if(type==-3) table_student.setValueAt("���� �Ұ�", row, col);
			}
			
			/**�����ϴ� ���� ���� �ð�ǥ �ҷ�����**/
			blockState=conn.createStatement();
			blockResult = blockState.executeQuery(
					"select s.name, day, time from timeblock t, courseRelation c, schedule s where c.user_id='" +id+ "' and c.no=s.lecture_id and s.stype='C' and t.scheduleNo=s.no and t.isAvailable='F'"
					);
			while(blockResult.next()) {
				row = enums.BlockToIndex(blockResult.getString("time"));
				col = enums.DayToIndex(blockResult.getString("day"));
				table_student.setValueAt(blockResult.getString("name"), row, col);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	/**�ִ� ������ Ŭ���ϸ� ����â �˾�**/
	public void PopUpMenu(MouseEvent ce, final int no){
		JPopupMenu Pmenu;
		Pmenu = new JPopupMenu();
		final JMenuItem deleteSchedule = new JMenuItem("�����ϱ�");
		Pmenu.add(deleteSchedule);
		Pmenu.show(ce.getComponent(), ce.getX(), ce.getY());
		
		deleteSchedule.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(e.getSource()==deleteSchedule) {
					if(JOptionPane.showConfirmDialog(null,"�� ������ ���� �Ͻðڽ��ϱ�?", "���� ���",
							JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)!=JOptionPane.NO_OPTION)
					{
						student.DeleteTimeBlock(no);
						InitializeTable();
					} 
				}
			}
		});
   }

	/**���콺 �巡��, Ŭ�� ������**/
	class studentListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			/**�л� ���� �Է� ��ư Ŭ��**/
			if(e.getSource() == btn_enter) {
				student.Save();
				
				int start_time = enums.TimeToIndex(cmbBx_startTime.getSelectedItem().toString());
				int end_time = enums.TimeToIndex(cmbBx_endTime.getSelectedItem().toString());
				
				for(int i=start_time; i<end_time; i++) {
					student.CreateTimeBlock(cmbBx_date.getSelectedItem().toString(), enums.IndexToBlock[i], notAvailType);
				}
				
				Fr_table_studentClick.setVisible(false);
				InitializeTable();
			}
			
			else if(e.getSource() == btn_cancel) {
				Fr_table_studentClick.setVisible(false);
			}
			
			else if(e.getSource() == rBtn_test) notAvailType = -1;
			else if(e.getSource() == rBtn_lecture) notAvailType = -2;
			else if(e.getSource() == rBtn_unable) notAvailType = -3;
		}
	}

	/**�̹� �ִ� ���� Ŭ����**/
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

	/**���콺 �巡�� �Ҷ� ���� ĭ�� �� ĭ ��ǥ ����**/
	public void mousePressed(MouseEvent pe) {
		// TODO Auto-generated method stub
		if(pe.getSource()==table_student) {
			this.dragStartRow = table_student.getSelectedRow();
			this.dragStartCol = table_student.getSelectedColumn();
			System.out.println(dragStartRow+"��,"+dragStartCol+"��");
		}
	}
	public void mouseReleased(MouseEvent re) {
		// TODO Auto-generated method stub
		if(re.getSource()==table_student) {
			this.dragEndRow = table_student.getSelectedRow();
			this.dragEndCol = table_student.getSelectedColumn();

			/**�巡���� ���� �ٸ��� ���� ���� ���� �Է�**/
			if(dragEndRow!=dragStartRow && dragEndCol == dragStartCol){
				/**�������� �ִ� ĭ �巡�� �ߴ��� Ȯ��**/
				boolean scheduleDuplicate = false;
				for(int i=dragStartRow; i<=dragEndRow; i++){
					if(table_student.getValueAt(i, dragStartCol)!=""){
						scheduleDuplicate = true;
						break;
					}
				}
				/**�̹� �������� �ִ� ĭ �巡�� ���� �� ����**/
				if(!scheduleDuplicate){
					if(dragStartRow<=dragEndRow)	table_studentClick(dragStartCol, dragStartRow, dragEndRow);					
					/**�巡�׸� �Ʒ��� ���� ���� ��� ���۰� ���ð� �ݴ�� �Է�**/
					else	table_studentClick(dragStartCol, dragEndRow, dragStartRow);
				}
			}
			System.out.println(dragEndRow+"��,"+dragStartCol+"��");
		}
	}
	/**********************************************/

	@Override
	public void mouseMoved(MouseEvent arg0) {}

	@Override
	public void mouseDragged(MouseEvent arg0) {}
}
