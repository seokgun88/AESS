/*
 * GUI_Login 구성을 거의 그대로 배낌
 * 조금 다른 부분은 주석처리 하겠음
 */
import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class GUI_SignUp extends JFrame{
	static DBconnect dbcon;
	static Connection conn;
	static Statement query;
	static ResultSet result;
	
	Toolkit tk = Toolkit.getDefaultToolkit(); //구현된 Toolkit객체를 얻는다.
	Dimension screenSize=tk.getScreenSize();//화면의 크기를 구한다.
	int x_l, y_l;
	
	ImageIcon icon = new ImageIcon("icon.png");
	ImageIcon small_logo = new ImageIcon("logo_small.png");
	JLabel lbSmallLogo = new JLabel(small_logo);
	
	JPanel pn_id = new JPanel();
	JPanel pn_password = new JPanel();
	JPanel pn_button = new JPanel();
	JPanel pn_box = new JPanel();

	JLabel lb_Id = new JLabel("ID : ", Label.RIGHT); // Label의 text정렬을 오른쪽으로.
	JLabel lb_Name = new JLabel("Name :", Label.RIGHT);
	JLabel lb_Pass = new JLabel("Password :", Label.RIGHT);
	JLabel valid = new JLabel("아이디와 비밀번호를 입력해 주세요.");
	
	boolean isSignUp;
	
	TextField tf_Id  = new TextField(20);
	TextField tf_Pwd = new TextField(20);
	TextField tf_Name = new TextField(20);
	JButton bt_SignUp = new JButton("회원가입");		//회원가입으로 바꿈
	JButton bt_Cancel = new JButton("취소");
	JButton bt1 = new JButton("dd");
	
	private String Name;
	
	public GUI_SignUp(Connection conn){
		super("AESS SignUp"); // Frame(String title)을 호출한다.
		super.setIconImage(icon.getImage());
		setLayout(null);
		this.conn = conn;
		Login.setConn(conn);
		
		dbcon = new DBconnect();
		conn = dbcon.connect();
		
		this.setSize(400,300);
		x_l = screenSize.width/2 - this.getWidth()/2 ; //x좌표구하기
		y_l = screenSize.height/2 - this.getHeight()/2; //y좌표구하기
		this.setLocation(x_l, y_l);
		
		this.addWindowListener(new EventHandler1());
		this.setVisible(true);
		tf_Pwd.setEchoChar('*'); // 입력한 값 대신 '*'이 보이게 한다.
		
		// 버튼과 TextField에 이벤트처리를 위한 Listener를 추가해준다.
		tf_Id.addKeyListener(new KeyHandler());
		tf_Name.addKeyListener(new KeyHandler());
		tf_Pwd.addKeyListener(new KeyHandler());
		
		bt_SignUp.addActionListener(new EventHandler());
		bt_SignUp.addKeyListener(new KeyHandler());
		bt_Cancel.addActionListener(new EventHandler());
		
		lbSmallLogo.setBounds(40, 0, 321, 68);
		add(lbSmallLogo, "North");
		
		
		lb_Id.setBounds(117, 80, 30, 22);
		tf_Id.setBounds(150, 80, 180, 22);
		add(lb_Id);
		add(tf_Id);	
		
		/*Name 추가*/
		lb_Name.setBounds(95, 110, 50, 22);
		tf_Name.setBounds(150, 110, 180, 22);
		add(lb_Name);
		add(tf_Name);
		
		lb_Pass.setBounds(73, 140, 70, 22);
		tf_Pwd.setBounds(150, 140, 180, 22);
		add(lb_Pass);
		add(tf_Pwd);	

		bt_SignUp.setBounds(115, 183, 80, 21);
		bt_Cancel.setBounds(205, 183, 80, 21);
		add(bt_SignUp);
		add(bt_Cancel);
		
		valid.setBounds(105, 213, 220, 21);
		add(valid);
		add(pn_box);		
		
		this.setResizable(false);
	}
	
	/*Login_set 함수 삭제*/
	
	class KeyHandler implements KeyListener
	{
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				GUI_Login gui_login = new GUI_Login(conn);
				System.out.printf("ID : %s\nName : %s\nPW : %s\n",tf_Id.getText(), tf_Name.getText(), tf_Pwd.getText());
				/*이름 조건*/
				setName(tf_Name.getText());
				if(getName().contains("0") || getName().contains("1") || getName().contains("2") || getName().contains("3") || getName().contains("4") || getName().contains("5") || getName().contains("6") || getName().contains("7") || getName().contains("8") || getName().contains("9"))
					isSignUp = false;
				if(getName().contains(")") || getName().contains("!") || getName().contains("@") || getName().contains("#") || getName().contains("$") || getName().contains("%") || getName().contains("^") || getName().contains("&") || getName().contains("*") || getName().contains("("))
					isSignUp = false;
				if(getName().contains("[") || getName().contains("]") || getName().contains("{")  || getName().contains("}") || getName().contains("-") || getName().contains("=") || getName().contains("_") || getName().contains("+") || getName().contains(";") || getName().contains("'") || getName().contains(":") || getName().contains("\"") || getName().contains("|") || getName().contains("\\") || getName().contains(",") || getName().contains(".") || getName().contains("/") || getName().contains("<") || getName().contains(">") || getName().contains("?") || getName().contains("`") || getName().contains("~"))
					isSignUp = false;
				isSignUp = true;
				setVisible(false);
				dispose();
				gui_login.setVisible(true);
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {}

		@Override
		public void keyTyped(KeyEvent arg0) {}
	}
	
	public void setName(String name){
		Name = name;
	}
	
	public String getName(){
		return Name;
	}

	class EventHandler implements ActionListener {
		public void actionPerformed(ActionEvent e){		
			
			if(e.getSource() == bt_SignUp){
				GUI_Login gui_login = new GUI_Login(conn);
				/*회원가입 시 아이디랑 비밀번호 출력*/
				System.out.printf("ID : %s\nName : %s\nPW : %s\n",tf_Id.getText(), tf_Name.getText(), tf_Pwd.getText());
				/*이름 조건*/
				String Name = tf_Name.getText();
				if(Name.contains("0") || Name.contains("1") || Name.contains("2") || Name.contains("3") || Name.contains("4") || Name.contains("5") || Name.contains("6") || Name.contains("7") || Name.contains("8") || Name.contains("9"))
					isSignUp = false;
				if(Name.contains(")") || Name.contains("!") || Name.contains("@") || Name.contains("#") || Name.contains("$") || Name.contains("%") || Name.contains("^") || Name.contains("&") || Name.contains("*") || Name.contains("("))
					isSignUp = false;
				if(Name.contains("[") || Name.contains("]") || Name.contains("{")  || Name.contains("}") || Name.contains("-") || Name.contains("=") || Name.contains("_") || Name.contains("+") || Name.contains(";") || Name.contains("'") || Name.contains(":") || Name.contains("\"") || Name.contains("|") || Name.contains("\\") || Name.contains(",") || Name.contains(".") || Name.contains("/") || Name.contains("<") || Name.contains(">") || Name.contains("?") || Name.contains("`") || Name.contains("~"))
					isSignUp = false;
				isSignUp = true;
				setVisible(false);
				dispose();
				gui_login.setVisible(true);
			}

			else if (e.getSource() == bt_Cancel) {
				GUI_Login gui_login = new GUI_Login(conn);
				setVisible(false);
				dispose();
				gui_login.setVisible(true);
			} 
		}
	} // class EventHandler

	class EventHandler1 implements WindowListener 
	{ 
		public void windowOpened(WindowEvent e) {} 
		// Frame의 닫기 버튼을 눌렀을 때 호출된다.
		public void windowClosing(WindowEvent e) {
			// Frame을 화면에서 보이지 않도록 하고
			e.getWindow().setVisible(false);
			e.getWindow().dispose();
			System.exit(0);		
			}

		//아무내용도 없는 메서드 구현
		public void windowClosed(WindowEvent e){} 
		public void windowIconified(WindowEvent e){} 
		public void windowDeiconified(WindowEvent e){} 
		public void windowActivated(WindowEvent e){}
		public void windowDeactivated(WindowEvent e){}
	}
}
