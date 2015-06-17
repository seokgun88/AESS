/*
 * 지수 : GUI_Login 구성을 거의 그대로 배낌
 * 조금 다른 부분은 주석처리 하겠음
 */
import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

@SuppressWarnings("serial")
/**GUI for sign up page**/
public class GUI_SignUp extends JFrame{	
	private Toolkit tk = Toolkit.getDefaultToolkit();//Get implemented Toolkit instance
	private Dimension screenSize=tk.getScreenSize();//Get screen size
	private int x_l, y_l;	
	private boolean isSignUp; //##지수 추가
	
	private ImageIcon icon = new ImageIcon("icon.png");
	private ImageIcon small_logo = new ImageIcon("logo_small.png");
	private JLabel lbSmallLogo = new JLabel(small_logo);
	
	private JPanel pn_id = new JPanel();
	private JPanel pn_password = new JPanel();
	private JPanel pn_button = new JPanel();

	private JLabel lb_Id = new JLabel("ID : ", Label.RIGHT); //Right alignment of texts in Label
	private JLabel lb_Name = new JLabel("Name :", Label.RIGHT);
	private JLabel lb_Pass = new JLabel("Password :", Label.RIGHT);
	
	private TextField tf_Id  = new TextField(20);
	private TextField tf_Pwd = new TextField(20);
	private TextField tf_Name = new TextField(20);
	
	private JButton bt_SignUp = new JButton("회원가입"); //Change to sign up
	private JButton bt_Cancel = new JButton("취소");
	private JButton bt1 = new JButton("dd");
	
	/*********이영석 추가 : 학생, 교수 중 골라서 회원가입 하게 하는 라디오 버튼************/
	private ButtonGroup bg_stdOrPrf = new ButtonGroup();
	private JRadioButton rbt_student = new JRadioButton("학생");
	private JRadioButton rbt_professor = new JRadioButton("교수");
	
	/**Constructor**/
	public GUI_SignUp(){
		super("AESS SignUp"); // Call Frame(String title)
		super.setIconImage(icon.getImage());
		setLayout(null);
		Login.setConn(Info.getConn());
		
		this.setSize(400,300);
		x_l = screenSize.width/2 - this.getWidth()/2 ; //Get x coordinate
		y_l = screenSize.height/2 - this.getHeight()/2; //Get y coordinate
		this.setLocation(x_l, y_l);
		
		this.addWindowListener(new EventHandler1());
		this.setVisible(true);
		tf_Pwd.setEchoChar('*'); //show '*' instead real value
		
		/******Add listener to Button and TextField for event handle****/		
		bt_SignUp.addActionListener(new EventHandler());
		bt_Cancel.addActionListener(new EventHandler());
		
		lbSmallLogo.setBounds(40, 0, 321, 68);
		add(lbSmallLogo, "North");
		
		
		lb_Id.setBounds(117, 80, 30, 22);
		tf_Id.setBounds(150, 80, 180, 22);
		add(lb_Id);
		add(tf_Id);	
		
		/**Add Name**/
		lb_Name.setBounds(95, 110, 50, 22);
		tf_Name.setBounds(150, 110, 180, 22);
		add(lb_Name);
		add(tf_Name);
		
		lb_Pass.setBounds(73, 140, 70, 22);
		tf_Pwd.setBounds(150, 140, 180, 22);
		add(lb_Pass);
		add(tf_Pwd);	

		bt_SignUp.setBounds(115, 213, 80, 21);
		bt_Cancel.setBounds(205, 213, 80, 21);
		add(bt_SignUp);
		add(bt_Cancel);
		
		/**********이영석 추가 : 학생, 교수 라디오버튼 등록************/
		bg_stdOrPrf.add(rbt_student);
		bg_stdOrPrf.add(rbt_professor);
		rbt_student.setBounds(115, 173, 80, 21);
		rbt_professor.setBounds(205, 173, 80, 21);
		add(rbt_student);
		add(rbt_professor);
		
		this.setResizable(false);
	}
	
	/**Login_set removed**/
	
	/**이영석 삭제 : 엔터키 리스너 삭제**/
	class EventHandler implements ActionListener {
		public void actionPerformed(ActionEvent e){					
			if(e.getSource() == bt_SignUp){
				GUI_Login gui_login = new GUI_Login();
				String type = "notSelect";
				
				if(rbt_student.isSelected())
					type = "S";
				else if(rbt_professor.isSelected())
					type = "P";
				
				ManageUser.setConn(Info.getConn());
				ManageUser.signUp(tf_Id.getText(), tf_Pwd.getText(), tf_Name.getText(), type);
				
				setVisible(false);
				dispose();
				gui_login.setVisible(true);
			}

			else if (e.getSource() == bt_Cancel) {
				GUI_Login gui_login = new GUI_Login();
				setVisible(false);
				dispose();
				gui_login.setVisible(true);
			} 
		}
	} // class EventHandler

	class EventHandler1 implements WindowListener 
	{ 
		// Called when close button of Frame is clicked
		public void windowClosing(WindowEvent e) {
			// Set Frame invisible
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
