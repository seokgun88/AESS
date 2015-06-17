import java.util.Calendar;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
/**************Calendar class to set exam period*********************/
public class CalendarModel extends AbstractTableModel {
	private Calendar calendar;
	private int year;
	private int month;
 
	/**Constructor**/
	public CalendarModel(int _year, int _month) {
		calendar = Calendar.getInstance();  // 현재의 일시 가지고 있는 칼렌더 취득
		if(_year==0&&_month==0) {
			_year = calendar.get(Calendar.YEAR);
			_month = calendar.get(Calendar.MONTH)+1;
		}
		this.year = _year;
		this.month = _month-1;
	}
 
	private int getDate(int row, int col) {
		calendar.set(Calendar.YEAR, this.year);
		calendar.set(Calendar.MONTH, this.month);
		calendar.set(Calendar.WEEK_OF_MONTH, row + 1);
		calendar.set(Calendar.DAY_OF_WEEK, col + 1);
		return calendar.get(Calendar.DATE);
	}
  
	@Override
	public int getColumnCount() {
		return 7;
	}
	
	@Override
	public int getRowCount() {
		if(getDate(6,0)<10) return 6;
		return 5;
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		if(col<6) if(getDate(row,col+1)==1) return 1; //Convert to days of week
		if(col==6) if(getDate(row+1, 0)==1) return 1;
	 
		return getDate(row, col)+1;
	}
 
	@Override
	public String getColumnName(int columnIndex) {
		String[] dayOfTheWeek = {"월", "화", "수", "목", "금", "토", "일"};
		return dayOfTheWeek[columnIndex];
	}
	
	public int getYear() {
		return this.year;
	}
	
	public int getMonth() {
		switch (this.month) {
			case Calendar.JANUARY: return 1;
			case Calendar.FEBRUARY: return 2;
			case Calendar.MARCH: return 3;
			case Calendar.APRIL: return 4;
			case Calendar.MAY: return 5;
			case Calendar.JUNE: return 6;
			case Calendar.JULY: return 7;
			case Calendar.AUGUST: return 8;
			case Calendar.SEPTEMBER: return 9;
			case Calendar.OCTOBER: return 10;
			case Calendar.NOVEMBER: return 11;
			case Calendar.DECEMBER: return 12;
			default: return 0;
		}
	}
}