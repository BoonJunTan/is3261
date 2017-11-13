package com.tanboonjun.oneandroid;

/**
 * Created by Ronald on 13/11/17.
 */

public class Topic {
    private final String title;
    private final Boolean isEnrolled;

    public Topic (String title, boolean isEnrolled) {
        this.title = title;
        this.isEnrolled = isEnrolled;
    }

    public String getTitle() {
        return title;
    }

    public boolean getIsEnrolled() {
        return isEnrolled;
    }
}
