import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Info {
	private static DBconnect dbcon;
	private static Connection conn;
	private static String id, name, type;
	private static int year, month, sDate, eDate, week;

	private static String season = "1";	
	private static String test = "기말";
	
	public Info() {
	}

	public static void setNameAndType(String loginId){
		Statement query;
		ResultSet result;
		
		String sql = "select * from member where id='"+loginId+"';";
		try {
			query = conn.createStatement();
			result = query.executeQuery(sql);			
			if(!result.next()){ //에러처리
				return;
			} else {
				id = loginId;
				name = result.getString("name");
				type = result.getString("type");
			}
			result.close();
			query.close();
		} catch(SQLException se) {
			System.out.println("Connection Fail :"+se.getMessage());
		}		
	}
	
	public static String getState(){
		Statement query;
		ResultSet result;
		String state=null;
		
		String sql = "select * from member where id='" +id+ "';";
		try {
			query = conn.createStatement();
			result = query.executeQuery(sql);
			if(result.next()){
				state = result.getString("state");
			}
			result.close();
			query.close();
		} catch(SQLException se) {
			System.out.println("Connection Fail :"+se.getMessage());
		}
		return state;
	}

	public static String getName() {
		return name;
	}

	public static String getType() {
		return type;
	}

	public static String getId() {
		return id;
	}

	public static void setId(String id) {
		Info.id = id;
	}

	public static Connection getConn() {
		return conn;
	}

	public static void setConn() {
		dbcon = new DBconnect();
		conn = dbcon.connect();
	}

	public static int getYear() {
		return year;
	}

	public static void setYear(int year) {
		Info.year = year;
	}

	public static int getMonth() {
		return month;
	}

	public static void setMonth(int month) {
		Info.month = month;
	}

	public static int getsDate() {
		return sDate;
	}

	public static void setsDate(int sDate) {
		Info.sDate = sDate;
	}

	public static int geteDate() {
		return eDate;
	}

	public static void seteDate(int eDate) {
		Info.eDate = eDate;
	}

	public static int getWeek() {
		return week;
	}

	public static void setWeek(int week) {
		Info.week = week;
	}
	
	public static String getSeason() {
		return season;
	}

	public static void setSeason(String season) {
		Info.season = season;
	}

	public static String getTest() {
		return test;
	}

	public static void setTest(String test) {
		Info.test = test;
	}
}
