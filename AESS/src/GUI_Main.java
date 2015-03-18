import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**메인 GUI 띄우기**/
public class GUI_Main extends JFrame implements ActionListener {
	/**DB연결 변수 설정**/
	static DBconnect dbcon = new DBconnect();
	static Connection conn = dbcon.connect();
	static Statement query;
	static ResultSet result;
	
	static String name, type;
	String id;
	
	/**********위치 중앙으로*******/
	Toolkit tk = Toolkit.getDefaultToolkit(); 		//구현된 Toolkit객체를 얻는다.
	Dimension screenSize=tk.getScreenSize();		//화면의 크기를 구한다.
	int x_l, y_l;
	
	/**GUI 구성요소 설정**/
	JPanel up, left;
	ImageIcon logo = new ImageIcon("logo.png");
	ImageIcon icon = new ImageIcon("icon.png");
	JLabel lbLogo = new JLabel(logo);
	JLabel lbWelcom = new JLabel();
	JLabel lbPeriod = new JLabel();
	JLabel lbInfo = new JLabel();
	JButton bt_logout;
	JButton menu_RoomList = new JButton("강의실 열람");
	JButton menu_stTimeTable = new JButton("시간표/스케쥴 입력");
	JButton menu_Professor = new JButton("수업 선택");
	JButton menu_SetPeriod = new JButton("시험기간 설정");
	
	BorderLayout GUI_MainLayout = new BorderLayout(10,10);
	
	GUI_ClassRoomList classRoom_list;
	GUI_StudentMain std_main;
	GUI_ProfessorTable prof_table;
	GUI_Professor prof_main;
	GUI_SetPeriod set_period;
	
	/**디폴트 생성자**/
	public GUI_Main(String id) {
		super("AESS");
		super.setIconImage(icon.getImage());
		
		this.id = id;
		info i = new info(id);
		this.setLayout(GUI_MainLayout);
		this.setSize(1100,750);
		
		/**프로그램 창 화면 중앙으로 셋팅**/
		x_l = screenSize.width/2 - this.getWidth()/2 ; //x좌표구하기
		y_l = screenSize.height/2 - this.getHeight()/2; //y좌표구하기
		this.setLocation(x_l, y_l); //화면 중앙에 띄우기
		
		
		/*************메인 컨텐츠 오픈******************/	
		classRoom_list = new GUI_ClassRoomList(conn, id);	//강의실 GUI		
		add(classRoom_list, "Center");
		if(info.is_P){
			prof_table = new GUI_ProfessorTable(conn, id);	//교수 수업별 시간표 GUI
			add(prof_table, "Center");
		}
		if(info.is_S){
			std_main = new GUI_StudentMain(conn, id);	//학생 GUI
			add(std_main, "Center");
		}
		/***************************************/
		
		up = new JPanel(null);
		
		up.setPreferredSize(new java.awt.Dimension(1, 100)); // 좌여백
		up.add(lbLogo);
		lbLogo.setBounds(0,0,455,68);
		
		lbInfo.setText(info.year+"년도 "+info.season+"학기 "+info.test+"고사 예약 시스템");
		up.add(lbInfo);
		lbInfo.setBounds(800,0,230,50);
		
		lbWelcom.setText(info.name+"님 환영합니다!");
		up.add(lbWelcom);
		lbWelcom.setBounds(800,25,180,50);
		
		ImageIcon logout = new ImageIcon("logout.png");
		bt_logout = new JButton(logout);
		bt_logout.setBounds(930,42,50,16);
		bt_logout.setFocusPainted(false);
		up.add(bt_logout);
		
		if(info.month!=0) {
			lbPeriod.setText("시험 기간 : "+info.month+"월 "+info.week+"째 주 "+info.sDate+"일 ~ " +info.eDate+"일");
			up.add(lbPeriod);
			lbPeriod.setFont(new Font("Sherif",Font.BOLD,12));
			lbPeriod.setBounds(800,50,200,50);
		}
		
		
		/************좌측 메뉴 구성*************/
		GridLayout Layout_leftMenu = new GridLayout(20,1,20,8);
		left = new JPanel(Layout_leftMenu);
		
		bt_logout.addActionListener(this);
		menu_Professor.setBounds(0,0,10,10);

		left.add(menu_RoomList);
		if(info.is_S) left.add(menu_stTimeTable);
		if(info.is_P) left.add(menu_Professor);
		if(info.is_A) left.add(menu_SetPeriod);
		
		menu_stTimeTable.addActionListener(this);
		menu_RoomList.addActionListener(this);
		menu_Professor.addActionListener(this);
		menu_SetPeriod.addActionListener(this);
		
		left.setPreferredSize(new java.awt.Dimension(200, 1)); // 좌여백
		
		/**************************************/
		add(up,"North");
		add(left,"West");
	}
	
	public void actionPerformed(ActionEvent e){		
		if(e.getSource() == menu_RoomList) {
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			add(classRoom_list, BorderLayout.CENTER);
			revalidate();
			repaint();
		} else if(e.getSource() == menu_Professor) {
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			GUI_ProfessorTable prof_table = new GUI_ProfessorTable(conn, id);
			add(prof_table, BorderLayout.CENTER);
			revalidate();
			repaint();
		} else if(e.getSource() == menu_stTimeTable) {
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			add(std_main, BorderLayout.CENTER);
			revalidate();
			repaint();
		} else if(e.getSource() == menu_SetPeriod) {
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			set_period = new GUI_SetPeriod(conn, id);	//시험 기간 설정 GUI
			add(set_period, BorderLayout.CENTER);
			revalidate();
			repaint();
		} else if(e.getSource() == bt_logout){
			dispose();
			GUI_Login gl = new GUI_Login();
		}
	}	
}

