import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

@SuppressWarnings("serial")
/**Open up main GUI**/
public class GUI_Main extends JFrame implements ActionListener {	
	/**********Locate center*******/
	private Toolkit tk = Toolkit.getDefaultToolkit();//Obtain implemented Toolkit instance
	private Dimension screenSize=tk.getScreenSize();//Get screen size
	
	/**Set GUI Components**/
	private JPanel up, left;
	private ImageIcon logo = new ImageIcon("logo.png");
	private ImageIcon icon = new ImageIcon("icon.png");
	private JLabel lbLogo = new JLabel(logo);
	private JLabel lbWelcom = new JLabel();
	private JLabel lbPeriod = new JLabel();
	private JLabel lbInfo = new JLabel();
	private JButton btn_logout;
	private JButton btn_roomList = new JButton("강의실 열람"); //For all user
	private JButton btn_timeTable = new JButton("시간표/스케쥴 입력"); //For student, assistant
	private JButton btn_leaveOfAbsence = new JButton("휴학신청"); //For student, assistant
	private JButton btn_notice = new JButton("공지사항"); //For student, assistant
	private JButton btn_changpwd = new JButton("비밀번호수정"); //For student, assistant, professor @@Password change
	private JButton btn_activate = new JButton("계정 활성화 신청"); //For inactivated user
	private JButton btn_selectLecture = new JButton("수업 선택"); //For professor
	private JButton btn_setPeriod = new JButton("시험기간 설정"); //For admin
	private JButton btn_dispUserList = new JButton("사용자 목록 표시"); //For admin
	
	private BorderLayout GUI_MainLayout = new BorderLayout(10,10);
	
	/**GUI class list**/
	private GUI_ClassRoomList classRoom_list;
	private GUI_StudentMain std_main;
	private GUI_ProfessorTable prof_table;
	private GUI_Professor prof_main;
	private GUI_SetPeriod set_period;
	private GUI_Notice notice; //이영석 추가 : 공지사항 클래스
	private GUI_UserList user_list; //정용훈 추가

	/**********************Main start function****************************/
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
		Info.setConn(); //DB connect
		GUI_Login gui = new GUI_Login();//Display login GUI
	    gui.setVisible(true);
	}
	
	/**Constructor**/
	public GUI_Main(String id) {
		super("AESS");
		super.setIconImage(icon.getImage());

		/**Initialize ManageUser DBconnection**/
		ManageUser.setConn(Info.getConn());
		
		Info.setNameAndType(id); //Get name and type by id
		
		this.setLayout(GUI_MainLayout);
		this.setSize(1100,750);
		this.addWindowListener(new EventHandler());
		
		/**Locate into the center of screen**/
		int x = screenSize.width/2 - this.getWidth()/2 ;//Get x coordinate
		int y = screenSize.height/2 - this.getHeight()/2;//Get y coordinate
		this.setLocation(x, y); //Display on center
		
		/*************GUI Top**************/		
		up = new JPanel(null);
		
		up.setPreferredSize(new java.awt.Dimension(1, 100)); //Left Margin
		up.add(lbLogo);
		lbLogo.setBounds(0,0,455,68);
		
		lbInfo.setText(Info.getYear()+"년도 "+Info.getSeason()+"학기 "+Info.getTest()+"고사 예약 시스템");
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
		
		/*************GUI Left menu and center screen******************/	
		classRoom_list = new GUI_ClassRoomList();	//Room GUI		
		add(classRoom_list, "Center");
		
		GridLayout Layout_leftMenu = new GridLayout(20,1,20,8);
		left = new JPanel(Layout_leftMenu);
		left.setPreferredSize(new java.awt.Dimension(200, 1)); //Left margin			

		left.add(btn_roomList);	//Add button to view room schedule for all users
		btn_roomList.addActionListener(this);
		
		if(Info.getState().equals("T") || Info.getState().equals("L")){//When activated(Include leave of absence)
			if(Info.getType().equals("A")){ //For admin
				set_period = new GUI_SetPeriod(id);	//Exam period set GUI
				user_list = new GUI_UserList(id); //User list GUI
				
				left.add(btn_setPeriod);//Add button to view to set exam period for admin
				btn_setPeriod.addActionListener(this);
				left.add(btn_dispUserList);
				btn_dispUserList.addActionListener(this);
			}		
			if(Info.getType().equals("P")){//For professor
				prof_table = new GUI_ProfessorTable(id);//GUI for class schedule
				add(prof_table, "Center");
				
				left.add(btn_selectLecture);//Add button to view list of rooms for professor
				btn_selectLecture.setBounds(0,0,10,10);
				btn_selectLecture.addActionListener(this);
				/*************************************/
				left.add(btn_changpwd); //@@Change password
				btn_changpwd.addActionListener(this); //@@Change password
			}
			else if(Info.getType().equals("S") || Info.getType().equals("J")){//For Student or Assistant
				std_main = new GUI_StudentMain(id);	//Student GUI
				add(std_main, "Center");
				
				left.add(btn_timeTable);	 //Add button to view schedule table
				btn_timeTable.addActionListener(this);
				left.add(btn_leaveOfAbsence); //Add button for leave of absence
				btn_leaveOfAbsence.addActionListener(this);
				/**********이영석 추가***************/
				notice = new GUI_Notice(); //Notice instantiation
				left.add(btn_notice); //Add button to view notice
				btn_notice.addActionListener(this); //Add listener to button to view notice
				/*************************************/
				left.add(btn_changpwd); //@@Change password
				btn_changpwd.addActionListener(this); //@@Change password
			}
		}
		else if(Info.getState().equals("F")){ //When inactivated
			left.add(btn_activate);
			btn_activate.addActionListener(this);
		}
		add(left,"West");
	}
	
	/************Events for the buttons*************/
	public void actionPerformed(ActionEvent e){		
		if(e.getSource() == btn_roomList) { //Room list button event
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			add(classRoom_list, BorderLayout.CENTER);
			revalidate();
			repaint();
		}
		else if(e.getSource() == btn_selectLecture) { //Class list button event
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			prof_table.resetProfessorTable();
			add(prof_table, BorderLayout.CENTER);
			revalidate();
			repaint();
		}
		else if(e.getSource() == btn_timeTable) { //View time table button event
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			add(std_main, BorderLayout.CENTER);
			revalidate();
			repaint();
		}
		else if(e.getSource() == btn_setPeriod) { //Set period button event
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			add(set_period, BorderLayout.CENTER);
			revalidate();
			repaint();
		}
		else if(e.getSource() == btn_logout){ //Log out button event
			GUI_Login gl = new GUI_Login();
			dispose();
		}
		else if(e.getSource() == btn_changpwd){ //@@Change password
			String pass=""; //@@Change password
			pass=JOptionPane.showInputDialog("변경할 비밀번호를 입력하십시오."); //@@Change password
			ManageUser.setPass(Info.getId(), pass);
		} //@@Change password
		else if(e.getSource() == btn_leaveOfAbsence){ //Leave of Absence => Move to other class
			if(JOptionPane.showConfirmDialog(null,"휴학신청을 하시겠습니까?\n(신청 후 자동로그아웃 됩니다.)", "휴학신청",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)==JOptionPane.YES_OPTION)
			{
				ManageUser.setLeaveOfAbsence();
				/**Sign in again**/
				GUI_Login gl = new GUI_Login();
				dispose();			
			} 
		}
		else if(e.getSource() == btn_activate){ //Submit activation
			if(JOptionPane.showConfirmDialog(null,"계정 활성화 신청을 하시겠습니까?\n(신청 후 자동로그아웃 됩니다.)", "계정 활성화 신청",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)==JOptionPane.YES_OPTION)
			{	
				ManageUser.setActivate();
				/**재접속**/
				GUI_Login gl = new GUI_Login();
				dispose();			
			}
		}
		else if(e.getSource() == btn_notice){ //이영석 추가 : 공지사항 보기
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			add(notice, BorderLayout.CENTER); //Add notice instance
			revalidate();
			repaint();			
		}
		else if(e.getSource() == btn_dispUserList){
			remove(GUI_MainLayout.getLayoutComponent(BorderLayout.CENTER));
			add(user_list, BorderLayout.CENTER);
			revalidate();
			repaint();
		}
	}
	
	//@@Add close button feature
	class EventHandler implements WindowListener{
		public void windowClosing(WindowEvent e) {
				System.exit(0);
		}
		/*******Vacant method implementation************/
		public void windowOpened(WindowEvent e) {} 
		public void windowClosed(WindowEvent e){} 
		public void windowIconified(WindowEvent e){} 
		public void windowDeiconified(WindowEvent e){} 
		public void windowActivated(WindowEvent e){}
		public void windowDeactivated(WindowEvent e){}
	}
}