import java.sql.*;
import java.util.*;

public class User_Student{
	private Connection conn;
	
	public User_Student(Connection conn){
		this.conn = conn;
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
	/******************�̿��� �߰� : �������� �о����*****************/
	public Vector getNotice(){
		Vector v_Notice = new Vector();
		try {
			Statement query = conn.createStatement();
			String sql = "select no from courserelation where user_id='" +Info.getId()+ "';"; //�л��� ��� ���� ���� ��� �о����
			ResultSet result = query.executeQuery(sql);	
			while(result.next()){
				String lectureCode = result.getString("no");
				Statement query2 = conn.createStatement();
				String sql2 = "select message from notice where courseNo='" +lectureCode+ "';"; //�ش� ���� �������� �о����
				ResultSet result2 = query2.executeQuery(sql2);
				if(result2.next()){
					Vector lectureCodeAndMessage = new Vector();
					lectureCodeAndMessage.addElement(lectureCode);
					lectureCodeAndMessage.addElement(result2.getString("message"));
					v_Notice.addElement(lectureCodeAndMessage);
				}
				result2.close();
				query2.close();
			}
			result.close();
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return v_Notice; //��� �������� ���������� ��ȯ
	}
}