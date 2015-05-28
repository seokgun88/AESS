import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class ManageUser {
	static private Connection conn;

	public static void setConn(Connection conn) {
		ManageUser.conn = conn;
	}	
	public static void setLeaveOfAbsence(){
		try {
			Statement query = conn.createStatement();
			query.execute("update member set state='L' where id='" +Info.getId()+ "';");
			query.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}	
	public static void setActivate(){
		try {
			Statement query = conn.createStatement();
			query.execute("update member set state='A' where id='" +Info.getId()+ "';");
			query.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}	
	public static boolean signUp(String id, String pass, String name, String type){
		boolean isSignUp;		
		/*회원가입 시 아이디랑 비밀번호 출력*/
		System.out.printf("ID : %s\nName : %s\nPW : %s\nType : %s\n",id, pass, name, type);
		/*이름 조건*/
		if(name.contains("0") || name.contains("1") || name.contains("2") || name.contains("3") || name.contains("4") || name.contains("5") || name.contains("6") || name.contains("7") || name.contains("8") || name.contains("9"))
			isSignUp = false;
		else if(name.contains(")") || name.contains("!") || name.contains("@") || name.contains("#") || name.contains("$") || name.contains("%") || name.contains("^") || name.contains("&") || name.contains("*") || name.contains("("))
			isSignUp = false;
		else if(name.contains("[") || name.contains("]") || name.contains("{")  || name.contains("}") || name.contains("-") || name.contains("=") || name.contains("_") || name.contains("+") || name.contains(";") || name.contains("'") || name.contains(":") || name.contains("\"") || name.contains("|") || name.contains("\\") || name.contains(",") || name.contains(".") || name.contains("/") || name.contains("<") || name.contains(">") || name.contains("?") || name.contains("`") || name.contains("~"))
			isSignUp = false;
		else if(name == null || name.contains("\t") || name.contains(" ") || name.contains("\n"))
			isSignUp = false;
		else
			isSignUp = true;	
		
		/**이영석 추가 : DB로 회원 가입 정보 전송**/
		try {
			Statement query = conn.createStatement();
			String sql = "insert into member (id, pass, name, type) values ('" +id+ "', '" +pass+ "', '" +name+ "', '" +type+ "');";
			query.execute(sql);
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return isSignUp;
	}
	public static boolean resetPass(String id){
		try {
			Statement query = conn.createStatement();
			String sql = "select * from member where id='" +id+ "';";
			ResultSet result = query.executeQuery(sql);
			if(!result.next())
				return false;
			sql = "update member set state='R' where id='" +id+ "';";
			query.execute(sql);
			result.close();
			query.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
