package com.example.dennismeisner.gimprove.ListContent;

import com.example.dennismeisner.gimprove.dummy.DummyContent;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<ListItem> ITEMS = new ArrayList<ListItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<Date, ListItem> ITEM_MAP = new HashMap<Date, ListItem>();

    private static final int COUNT = 25;

    private static void addItem(ListItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.date, item);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }
}
