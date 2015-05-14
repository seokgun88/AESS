import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Manage_User {
	static private Connection conn;
	
	public static void setLeaveOfAbsence(){
		try {
			Statement query = conn.createStatement();
			query.execute("update member set type='L' where id='" +Info.getId()+ "';");
			query.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static void setReturnToSchool(){
		try {
			Statement query = conn.createStatement();
			query.execute("update member set type='S' where id='" +Info.getId()+ "';");
			query.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}
	
	public static boolean signUp(String id, String pass, String name){
		boolean isSignUp;
		
		/*회원가입 시 아이디랑 비밀번호 출력*/
		System.out.printf("ID : %s\nName : %s\nPW : %s\n",id, pass, name);

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
		return isSignUp;
	}

	public static void setConn(Connection conn) {
		Manage_User.conn = conn;
	}	
}
