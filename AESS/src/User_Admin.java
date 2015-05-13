import java.sql.*;

public class User_Admin{	
	private Connection conn;
	
	public User_Admin(Connection conn){
		this.conn = conn;
	}
	
	public void GetExamSchedule(int classroom){
		try {
			String sql = "select * from timeblock where classroom=" +classroom+ ";";
			Statement query = conn.createStatement();
			ResultSet result = query.executeQuery(sql);
			while(result.next()){
				System.out.println(result.getString("lectureNo"));
			}
			result.close();
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void CreateClassRoom(String roomNo, String location, String maxSeat, String equipment){
		try {

			String sql = "insert into classroom values ('"+roomNo+ "', '" +location+ "', '" +maxSeat+ "', '" +equipment+ "');";
			Statement query = conn.createStatement();
			query.execute(sql);
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void SetClassRoom(String location, String no, String maxSeat, String cLocation, String cNo, String cMax){
		try {
			//������ �ǹ�, ��, �ο����� �˻��Ͽ�, ����Ǿ��� ���� �ִ� ����� ��� ������Ʈ �ع�����.
			String sql = "update classroom set no='"+cNo+"', location='" +cLocation+ "', maxSeat='" +cMax+ "' where no='"+no+"' and location='" +location+ "' and maxSeat='"+maxSeat+"'";
			Statement query = conn.createStatement();
			query.execute(sql);
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void DeleteClassRoom(String no, String location){
		try {
			String sql = "delete from classroom where no=" +no;
			Statement query = conn.createStatement();
			query.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void SetExamWeek(int year, String semester, String test, int month, int week, int sDate, int eDate) {
		System.out.printf("%d %s %s %d %d %d %d", year, semester, test, month, week, sDate, eDate);
		int isSet=0;
		
		try {			
			String sql = "select week from period where year='"+year+"' and semester='"+semester+"' and test='"+test+"'";
			Statement query = conn.createStatement();
			ResultSet result = query.executeQuery(sql);
			while(result.next()) {
				isSet = result.getInt("week");
			}
			result.close();
			if(isSet==0) {
				sql = "insert into period values ('"+year+"', '"+semester+"', '"+test+"', '"+month+"', '"+week+"', '"+sDate+"', '"+eDate+"')";
				query.execute(sql);
			} else {
				sql = "update period set month='"+month+"', week='"+week+"', sDate='"+sDate+"', eDate='"+eDate+"' where year='"+year+"' and semester='"+semester+"' and test='"+test+"'";
				query.execute(sql);
			}
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int CreateLectureSchedule(String name, String prof, String code){
		int resultno=0;
		try {
			String sql = "select no from schedule where stype='C' and lecture_id='"+code+"'";  //�ϴ� �ֳ� ���� select �� ����.
			Statement query = conn.createStatement();
			ResultSet result = query.executeQuery(sql);
			while(result.next()) {
				resultno = result.getInt("no");
			}
			result.close();
			
			//���࿡ while�� �� ���ȴµ� �ϳ��� ���ٸ�
			if(resultno==0) {
				sql = "insert into schedule(name, stype, user_id, lecture_id) values ('"+name+"', 'C', '"+prof+"', '"+code+"')";
				query.execute(sql);
				
				//�ٽ� select�Ѵ�.
				sql = "select no from schedule where stype='C' and lecture_id='"+code+"'";
				result = query.executeQuery(sql);
				while(result.next()) {
					resultno = result.getInt("no");
				}
				result.close();
			}
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultno;
	}
	
	public int CreateOtherSchedule(String name){
		int resultno=0;
		try {
			String sql = "select no from schedule where stype='E' and name='"+name+"'"; //�ϴ� �ֳ� ���� select �� ����.
			Statement query = conn.createStatement();
			ResultSet result = query.executeQuery(sql);
			while(result.next()) {
				resultno = result.getInt("no");
			}
			result.close();
			//���࿡ while�� �� ���ȴµ� �ϳ��� ���ٸ�
			if(resultno==0) {
				sql = "insert into schedule(name, stype) values ('"+name+"', 'E')";
				query.execute(sql);
				
				sql = "select no from schedule where stype='E' and name='"+name+"'";
				result = query.executeQuery(sql);
				while(result.next()) {
					resultno = result.getInt("no");
				}
				result.close();
			}
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultno;
	}
	
	public void DeleteSchedule(String schedule){
		try {
			String sql = "delete from timeblock where scheduleNo='"+schedule+"'";
			Statement query = conn.createStatement();
			query.execute(sql);
			
			sql = "delete from schedule where no='"+schedule+"'";
			query.execute(sql);
			
			query.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void CreateTimeBlock(String id, String location, String classroom, String day, String time, int scheNo){
		try {
			Statement query = conn.createStatement();
			String sql = "insert into timeblock(location, classroom, day, time, isAvailable, scheduleNo, user_id) values ('" 
		+location+ "', '" +classroom+ "', '" +day+ "', '" +time+ "', 'F', '" +scheNo+ "', '" +id+ "')";
			query.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
