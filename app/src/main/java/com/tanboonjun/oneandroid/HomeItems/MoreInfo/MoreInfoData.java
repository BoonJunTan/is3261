package com.tanboonjun.oneandroid.HomeItems.MoreInfo;

import android.os.Parcel;
import android.os.Parcelable;

public class MoreInfoData implements Parcelable {
    public final String title;
    public final String description;

    public MoreInfoData(String title, String description) {
        this.title = title;
        this.description = description;
    }

    private MoreInfoData(Parcel in) {
        title = in.readString();
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
    }

    public static final Creator<MoreInfoData> CREATOR = new Creator<MoreInfoData>() {
        @Override
        public MoreInfoData createFromParcel(Parcel parcel) {
            return new MoreInfoData(parcel);
        }

        @Override
        public MoreInfoData[] newArray(int size) {
            return new MoreInfoData[size];
        }
    };

}
