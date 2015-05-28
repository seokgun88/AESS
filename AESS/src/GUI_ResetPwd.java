import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

/**************�̿��� �߰� : �л� �������� Ȯ�� Ŭ����******************/
public class GUI_ResetPwd extends JPanel implements MouseListener{
	private ResetPwd resetpwd; //��й�ȣ �ʱ�ȭ Ŭ����
	private JLabel lb_resetpwd; //��й�ȣ �ʱ�ȭ�ο�
	private JTable t_participateClass; //�������� ��� ���̺�
	private JScrollPane sp_participateClass;
	private JTextArea ta_notice = new JTextArea(30, 60); //�������� �о�� textarea
	
	public GUI_ResetPwd(){
		resetpwd = new ResetPwd();
		this.setLayout(new BorderLayout());
		
		/*************************���Ǹ�� ����Ʈ*****************************/
		Vector v_participateClassHead = new Vector(); //�ʱ�ȭ ��û �ο� ���
		v_participateClassHead.addElement("�ο����"); //���̺� ��� �̸�
		Vector v_participateClass = resetpwd.getParticipateClasss(); //�ο� ��� �޾ƿ���
		t_participateClass = new JTable(new DefaultTableModel(v_participateClass ,v_participateClassHead){
			public boolean isCellEditable(int i, int c){ //�� ���� ���� �ȵǰ� ����
				return false;
			}
		}); //���̺� ����
		t_participateClass.getTableHeader().setReorderingAllowed(false); //���̺� ��� �巡�� �Ұ�
		t_participateClass.addMouseListener(this); //�� ���콺 ������ ���
		sp_participateClass = new JScrollPane(t_participateClass);
		sp_participateClass.setPreferredSize(new Dimension(100, 400));
		add(sp_participateClass, "West");
				
	}
	
	public void mouseClicked(MouseEvent arg0) {
		/****************��й�ȣ �ʱ�ȭ*****************/
		int row = t_participateClass.getSelectedRow();
		int col = t_participateClass.getSelectedColumn();
		String list = (String)t_participateClass.getValueAt(row, col);
		
		if(JOptionPane.showConfirmDialog(null,"��й�ȣ �ʱ�ȭ �ϰڽ��ϱ�?", "��й�ȣ �ʱ�ȭ",
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)==JOptionPane.YES_OPTION)
		{
			System.out.println(list);  //����� a�� ��й�ȣ �ʱ�ȭ
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
