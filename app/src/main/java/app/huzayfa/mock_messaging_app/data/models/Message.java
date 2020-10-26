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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(tableName = "messages")
public class Message implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private Long mId;

    private Long userId;

    @ColumnInfo(name = "received_msg")
    private String receivedMessage;

    @ColumnInfo(name = "sent_msg")
    private String sentMessage;

    @ColumnInfo(name = "sends_at")
    @TypeConverters({DateDeserializer.class})
    private Date sendsAt;

    protected Message(Parcel in) {
        if (in.readByte() == 0) {
            mId = null;
        } else {
            mId = in.readLong();
        }
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readLong();
        }
        receivedMessage = in.readString();
        sentMessage = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (mId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(mId);
        }
        if (userId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(userId);
        }
        parcel.writeString(receivedMessage);
        parcel.writeString(sentMessage);
    }
}
