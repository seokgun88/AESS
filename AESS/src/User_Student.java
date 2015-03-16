import java.sql.Connection;
import java.sql.SQLException;


public class User_Student extends User{
	public User_Student(String id, Connection conn){
		super(id, conn);
	}
	public void CreateTimeBlock(String day, String time, int type){
		qry = "insert into timeblock(location, classroom, day, time, isAvailable, user_id, scheduleNo) values ('-1', '-1', '"+day+"', '"+time+"', 'F', '"+id+"', '"+type+"')";
		try {
			query.execute(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void DeleteTimeBlock(int no){
		qry = "delete from timeblock where no = '"+no+"'";
		try {
			query.execute(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void Save(){
		qry = "update courserelation set schedule_save='T' where user_id='" +id+ "';";
		try {
			query.execute(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
