import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

/**************�̿��� �߰� : �л� �������� Ȯ�� Ŭ����******************/
public class GUI_Notice extends JPanel implements MouseListener{
	Notice notice; //�������� Ŭ����
	private JLabel lb_notice; //�������� ��
	private JTable t_participateClass; //�������� ���� ��� ���̺�
	private JScrollPane sp_participateClass;
	private JTextArea ta_notice = new JTextArea(30, 60); //�������� �о�� textarea
	
	public GUI_Notice(Connection conn){
		notice = new Notice(conn);		
		this.setLayout(new BorderLayout());
		
		/*************************���Ǹ�� ����Ʈ*****************************/
		Vector v_participateClassHead = new Vector(); //���Ǹ�� ���̺� ���
		v_participateClassHead.addElement("���Ǹ��"); //���̺� ��� �̸�
		Vector v_participateClass = notice.getParticipateClasss(); //���� ��� �޾ƿ���
		if(v_participateClass.size() == 0){
			v_participateClass.addElement("�������� ���ǰ� �����ϴ�.");
		}
		t_participateClass = new JTable(new DefaultTableModel(v_participateClass ,v_participateClassHead){
			public boolean isCellEditable(int i, int c){ //�� ���� ���� �ȵǰ� ����
				return false;
			}
		}); //���̺� ����
		t_participateClass.addMouseListener(this); //�� ���콺 ������ ���
		sp_participateClass = new JScrollPane(t_participateClass);
		sp_participateClass.setPreferredSize(new Dimension(100, 500));
		add(sp_participateClass, "West");
				
		/*******************������ ���� �������� ����� textarea ����*******************/
		lb_notice = new JLabel("                                   ��������");
		add(lb_notice, "North");
		
		ta_notice.setEditable(false); //�о�� �������� ���� �Ұ�
		add(ta_notice, "Center"); //textarea �гο� ���
		JScrollPane scrollPane = new JScrollPane(ta_notice); //��ũ�� ����
		add(scrollPane); //��ũ�� �гο� �߰�
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
