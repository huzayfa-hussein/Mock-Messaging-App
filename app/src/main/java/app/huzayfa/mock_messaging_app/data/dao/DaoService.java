package app.huzayfa.mock_messaging_app.data.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import app.huzayfa.mock_messaging_app.data.models.Message;
import app.huzayfa.mock_messaging_app.data.models.User;
import app.huzayfa.mock_messaging_app.data.models.UserAndMessage;

/**
 * An interface responding to access the objects
 * form {@link DaoDatabase}
 *
 * @author Huzayfa
 */
@Dao
public interface DaoService {

    /**
     * @return list of {@link User} with no duplicates
     * and ordered by date and with limit 200, if there is no users return empty
     */
    @Query("Select distinct * from users order by sent_at desc limit 200")
    List<User> fetchAllUsers();

    /**
     * This will save a new user to db
     *
     * @param user The user that will be saved
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    /**
     * This method will fetch all the messages for the selected user
     *
     * @return List of {@link Message} with the specified {@link User}
     * otherwise it will return empty list
     */
    @Transaction
    @Query("Select * from users ")
    List<UserAndMessage> fetchUserMessages();

    /**
     * This method will insert a new message to db
     *
     * @param message the {@link Message} that will be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveNewMessage(Message message);

    /**
     * This method will update {@link User} last sent message
     *
     * @param user The user that will be updated
     */
    @Update
    void updateUser(User user);


}
