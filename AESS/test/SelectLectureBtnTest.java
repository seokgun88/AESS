import static org.junit.Assert.*;

import java.sql.*;

import org.junit.*;


public class SelectLectureBtnTest {

	@Test
	public void test() {
		DBconnect dbcon = new DBconnect();
		Connection conn = dbcon.connect();
		GUI_ProfessorTable profTable = new GUI_ProfessorTable(conn, "P");
		profTable.resetProfessorTable();
		assertEquals(true,profTable.isProfTable);
	}
}
