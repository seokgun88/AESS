import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Manage_User {
	static private Connection conn;
	
	public static void setLeaveOfAbsence(){
		try {
			Statement query = conn.createStatement();
			query.execute("update member set type='L' where id='" +Info.getId()+ "';");
			query.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static void setReturnToSchool(){
		try {
			Statement query = conn.createStatement();
			query.execute("update member set type='S' where id='" +Info.getId()+ "';");
			query.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}

	public static void setConn(Connection conn) {
		Manage_User.conn = conn;
	}	
}
