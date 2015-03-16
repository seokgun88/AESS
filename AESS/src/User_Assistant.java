import java.sql.Connection;
import java.sql.SQLException;


public class User_Assistant extends User {
	public User_Assistant(String id, String pass, Connection conn){
		super(id, conn);
	}
	
	public void CreateImpossibleExamTime(){
		String qry = "insert into schedule (name, type, classroom) values ('Á¶±³', 'J', 1113)";
		try {
			query.execute(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void SetImpossibleExamTime(String name){
		qry = "update schedule set classroom=1555 where name='" +name+ "';";
		try {
			query.execute(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void DeleteImpossibleExamTime(String name){
		qry = "delete from schedule where name='" +name+ "';";
		try {
			query.execute(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
