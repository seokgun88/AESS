import static org.junit.Assert.*;

import java.sql.*;

import org.junit.*;

public class SignUpNameTest {
	@Test
	public void TestSucceed() {
		assertEquals(true, Manage_User.signUp("abc", "123", "이영석"));
	}
	@Test
	public void TestContainingNumber() {
		assertEquals(false, Manage_User.signUp("abc", "123", "1이영석"));
	}
	@Test
	public void TestContainingSymbol() {
		assertEquals(false, Manage_User.signUp("abc", "123", "#$이영석"));
	}
	@Test
	public void TestContainingSpace() {
		assertEquals(false, Manage_User.signUp("abc", "123", " 이영석	"));
	}
	@Test
	public void TestContainingNumberAndSymbol() {
		assertEquals(false, Manage_User.signUp("abc", "123", "1!이영석"));
	}
	@Test
	public void TestContainingNumberAndSpace() {
		assertEquals(false, Manage_User.signUp("abc", "123", "1이영석	"));
	}
	@Test
	public void TestContainingSymbolAndSpace() {
		assertEquals(false, Manage_User.signUp("abc", "123", "#$@이영석	"));
	}
}