package com.example.proofoftraining.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bokajs on 09.04.2015.
 */
public class workweekParcelable extends workweek implements Parcelable {

    public static final Parcelable.Creator<workweekParcelable> CREATOR =
            new Parcelable.Creator<workweekParcelable>() {
                public workweekParcelable createFromParcel(Parcel in) {
                    long week_ID = in.readLong();
                    return new workweekParcelable(in, week_ID);
                }
                public workweekParcelable[] newArray(int size) {
                    return new workweekParcelable[size];
                }
            };

    private workweekParcelable(Parcel in, long week_ID) {
        super(week_ID);
        week_start = in.readString();
        week_end = in.readString();
        year_of_training = in.readInt();
        comment = in.readString();
    }


    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(week_ID);
        out.writeString(week_start);
        out.writeString(week_end);
        out.writeInt(year_of_training);
        out.writeString(comment);
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
}

