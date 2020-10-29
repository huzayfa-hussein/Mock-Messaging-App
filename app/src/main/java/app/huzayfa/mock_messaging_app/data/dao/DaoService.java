package app.huzayfa.mock_messaging_app.data.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import app.huzayfa.mock_messaging_app.data.models.Message;
import app.huzayfa.mock_messaging_app.data.models.User;
import app.huzayfa.mock_messaging_app.data.models.UserAndMessage;
import app.huzayfa.mock_messaging_app.data.models.UserLatestMessage;

@Dao
public interface DaoService {


    @Query("Select * from users order by sent_at desc")
    List<User> fetchAllUsers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Transaction
    @Query("Select * from users ")
    List<UserAndMessage> fetchUserMessages();

    @Transaction
    @Query("select * from messages order by sends_at desc ")
    List<UserLatestMessage> fetchUserLatestMessage();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveNewMessage(Message message);

    @Update
    void updateUser(User user);


}
