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
import app.huzayfa.mock_messaging_app.data.models.UserLatestMessage;

public class AppRepository {

    private DaoService daoService;
    private List<User> users;

    private MutableLiveData<Resource<List<User>>> usersListData;
    private MutableLiveData<Resource<List<UserAndMessage>>> userMessagesData;
    private MutableLiveData<Resource<List<UserLatestMessage>>> userLatestMessageData;

    public AppRepository(Application application) {
        DaoDatabase db = DaoDatabase.getDatabase(application);
        daoService = db.daoService();
        usersListData = new MutableLiveData<>();
        userMessagesData = new MutableLiveData<>();
        userLatestMessageData = new MutableLiveData<>();
    }

    public void fetchAllUsers() {
        usersListData.setValue(Resource.loading(null));
        DaoDatabase.databaseWriteExecutor.execute(() -> {
            List<User> users = daoService.fetchAllUsers();
            if (users == null) {
                usersListData.postValue(Resource.error("Fails", null));
            } else usersListData.postValue(Resource.success(users));
        });

    }

    public void fetchUsersWithLatestMessage() {
        userLatestMessageData.setValue(Resource.loading(null));
        DaoDatabase.databaseWriteExecutor.execute(() -> {
            List<UserLatestMessage> usersLatestMsg = daoService.fetchUserLatestMessage();
            if (usersLatestMsg == null) {
                userLatestMessageData.postValue(Resource.error("Fails", null));
            } else userLatestMessageData.postValue(Resource.success(usersLatestMsg));
        });

    }

    public LiveData<Resource<List<UserLatestMessage>>> getUserLatestMessageData() {
        return userLatestMessageData;
    }

    public LiveData<Resource<List<User>>> getUsersListData() {
        return usersListData;
    }


    public void fetchAllUserMessages() {
        userMessagesData.setValue(Resource.loading(null));
        DaoDatabase.databaseWriteExecutor.execute(() -> {
            List<UserAndMessage> userAndMessages = daoService.fetchUserMessages();
            if (userAndMessages == null) {
                userMessagesData.postValue(Resource.error("Fails", null));
            } else userMessagesData.postValue(Resource.success(userAndMessages));
        });
    }

    public LiveData<Resource<List<UserAndMessage>>> getUserMessages() {
        return userMessagesData;
    }

    public void addUsers(User... users) {
        DaoDatabase.databaseWriteExecutor.execute(() -> {
            daoService.insertAll(users);
        });
    }

    public void updateUser(User user) {
        DaoDatabase.databaseWriteExecutor.execute(() -> {
            daoService.updateUser(user);
        });
    }

    public void saveMessage(Message message) {
        DaoDatabase.databaseWriteExecutor.execute(() -> {
            daoService.saveNewMessage(message);
        });
    }
}
