package com.dennismeisner.gimprove.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class DateFormater {

    static private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    static private SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSXXX");

    /**
     * Format Date to String of dd-MM-yyyy
     * @param date input Date
     * @return Date as String with format dd-MM-yyyy
     */
    public static String getDateString(Date date) {
        return dateFormat.format(date);
    }

    /**
     * Parse a date to a timestamp
     * @param date input date
     * @return Timestamp as String with format yyyy-MM-dd'T'hh:mm:ss.ssssssXXX
     */
    public static String getTimeStampString(Date date) {
        return timestampFormat.format(date);
    }

    public static Date parseTimestamp(String timeStamp) throws ParseException {
        SimpleDateFormat ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSXXX");
        ts.setTimeZone(TimeZone.getDefault());
        // remove T
        String timeStampWithT = timeStamp.replace("T", " ");
        // remove last 3 digits of seconds
        String[] fragments = timeStampWithT.split("\\.");
        String[] subfragments = fragments[1].split("\\+");
        subfragments[0] = subfragments[0].substring(0,3);
        String completeTimeStamp = fragments[0] + "." + subfragments[0] + "+" + subfragments[1];
        return ts.parse(completeTimeStamp);
    }
}
