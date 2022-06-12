package pe.edu.pucp.g4algoritmos.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.time.ZonedDateTime;

public class AuxiliaryFunctions {
    

    public static Date addHoursToJavaUtilDate(Date date, double hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, (int)Math.round(hours));
        return calendar.getTime();
    }

    public static Date getNowTime(){
        // Creating the LocalDatetime object
        LocalDate currentLocalDate = LocalDate.now();

        // Getting system timezone
        ZoneId systemTimeZone = ZoneId.systemDefault();

        // converting LocalDateTime to ZonedDateTime with the system timezone
        ZonedDateTime zonedDateTime = currentLocalDate.atStartOfDay(systemTimeZone);

        // converting ZonedDateTime to Date using Date.from() and ZonedDateTime.toInstant()
        Date DateNow = Date.from(zonedDateTime.toInstant());

        return DateNow;
    }

    public static int compareDatesByMinutes(Date date1, Date date2){

        //0: equals
        //Negative: date 1 is more
        //Positive: date 2 is more

        return (date1.getMinutes() - date2.getMinutes());
    }

    public static int compareDatesBySeconds(Date date1, Date date2){

        //0: equals
        //Negative: date 1 is more
        //Positive: date 2 is more

        return (date1.getSeconds() - date2.getSeconds());
    }

    public static int compareDates(Date date1, Date date2){

        //0: equals
        //Positive: date 1 occurs later
        //Negative: date 2 occurs later

        return date1.compareTo(date2);
    }

    public static Date minimumDate(Date date1, Date date2){

        //0: equals
        //Positive: date 1 occurs later
        //Negative: date 2 occurs later

        if (date1.compareTo(date2) > 0 ){
            return date2;
        }
        else{
            return date1;
        }
    }

    public static Date maximumDate(Date date1, Date date2){

        //0: equals
        //Positive: date 1 occurs later
        //Negative: date 2 occurs later

        if (date1.compareTo(date2) > 0 ){
            return date1;
        }
        else{
            return date2;
        }
    }


    public static Date setDateWithTime(String datetime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(datetime);
        } catch (ParseException e) {
           
             
        }
        return date;
    }

  
    public static String toTxtFileName(LocalDateTime datetime){
        String year = Integer.toString(datetime.getYear());
        String month = Integer.toString(datetime.getMonthValue());
        String day = Integer.toString(datetime.getDayOfYear());
        String hour = Integer.toString(datetime.getHour());
        String minute = Integer.toString(datetime.getMinute());

        return year + "_" + month + "_" + day + "_" + hour + "_" + minute;
    }


}
