import java.sql.*;

/**�����ͺ��̽� ����**/
public class DBconnect {
	public Connection connect() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://sukgun.iptime.org:3306/aess?useUnicode=true&characterEncoding=utf-8","aess","");
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
