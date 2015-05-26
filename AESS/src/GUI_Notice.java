import java.awt.*;
import java.sql.*;
import java.util.*;

import javax.swing.*;

/**************�̿��� �߰� : �л� �������� Ȯ�� Ŭ����******************/
public class GUI_Notice extends JPanel{
	User_Student student;
	private JTextArea ta_notice = new JTextArea(30, 60); //�������� �о�� textarea
	
	public GUI_Notice(Connection conn){
		student = new User_Student(conn);		
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		ta_notice.setEditable(false); //�о�� �������� ���� �Ұ�
		add(ta_notice); //textarea �гο� ���
		JScrollPane scrollPane = new JScrollPane(ta_notice); //��ũ�� ����
		add(scrollPane); //��ũ�� �гο� �߰�

		Vector v_notice = student.getNotice(); //���������� �޾ƿ´�
		if(v_notice.size() == 0){ //���������� �ϳ��� ���� ���
			ta_notice.setText("�������� ����");			
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
