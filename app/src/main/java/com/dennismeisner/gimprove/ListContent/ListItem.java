package com.dennismeisner.gimprove.ListContent;

import java.util.Date;

public abstract class ListItem {

    public Date date;

    public abstract String toString();

    public abstract String getContent();

    public abstract String getId();

}
