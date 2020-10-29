package app.huzayfa.mock_messaging_app.data.models;

import androidx.room.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLatestMessage {


    @Embedded
    private Message message;
    @Relation(
            parentColumn = "userId",
            entityColumn = "uId"
    )
    private User user;
}
