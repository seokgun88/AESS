import static org.junit.Assert.*;

import java.sql.*;

import org.junit.*;

public class SignUpNameTest {
	@Test
	public void TestSucceed() {
		assertEquals(true, ManageUser.signUp("abc", "123", "이영석", "S"));
	}
	@Test
	public void TestContainingNumber() {
		assertEquals(false, ManageUser.signUp("abc", "123", "1이영석", "S"));
	}
	@Test
	public void TestContainingSymbol() {
		assertEquals(false, ManageUser.signUp("abc", "123", "#$이영석", "S"));
	}
	@Test
	public void TestContainingSpace() {
		assertEquals(false, ManageUser.signUp("abc", "123", " 이영석	", "S"));
	}
	@Test
	public void TestContainingNumberAndSymbol() {
		assertEquals(false, ManageUser.signUp("abc", "123", "1!이영석", "S"));
	}
	@Test
	public void TestContainingNumberAndSpace() {
		assertEquals(false, ManageUser.signUp("abc", "123", "1이영석	", "S"));
	}
	@Test
	public void TestContainingSymbolAndSpace() {
		assertEquals(false, ManageUser.signUp("abc", "123", "#$@이영석	", "S"));
	}
}