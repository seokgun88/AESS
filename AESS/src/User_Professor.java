import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Vector;
import javax.swing.JTable;

public class User_Professor{
	private Connection conn;
	private Enums enums = new Enums();
	
	public User_Professor(String id){
		conn = Info.getConn();
	}
	
	public void GetLectureSchedule(){
		try {
			Statement query = conn.createStatement();
			String sql = "select no from courseRelation where user_id='" +Info.getId()+ "';";
			ResultSet result = query.executeQuery(sql);			
			
			String lectureNo;
			Statement query2 = conn.createStatement();
			while(result.next()){
				lectureNo = result.getString("no");
				String sql2 = "select * from timeblock where user_id='" +Info.getId()+ "' and lectureNo=" +lectureNo;
				ResultSet result2 = query2.executeQuery(sql2);
				while(result2.next())
					System.out.println(result2.getString("day") + result2.getString("time"));
				result2.close();
			}
			result.close();
			query2.close();
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void SetIsLecture(String lectureNo, String isExam){
		try {
			Statement query = conn.createStatement();
			String sql = "select no from schedule where lecture_id = '" +lectureNo+ "';";
			ResultSet result = query.executeQuery(sql);	
			if(!result.next()){
				System.out.println("해당 과목 넘버가 없습니다.");
			}
			String no = result.getString("no");
			result.close();
			
			sql = "update timeblock set isAvailable='" +isExam+ "' where location != '-1' and scheduleNo='" +no+ "';";
			query.execute(sql);
			
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String GetIsLecture(String lectureNo){
		try {
			Statement query = conn.createStatement();
			String sql = "select no from schedule where stype='C' and lecture_id = '" +lectureNo+ "';";
			ResultSet result = query.executeQuery(sql);	
			if(!result.next()){
				System.out.println("해당 과목 넘버가 없습니다.");
			}
			String no = result.getString("no");
			result.close();
			
			sql = "select isAvailable from timeblock where location != '-1' and scheduleNo='" +no+ "';";
			result = query.executeQuery(sql);
			if(!result.next()){
				System.out.println("해당 과목 스케쥴이 없습니다.");
			}
			String isAvailable = result.getString("isAvailable");
			result.close();
			query.close();
			
			return isAvailable;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "에러";
	}
	public void SetRequiredInfo(String lectureNo, int max, int rooms, int timelen){
		try {
			Statement query = conn.createStatement();
			String sql = "select * from examRequired where lecNo='" +lectureNo+ "'";
			ResultSet result = query.executeQuery(sql);	
			if(!result.next()){
				sql = "insert into examRequired values ('" +lectureNo+ "', " +max+ ", " +rooms+ ", " +timelen+ ")";
				query.execute(sql);
			}
			else{
				sql = "update examRequired set max=" +max+ ", rooms=" +rooms+ ", examLen=" +timelen+ 
						" where lecNo='" +lectureNo+ "'";
				query.execute(sql);
			}
			result.close();
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String[] GetRequiredInfo(String lectureNo){
		String [] require = new String[3];
		for(int i=0; i<3; i++){
			require[i] = "";
		}
		try {
			Statement query = conn.createStatement();
			String sql = "select * from examRequired where lecNo='" +lectureNo+ "';";
			ResultSet result = query.executeQuery(sql);	
			if(!result.next()){
				System.out.println("시험정보가 없습니다.");
				return require;
			}
			require[0] = result.getString("max");
			require[1] = result.getString("rooms");
			require[2] = result.getString("examLen");
			
			result.close();
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return require;
	}
	/**이영석 추가 : 선호 시험 시간이 이미 있는 경우**/
	public void checkOverlapPreferredTime(int rank, String lectureCode){
		try {
			Statement query = conn.createStatement();
			String sql = "select no from schedule where stype='P' and rank=" +rank+ " and lecture_id = '" +lectureCode+ "';";
			ResultSet result = query.executeQuery(sql);	
			if(result.next()){
				sql = "delete from timeblock where scheduleNo=" +result.getString("no");
				query.execute(sql);
			}
			result.close();
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	public void SetPreferredTime(int rank, String day, String rtime, String lectureNo){
		try {			
			String time = enums.TimeToBlock(rtime);
			Statement query = conn.createStatement();
			String sql = "select no from schedule where stype='P' and rank=" +rank+ " and lecture_id = '" +lectureNo+ "';";
			ResultSet result = query.executeQuery(sql);	
			if(!result.next()){
				result.close();
				sql = "select name from schedule where lecture_id = '" +lectureNo+ "';";
				result = query.executeQuery(sql);
				if(!result.next()){
					System.out.println("해당 과목이 없습니다.");
				}
				String name = result.getString("name");
				result.close();
				
				sql = "insert into schedule(name, stype, user_id, lecture_id, rank) " +
						"values('" +name+ "', 'P', '" +Info.getId()+ "', '" +lectureNo+ "', " +rank+ ")";		
				query.execute(sql);

				sql = "select no from schedule where stype='P' and rank=" +rank+ " and lecture_id = '" +lectureNo+ "';";
				result = query.executeQuery(sql);
				result.next();
			}
			String no = result.getString("no");
			result.close();
			
			sql = "insert into timeblock(location, classroom, day, time, isAvailable, scheduleNo, user_id) " +
					"values (-2, -2, '" +day+ "', '" +time+ "', 'F', '" +no+ "', '" +Info.getId()+ "')";		
			query.execute(sql);
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public Vector GetPreferredTime(String lectureNo){
		Vector vPrefer = new Vector();
		Vector vPreferCol;
		String stime="", etime="", day="";
		for(int i=1; i<6; i++){
			try {
				Statement query = conn.createStatement();
				String sql = "select no from schedule where stype='P' and rank=" +i+ " and lecture_id = '" +lectureNo+ "';";
				ResultSet result = query.executeQuery(sql);	
				if(!result.next()){
					System.out.println(i+ ": 해당 과목 넘버가 없습니다.");
					continue;
				}
				String no = result.getString("no");
				result.close();
				
				sql = "select * from timeblock where location = '-2' and scheduleNo = '" +no+ "' order by time;";
				result = query.executeQuery(sql);
				if(result.next()){
					day = result.getString("day");
					stime = result.getString("time");
					etime = result.getString("time");
					while(result.next()){
						if(etime.substring(1).equals("a")){
							etime = etime.substring(0, 1) + "b";
						}
						else{
							etime = (Integer.parseInt(etime.substring(0, 1))+1) + "a";
						}
					}
					vPreferCol = new Vector();
					vPreferCol.addElement(i);
					vPreferCol.addElement(day);
					vPreferCol.addElement(enums.BlockToTime(stime));

					if(etime.substring(1).equals("a")){
						etime = etime.substring(0, 1) + "b";
					}
					else{
						etime = (Integer.parseInt(etime.substring(0, 1))+1) + "a";
					}
					
					vPreferCol.addElement(enums.BlockToTime(etime));
					vPrefer.addElement(vPreferCol);
				}
				result.close();
				query.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return vPrefer;
	}
	
	public void DelPreferredTime(int rank, String lectureNo){
		try {
			Statement query = conn.createStatement();
			String sql = "select no from schedule where stype='P' and rank=" +rank+ " and lecture_id = '" +lectureNo+ "';";
			ResultSet result = query.executeQuery(sql);	
			if(!result.next()){
				System.out.println("해당 과목 넘버가 없습니다.");
				return;
			}
			String no = result.getString("no");
			result.close();
			
			sql = "delete from timeblock where scheduleNo=" +no;
			query.execute(sql);
			sql = "delete from schedule where no='" +no+ "'";
			query.execute(sql);
			
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void SetImpossibleTime(int rank, String day, String rtime, String lectureNo){
		try {			
			String time = enums.TimeToBlock(rtime);
			Statement query = conn.createStatement();
			String sql = "select no from schedule where rank=" +rank+ " and stype='X' and lecture_id = '" +lectureNo+ "';";
			ResultSet result = query.executeQuery(sql);	
			if(!result.next()){
				result.close();
				sql = "select name from schedule where lecture_id = '" +lectureNo+ "';";
				result = query.executeQuery(sql);
				if(!result.next()){
					System.out.println("해당 과목이 없습니다.");
				}
				String name = result.getString("name");
				result.close();
				
				sql = "insert into schedule(name, stype, user_id, lecture_id, rank) " +
						"values('" +name+ "', 'X', '" +Info.getId()+ "', '" +lectureNo+ "', " +rank+ ")";		
				query.execute(sql);
				
				sql = "select no from schedule where rank=" +rank+ " and stype='X' and lecture_id = '" +lectureNo+ "';";
				result = query.executeQuery(sql);
				result.next();
			}
			String no = result.getString("no");
			result.close();
			
			sql = "insert into timeblock(location, classroom, day, time, isAvailable, scheduleNo, user_id) " +
					"values (-1, -1, '" +day+ "', '" +time+ "', 'F', '" +no+ "', '" +Info.getId()+ "')";		
			query.execute(sql);
			
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public Vector GetImpossibleTime(String lectureNo){
		Vector vImpossible = new Vector();
		Vector vImpossibleCol;
		Vector vRank = new Vector();
		String stime="", etime="", day="";
		try {
			Statement query = conn.createStatement();
			String sql = "select rank from schedule where stype='X' and lecture_id = '" +lectureNo+ "';";
			ResultSet result = query.executeQuery(sql);	
			if(!result.next()){
				System.out.println("해당 과목 넘버가 없습니다.");
				return vImpossible;
			}
			while(true){
				vRank.add(result.getString("rank"));
				if(!result.next())
					break;
			}
			result.close();
			
			for(int i=0; i<vRank.size(); i++){
				int rank = Integer.parseInt((String)vRank.get(i));
				System.out.println(rank);
				sql = "select no from schedule where rank=" +rank+ " and stype='X' and lecture_id = '" +lectureNo+ "';";
				result = query.executeQuery(sql);
				if(!result.next()){
					System.out.println("해당 과목 넘버가 없습니다.");
					continue;
				}
				String no = result.getString("no");
				result.close();
				
				sql = "select * from timeblock where location = '-1' and scheduleNo = '" +no+ "' order by time;";
				result = query.executeQuery(sql);
				if(result.next()){
					day = result.getString("day");
					stime = result.getString("time");
					etime = result.getString("time");
					while(result.next()){
						if(etime.substring(1).equals("a")){
							etime = etime.substring(0, 1) + "b";
						}
						else{
							etime = (Integer.parseInt(etime.substring(0, 1))+1) + "a";
						}
					}
					vImpossibleCol = new Vector();
					vImpossibleCol.addElement(day);
					vImpossibleCol.addElement(enums.BlockToTime(stime));
					if(etime.substring(1).equals("a")){
						etime = etime.substring(0, 1) + "b";
					}
					else{
						etime = (Integer.parseInt(etime.substring(0, 1))+1) + "a";
					}
					vImpossibleCol.addElement(enums.BlockToTime(etime));
					vImpossible.addElement(vImpossibleCol);
				}
				result.close();
				query.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vImpossible;
	}
	
	public void DelImpossibleTime(String day, String starttime, String endtime, String lectureNo){
		int rank= enums.TimeToRank(day, starttime);
		String time=enums.TimeToBlock(starttime);
		String etime=enums.TimeToBlock(endtime);
		try {
			Statement query = conn.createStatement();
			String sql = "select no from schedule where rank=" +rank+ " and stype='X' and lecture_id = '" +lectureNo+ "';";
			ResultSet result = query.executeQuery(sql);	
			if(!result.next()){
				System.out.println("해당 과목 넘버가 없습니다.");
				return;
			}
			String no = result.getString("no");
			result.close();
			
			sql = "delete from timeblock where scheduleNo=" +no;
			query.execute(sql);
			sql = "delete from schedule where rank=" +rank;
			query.execute(sql);
			
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void SetAssistant(String id, String lectureNo){
		try {
			Statement query = conn.createStatement();
			String sql = "select * from courseRelation where user_id='" +id+ "' and no='" +lectureNo+ "';";
			ResultSet result = query.executeQuery(sql);	
			if(result.next()){
				System.out.println("수업듣는 학생은 조교 불가능입니다..");
				return;
			}
			result.close();
			
			sql = "select * from member where id='" +id+ "';";
			result = query.executeQuery(sql);
			if(!result.next()){
				System.out.println("해당 학생이 존재하지 않습니다.");
				return;
			}
			result.close();
			
			sql = "update member set type='J' where id='" +id+ "';";
			query.execute(sql);
			
			sql = "insert into courseRelation values ('" +lectureNo+ "', '" +id+ "', 'J', 0)";
			query.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Vector GetAssistant(String lectureNo){
		Vector vAssi = new Vector();
		Vector vAssiCol;
		String assi_id;
		try {
			Statement query = conn.createStatement();
			String sql = "select * from courseRelation where user_type='J' and no='" +lectureNo+ "';";
			ResultSet result = query.executeQuery(sql);	
			
			Statement query2 = conn.createStatement();			
			while(result.next()){
				String sql2 = "select name from member where id='" +result.getString("user_id")+ "';";
				ResultSet result2 = query2.executeQuery(sql2);
				if(!result2.next()){
					System.out.println("조교가 명단에 없슨니다.");
					break;
				}
				vAssiCol = new Vector();
				vAssiCol.addElement(result.getString("user_id"));		
				vAssiCol.addElement(result2.getString("name"));
				vAssi.addElement(vAssiCol);
				
				result2.close();
			}
			query2.close();
			result.close();
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vAssi;
	}
	
	public void DeleteAssistant(String id){
		try {
			Statement query = conn.createStatement();
			String sql = "select * from member where id='" +id+ "';";
			ResultSet result = query.executeQuery(sql);	
			if(!result.next()){
				System.out.println("해당 학생이 존재하지 않습니다.");
				return;
			}
			result.close();
			
			sql = "update member set type='S' where id='" +id+ "';";
			try {
				query.execute(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sql = "delete from courseRelation where user_id = '" +id+ "';";
			query.execute(sql);
			
			query.close();;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void InitializeTable(JTable table, String[][] schedule_no){
		Statement scheduleState, blockState;
		ResultSet scheduleResult, blockResult;
		
		Enums enums = new Enums();
		int row;
		int col;
		
		try {
			scheduleState=conn.createStatement();
			scheduleResult = scheduleState.executeQuery("select * from schedule where user_id='" +Info.getId()+ "' and stype='C'");
			
			blockState=conn.createStatement();
			while(scheduleResult.next()){
				blockResult = blockState.executeQuery("select * from timeblock where scheduleNo='" +scheduleResult.getString("no")+ "'");
				while(blockResult.next()) {
					row = enums.BlockToIndex(blockResult.getString("time"));
					col = enums.DayToIndex(blockResult.getString("day"));
					table.setValueAt(scheduleResult.getString("name"), row, col);
					schedule_no[row][col] = scheduleResult.getString("lecture_id");
				}
				blockResult.close();				
			}
			blockState.close();
			scheduleResult.close();
			scheduleState.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Vector CheckStudentSchedule(String lectureNo){
		Vector vStdchk = new Vector();
		Vector vStdchkCol;
		String std_id;
		try {
			Statement query = conn.createStatement();
			String sql = "select * from courseRelation where user_type='S' and no='" +lectureNo+ "';";
			ResultSet result = query.executeQuery(sql);	
			
			Statement query2 = conn.createStatement();			
			while(result.next()){
				String sql2 = "select name from member where id='" +result.getString("user_id")+ "';";
				ResultSet result2 = query2.executeQuery(sql2);
				if(!result2.next()){
					System.out.println("학생이 명단에 없습니다.");
				}
				vStdchkCol = new Vector();
				vStdchkCol.addElement(result.getString("user_id"));		
				vStdchkCol.addElement(result2.getString("name"));
				vStdchkCol.addElement(result.getString("schedule_save").equals("0")?"X":"O");
				vStdchk.addElement(vStdchkCol);
				result2.close();
			}
			query2.close();
			result.close();
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vStdchk;
	}
	
	public void SelectExamTime(String lectureNo, int rank, int examLen){
		int roomcnt = 0;
		int availRooms [] = new int[100];
		int roomNo, time;
		String start, cur;
		boolean avail;
		try {
			Statement query = conn.createStatement();
			Statement query2 = conn.createStatement();
			Statement query3 = conn.createStatement();	
			
			String sql = "select no from classroom, required where lecNo=" +lectureNo+ " and max<maxSeat";
			ResultSet result = query.executeQuery(sql);			
			
			String sql2 = "select * from schedule where lecture_id='" +lectureNo+ "' and rank=" +rank;
			ResultSet result2 = query2.executeQuery(sql2);
			result2.next();			
			String day = result2.getString("day");
			start = result2.getString("start_time");
			result2.close();
			
			while(result.next()){
				avail = true;
				roomNo = result.getInt("no");
				cur = start;
				
				for(int i=0; i<examLen; i++){
					String sql3 = "select isAvailable from timeblock where classroom=" +roomNo+ " and " +
							"time='" +cur+ "' and day='" +day+ "';";
					ResultSet result3 = query3.executeQuery(sql3);
					if(!result3.next())
						break;
					else if(result3.getString("isAvailable").equals("F")){
						avail = false;
						break;
					}
					result3.close();
					
					time = Integer.parseInt(cur.substring(0,1));
					if(start.substring(1).equals("a")){
						cur = time+"b";
					}
					else
						cur = (time+1) + "a";
				}
				if(avail){
					availRooms[roomcnt++] = roomNo;
				}
			}
			result.close();
			query3.close();
			query2.close();
			query.close();
		}				
		 catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("가능한 강의실 목록");
		for(int i=0; i<roomcnt; i++){
			System.out.println(availRooms[i]);
		}
	}
	
	public int GetPeriodStartday(){
		Calendar calendar = Calendar.getInstance();
		try {
			Statement query = conn.createStatement();
			String sql = "select * from period";
			ResultSet result = query.executeQuery(sql);	
			if(result.next()){
				calendar.set(Calendar.YEAR, Integer.parseInt(result.getString("year")));
				calendar.set(Calendar.MONTH, Integer.parseInt(result.getString("month"))-1);
				calendar.set(Calendar.WEEK_OF_MONTH, Integer.parseInt(result.getString("week")));
				calendar.set(Calendar.DAY_OF_WEEK, 1);
			}
			else{
				System.out.println("시험 기간 불러오기 실패");
			}
			result.close();
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return calendar.get(Calendar.DATE);
	}
	
	public int[] SetPossibleTime(String lectureNo){
		int [] possibleRank = new int[5];
		for(int i=0; i<5; i++){
			possibleRank[i] = 0;
		}
		try {
			String day="", time="";
			Statement npQuery=conn.createStatement();
			ResultSet npResult=null;

			Statement query = conn.createStatement();
			String sql = "select no from schedule where stype='T' and lecture_id = '" +lectureNo+ "';";
			ResultSet result = query.executeQuery(sql);	
			if(!result.next()){
				result.close();
				sql = "insert into schedule(name, stype, user_id, lecture_id) " +
							"values('시험시간후보', 'T', '" +Info.getId()+ "', '" +lectureNo+ "')";
				query.execute(sql);
				
				sql = "select no from schedule where stype='T' and lecture_id = '" +lectureNo+ "';";
				result = query.executeQuery(sql);
				result.next();
			}
			String no = result.getString("no");
			result.close();
			
			//시험 바탕 시간표 초기화
			for(int i=0; i<7; i++){
				switch(i){
				case 0:
					day="월";
					break;
				case 1:
					day="화";
					break;
				case 2:
					day="수";
					break;
				case 3:
					day="목";
					break;
				case 4:
					day="금";
					break;
				case 5:
					day="토";
					break;
				case 6:
					day="일";
					break;
				}
				for(int j=0; j<9; j++){
					for(int k=0; k<2; k++){
						if(k==0)
							time = (j+1)+"a";
						else
							time = (j+1)+"b";
						sql = "insert into timeblock(location, classroom, day, time, isAvailable, scheduleNo, user_id)" +
								" values (-3, -3, '" +day+ "', '" +time+ "', 'T', '" +no+ "', '" +Info.getId()+ "')";
						
						query.execute(sql);
					}
				}
			}
			
			//교수 안되는 시간 표시
			Vector vRank = new Vector();
			sql = "select rank from schedule where stype='X' and lecture_id = '" +lectureNo+ "';";
			result = query.executeQuery(sql);
			if(!result.next()){
				System.out.println("해당 과목 넘버가 없습니다.");
			}
			else{
				while(true){
					vRank.add(result.getString("rank"));
					if(!result.next())
						break;
				}
			}
			result.close();
			
			for(int i=0; i<vRank.size(); i++){
				int rank = Integer.parseInt((String)vRank.get(i));
				sql = "select no from schedule where stype='X' and rank=" +rank+ " and lecture_id = '" +lectureNo+ "';";
				result = query.executeQuery(sql);
				if(!result.next()){
					return possibleRank;
				}
				String pNo = result.getString("no");
				result.close();
				
				sql = "select * from timeblock where scheduleNo=" +pNo;
				result = query.executeQuery(sql);
				while(result.next()){
					String npQry = "update timeblock set isAvailable='F' where time='" 
				+result.getString("time")+ "' and day='" +result.getString("day")+ "' and scheduleNo='" +no+ "'";
					npQuery.execute(npQry);
				}
				result.close();
			}
			
			//학생 안되는시간 표시
			Vector vStd = new Vector();
			sql = "select user_id from courseRelation where user_type='S' and no='" +lectureNo+ "'";
			result = query.executeQuery(sql);
			while(result.next()){
				vStd.add(result.getString("user_id"));
			}
			result.close();
			
			for(int i=0; i<vStd.size(); i++){
				String std = (String)vStd.get(i);
				sql = "select * from timeblock where user_id='" +std+ "'";
				result = query.executeQuery(sql);
				while(result.next()){
					String npQry = "update timeblock set isAvailable='" +result.getString("isAvailable")+ 
							"' where time='" +result.getString("time")+ "' and day='" +result.getString("day")+ 
							"' and scheduleNo='" +no+ "'";
					npQuery.execute(npQry);
				}
				result.close();
			}		
			
			//우선순위 시간중 가능한거 찾기
			boolean [] possible = new boolean [5];
			for(int i=0; i<5; i++){
				possible[i] = true;
			}
			sql = "select no from schedule where stype='T' and user_id='" +Info.getId()+ "'";
			result = query.executeQuery(sql);
			if(!result.next()){
				return possibleRank;
			}
			String tNo = result.getString("no");
			result.close();
			
			for(int i=1; i<6; i++){
				sql = "select no from schedule where stype='P' and rank=" +i+ " and user_id='" +Info.getId()+ "'";
				result = query.executeQuery(sql);
				if(!result.next()){
					possible[i-1] = false;
					possibleRank[i-1] = -1;
					System.out.println(i+": 해당 랭크가 불가능합니다.");
					continue;
				}
				String rNo = result.getString("no");
				result.close();
				
				sql = "select * from timeblock where scheduleNo='" +rNo+ "'";
				result = query.executeQuery(sql);
				while(result.next()){
					String npQry = "select * from timeblock" +
							" where isAvailable='F' and time='" +result.getString("time")+ 
							"' and day='" +result.getString("day")+ "' and scheduleNo=" +tNo;
					npResult = npQuery.executeQuery(npQry);
					if(npResult.next()){
						possible[i-1] = false;
						System.out.println(i+": 해당 랭크가 불가능합니다.");
						break;
					}
				}
				result.close();
			}
			for(int i=0; i<5; i++){
				if(possible[i]){
					possibleRank[i] = i+1;
					System.out.println((i+1)+"번");
				}
			}
			
			//안되는시간 블럭들 테이블에서 삭제			
			sql = "delete from timeblock where scheduleNo=" +no;
			query.execute(sql);
			
			query.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		return possibleRank;	
	}
	
	public Vector FindClassroom(String lectureNo, int rank){	
		Vector room = new Vector();	
		try {
			Statement query = conn.createStatement();
			Statement query2=conn.createStatement();
			Statement query3=conn.createStatement();
			
			ResultSet tbResult=null;
			boolean isPossible;
			String [] infos = GetRequiredInfo(lectureNo);
			Vector location = new Vector();
			Vector roomno = new Vector();

			String sql = "select no from schedule where stype='P' and rank=" +rank+ " and user_id='" +Info.getId()+ "'";
			ResultSet result = query.executeQuery(sql);	
			if(!result.next()){
				return room;
			}
			String rNo = result.getString("no");
			result.close();
			
			sql = "select * from classroom where maxSeat>=" +Integer.parseInt(infos[0]);
			result = query.executeQuery(sql);
			
			while(result.next()){
				isPossible = true;
				String sql2 = "select * from timeblock where scheduleNo='" +rNo+ "'";
				tbResult = query2.executeQuery(sql2);
				while(tbResult.next()){
					String sql3 = "select * from timeblock where location='" +result.getString("location")+ 
				"' and classroom='" +result.getString("no")+ "' and isAvailable='F' and day='" + tbResult.getString("day")+ 
				"' and time='" +tbResult.getString("time")+ "'";
					
					ResultSet result3 = query3.executeQuery(sql3);
					if(result3.next()){
						isPossible = false;
						break;
					}
					result3.close();
				}
				tbResult.close();
				if(isPossible){
					location.add(result.getString("location"));
					roomno.add(result.getString("no"));
				}
			}
			result.close();
			
			for(int i=0; i<Integer.parseInt(infos[1]); i++){
				room.addElement(location.get(i) +" "+ roomno.get(i));
			}
			
			query3.close();
			query2.close();
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return room;
	}
	
	public String SetFinal(String lectureNo, int rank){
		String rt="";
		try {
			Statement query = conn.createStatement();
			String sql = "update schedule set stype='F' where stype='P' and lecture_id='" +lectureNo+ "' and rank=" +rank;
			query.execute(sql);
			
			sql = "select * from schedule where stype='F' and lecture_id='" +lectureNo+ "' and rank=" +rank;
			ResultSet result = query.executeQuery(sql);
			result.next();
			rt = result.getString("no");
			
			result.close();
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rt;
	}
	
	public void SetFinalClassSchedule(String lectureNo, String day, String start, String end, String location, String roomno){
		try {
			String time=start;
			Statement query = conn.createStatement();
			String sql = "select * from schedule where lecture_id='" +lectureNo+ "' and stype='F'";
			ResultSet result = query.executeQuery(sql);
			result.next();
			String no = result.getString("no");
			result.close();
			while(true){
				sql = "insert into timeblock(location, classroom, day, time, isAvailable, scheduleNo)" +
						" values('" +location+ "', '" +roomno+ "', '" +day+ "', '" +time+ "', 'F', '" +no+ "')";
				query.execute(sql);

				if(time.substring(1).equals("a")){
					time = time.substring(0, 1)+"b";
				}
				else
					time = (Integer.parseInt(time.substring(0, 1))+1) + "a";
				if(time.equals(end))
					break;
			}
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void DelPossibleToFinal(String lectureNo){
		try {
			Statement query = conn.createStatement();
			String sql = "select * from schedule where lecture_id='" +lectureNo+ "' and stype='F'";
			ResultSet result = query.executeQuery(sql);
			result.next();
			String no = result.getString("no");
			result.close();
			
			sql = "delete from timeblock where scheduleNo='" +no+ "'";
			query.execute(sql);
			query.close();;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*******************이영석 추가 : 공지사항 입력**************************/
	public void SetNotice(String lectureCode, String message){
		try {
			Statement query = conn.createStatement();
			String sql = "select * from notice where courseNo='" +lectureCode+ "';";	 //공지사항이 있는지 확인
			ResultSet result = query.executeQuery(sql);
			if(result.next()){ //이미 있다면 공지사항 수정
				sql = "update notice set message='" +message+ "' where courseNo='" +lectureCode+ "';";
				query.execute(sql);
			}
			else{ //없다면 새 공지사항 입력
				sql = "insert into notice(courseNo, message) values ('" +lectureCode+ "', '" +message+ "');";
				query.execute(sql);
			}
			result.close();
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	/************************************************************************/
}
