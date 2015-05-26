import java.awt.*;
import java.sql.*;
import java.util.*;

import javax.swing.*;

/**************이영석 추가 : 학생 공지사항 확인 클래스******************/
public class GUI_Notice extends JPanel{
	User_Student student;
	private JTextArea ta_notice = new JTextArea(30, 60); //공지사항 읽어올 textarea
	
	public GUI_Notice(Connection conn){
		student = new User_Student(conn);		
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		ta_notice.setEditable(false); //읽어온 공지사항 수정 불가
		add(ta_notice); //textarea 패널에 등록
		JScrollPane scrollPane = new JScrollPane(ta_notice); //스크롤 생성
		add(scrollPane); //스크롤 패널에 추가

		Vector v_notice = student.getNotice(); //공지사항을 받아온다
		if(v_notice.size() == 0){ //공지사항이 하나도 없는 경우
			ta_notice.setText("공지사항 없음");			
		}
		else{
			for(int i=0; i<v_notice.size(); i++){
				String lectureCode = (String)((Vector)v_notice.get(i)).get(0);
				String message = (String)((Vector)v_notice.get(i)).get(1);
				ta_notice.setText(lectureCode + " : " + message);
			}
		}
	}
}
