package app.huzayfa.mock_messaging_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import app.huzayfa.mock_messaging_app.R;
import app.huzayfa.mock_messaging_app.data.helper.Constants;
import app.huzayfa.mock_messaging_app.ui.adapters.UserAdapter;
import app.huzayfa.mock_messaging_app.viewModels.UsersListViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UsersListActivity extends AppCompatActivity {

    private Unbinder unbinder;

    private UsersListViewModel usersListViewModel;

    @BindView(R.id.users_rv)
    RecyclerView usersRv;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        usersListViewModel = viewModelProvider.get(UsersListViewModel.class);

        loadUsers();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE) {
            loadUsers();
        }

    }


    private void loadUsers() {
        usersListViewModel.fetchAllUsers(this);
        usersListViewModel.getUsersList().observe(this, arrayListResource -> {
            switch (arrayListResource.status) {
                case ERROR:
                    usersListViewModel.getUsersList().removeObservers(this);
                    progressBar.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    if (arrayListResource.data != null) {
                        usersListViewModel.getUsersList().removeObservers(this);
                        progressBar.setVisibility(View.GONE);
                        UserAdapter userAdapter = new UserAdapter(this, arrayListResource.data);
                        userAdapter.notifyDataSetChanged();
                        usersRv.setAdapter(userAdapter);
                    }
                    break;
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}