import static org.junit.Assert.*;

import java.sql.*;

import org.junit.*;


public class CheckClasssCodeTest {
	//���ڸ� ���ĺ�4�� ���ڸ� ����6���Է� �� �� �ڵ� üũ(��밪:��)
	@Test
	public void correctCodeTest() {
		Connection conn = null;
		User_Admin admin = new User_Admin(conn);
		boolean isCorrectCode = admin.checkClassCode("CLTR123456");
		assertEquals(true, isCorrectCode);
	}
	//��ü �ڸ����� 10�ڸ��� �ƴҰ�� �ڵ� üũ(��밪:����)
	@Test
	public void incorrectLengthTest() {
		Connection conn = null;
		User_Admin admin = new User_Admin(conn);
		boolean isRightCode = admin.checkClassCode("CLTR123");
		assertEquals(false, isRightCode);
	}
	//���ڸ� 4�ڸ��� ���ĺ��� �ƴҰ�� �ڵ� üũ(��밪:����)
	@Test
	public void noncoverageAlphabetTest() {
		Connection conn = null;
		User_Admin admin = new User_Admin(conn);
		boolean isRightCode = admin.checkClassCode("1234123456");
		assertEquals(false, isRightCode);
	}
	//���ڸ� 6�ڸ��� ���ڰ� �ƴҰ�� �ڵ� üũ(��밪:����)
	@Test
	public void noncoverageNumberTest() {
		Connection conn = null;
		User_Admin admin = new User_Admin(conn);
		boolean isRightCode = admin.checkClassCode("CLTRABCDEF");
		assertEquals(false, isRightCode);
	}
}
