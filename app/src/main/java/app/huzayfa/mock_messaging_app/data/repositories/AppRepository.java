package app.huzayfa.mock_messaging_app.data.repositories;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import app.huzayfa.mock_messaging_app.data.dao.DaoDatabase;
import app.huzayfa.mock_messaging_app.data.dao.DaoService;
import app.huzayfa.mock_messaging_app.data.models.Message;
import app.huzayfa.mock_messaging_app.data.models.Resource;
import app.huzayfa.mock_messaging_app.data.models.User;
import app.huzayfa.mock_messaging_app.data.models.UserAndMessage;

/**
 * This repository class is responsible
 * to communicate with {@link DaoService}
 * to fetch and save data to {@link DaoDatabase}
 *
 * @author Huzayfa
 */
public class AppRepository {

    private DaoService daoService;

    private MutableLiveData<Resource<List<User>>> usersListData;
    private MutableLiveData<Resource<List<UserAndMessage>>> userMessagesData;

    public AppRepository(Application application) {
        DaoDatabase db = DaoDatabase.getDatabase(application);
        daoService = db.daoService();
        usersListData = new MutableLiveData<>();
        userMessagesData = new MutableLiveData<>();
    }

    /**
     * This method will run on background thread
     * using {@link java.util.concurrent.ExecutorService}
     * and will post the {@link #usersListData} with the returned
     * users
     */

    public void fetchAllUsers() {
        usersListData.setValue(Resource.loading(null));
        DaoDatabase.databaseWriteExecutor.execute(() -> {
            List<User> users = daoService.fetchAllUsers();
            if (users == null) {
                usersListData.postValue(Resource.error("Fails", null));
            } else usersListData.postValue(Resource.success(users));
        });

    }

    /**
     * @return a liveData object to observe and listen to any
     * change in data later on in {@link app.huzayfa.mock_messaging_app.viewModels.UsersListViewModel}
     */

    public LiveData<Resource<List<User>>> getUsersListData() {
        return usersListData;
    }

    /**
     * This method will run on background thread
     * using {@link java.util.concurrent.ExecutorService}
     * and will post the {@link #userMessagesData} with the returned
     * {@link UserAndMessage}
     */
    public void fetchAllUserMessages() {
        userMessagesData.setValue(Resource.loading(null));
        DaoDatabase.databaseWriteExecutor.execute(() -> {
            List<UserAndMessage> userAndMessages = daoService.fetchUserMessages();
            if (userAndMessages == null) {
                userMessagesData.postValue(Resource.error("Fails", null));
            } else userMessagesData.postValue(Resource.success(userAndMessages));
        });
    }

    /**
     * @return a liveData object to observe and listen to any
     * change in data later on in {@link app.huzayfa.mock_messaging_app.viewModels.ChatViewModel}
     */

    public LiveData<Resource<List<UserAndMessage>>> getUserMessages() {
        return userMessagesData;
    }


    /**
     * This method is responsible to update
     *
     * @param user to {@link DaoDatabase}
     */
    public void updateUser(User user) {
        DaoDatabase.databaseWriteExecutor.execute(() -> {
            daoService.updateUser(user);
        });
    }

    /**
     * This method is responsible to insert a new
     *
     * @param message to {@link DaoDatabase}
     */
    public void saveMessage(Message message) {
        DaoDatabase.databaseWriteExecutor.execute(() -> {
            daoService.saveNewMessage(message);
        });
    }

    /**
     * This method is responsible to insert a new
     *
     * @param user to {@link DaoDatabase}
     */
    public void addUser(User user) {
        DaoDatabase.databaseWriteExecutor.execute(() -> {
            daoService.insertUser(user);
        });
    }
}
