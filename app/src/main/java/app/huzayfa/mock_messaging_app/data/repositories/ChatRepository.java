package app.huzayfa.mock_messaging_app.data.repositories;

import android.app.Application;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import app.huzayfa.mock_messaging_app.data.dao.DaoDatabase;
import app.huzayfa.mock_messaging_app.data.dao.DaoService;
import app.huzayfa.mock_messaging_app.data.models.Resource;
import app.huzayfa.mock_messaging_app.data.models.User;

public class ChatRepository {

    private DaoService daoService;
    private List<User> users;

    private MutableLiveData<Resource<List<User>>> usersListData;

    public ChatRepository(Application application) {
        DaoDatabase db = DaoDatabase.getDatabase(application);
        daoService = db.daoService();
        usersListData = new MutableLiveData<>();
    }

    public void fetchAllUsers() {
        usersListData.setValue(Resource.loading(null));
        new AsyncTask<Void, Void, List<User>>() {
            @Override
            protected List<User> doInBackground(Void... voids) {
                return daoService.fetchAllUsers();
            }

            @Override
            protected void onPostExecute(List<User> users) {
                if (users != null ) {
                    usersListData.setValue(Resource.success(users));
                } else {
                    usersListData.setValue(Resource.error("Fail", null));

                }
            }
        }.execute();
    }

    public LiveData<Resource<List<User>>> getUsersListData() {
        return usersListData;
    }
    //    public List<User> fetchAllUsers() {
//        DaoDatabase.databaseWriteExecutor.execute(() -> {
//            users = daoService.fetchAllUsers();
//        });
//        return users;
//
//    }

    public void addUsers(User... users) {
        DaoDatabase.databaseWriteExecutor.execute(() -> {
            daoService.insertAll(users);
        });
    }
}
