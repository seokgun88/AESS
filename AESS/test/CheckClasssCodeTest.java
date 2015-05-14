import static org.junit.Assert.*;

import java.sql.*;

import org.junit.*;


public class CheckClasssCodeTest {
	//앞자리 알파벳4자 뒷자리 숫자6자입력 할 때 코드 체크(기대값:참)
	@Test
	public void correctCodeTest() {
		Connection conn = null;
		User_Admin admin = new User_Admin(conn);
		boolean isCorrectCode = admin.checkClassCode("CLTR123456");
		assertEquals(true, isCorrectCode);
	}
	//전체 자리수가 10자리가 아닐경우 코드 체크(기대값:거짓)
	@Test
	public void incorrectLengthTest() {
		Connection conn = null;
		User_Admin admin = new User_Admin(conn);
		boolean isRightCode = admin.checkClassCode("CLTR123");
		assertEquals(false, isRightCode);
	}
	//앞자리 4자리가 알파벳이 아닐경우 코드 체크(기대값:거짓)
	@Test
	public void noncoverageAlphabetTest() {
		Connection conn = null;
		User_Admin admin = new User_Admin(conn);
		boolean isRightCode = admin.checkClassCode("1234123456");
		assertEquals(false, isRightCode);
	}
	//뒷자리 6자리가 숫자가 아닐경우 코드 체크(기대값:거짓)
	@Test
	public void noncoverageNumberTest() {
		Connection conn = null;
		User_Admin admin = new User_Admin(conn);
		boolean isRightCode = admin.checkClassCode("CLTRABCDEF");
		assertEquals(false, isRightCode);
	}
}
