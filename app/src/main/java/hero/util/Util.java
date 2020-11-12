package hero.util;

import java.util.Calendar;

public class Util {

    public static final String[] BADGE_COLORS = new String[]{
            "#ff0000", "#00ff00", "#0000ff", "#ff0000", "#00ff00", "#0000ff", "#ff0000", "#00ff00", "#0000ff",
            "#ff0000", "#00ff00", "#0000ff", "#ff0000", "#00ff00", "#0000ff", "#ff0000", "#00ff00", "#0000ff",
            "#ff0000", "#00ff00", "#0000ff", "#ff0000", "#00ff00", "#0000ff", "#ff0000", "#00ff00", "#0000ff",
            "#ff0000", "#00ff00", "#0000ff", "#ff0000", "#00ff00", "#0000ff", "#ff0000", "#00ff00", "#0000ff",
            "#ff0000", "#00ff00", "#0000ff", "#ff0000", "#00ff00", "#0000ff", "#ff0000", "#00ff00", "#0000ff",
    };

    public Util(){

    }

    public static long getLongHour0(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static Calendar timeStrToCalendar(String timeStr){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeStr.substring(0, 2)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeStr.substring(3, 5)));
        return calendar;
    }

    public static String calendarToTimeStr(Calendar cal){
        String result = "";
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        if (hour < 10) result += "0";
        result += hour;
        result += ":";
        if (minute < 10) result += 0;
        result += minute;
        return result;
    }

    public static String badgeIdToName(int id){
        String badgeName = "awards_";
        if (id < 10) badgeName += "0";
        badgeName += id;
        return badgeName;
    }

}
