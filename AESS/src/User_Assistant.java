import java.sql.*;

/**Assistant user class**/
public class User_Assistant{
	private Connection conn;
	
	/**Constructor**/
	public User_Assistant(String id, String pass){
		conn = Info.getConn();
	}
	
	/**Create Impossible Exam Time**/
	public void createImpossibleExamTime(){
		try {
			Statement query = conn.createStatement();
			String sql = "insert into schedule (name, type, classroom) values ('Á¶±³', 'J', 1113)";
			query.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setImpossibleExamTime(String name){
		try {
			Statement query = conn.createStatement();
			String sql = "update schedule set classroom=1555 where name='" +name+ "';";
			query.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**Delete impossible exam time**/
	public void deleteImpossibleExamTime(String name){
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
