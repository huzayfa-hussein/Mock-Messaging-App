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


public class ChatViewModel extends AndroidViewModel {

    private AppRepository appRepository;

    private MutableLiveData<Resource<List<Message>>> userMessagesData;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
        userMessagesData = new MutableLiveData<>();

    }

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

    private void setUserMessages(Long userId, List<UserAndMessage> data) {
        List<Message> messageList = new ArrayList<>();
        for (UserAndMessage userAndMessage : data) {
            if (userAndMessage.getUser().getUId().equals(userId)) {
                messageList.addAll(userAndMessage.getMessage());
                break;
            }
        }
        userMessagesData.setValue(Resource.success(messageList));

    }

    public LiveData<Resource<List<Message>>> getUserMessagesData() {
        return userMessagesData;
    }

    public void saveNewMessage(Message message) {
        appRepository.saveMessage(message);
    }

    public void updateUser(User user) {
        appRepository.updateUser(user);
    }
}
