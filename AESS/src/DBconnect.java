import java.sql.*;

/**DB Connection class**/
public class DBconnect {
	/**Connect to DB**/
	public Connection connect() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://183.106.12.154:3306/aess?useUnicode=true&characterEncoding=utf-8","aess","");
			System.out.println("db�� ���� �Ϸ�");
		} 
		catch(ClassNotFoundException cnfe) {
			System.out.println("jdbcŬ���� ����");
		} 
		catch(SQLException se) {
			System.out.println("Connection Fail :"+se.getMessage());
		}
		return conn;
	}
}