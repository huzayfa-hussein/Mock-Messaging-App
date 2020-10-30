package app.huzayfa.mock_messaging_app.viewModels;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import app.huzayfa.mock_messaging_app.data.helper.MethodUtility;
import app.huzayfa.mock_messaging_app.data.models.Message;
import app.huzayfa.mock_messaging_app.data.models.Resource;
import app.huzayfa.mock_messaging_app.data.models.User;
import app.huzayfa.mock_messaging_app.data.models.UserAndMessage;
import app.huzayfa.mock_messaging_app.data.repositories.AppRepository;

/**
 * This viewModel class is responsible for
 * communicate with {@link AppRepository} to
 * get the data required for {@link app.huzayfa.mock_messaging_app.ui.ChatActivity}
 * activity and prepare ui logic for the activity
 *
 * @author Huzayfa
 */

public class ChatViewModel extends AndroidViewModel {

    private AppRepository appRepository;

    private MutableLiveData<Resource<List<Message>>> userMessagesData;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
        userMessagesData = new MutableLiveData<>();

    }

    /**
     * This method is listening to  the liveData object returned
     * from {@link AppRepository} then filter messages based on the
     *
     * @param userId with {@link #setUserMessages(Long, List)} and update
     *               the value {@link #userMessagesData}
     * @param owner  is the activity lifecycle owner and used in observing
     *               liveData
     */

    public void fetchUserMessages(Long userId, LifecycleOwner owner) {
        userMessagesData.setValue(Resource.loading(null));
        appRepository.fetchAllUserMessages();
        appRepository.getUserMessages().observe(owner, ListResource -> {
            switch (ListResource.status) {
                case LOADING:
                    MethodUtility.toast(getApplication().getApplicationContext(), "Loading");
                    break;
                case ERROR:
                    if (ListResource.message != null) {
                        appRepository.getUserMessages().removeObservers(owner);
                        userMessagesData.setValue(Resource.error(ListResource.message, null));
                        MethodUtility.toast(getApplication().getApplicationContext(), ListResource.message);

                    }
                    break;
                case SUCCESS:
                    if (ListResource.data != null) {
                        appRepository.getUserMessages().removeObservers(owner);
                        setUserMessages(userId, ListResource.data);
                        MethodUtility.toast(getApplication().getApplicationContext(), "Success");
                    }
                    break;
            }
        });
    }

    /**
     * This method is used to filter @param userAndMessages
     * based on @param userId
     */
    private void setUserMessages(Long userId, List<UserAndMessage> userAndMessages) {
        List<Message> messageList = new ArrayList<>();
        for (UserAndMessage userAndMessage : userAndMessages) {
            if (userAndMessage.getUser().getUId().equals(userId)) {
                messageList.addAll(userAndMessage.getMessage());
                break;
            }
        }
        userMessagesData.setValue(Resource.success(messageList));

    }

    /**
     * @return a liveData object to observe and listen to any
     * change in data later on in {@link app.huzayfa.mock_messaging_app.ui.ChatActivity}
     */
    public LiveData<Resource<List<Message>>> getUserMessagesData() {
        return userMessagesData;
    }

    /**
     * This method will be invoked when the {@link User}
     * send a new {@link Message} with @param message
     */

    public void saveNewMessage(Message message) {
        appRepository.saveMessage(message);
    }

    /**
     * This method will be invoked when we try
     * to update{@link User} record with the
     * latest message
     *
     * @param user is the record that will be updated
     */

    public void updateUser(User user) {
        appRepository.updateUser(user);
    }
}
