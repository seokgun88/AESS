import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
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
 *
 */
public class GUI_UserList extends JPanel implements ActionListener, ListSelectionListener {
	private JList userJList ;
	private JScrollPane scrollPane;
	private JRadioButton [] listState = new JRadioButton[3]; //0: ����, 1: �ű�, 2: ��ü
	private ButtonGroup bg;
	private JButton btnApprv, btnDelete, btnTogAct; //���� ���� ��ư, ���� ���� ��ư, Ȱ�� ���� ��� ��ư
	private JPanel [] horPane = new JPanel[2] ; //�������� ��ü���� ��ġ�� �������� �ױ� ���� �г�
//	private String [] allUserList, newUserList, userList;
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
		userJList.addListSelectionListener(this);
		
		//��ü ���
//		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLayout(null);
		horPane[0].setLayout(new FlowLayout(FlowLayout.LEFT));
		horPane[0].add(listState[0]);
		horPane[0].add(listState[1]);
		horPane[0].add(listState[2]);
		add(horPane[0]);
		horPane[1].setLayout(new FlowLayout(FlowLayout.LEFT));
		horPane[1].add(btnApprv);
		horPane[1].add(btnDelete);
		horPane[1].add(btnTogAct);
		add(horPane[1]);
		add(scrollPane);
		
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
			if(admin.isUserActivated(curList[i]))
				buf="";
			else
				buf=deactTag;
			tempList[i]=curList[i]+buf;
		}
		
		userJList.setListData(tempList);
		
//		System.out.println(curList);
//		for(int i=0; i<curList.length; i++){
//			System.out.println(curList[i]);
//		}
	}
	
	//Refresh Views
	public void refreshView(){
		try{
			Rectangle bndry=this.getBounds();
			int btn_h = 40;
			horPane[0].setBounds(0, 0, bndry.width, btn_h);
			horPane[1].setBounds(0, (int) horPane[0].getBounds().getMaxY(), bndry.width, btn_h);
			scrollPane.setBounds(0, (int) horPane[1].getBounds().getMaxY(), bndry.width, bndry.height-btn_h*2);
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
			if(userJList.getSelectedValue().toString().contains(deactTag))
				
				admin.activateUser(curList[userJList.getSelectedIndex()], true);
			else
				admin.activateUser(curList[userJList.getSelectedIndex()], false);
		}
		
		refreshList();
	}
	public void valueChanged(ListSelectionEvent e) {
		//������ ���� Ȯ�ο�
//		System.out.println(userJList.getSelectedValue().toString());
	}	
}