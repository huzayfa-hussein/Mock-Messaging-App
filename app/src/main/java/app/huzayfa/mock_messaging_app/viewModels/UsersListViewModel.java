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
import app.huzayfa.mock_messaging_app.data.models.UserLatestMessage;
import app.huzayfa.mock_messaging_app.data.repositories.AppRepository;


public class UsersListViewModel extends AndroidViewModel {

    private AppRepository appRepository;

    private List<User> users;

    private MutableLiveData<Resource<List<User>>> usersListData;
    private MutableLiveData<Resource<List<UserLatestMessage>>> usersLatestMsgData;

    public UsersListViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
        usersListData = new MutableLiveData<>();
        usersLatestMsgData = new MutableLiveData<>();
    }


    public void fetchAllUsers(LifecycleOwner owner) {
        usersListData.setValue(Resource.loading(null));
        appRepository.fetchAllUsers();
        appRepository.getUsersListData().observe(owner, listResource -> {
            switch (listResource.status) {
                case SUCCESS:
                    if (listResource.data != null) {
                        appRepository.getUsersListData().removeObservers(owner);
                        if (listResource.data.isEmpty()) {
                            setUsers(owner);
                        } else {
                            usersListData.setValue(Resource.success(listResource.data));
                            MethodUtility.toast(getApplication().getApplicationContext(), "Retrieved");
                        }

                    }
                    break;
                case ERROR:
                    if (listResource.message != null) {
                        appRepository.getUsersListData().removeObservers(owner);
                        MethodUtility.toast(getApplication().getApplicationContext(), listResource.message);
                    }
                    break;
            }
        });

    }

    public void fetchUsersWithLatestMsg(LifecycleOwner owner) {
        usersLatestMsgData.setValue(Resource.loading(null));
        appRepository.fetchUsersWithLatestMessage();
        appRepository.getUserLatestMessageData().observe(owner, listResource -> {
            switch (listResource.status) {
                case SUCCESS:
                    if (listResource.data != null) {
                        appRepository.getUsersListData().removeObservers(owner);
//                        if (listResource.data.isEmpty()) {
//                            MethodUtility.toast(getApplication().getApplicationContext(), "Empty");
////                            setUsers(owner);
//                        } else {
                        usersLatestMsgData.setValue(Resource.success(listResource.data));
                        MethodUtility.toast(getApplication().getApplicationContext(), "Retrieved");
//                        }

                    }
                    break;
                case ERROR:
                    if (listResource.message != null) {
                        usersLatestMsgData.setValue(Resource.error(listResource.message, null));
                        appRepository.getUsersListData().removeObservers(owner);
                        MethodUtility.toast(getApplication().getApplicationContext(), listResource.message);
                    }
                    break;
            }
        });

    }

    public LiveData<Resource<List<UserLatestMessage>>> getUsersLatestMsgData() {
        return usersLatestMsgData;
    }

    public void saveAllUsers(LifecycleOwner owner) {
        appRepository.addUsers(users.toArray(new User[0]));
        fetchAllUsers(owner);
//        if (insertedUsers != null && !insertedUsers.isEmpty()) {
//            usersListData.setValue(Resource.success(insertedUsers));
//        } else {
//            usersListData.setValue(Resource.error("Failed to fetch Users", null));
//        }
    }


    private void setUsers(LifecycleOwner owner) {
        users = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Faker faker = new Faker();
            User user = new User();
            user.setName(faker.name().fullName());
            user.setImage(faker.avatar().image());
//            appRepository.addUser(user);
            Log.i("MESSAGE", "setUsers: " + user.getName() + " Saved " + i);
            users.add(user);
        }
//        fetchAllUsers(owner);
        saveAllUsers(owner);
    }



    public LiveData<Resource<List<User>>> getUsersList() {
        return usersListData;
    }

}
