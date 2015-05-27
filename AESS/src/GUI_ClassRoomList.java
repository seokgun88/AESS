import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**���ǽ� ��� �� ������ GUI**/
public class GUI_ClassRoomList extends JPanel implements MouseListener{
	private Connection conn;	//DB������ ���� ���ؼ� ����
	private User_Admin admin; //�������� ��� ���ǽ� ���� ���� ����
	
	Object nowListValue, nowRowB, nowRowC, nowRowM, nowRowE; //�̿��� �߰� : nowRowE(�μ����� ��)
	int nowListRow, nowListCol;
	int dragStartRow, dragStartCol, dragEndRow;
		
	JFrame Fr_addRoom, Fr_addSchedule;
	JTable table, table_Room;
	DefaultTableModel sche;
	MyTableModel DTM_Room;
	
	JButton bt_addRoom = new JButton("���ǽ� �߰�");
	JButton bt_delRoom = new JButton("���ǽ� ����");
	JButton bt_popAdd = new JButton("�߰�");
	JButton enterB = new JButton("�Է�");
	JButton cancelB = new JButton("���");
	JTextField tf_building, tf_room, tf_maxSeat, tf_equip; //�̿��� �߰� : tf_equip(�μ�����)
	JPanel pn_roomList = new JPanel();
	JPanel pn_listButton = new JPanel();
	JPanel pn_roomInfo = new JPanel();
	String [][] schedule_no = new String[20][20];
	String [] col_roomList = {"�ǹ�","ȣ��","�ο�","�μ�����"}; //�߰� : �μ�����
	String [][] data_roomList;
	
	
	String [] col_roomTimetable = {"�ð�","��","ȭ","��","��","��","��","��"};
	String [][] data_roomTimetable = {
			{"1A(09:00~09:30)","","","","","","",""},
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
			{"9B(05:30~06:00)","","","","","","",""}
			};
	
	JRadioButton tableJRadioButton;
	JRadioButton eventJRadioButton;
	JLabel lb_name, lb_code, lb_prof;
	JComboBox cb_prof, cb_prof_id, dateC, timeC1, timeC2;
	JTextField tf_name, tf_code;
	int regularOrEtc = 0; //���Խð�ǥ���� Ư�� �̺�Ʈ ���������� Ȯ���ϴ� ����
	Enums enums = new Enums();
	
	public GUI_ClassRoomList(Connection conn){
		this.conn = conn;
		ClassRoomList.setConn(conn);
	
		/*********�������� ��� ���ǽ� ���� ���� ��� �߰�**********/
		if(Info.getType().equals("A"))
			admin = new User_Admin(conn);	
		
		/*************���� ����Ʈ***************/
		setLayout(new BorderLayout());
		pn_roomList.setBorder(new TitledBorder("���ǽ� List"));
		pn_roomList.setLayout(new BorderLayout());
		
		sche = new DefaultTableModel(data_roomList, col_roomList);
		
		table = new JTable(sche);
		table.getTableHeader().setReorderingAllowed(false); //�̿��� �߰� : ��� �巡�� �Ұ�
		table.getTableHeader().setResizingAllowed(false); //�̿��� �߰� : ��� ũ�� ���� �Ұ�
		JScrollPane sp = new JScrollPane(table);
		listRoom();
					
		pn_listButton.add(bt_addRoom, "North");
		pn_listButton.add(bt_delRoom, "North");
		
		/********�������� ��� ���ǽ� ���� ���� ��ư �߰�***************/
		if(Info.getType().equals("A"))
			pn_roomList.add(pn_listButton, "North");
		
		pn_roomList.add(sp, "Center");
		add(pn_roomList,"East");
		pn_roomList.setPreferredSize(new java.awt.Dimension(200, 1));
		
		bt_addRoom.addActionListener(new adminListener());
		bt_delRoom.addActionListener(new adminListener());
		table.addMouseListener(this);
		
		/************* ���� ���ǽ� info ************/
		
		pn_roomInfo.setBorder(new TitledBorder("���ǽ� �ð�ǥ"));
		pn_roomInfo.setLayout(new BoxLayout(pn_roomInfo, BoxLayout.Y_AXIS));
		
		/********************************************/
		JPanel scheP = new JPanel();
		DTM_Room = new MyTableModel(data_roomTimetable, col_roomTimetable);
		table_Room = new JTable(DTM_Room);
		
		/*****************�̿��� �߰� : ���̺� ��� ���� �Ұ�*********************/		
		table_Room.getTableHeader().setReorderingAllowed(false);
		table_Room.getTableHeader().setResizingAllowed(false);
		
		JScrollPane sp_RoomTimetalbe = new JScrollPane(table_Room);
		
		sp_RoomTimetalbe.setPreferredSize(new java.awt.Dimension(650, 500));
		table_Room.setColumnSelectionAllowed(true);
		table_Room.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_Room.setRowHeight(35);
		TableColumn column = table_Room.getColumnModel().getColumn(0);
		column.setPreferredWidth(30);
		table_Room.getColumn("�ð�").setPreferredWidth(95);
		
		/**********�������� ��� ���� �����ϰ� ������ ���***********/
		if(Info.getType().equals("A"))
			table_Room.addMouseListener(this);
		
		scheP.add("Center",sp_RoomTimetalbe);
					
		/********************************************/
		JPanel buttonP = new JPanel();
		JButton okJButton = new JButton("Ȯ��");
		buttonP.add("South", okJButton);
		
		pn_roomInfo.add(scheP);
		pn_roomInfo.add(buttonP);		
		
		add(pn_roomInfo,"Center");		
	}
	
	/***********���ο� ���ǽ� ���� �߰�********************/
	public void addRoomPop(){
		/**ó������ �����ϴ� ���ǽ� �߰� �����Ӹ� ���� ����**/
		if(Fr_addRoom == null){
			Fr_addRoom = new JFrame("���ǽ� �߰�");
			
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder("���ǽ� �߰�"));
			panel.setLayout(new FlowLayout());
			
			/******************************************/
			JLabel buildL = new JLabel("�ǹ�");
			tf_building = new JTextField(5);
			
			panel.add(buildL);
			panel.add(tf_building);
			
			/******************************************/
			JLabel roomL = new JLabel("ȣ��");
			tf_room = new JTextField(5);
			
			panel.add(roomL);
			panel.add(tf_room);
		
			/*****************************************/
			JLabel seatL = new JLabel("�����ο�");
			tf_maxSeat = new JTextField(5);
			
			panel.add(seatL);
			panel.add(tf_maxSeat);
			
			/*****************************************/
			
			panel.add(bt_popAdd);
			bt_popAdd.addActionListener(new adminListener());
			
			/*****************************************/
			//�߰� : �μ����� ��
			JLabel equipL = new JLabel("�μ�����");	
			panel.add(equipL);
			
			tf_equip = new JTextField(20);
			panel.add(tf_equip);						

			/*****************************************/

			Fr_addRoom.add(panel);
			Fr_addRoom.setSize(420, 120);
			Fr_addRoom.setLocationRelativeTo(pn_listButton);
		}
		Fr_addRoom.setVisible(true);
	}
	
	/*****************�� ������ �Է�************************/
	public void addSchedulePop(int dayIndex, int sTimeIndex, int eTimeIndex)
	{
		Fr_addSchedule = new JFrame("������ �߰�");
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		/*****************************************/
		JPanel panel1 = new JPanel();
		panel1.setBorder(new TitledBorder("�Է��� ���� ����"));
		tableJRadioButton = new JRadioButton("���Խð�ǥ",true);
		eventJRadioButton = new JRadioButton("��Ÿ ����",false);
		tableJRadioButton.addMouseListener(this);
		eventJRadioButton.addMouseListener(this);
		
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(tableJRadioButton);
		radioGroup.add(eventJRadioButton);
		
		panel1.add(tableJRadioButton);
		panel1.add(eventJRadioButton);
		
		/*****************************************/
		JPanel dateP = new JPanel();
		JLabel dateL = new JLabel("����");
		String[] dates = {"��","ȭ","��","��","��","��","��"};
		dateC = new JComboBox(dates);
		dateC.setSelectedIndex(dayIndex-1);
		dateC.setMaximumRowCount(3);
		
		dateP.add(dateL);
		dateP.add(dateC);
		
		JLabel timeL = new JLabel("�ð�");
		String[] Time = {"09:00","09:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00"};
		timeC1 = new JComboBox(Time);
		timeC1.setSelectedIndex(sTimeIndex);
		timeC2 = new JComboBox(Time);
		timeC2.setSelectedIndex(eTimeIndex+1);
		
		dateP.add(timeL);
		dateP.add(timeC1);
		dateP.add(timeC2);
		
		/*****************************************/
		JPanel scheP = new JPanel();
		lb_code = new JLabel("�����ڵ�");
		tf_code = new JTextField(8);
		
		scheP.add(lb_code);
		scheP.add(tf_code);
		
		lb_name = new JLabel("�����");
		tf_name = new JTextField(10);
		
		scheP.add(lb_name);
		scheP.add(tf_name);
		
		lb_prof = new JLabel("��米��");
		cb_prof = new JComboBox();
		cb_prof_id = new JComboBox();
		
		/*************���� ��� �ҷ�����***************/
		ArrayList nameAndIdList = ClassRoomList.getProfessorList();
		ArrayList<String> nameList = (ArrayList)nameAndIdList.get(0);
		ArrayList<String> idList = (ArrayList)nameAndIdList.get(1);
		for(int i=0; i<nameList.size(); i++){
			cb_prof.addItem(nameList.get(i));
			cb_prof_id.addItem(idList.get(i));			
		}
		
		scheP.add(lb_prof);
		scheP.add(cb_prof);
		
		/******************************************/
		JPanel buttonP = new JPanel();
		enterB.addActionListener(new adminListener()); 
		cancelB.addActionListener(new adminListener());
		
		buttonP.add(enterB);
		buttonP.add(cancelB);
		
		/******************************************/
		panel.add(panel1);
		panel.add(dateP);
		panel.add(scheP);
		panel.add(buttonP);
		
		Fr_addSchedule.add(panel);
		Fr_addSchedule.setSize(450, 250);
		Fr_addSchedule.setVisible(true);
	}
	
	/************���ǽ� ��� ���****************/
	public void listRoom() {
		Statement scheduleState;
		ResultSet scheduleResult;
		System.out.println("listRoom");
		
		Vector vRoomListHead = new Vector();
		vRoomListHead.addElement("�ǹ�");
		vRoomListHead.addElement("ȣ��");	
		vRoomListHead.addElement("�ο�");
		vRoomListHead.addElement("�μ�����"); //�߰� : �μ�����	
		Vector vRoomList = new Vector();
		Vector vRoomListCol;

		/*************���ǽ� ��� �ҷ�����****************/
		ArrayList classRoomList = ClassRoomList.getClassRoomList();
		for(int i=0; i<classRoomList.size(); i++){			
			String[] classInfo = (String[]) classRoomList.get(i);
			vRoomListCol = new Vector();
			vRoomListCol.addElement(classInfo[0]);
			vRoomListCol.addElement(classInfo[1]);		
			vRoomListCol.addElement(classInfo[2]);
			vRoomListCol.addElement(classInfo[3]); //�߰� : �μ�����
			vRoomList.addElement(vRoomListCol);
		}		
		DefaultTableModel DTM2 = new DefaultTableModel(vRoomList, vRoomListHead);
		table.setModel(DTM2);
		
		/*********************�̿��� �߰� : ���̺� ��� ���� �Ұ�********************/
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		
		table.getColumnModel().getColumn(0).setPreferredWidth(45);
		table.getColumnModel().getColumn(1).setPreferredWidth(45);
		table.getColumnModel().getColumn(2).setPreferredWidth(45);
		table.getColumnModel().getColumn(3).setPreferredWidth(150); //�߰� : �μ������� ���� ����
		table.getModel().addTableModelListener(new ChangeListener());
		table.revalidate();
		table.repaint();
	}
	
	public void InitializeTable(String location, String roomNo) {
		Statement scheduleState, blockState;
		ResultSet scheduleResult, blockResult;
	
		int row;
		int col;
		int i =0;
		
		MyTableModel DTM_Room2 = new MyTableModel(data_roomTimetable, col_roomTimetable);
		table_Room.setModel(DTM_Room2);
		/**********************�̿��� �߰� : ���̺� ��� ���� �Ұ�**********************/
		table_Room.getTableHeader().setReorderingAllowed(false);
		table_Room.getTableHeader().setResizingAllowed(false);
		
		table_Room.setColumnSelectionAllowed(true);
		table_Room.setRowHeight(35);
		TableColumn column = table_Room.getColumnModel().getColumn(0);
		column.setPreferredWidth(30);
		table_Room.getColumn("�ð�").setPreferredWidth(95);
		tableCellCenter(table_Room);

		table_Room.revalidate();
		//table_Room.repaint();
		schedule_no = new String[20][20];
		
		try {
			//**GUI�� �� ���� �ƴ�
			blockState=conn.createStatement();
			blockResult = blockState.executeQuery("select * from timeblock where location='" +location+ "' and classroom='"+roomNo+"' and isAvailable='F'");
			System.out.println(location + roomNo);
			while(blockResult.next()) {
				scheduleState=conn.createStatement();
				scheduleResult = scheduleState.executeQuery("select * from schedule where no='" +blockResult.getString("scheduleNo")+ "'");
				row = enums.BlockToIndex(blockResult.getString("time"));
				col = enums.DayToIndex(blockResult.getString("day"));
				schedule_no[row][col] = blockResult.getString("scheduleNo");
				while(scheduleResult.next()) {
					System.out.println("schedule");
					table_Room.setValueAt(scheduleResult.getString("name"), row, col);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void tableCellColor(JTable t, int row, int col) // �� ���� �Ⱥ������ ��ĥ
	{
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setBackground(Color.yellow);
		TableColumnModel tcm = t.getColumnModel();
		tcm.getColumn(col).setCellRenderer(dtcr);
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
	
	public void mouseClicked(MouseEvent me) {
		if(me.getSource()==table) {
			this.nowListRow = table.getSelectedRow();
			this.nowListCol = table.getSelectedColumn();
			this.nowListValue = table.getValueAt(nowListRow, nowListCol);
			this.nowRowB = table.getValueAt(nowListRow, 0);
			this.nowRowC = table.getValueAt(nowListRow, 1);
			this.nowRowM = table.getValueAt(nowListRow, 2);
			this.nowRowE = table.getValueAt(nowListRow, 3);
			System.out.println(nowListRow+","+nowListCol+","+nowListValue.toString()+"selected");
			InitializeTable(table.getValueAt(nowListRow, 0).toString(), table.getValueAt(nowListRow, 1).toString());
			System.out.println(table.getValueAt(nowListRow, 0).toString()+","+ table.getValueAt(nowListRow, 1).toString());
		} else if(me.getSource()==table_Room) {
			if(schedule_no[table_Room.getSelectedRow()][table_Room.getSelectedColumn()]!=null)
				PopUpMenu(me, schedule_no[table_Room.getSelectedRow()][table_Room.getSelectedColumn()]);
		}
		else if(me.getSource()==tableJRadioButton)
		{
			regularOrEtc = 0;
			lb_name.setText("�����");
			lb_code.setVisible(true);
			tf_code.setVisible(true);
			lb_prof.setVisible(true);
			cb_prof.setVisible(true);
		}
		else if(me.getSource()==eventJRadioButton)
		{
			regularOrEtc = 1;
			lb_name.setText("��������");
			lb_code.setVisible(false);
			tf_code.setVisible(false);
			lb_prof.setVisible(false);
			cb_prof.setVisible(false);
		}
	}
	public void mouseEntered(MouseEvent Ee) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent pe) {
		if(pe.getSource()==table_Room) {
			this.dragStartRow = table_Room.getSelectedRow();
			this.dragStartCol = table_Room.getSelectedColumn();
			table_Room.setSelectionBackground(Color.yellow);
			System.out.print("("+dragStartRow+"��,"+dragStartCol+"��"+")");
		}
	}
	public void mouseReleased(MouseEvent re) {
		if(re.getSource()==table_Room) {
			this.dragEndRow = table_Room.getSelectedRow();
			if(dragEndRow!=dragStartRow) addSchedulePop(dragStartCol, dragStartRow, dragEndRow);			
			System.out.println("("+dragEndRow+"��,"+dragStartCol+"��"+")");
		}
	}
	public void mouseDragged(MouseEvent me){}
	
	class ChangeListener implements TableModelListener {
		public void tableChanged(TableModelEvent e) { 
	        System.out.println(table.getValueAt(nowListRow, nowListCol).toString());
	        if(!nowListValue.equals(table.getValueAt(nowListRow, nowListCol))) {
	        	if(JOptionPane.showConfirmDialog(null,"���ǽ� ������ ���� �Ͻðڽ��ϱ�?","���ǽ� ���� ����",
							JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)!=JOptionPane.NO_OPTION){
	       		 		String changedRowB = table.getValueAt(nowListRow, 0).toString();
	       		 		String changedRowC = table.getValueAt(nowListRow, 1).toString();
	       		 		String changedRowM = table.getValueAt(nowListRow, 2).toString();
	       		 		/**�̿��� �߰� : �μ����� ���� ���� ����**/
	       		 		String changedRowE = table.getValueAt(nowListRow, 3).toString();
	       		 		System.out.printf("%s %s %s %s %s %s %s",nowRowB.toString(), nowRowC.toString(), nowRowM.toString(), changedRowB, changedRowC, changedRowM, changedRowE);
	       		 		admin.SetClassRoom(nowRowB.toString(), nowRowC.toString(), nowRowM.toString(), nowRowE.toString(), changedRowB, changedRowC, changedRowM, changedRowE);
	       		 }
	        	else{
						table.setValueAt(nowListValue, nowListRow, nowListCol);
						System.out.println("����ȵ�");
						}
	        }
	    }
	}
	
	class adminListener implements ActionListener {
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == bt_addRoom) {
				System.out.println("bt_addRoom Clicked");
				addRoomPop();
			}
			
			if(e.getSource() == bt_delRoom) {
				System.out.println(" bt_delRoomClicked");
				admin.DeleteClassRoom(table.getValueAt(nowListRow, 1).toString(), table.getValueAt(nowListRow, 0).toString());
				listRoom();
			}
			
			if(e.getSource() == bt_popAdd) {
				System.out.println("bt_popAdd Clicked");
				
				String equipment = ""; //�߰� : �μ����� ���⿡ �����ؼ� ����
				equipment = tf_equip.getText();
				System.out.println("�μ����� : " + equipment);
				
				admin.CreateClassRoom(tf_room.getText(), tf_building.getText(), tf_maxSeat.getText(), equipment); 
				listRoom();
				Fr_addRoom.setVisible(false);
			}
			
			if(e.getSource() == enterB) {
				int scheNo;
				System.out.println("enterB Clicked");
				if(regularOrEtc==0){
					System.out.println("��");
					scheNo = admin.CreateLectureSchedule(tf_name.getText(), cb_prof_id.getItemAt(cb_prof.getSelectedIndex()).toString(), tf_code.getText());
				}
				else{
					System.out.println("�ٿ�");
					scheNo = admin.CreateOtherSchedule(tf_name.getText());
				}
				
				int start_time = enums.TimeToIndex(timeC1.getSelectedItem().toString());
				int end_time = enums.TimeToIndex(timeC2.getSelectedItem().toString());
				int i=start_time;
				
				System.out.println("returned schNo : "+scheNo);
				for(i=start_time; i<end_time; i++) {
					System.out.printf("%d %s %d",nowListRow, dateC.getSelectedItem().toString(), scheNo);
					admin.CreateTimeBlock(cb_prof_id.getItemAt(cb_prof.getSelectedIndex()).toString(), table.getValueAt(nowListRow, 0).toString(), 
							table.getValueAt(nowListRow, 1).toString(), dateC.getSelectedItem().toString(), enums.IndexToBlock[i], scheNo);
				}
				Fr_addSchedule.setVisible(false);
				InitializeTable(table.getValueAt(nowListRow, 0).toString(), table.getValueAt(nowListRow, 1).toString());
			}
			
			if(e.getSource() == cancelB) {
				Fr_addSchedule.setVisible(false);
			}
		}
	}
	
	public void PopUpMenu(MouseEvent ce, final String schedule){
		JPopupMenu Pmenu;
		Pmenu = new JPopupMenu();
		final JMenuItem modifySchedule = new JMenuItem("Sche No. "+schedule);
		Pmenu.add(modifySchedule);
		modifySchedule.setEnabled(false);
		final JMenuItem deleteSchedule = new JMenuItem("�����ϱ�");
		Pmenu.add(deleteSchedule);
		Pmenu.show(ce.getComponent(), ce.getX(), ce.getY());
		deleteSchedule.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(e.getSource()==deleteSchedule) {
					if(JOptionPane.showConfirmDialog(null,"������ ���� �Ͻðڽ��ϱ�?", schedule,
							JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)!=JOptionPane.NO_OPTION)
					{
						admin.DeleteSchedule(schedule);
						InitializeTable(table.getValueAt(nowListRow, 0).toString(), table.getValueAt(nowListRow, 1).toString());
					} 
				}
			}
		});
   }
}
