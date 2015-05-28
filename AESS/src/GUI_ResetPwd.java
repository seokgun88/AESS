import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

/**************이영석 추가 : 학생 공지사항 확인 클래스******************/
public class GUI_ResetPwd extends JPanel implements MouseListener{
	private ResetPwd resetpwd; //비밀번호 초기화 클래스
	private JLabel lb_resetpwd; //비밀번호 초기화인원
	private JTable t_participateClass; //참여중인 목록 테이블
	private JScrollPane sp_participateClass;
	private JTextArea ta_notice = new JTextArea(30, 60); //공지사항 읽어올 textarea
	
	public GUI_ResetPwd(){
		resetpwd = new ResetPwd();
		this.setLayout(new BorderLayout());
		
		/*************************강의목록 리스트*****************************/
		Vector v_participateClassHead = new Vector(); //초기화 신청 인원 목록
		v_participateClassHead.addElement("인원목록"); //테이블 헤드 이름
		Vector v_participateClass = resetpwd.getParticipateClasss(); //인원 목록 받아오기
		t_participateClass = new JTable(new DefaultTableModel(v_participateClass ,v_participateClassHead){
			public boolean isCellEditable(int i, int c){ //셀 내용 수정 안되게 설정
				return false;
			}
		}); //테이블 생성
		t_participateClass.getTableHeader().setReorderingAllowed(false); //테이블 헤더 드래그 불가
		t_participateClass.addMouseListener(this); //셀 마우스 리스너 등록
		sp_participateClass = new JScrollPane(t_participateClass);
		sp_participateClass.setPreferredSize(new Dimension(100, 400));
		add(sp_participateClass, "West");
				
	}
	
	public void mouseClicked(MouseEvent arg0) {
		/****************비밀번호 초기화*****************/
		int row = t_participateClass.getSelectedRow();
		int col = t_participateClass.getSelectedColumn();
		String list = (String)t_participateClass.getValueAt(row, col);
		
		if(JOptionPane.showConfirmDialog(null,"비밀번호 초기화 하겠습니까?", "비밀번호 초기화",
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)==JOptionPane.YES_OPTION)
		{
			System.out.println(list);  //사용자 a의 비밀번호 초기화
		} 
		
		
	}	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub		
	}
}
