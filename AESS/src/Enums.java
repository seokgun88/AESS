public class Enums {
	String[] IndexToBlock = {"1a", "1b", "2a", "2b", "3a", "3b", "4a", "4b", "5a", "5b", "6a", "6b", "7a", "7b", "8a", "8b", "9a", "9b"};
	
	int DayToIndex(String Day) {
		switch(Day) {
			case "월" : return 1;
			case "화" : return 2;
			case "수" : return 3;
			case "목" : return 4;
			case "금" : return 5;
			case "토" : return 6;
			case "일" : return 7;
		}
		return 0;
	}

	int BlockToIndex(String block) {
		switch(block) {
			case "1a" : return 0;
			case "1b" : return 1;
			case "2a" : return 2;
			case "2b" : return 3;
			case "3a" : return 4;
			case "3b" : return 5;
			case "4a" : return 6;
			case "4b" : return 7;
			case "5a" : return 8;
			case "5b" : return 9;
			case "6a" : return 10;
			case "6b" : return 11;
			case "7a" : return 12;
			case "7b" : return 13;
			case "8a" : return 14;
			case "8b" : return 15;
			case "9a" : return 16;
			case "9b" : return 17;
		}
		return 0;
	}
		
	String BlockToTime(String block) {
		switch(block) {
			case "1a" : return "09:00";
			case "1b" : return "09:30";
			case "2a" : return "10:00";
			case "2b" : return "10:30";
			case "3a" : return "11:00";
			case "3b" : return "11:30";
			case "4a" : return "12:00";
			case "4b" : return "12:30";
			case "5a" : return "13:00";
			case "5b" : return "13:30";
			case "6a" : return "14:00";
			case "6b" : return "14:30";
			case "7a" : return "15:00";
			case "7b" : return "15:30";
			case "8a" : return "16:00";
			case "8b" : return "16:30";
			case "9a" : return "17:00";
			case "9b" : return "17:30";
		}
		return "00:00";
	}
	
	String TimeToBlock(String time){
		switch(time){
			 case "09:00" : return "1a";
			 case "09:30" : return "1b";
			 case "10:00" : return "2a";
			 case "10:30" : return "2b";
			 case "11:00" : return "3a";
			 case "11:30" : return "3b";
			 case "12:00" : return "4a";
			 case "12:30" : return "4b";
			 case "13:00" : return "5a";
			 case "13:30" : return "5b";
			 case "14:00" : return "6a";
			 case "14:30" : return "6b";
			 case "15:00" : return "7a";
			 case "15:30" : return "7b";
			 case "16:00" : return "8a";
			 case "16:30" : return "8b";
			 case "17:00" : return "9a";
			 case "17:30" : return "9b";
		}
		return "00";
	}
	
	int TimeToIndex(String time){
		switch(time){
			 case "09:00" : return 0;
			 case "09:30" : return 1;
			 case "10:00" : return 2;
			 case "10:30" : return 3;
			 case "11:00" : return 4;
			 case "11:30" : return 5;
			 case "12:00" : return 6;
			 case "12:30" : return 7;
			 case "13:00" : return 8;
			 case "13:30" : return 9;
			 case "14:00" : return 10;
			 case "14:30" : return 11;
			 case "15:00" : return 12;
			 case "15:30" : return 13;
			 case "16:00" : return 14;
			 case "16:30" : return 15;
			 case "17:00" : return 16;
			 case "17:30" : return 17;
		}
		return -1;
	}
	
	int TimeToRank(String day, String starttime){
		return 100*DayToIndex(day) + Integer.parseInt(starttime.substring(0, 2));
	}
}