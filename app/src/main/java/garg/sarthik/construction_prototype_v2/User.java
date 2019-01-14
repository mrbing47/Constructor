package garg.sarthik.construction_prototype_v2;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    String userName;
    String userId;

    public User(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }

    public User() {
    }

    protected User(Parcel in) {
        userName = in.readString();
        userId = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(userId);
    }
}
