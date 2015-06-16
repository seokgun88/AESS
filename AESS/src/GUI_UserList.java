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
	private JRadioButton [] listState = new JRadioButton[3]; //0: 기존, 1: 신규, 2: 전체
	private ButtonGroup bg;
	private JButton btnApprv, btnDelete, btnTogAct; //가입 승인 버튼, 계정 삭제 버튼, 활성 상태 토글 버튼
	private JButton btnReset; //비밀번호 초기화 버튼
	private JPanel p_east; //버튼들을 하나로 묶는 패널
	private JPanel [] horPane = new JPanel[2] ; //수평으로 객체들을 배치해 수직으로 쌓기 위한 패널
	private String [] curList, tempList;
	
	private User_Admin admin;
	private Connection conn;
	private String id;
	
	static final String deactTag="[비활성화]";//비활성화 태그
	
	public GUI_UserList(String id){
		conn = Info.getConn();
		this.id = id;
		admin = new User_Admin();
		
		//객체 생성
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
		
		
		//객체 초기화
		listState[0].addActionListener(this);
		listState[1].addActionListener(this);
		listState[2].addActionListener(this);
		btnApprv.addActionListener(this);
		btnDelete.addActionListener(this);
		btnTogAct.addActionListener(this);
		btnReset.addActionListener(this);
		userJList.addListSelectionListener(this);
		
		//객체 등록
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
			if(admin.isUserInactivated(curList[i]))
				buf=deactTag;
			else if(admin.isUserLeave(curList[i]))
				buf="[휴학신청]";
			else if(admin.isUserRequireActivate(curList[i]))
				buf="[활성화신청]";
			else
				buf="";
			//지수 수정
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
			//가입 승인 버튼
			admin.approveUser(curList[userJList.getSelectedIndex()]);
		} else if(arg0.getSource().equals(btnDelete)){
			//계정 삭제 버튼
			admin.deleteUser(curList[userJList.getSelectedIndex()]);
		} else if(arg0.getSource().equals(btnTogAct)){
			//계정 비/활성화버튼
			if(userJList.getSelectedValue().toString().contains(deactTag) || userJList.getSelectedValue().toString().contains("[활성화신청]"))				
				admin.activateUser(curList[userJList.getSelectedIndex()], "T");
			else
				admin.activateUser(curList[userJList.getSelectedIndex()], "F");
		} else if(arg0.getSource().equals(btnReset)){
			//비밀번호 초기화 버튼
			admin.resetPassword(curList[userJList.getSelectedIndex()]);
		}
		
		refreshList();
	}
	public void valueChanged(ListSelectionEvent e) {
		//아이템 선택 확인용
//		System.out.println(userJList.getSelectedValue().toString());
	}	
}