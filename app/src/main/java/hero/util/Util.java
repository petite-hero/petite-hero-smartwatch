package hero.util;

import java.util.Calendar;

public class Util {

    private static final String YELLOW = "#FBC424";
    private static final String CYAN = "#00ADE8";
    private static final String BROWN = "#D67046";
    private static final String RED = "#EC546B";
    public static final String[] BADGE_COLORS = new String[]{
            YELLOW, CYAN, BROWN, BROWN, CYAN, RED, YELLOW, RED, RED, YELLOW,
            RED, CYAN, RED, CYAN, CYAN, CYAN, CYAN, CYAN, RED, BROWN
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
        calendar.set(Calendar.SECOND, 0);
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
        String badgeName = "aw";
        badgeName += id;
        return badgeName;
    }

}
