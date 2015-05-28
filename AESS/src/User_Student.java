import java.sql.*;
import java.util.*;

public class User_Student{
	private Connection conn;
	
	public User_Student(){
		conn = Info.getConn();
	}
	public void CreateTimeBlock(String day, String time, int type){
		try {
			Statement query = conn.createStatement();
			String sql = "insert into timeblock(location, classroom, day, time, isAvailable, user_id, scheduleNo) values ('-1', '-1', '"+day+"', '"+time+"', 'F', '"+Info.getId()+"', '"+type+"')";
			query.execute(sql);
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void DeleteTimeBlock(int no){
		try {
			Statement query = conn.createStatement();
			String sql = "delete from timeblock where no = '"+no+"'";
			query.execute(sql);
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void Save(){
		try {
			Statement query = conn.createStatement();
			String sql = "update courserelation set schedule_save='T' where user_id='" +Info.getId()+ "';";
			query.execute(sql);
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}