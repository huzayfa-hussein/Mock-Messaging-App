package app.huzayfa.mock_messaging_app.data.dao;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import app.huzayfa.mock_messaging_app.data.models.User;

@Dao
public interface DaoService {


    @Query("Select * from users")
    List<User> fetchAllUsers();

    @Insert
    void insertAll(User... users);


}
