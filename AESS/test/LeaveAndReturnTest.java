import static org.junit.Assert.*;

import java.sql.*;

import org.junit.*;

/***********************데이터베이스가 켜져있어야 정상 테스트 됨***************************/
public class LeaveAndReturnTest {
	/************휴학 신청시 데이터베이스에 휴학처리 되는지 확인*************/
	@Test
	public void leaveOfAbsenceTest() {
		DBconnect dbcon = new DBconnect();
		Connection conn = dbcon.connect();
		Info.setId("s");
		ManageUser.setConn(conn);
		ManageUser.setLeaveOfAbsence();
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
		ManageUser.setConn(conn);
		ManageUser.setActivate();
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
