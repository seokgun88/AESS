import static org.junit.Assert.*;

import java.sql.*;

import org.junit.*;

public class SignUpInfoTest {
	@Test
	public void test() {
		DBconnect dbcon = new DBconnect();
		Connection conn = dbcon.connect();
		GUI_SignUp signup = new GUI_SignUp(conn);
		
		signup.setName("ABC"); signup.setName("AB#");signup.setName("ÃÖÁö¼ö");signup.setName("A}{¤·");signup.setName("¤±Afdf");
		
		assertEquals(true,signup.isSignUp);
	}
}
