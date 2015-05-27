/*
 * GUI_Login ������ ���� �״�� �賦
 * ���� �ٸ� �κ��� �ּ�ó�� �ϰ���
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
	
	Toolkit tk = Toolkit.getDefaultToolkit(); //������ Toolkit��ü�� ��´�.
	Dimension screenSize=tk.getScreenSize();//ȭ���� ũ�⸦ ���Ѵ�.
	int x_l, y_l;
	
	ImageIcon icon = new ImageIcon("icon.png");
	ImageIcon small_logo = new ImageIcon("logo_small.png");
	JLabel lbSmallLogo = new JLabel(small_logo);
	
	JPanel pn_id = new JPanel();
	JPanel pn_password = new JPanel();
	JPanel pn_button = new JPanel();

	JLabel lb_Id = new JLabel("ID : ", Label.RIGHT); // Label�� text������ ����������.
	JLabel lb_Name = new JLabel("Name :", Label.RIGHT);
	JLabel lb_Pass = new JLabel("Password :", Label.RIGHT);
	
	boolean isSignUp; //##���� �߰�
	
	TextField tf_Id  = new TextField(20);
	TextField tf_Pwd = new TextField(20);
	TextField tf_Name = new TextField(20);
	JButton bt_SignUp = new JButton("ȸ������");		//ȸ���������� �ٲ�
	JButton bt_Cancel = new JButton("���");
	JButton bt1 = new JButton("dd");
	
	/*********�̿��� �߰� : �л�, ���� �� ��� ȸ������ �ϰ� �ϴ� ���� ��ư************/
	ButtonGroup bg_stdOrPrf = new ButtonGroup();
	JRadioButton rbt_student = new JRadioButton("�л�");
	JRadioButton rbt_professor = new JRadioButton("����");
		
	public GUI_SignUp(Connection conn){
		super("AESS SignUp"); // Frame(String title)�� ȣ���Ѵ�.
		super.setIconImage(icon.getImage());
		setLayout(null);
		this.conn = conn;
		Login.setConn(conn);
		
		dbcon = new DBconnect();
		conn = dbcon.connect();
		
		this.setSize(400,300);
		x_l = screenSize.width/2 - this.getWidth()/2 ; //x��ǥ���ϱ�
		y_l = screenSize.height/2 - this.getHeight()/2; //y��ǥ���ϱ�
		this.setLocation(x_l, y_l);
		
		this.addWindowListener(new EventHandler1());
		this.setVisible(true);
		tf_Pwd.setEchoChar('*'); // �Է��� �� ��� '*'�� ���̰� �Ѵ�.
		
		// ��ư�� TextField�� �̺�Ʈó���� ���� Listener�� �߰����ش�.
		
		bt_SignUp.addActionListener(new EventHandler());
		bt_Cancel.addActionListener(new EventHandler());
		
		lbSmallLogo.setBounds(40, 0, 321, 68);
		add(lbSmallLogo, "North");
		
		
		lb_Id.setBounds(117, 80, 30, 22);
		tf_Id.setBounds(150, 80, 180, 22);
		add(lb_Id);
		add(tf_Id);	
		
		/*Name �߰�*/
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
		
		/**********�̿��� �߰� : �л�, ���� ������ư ���************/
		bg_stdOrPrf.add(rbt_student);
		bg_stdOrPrf.add(rbt_professor);
		rbt_student.setBounds(115, 173, 80, 21);
		rbt_professor.setBounds(205, 173, 80, 21);
		add(rbt_student);
		add(rbt_professor);
		
		this.setResizable(false);
	}
	
	/**Login_set �Լ� ����**/
	/**�̿��� ���� : ����Ű ������ ����**/
	class EventHandler implements ActionListener {
		public void actionPerformed(ActionEvent e){					
			if(e.getSource() == bt_SignUp){
				GUI_Login gui_login = new GUI_Login(conn);
				String type = "notSelect";
				
				if(rbt_student.isSelected())
					type = "S";
				else if(rbt_professor.isSelected())
					type = "P";
				
				ManageUser.setConn(conn);
				ManageUser.signUp(tf_Id.getText(), tf_Pwd.getText(), tf_Name.getText(), type);
				
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
		// Frame�� �ݱ� ��ư�� ������ �� ȣ��ȴ�.
		public void windowClosing(WindowEvent e) {
			// Frame�� ȭ�鿡�� ������ �ʵ��� �ϰ�
			System.exit(0);		
		}
		//�ƹ����뵵 ���� �޼��� ����
		public void windowOpened(WindowEvent e) {} 
		public void windowClosed(WindowEvent e){} 
		public void windowIconified(WindowEvent e){} 
		public void windowDeiconified(WindowEvent e){} 
		public void windowActivated(WindowEvent e){}
		public void windowDeactivated(WindowEvent e){}
	}
}
