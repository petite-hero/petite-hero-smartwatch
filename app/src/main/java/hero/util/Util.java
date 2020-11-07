package hero.util;

import java.util.Calendar;

public class Util {

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

//    public static String calendarToTimeStr(Calendar cal){
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeStr.substring(0, 2)));
//        calendar.set(Calendar.MINUTE, Integer.parseInt(timeStr.substring(3, 5)));
////        return calendar;
//    }

}
