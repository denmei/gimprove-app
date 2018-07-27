package com.example.dennismeisner.gimprove.ListContent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class ListItem {

    public Date date;
    protected SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.ssssssXXX");
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public abstract String toString();

    public abstract String getContent();

    public abstract String getId();

    public Date parseTimestamp(String timeStamp) throws ParseException {
        String timeStampWithT = timeStamp.replace(" ", "T");
        return timestampFormat.parse(timeStampWithT);
    }
}
