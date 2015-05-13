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
	JPanel pn_box = new JPanel();

	JLabel lb_Id = new JLabel("ID : ", Label.RIGHT); // Label�� text������ ����������.
	JLabel lb_Pass = new JLabel("Password :", Label.RIGHT);
	JLabel valid = new JLabel("���̵�� ��й�ȣ�� �Է��� �ּ���.");
	

	TextField tf_Id  = new TextField(20);
	TextField tf_Pwd = new TextField(20);
	JButton bt_SignUp = new JButton("ȸ������");		//ȸ���������� �ٲ�
	JButton bt_Cancel = new JButton("���");
	JButton bt1 = new JButton("dd");
	
	public GUI_SignUp(){
		super("AESS SignUp"); // Frame(String title)�� ȣ���Ѵ�.
		super.setIconImage(icon.getImage());
		setLayout(null);
		
		dbcon = new DBconnect();
		conn = dbcon.connect();
		
		this.setSize(400,250);
		x_l = screenSize.width/2 - this.getWidth()/2 ; //x��ǥ���ϱ�
		y_l = screenSize.height/2 - this.getHeight()/2; //y��ǥ���ϱ�
		this.setLocation(x_l, y_l);
		
		this.addWindowListener(new EventHandler1());
		this.setVisible(true);
		tf_Pwd.setEchoChar('*'); // �Է��� �� ��� '*'�� ���̰� �Ѵ�.
		
		// ��ư�� TextField�� �̺�Ʈó���� ���� Listener�� �߰����ش�.
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
	
	/*Login_set �Լ� ����*/
	
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
				/*ȸ������ �� ���̵�� ��й�ȣ ���*/
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
		// Frame�� �ݱ� ��ư�� ������ �� ȣ��ȴ�.
		public void windowClosing(WindowEvent e) {
			// Frame�� ȭ�鿡�� ������ �ʵ��� �ϰ�
			e.getWindow().setVisible(false);
			e.getWindow().dispose();
			System.exit(0);		
			}

		//�ƹ����뵵 ���� �޼��� ����
		public void windowClosed(WindowEvent e){} 
		public void windowIconified(WindowEvent e){} 
		public void windowDeiconified(WindowEvent e){} 
		public void windowActivated(WindowEvent e){}
		public void windowDeactivated(WindowEvent e){}
	}
}
