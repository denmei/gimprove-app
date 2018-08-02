package com.example.dennismeisner.gimprove.Utilities;

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
    }
}

