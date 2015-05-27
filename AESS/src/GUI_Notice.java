import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

/**************이영석 추가 : 학생 공지사항 확인 클래스******************/
public class GUI_Notice extends JPanel implements MouseListener{
	Notice notice; //공지사항 클래스
	private JLabel lb_notice; //공지사항 라벨
	private JTable t_participateClass; //참여중인 강의 목록 테이블
	private JScrollPane sp_participateClass;
	private JTextArea ta_notice = new JTextArea(30, 60); //공지사항 읽어올 textarea
	
	public GUI_Notice(Connection conn){
		notice = new Notice(conn);		
		this.setLayout(new BorderLayout());
		
		/*************************강의목록 리스트*****************************/
		Vector v_participateClassHead = new Vector(); //강의목록 테이블 헤드
		v_participateClassHead.addElement("강의목록"); //테이블 헤드 이름
		Vector v_participateClass = notice.getParticipateClasss(); //강의 목록 받아오기
		if(v_participateClass.size() == 0){
			v_participateClass.addElement("참여중인 강의가 없습니다.");
		}
		t_participateClass = new JTable(new DefaultTableModel(v_participateClass ,v_participateClassHead){
			public boolean isCellEditable(int i, int c){ //셀 내용 수정 안되게 설정
				return false;
			}
		}); //테이블 생성
		t_participateClass.addMouseListener(this); //셀 마우스 리스너 등록
		sp_participateClass = new JScrollPane(t_participateClass);
		sp_participateClass.setPreferredSize(new Dimension(100, 500));
		add(sp_participateClass, "West");
				
		/*******************선택한 강의 공지사항 출력할 textarea 설정*******************/
		lb_notice = new JLabel("                                   공지사항");
		add(lb_notice, "North");
		
		ta_notice.setEditable(false); //읽어온 공지사항 수정 불가
		add(ta_notice, "Center"); //textarea 패널에 등록
		JScrollPane scrollPane = new JScrollPane(ta_notice); //스크롤 생성
		add(scrollPane); //스크롤 패널에 추가
	}
	
	public void mouseClicked(MouseEvent arg0) {
		int row = t_participateClass.getSelectedRow();
		int col = t_participateClass.getSelectedColumn();
		String lectureCode = (String)t_participateClass.getValueAt(row, col);
		ta_notice.setText(notice.getNotice(lectureCode));
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
