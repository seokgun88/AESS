import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author Seiryu
 * User List GUI
 */
public class GUI_UserList extends JPanel implements ActionListener, ListSelectionListener {
	private JList userJList ;
	private JScrollPane scrollPane;
	private JRadioButton [] listState = new JRadioButton[3]; //0: Existing, 1: New, 2: All
	private ButtonGroup bg;
	private JButton btnApprv, btnDelete, btnTogAct; //Approve, Delete, Toggle Activation
	private JButton btnReset; //Password reset button
	private JPanel p_east; //Panel to gather all buttons
	private JPanel [] horPane = new JPanel[2] ; //Panels to arrange instances horizontally, and lay them vertically.
	private String [] curList, tempList;
	
	private User_Admin admin;
	private Connection conn;
	private String id;
	
	static final String deactTag="[비활성화]";//Inactivation tag
	
	public GUI_UserList(String id){
		conn = Info.getConn();
		this.id = id;
		admin = new User_Admin();
		
		//Instantiate instances
		listState[0]=new JRadioButton("기존 사용자");
		listState[1]=new JRadioButton("신규 가입");
		listState[2]=new JRadioButton("전체");
		bg=new ButtonGroup();
		bg.add(listState[0]);
		bg.add(listState[1]);
		bg.add(listState[2]);
		btnApprv=new JButton("가입 허가");
		btnDelete=new JButton("계정 삭제");
		btnTogAct=new JButton("계정 비/활성화");
		btnReset=new JButton("비밀번호 초기화");
		horPane[0]=new JPanel();
		horPane[1]=new JPanel();
		userJList=new JList<String>();
		scrollPane=new JScrollPane();
		scrollPane.setViewportView(userJList);
		
		
		//Initialize instances
		listState[0].addActionListener(this);
		listState[1].addActionListener(this);
		listState[2].addActionListener(this);
		btnApprv.addActionListener(this);
		btnDelete.addActionListener(this);
		btnTogAct.addActionListener(this);
		btnReset.addActionListener(this);
		userJList.addListSelectionListener(this);
		
		//Add instances
		setLayout(new BorderLayout());
		p_east = new JPanel();
		p_east.setLayout(new BorderLayout());
		horPane[0].setLayout(new FlowLayout(FlowLayout.LEFT));
		horPane[0].add(listState[0]);
		horPane[0].add(listState[1]);
		horPane[0].add(listState[2]);
		p_east.add(horPane[0], "North");
		horPane[1].setLayout(new FlowLayout(FlowLayout.LEFT));
		horPane[1].add(btnApprv);
		horPane[1].add(btnDelete);
		horPane[1].add(btnTogAct);
		horPane[1].add(btnReset);
		p_east.add(horPane[1], "Center");
		add(p_east, "East");
		add(scrollPane, "Center");
		
		listState[0].setSelected(true);
		
		refreshList();
		refreshView();
	}
	
	/**Refresh the list. Including visualization.**/
	public void refreshList(){
		String buf;
		User_Admin.AccountState accSt;
		if(listState[0].isSelected())
			accSt=User_Admin.AccountState.AUTHED;
		else if(listState[1].isSelected())
			accSt=User_Admin.AccountState.NEW;
		else
			accSt=null;
		
		curList=admin.getUserList(accSt);
		tempList = new String[curList.length];
		for(int i=0;i<curList.length;i++){
			buf = "";
			if(admin.isUserInactivated(curList[i]))
				buf += deactTag;
			else if(admin.isUserLeave(curList[i]))
				buf += "[휴학 신청]";
			else if(admin.isUserRequireActivate(curList[i]))
				buf += "[활성화 신청]";
			else if(admin.isRequireResetPass(curList[i]))
				buf += "[비밀번호 초기화]";
			//지수 수정
			tempList[i] = curList[i] + buf + '[' + admin.getUserType(curList[i]) + ']';
		}
		
		userJList.setListData(tempList);
	}
	
	/**Refresh Views**/
	public void refreshView(){
		try{
			Rectangle bndry=this.getBounds();
			int btn_h = 40;
			scrollPane.setBounds(50, (int) horPane[1].getBounds().getMaxY(), bndry.width/2, bndry.height-btn_h*2);
		}catch(NullPointerException npe){}
	}
	
	@Override
	public void repaint(){
		refreshView();
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(btnApprv)){
			/**Account Approve button**/
			admin.approveUser(curList[userJList.getSelectedIndex()]);
		} else if(arg0.getSource().equals(btnDelete)){
			/**Account Delete button**/
			admin.deleteUser(curList[userJList.getSelectedIndex()]);
		} else if(arg0.getSource().equals(btnTogAct)){
			/**Account In/active button**/
			if(userJList.getSelectedValue().toString().contains(deactTag) || userJList.getSelectedValue().toString().contains("[활성화 신청]"))	
				admin.activateUser(curList[userJList.getSelectedIndex()], "T");
			else
				admin.activateUser(curList[userJList.getSelectedIndex()], "F");
		} else if(arg0.getSource().equals(btnReset)){
			/**Reset Password button**/
			admin.resetPassword(curList[userJList.getSelectedIndex()]);
		}
		
		refreshList();
	}
	/**To confirm selected item**/
	public void valueChanged(ListSelectionEvent e) {
//		System.out.println(userJList.getSelectedValue().toString());
	}	
}