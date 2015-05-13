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
	JLabel lb_Pass = new JLabel("Password :", Label.RIGHT);
	JLabel valid = new JLabel("아이디와 비밀번호를 입력해 주세요.");
	

	TextField tf_Id  = new TextField(20);
	TextField tf_Pwd = new TextField(20);
	JButton bt_SignUp = new JButton("회원가입");		//회원가입으로 바꿈
	JButton bt_Cancel = new JButton("취소");
	JButton bt1 = new JButton("dd");
	
	public GUI_SignUp(){
		super("AESS SignUp"); // Frame(String title)을 호출한다.
		super.setIconImage(icon.getImage());
		setLayout(null);
		
		dbcon = new DBconnect();
		conn = dbcon.connect();
		
		this.setSize(400,250);
		x_l = screenSize.width/2 - this.getWidth()/2 ; //x좌표구하기
		y_l = screenSize.height/2 - this.getHeight()/2; //y좌표구하기
		this.setLocation(x_l, y_l);
		
		this.addWindowListener(new EventHandler1());
		this.setVisible(true);
		tf_Pwd.setEchoChar('*'); // 입력한 값 대신 '*'이 보이게 한다.
		
		// 버튼과 TextField에 이벤트처리를 위한 Listener를 추가해준다.
		tf_Id.addKeyListener(new KeyHandler());
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
		
		lb_Pass.setBounds(70, 110, 80, 22);
		tf_Pwd.setBounds(150, 110, 180, 22);
		add(lb_Pass);
		add(tf_Pwd);

		bt_SignUp.setBounds(115, 153, 80, 21);
		bt_Cancel.setBounds(205, 153, 80, 21);
		add(bt_SignUp);
		add(bt_Cancel);
		
		valid.setBounds(105, 190, 220, 21);
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
				System.out.printf("ID : %d\n PW : %d\n",tf_Id.getText(), tf_Pwd.getText());
				setVisible(false);
				dispose();
				System.exit(0);
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {}

		@Override
		public void keyTyped(KeyEvent arg0) {}
	}

	class EventHandler implements ActionListener {
		public void actionPerformed(ActionEvent e){		
			
			if(e.getSource() == bt_SignUp){
				/*회원가입 시 아이디랑 비밀번호 출력*/
				System.out.printf("ID : %s\nPW : %s\n",tf_Id.getText(), tf_Pwd.getText());
				setVisible(false);
				dispose();
			}

			else if (e.getSource() == bt_Cancel) {
				setVisible(false);
				dispose();
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
