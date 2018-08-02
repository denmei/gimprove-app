package com.example.dennismeisner.gimprove.ListContent;

import com.example.dennismeisner.gimprove.Utilities.DateFormater;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class ListItem {

    public Date date;

    public abstract String toString();

    public abstract String getContent();

    public abstract String getId();

}
