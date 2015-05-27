import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**메인 GUI 띄우기**/
public class GUI_Main extends JFrame implements ActionListener {
	/**DB연결 변수 설정**/
	private static DBconnect dbcon = new DBconnect();
	private static Connection conn = dbcon.connect();
	
	/**********위치 중앙으로*******/
	Toolkit tk = Toolkit.getDefaultToolkit(); 		//구현된 Toolkit객체를 얻는다.
	Dimension screenSize=tk.getScreenSize();		//화면의 크기를 구한다.
	
	/**GUI 구성요소 설정**/
	JPanel up, left;
	ImageIcon logo = new ImageIcon("logo.png");
	ImageIcon icon = new ImageIcon("icon.png");
	JLabel lbLogo = new JLabel(logo);
	JLabel lbWelcom = new JLabel();
	JLabel lbPeriod = new JLabel();
	JLabel lbInfo = new JLabel();
	JButton btn_logout;
	JButton btn_roomList = new JButton("강의실 열람"); //모든 사용자가 사용
	JButton btn_timeTable = new JButton("시간표/스케쥴 입력"); //학생, 조교가 사용
	JButton btn_leaveOfAbsence = new JButton("휴학신청"); //학생, 조교가 사용
	JButton btn_notice = new JButton("공지사항"); //학생, 조교가 사용
	JButton btn_returnToSchool = new JButton("복학신청"); //휴학생이 사용
	JButton btn_selectLecture = new JButton("수업 선택"); //교수가 사용
	JButton btn_setPeriod = new JButton("시험기간 설정"); //관리자가 사용
	
	BorderLayout GUI_MainLayout = new BorderLayout(10,10);
	
	GUI_ClassRoomList classRoom_list;
	GUI_StudentMain std_main;
	GUI_ProfessorTable prof_table;
	GUI_Professor prof_main;
	GUI_SetPeriod set_period;
	GUI_Notice notice; //이영석 추가 : 공지사항 클래스

	/**********************메인 시작 함수****************************/
	public static void main(String argv[]){
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");		//JTattoo 룩앤필 가져오기
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
	
	/**디폴트 생성자**/
	public GUI_Main(String id) {
		super("AESS");
		super.setIconImage(icon.getImage());
		
		Info.setNameAndType(id);
		
		this.setLayout(GUI_MainLayout);
		this.setSize(1100,750);
		this.addWindowListener(new EventHandler());
		
		/**프로그램 창 화면 중앙으로 셋팅**/
		int x = screenSize.width/2 - this.getWidth()/2 ; //x좌표구하기
		int y = screenSize.height/2 - this.getHeight()/2; //y좌표구하기
		this.setLocation(x, y); //화면 중앙에 띄우기
		
		/*************상단 부분 설정**************/		
		up = new JPanel(null);
		
		up.setPreferredSize(new java.awt.Dimension(1, 100)); // 좌여백
		up.add(lbLogo);
		lbLogo.setBounds(0,0,455,68);
		
		lbInfo.setText(Info.getYear()+"년도 "+Info.season+"학기 "+Info.test+"고사 예약 시스템");
		up.add(lbInfo);
		lbInfo.setBounds(800,0,230,50);
		
		lbWelcom.setText(Info.getName()+"님 환영합니다!");
		up.add(lbWelcom);
		lbWelcom.setBounds(800,25,180,50);
		
		ImageIcon logout = new ImageIcon("logout.png");
		btn_logout = new JButton(logout);
		btn_logout.setBounds(930,42,50,16);
		btn_logout.setFocusPainted(false);
		up.add(btn_logout);
		btn_logout.addActionListener(this);
		
		if(Info.getMonth()!=0) {
			lbPeriod.setText("시험 기간 : "+Info.getMonth()+"월 "+Info.getWeek()+"째 주 "+Info.getsDate()+"일 ~ " +Info.geteDate()+"일");
			up.add(lbPeriod);
			lbPeriod.setFont(new Font("Sherif",Font.BOLD,12));
			lbPeriod.setBounds(800,50,200,50);
		}		
		add(up,"North");		
		
		/*************좌측 메뉴 및 중앙 화면 설정******************/	
		classRoom_list = new GUI_ClassRoomList(conn);	//강의실 GUI		
		add(classRoom_list, "Center");
		
		GridLayout Layout_leftMenu = new GridLayout(20,1,20,8);
		left = new JPanel(Layout_leftMenu);
		left.setPreferredSize(new java.awt.Dimension(200, 1)); // 좌여백				

		left.add(btn_roomList);	//모든 사용자에게 강의실 시간표 보기 버튼 추가
		btn_roomList.addActionListener(this);

		if(Info.getType().equals("A")){ //관리자 일때
			set_period = new GUI_SetPeriod(conn, id);	//시험 기간 설정 GUI
			
			left.add(btn_setPeriod);	//관리자 일때 시험기간 설정 보기 버튼 추가
			btn_setPeriod.addActionListener(this);
		}		
		else if(Info.getType().equals("P")){ //교수 일때
			prof_table = new GUI_ProfessorTable(conn, id);	//교수 수업별 시간표 GUI
			add(prof_table, "Center");
			
			left.add(btn_selectLecture);	//교수 일때 강의 목록 보기 버튼 추가
			btn_selectLecture.setBounds(0,0,10,10);
			btn_selectLecture.addActionListener(this);
		}
		else if(Info.getType().equals("S") || Info.getType().equals("J")){ //학생 또는 조교일때
			std_main = new GUI_StudentMain(conn, id);	//학생 GUI
			add(std_main, "Center");
			
			left.add(btn_timeTable);	 //스케쥴 테이블 보기 버튼 추가
			btn_timeTable.addActionListener(this);
			left.add(btn_leaveOfAbsence); //휴학 버튼 추가
			btn_leaveOfAbsence.addActionListener(this);
			/**********이영석 추가***************/
			notice = new GUI_Notice(conn); //공지사항 인스턴스 생성
			left.add(btn_notice); //공지사항 보기 버튼 추가
			btn_notice.addActionListener(this); //공지사항 보기 버튼 리스너 등록
			/*************************************/
		}
		else if(Info.getType().equals("L")){
			left.add(btn_returnToSchool);
			btn_returnToSchool.addActionListener(this);
		}
		add(left,"West");
	}
	
	/************각 버튼별 발생 이벤트*************/
	public void actionPerformed(ActionEvent e){		
		if(e.getSource() == btn_roomList) { //강의실 목록 버튼 이벤트
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			add(classRoom_list, BorderLayout.CENTER);
			revalidate();
			repaint();
		}
		else if(e.getSource() == btn_selectLecture) { //강의 목록 버튼 이벤트
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			prof_table.resetProfessorTable();
			add(prof_table, BorderLayout.CENTER);
			revalidate();
			repaint();
		}
		else if(e.getSource() == btn_timeTable) { //스케쥴 보기 버튼 이벤트
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			add(std_main, BorderLayout.CENTER);
			revalidate();
			repaint();
		}
		else if(e.getSource() == btn_setPeriod) { //시험기간 설정 버튼 이벤트
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			add(set_period, BorderLayout.CENTER);
			revalidate();
			repaint();
		}
		else if(e.getSource() == btn_logout){ //로그아웃 버튼 이벤트
			GUI_Login gl = new GUI_Login(conn);
			dispose();
		}
		else if(e.getSource() == btn_leaveOfAbsence){ //복학 신청 =>이후 다른 클래스로 이동해야 함
			if(JOptionPane.showConfirmDialog(null,"휴학신청을 하시겠습니까?\n(신청 후 자동로그아웃 됩니다.)", "휴학신청",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)==JOptionPane.YES_OPTION)
			{
				ManageUser.setConn(conn);
				ManageUser.setLeaveOfAbsence();
				/**재접속**/
				GUI_Login gl = new GUI_Login(conn);
				dispose();			
			} 
		}
		else if(e.getSource() == btn_returnToSchool){ //복학신청 =>이후 다른 클래스로 이동해야 함
			if(JOptionPane.showConfirmDialog(null,"복학신청을 하시겠습니까?\n(신청 후 자동로그아웃 됩니다.)", "복학신청",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)==JOptionPane.YES_OPTION)
			{	
				ManageUser.setConn(conn);
				ManageUser.setReturnToSchool();
				/**재접속**/
				GUI_Login gl = new GUI_Login(conn);
				dispose();			
			}
		}
		else if(e.getSource() == btn_notice){ //이영석 추가 : 공지사항 보기
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			add(notice, BorderLayout.CENTER); //공지사항 인스턴스 등록
			revalidate();
			repaint();			
		}
	}
	
	//@@종료버튼 작동 추가
	class EventHandler implements WindowListener{
		public void windowClosing(WindowEvent e) {
				System.exit(0);
		}
		/*******아무내용도 없는 메서드 구현************/
		public void windowOpened(WindowEvent e) {} 
		public void windowClosed(WindowEvent e){} 
		public void windowIconified(WindowEvent e){} 
		public void windowDeiconified(WindowEvent e){} 
		public void windowActivated(WindowEvent e){}
		public void windowDeactivated(WindowEvent e){}
	}
}

