import java.sql.*;
import java.util.*;

public class ResetPwd {
	private Connection conn;
	
	public ResetPwd(){
		conn = Info.getConn();
	}
	public Vector getParticipateClasss(){  //��й�ȣ �ʱ�ȭ ��û�� �ο���� �޾ƿ���
		Vector v_participateClass = new Vector();;
		Vector v_participateClassCol;
		try {
			Statement query = conn.createStatement();
			String sql = "select no from courserelation where user_id='" +Info.getId()+ "';"; //��й�ȣ �ʱ�ȭ ��û�� �ο���� �޾ƿ���
			ResultSet result = query.executeQuery(sql);	
			while(result.next()){
				v_participateClassCol = new Vector();
				v_participateClassCol.addElement(result.getString("no"));
				v_participateClass.addElement(v_participateClassCol);
			}
			result.close();
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return v_participateClass;
	}
}
