import static org.junit.Assert.*;

import java.sql.*;

import org.junit.*;

public class ResetPwdTest {
	
	@Test
	public void ResetPwdTest1() {
		assertEquals(false, Login.check_resetable_id("a") );
	}
	
	@Test
	public void ResetPwdTest2() {
		assertEquals(true, Login.check_resetable_id("s") );
	}
	
	@Test
	public void ResetPwdTest3() {
		assertEquals(true, Login.check_resetable_id("p") );
	}
	
	@Test
	public void ResetPwdTest4() {
		assertEquals(true, Login.check_resetable_id("w") );
	}

	@Test
	public void ResetPwdTest5() {
		assertEquals(true, Login.check_resetable_id("r") );
	}
	
	@Test
	public void ResetPwdTest6() {
		assertEquals(false, Login.check_resetable_id("") );
	}
	
	@Test
	public void ResetPwdTest7() {
		assertEquals(false, Login.check_resetable_id(" ") );
	}

}
