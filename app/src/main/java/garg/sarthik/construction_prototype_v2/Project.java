package garg.sarthik.construction_prototype_v2;

import android.os.Parcel;
import android.os.Parcelable;

public class Project implements Parcelable {


    String proName;
    String proId;
    String proDate;
    String proStatus;
    double proLatitude;
    double proLongitude;
    User user;
    Contractor contractor;

    public Project() {
    }

    public Project(String proStatus, User user, Contractor contractor) {
        this.proStatus = proStatus;
        this.user = user;
        this.contractor = contractor;
    }

    protected Project(Parcel in) {
        proName = in.readString();
        proId = in.readString();
        proDate = in.readString();
        proStatus = in.readString();
        proLatitude = in.readDouble();
        proLongitude = in.readDouble();
        user = in.readParcelable(User.class.getClassLoader());
        contractor = in.readParcelable(Contractor.class.getClassLoader());
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getProDate() {
        return proDate;
    }

    public void setProDate(String proDate) {
        this.proDate = proDate;
    }

    public String getProStatus() {
        return proStatus;
    }

    public void setProStatus(String proStatus) {
        this.proStatus = proStatus;
    }

    public double getProLatitude() {
        return proLatitude;
    }

    public void setProLatitude(double proLatitude) {
        this.proLatitude = proLatitude;
    }

    public double getProLongitude() {
        return proLongitude;
    }

    public void setProLongitude(double proLongitude) {
        this.proLongitude = proLongitude;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(proName);
        dest.writeString(proId);
        dest.writeString(proDate);
        dest.writeString(proStatus);
        dest.writeDouble(proLatitude);
        dest.writeDouble(proLongitude);
        dest.writeParcelable(user, flags);
        dest.writeParcelable(contractor, flags);
    }
}
