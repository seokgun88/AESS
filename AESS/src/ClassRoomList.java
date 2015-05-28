import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class ClassRoomList {
	static private Connection conn;

	public static ArrayList getProfessorList(){
		ArrayList<ArrayList> nameAndIdList = new ArrayList<ArrayList>();
		ArrayList<String> nameList = new ArrayList<String>();
		ArrayList<String> idList = new ArrayList<String>();
		try {
			Statement query = conn.createStatement();
			ResultSet result = query.executeQuery("select name, id from member where type='P'");
			while(result.next()) {
				nameList.add(result.getString("name"));
				idList.add(result.getString("id"));
			}
			nameAndIdList.add(nameList);
			nameAndIdList.add(idList);
			result.close();
			query.close();
			return nameAndIdList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null; //비정상적인 종료
	}
	
	public static ArrayList getClassRoomList(){
		ArrayList classRoomList = new ArrayList();
		try {
			Statement query = conn.createStatement();
			ResultSet result = query.executeQuery("select * from classroom order by location");
			while(result.next()){

				String [] classInfo = new String[4];
				classInfo[0] = result.getString("location");
				classInfo[1] = result.getString("no");
				classInfo[2] = result.getString("maxSeat");
				classInfo[3] = result.getString("equipment"); //추가 : 부수기재 정보 얻어옴
				classRoomList.add(classInfo);
			}
			result.close();
			query.close();
			return classRoomList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null; //비정상적인 종료
	}
	
	public static Connection getConn() {
		return conn;
	}

	public static void setConn(Connection conn) {
		ClassRoomList.conn = conn;
	}
	
	/**이영석 추가 : GUI_ClassRoomList에 있던 기능 이동**/
	public static Vector getRoomTimetable(String location, String roomNo){
		Vector v_schedule = new Vector();
		String schedule_no[][] = new String[20][20];
		Vector v_scheduleInfos = new Vector();
		int row;
		int col;
		Statement scheduleState, blockState;
		ResultSet scheduleResult, blockResult;
		try {
			blockState=conn.createStatement();
			blockResult = blockState.executeQuery("select * from timeblock where location='" +location+ "' and classroom='"+roomNo+"' and isAvailable='F'");
			System.out.println(location + roomNo);
			while(blockResult.next()) {
				row = Enums.BlockToIndex(blockResult.getString("time"));
				col = Enums.DayToIndex(blockResult.getString("day"));	
				schedule_no[row][col] = blockResult.getString("scheduleNo");
				
				scheduleState=conn.createStatement();
				scheduleResult = scheduleState.executeQuery("select * from schedule where no='" +blockResult.getString("scheduleNo")+ "'");
				while(scheduleResult.next()) {
					System.out.println("schedule");
					Vector v_scheduleInfo = new Vector();
					v_scheduleInfo.add(scheduleResult.getString("name"));
					v_scheduleInfo.add(row);
					v_scheduleInfo.add(col);
					v_scheduleInfos.add(v_scheduleInfo);
				}
			}
			v_schedule.add(schedule_no);
			v_schedule.add(v_scheduleInfos);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return v_schedule;
	}
}
