package com.tanboonjun.oneandroid;

/**
 * Created by Ronald on 13/11/17.
 */

public class Topic {
    private final String title;
    private final Boolean isEnrolled;
    private final int id;

    public Topic (String title, boolean isEnrolled, int id) {
        this.title = title;
        this.isEnrolled = isEnrolled;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public boolean getIsEnrolled() {
        return isEnrolled;
    }

    public int getId() {
        return id;
    }
}
