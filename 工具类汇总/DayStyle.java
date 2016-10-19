
import java.util.Calendar;

public class DayStyle {

	// 字体颜色
	public final static int iColorText = 0xff002200;
	public final static int iColorTextSelected = 0xff002200;
	public final static int iColorTextFocused = 0xff001122;
	// 背景颜色
	public final static int iColorBkg = 0xffffffff;
	public final static int iColorBkgSelectedLight = 0xff88bb88;
	public final static int iColorBkgSelectedDark = 0xff88bb88;
	public final static int iColorBkgFocusLight = 0xff225599;
	public final static int iColorBkgFocusDark = 0xff225599;
	public final static int iColorHasJournal = 0xffCC99CC;

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

}