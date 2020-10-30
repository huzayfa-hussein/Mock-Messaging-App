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
import app.huzayfa.mock_messaging_app.data.repositories.AppRepository;

/**
 * This viewModel class is responsible for
 * communicate with {@link AppRepository} to
 * get the data required for {@link app.huzayfa.mock_messaging_app.ui.UsersListActivity}
 * activity and prepare ui logic for the activity
 *
 * @author Huzayfa
 */

public class UsersListViewModel extends AndroidViewModel {

    private AppRepository appRepository;

    private MutableLiveData<Resource<List<User>>> usersListData;

    public UsersListViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
        usersListData = new MutableLiveData<>();
    }

    /**
     * This method is listening to the liveData object
     * returned from {@link AppRepository}, if there is no
     * returned data with the observer then {@link #loadNewUsers(LifecycleOwner)}
     * will create and save random users to {@link app.huzayfa.mock_messaging_app.data.dao.DaoDatabase}
     * otherwise the data will be set in {@link #usersListData}
     *
     * @param owner The activity lifecycle owner {@link app.huzayfa.mock_messaging_app.ui.UsersListActivity}
     */

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

    /**
     * @return a liveData object to observe and listen to any
     * change in data later on in {@link app.huzayfa.mock_messaging_app.ui.UsersListActivity}
     */
    public LiveData<Resource<List<User>>> getUsersList() {
        return usersListData;
    }

    /**
     * This method is responsible to generate
     * 20 random faker users using {@link Faker} library :
     * https://github.com/DiUS/java-faker#java-faker
     * and save each generated user, then invoke
     * {@link #fetchAllUsers(LifecycleOwner)} to fetch and observe the
     * new users for the activity {@link app.huzayfa.mock_messaging_app.ui.UsersListActivity}
     *
     * @param owner The activity lifecycle owner {@link app.huzayfa.mock_messaging_app.ui.UsersListActivity}
     */

    public void loadNewUsers(LifecycleOwner owner) {
        for (int i = 0; i < 20; i++) {
            Faker faker = new Faker();
            appRepository.addUser(new User(faker.name().fullName(), faker.avatar().image()));

        }
        fetchAllUsers(owner);

    }

}
