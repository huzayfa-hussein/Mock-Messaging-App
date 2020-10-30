package app.huzayfa.mock_messaging_app.data.models;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * This class provides one to many relationship in
 * {@link app.huzayfa.mock_messaging_app.data.dao.DaoDatabase} between
 * {@link User} and {@link Message} entities
 * This relationship is used to find all the {@link #message}
 * for the selected {@link #user} in the adapter {@link app.huzayfa.mock_messaging_app.ui.adapters.UserAdapter}
 * and display them in {@link app.huzayfa.mock_messaging_app.ui.ChatActivity}
 *
 * @author Huzayfa
 */
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
