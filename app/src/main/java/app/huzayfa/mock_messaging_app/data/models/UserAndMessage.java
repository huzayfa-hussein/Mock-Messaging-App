package app.huzayfa.mock_messaging_app.data.models;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAndMessage {

    @Embedded
    private User user;
    @Relation(
            parentColumn = "uId",
            entityColumn = "userId",
            entity = Message.class
    )
    private List<Message> message;
}
