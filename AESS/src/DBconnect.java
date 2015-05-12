import java.sql.*;

/**데이터베이스 연결**/
public class DBconnect {
	public Connection connect() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://sukgun.iptime.org:3306/aess?useUnicode=true&characterEncoding=utf-8","aess","");
			System.out.println("db에 접속 완료");
		} 
		catch(ClassNotFoundException cnfe) {
			System.out.println("jdbc클래스 에러");
		} 
		catch(SQLException se) {
			System.out.println("Connection Fail :"+se.getMessage());
		}
		return conn;
	}
}
