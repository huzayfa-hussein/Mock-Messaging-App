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

    public void fetchAllUsers() {
        usersListData.setValue(Resource.loading(null));
        DaoDatabase.databaseWriteExecutor.execute(() -> {
            List<User> users = daoService.fetchAllUsers();
            if (users == null) {
                usersListData.postValue(Resource.error("Fails", null));
            } else usersListData.postValue(Resource.success(users));
        });

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

    public void addUser(User user) {
        DaoDatabase.databaseWriteExecutor.execute(() -> {
            daoService.insertUser(user);
        });
    }
}
