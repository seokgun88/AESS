import static org.junit.Assert.*;

import java.sql.*;

import org.junit.*;

public class CreateClassRoomTest {
	
	@Test
	public void CreateClassRoomTest1() {
		assertEquals(false, User_Admin.createClassRoom("410", "  ", "50" , "bim mic" ) );
	}
	
	@Test
	public void CreateClassRoomTest2() {
		assertEquals(false, User_Admin.createClassRoom("410", "9", "50" , "bim mic" ) );
	}
	
	@Test
	public void CreateClassRoomTest3() {
		assertEquals(false, User_Admin.createClassRoom("410", "", "50" , "bim mic" ) );
	}
	
	@Test
	public void CreateClassRoomTest4() {
		assertEquals(false, User_Admin.createClassRoom("410", "#E9", "50" , "bim mic" ) );
	}

	@Test
	public void CreateClassRoomTest5() {
		assertEquals(false, User_Admin.createClassRoom("4a10", "E9", "50" , "bim mic" ) );
	}
	
	@Test
	public void CreateClassRoomTest6() {
		assertEquals(false, User_Admin.createClassRoom("411", "E9", "a50" , "bim mic" ) );
	}
	
	@Test
	public void CreateClassRoomTest7() {
		assertEquals(false, User_Admin.createClassRoom("411", "", "50" , "bim mic" ) );
	}
	
	@Test
	public void CreateClassRoomTest8() {
		assertEquals(false, User_Admin.createClassRoom("411", " ", "50" , "bim mic" ) );
	}
	
	@Test
	public void CreateClassRoomTest9() {
		assertEquals(false, User_Admin.createClassRoom("411", "1", "50" , "bim mic" ) );
	}
	
	@Test
	public void CreateClassRoomTest10() {
		assertEquals(false, User_Admin.createClassRoom("411", "E9", "" , "bim mic" ) );
	}
	
	@Test
	public void CreateClassRoomTest11() {
		assertEquals(false, User_Admin.createClassRoom("411", "E9", " " , "bim mic" ) );
	}
	
	@Test
	public void CreateClassRoomTest12() {
		assertEquals(false, User_Admin.createClassRoom("411", "E9", "50 " , "bim mic" ) );
	}
	
}
