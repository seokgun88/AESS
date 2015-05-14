import static org.junit.Assert.*;

import java.sql.*;

import org.junit.*;

public class LeaveAndReturnTest {
	/************휴학 신청시 데이터베이스에 휴학처리 되는지 확인*************/
	@Test
	public void leaveOfAbsenceTest() {
		DBconnect dbcon = new DBconnect();
		Connection conn = dbcon.connect();
		Info.setId("s");
		Manage_User.setConn(conn);
		Manage_User.setLeaveOfAbsence();
		try {
			Statement query = conn.createStatement();
			ResultSet result = query.executeQuery("select type from member where id='" +Info.getId()+ "';");
			if(result.next()){
				assertEquals("L", result.getString("type"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/************복학 신청시 데이터베이스에 복학처리 되는지 확인*************/
	@Test
	public void ReturnToSchoolTest() {
		DBconnect dbcon = new DBconnect();
		Connection conn = dbcon.connect();
		Info.setId("s");
		Manage_User.setConn(conn);
		Manage_User.setReturnToSchool();
		try {
			Statement query = conn.createStatement();
			ResultSet result = query.executeQuery("select type from member where id='" +Info.getId()+ "';");
			if(result.next()){
				assertEquals("S", result.getString("type"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
