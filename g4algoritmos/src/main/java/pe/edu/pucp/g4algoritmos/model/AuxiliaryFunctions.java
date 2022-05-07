package pe.edu.pucp.g4algoritmos.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

    public static Date setDateWithTime(String datetime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(datetime);
        } catch (ParseException e) {
           
             
        }
        return date;
    }


}
