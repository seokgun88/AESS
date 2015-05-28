import static org.junit.Assert.*;

import java.sql.*;

import org.junit.*;

public class ResetPwdTest {
	DBconnect dbConn = new DBconnect();
	Connection conn = dbConn.connect();
	
	@Test
	public void ResetPwdTest1() {
		ManageUser.setConn(conn);
		assertEquals(true, ManageUser.resetPass("a") );
	}
	
	@Test
	public void ResetPwdTest2() {
		ManageUser.setConn(conn);
		assertEquals(true, ManageUser.resetPass("s") );
	}
	
	@Test
	public void ResetPwdTest3() {
		ManageUser.setConn(conn);
		assertEquals(true, ManageUser.resetPass("p") );
	}
	
	@Test
	public void ResetPwdTest4() {
		ManageUser.setConn(conn);
		assertEquals(false, ManageUser.resetPass("w") );
	}

	@Test
	public void ResetPwdTest5() {
		ManageUser.setConn(conn);
		assertEquals(false, ManageUser.resetPass("r") );
	}
	
	@Test
	public void ResetPwdTest6() {
		ManageUser.setConn(conn);
		assertEquals(false, ManageUser.resetPass("") );
	}
	
	@Test
	public void ResetPwdTest7() {
		ManageUser.setConn(conn);
		assertEquals(false, ManageUser.resetPass(" ") );
	}
}
