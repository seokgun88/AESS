import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.Connection;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author Seiryu
 *
 */
public class GUI_UserList extends JPanel implements ActionListener, ListSelectionListener {
	private JList userJList ;
	private JScrollPane scrollPane;
	private JRadioButton [] listState = new JRadioButton[3]; //0: ����, 1: �ű�, 2: ��ü
	private ButtonGroup bg;
	private JButton btnApprv, btnDelete, btnTogAct; //���� ���� ��ư, ���� ���� ��ư, Ȱ�� ���� ��� ��ư
	private JButton btnReset; //��й�ȣ �ʱ�ȭ ��ư
	private JPanel p_east; //��ư���� �ϳ��� ���� �г�
	private JPanel [] horPane = new JPanel[2] ; //�������� ��ü���� ��ġ�� �������� �ױ� ���� �г�
	private String [] curList, tempList;
	
	private User_Admin admin;
	private Connection conn;
	private String id;
	
	static final String deactTag="[��Ȱ��ȭ]";//��Ȱ��ȭ �±�
	
	public GUI_UserList(String id){
		conn = Info.getConn();
		this.id = id;
		admin = new User_Admin();
		
		//��ü ����
		listState[0]=new JRadioButton("���� �����");
		listState[1]=new JRadioButton("�ű� ����");
		listState[2]=new JRadioButton("��ü");
		bg=new ButtonGroup();
		bg.add(listState[0]);
		bg.add(listState[1]);
		bg.add(listState[2]);
		btnApprv=new JButton("���� �㰡");
		btnDelete=new JButton("���� ����");
		btnTogAct=new JButton("���� ��/Ȱ��ȭ");
		btnReset=new JButton("��й�ȣ �ʱ�ȭ");
		horPane[0]=new JPanel();
		horPane[1]=new JPanel();
		userJList=new JList<String>();
		scrollPane=new JScrollPane();
		scrollPane.setViewportView(userJList);
		
		
		//��ü �ʱ�ȭ
		listState[0].addActionListener(this);
		listState[1].addActionListener(this);
		listState[2].addActionListener(this);
		btnApprv.addActionListener(this);
		btnDelete.addActionListener(this);
		btnTogAct.addActionListener(this);
		btnReset.addActionListener(this);
		userJList.addListSelectionListener(this);
		
		//��ü ���
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
	
	//Refresh the list. Including visualization.
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
				buf += "[���� ��û]";
			else if(admin.isUserRequireActivate(curList[i]))
				buf += "[Ȱ��ȭ ��û]";
			else if(admin.isRequireResetPass(curList[i]))
				buf += "[��й�ȣ �ʱ�ȭ]";
			//���� ����
			tempList[i] = curList[i] + buf + '[' + admin.getUserType(curList[i]) + ']';
		}
		
		userJList.setListData(tempList);
	}
	
	//Refresh Views
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
			//���� ���� ��ư
			admin.approveUser(curList[userJList.getSelectedIndex()]);
		} else if(arg0.getSource().equals(btnDelete)){
			//���� ���� ��ư
			admin.deleteUser(curList[userJList.getSelectedIndex()]);
		} else if(arg0.getSource().equals(btnTogAct)){
			//���� ��/Ȱ��ȭ��ư
			if(userJList.getSelectedValue().toString().contains(deactTag) || userJList.getSelectedValue().toString().contains("[Ȱ��ȭ ��û]"))	
				admin.activateUser(curList[userJList.getSelectedIndex()], "T");
			else
				admin.activateUser(curList[userJList.getSelectedIndex()], "F");
		} else if(arg0.getSource().equals(btnReset)){
			//��й�ȣ �ʱ�ȭ ��ư
			admin.resetPassword(curList[userJList.getSelectedIndex()]);
		}
		
		refreshList();
	}
	public void valueChanged(ListSelectionEvent e) {
		//������ ���� Ȯ�ο�
//		System.out.println(userJList.getSelectedValue().toString());
	}	
}