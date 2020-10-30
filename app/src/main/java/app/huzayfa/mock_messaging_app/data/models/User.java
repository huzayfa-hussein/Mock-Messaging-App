package app.huzayfa.mock_messaging_app.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import app.huzayfa.mock_messaging_app.data.converters.DateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This entity class used to read and write
 * {@link User} to {@link app.huzayfa.mock_messaging_app.data.dao.DaoDatabase}
 * The {@link #name} is the full user name and {@link #image} is the avatar image
 * for each {@link User}
 * The {@link #msg} & {@link #sent_at} parameters used for fetch and update user latest
 * {@link Message}
 *
 * @author Huzayfa
 */

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

    @ColumnInfo(name = "msg")
    private String msg;

    @ColumnInfo(name = "sent_at")
    @TypeConverters({DateDeserializer.class})
    private Date sent_at;


    public User(String name, String image) {
        this.name = name;
        this.image = image;
    }

    protected User(Parcel in) {
        if (in.readByte() == 0) {
            uId = null;
        } else {
            uId = in.readLong();
        }
        name = in.readString();
        image = in.readString();
        msg = in.readString();
        if (in.readByte() == 0) {
            sent_at = null;
        } else sent_at = new Date(in.readLong());
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
        parcel.writeString(msg);
        if (sent_at == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(sent_at.getTime());
        }
    }
}
