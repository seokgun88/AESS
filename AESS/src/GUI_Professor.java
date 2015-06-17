import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class GUI_Professor extends JTabbedPane implements MouseListener {
	private User_Professor prof;
	private Notice notice;
	private Enums enums = new Enums();
	private String lectureCode;
	
	private JButton btAdd = new JButton("�����߰�");
	private JButton btRemove = new JButton("����");
	private JTextField tfNum = new JTextField(16);
	private JTextField tfAcceptNum = new JTextField(16);
	private JTextField tfClassNum = new JTextField(16);
	private JTextField tfTestPeriod = new JTextField(16);
	private JTextArea ta_notice = new JTextArea(30, 60); //�������� �Է��� textarea
	private JTable table;
	private JTable prrior_table;
	private JTable impos_tbList;
	private JTable table5;
	private JComboBox cbPrrior;
	private JComboBox cbDay;
	private JComboBox cbStarttime;
	private JComboBox cbEndtime;
	private JComboBox impos_cbDay;
	private JComboBox impos_cbStarttime;
	private JComboBox impos_cbEndtime;
	private int[][] schedule_no = new int[20][20];

	public GUI_Professor(String id, String lectureCode){
		this.lectureCode = lectureCode; //���� �ڵ� �ʱ�ȭ
		prof = new User_Professor(id);
		notice = new Notice();
		JPanel pn_TestInfo = new JPanel(); //���� ���	
		
		BoxLayout TestInfo_box  = new BoxLayout(pn_TestInfo, BoxLayout.Y_AXIS);	//�ڽ� ���̾ƿ�
		pn_TestInfo.setLayout(TestInfo_box);		
		
		/********************�̿��� �߰� : ��������*************************/
		JPanel pn_notice = new JPanel(); //�������� �г�
		ta_notice.setText(notice.getNotice(lectureCode));
		pn_notice.add(ta_notice); //textarea �гο� ���
		JScrollPane scrollPane = new JScrollPane(ta_notice); //��ũ�� ����
		pn_notice.add(scrollPane); //��ũ�� �гο� �߰�		
		JButton bt_sendNotice = new JButton("�Է�"); //�������� �Է� ��ư
		bt_sendNotice.addActionListener(new profListener()); //�Է� ��ư ������ ���
		pn_notice.add(bt_sendNotice); //��ư �гο� ���
		addTab("��������", null, pn_notice, "�������� �Է� �� ����"); //�г��� ������ ����
		
		/******************************���� ���� / �ް�********************************/
		//�ް� ���� ����
		JPanel TestInfo_up = new JPanel();		
		TestInfo_up.setBorder(new TitledBorder("�ް� ���� ����"));
		TestInfo_up.setLayout(null);
		JRadioButton rbNotexam = new JRadioButton("����");
		JRadioButton rbExam = new JRadioButton("�ް�");
		ButtonGroup group = new ButtonGroup();
		group.add(rbNotexam);
		group.add(rbExam);
		TestInfo_up.add(rbNotexam);
		TestInfo_up.add(rbExam);
		rbNotexam.setBounds(50,70,70,30);
		rbExam.setBounds(200,70,70,30);
		rbNotexam.addActionListener(new profListener());
		rbExam.addActionListener(new profListener());
		if(prof.GetIsLecture(lectureCode).equals("F")){
			rbNotexam.setSelected(true);
		}
		else if(prof.GetIsLecture(lectureCode).equals("T")){
			rbExam.setSelected(true);
		}
		
		//���� ���� �Է�
		JPanel TestInfo_middle = new JPanel();
		TestInfo_middle.setBorder(new TitledBorder("���� ���� �Է�"));
		TestInfo_middle.setLayout(null);		
		JLabel lbAcceptNum = new JLabel("�л� �ִ� ���� �ο�");
		JLabel lbClassNum = new JLabel("���ǽ� ����");
		JLabel lbTestPeriod = new JLabel("����Ⱓ ����");
		lbAcceptNum.setBounds(39, 37, 140, 17);
		lbClassNum.setBounds(39, 60, 140, 17);
		lbTestPeriod.setBounds(39, 87, 140, 17);
		tfAcceptNum.setBounds(191, 34, 175, 23);
		tfClassNum.setBounds(191, 60, 175, 23);
		tfTestPeriod.setBounds(191, 86, 175, 23);	
		String [] require = prof.GetRequiredInfo(lectureCode);
		tfAcceptNum.setText(require[0]);
		tfClassNum.setText(require[1]);
		tfTestPeriod.setText(require[2]);
		
		JButton jsave2 = new JButton("����");//�����ư
		jsave2.addActionListener(new profListener());
		TestInfo_middle.add(lbAcceptNum);	
		TestInfo_middle.add(tfAcceptNum);
		TestInfo_middle.add(lbClassNum);
		TestInfo_middle.add(tfClassNum);
		TestInfo_middle.add(lbTestPeriod);	
		TestInfo_middle.add(tfTestPeriod);
		TestInfo_middle.add(jsave2);
		jsave2.setBounds(400,80,70,30);
		
		JPanel TestInfo_down = new JPanel();
		TestInfo_down.setPreferredSize(new Dimension(60,60));
		
		pn_TestInfo.add(TestInfo_up);
		pn_TestInfo.add(TestInfo_middle);	
		pn_TestInfo.add(TestInfo_down);
		addTab("�ް�/�������� �Է�", null, pn_TestInfo, "�ް�/�������� �Է�");			
		
		/******************************���� ���********************************/
		JPanel pn_SetAssist = new JPanel();
		JPanel pTop = new JPanel();
		btAdd.addActionListener(new profListener());
		btRemove.addActionListener(new profListener());
		pTop.add(new JLabel("����"));					
		pTop.add(tfNum);
		pTop.add(btAdd);
		pTop.add(btRemove);
		pn_SetAssist.add("North",pTop);
		
		Vector vAssiHead = new Vector();
		vAssiHead.addElement("�й�");
		vAssiHead.addElement("�̸�");		
		Vector vAssi = prof.GetAssistant(lectureCode);

		table = new JTable(new DefaultTableModel(vAssi, vAssiHead));
		JScrollPane sp = new JScrollPane(table);
		JCheckBox checkBox = new JCheckBox();			
		checkBox.setHorizontalAlignment(JLabel.CENTER);
		pn_SetAssist.add("Center",sp);
		table.setPreferredScrollableViewportSize(new Dimension(700,1000));
		addTab("�������ϱ�", null, pn_SetAssist, "���� ���");
		
		/******************************������ ��Ȳ*******************************/
		Vector vStdchkHead = new Vector();
		
		JPanel panel3 = new JPanel();//3��° ��
		JPanel pTop3 = new JPanel();
		
		vStdchkHead.addElement("�й�");
		vStdchkHead.addElement("�̸�");
		vStdchkHead.addElement("�ð�ǥ�Է�");
		
		panel3.add("North",pTop3);
		JTable table2 = new JTable(prof.CheckStudentSchedule(lectureCode), vStdchkHead);		
		JScrollPane sp2 = new JScrollPane(table2);
		panel3.add("Center",sp2);
		table2.setPreferredScrollableViewportSize(new Dimension(700,1000));				
		addTab("��������Ȳ", null, panel3, "������ ��Ȳ");
		
		/******************************�ð� ����********************************/
		JPanel pn_TestTime = new JPanel();		
		BoxLayout layout1 = new BoxLayout(pn_TestTime, BoxLayout.Y_AXIS);	
		pn_TestTime.setLayout(layout1);
		
		//��ȣ �ð� ���
		JPanel TestTime_up = new JPanel();
		TestTime_up.setBorder(new TitledBorder("��ȣ �ð� ���"));
		TestTime_up.setLayout(null);
		JLabel prrior_lbPrrior = new JLabel("�켱����");		
		prrior_lbPrrior.setBounds(28, 40, 70, 17);		
		Integer [] prrior= {1, 2, 3 ,4, 5};
		cbPrrior = new JComboBox(prrior);
		cbPrrior.setBounds(80, 40, 38, 17);
		cbPrrior.setMaximumRowCount(5);		
		JLabel lbDate = new JLabel("����");
		lbDate.setBounds(130, 40, 38, 17);		
		
		String [] days = {"��", "��", "ȭ", "��", "��", "��", "��"};
		cbDay = new JComboBox(days);
		cbDay.setBounds(160, 40, 38, 17);
		cbDay.setMaximumRowCount(7);		
		JLabel lbTime = new JLabel("�ð�");
		lbTime.setBounds(220, 40, 38, 17);		
		
		String [] time = {"09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", 
				"14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30"};
		cbStarttime = new JComboBox(time);
		cbStarttime.setBounds(250, 40, 55, 17);
		cbStarttime.setMaximumRowCount(9);		
		JLabel lbwave = new JLabel("~");	
		lbwave.setBounds(317, 40, 38, 17);		
		cbEndtime = new JComboBox(time);
		cbEndtime.setBounds(337, 40, 55, 17);
		cbEndtime.setMaximumRowCount(9);			
		JButton btAdd1 = new JButton("��ȣ�ð��߰�");
		btAdd1.addActionListener(new profListener());
		btAdd1.setBounds(397, 37, 120, 25);		

		Vector vPtimeHead = new Vector();
		vPtimeHead.addElement("�켱����");
		vPtimeHead.addElement("����");
		vPtimeHead.addElement("���۽ð�");
		vPtimeHead.addElement("���ð�");
		
		prrior_table = new JTable(prof.GetPreferredTime(lectureCode), vPtimeHead);//prrior_Table �߰� 
		JScrollPane prrior_sp = new JScrollPane(prrior_table);
		JCheckBox checkBox2 = new JCheckBox();		
		checkBox2.setHorizontalAlignment(JLabel.CENTER);
		prrior_sp.setBounds(28, 70, 700, 135);		
		JButton prrior_btDelete = new JButton("��ȣ�ð�����");
		prrior_btDelete.addActionListener(new profListener());
		prrior_btDelete.setBounds(28, 230, 120, 28);		
		TestTime_up.add(prrior_lbPrrior);
		TestTime_up.add(cbPrrior);
		TestTime_up.add(lbDate);
		TestTime_up.add(cbDay);
		TestTime_up.add(lbTime);
		TestTime_up.add(cbStarttime);
		TestTime_up.add(lbwave);
		TestTime_up.add(cbEndtime);
		TestTime_up.add(btAdd1);
		TestTime_up.add(prrior_sp);
		TestTime_up.add(prrior_btDelete);			
		
		//�Ұ��� �ð� ���
		JPanel TestTime_down = new JPanel();
		TestTime_down.setBorder(new TitledBorder("�Ұ��� �ð� ���"));
		TestTime_down.setLayout(null);		
		JLabel impos_lbDate = new JLabel("����");
		impos_lbDate.setBounds(130, 40, 38, 17);		
		
		impos_cbDay = new JComboBox(days);
		impos_cbDay.setBounds(160, 40, 38, 17);
		impos_cbDay.setMaximumRowCount(7);		
		JLabel impos_lbTime = new JLabel("�ð�");
		impos_lbTime.setBounds(220, 40, 38, 17);		
		impos_cbStarttime = new JComboBox(time);
		impos_cbStarttime.setBounds(250, 40, 55, 17);
		impos_cbStarttime.setMaximumRowCount(9);		
		JLabel impos_lbwave = new JLabel("~");	
		impos_lbwave.setBounds(317, 40, 38, 17);		
		impos_cbEndtime = new JComboBox(time);
		impos_cbEndtime.setBounds(337, 40, 55, 17);
		impos_cbEndtime.setMaximumRowCount(9);			
		JButton impos_btAdd1 = new JButton("�߰�");
		impos_btAdd1.addActionListener(new profListener());		
		impos_btAdd1.setBounds(397, 37, 60, 25);		
		
		Vector vImpossibleHead = new Vector();
		vImpossibleHead.addElement("����");
		vImpossibleHead.addElement("���۽ð�");
		vImpossibleHead.addElement("���ð�");
		
		impos_tbList = new JTable(prof.GetImpossibleTime(lectureCode), vImpossibleHead); 		
		JScrollPane impos_tbScroll = new JScrollPane(impos_tbList);
		JCheckBox checkBox3 = new JCheckBox();		
		//impos_tbList.getColumn("ChkBox2").setCellRenderer(d);		
		checkBox3.setHorizontalAlignment(JLabel.CENTER);
		//impos_tbList.getColumn("ChkBox2").setCellEditor(new DefaultCellEditor(checkBox));		
		impos_tbScroll.setBounds(28, 70, 600, 120);		
		JButton impos_btDelete = new JButton("�Ұ��ɽð�����");
		impos_btDelete.addActionListener(new profListener());
		impos_btDelete.setBounds(28, 220, 130, 28);		
		TestTime_down.add(impos_lbDate);
		TestTime_down.add(impos_cbDay);
		TestTime_down.add(impos_lbTime);
		TestTime_down.add(impos_cbStarttime);
		TestTime_down.add(impos_lbwave);
		TestTime_down.add(impos_cbEndtime);
		TestTime_down.add(impos_btAdd1);
		TestTime_down.add(impos_tbScroll);
		TestTime_down.add(impos_btDelete);
		
		pn_TestTime.add(TestTime_up);
		pn_TestTime.add(TestTime_down);		
		addTab("���� �ð� ����", null, pn_TestTime, "���� �ð� ����");	
		
		/******************************���� Ȯ��********************************/
		Vector vPossibleHead = new Vector();
		vPossibleHead.addElement("�ð�");
		vPossibleHead.addElement("��");
		vPossibleHead.addElement("ȭ");
		vPossibleHead.addElement("��");
		vPossibleHead.addElement("��");
		vPossibleHead.addElement("��");
		vPossibleHead.addElement("��");
		vPossibleHead.addElement("��");

		Vector vPossible = new Vector();
		Vector vPossibleCol;
		for(int i=0; i<18; i++){
			vPossibleCol = new Vector();
			vPossibleCol.addElement(i%2==0?(i/2)+1+"A":(i/2)+1+"B");
			vPossibleCol.addElement("");
			vPossibleCol.addElement("");
			vPossibleCol.addElement("");
			vPossibleCol.addElement("");
			vPossibleCol.addElement("");
			vPossibleCol.addElement("");
			vPossibleCol.addElement("");
			vPossible.addElement(vPossibleCol);
		}
		table5 = new JTable(vPossible, vPossibleHead);
		JPanel panel5 = new JPanel();
		panel5.setLayout(new BorderLayout());
		JScrollPane sp5 = new JScrollPane(table5);
		panel5.add("Center",sp5);		
		JButton btFinal = new JButton("���ɽð�Ȯ��");
		btFinal.addActionListener(new profListener());
		panel5.add("South", btFinal);
		addTab("����Ȯ��", null, panel5, "���� ���� Ȯ��");	
		tableCellCenter(table5);
		table5.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table5.addMouseListener(this);
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
	
	DefaultTableCellRenderer d = new DefaultTableCellRenderer(){
		public Component getTableCellRendererComponent(
	                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		JCheckBox checkBox= new JCheckBox();
		checkBox.setSelected(((Boolean)value).booleanValue());
		checkBox.setHorizontalAlignment(JLabel.CENTER);
		return checkBox;

	        }
	};

	public void listPrefer(){
		Vector vPreferHead = new Vector();
		vPreferHead.addElement("�켱����");
		vPreferHead.addElement("����");
		vPreferHead.addElement("���۽ð�");
		vPreferHead.addElement("���ð�");
		
		DefaultTableModel DTM = new DefaultTableModel(prof.GetPreferredTime(lectureCode), vPreferHead);
		prrior_table.setModel(DTM);
		prrior_table.revalidate();
		prrior_table.repaint();
	}
	public void listAssi() {
		Vector vAssiHead = new Vector();
		vAssiHead.addElement("�й�");
		vAssiHead.addElement("�̸�");	

		DefaultTableModel DTM = new DefaultTableModel(prof.GetAssistant(lectureCode), vAssiHead);
		table.setModel(DTM);
		table.revalidate();
		table.repaint();
	}
	
	public void listImpossible(){
		Vector vImpossibleHead = new Vector();
		vImpossibleHead.addElement("����");
		vImpossibleHead.addElement("���۽ð�");
		vImpossibleHead.addElement("���ð�");
		
		DefaultTableModel DTM = new DefaultTableModel(prof.GetImpossibleTime(lectureCode), vImpossibleHead);
		impos_tbList.setModel(DTM);
		impos_tbList.revalidate();
		impos_tbList.repaint();
	}
	
	class profListener implements ActionListener {
		public void actionPerformed(ActionEvent e)
		{
			String menu = e.getActionCommand();	
			
			if(menu.equals("�����߰�")) {
				prof.SetAssistant(tfNum.getText(), lectureCode);
				listAssi();
				System.out.println("Clicked");
			}
			else if(menu.equals("����")){
				prof.DeleteAssistant(tfNum.getText());
				listAssi();
				System.out.println("Clicked2");
			}			
			else if(menu.equals("����")){
				prof.SetIsLecture(lectureCode, "F");
			}
			else if(menu.equals("�ް�")){
				prof.SetIsLecture(lectureCode, "T");
			}
			else if(menu.equals("����")){
				prof.SetRequiredInfo(lectureCode, Integer.parseInt(tfAcceptNum.getText()), 
						Integer.parseInt(tfClassNum.getText()), Integer.parseInt(tfTestPeriod.getText()));
				System.out.println("����");
			}
			else if(menu.equals("��ȣ�ð��߰�")){
				int rank= (int) cbPrrior.getSelectedItem();
				String day=(String) cbDay.getSelectedItem();
				String stime=(String) cbStarttime.getSelectedItem();
				String etime=(String) cbEndtime.getSelectedItem();
				String time = stime;	
				
				/**�̿��� �߰� : ���� �ð��� ���� �ð����� ���� ��쿡�� �߰�**/
				if(Enums.TimeToIndex(stime)<Enums.TimeToIndex(etime)){	
					prof.checkOverlapPreferredTime(rank, lectureCode);
					while(true){
						if(time.equals(etime))
							break;
						prof.SetPreferredTime(rank, day, time, lectureCode);
						if(time.substring(3).equals("00")){
							time = time.substring(0, 2) + ":30";
						}
						else{
							time = (Integer.parseInt(time.substring(0, 2))+1) + ":00";
						}
					}
					listPrefer();
					System.out.println("��ȣ�ð��߰�");
				}
			}
			else if(menu.equals("��ȣ�ð�����")){
				int rank=Integer.parseInt(prrior_table.getValueAt(prrior_table.getSelectedRow(), 0).toString());
				prof.DelPreferredTime(rank, lectureCode);
				listPrefer();
				System.out.println(rank);
			}
			else if(menu.equals("�߰�")){
				String day=(String) impos_cbDay.getSelectedItem();
				String stime=(String) impos_cbStarttime.getSelectedItem();
				String etime=(String) impos_cbEndtime.getSelectedItem();
				String time;
				
				time = stime;
				int rank = enums.TimeToRank(day, stime);
				while(true){
					if(stime.equals(etime)){
						prof.SetImpossibleTime(rank, day, time, lectureCode);
						break;
					}
					else if(time.equals(etime)){
						break;
					}
					System.out.println(time);
					prof.SetImpossibleTime(rank, day, time, lectureCode);
					if(time.substring(3).equals("00")){
						time = time.substring(0, 2) + ":30";
					}
					else{
						time = (Integer.parseInt(time.substring(0, 2))+1) + ":00";
					}
				}
				listImpossible();
				System.out.println("�Ұ����߰�");
			}
			else if(menu.equals("�Ұ��ɽð�����")){
				prof.DelImpossibleTime(impos_tbList.getValueAt(impos_tbList.getSelectedRow(), 0).toString(), 
						impos_tbList.getValueAt(impos_tbList.getSelectedRow(), 1).toString(), 
						impos_tbList.getValueAt(impos_tbList.getSelectedRow(), 2).toString(), 
						lectureCode);
				listImpossible();
				System.out.println("�Ұ��ɻ���");
			}
			else if(menu.equals("���ɽð�Ȯ��")){
				schedule_no = new int[20][20];
				int [] possibleRank = prof.SetPossibleTime(lectureCode);
				Vector vPossibletime = prof.GetPreferredTime(lectureCode);
				Vector vPossibleRank;
				int start, end, day, cnt=0;
				for(int i=0; i<5; i++){
					if(possibleRank[i] != 0 && possibleRank[i] != -1){
						vPossibleRank = (Vector) vPossibletime.get(cnt);
						day = enums.DayToIndex((String) vPossibleRank.get(1));
						start = enums.TimeToIndex((String) vPossibleRank.get(2));
						end = enums.TimeToIndex((String) vPossibleRank.get(3));
						for(int j=start; j<=end; j++){
							schedule_no[j][day] = i+1;
							table5.setValueAt("��ȣ�ð� "+(i+1), j, day);
						}
					}
					if(possibleRank[i] != -1){
						cnt++;
					}
				}
			}
			else if(menu.equals("�Է�")){ //�̿��� �߰� : �������� �Է� ��ư �׼�
				JOptionPane.showMessageDialog(null, "�������� �Է� �Ϸ�", "�������� �Է�", JOptionPane.INFORMATION_MESSAGE);
				prof.SetNotice(lectureCode, ta_notice.getText());
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = table5.getSelectedRow();
		int column = table5.getSelectedColumn();
		if(schedule_no[row][column]>0) {
			int rank = schedule_no[row][column];
			int vRank;
			String day="", start="", end="";
			Vector room = prof.FindClassroom(lectureCode, rank);
			Vector timeinfo = prof.GetPreferredTime(lectureCode);
			Vector time;
			for(int i=0; i<timeinfo.size(); i++){
				time = (Vector) timeinfo.get(i);
				vRank = (int) time.get(0);
				if(vRank == rank){
					day = (String) time.get(1);
					start = (String) time.get(2);
					end = (String) time.get(3);
					break;
				}
			}
			String no = prof.SetFinal(lectureCode, rank);
			String message = lectureCode+"������ ���� �Ⱓ�� Ȯ�� �Ǿ����ϴ�.";
			message += '\n' + day +"����, ���۽ð� "+ start +", ���ð� " +end; 
			prof.DelPossibleToFinal(lectureCode);
			for(int i=0; i<room.size(); i++){
				message += "\n" + room.get(i);
				String Room = (String)room.get(i);
				String [] sRoom = Room.split(" ");
				prof.SetFinalClassSchedule(lectureCode, day, enums.TimeToBlock(start), enums.TimeToBlock(end), sRoom[0], sRoom[1]);
			}
			JOptionPane.showMessageDialog(null, message); 
		}
		System.out.println(row);
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub		
	}
}

