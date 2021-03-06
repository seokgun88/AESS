import java.sql.*;

import javax.swing.JOptionPane;

/**Admin user Class**/
public class User_Admin{	
	private static Connection conn; //@@승훈 추가 : static으로 선언함. test때문에
	
	/**Enumeration type for account state**/
	public enum AccountState {NEW,AUTHED}
	
	/**Constructor**/
	public User_Admin(){
		conn = Info.getConn(); //@@승훈 추가 : static으로 선언함. test때문에
	}
	
	public void getExamSchedule(int classroom){
		try {
			String sql = "select * from timeblock where classroom=" +classroom+ ";";
			Statement query = conn.createStatement();
			ResultSet result = query.executeQuery(sql);
			while(result.next()){
				System.out.println(result.getString("lectureNo"));
			}
			result.close();
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**Create Class Room**/
	public static boolean createClassRoom(String roomNo, String location, String maxSeat, String equipment){ //@@승훈 추가 : test를 위해서 static선언
		boolean createroom_success = true; //@@승훈 추가 : 리턴타입 bool변수
		boolean room_check = true; //@@승훈 추가 : room이름 숫자인지 체크
		boolean seat_check = true; //@@승훈 추가 : maxseat 숫자인지 체크
		try {
			//@@승훈 추가 : GUI에서 떼옴(기능)
			if(!location.startsWith("E")&&!location.startsWith("I")) {
				createroom_success = false;
				JOptionPane.showMessageDialog(null, "건물을 잘못 입력하셨습니다.");
			}
			else if( location!=null&& roomNo!=null && maxSeat!=null) {
			}
			
			//@@승훈 추가 : 호수 검사 (숫자)
			for(int i = 0; i< roomNo.length(); i++){
				char room_char = roomNo.charAt(i);
				if( room_char < 48 || room_char > 58 || room_char == ' ') {
					//If char value is not number
					room_check = false;
				}
			}
			if(room_check == false || roomNo.length()==0) {
				createroom_success = false;
				JOptionPane.showMessageDialog(null, "호수를 잘못 입력하셨습니다.");
			}
			else if( location!=null&& roomNo!=null && maxSeat!=null) {
			}
			
			//@@승훈 추가 : 좌석 검사 (숫자)
			for(int i = 0; i< maxSeat.length(); i++){
				char seat_char = maxSeat.charAt(i);
				if( seat_char < 48 || seat_char > 58 || seat_char==' ') {
					//If char value is not number
					seat_check = false;
				}
			}
			if(seat_check == false || maxSeat.length()==0) {
				createroom_success = false;
				JOptionPane.showMessageDialog(null, "좌석수를 잘못 입력하셨습니다.");
			}
			else if( location!=null&& roomNo!=null && maxSeat!=null) {
			}
			
			if(createroom_success == true) { //@@승훈 추가  : create 조건
				System.out.println(roomNo+location);
				String sql = "insert into classroom values ('"+roomNo+ "', '" +location+ "', '" +maxSeat+ "', '" +equipment+ "');";
				Statement query = conn.createStatement();
				query.execute(sql);
				query.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return createroom_success; //@@승훈 추가  : bool타입 반환
	}
	
	/**이영석 추가 : 부수기재 정보 수정 추가**/
	public void setClassRoom(String location, String no, String maxSeat, String equipment, String cLocation, String cNo, String cMax, String cEquipment){
		try {
			//Search with existing building, room, the number of people, update everything with the changed value
			String sql = "update classroom set no='"+cNo+"', location='" +cLocation+ "', maxSeat='" +cMax+ "', equipment='" +cEquipment+ 
					"' where no='"+no+"' and location='" +location+ "' and maxSeat='"+maxSeat+"' and equipment='" +equipment+ "';";
			Statement query = conn.createStatement();
			query.execute(sql);
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**Delete Class Room**/
	public void deleteClassRoom(String no, String location){
		try {
			String sql = "delete from classroom where no=" +no;
			Statement query = conn.createStatement();
			query.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setExamWeek(int year, String semester, String test, int month, int week, int sDate, int eDate) {
		System.out.printf("%d %s %s %d %d %d %d", year, semester, test, month, week, sDate, eDate);
		int isSet=0;
		
		try {			
			String sql = "select week from period where year='"+year+"' and semester='"+semester+"' and test='"+test+"'";
			Statement query = conn.createStatement();
			ResultSet result = query.executeQuery(sql);
			while(result.next()) {
				isSet = result.getInt("week");
			}
			result.close();
			if(isSet==0) {
				sql = "insert into period values ('"+year+"', '"+semester+"', '"+test+"', '"+month+"', '"+week+"', '"+sDate+"', '"+eDate+"')";
				query.execute(sql);
			} else {
				sql = "update period set month='"+month+"', week='"+week+"', sDate='"+sDate+"', eDate='"+eDate+"' where year='"+year+"' and semester='"+semester+"' and test='"+test+"'";
				query.execute(sql);
			}
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**Create lecture schedule**/
	public int CreateLectureSchedule(String name, String prof, String code){
		int resultno=0;
		try {
			String sql = "select no from schedule where stype='C' and lecture_id='"+code+"'";  //일단 있나 없다 select 해 본다.
			Statement query = conn.createStatement();
			ResultSet result = query.executeQuery(sql);
			while(result.next()) {
				resultno = result.getInt("no");
			}
			result.close();
			
			//If nothing at the end of the while loop,
			if(resultno==0) {
				sql = "insert into schedule(name, stype, user_id, lecture_id) values ('"+name+"', 'C', '"+prof+"', '"+code+"')";
				query.execute(sql);
				
				//select again.
				sql = "select no from schedule where stype='C' and lecture_id='"+code+"'";
				result = query.executeQuery(sql);
				while(result.next()) {
					resultno = result.getInt("no");
				}
				result.close();
			}
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultno;
	}
	
	/**Class code checking**/
	public boolean checkClassCode(String classCode){
		if(classCode.length() != 10)
			return false;
		else{
			for(int i=0; i<10; i++){
				if(i<4 && !Character.isAlphabetic(classCode.charAt(i)))
					return false;
				else if(i>=4 && !Character.isDigit(classCode.charAt(i)))
					return false;
			}
		}
		return true;
	}
	
	/**Create other schedule**/
	public int createOtherSchedule(String name){
		int resultno=0;
		try {
			String sql = "select no from schedule where stype='E' and name='"+name+"'"; //일단 있나 없다 select 해 본다.
			Statement query = conn.createStatement();
			ResultSet result = query.executeQuery(sql);
			while(result.next()) {
				resultno = result.getInt("no");
			}
			result.close();
			//If nothing at the end of while loop,
			if(resultno==0) {
				sql = "insert into schedule(name, stype) values ('"+name+"', 'E')";
				query.execute(sql);
				
				sql = "select no from schedule where stype='E' and name='"+name+"'";
				result = query.executeQuery(sql);
				while(result.next()) {
					resultno = result.getInt("no");
				}
				result.close();
			}
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultno;
	}
	
	public void DeleteSchedule(String schedule){
		try {
			String sql = "delete from timeblock where scheduleNo='"+schedule+"'";
			Statement query = conn.createStatement();
			query.execute(sql);
			
			sql = "delete from schedule where no='"+schedule+"'";
			query.execute(sql);
			
			query.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**Create Time block**/
	public void createTimeBlock(String id, String location, String classroom, String day, String time, int scheNo){
		try {
			Statement query = conn.createStatement();
			String sql = "insert into timeblock(location, classroom, day, time, isAvailable, scheduleNo, user_id) values ('" 
		+location+ "', '" +classroom+ "', '" +day+ "', '" +time+ "', 'F', '" +scheNo+ "', '" +id+ "')";
			query.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**정용훈 추가
	 * Approve requested one where member = id.
	 * Update from auth=F to auth=T
	 * **/
	public void approveUser(String id){
		if(!isUserUnapproved(id))
			return;
		try{			
			Statement query = conn.createStatement();
			String sql = "update member set state='T' where id='" +id+ "';";
			query.execute(sql);
			query.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	/**이영석 추가 : 쿼리문 작성
	 * Delete user where member = id
	 * **/
	public void deleteUser(String id){
		try{
			Statement query = conn.createStatement();
			String sql = "delete from member where id = '" +id+ "';";
			query.execute(sql);
			query.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	/**이영석 추가 : 쿼리문 작성, state가 T,F들어오면 DB 멤버의 state 상태에 그대로 T,F 업데이트
	 * Chose by the value of state and active value of member = id.
	 * Active : T, Inactive : F, using update query.
	 * **/
	public void activateUser(String id, String state){
		try{
			Statement query = conn.createStatement();
			String stateStr;
			if(isUserUnapproved(id))//Not to change one who is approved.
				return;
			String sql = "update member set state='" +state+ "' where id='" +id+ "';";
			query.execute(sql);
			query.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	/**이영석 추가 : 사용자 type 확인**/
	public String getUserType(String id){
		String type=null;
		try{
			Statement query = conn.createStatement();
			String sql = "select * from member where id='" +id+ "';";
			ResultSet result;
			result = query.executeQuery(sql);
			result.next();
			type = result.getString("type");
			result.close();
			query.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return type;
	}
	/**이영석 추가 : 사용자의 state 확인**/
	public String getUserState(String id){
		String state=null;
		try{
			Statement query = conn.createStatement();
			String sql = "select * from member where id='" +id+ "';";
			ResultSet result;
			result = query.executeQuery(sql);
			result.next();
			state = result.getString("state");
			result.close();
			query.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return state;
	}
	public boolean isUserInactivated(String id){
		boolean res=false;
		if(getUserState(id).equals("F"))
			res = true;
		return res;
	}
	public boolean isUserUnapproved(String id){
		boolean res=false;
		if(getUserState(id).equals("N"))
			res = true;
		return res;
	}
	/**이영석 추가 : 학생 휴학신청 상태인지 확인**/
	public boolean isUserLeave(String id){
		boolean res=false;
		if(getUserState(id).equals("L"))
			res = true;
		return res;		
	}
	/**이영석 추가 : 활성화 요청 상태 인지 확인**/
	public boolean isUserRequireActivate(String id){
		boolean res=false;
		if(getUserState(id).equals("A"))
			res = true;
		return res;				
	}
	/**이영석 추가 : 비밀번호 초기화 요청 확인**/
	public boolean isRequireResetPass(String id){
		boolean res=false;
		if(getUserState(id).equals("R"))
			res = true;
		return res;						
	}
	/**이영석 추가 : 쿼리문 작성, state가 T면 가입된 즉 활성화된 회원
	 * F면 비활성화된 회원
	 * N(new)면 가입 신청한 회원
	 */
	public String [] getUserList(AccountState state){
		try{
			String [] temp;
			int resCnt, i=0;
			
			Statement query = conn.createStatement();
			String sql;
			ResultSet result;
			
			if(state==AccountState.NEW){
				sql = "select id from member where state='N';";//Show only requested ones
			}else if(state==AccountState.AUTHED){
				sql = "select id from member where state!='N';";//Show approved only(Include inactivated ones)
			}else{
				//All accounts
				sql = "select id from member";//Show all users
			}
			
			result = query.executeQuery(sql);
			result.last();
			resCnt=result.getRow();
			result.beforeFirst();
			temp = new String[resCnt];
			while(result.next()){
				temp[i++]=result.getString("id");
				System.out.println(result.getString("id"));
			}
			return temp;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	/**이영석 추가 : 비밀번호 초기화 sql**/
	public void resetPassword(String id){
		try{
			Statement query = conn.createStatement();
			String sql = "select * from member where id='" +id+ "';";
			ResultSet result = query.executeQuery(sql);
			if(result.next()){
				if(result.getString("state").equals("R")){
					sql = "update member set pass='0000', state='T' where id='" +id+ "';";
					query.execute(sql);
				}
			}			
			result.close();
			query.close();
		}catch(SQLException e){
			e.printStackTrace();
		}		
	}
}
