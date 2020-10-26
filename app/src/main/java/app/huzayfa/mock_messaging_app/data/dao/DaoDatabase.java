package app.huzayfa.mock_messaging_app.data.dao;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import app.huzayfa.mock_messaging_app.data.models.Message;
import app.huzayfa.mock_messaging_app.data.models.User;

@Database(entities = {User.class, Message.class}, version = 1,exportSchema = false)
public abstract class DaoDatabase extends RoomDatabase {


    public abstract DaoService daoService();

    private static volatile DaoDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static DaoDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DaoDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DaoDatabase.class, "messaging_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
