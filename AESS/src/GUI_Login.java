import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

class GUI_Login extends JFrame{	
	static Connection conn;
	
	Toolkit tk = Toolkit.getDefaultToolkit(); //������ Toolkit��ü�� ��´�.
	Dimension screenSize=tk.getScreenSize(); //ȭ���� ũ�⸦ ���Ѵ�.
	
	/***************�ΰ� ����******************/
	ImageIcon icon = new ImageIcon("icon.png");
	ImageIcon small_logo = new ImageIcon("logo_small.png");
	JLabel lbSmallLogo = new JLabel(small_logo);
	
	/**************�α��� ������Ʈ ����***************/
	JLabel lb_Id = new JLabel("ID : ", Label.RIGHT); // Label.RIGHT : Label�� text������ ����������.
	JLabel lb_Pass = new JLabel("Password :", Label.RIGHT);
	JLabel lb_Valid = new JLabel("���̵�� ��й�ȣ�� �Է��� �ּ���.");
	TextField tf_Id  = new TextField(20);
	TextField tf_Pwd = new TextField(20);
	JButton bt_Login = new JButton("�α���");
	JButton bt_Cancel = new JButton("���");	
	JButton bt_SignUp = new JButton("ȸ������");

	/*************����Ʈ ������*****************/
	public GUI_Login(Connection conn){
		super("AESS LOGIN"); // Frame(String title)�� ȣ���Ѵ�.
		super.setIconImage(icon.getImage());
		setLayout(null);
		this.conn = conn;
		Login.setConn(conn);
		
		/*************â ��ġ ����***************/
		this.setSize(400,250);
		int x = screenSize.width/2 - this.getWidth()/2 ; //x��ǥ���ϱ�
		int y = screenSize.height/2 - this.getHeight()/2; //y��ǥ���ϱ�
		this.setLocation(x, y);
		
		this.addWindowListener(new EventHandler1());
		this.setVisible(true);
		
		tf_Pwd.setEchoChar('*'); // �Է��� �� ��� '*'�� ���̰� �Ѵ�.
		
		/******��ư�� TextField�� �̺�Ʈó���� ���� Listener�� �߰����ش�.****/
		tf_Id.addKeyListener(new KeyHandler());
		tf_Pwd.addKeyListener(new KeyHandler());
		
		bt_Login.addActionListener(new EventHandler());
		bt_Login.addKeyListener(new KeyHandler());
		bt_Cancel.addActionListener(new EventHandler());
		
		/**********************ȸ������ ��ư**********************/
		bt_SignUp.addActionListener(new EventHandler());
		
		lbSmallLogo.setBounds(40, 0, 321, 68);
		add(lbSmallLogo, "North");		
		
		/*************ID, ��й�ȣ, �α��� ������Ʈ add*************/
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
		/**********************ȸ������**********************/
		add(bt_SignUp);
		
		lb_Valid.setBounds(105, 190, 220, 21);
		add(lb_Valid);
		
		this.setResizable(false); //ũ�� ����
	}
	
	/****************�α��� ����� ���� ���*******************/
	public void Login_Setting(){
		String id = tf_Id.getText();
		String password = tf_Pwd.getText();
		int loginResult = Login.loginSetting(id, password);
		if(loginResult == 0){
			//gui ���� gui_main���� ����
			GUI_Main gui_main = new GUI_Main(id);
			gui_main.setVisible(true);
			dispose();
		}
		else if(loginResult == 1){
			lb_Valid.setText("���̵� �Ǵ� ��й�ȣ�� �߸��Ǿ����ϴ�.");
			lb_Valid.setForeground(Color.red); // ���� ���������� ���		
		}
		else if(loginResult == 2){
			lb_Valid.setText("���ǽǿ���ý��� ��� �Ⱓ�� �ƴմϴ�.");
			lb_Valid.setForeground(Color.red); // ���� ���������� ���			
		}
	}
	
	/****************���� �Է��� ���� Ű ������*****************/
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
	
	/*******************�� ��ư�� �׼� ������********************/
	class EventHandler implements ActionListener {
		public void actionPerformed(ActionEvent e){
			//�α��� ��ư Ŭ����
			if(e.getSource() == bt_Login) 
				Login_Setting();

			//��� ��ư Ŭ���� ����
			else if (e.getSource() == bt_Cancel) {
				System.exit(0);
			} 

			//ȸ������ â���� �̵�
			else if(e.getSource() == bt_SignUp){
				setVisible(false);
				GUI_SignUp gui_signup = new GUI_SignUp(conn);
			    gui_signup.setVisible(true);
			}
		}
	}

	class EventHandler1 implements WindowListener	{ 		
		// Frame�� �ݱ� ��ư Ŭ�� �� ����
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

