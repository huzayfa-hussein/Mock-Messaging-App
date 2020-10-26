package app.huzayfa.mock_messaging_app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import app.huzayfa.mock_messaging_app.R;
import app.huzayfa.mock_messaging_app.ui.adapters.UserAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.os.Bundle;

public class UsersListActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @BindView(R.id.users_rv)
    RecyclerView usersRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        UserAdapter userAdapter = new UserAdapter(this);
        userAdapter.notifyDataSetChanged();
        usersRv.setAdapter(userAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}