package app.huzayfa.mock_messaging_app.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "users")
public class User implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private Long uId;

    @ColumnInfo(name = "user_name")
    private String name;

    @ColumnInfo(name = "user_image")
    private String image;

    protected User(Parcel in) {
        if (in.readByte() == 0) {
            uId = null;
        } else {
            uId = in.readLong();
        }
        name = in.readString();
        image = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (uId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(uId);
        }
        parcel.writeString(name);
        parcel.writeString(image);
    }
}
