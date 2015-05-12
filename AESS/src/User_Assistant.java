import java.sql.*;

public class User_Assistant{
	private Connection conn;
	
	public User_Assistant(String id, String pass, Connection conn){
		this.conn = conn;
	}
	
	public void CreateImpossibleExamTime(){
		try {
			Statement query = conn.createStatement();
			String sql = "insert into schedule (name, type, classroom) values ('Á¶±³', 'J', 1113)";
			query.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void SetImpossibleExamTime(String name){
		try {
			Statement query = conn.createStatement();
			String sql = "update schedule set classroom=1555 where name='" +name+ "';";
			query.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void DeleteImpossibleExamTime(String name){
		try {
			Statement query = conn.createStatement();
			String sql = "delete from schedule where name='" +name+ "';";
			query.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
