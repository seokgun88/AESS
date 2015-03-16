import java.sql.Connection;
import java.sql.SQLException;


public class User_Admin extends User{	
	public User_Admin(String id, Connection conn){
		super(id, conn);
	}
	public void GetExamSchedule(int classroom){
		qry = "select * from timeblock where classroom=" +classroom+ ";";
		try {
			result = query.executeQuery(qry);
			while(result.next()){
				System.out.println(result.getString("lectureNo"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void CreateClassRoom(String roomNo, String location, String maxSeat){
		qry = "insert into classroom values ('"+roomNo+ "', '" +location+ "', '" +maxSeat+ "');";
		try {
			query.execute(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void SetClassRoom(String location, String no, String maxSeat, String cLocation, String cNo, String cMax){
		//기존의 건물, 방, 인원으로 검색하여, 변경되었을 수도 있는 값들로 모두 업데이트 해버린다.
		qry = "update classroom set no='"+cNo+"', location='" +cLocation+ "', maxSeat='" +cMax+ "' where no='"+no+"' and location='" +location+ "' and maxSeat='"+maxSeat+"'";
		try {
			query.execute(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void DeleteClassRoom(String no, String location){
		qry = "delete from classroom where no=" +no;
		try {
			query.execute(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void SetExamWeek(int year, String semester, String test, int month, int week, int sDate, int eDate) {
		System.out.printf("%d %s %s %d %d %d %d", year, semester, test, month, week, sDate, eDate);
		int isSet=0;
		try {
			qry = "select week from period where year='"+year+"' and semester='"+semester+"' and test='"+test+"'";
			result = query.executeQuery(qry);
			while(result.next()) {
				isSet = result.getInt("week");
			}
			if(isSet==0) {
				qry = "insert into period values ('"+year+"', '"+semester+"', '"+test+"', '"+month+"', '"+week+"', '"+sDate+"', '"+eDate+"')";
				query.execute(qry);
			} else {
				qry = "update period set month='"+month+"', week='"+week+"', sDate='"+sDate+"', eDate='"+eDate+"' where year='"+year+"' and semester='"+semester+"' and test='"+test+"'";
				query.execute(qry);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int CreateLectureSchedule(String name, String prof, String code){
		int resultno=0;
		try {
			//일단 있나 없다 select 해 본다.
			qry = "select no from schedule where stype='C' and lecture_id='"+code+"'";
			result = query.executeQuery(qry);
			while(result.next()) {
				resultno = result.getInt("no");
			}
			//만약에 while문 다 돌렸는데 하나도 없다면
			if(resultno==0) {
				qry = "insert into schedule(name, stype, user_id, lecture_id) values ('"+name+"', 'C', '"+prof+"', '"+code+"')";
				query.execute(qry);
				
				//다시 select한다.
				qry = "select no from schedule where stype='C' and lecture_id='"+code+"'";
				result = query.executeQuery(qry);
				while(result.next()) {
					resultno = result.getInt("no");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultno;
	}
	
	public int CreateOtherSchedule(String name){
		int resultno=0;
		try {
			//일단 있나 없다 select 해 본다.
			qry = "select no from schedule where stype='E' and name='"+name+"'";
			result = query.executeQuery(qry);
			while(result.next()) {
				resultno = result.getInt("no");
			}
			//만약에 while문 다 돌렸는데 하나도 없다면
			if(resultno==0) {
				qry = "insert into schedule(name, stype) values ('"+name+"', 'E')";
				query.execute(qry);
				
				qry = "select no from schedule where stype='E' and name='"+name+"'";
				result = query.executeQuery(qry);
				while(result.next()) {
					resultno = result.getInt("no");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultno;
	}
	
	public void DeleteSchedule(String schedule){
		try {
			qry = "delete from timeblock where scheduleNo='"+schedule+"'";
			query.execute(qry);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			qry = "delete from schedule where no='"+schedule+"'";
			query.execute(qry);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void CreateTimeBlock(String id, String location, String classroom, String day, String time, int scheNo){
		qry = "insert into timeblock(location, classroom, day, time, isAvailable, scheduleNo, user_id) values ('" +location+ "', '" +classroom+ "', '" +day+ "', '" +time+ "', 'F', '" +scheNo+ "', '" +id+ "')";
		try {
			query.execute(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
