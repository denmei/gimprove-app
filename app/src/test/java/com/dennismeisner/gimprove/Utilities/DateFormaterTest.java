package com.dennismeisner.gimprove.Utilities;

import com.dennismeisner.gimprove.Utilities.DateFormater;

import junit.framework.Assert;

import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class DateFormaterTest {

    @Test
    public void testGetDateString() {
        Calendar cal = Calendar.getInstance();
        cal.set(2005, 6, 2);
        Date testDate = cal.getTime();
        String dateString = DateFormater.getDateString(testDate);
        Assert.assertEquals("02-07-2005", dateString);
    }

    @Test
    public void testGetTimeStampString() {
        // TODO
    }

    @Test
    public void testParseTimeStamp() throws ParseException {
        String testString = "2018-08-01T08:01:18.397888+02:00";
        Date parsedString = DateFormater.parseTimestamp(testString);
        Assert.assertEquals("1533103278397", Long.toString(parsedString.getTime()));

        String testString2 = "2018-08-01 08:01:18.397888+02:00";
        Date parsedString2 = DateFormater.parseTimestamp(testString2);
        Assert.assertEquals("1533103278397", Long.toString(parsedString2.getTime()));

        String testString3 = "2018-08-01 08:01:18.397+02:00";
        Date parsedString3 = DateFormater.parseTimestamp(testString3);
        Assert.assertEquals("1533103278397", Long.toString(parsedString3.getTime()));

        String testString4 = "2018-07-05T08:27:22.390460+02:00";
        Date parsedString4 = DateFormater.parseTimestamp(testString4);
        Assert.assertEquals("Thu Jul 05 08:27:22 CEST 2018", parsedString4.toString());
    }
}

