import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Vector;
import javax.swing.JTable;


public class User_Professor extends User{
	Enums enums = new Enums();
	public User_Professor(String id, Connection conn){
		super(id, conn);
	}
	public void GetLectureSchedule(){
		String lectureNo;
		String qry2;
		Statement query2=null;
		ResultSet result2;
		qry = "select no from courseRelation where user_id='" +id+ "';";
		try {
			query2 = conn.createStatement();
			result = query.executeQuery(qry);
			while(result.next()){
				lectureNo = result.getString("no");
				qry2 = "select * from timeblock where user_id='" +id+ "' and lectureNo=" +lectureNo;
				result2 = query2.executeQuery(qry2);
				while(result2.next())
					System.out.println(result2.getString("day") + result2.getString("time"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void SetIsLecture(String lectureNo, String isExam){
		try {
			qry = "select no from schedule where lecture_id = '" +lectureNo+ "';";
			result = query.executeQuery(qry);
			if(!result.next()){
				System.out.println("해당 과목 넘버가 없습니다.");
			}
			String no = result.getString("no");
			qry = "update timeblock set isAvailable='" +isExam+ "' where location != '-1' and scheduleNo='" +no+ "';";
			query.execute(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String GetIsLecture(String lectureNo){
		try {
			qry = "select no from schedule where stype='C' and lecture_id = '" +lectureNo+ "';";
			result = query.executeQuery(qry);
			if(!result.next()){
				System.out.println("해당 과목 넘버가 없습니다.");
			}
			String no = result.getString("no");
			qry = "select isAvailable from timeblock where location != '-1' and scheduleNo='" +no+ "';";
			result = query.executeQuery(qry);
			if(!result.next()){
				System.out.println("해당 과목 스케쥴이 없습니다.");
			}
			return result.getString("isAvailable");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "에러";
	}
	public void SetRequiredInfo(String lectureNo, int max, int rooms, int timelen){
		try {
			qry = "select * from examRequired where lecNo='" +lectureNo+ "'";
			result = query.executeQuery(qry);
			if(!result.next()){
				qry = "insert into examRequired values ('" +lectureNo+ "', " +max+ ", " +rooms+ ", " +timelen+ ")";
				query.execute(qry);
			}
			else{
				qry = "update examRequired set max=" +max+ ", rooms=" +rooms+ ", examLen=" +timelen+ 
						" where lecNo='" +lectureNo+ "'";
				query.execute(qry);
			}
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
		qry = "select * from examRequired where lecNo='" +lectureNo+ "';";
		try {
			result = query.executeQuery(qry);
			if(!result.next()){
				System.out.println("시험정보가 없습니다.");
				return require;
			}
			require[0] = result.getString("max");
			require[1] = result.getString("rooms");
			require[2] = result.getString("examLen");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return require;
	}
	public void SetPreferredTime(int rank, String day, String rtime, String lectureNo){
		try {			
			String time = enums.TimeToBlock(rtime);
			qry = "select no from schedule where stype='P' and rank=" +rank+ " and lecture_id = '" +lectureNo+ "';";
			result = query.executeQuery(qry);
			if(!result.next()){
				qry = "select name from schedule where lecture_id = '" +lectureNo+ "';";
				result = query.executeQuery(qry);
				if(!result.next()){
					System.out.println("해당 과목이 없습니다.");
				}
				String name = result.getString("name");
				
				qry = "insert into schedule(name, stype, user_id, lecture_id, rank) " +
						"values('" +name+ "', 'P', '" +id+ "', '" +lectureNo+ "', " +rank+ ")";		
				query.execute(qry);

				qry = "select no from schedule where stype='P' and rank=" +rank+ " and lecture_id = '" +lectureNo+ "';";
				result = query.executeQuery(qry);
				result.next();
			}
			String no = result.getString("no");
			
			qry = "insert into timeblock(location, classroom, day, time, isAvailable, scheduleNo, user_id) " +
					"values (-2, -2, '" +day+ "', '" +time+ "', 'F', '" +no+ "', '" +id+ "')";
		
			query.execute(qry);
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
				qry = "select no from schedule where stype='P' and rank=" +i+ " and lecture_id = '" +lectureNo+ "';";
				result = query.executeQuery(qry);
				if(!result.next()){
					System.out.println(i+ ": 해당 과목 넘버가 없습니다.");
					continue;
				}
				String no = result.getString("no");
				qry = "select * from timeblock where location = '-2' and scheduleNo = '" +no+ "' order by time;";
				result = query.executeQuery(qry);
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
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return vPrefer;
	}
	public void DelPreferredTime(int rank, String lectureNo){
		try {
			qry = "select no from schedule where stype='P' and rank=" +rank+ " and lecture_id = '" +lectureNo+ "';";
			result = query.executeQuery(qry);
			if(!result.next()){
				System.out.println("해당 과목 넘버가 없습니다.");
				return;
			}
			String no = result.getString("no");
			qry = "delete from timeblock where scheduleNo=" +no;
			query.execute(qry);
			qry = "delete from schedule where no='" +no+ "'";
			query.execute(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public void SetImpossibleTime(int rank, String day, String rtime, String lectureNo){
		try {			
			String time = enums.TimeToBlock(rtime);
			qry = "select no from schedule where rank=" +rank+ " and stype='X' and lecture_id = '" +lectureNo+ "';";
			result = query.executeQuery(qry);
			if(!result.next()){
				qry = "select name from schedule where lecture_id = '" +lectureNo+ "';";
				result = query.executeQuery(qry);
				if(!result.next()){
					System.out.println("해당 과목이 없습니다.");
				}
				String name = result.getString("name");
				
				qry = "insert into schedule(name, stype, user_id, lecture_id, rank) " +
						"values('" +name+ "', 'X', '" +id+ "', '" +lectureNo+ "', " +rank+ ")";		
				query.execute(qry);
				
				qry = "select no from schedule where rank=" +rank+ " and stype='X' and lecture_id = '" +lectureNo+ "';";
				result = query.executeQuery(qry);
				result.next();
			}
			String no = result.getString("no");
			
			qry = "insert into timeblock(location, classroom, day, time, isAvailable, scheduleNo, user_id) " +
					"values (-1, -1, '" +day+ "', '" +time+ "', 'F', '" +no+ "', '" +id+ "')";
		
			query.execute(qry);
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
			qry = "select rank from schedule where stype='X' and lecture_id = '" +lectureNo+ "';";
			result = query.executeQuery(qry);
			if(!result.next()){
				System.out.println("해당 과목 넘버가 없습니다.");
				return vImpossible;
			}
			while(true){
				vRank.add(result.getString("rank"));
				if(!result.next())
					break;
			}
			
			for(int i=0; i<vRank.size(); i++){
				int rank = Integer.parseInt((String)vRank.get(i));
				System.out.println(rank);
				qry = "select no from schedule where rank=" +rank+ " and stype='X' and lecture_id = '" +lectureNo+ "';";
				result = query.executeQuery(qry);
				if(!result.next()){
					System.out.println("해당 과목 넘버가 없습니다.");
					continue;
				}
				String no = result.getString("no");
				qry = "select * from timeblock where location = '-1' and scheduleNo = '" +no+ "' order by time;";
				result = query.executeQuery(qry);
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
			qry = "select no from schedule where rank=" +rank+ " and stype='X' and lecture_id = '" +lectureNo+ "';";
			result = query.executeQuery(qry);
			if(!result.next()){
				System.out.println("해당 과목 넘버가 없습니다.");
				return;
			}
			String no = result.getString("no");
			qry = "delete from timeblock where scheduleNo=" +no;
			query.execute(qry);
			qry = "delete from schedule where rank=" +rank;
			query.execute(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public void SetAssistant(String id, String lectureNo){
		try {
			qry = "select * from courseRelation where user_id='" +id+ "' and no='" +lectureNo+ "';";
			result = query.executeQuery(qry);
			if(result.next()){
				System.out.println("수업듣는 학생은 조교 불가능입니다..");
				return;
			}			
			qry = "select * from member where id='" +id+ "';";
			result = query.executeQuery(qry);
			if(!result.next()){
				System.out.println("해당 학생이 존재하지 않습니다.");
				return;
			}			
			qry = "update member set type='J' where id='" +id+ "';";
			query.execute(qry);
			
			qry = "insert into courseRelation values ('" +lectureNo+ "', '" +id+ "', 'J', 0)";
			query.execute(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Vector GetAssistant(String lectureNo){
		Vector vAssi = new Vector();
		Vector vAssiCol;
		String assi_id;
		String qry2;
		Statement query2=null;
		ResultSet result2;
		qry = "select * from courseRelation where user_type='J' and no='" +lectureNo+ "';";
		try {
			result = query.executeQuery(qry);
			query2 = conn.createStatement();
			
			while(result.next()){
				qry2 = "select name from member where id='" +result.getString("user_id")+ "';";
				result2 = query2.executeQuery(qry2);
				if(!result2.next()){
					System.out.println("조교가 명단에 없슨니다.");
					break;
				}
				vAssiCol = new Vector();
				vAssiCol.addElement(result.getString("user_id"));		
				vAssiCol.addElement(result2.getString("name"));
				vAssi.addElement(vAssiCol);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vAssi;
	}
	public void DeleteAssistant(String id){
		try {
			qry = "select * from member where id='" +id+ "';";
			result = query.executeQuery(qry);
			if(!result.next()){
				System.out.println("해당 학생이 존재하지 않습니다.");
				return;
			}			
			qry = "update member set type='S' where id='" +id+ "';";
			try {
				query.execute(qry);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			qry = "delete from courseRelation where user_id = '" +id+ "';";
			query.execute(qry);
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
			scheduleResult = scheduleState.executeQuery("select * from schedule where user_id='" +id+ "' and stype='C'");
			while(scheduleResult.next()){
				blockState=conn.createStatement();
				blockResult = blockState.executeQuery("select * from timeblock where scheduleNo='" +scheduleResult.getString("no")+ "'");
				while(blockResult.next()) {
					row = enums.BlockToIndex(blockResult.getString("time"));
					col = enums.DayToIndex(blockResult.getString("day"));
					table.setValueAt(scheduleResult.getString("name"), row, col);
					schedule_no[row][col] = scheduleResult.getString("lecture_id");
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Vector CheckStudentSchedule(String lectureNo){
		Vector vStdchk = new Vector();
		Vector vStdchkCol;
		String std_id;
		String qry2;
		Statement query2=null;
		ResultSet result2;
		qry = "select * from courseRelation where user_type='S' and no='" +lectureNo+ "';";
		try {
			result = query.executeQuery(qry);
			query2 = conn.createStatement();
			
			while(result.next()){
				qry2 = "select name from member where id='" +result.getString("user_id")+ "';";
				result2 = query2.executeQuery(qry2);
				if(!result2.next()){
					System.out.println("학생이 명단에 없슨니다.");
				}
				vStdchkCol = new Vector();
				vStdchkCol.addElement(result.getString("user_id"));		
				vStdchkCol.addElement(result2.getString("name"));
				vStdchkCol.addElement(result.getString("schedule_save").equals("0")?"X":"O");
				vStdchk.addElement(vStdchkCol);
			}
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
		Statement query2=null;
		Statement query3=null;
		ResultSet result2, result3;
		boolean avail;
		try {
			query2 = conn.createStatement();
			query3 = conn.createStatement();			
			String qry = "select no from classroom, required where lecNo=" +lectureNo+ " and max<maxSeat";
			String qry2 = "select * from schedule where lecture_id='" +lectureNo+ "' and rank=" +rank;
			
			result = query.executeQuery(qry);
			result2 = query2.executeQuery(qry2);
			result2.next();
			String day = result2.getString("day");
			start = result2.getString("start_time");
			
			while(result.next()){
				avail = true;
				roomNo = result.getInt("no");
				cur = start;
				
				for(int i=0; i<examLen; i++){
					String qry3 = "select isAvailable from timeblock where classroom=" +roomNo+ " and " +
							"time='" +cur+ "' and day='" +day+ "';";
					result3 = query3.executeQuery(qry3);
					if(!result3.next())
						break;
					else if(result3.getString("isAvailable").equals("F")){
						avail = false;
						break;
					}
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
			qry = "select * from period";
			result = query.executeQuery(qry);
			if(result.next()){
				calendar.set(Calendar.YEAR, Integer.parseInt(result.getString("year")));
				calendar.set(Calendar.MONTH, Integer.parseInt(result.getString("month"))-1);
				calendar.set(Calendar.WEEK_OF_MONTH, Integer.parseInt(result.getString("week")));
				calendar.set(Calendar.DAY_OF_WEEK, 1);
			}
			else{
				System.out.println("시험 기간 불러오기 실패");
			}
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
			
			qry = "select no from schedule where stype='T' and lecture_id = '" +lectureNo+ "';";
			result = query.executeQuery(qry);
			if(!result.next()){
				qry = "insert into schedule(name, stype, user_id, lecture_id) " +
							"values('시험시간후보', 'T', '" +id+ "', '" +lectureNo+ "')";
				query.execute(qry);
				
				qry = "select no from schedule where stype='T' and lecture_id = '" +lectureNo+ "';";
				result = query.executeQuery(qry);
				result.next();
			}
			String no = result.getString("no");
			
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
						qry = "insert into timeblock(location, classroom, day, time, isAvailable, scheduleNo, user_id)" +
								" values (-3, -3, '" +day+ "', '" +time+ "', 'T', '" +no+ "', '" +id+ "')";
						
						query.execute(qry);
					}
				}
			}
			
			//교수 안되는 시간 표시
			Vector vRank = new Vector();
			qry = "select rank from schedule where stype='X' and lecture_id = '" +lectureNo+ "';";
			result = query.executeQuery(qry);
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
			
			for(int i=0; i<vRank.size(); i++){
				int rank = Integer.parseInt((String)vRank.get(i));
				qry = "select no from schedule where stype='X' and rank=" +rank+ " and lecture_id = '" +lectureNo+ "';";
				result = query.executeQuery(qry);
				if(!result.next()){
					return possibleRank;
				}
				String pNo = result.getString("no");
				qry = "select * from timeblock where scheduleNo=" +pNo;
				result = query.executeQuery(qry);
				while(result.next()){
					String npQry = "update timeblock set isAvailable='F' where time='" 
				+result.getString("time")+ "' and day='" +result.getString("day")+ "' and scheduleNo='" +no+ "'";
					npQuery.execute(npQry);
				}
			}
			
			//학생 안되는시간 표시
			Vector vStd = new Vector();
			qry = "select user_id from courseRelation where user_type='S' and no='" +lectureNo+ "'";
			result = query.executeQuery(qry);
			while(result.next()){
				vStd.add(result.getString("user_id"));
			}
			for(int i=0; i<vStd.size(); i++){
				String std = (String)vStd.get(i);
				qry = "select * from timeblock where user_id='" +std+ "'";
				result = query.executeQuery(qry);
				while(result.next()){
					String npQry = "update timeblock set isAvailable='" +result.getString("isAvailable")+ 
							"' where time='" +result.getString("time")+ "' and day='" +result.getString("day")+ 
							"' and scheduleNo='" +no+ "'";
					npQuery.execute(npQry);
				}
			}		
			
			//우선순위 시간중 가능한거 찾기
			boolean [] possible = new boolean [5];
			for(int i=0; i<5; i++){
				possible[i] = true;
			}
			qry = "select no from schedule where stype='T' and user_id='" +id+ "'";
			result = query.executeQuery(qry);
			if(!result.next()){
				return possibleRank;
			}
			String tNo = result.getString("no");
			
			for(int i=1; i<6; i++){
				qry = "select no from schedule where stype='P' and rank=" +i+ " and user_id='" +id+ "'";
				result = query.executeQuery(qry);
				if(!result.next()){
					possible[i-1] = false;
					possibleRank[i-1] = -1;
					System.out.println(i+": 해당 랭크가 불가능합니다.");
					continue;
				}
				String rNo = result.getString("no");
				
				qry = "select * from timeblock where scheduleNo='" +rNo+ "'";
				result = query.executeQuery(qry);
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
			}
			for(int i=0; i<5; i++){
				if(possible[i]){
					possibleRank[i] = i+1;
					System.out.println((i+1)+"번");
				}
			}
			
			//안되는시간 블럭들 테이블에서 삭제			
			qry = "delete from timeblock where scheduleNo=" +no;
			query.execute(qry);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		return possibleRank;	
	}
	public Vector FindClassroom(String lectureNo, int rank){	
		Vector room = new Vector();	
		try {
			Statement query2=conn.createStatement();
			Statement query3=conn.createStatement();
			ResultSet tbResult=null;
			ResultSet result3=null;
			boolean isPossible;
			String [] infos = GetRequiredInfo(lectureNo);
			Vector location = new Vector();
			Vector roomno = new Vector();
			qry = "select no from schedule where stype='P' and rank=" +rank+ " and user_id='" +id+ "'";
			result = query.executeQuery(qry);
			if(!result.next()){
				return room;
			}
			String rNo = result.getString("no");
			qry = "select * from classroom where maxSeat>=" +Integer.parseInt(infos[0]);
			result = query.executeQuery(qry);
			qry = "select * from timeblock where scheduleNo='" +rNo+ "'";
			while(result.next()){
				isPossible = true;
				tbResult = query2.executeQuery(qry);
				while(tbResult.next()){
					String qry2 = "select * from timeblock where location='" +result.getString("location")+ 
				"' and classroom='" +result.getString("no")+ "' and isAvailable='F' and day='" + tbResult.getString("day")+ 
				"' and time='" +tbResult.getString("time")+ "'";
					result3 = query3.executeQuery(qry2);
					if(result3.next()){
						isPossible = false;
						break;
					}
				}
				if(isPossible){
					location.add(result.getString("location"));
					roomno.add(result.getString("no"));
				}
			}
			
			for(int i=0; i<Integer.parseInt(infos[1]); i++){
				room.addElement(location.get(i) +" "+ roomno.get(i));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return room;
	}
	public String SetFinal(String lectureNo, int rank){
		String rt="";
		try {
			qry = "update schedule set stype='F' where stype='P' and lecture_id='" +lectureNo+ "' and rank=" +rank;
			query.execute(qry);
			qry = "select * from schedule where stype='F' and lecture_id='" +lectureNo+ "' and rank=" +rank;
			result = query.executeQuery(qry);
			result.next();
			rt = result.getString("no");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rt;
	}
	public void SetFinalClassSchedule(String lectureNo, String day, String start, String end, String location, String roomno){
		try {
			String time=start;
			qry = "select * from schedule where lecture_id='" +lectureNo+ "' and stype='F'";
			result = query.executeQuery(qry);
			result.next();
			String no = result.getString("no");
			while(true){
				qry = "insert into timeblock(location, classroom, day, time, isAvailable, scheduleNo)" +
						" values('" +location+ "', '" +roomno+ "', '" +day+ "', '" +time+ "', 'F', '" +no+ "')";
				query.execute(qry);

				if(time.substring(1).equals("a")){
					time = time.substring(0, 1)+"b";
				}
				else
					time = (Integer.parseInt(time.substring(0, 1))+1) + "a";
				if(time.equals(end))
					break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public void DelPossibleToFinal(String lectureNo){
		try {
			qry = "select * from schedule where lecture_id='" +lectureNo+ "' and stype='F'";
			result = query.executeQuery(qry);
			result.next();
			String no = result.getString("no");
			
			qry = "delete from timeblock where scheduleNo='" +no+ "'";
			query.execute(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
