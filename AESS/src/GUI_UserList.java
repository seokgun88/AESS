import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * @author Seiryu
 *
 */
public class GUI_UserList extends JPanel implements ActionListener, MouseListener {
	private JList user_list ;
//	JRadioButton ;
	
	private User_Admin admin;
	private Connection conn;
	private String id;
	
	public GUI_UserList(String id){
		conn = Info.getConn();
		this.id = id;
		
		refreshList();
	}
	
	//Refresh the list. Including visualization.
	public void refreshList(){
		admin.getUserList(null);
	}
	
	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	public void actionPerformed(ActionEvent arg0) {}	
}
