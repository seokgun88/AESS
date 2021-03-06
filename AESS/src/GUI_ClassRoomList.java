import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
/**GUI for Room list and Schedule**/
public class GUI_ClassRoomList extends JPanel implements MouseListener{
	private User_Admin admin; //Admin can edit room info
	
	private Object nowListValue, nowRowB, nowRowC, nowRowM, nowRowE; //이영석 추가 : nowRowE(부수기재 열)
	private int nowListRow, nowListCol;
	private int dragStartRow, dragStartCol, dragEndRow;
		
	private JFrame Fr_addRoom, Fr_addSchedule;
	private JTable table, table_Room;
	private DefaultTableModel sche;
	private MyTableModel DTM_Room;
	
	private JButton bt_addRoom = new JButton("강의실 추가");
	private JButton bt_delRoom = new JButton("강의실 삭제");
	private JButton bt_popAdd = new JButton("추가");
	private JButton enterB = new JButton("입력");
	private JButton cancelB = new JButton("취소");
	private JTextField tf_building, tf_room, tf_maxSeat, tf_equip; //이영석 추가 : tf_equip(부수기재)
	private JPanel pn_roomList = new JPanel();
	private JPanel pn_listButton = new JPanel();
	private JPanel pn_roomInfo = new JPanel();
	private String [][] schedule_no = new String[20][20];
	private String [] col_roomList = {"건물","호수","인원","부수기재"}; //추가 : 부수기재
	private String [][] data_roomList;
	
	
	private String [] col_roomTimetable = {"시간","월","화","수","목","금","토","일"};
	private String [][] data_roomTimetable = {
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
	
	private JRadioButton tableJRadioButton;
	private JRadioButton eventJRadioButton;
	private JLabel lb_name, lb_code, lb_prof;
	private JComboBox cb_prof, cb_prof_id, dateC, timeC1, timeC2;
	private JTextField tf_name, tf_code;
	private int regularOrEtc = 0; //정규시간표인지 특별 이벤트 스케쥴인지 확인하는 변수
	private Enums enums = new Enums();
	
	/**Constructor**/
	public GUI_ClassRoomList(){
		ClassRoomList.setConn(Info.getConn());
	
		/*********Add features to edit room info for admin**********/
		if(Info.getType().equals("A"))
			admin = new User_Admin();	
		
		/*************List on right***************/
		setLayout(new BorderLayout());
		pn_roomList.setBorder(new TitledBorder("강의실 List"));
		pn_roomList.setLayout(new BorderLayout());
		
		sche = new DefaultTableModel(data_roomList, col_roomList);
		
		table = new JTable(sche);
		table.getTableHeader().setReorderingAllowed(false); //이영석 추가 : 헤더 드래그 불가
		table.getTableHeader().setResizingAllowed(false); //이영석 추가 : 헤더 크기 수정 불가
		JScrollPane sp = new JScrollPane(table);
		listRoom();
					
		pn_listButton.add(bt_addRoom, "North");
		pn_listButton.add(bt_delRoom, "North");
		
		/********Add edit room info button for admin***************/
		if(Info.getType().equals("A"))
			pn_roomList.add(pn_listButton, "North");
		
		pn_roomList.add(sp, "Center");
		add(pn_roomList,"East");
		pn_roomList.setPreferredSize(new java.awt.Dimension(200, 1));
		
		bt_addRoom.addActionListener(new adminListener());
		bt_delRoom.addActionListener(new adminListener());
		table.addMouseListener(this);
		
		/************* Main room info ************/
		
		pn_roomInfo.setBorder(new TitledBorder("강의실 시간표"));
		pn_roomInfo.setLayout(new BoxLayout(pn_roomInfo, BoxLayout.Y_AXIS));
		
		/********************************************/
		JPanel scheP = new JPanel();
		DTM_Room = new MyTableModel(data_roomTimetable, col_roomTimetable);
		table_Room = new JTable(DTM_Room);
		
		/*****************이영석 추가 : 테이블 헤더 수정 불가*********************/		
		table_Room.getTableHeader().setReorderingAllowed(false);
		table_Room.getTableHeader().setResizingAllowed(false);
		
		JScrollPane sp_RoomTimetalbe = new JScrollPane(table_Room);
		
		sp_RoomTimetalbe.setPreferredSize(new java.awt.Dimension(650, 500));
		table_Room.setColumnSelectionAllowed(true);
		table_Room.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_Room.setRowHeight(35);
		TableColumn column = table_Room.getColumnModel().getColumn(0);
		column.setPreferredWidth(30);
		table_Room.getColumn("시간").setPreferredWidth(95);
		
		/**********Add listener for admin to allow edit***********/
		if(Info.getType().equals("A"))
			table_Room.addMouseListener(this);
		
		scheP.add("Center",sp_RoomTimetalbe);
					
		/********************************************/
		JPanel buttonP = new JPanel();
		JButton okJButton = new JButton("확인");
		buttonP.add("South", okJButton);
		
		pn_roomInfo.add(scheP);
		pn_roomInfo.add(buttonP);		
		
		add(pn_roomInfo,"Center");		
	}
	
	/***********Add new room info********************/
	public void addRoomPop(){
		/**Generate new frame for add room for once**/
		if(Fr_addRoom == null){
			Fr_addRoom = new JFrame("강의실 추가");
			
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder("강의실 추가"));
			panel.setLayout(new FlowLayout());
			
			/******************************************/
			JLabel buildL = new JLabel("건물");
			tf_building = new JTextField(5);
			
			panel.add(buildL);
			panel.add(tf_building);
			
			/******************************************/
			JLabel roomL = new JLabel("호수");
			tf_room = new JTextField(5);
			
			panel.add(roomL);
			panel.add(tf_room);
		
			/*****************************************/
			JLabel seatL = new JLabel("수용인원");
			tf_maxSeat = new JTextField(5);
			
			panel.add(seatL);
			panel.add(tf_maxSeat);
			
			/*****************************************/
			
			panel.add(bt_popAdd);
			bt_popAdd.addActionListener(new adminListener());
			
			/*****************************************/
			//추가 : 부수기재 란
			JLabel equipL = new JLabel("부수기재");	
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
	
	/*****************Input new schedule************************/
	public void addSchedulePop(int dayIndex, int sTimeIndex, int eTimeIndex)
	{
		Fr_addSchedule = new JFrame("스케쥴 추가");
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		/*****************************************/
		JPanel panel1 = new JPanel();
		panel1.setBorder(new TitledBorder("입력할 정보 선택"));
		tableJRadioButton = new JRadioButton("정규시간표",true);
		eventJRadioButton = new JRadioButton("기타 일정",false);
		tableJRadioButton.addMouseListener(this);
		eventJRadioButton.addMouseListener(this);
		
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(tableJRadioButton);
		radioGroup.add(eventJRadioButton);
		
		panel1.add(tableJRadioButton);
		panel1.add(eventJRadioButton);
		
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
		lb_code = new JLabel("과목코드");
		tf_code = new JTextField(8);
		
		scheP.add(lb_code);
		scheP.add(tf_code);
		
		lb_name = new JLabel("과목명");
		tf_name = new JTextField(10);
		
		scheP.add(lb_name);
		scheP.add(tf_name);
		
		lb_prof = new JLabel("담당교수");
		cb_prof = new JComboBox();
		cb_prof_id = new JComboBox();
		
		/*************Load professor list***************/
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
	
	/************Print room list****************/
	public void listRoom() {
		Statement scheduleState;
		ResultSet scheduleResult;
		System.out.println("listRoom");
		
		Vector vRoomListHead = new Vector();
		vRoomListHead.addElement("건물");
		vRoomListHead.addElement("호수");	
		vRoomListHead.addElement("인원");
		vRoomListHead.addElement("부수기재"); //추가 : 부수기재	
		Vector vRoomList = new Vector();
		Vector vRoomListCol;

		/*************Load room list****************/
		ArrayList classRoomList = ClassRoomList.getClassRoomList();
		for(int i=0; i<classRoomList.size(); i++){			
			String[] classInfo = (String[]) classRoomList.get(i);
			vRoomListCol = new Vector();
			vRoomListCol.addElement(classInfo[0]);
			vRoomListCol.addElement(classInfo[1]);		
			vRoomListCol.addElement(classInfo[2]);
			vRoomListCol.addElement(classInfo[3]); //추가 : 부수기재
			vRoomList.addElement(vRoomListCol);
		}		
		DefaultTableModel DTM2 = new DefaultTableModel(vRoomList, vRoomListHead);
		table.setModel(DTM2);
		
		/*********************이영석 추가 : 테이블 헤더 수정 불가********************/
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		
		table.getColumnModel().getColumn(0).setPreferredWidth(45);
		table.getColumnModel().getColumn(1).setPreferredWidth(45);
		table.getColumnModel().getColumn(2).setPreferredWidth(45);
		table.getColumnModel().getColumn(3).setPreferredWidth(150); //추가 : 부수기재쪽 넓이 넓힘
		table.getModel().addTableModelListener(new ChangeListener());
		table.revalidate();
		table.repaint();
	}
	
	/**Initialize table**/
	public void initializeTable(String location, String roomNo) {			
		MyTableModel DTM_Room2 = new MyTableModel(data_roomTimetable, col_roomTimetable);
		table_Room.setModel(DTM_Room2);
		/**********************이영석 추가 : 테이블 헤더 수정 불가**********************/
		table_Room.getTableHeader().setReorderingAllowed(false);
		table_Room.getTableHeader().setResizingAllowed(false);
		
		table_Room.setColumnSelectionAllowed(true);
		table_Room.setRowHeight(35);
		TableColumn column = table_Room.getColumnModel().getColumn(0);
		column.setPreferredWidth(30);
		table_Room.getColumn("시간").setPreferredWidth(95);
		tableCellCenter(table_Room);

		table_Room.revalidate();
		//table_Room.repaint();
		
		/**이영석 수정 : GUI에 있던 세부 기능을 ClassRoomList로 이동**/
		Vector v_schedule = ClassRoomList.getRoomTimetable(location, roomNo);
		schedule_no = (String[][]) v_schedule.get(0);
		Vector v_scheduleInfos = (Vector) v_schedule.get(1);
		for(int i=0; i<v_scheduleInfos.size(); i++){
			Vector v_scheduleInfo = (Vector) v_scheduleInfos.get(i);
			table_Room.setValueAt((String)v_scheduleInfo.get(0), (int)v_scheduleInfo.get(1), (int)v_scheduleInfo.get(2));			
		}				
	}
	
	/**Paint if the cell is not vacant**/
	public void tableCellColor(JTable t, int row, int col)
	{
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setBackground(Color.yellow);
		TableColumnModel tcm = t.getColumnModel();
		tcm.getColumn(col).setCellRenderer(dtcr);
	}
	
	/**Align center inside cell**/
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
			initializeTable(table.getValueAt(nowListRow, 0).toString(), table.getValueAt(nowListRow, 1).toString());
			System.out.println(table.getValueAt(nowListRow, 0).toString()+","+ table.getValueAt(nowListRow, 1).toString());
		} else if(me.getSource()==table_Room) {
			if(schedule_no[table_Room.getSelectedRow()][table_Room.getSelectedColumn()]!=null)
				popUpMenu(me, schedule_no[table_Room.getSelectedRow()][table_Room.getSelectedColumn()]);
		}
		else if(me.getSource()==tableJRadioButton)
		{
			regularOrEtc = 0;
			lb_name.setText("과목명");
			lb_code.setVisible(true);
			tf_code.setVisible(true);
			lb_prof.setVisible(true);
			cb_prof.setVisible(true);
		}
		else if(me.getSource()==eventJRadioButton)
		{
			regularOrEtc = 1;
			lb_name.setText("일정제목");
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
			System.out.print("("+dragStartRow+"행,"+dragStartCol+"열"+")");
		}
	}
	public void mouseReleased(MouseEvent re) {
		if(re.getSource()==table_Room) {
			this.dragEndRow = table_Room.getSelectedRow();
			if(dragEndRow!=dragStartRow) addSchedulePop(dragStartCol, dragStartRow, dragEndRow);			
			System.out.println("("+dragEndRow+"행,"+dragStartCol+"열"+")");
		}
	}
	public void mouseDragged(MouseEvent me){}
	
	class ChangeListener implements TableModelListener {
		public void tableChanged(TableModelEvent e) { 
	        System.out.println(table.getValueAt(nowListRow, nowListCol).toString());
	        if(!nowListValue.equals(table.getValueAt(nowListRow, nowListCol))) {
	        	if(JOptionPane.showConfirmDialog(null,"강의실 정보를 변경 하시겠습니까?","강의실 정보 변경",
							JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)!=JOptionPane.NO_OPTION){
	       		 		String changedRowB = table.getValueAt(nowListRow, 0).toString();
	       		 		String changedRowC = table.getValueAt(nowListRow, 1).toString();
	       		 		String changedRowM = table.getValueAt(nowListRow, 2).toString();
	       		 		/**이영석 추가 : 부수기재 정보 수정 가능**/
	       		 		String changedRowE = table.getValueAt(nowListRow, 3).toString();
	       		 		System.out.printf("%s %s %s %s %s %s %s",nowRowB.toString(), nowRowC.toString(), nowRowM.toString(), changedRowB, changedRowC, changedRowM, changedRowE);
	       		 		admin.setClassRoom(nowRowB.toString(), nowRowC.toString(), nowRowM.toString(), nowRowE.toString(), changedRowB, changedRowC, changedRowM, changedRowE);
	       		 }
	        	else{
						table.setValueAt(nowListValue, nowListRow, nowListCol);
						System.out.println("변경안됨");
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
				admin.deleteClassRoom(table.getValueAt(nowListRow, 1).toString(), table.getValueAt(nowListRow, 0).toString());
				listRoom();
			}
			
			if(e.getSource() == bt_popAdd) {
				System.out.println("bt_popAdd Clicked");
				
				String equipment = ""; //추가 : 부수기재 여기에 저장해서 보냄
				equipment = tf_equip.getText();
				System.out.println("부수기재 : " + equipment);
				
				admin.createClassRoom(tf_room.getText(), tf_building.getText(), tf_maxSeat.getText(), equipment); 
				listRoom();
				Fr_addRoom.setVisible(false);
			}
			
			if(e.getSource() == enterB) {
				int scheNo;
				System.out.println("enterB Clicked");
				if(regularOrEtc==0){
					System.out.println("업");
					scheNo = admin.CreateLectureSchedule(tf_name.getText(), cb_prof_id.getItemAt(cb_prof.getSelectedIndex()).toString(), tf_code.getText());
				}
				else{
					System.out.println("다운");
					scheNo = admin.createOtherSchedule(tf_name.getText());
				}
				
				int start_time = enums.TimeToIndex(timeC1.getSelectedItem().toString());
				int end_time = enums.TimeToIndex(timeC2.getSelectedItem().toString());
				int i=start_time;
				
				System.out.println("returned schNo : "+scheNo);
				for(i=start_time; i<end_time; i++) {
					System.out.printf("%d %s %d",nowListRow, dateC.getSelectedItem().toString(), scheNo);
					admin.createTimeBlock(cb_prof_id.getItemAt(cb_prof.getSelectedIndex()).toString(), table.getValueAt(nowListRow, 0).toString(), 
							table.getValueAt(nowListRow, 1).toString(), dateC.getSelectedItem().toString(), enums.IndexToBlock[i], scheNo);
				}
				Fr_addSchedule.setVisible(false);
				initializeTable(table.getValueAt(nowListRow, 0).toString(), table.getValueAt(nowListRow, 1).toString());
			}
			
			if(e.getSource() == cancelB) {
				Fr_addSchedule.setVisible(false);
			}
		}
	}
	
	/****/
	public void popUpMenu(MouseEvent ce, final String schedule){
		JPopupMenu Pmenu;
		Pmenu = new JPopupMenu();
		final JMenuItem modifySchedule = new JMenuItem("Sche No. "+schedule);
		Pmenu.add(modifySchedule);
		modifySchedule.setEnabled(false);
		final JMenuItem deleteSchedule = new JMenuItem("삭제하기");
		Pmenu.add(deleteSchedule);
		Pmenu.show(ce.getComponent(), ce.getX(), ce.getY());
		deleteSchedule.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(e.getSource()==deleteSchedule) {
					if(JOptionPane.showConfirmDialog(null,"일정을 삭제 하시겠습니까?", schedule,
							JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)!=JOptionPane.NO_OPTION)
					{
						admin.DeleteSchedule(schedule);
						initializeTable(table.getValueAt(nowListRow, 0).toString(), table.getValueAt(nowListRow, 1).toString());
					} 
				}
			}
		});
   }
}
