import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

class GUI_Login extends JFrame{
	
	
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
	JButton bt_Login = new JButton("로그인");
	JButton bt_Cancel = new JButton("취소");
	JButton bt_SignUp = new JButton("회원가입");
	JButton bt1 = new JButton("dd");
	

	public GUI_Login(){
		super("AESS LOGIN"); // Frame(String title)을 호출한다.
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
		
		bt_Login.addActionListener(new EventHandler());
		bt_Login.addKeyListener(new KeyHandler());
		bt_Cancel.addActionListener(new EventHandler());
		
		/*회원가입 버튼 기능*/
		bt_SignUp.addActionListener(new EventHandler());
		
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

		bt_Login.setBounds(70, 153, 80, 21);
		bt_Cancel.setBounds(160, 153, 80, 21);
		bt_SignUp.setBounds(250, 153, 80, 21);
		add(bt_Login);
		add(bt_Cancel);
		/*회원가입 기능*/
		add(bt_SignUp);
		
		valid.setBounds(105, 190, 220, 21);
		add(valid);
		add(pn_box);		
		
		this.setResizable(false);
	}

	void Login_Set() {
		char nowTest;
		int notYet=0;
		if(info.test.equals("중간")) nowTest='M';
		else nowTest='F';
		
		Calendar c = Calendar.getInstance();
		Calendar c_periodStart = Calendar.getInstance();
		c.add(Calendar.DATE, 14);
		
		String qry = "select * from period where year='"+info.year+"' and semester='"+info.season+"' and test='"+nowTest+"';";
		try {
			query = conn.createStatement();
			result = query.executeQuery(qry);
			while(result.next()) {
				c_periodStart.set(result.getInt("year"), result.getInt("month")-1, result.getInt("sDate"));
				System.out.printf("%d %d %d",result.getInt("year"), result.getInt("month")-1, result.getInt("sDate"));
				info.month = result.getInt("month");
				info.week = result.getInt("week");
				info.sDate = result.getInt("sDate");
				info.eDate = result.getInt("eDate");
				if(result.getString("year")==null) notYet=1;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.printf("오늘로 부터 14일 이후 : %d %d %d\n",c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DATE));
		System.out.printf("시험 시작일 : %d %d %d\n",c_periodStart.get(Calendar.YEAR), c_periodStart.get(Calendar.MONTH)+1, c_periodStart.get(Calendar.DATE));
		
		
		if(c.compareTo(c_periodStart)<0) notYet=1;
		
		System.out.println(c.compareTo(c_periodStart)+","+notYet);
		
		String id = tf_Id.getText();  // tfId에 입력되어있는 text를 얻어온다.
		String password = tf_Pwd.getText();
		
		qry = "select * from member where id='"+id+"' and pass='"+password+"';";
		try {
			query = conn.createStatement();
			result = query.executeQuery(qry);
			
			if(!result.next()){
				valid.setText("아이디 또는 비밀번호가 잘못되었습니다.");
				valid.setForeground(Color.red); // 글자 빨간색으로 출력
				
				return;
			} else {
				if(notYet==1&&!result.getString("type").equals("A")) {
					valid.setText("강의실예약시스템 사용 기간이 아닙니다.");
					valid.setForeground(Color.red); // 글자 빨간색으로 출력
					return;
				} else {
					GUI_Main gui = new GUI_Main(id);
					gui.setVisible(true);
					setVisible(false);
				}
			}
		}
		catch(SQLException se) {
			System.out.println("Connection Fail :"+se.getMessage());
		}
		
		try {
			conn.close();
		}
		catch (SQLException con_e) {
			// TODO Auto-generated catch block
			con_e.printStackTrace();
		}
	}

	
	
	class KeyHandler implements KeyListener
	{
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) Login_Set();
		}

		@Override
		public void keyReleased(KeyEvent arg0) {}

		@Override
		public void keyTyped(KeyEvent arg0) {}
	}
	
	class EventHandler implements ActionListener {
		public void actionPerformed(ActionEvent e){
			
			if(e.getSource() == bt_Login) Login_Set();

			else if (e.getSource() == bt_Cancel) {
				setVisible(false);
				dispose();
				System.exit(0);
			} 
			/*회원가입 버튼 기능 추가*/
			else if(e.getSource() == bt_SignUp){
				System.out.printf("1\n");
				GUI_SignUp gui = new GUI_SignUp();
			    gui.setVisible(true);
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

