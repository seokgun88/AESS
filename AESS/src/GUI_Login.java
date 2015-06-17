import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
/**Login GUI**/
class GUI_Login extends JFrame{	
	private Toolkit tk = Toolkit.getDefaultToolkit();//Obtain implemented Toolkit instance
	private Dimension screenSize=tk.getScreenSize();//Get screen size
	
	/***************Insert logo******************/
	private ImageIcon icon = new ImageIcon("icon.png");
	private ImageIcon small_logo = new ImageIcon("logo_small.png");
	private JLabel lbSmallLogo = new JLabel(small_logo);
	
	/**************Setup Login Component***************/
	private JLabel lb_Id = new JLabel("ID : ", Label.RIGHT); // Label.RIGHT : right align of text
	private JLabel lb_Pass = new JLabel("Password :", Label.RIGHT);
	private JLabel lb_Valid = new JLabel("아이디와 비밀번호를 입력해 주세요.");
	private TextField tf_Id  = new TextField(20);
	private TextField tf_Pwd = new TextField(20);
	private JButton bt_Login = new JButton("로그인");
	private JButton bt_Cancel = new JButton("취소");	
	private JButton bt_SignUp = new JButton("회원가입");
	private int false_check = 0; //승훈추가 : 비밀번호 초기화용

	/*************Constructor*****************/
	public GUI_Login(){
		super("AESS LOGIN"); // Call Frame(String title)
		super.setIconImage(icon.getImage());
		setLayout(null);
		Login.setConn(Info.getConn());
		
		/*************Set window position***************/
		this.setSize(400,250);
		int x = screenSize.width/2 - this.getWidth()/2 ; //Get x coordinate
		int y = screenSize.height/2 - this.getHeight()/2; //Get y coordinate
		this.setLocation(x, y);
		
		this.addWindowListener(new EventHandler1());
		this.setVisible(true);
		
		tf_Pwd.setEchoChar('*'); //Show '*' instead real value
		
		/******Add listener to Button and TextField for event handle****/
		tf_Id.addKeyListener(new KeyHandler());
		tf_Pwd.addKeyListener(new KeyHandler());
		
		bt_Login.addActionListener(new EventHandler());
		bt_Login.addKeyListener(new KeyHandler());
		bt_Cancel.addActionListener(new EventHandler());
		
		/**********************Sign up button**********************/
		bt_SignUp.addActionListener(new EventHandler());
		
		lbSmallLogo.setBounds(40, 0, 321, 68);
		add(lbSmallLogo, "North");		
		
		/*************ID, Password, Login components add*************/
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
		/**********************Sign up**********************/
		add(bt_SignUp);
		
		lb_Valid.setBounds(105, 190, 220, 21);
		add(lb_Valid);
		
		this.setResizable(false); //Fix the size
	}
	
	/****************Print result of login*******************/
	public void Login_Setting(){
		String id = tf_Id.getText();
		String password = tf_Pwd.getText();
		int loginResult = Login.loginSetting(id, password);
		if(loginResult == 0){
			//gui variable is changed into gui_main
			GUI_Main gui_main = new GUI_Main(id);
			gui_main.setVisible(true);
			dispose();
		}
		else if(loginResult == 1){
			lb_Valid.setText("아이디 또는 비밀번호가 잘못되었습니다.");
			lb_Valid.setForeground(Color.red); //Print text color in red	
			false_check++; //승훈추가 : 비밀번호 틀린횟수 체크, 5이상일때 초기화창 만들려고
			System.out.printf("   %d\n",false_check);
			if(false_check>5) {
				if(JOptionPane.showConfirmDialog(null,"비밀번호 초기화 요청하시겠습니까?", "비밀번호 초기화",
						JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)==JOptionPane.YES_OPTION) {
					false_check=0;
					System.out.println("id : " + id + " 초기화 요청 발사"); //승훈추가 : 비밀번호 초기화 요청
					ManageUser.setConn(Info.getConn());
					if(!ManageUser.resetPass(id)){
						JOptionPane.showMessageDialog(null, "존재하지 않는 ID입니다.", "아이디 오류", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		}
		else if(loginResult == 2){
			lb_Valid.setText("강의실예약시스템 사용 기간이 아닙니다.");
			lb_Valid.setForeground(Color.red); //Print text in red			
		}
	}

	/****************Key listener to type return key(enter)*****************/
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
	
	/*******************Action listener for each button********************/
	class EventHandler implements ActionListener {
		public void actionPerformed(ActionEvent e){
			//Click login button
			if(e.getSource() == bt_Login) 
				Login_Setting();

			//Terminate when cancel button is clicked
			else if (e.getSource() == bt_Cancel) {
				System.exit(0);
			} 

			//Move to sign up window
			else if(e.getSource() == bt_SignUp){
				setVisible(false);
				GUI_SignUp gui_signup = new GUI_SignUp();
			    gui_signup.setVisible(true);
			}
		}
	}

	class EventHandler1 implements WindowListener	{ 		
		// Close when close button of Frame is clicked
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

