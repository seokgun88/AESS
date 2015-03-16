import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Toolkit;
import java.awt.Dimension;


public class Main {
		
	public static void main(String argv[]){
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");		//JTattoo 룩앤필 가져오기
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		GUI_Login gui = new GUI_Login();
	    gui.setVisible(true);
	}
	
	/*
	public static void studentLogin(Connection conn) throws SQLException{
		User_Student std = new User_Student(result.getString("id"), result.getString("pass"), conn);
		//std.SetStudentSchedule();
	}
	public static void profLogin(Connection conn) throws SQLException{
		User_Professor prof = new User_Professor(result.getString("id"), result.getString("pass"), conn);
		prof.GetLectureSchedule();
		//prof.SetNotAvailable("3000");
		//prof.SelectExamTime("3000", "금", "8a");
		prof.FindAvailablePrefer("3000");
		prof.SelectExamTime("3000", 0, 60);
		
	}
	public static void assistantLogin(Connection conn) throws SQLException{
		User_Assistant assistant = new User_Assistant(result.getString("id"), result.getString("pass"), conn);
		assistant.CreateImpossibleExamTime();
	}
	public static void adminLogin(Connection conn) throws SQLException{
		User_Admin admin = new User_Admin(result.getString("id"), result.getString("pass"), conn);
		String command = scanf.nextLine();
		if(command.equals("시험일정")){
			int classroom = scanf.nextInt();
			admin.GetExamSchedule(classroom);
		}
		else if(command.equals("강의실추가")){
			int no = scanf.nextInt();
			String location = scanf.next();
			int maxSeat = scanf.nextInt();
			admin.CreateClassRoom(no, location, maxSeat);
		}
		else if(command.equals("강의실수정")){
			admin.SetClassRoom(1, "9호관 418", 80);
		}
		else if(command.equals("강의실삭제")){
			admin.DeleteClassRoom(1);
		}
		else if(command.equals("시험기간")){
			admin.SetExamWeek(2012, '2', 12, 2);
		}
		else if(command.equals("시간표")){
			int classroom = scanf.nextInt();
			String day = scanf.next();
			String time = scanf.next();
			String lectureNo = scanf.next();
			admin.CreateLectureSchedule(classroom, day, time, lectureNo);
		}
		else if(command.equals("기타")){
			int classroom = scanf.nextInt();
			String day = scanf.next();
			String time = scanf.next();
			admin.CreateOtherSchedule(classroom, day, time);
		}
	}
	*/
}
