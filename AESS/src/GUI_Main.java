import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**���� GUI ����**/
public class GUI_Main extends JFrame implements ActionListener {
	/**DB���� ���� ����**/
	private static DBconnect dbcon = new DBconnect();
	private static Connection conn = dbcon.connect();
	
	/**********��ġ �߾�����*******/
	Toolkit tk = Toolkit.getDefaultToolkit(); 		//������ Toolkit��ü�� ��´�.
	Dimension screenSize=tk.getScreenSize();		//ȭ���� ũ�⸦ ���Ѵ�.
	
	/**GUI ������� ����**/
	JPanel up, left;
	ImageIcon logo = new ImageIcon("logo.png");
	ImageIcon icon = new ImageIcon("icon.png");
	JLabel lbLogo = new JLabel(logo);
	JLabel lbWelcom = new JLabel();
	JLabel lbPeriod = new JLabel();
	JLabel lbInfo = new JLabel();
	JButton btn_logout;
	JButton btn_roomList = new JButton("���ǽ� ����"); //��� ����ڰ� ���
	JButton btn_timeTable = new JButton("�ð�ǥ/������ �Է�"); //�л�, ������ ���
	JButton btn_leaveOfAbsence = new JButton("���н�û"); //�л�, ������ ���
	JButton btn_notice = new JButton("��������"); //�л�, ������ ���
	JButton btn_returnToSchool = new JButton("���н�û"); //���л��� ���
	JButton btn_selectLecture = new JButton("���� ����"); //������ ���
	JButton btn_setPeriod = new JButton("����Ⱓ ����"); //�����ڰ� ���
	
	BorderLayout GUI_MainLayout = new BorderLayout(10,10);
	
	GUI_ClassRoomList classRoom_list;
	GUI_StudentMain std_main;
	GUI_ProfessorTable prof_table;
	GUI_Professor prof_main;
	GUI_SetPeriod set_period;
	GUI_Notice notice; //�̿��� �߰� : �������� Ŭ����

	/**********************���� ���� �Լ�****************************/
	public static void main(String argv[]){
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");		//JTattoo ����� ��������
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		GUI_Login gui = new GUI_Login(conn);
	    gui.setVisible(true);
	}
	
	/**����Ʈ ������**/
	public GUI_Main(String id) {
		super("AESS");
		super.setIconImage(icon.getImage());
		
		Info.setNameAndType(id);
		
		this.setLayout(GUI_MainLayout);
		this.setSize(1100,750);
		this.addWindowListener(new EventHandler());
		
		/**���α׷� â ȭ�� �߾����� ����**/
		int x = screenSize.width/2 - this.getWidth()/2 ; //x��ǥ���ϱ�
		int y = screenSize.height/2 - this.getHeight()/2; //y��ǥ���ϱ�
		this.setLocation(x, y); //ȭ�� �߾ӿ� ����
		
		/*************��� �κ� ����**************/		
		up = new JPanel(null);
		
		up.setPreferredSize(new java.awt.Dimension(1, 100)); // �¿���
		up.add(lbLogo);
		lbLogo.setBounds(0,0,455,68);
		
		lbInfo.setText(Info.getYear()+"�⵵ "+Info.season+"�б� "+Info.test+"��� ���� �ý���");
		up.add(lbInfo);
		lbInfo.setBounds(800,0,230,50);
		
		lbWelcom.setText(Info.getName()+"�� ȯ���մϴ�!");
		up.add(lbWelcom);
		lbWelcom.setBounds(800,25,180,50);
		
		ImageIcon logout = new ImageIcon("logout.png");
		btn_logout = new JButton(logout);
		btn_logout.setBounds(930,42,50,16);
		btn_logout.setFocusPainted(false);
		up.add(btn_logout);
		btn_logout.addActionListener(this);
		
		if(Info.getMonth()!=0) {
			lbPeriod.setText("���� �Ⱓ : "+Info.getMonth()+"�� "+Info.getWeek()+"° �� "+Info.getsDate()+"�� ~ " +Info.geteDate()+"��");
			up.add(lbPeriod);
			lbPeriod.setFont(new Font("Sherif",Font.BOLD,12));
			lbPeriod.setBounds(800,50,200,50);
		}		
		add(up,"North");		
		
		/*************���� �޴� �� �߾� ȭ�� ����******************/	
		classRoom_list = new GUI_ClassRoomList(conn);	//���ǽ� GUI		
		add(classRoom_list, "Center");
		
		GridLayout Layout_leftMenu = new GridLayout(20,1,20,8);
		left = new JPanel(Layout_leftMenu);
		left.setPreferredSize(new java.awt.Dimension(200, 1)); // �¿���				

		left.add(btn_roomList);	//��� ����ڿ��� ���ǽ� �ð�ǥ ���� ��ư �߰�
		btn_roomList.addActionListener(this);

		if(Info.getType().equals("A")){ //������ �϶�
			set_period = new GUI_SetPeriod(conn, id);	//���� �Ⱓ ���� GUI
			
			left.add(btn_setPeriod);	//������ �϶� ����Ⱓ ���� ���� ��ư �߰�
			btn_setPeriod.addActionListener(this);
		}		
		else if(Info.getType().equals("P")){ //���� �϶�
			prof_table = new GUI_ProfessorTable(conn, id);	//���� ������ �ð�ǥ GUI
			add(prof_table, "Center");
			
			left.add(btn_selectLecture);	//���� �϶� ���� ��� ���� ��ư �߰�
			btn_selectLecture.setBounds(0,0,10,10);
			btn_selectLecture.addActionListener(this);
		}
		else if(Info.getType().equals("S") || Info.getType().equals("J")){ //�л� �Ǵ� �����϶�
			std_main = new GUI_StudentMain(conn, id);	//�л� GUI
			add(std_main, "Center");
			
			left.add(btn_timeTable);	 //������ ���̺� ���� ��ư �߰�
			btn_timeTable.addActionListener(this);
			left.add(btn_leaveOfAbsence); //���� ��ư �߰�
			btn_leaveOfAbsence.addActionListener(this);
			/**********�̿��� �߰�***************/
			notice = new GUI_Notice(conn); //�������� �ν��Ͻ� ����
			left.add(btn_notice); //�������� ���� ��ư �߰�
			btn_notice.addActionListener(this); //�������� ���� ��ư ������ ���
			/*************************************/
		}
		else if(Info.getType().equals("L")){
			left.add(btn_returnToSchool);
			btn_returnToSchool.addActionListener(this);
		}
		add(left,"West");
	}
	
	/************�� ��ư�� �߻� �̺�Ʈ*************/
	public void actionPerformed(ActionEvent e){		
		if(e.getSource() == btn_roomList) { //���ǽ� ��� ��ư �̺�Ʈ
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			add(classRoom_list, BorderLayout.CENTER);
			revalidate();
			repaint();
		}
		else if(e.getSource() == btn_selectLecture) { //���� ��� ��ư �̺�Ʈ
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			prof_table.resetProfessorTable();
			add(prof_table, BorderLayout.CENTER);
			revalidate();
			repaint();
		}
		else if(e.getSource() == btn_timeTable) { //������ ���� ��ư �̺�Ʈ
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			add(std_main, BorderLayout.CENTER);
			revalidate();
			repaint();
		}
		else if(e.getSource() == btn_setPeriod) { //����Ⱓ ���� ��ư �̺�Ʈ
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			add(set_period, BorderLayout.CENTER);
			revalidate();
			repaint();
		}
		else if(e.getSource() == btn_logout){ //�α׾ƿ� ��ư �̺�Ʈ
			GUI_Login gl = new GUI_Login(conn);
			dispose();
		}
		else if(e.getSource() == btn_leaveOfAbsence){ //���� ��û =>���� �ٸ� Ŭ������ �̵��ؾ� ��
			if(JOptionPane.showConfirmDialog(null,"���н�û�� �Ͻðڽ��ϱ�?\n(��û �� �ڵ��α׾ƿ� �˴ϴ�.)", "���н�û",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)==JOptionPane.YES_OPTION)
			{
				ManageUser.setConn(conn);
				ManageUser.setLeaveOfAbsence();
				/**������**/
				GUI_Login gl = new GUI_Login(conn);
				dispose();			
			} 
		}
		else if(e.getSource() == btn_returnToSchool){ //���н�û =>���� �ٸ� Ŭ������ �̵��ؾ� ��
			if(JOptionPane.showConfirmDialog(null,"���н�û�� �Ͻðڽ��ϱ�?\n(��û �� �ڵ��α׾ƿ� �˴ϴ�.)", "���н�û",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)==JOptionPane.YES_OPTION)
			{	
				ManageUser.setConn(conn);
				ManageUser.setReturnToSchool();
				/**������**/
				GUI_Login gl = new GUI_Login(conn);
				dispose();			
			}
		}
		else if(e.getSource() == btn_notice){ //�̿��� �߰� : �������� ����
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			add(notice, BorderLayout.CENTER); //�������� �ν��Ͻ� ���
			revalidate();
			repaint();			
		}
	}
	
	//@@�����ư �۵� �߰�
	class EventHandler implements WindowListener{
		public void windowClosing(WindowEvent e) {
				System.exit(0);
		}
		/*******�ƹ����뵵 ���� �޼��� ����************/
		public void windowOpened(WindowEvent e) {} 
		public void windowClosed(WindowEvent e){} 
		public void windowIconified(WindowEvent e){} 
		public void windowDeiconified(WindowEvent e){} 
		public void windowActivated(WindowEvent e){}
		public void windowDeactivated(WindowEvent e){}
	}
}

