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
		/******���� �Ⱓ�� ���� ��¥ �񱳸� ���� Calendar ��ü******/
		Calendar c = Calendar.getInstance();
		Calendar c_periodStart = Calendar.getInstance();
		Info.setConn(conn);
		
		String sql = "select * from period;"; //���� �Ⱓ�� �ҷ����� ������
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
		
		/************�Էµ� ID, ��й�ȣ�� ����******************/
		
		sql = "select * from member where id='"+id+"' and pass='"+password+"';";
		try {
			Statement query = conn.createStatement();
			ResultSet result = query.executeQuery(sql);
			
			if(!result.next()){ //���̵�, ��й�ȣ�� DB�� ���� ���		
				result.close();
				query.close();			
				return 1;
			} else {
				c.add(Calendar.DATE, 14); //���� ��¥+14(2���� ��¥)
				/***2���� ��¥�� ��������� ���� ���� ��� 2�ֺ��� ���� �������Ƿ� �ý��� ����� �Ұ�***/
				/***�� �������� ��� �׻� ��� ����***/
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
		return -1; //���������� ����
	}

	public static Connection getConn() {
		return conn;
	}

	public static void setConn(Connection conn) {
		Login.conn = conn;
	}	
}
