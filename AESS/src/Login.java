import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

/**Log in class**/
public class Login {
	static private Connection conn;
	
	/******Calendar instance to compare current date and exam period******/
	public static int loginSetting(String id, String password) {		
		Calendar c = Calendar.getInstance();
		Calendar c_periodStart = Calendar.getInstance();
		
		String sql = "select * from period;"; //Query to get exam period
		try {
			Statement query = conn.createStatement();
			ResultSet result = query.executeQuery(sql);
			while(result.next()) {
				Info.setYear(result.getInt("year"));
				Info.setMonth(result.getInt("month"));
				Info.setWeek(result.getInt("week"));
				Info.setsDate(result.getInt("sDate"));
				Info.seteDate(result.getInt("eDate"));
				
				c_periodStart.set(Info.getYear(), Info.getMonth()-1, Info.getsDate());
				System.out.printf("%d %d %d",result.getInt("year"), result.getInt("month")-1, result.getInt("sDate"));
			}
			result.close();
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/************입력된 ID, 비밀번호를 얻어옴******************/
		
		sql = "select * from member where id='"+id+"' and pass='"+password+"';";
		try {
			Statement query = conn.createStatement();
			ResultSet result = query.executeQuery(sql);
			
			if(!result.next()){ //If id, password is not in DB		
				result.close();
				query.close();			
				return 1;
			} else {
				c.add(Calendar.DATE, 14); //Current date+14(2 weeks later)
				/***If the date 2 weeks later is closer than exam period,
				 * the time left more than 2 weeks so the system is unavailable***/
				/***But admin always can use***/
				if(c.compareTo(c_periodStart)<0 && !result.getString("type").equals("A")){
					result.close();
					query.close();			
					return 2;
				} else {
					result.close();
					query.close();			
					return 0;
				}
			}
		}
		catch(SQLException se) {
			System.out.println("Connection Fail :"+se.getMessage());
		}
		return -1; //비정상적인 종료
	}

	public static Connection getConn() {
		return conn;
	}

	public static void setConn(Connection conn) {
		Login.conn = conn;
	}	
}
