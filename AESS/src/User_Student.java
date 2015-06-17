import java.sql.*;
import java.util.*;

/**Student user class**/
public class User_Student{
	private Connection conn;
	
	/**Constructor**/
	public User_Student(){
		conn = Info.getConn();
	}
	
	/**Create time block**/
	public void createTimeBlock(String day, String time, int type){
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
	/**Delete time block**/
	public void deleteTimeBlock(int no){
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
	
	/**Save schedule**/
	public void save(){
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
	
	/**이영석 수정 : GUI_StudentMain에 있던 세부 기능 이동**/
	public Vector getStudentSchedule(String id){
		Vector v_schedule = new Vector();
		String schedule_no[][] = new String[20][20];
		Vector v_scheduleInfos = new Vector();
		int row;
		int col;
		Statement blockState;
		ResultSet blockResult;
		try {
			blockState=conn.createStatement();
			/**Load existing schedule**/
			blockResult = blockState.executeQuery("select * from timeblock where user_id='" +id+ "' and isAvailable='F'");
			while(blockResult.next()) {
				row = Enums.BlockToIndex(blockResult.getString("time"));
				col = Enums.DayToIndex(blockResult.getString("day"));
				schedule_no[row][col] = blockResult.getString("no");
				
				Vector v_scheduleInfo = new Vector();
				/**Recognize <Exam, Class, Impossible> by the value of scheduleNo**/
				int type = blockResult.getInt("scheduleNo");
				if(type==-1)
					v_scheduleInfo.add("시험");
				else if(type==-2)
					v_scheduleInfo.add("수업");
				else if(type==-3)
					v_scheduleInfo.add("감독 불가");
				v_scheduleInfo.add(row);
				v_scheduleInfo.add(col);
				v_scheduleInfos.add(v_scheduleInfo);
			}
			
			/**Load attending formed time schedule**/
			blockState=conn.createStatement();
			blockResult = blockState.executeQuery(
					"select s.name, day, time from timeblock t, courseRelation c, schedule s where c.user_id='" +id+ "' and c.no=s.lecture_id and s.stype='C' and t.scheduleNo=s.no and t.isAvailable='F'"
					);
			while(blockResult.next()) {
				row = Enums.BlockToIndex(blockResult.getString("time"));
				col = Enums.DayToIndex(blockResult.getString("day"));
				Vector v_scheduleInfo = new Vector();
				v_scheduleInfo.add(blockResult.getString("name"));
				v_scheduleInfo.add(row);
				v_scheduleInfo.add(col);
				v_scheduleInfos.add(v_scheduleInfo);
			}
			v_schedule.add(schedule_no);
			v_schedule.add(v_scheduleInfos);
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return v_schedule;
	}
}