package app.huzayfa.mock_messaging_app.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.huzayfa.mock_messaging_app.R;
import app.huzayfa.mock_messaging_app.data.helper.Constants;
import app.huzayfa.mock_messaging_app.data.models.User;
import app.huzayfa.mock_messaging_app.ui.adapters.UserAdapter;
import app.huzayfa.mock_messaging_app.viewModels.UsersListViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UsersListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Unbinder unbinder;

    private UsersListViewModel usersListViewModel;

    @BindView(R.id.users_rv)
    RecyclerView usersRv;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private boolean isLoading = false;
    private List<User> userList;
    private UserAdapter userAdapter;
    private boolean shouldUpdateAdapter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        userList = new ArrayList<>();
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        usersListViewModel = viewModelProvider.get(UsersListViewModel.class);
        userAdapter = new UserAdapter(this, userList);
        usersRv.setAdapter(userAdapter);
        loadUsers();
        initRvScrollLazyLoading();

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
                        if (arrayListResource.data.size() == Constants.MAX_USERS_COUNT) {
                            usersListViewModel.getUsersList().removeObservers(this);
                            userList.addAll(arrayListResource.data);
                        } else {
                            for (User user : arrayListResource.data) {
                                if (!userList.contains(user)) {
                                    userList.add(user);
                                }
                            }

                        }
                        if (shouldUpdateAdapter) {
                            shouldUpdateAdapter = false;
                            userAdapter = new UserAdapter(this, userList);
                            userAdapter.notifyDataSetChanged();
                            usersRv.setAdapter(userAdapter);
                        } else userAdapter.notifyDataSetChanged();

                        isLoading = false;
                        progressBar.setVisibility(View.GONE);

                    }
                    break;
                case LOADING:
                    if (!isLoading)
                        progressBar.setVisibility(View.VISIBLE);
                    break;
            }
        });
    }

    private void initRvScrollLazyLoading() {
        usersRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == userList.size() - 1 && userList.size() < Constants.MAX_USERS_COUNT) {
                        progressBar.setVisibility(View.VISIBLE);
                        usersListViewModel.loadNewUsers(UsersListActivity.this);
                        isLoading = true;
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE) {
            userList = new ArrayList<>();
            shouldUpdateAdapter = true;
            loadUsers();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        MenuItem modeItem = menu.findItem(R.id.mode);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            setDarkModeIcon(modeItem);
        } else modeItem.setVisible(false);
        return true;
    }

    private void setDarkModeIcon(MenuItem modeItem) {
        int nightModeFlags =
                getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                modeItem.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_light, getResources().newTheme()));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                modeItem.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_night, getResources().newTheme()));
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                modeItem.setVisible(false);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.mode) {
            if (item.getIcon() == ResourcesCompat.getDrawable(getResources(), R.drawable.ic_night, getResources().newTheme())) {
                item.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_light, getResources().newTheme()));
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                item.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_night, getResources().newTheme()));
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        userAdapter.filterUsers(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        userAdapter.filterUsers(newText);
        return false;
    }
}