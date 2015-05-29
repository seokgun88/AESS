import java.sql.*;

import javax.swing.JOptionPane;

public class User_Admin{	
	private static Connection conn; //@@���� �߰� : static���� ������. test������
	
	public enum AccountState {NEW,AUTHED}
	
	public User_Admin(){
		conn = Info.getConn(); //@@���� �߰� : static���� ������. test������
	}
	
	public void GetExamSchedule(int classroom){
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
	
	public static boolean CreateClassRoom(String roomNo, String location, String maxSeat, String equipment){ //@@���� �߰� : test�� ���ؼ� static����
		boolean createroom_success = true; //@@���� �߰� : ����Ÿ�� bool����
		boolean room_check = true; //@@���� �߰� : room�̸� �������� üũ
		boolean seat_check = true; //@@���� �߰� : maxseat �������� üũ
		try {
			//@@���� �߰� : GUI���� ����(���)
			if(!location.startsWith("E")&&!location.startsWith("I")) {
				createroom_success = false;
				JOptionPane.showMessageDialog(null, "�ǹ��� �߸� �Է��ϼ̽��ϴ�.");
			}
			else if( location!=null&& roomNo!=null && maxSeat!=null) {
			}
			
			//@@���� �߰� : ȣ�� �˻� (����)
			for(int i = 0; i< roomNo.length(); i++){
				char room_char = roomNo.charAt(i);
				if( room_char < 48 || room_char > 58 || room_char == ' ') {
					//�ش� char���� ���ڰ� �ƴ� ���
					room_check = false;
				}
			}
			if(room_check == false || roomNo.length()==0) {
				createroom_success = false;
				JOptionPane.showMessageDialog(null, "ȣ���� �߸� �Է��ϼ̽��ϴ�.");
			}
			else if( location!=null&& roomNo!=null && maxSeat!=null) {
			}
			
			//@@���� �߰� : �¼� �˻� (����)
			for(int i = 0; i< maxSeat.length(); i++){
				char seat_char = maxSeat.charAt(i);
				if( seat_char < 48 || seat_char > 58 || seat_char==' ') {
					//�ش� char���� ���ڰ� �ƴ� ���
					seat_check = false;
				}
			}
			if(seat_check == false || maxSeat.length()==0) {
				createroom_success = false;
				JOptionPane.showMessageDialog(null, "�¼����� �߸� �Է��ϼ̽��ϴ�.");
			}
			else if( location!=null&& roomNo!=null && maxSeat!=null) {
			}
			
			if(createroom_success == true) { //@@���� �߰�  : create ����
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
		return createroom_success; //@@���� �߰�  : boolŸ�� ��ȯ
	}
	
	/**�̿��� �߰� : �μ����� ���� ���� �߰�**/
	public void SetClassRoom(String location, String no, String maxSeat, String equipment, String cLocation, String cNo, String cMax, String cEquipment){
		try {
			//������ �ǹ�, ��, �ο����� �˻��Ͽ�, ����Ǿ��� ���� �ִ� ����� ��� ������Ʈ �ع�����.
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
	
	public void DeleteClassRoom(String no, String location){
		try {
			String sql = "delete from classroom where no=" +no;
			Statement query = conn.createStatement();
			query.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void SetExamWeek(int year, String semester, String test, int month, int week, int sDate, int eDate) {
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
	
	public int CreateLectureSchedule(String name, String prof, String code){
		int resultno=0;
		try {
			String sql = "select no from schedule where stype='C' and lecture_id='"+code+"'";  //�ϴ� �ֳ� ���� select �� ����.
			Statement query = conn.createStatement();
			ResultSet result = query.executeQuery(sql);
			while(result.next()) {
				resultno = result.getInt("no");
			}
			result.close();
			
			//���࿡ while�� �� ���ȴµ� �ϳ��� ���ٸ�
			if(resultno==0) {
				sql = "insert into schedule(name, stype, user_id, lecture_id) values ('"+name+"', 'C', '"+prof+"', '"+code+"')";
				query.execute(sql);
				
				//�ٽ� select�Ѵ�.
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
	
	public int CreateOtherSchedule(String name){
		int resultno=0;
		try {
			String sql = "select no from schedule where stype='E' and name='"+name+"'"; //�ϴ� �ֳ� ���� select �� ����.
			Statement query = conn.createStatement();
			ResultSet result = query.executeQuery(sql);
			while(result.next()) {
				resultno = result.getInt("no");
			}
			result.close();
			//���࿡ while�� �� ���ȴµ� �ϳ��� ���ٸ�
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
	
	public void CreateTimeBlock(String id, String location, String classroom, String day, String time, int scheNo){
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
	
	
	/**������ �߰�**/
	public void approveUser(String id){
		//���� ���� ��û�� ���� ��� �� id�� ����� ���� ����. auth=F���� auth=T�� Update
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
	public void deleteUser(String id){
		/**�̿��� �߰� : ������ �ۼ�**/
		//id�� ����� ����. Delete���
		try{
			Statement query = conn.createStatement();
			String sql = "delete from member where id = '" +id+ "';";
			query.execute(sql);
			query.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	public void activateUser(String id, String state){
		/**�̿��� �߰� : ������ �ۼ�, state�� T,F������ DB ����� state ���¿� �״�� T,F ������Ʈ**/
		//state�� ������ ���� ��� �� id�� active������ ����.
		//Ȱ�������϶� ���� T, ��Ȱ�������϶��� F. Update���� ���.
		try{
			Statement query = conn.createStatement();
			String stateStr;
			if(isUserUnapproved(id))//���� ���ε��� ���� ȸ���� ���°� ������� �ʵ���.
				return;
			String sql = "update member set state='" +state+ "' where id='" +id+ "';";
			query.execute(sql);
			query.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	/**�̿��� �߰� : ������� state Ȯ��**/
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
	/**�̿��� �߰� : �л� ���н�û �������� Ȯ��**/
	public boolean isUserLeave(String id){
		boolean res=false;
		if(getUserState(id).equals("L"))
			res = true;
		return res;		
	}
	/**�̿��� �߰� : Ȱ��ȭ ��û ���� ���� Ȯ��**/
	public boolean isUserRequireActivate(String id){
		boolean res=false;
		if(getUserState(id).equals("A"))
			res = true;
		return res;				
	}
	/**�̿��� �߰� : ������ �ۼ�, state�� T�� ���Ե� �� Ȱ��ȭ�� ȸ��
	 * F�� ��Ȱ��ȭ�� ȸ��
	 * N(new)�� ���� ��û�� ȸ��
	 */
	public String [] getUserList(AccountState state){
		try{
			String [] temp;
			int resCnt, i=0;
			
			Statement query = conn.createStatement();
			String sql;
			ResultSet result;
			
			if(state==AccountState.NEW){
				sql = "select id from member where state='N';";//���� ��û�� ȸ���� ǥ��
			}else if(state==AccountState.AUTHED){
				sql = "select id from member where state!='N';";//���Ե� ȸ���� ǥ��(��Ȱ��ȭ ȸ�� ����)
			}else{
				//All accounts
				sql = "select id from member";//��ü ȸ�� ǥ��
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
	/**�̿��� �߰� : ��й�ȣ �ʱ�ȭ sql**/
	public void resetPassword(String id){
		try{
			Statement query = conn.createStatement();
			String sql = "select * from member where id='" +id+ "';";
			ResultSet result = query.executeQuery(sql);
			if(result.next()){
				if(result.getString("state").equals("R")){
					sql = "update member set pass='0000' where id='" +id+ "';";
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
