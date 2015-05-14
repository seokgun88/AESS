import static org.junit.Assert.*;

import java.sql.*;

import org.junit.*;


public class SelectLectureBtnTest {
	/******교수가 강의목록 버튼 눌렀을때 다시 강의목록이 뜨게 JPanel이 리셋이 되는지 확인********/
	@Test
	public void test() {
		DBconnect dbcon = new DBconnect();
		Connection conn = dbcon.connect();
		GUI_ProfessorTable profTable = new GUI_ProfessorTable(conn, "P");
		profTable.resetProfessorTable();
		assertEquals(true,profTable.isProfTable);
	}
}