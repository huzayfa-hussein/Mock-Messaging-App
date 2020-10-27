package app.huzayfa.mock_messaging_app.viewModels;

import android.app.Application;
import android.util.Log;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import app.huzayfa.mock_messaging_app.data.helper.MethodUtility;
import app.huzayfa.mock_messaging_app.data.models.Resource;
import app.huzayfa.mock_messaging_app.data.models.User;
import app.huzayfa.mock_messaging_app.data.repositories.ChatRepository;


public class ChatViewModel extends AndroidViewModel {

    private ChatRepository chatRepository;

    private List<User> users;

    private MutableLiveData<Resource<List<User>>> usersListData;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        chatRepository = new ChatRepository(application);
        usersListData = new MutableLiveData<>();
    }


    public void fetchAllUsers(LifecycleOwner owner) {
        usersListData.setValue(Resource.loading(null));
        chatRepository.fetchAllUsers();
        chatRepository.getUsersListData().observe(owner, listResource -> {
            switch (listResource.status) {
                case SUCCESS:
                    if (listResource.data != null) {
                        if (listResource.data.isEmpty()) {
                            setUsers(owner);
                        } else {
                            usersListData.setValue(Resource.success(listResource.data));
                            MethodUtility.toast(getApplication().getApplicationContext(), "Retrieved");
                        }
                        chatRepository.getUsersListData().removeObservers(owner);
                    }
                    break;
                case ERROR:
                    if (listResource.message != null) {
                        chatRepository.getUsersListData().removeObservers(owner);
                        MethodUtility.toast(getApplication().getApplicationContext(), listResource.message);
                    }
                    break;
            }
        });
//        if (users == null || users.isEmpty()) {
//            setUsers();
//        } else {
//            usersListData.setValue(Resource.success(users));
//        }
    }

    public void saveAllUsers(LifecycleOwner owner) {
        chatRepository.addUsers(users.toArray(new User[0]));
        fetchAllUsers(owner);
//        if (insertedUsers != null && !insertedUsers.isEmpty()) {
//            usersListData.setValue(Resource.success(insertedUsers));
//        } else {
//            usersListData.setValue(Resource.error("Failed to fetch Users", null));
//        }
    }


    private void setUsers(LifecycleOwner owner) {
        users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Faker faker = new Faker();
            User user = new User();
            user.setName(faker.name().fullName());
            user.setImage(faker.avatar().image());
            Log.i("NAME", "setUsers: " + user.getName());
            users.add(user);
        }
        saveAllUsers(owner);
    }

    public LiveData<Resource<List<User>>> getUsersList() {
        return usersListData;
    }

}
