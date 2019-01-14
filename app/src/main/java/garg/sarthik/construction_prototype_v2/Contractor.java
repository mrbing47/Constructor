package garg.sarthik.construction_prototype_v2;

import android.os.Parcel;
import android.os.Parcelable;

public class Contractor implements Parcelable {

    String contName;
    String contId;

    public Contractor() {
    }

    public Contractor(String contName, String contId) {
        this.contName = contName;
        this.contId = contId;
    }

    protected Contractor(Parcel in) {
        contName = in.readString();
        contId = in.readString();
    }

    public static final Creator<Contractor> CREATOR = new Creator<Contractor>() {
        @Override
        public Contractor createFromParcel(Parcel in) {
            return new Contractor(in);
        }

        @Override
        public Contractor[] newArray(int size) {
            return new Contractor[size];
        }
    };

    public String getContName() {
        return contName;
    }

    public String getContId() {
        return contId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contName);
        dest.writeString(contId);
    }
}
