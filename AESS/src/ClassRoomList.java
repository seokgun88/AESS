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
}
