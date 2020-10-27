package app.huzayfa.mock_messaging_app.ui;

import android.os.Bundle;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import app.huzayfa.mock_messaging_app.R;
import app.huzayfa.mock_messaging_app.data.models.User;
import app.huzayfa.mock_messaging_app.ui.adapters.UserAdapter;
import app.huzayfa.mock_messaging_app.viewModels.ChatViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UsersListActivity extends AppCompatActivity {

    private Unbinder unbinder;

    private ChatViewModel chatViewModel;

    @BindView(R.id.users_rv)
    RecyclerView usersRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        chatViewModel = viewModelProvider.get(ChatViewModel.class);

        loadUsers();

    }

    private void loadUsers() {
        chatViewModel.fetchAllUsers(this);
        chatViewModel.getUsersList().observe(this, arrayListResource -> {
            switch (arrayListResource.status) {
                case ERROR:
                    chatViewModel.getUsersList().removeObservers(this);
                    break;
                case SUCCESS:
                    if (arrayListResource.data != null) {
                        chatViewModel.getUsersList().removeObservers(this);
                        UserAdapter userAdapter = new UserAdapter(this, (ArrayList<User>) arrayListResource.data);
                        userAdapter.notifyDataSetChanged();
                        usersRv.setAdapter(userAdapter);
                    }
                    break;
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}