import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

class GUI_Login extends JFrame{	
	private Toolkit tk = Toolkit.getDefaultToolkit(); //구현된 Toolkit객체를 얻는다.
	private Dimension screenSize=tk.getScreenSize(); //화면의 크기를 구한다.
	
	/***************로고 삽입******************/
	private ImageIcon icon = new ImageIcon("icon.png");
	private ImageIcon small_logo = new ImageIcon("logo_small.png");
	private JLabel lbSmallLogo = new JLabel(small_logo);
	
	/**************로그인 컴포넌트 설정***************/
	private JLabel lb_Id = new JLabel("ID : ", Label.RIGHT); // Label.RIGHT : Label의 text정렬을 오른쪽으로.
	private JLabel lb_Pass = new JLabel("Password :", Label.RIGHT);
	private JLabel lb_Valid = new JLabel("아이디와 비밀번호를 입력해 주세요.");
	private TextField tf_Id  = new TextField(20);
	private TextField tf_Pwd = new TextField(20);
	private JButton bt_Login = new JButton("로그인");
	private JButton bt_Cancel = new JButton("취소");	
	private JButton bt_SignUp = new JButton("회원가입");
	private int false_check = 0; //승훈추가 : 비밀번호 초기화용

	/*************디폴트 생성자*****************/
	public GUI_Login(){
		super("AESS LOGIN"); // Frame(String title)을 호출한다.
		super.setIconImage(icon.getImage());
		setLayout(null);
		Login.setConn(Info.getConn());
		
		/*************창 위치 설정***************/
		this.setSize(400,250);
		int x = screenSize.width/2 - this.getWidth()/2 ; //x좌표구하기
		int y = screenSize.height/2 - this.getHeight()/2; //y좌표구하기
		this.setLocation(x, y);
		
		this.addWindowListener(new EventHandler1());
		this.setVisible(true);
		
		tf_Pwd.setEchoChar('*'); // 입력한 값 대신 '*'이 보이게 한다.
		
		/******버튼과 TextField에 이벤트처리를 위한 Listener를 추가해준다.****/
		tf_Id.addKeyListener(new KeyHandler());
		tf_Pwd.addKeyListener(new KeyHandler());
		
		bt_Login.addActionListener(new EventHandler());
		bt_Login.addKeyListener(new KeyHandler());
		bt_Cancel.addActionListener(new EventHandler());
		
		/**********************회원가입 버튼**********************/
		bt_SignUp.addActionListener(new EventHandler());
		
		lbSmallLogo.setBounds(40, 0, 321, 68);
		add(lbSmallLogo, "North");		
		
		/*************ID, 비밀번호, 로그인 컴포넌트 add*************/
		lb_Id.setBounds(117, 80, 30, 22);
		tf_Id.setBounds(150, 80, 180, 22);
		add(lb_Id);
		add(tf_Id);
		
		lb_Pass.setBounds(70, 110, 80, 22);
		tf_Pwd.setBounds(150, 110, 180, 22);
		add(lb_Pass);
		add(tf_Pwd);

		bt_Login.setBounds(70, 153, 80, 21);
		bt_Cancel.setBounds(160, 153, 80, 21);
		bt_SignUp.setBounds(250, 153, 80, 21);
		add(bt_Login);
		add(bt_Cancel);
		/**********************회원가입**********************/
		add(bt_SignUp);
		
		lb_Valid.setBounds(105, 190, 220, 21);
		add(lb_Valid);
		
		this.setResizable(false); //크기 고정
	}
	
	/****************로그인 결과에 따른 출력*******************/
	public void Login_Setting(){
		String id = tf_Id.getText();
		String password = tf_Pwd.getText();
		int loginResult = Login.loginSetting(id, password);
		if(loginResult == 0){
			//gui 변수 gui_main으로 변경
			GUI_Main gui_main = new GUI_Main(id);
			gui_main.setVisible(true);
			dispose();
		}
		else if(loginResult == 1){
			lb_Valid.setText("아이디 또는 비밀번호가 잘못되었습니다.");
			lb_Valid.setForeground(Color.red); // 글자 빨간색으로 출력	
			false_check++; //승훈추가 : 비밀번호 틀린횟수 체크, 5이상일때 초기화창 만들려고
			System.out.printf("   %d\n",false_check);
			if(false_check>5) {
				if(JOptionPane.showConfirmDialog(null,"비밀번호 초기화 요청하시겠습니까?", "비밀번호 초기화",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)==JOptionPane.YES_OPTION) {
						false_check=0;
						System.out.println("id : " + id + " 초기화 요청 발사"); //승훈추가 : 비밀번호 초기화 요청
					}
			}
		}
		else if(loginResult == 2){
			lb_Valid.setText("강의실예약시스템 사용 기간이 아닙니다.");
			lb_Valid.setForeground(Color.red); // 글자 빨간색으로 출력			
		}
	}
	
	/****************엔터 입력할 때의 키 리스너*****************/
	class KeyHandler implements KeyListener{
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER)
				Login_Setting();
		}
		@Override
		public void keyReleased(KeyEvent arg0) {}
		@Override
		public void keyTyped(KeyEvent arg0) {}
	}
	
	/*******************각 버튼의 액션 리스너********************/
	class EventHandler implements ActionListener {
		public void actionPerformed(ActionEvent e){
			//로그인 버튼 클릭시
			if(e.getSource() == bt_Login) 
				Login_Setting();

			//취소 버튼 클릭시 종료
			else if (e.getSource() == bt_Cancel) {
				System.exit(0);
			} 

			//회원가입 창으로 이동
			else if(e.getSource() == bt_SignUp){
				setVisible(false);
				GUI_SignUp gui_signup = new GUI_SignUp();
			    gui_signup.setVisible(true);
			}
		}
	}

	class EventHandler1 implements WindowListener	{ 		
		// Frame의 닫기 버튼 클릭 시 종료
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

