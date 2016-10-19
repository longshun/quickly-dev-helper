

import java.util.Calendar;

public class CalendarStyle {

	// 字体颜色
	public final static int iColorText = 0xff999999;
	public final static int iColorPicText = 0xff999999;
	public final static int iColorTextSelected = 0xff999999;
	public final static int iColorTextFocused = 0xff999999;
	public final static int iColorFrameHeader = 0xff666666;
	public final static int iColorFrameHeaderHoliday = 0xff5EAEED;
	public final static int iColorTextHoliday = 0xff5EAEED;
	// 背景颜色
	public final static int iColorBkg = 0xffffffff;
	public final static int iColorBkgSelectedLight = 0xff88bb88;
	public final static int iColorBkgSelectedDark = 0xff88bb88;
	public final static int iColorBkgFocusLight = 0xff225599;
	public final static int iColorBkgFocusDark = 0xff225599;
	public final static int iColorHasJournal = 0xffCC99CC;

	public static String getWeekDayName(int iDay) {
		return vecStrWeekDayNames[iDay];
	}

	private final static String[] vecStrWeekDayNames = getWeekDayNames();

	public static int getWeekDay(int index, int iFirstDayOfWeek) {
		int iWeekDay = -1;
		if (iFirstDayOfWeek == Calendar.MONDAY) {
			iWeekDay = index + Calendar.MONDAY;
			if (iWeekDay > Calendar.SATURDAY)
				iWeekDay = Calendar.SUNDAY;
		}
		if (iFirstDayOfWeek == Calendar.SUNDAY) {
			iWeekDay = index + Calendar.SUNDAY;
		}
		return iWeekDay;
	}

	public static int getColorFrameHeader(boolean bHoliday) {
		if (bHoliday)
			return iColorFrameHeaderHoliday;
		return iColorText;
	}

	public static int getColorText(boolean bHoliday) {
		if (bHoliday) {
			return iColorTextHoliday;
		}
		return iColorText;
	}

	private static String[] getWeekDayNames() {
		String[] vec = new String[10];
		vec[Calendar.SUNDAY] = "SUN";
		vec[Calendar.MONDAY] = "MON";
		vec[Calendar.TUESDAY] = "TUE";
		vec[Calendar.WEDNESDAY] = "WED";
		vec[Calendar.THURSDAY] = "THU";
		vec[Calendar.FRIDAY] = "FRI";
		vec[Calendar.SATURDAY] = "SAT";
		return vec;
	}

	public static int getColorTextHeader(boolean bHoliday) {
		if (bHoliday) {
			return iColorTextHoliday;
		}
		return 0xff000000;
	}
}