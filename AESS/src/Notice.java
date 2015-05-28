import java.sql.*;
import java.util.*;

public class Notice {
	private Connection conn;
	
	public Notice(){
		conn = Info.getConn();
	}
	public Vector getParticipateClasss(){
		Vector v_participateClass = new Vector();;
		Vector v_participateClassCol;
		try {
			Statement query = conn.createStatement();
			String sql = "select no from courserelation where user_id='" +Info.getId()+ "';"; //학생이 듣는 수강 과목 목록 읽어오기
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
	/******************이영석 추가 : 공지사항 읽어오기*****************/
	public String getNotice(String lectureCode){
		String notice = "";
		try {
			Statement query = conn.createStatement();
			String sql = "select message from notice where courseNo='" +lectureCode+ "';"; //해당 과목 공지사항 읽어오기
			ResultSet result = query.executeQuery(sql);
			if(result.next())
				notice = result.getString("message");
			result.close();
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return notice; //공지사항 내용 리턴
	}
}
