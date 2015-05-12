import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**���� GUI ����**/
public class GUI_Main extends JFrame implements ActionListener {
	/**DB���� ���� ����**/
	static DBconnect dbcon = new DBconnect();
	static Connection conn = dbcon.connect();
	static Statement query;
	static ResultSet result;
	
	static String name, type;
	String id;
	
	/**********��ġ �߾�����*******/
	Toolkit tk = Toolkit.getDefaultToolkit(); 		//������ Toolkit��ü�� ��´�.
	Dimension screenSize=tk.getScreenSize();		//ȭ���� ũ�⸦ ���Ѵ�.
	int x_l, y_l;
	
	/**GUI ������� ����**/
	JPanel up, left;
	ImageIcon logo = new ImageIcon("logo.png");
	ImageIcon icon = new ImageIcon("icon.png");
	JLabel lbLogo = new JLabel(logo);
	JLabel lbWelcom = new JLabel();
	JLabel lbPeriod = new JLabel();
	JLabel lbInfo = new JLabel();
	JButton btn_logout;
	JButton btn_roomList = new JButton("���ǽ� ����");
	JButton btn_timeTable = new JButton("�ð�ǥ/������ �Է�");
	JButton btn_selectLecture = new JButton("���� ����");
	JButton btn_setPeriod = new JButton("����Ⱓ ����");
	
	BorderLayout GUI_MainLayout = new BorderLayout(10,10);
	
	GUI_ClassRoomList classRoom_list;
	GUI_StudentMain std_main;
	GUI_ProfessorTable prof_table;
	GUI_Professor prof_main;
	GUI_SetPeriod set_period;
	
	/**����Ʈ ������**/
	public GUI_Main(String id) {
		super("AESS");
		super.setIconImage(icon.getImage());
		
		this.id = id;
		info i = new info(id);
		this.setLayout(GUI_MainLayout);
		this.setSize(1100,750);
		
		/**���α׷� â ȭ�� �߾����� ����**/
		x_l = screenSize.width/2 - this.getWidth()/2 ; //x��ǥ���ϱ�
		y_l = screenSize.height/2 - this.getHeight()/2; //y��ǥ���ϱ�
		this.setLocation(x_l, y_l); //ȭ�� �߾ӿ� ����
		
		
		/*************���� ������ ����******************/	
		classRoom_list = new GUI_ClassRoomList(conn, id);	//���ǽ� GUI		
		add(classRoom_list, "Center");
		if(info.is_A){	//������ �϶�
			set_period = new GUI_SetPeriod(conn, id);	//���� �Ⱓ ���� GUI
		}
		if(info.is_P){	//���� �϶�
			prof_table = new GUI_ProfessorTable(conn, id);	//���� ������ �ð�ǥ GUI
			add(prof_table, "Center");
		}
		if(info.is_S){	//�л� �϶�
			std_main = new GUI_StudentMain(conn, id);	//�л� GUI
			add(std_main, "Center");
		}
		/*************��� �κ� ����**************/
		
		up = new JPanel(null);
		
		up.setPreferredSize(new java.awt.Dimension(1, 100)); // �¿���
		up.add(lbLogo);
		lbLogo.setBounds(0,0,455,68);
		
		lbInfo.setText(info.year+"�⵵ "+info.season+"�б� "+info.test+"��� ���� �ý���");
		up.add(lbInfo);
		lbInfo.setBounds(800,0,230,50);
		
		lbWelcom.setText(info.name+"�� ȯ���մϴ�!");
		up.add(lbWelcom);
		lbWelcom.setBounds(800,25,180,50);
		
		ImageIcon logout = new ImageIcon("logout.png");
		btn_logout = new JButton(logout);
		btn_logout.setBounds(930,42,50,16);
		btn_logout.setFocusPainted(false);
		up.add(btn_logout);
		
		if(info.month!=0) {
			lbPeriod.setText("���� �Ⱓ : "+info.month+"�� "+info.week+"° �� "+info.sDate+"�� ~ " +info.eDate+"��");
			up.add(lbPeriod);
			lbPeriod.setFont(new Font("Sherif",Font.BOLD,12));
			lbPeriod.setBounds(800,50,200,50);
		}		
		add(up,"North");		
		
		/************���� �޴� ����*************/
		GridLayout Layout_leftMenu = new GridLayout(20,1,20,8);
		left = new JPanel(Layout_leftMenu);
		left.setPreferredSize(new java.awt.Dimension(200, 1)); // �¿���		
		
		btn_logout.addActionListener(this);
		btn_selectLecture.setBounds(0,0,10,10);

		left.add(btn_roomList);	//��� ����ڿ��� ���ǽ� �ð�ǥ ���� ��ư �߰�
		btn_roomList.addActionListener(this);
		if(info.is_S){
			left.add(btn_timeTable);	//�л� �϶� ������ ���̺� ���� ��ư �߰�
			btn_timeTable.addActionListener(this);
		}
		if(info.is_P){
			left.add(btn_selectLecture);	//���� �϶� ���� ��� ���� ��ư �߰�
			btn_selectLecture.addActionListener(this);
		}
		if(info.is_A){
			left.add(btn_setPeriod);	//������ �϶� ����Ⱓ ���� ���� ��ư �߰�
			btn_setPeriod.addActionListener(this);
		}		
		add(left,"West");
	}
	
	/**�� ��ư�� �߻� �̺�Ʈ**/
	public void actionPerformed(ActionEvent e){		
		if(e.getSource() == btn_roomList) {	//���ǽ� ��� ��ư �̺�Ʈ
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			add(classRoom_list, BorderLayout.CENTER);
			revalidate();
			repaint();
		} else if(e.getSource() == btn_selectLecture) {	//���� ��� ��ư �̺�Ʈ
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			add(prof_table, BorderLayout.CENTER);
			revalidate();
			repaint();
		} else if(e.getSource() == btn_timeTable) {	//������ ���� ��ư �̺�Ʈ
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			add(std_main, BorderLayout.CENTER);
			revalidate();
			repaint();
		} else if(e.getSource() == btn_setPeriod) {	//����Ⱓ ���� ��ư �̺�Ʈ
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			add(set_period, BorderLayout.CENTER);
			revalidate();
			repaint();
		} else if(e.getSource() == btn_logout){	//�α׾ƿ� ��ư �̺�Ʈ
			dispose();
			GUI_Login gl = new GUI_Login();
		}
	}	
}

