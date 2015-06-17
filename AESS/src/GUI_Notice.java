import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

/**************�̿��� �߰� : �л� �������� Ȯ�� Ŭ����******************/
public class GUI_Notice extends JPanel implements MouseListener{
	private Notice notice; //�������� Ŭ����
	private JLabel lb_notice; //�������� ��
	private JTable t_participateClass; //�������� ���� ��� ���̺�
	private JScrollPane sp_participateClass;
	private JTextArea ta_notice = new JTextArea(30, 60); //�������� �о�� textarea
	
	public GUI_Notice(){
		notice = new Notice();
		this.setLayout(new BorderLayout());
		
		/*************************Class list*****************************/
		Vector v_participateClassHead = new Vector(); //Class list table head
		v_participateClassHead.addElement("���Ǹ��"); //Name of table head
		Vector v_participateClass = notice.getParticipateClasss(); //Get class list
		t_participateClass = new JTable(new DefaultTableModel(v_participateClass ,v_participateClassHead){
			@Override
			public boolean isCellEditable(int i, int c){ //make cell uneditable
				return false;
			}
		}); //Generate table
		t_participateClass.getTableHeader().setReorderingAllowed(false); //Set table header undraggable
		t_participateClass.addMouseListener(this); //Add cell mouse listener
		sp_participateClass = new JScrollPane(t_participateClass);
		sp_participateClass.setPreferredSize(new Dimension(100, 500));
		add(sp_participateClass, "West");
				
		/*******************Set textarea to show notice of selected class*******************/
		lb_notice = new JLabel("                                   ��������");
		add(lb_notice, "North");
		
		ta_notice.setEditable(false); //Set read notice uneditable
		add(ta_notice, "Center"); //textarea �гο� ���
		JScrollPane scrollPane = new JScrollPane(ta_notice); //��ũ�� ����
		add(scrollPane); //��ũ�� �гο� �߰�
	}
	
	/****************Read notice by the class code of selected item*****************/
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
