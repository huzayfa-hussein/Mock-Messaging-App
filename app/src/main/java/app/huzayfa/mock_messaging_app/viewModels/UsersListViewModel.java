package app.huzayfa.mock_messaging_app.viewModels;

import android.app.Application;

import com.github.javafaker.Faker;

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
                            loadNewUsers(owner);
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

    public LiveData<Resource<List<User>>> getUsersList() {
        return usersListData;
    }


    public void loadNewUsers(LifecycleOwner owner) {
        for (int i = 0; i < 20; i++) {
            Faker faker = new Faker();
            appRepository.addUser(new User(faker.name().fullName(), faker.avatar().image()));

        }
        fetchAllUsers(owner);

    }

}
