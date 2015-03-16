import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class info {
	static DBconnect dbcon = new DBconnect();
	static Connection conn = dbcon.connect();
	static Statement query;
	static ResultSet result;
	static String name, type;
	static boolean is_P, is_A, is_S, is_J;
	static int year = 2015;
	static String season = "1";	
	static String test = "기말";
	static int month=0, sDate=0, eDate=0, week=0;
	
	public info(String id) {
		this.is_P=false; this.is_A=false; this.is_S=false; this.is_J=false;
		String qry = "select * from member where id='"+id+"';";
		try {
			query = conn.createStatement();
			result = query.executeQuery(qry);
			
			if(!result.next()){
				//에러처리
				return;
			} else {
				name = result.getString("name");
				type = result.getString("type");
				if(type.equals("P")) is_P = true;
				else if (type.equals("S")) is_S = true;
				else if (type.equals("J")) {is_S = true; is_J = true;}
				else if (type.equals("A")) is_A = true;
				else System.out.println("Error! 타입 없음");
			}
		} catch(SQLException se) {
			System.out.println("Connection Fail :"+se.getMessage());
		}
	}

}
