import java.sql.*;
import java.util.*;

public class ResetPwd {
	private Connection conn;
	
	public ResetPwd(){
		conn = Info.getConn();
	}
	public Vector getParticipateClasss(){  //비밀번호 초기화 요청한 인원목록 받아오기
		Vector v_participateClass = new Vector();;
		Vector v_participateClassCol;
		try {
			Statement query = conn.createStatement();
			String sql = "select no from courserelation where user_id='" +Info.getId()+ "';"; //비밀번호 초기화 요청한 인원목록 받아오기
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
