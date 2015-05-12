import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Login {
	static private Connection conn;
	
	public Login(Connection conn){
		this.conn = conn;
	}
	
	public static int loginSetting(String id, String password) {		
		/******시험 기간과 현재 날짜 비교를 위한 Calendar 객체******/
		Calendar c = Calendar.getInstance();
		Calendar c_periodStart = Calendar.getInstance();
		Info.setConn(conn);
		
		String sql = "select * from period;"; //시험 기간을 불러오는 쿼리문
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
			
			if(!result.next()){ //아이디, 비밀번호가 DB에 없는 경우		
				result.close();
				query.close();			
				return 1;
			} else {
				c.add(Calendar.DATE, 14); //오늘 날짜+14(2주후 날짜)
				/***2주후 날짜가 시험시작일 보다 작을 경우 2주보다 많이 남았으므로 시스템 사용이 불가***/
				/***단 관리자일 경우 항상 사용 가능***/
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
