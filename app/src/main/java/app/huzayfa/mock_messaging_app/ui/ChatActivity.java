package app.huzayfa.mock_messaging_app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import app.huzayfa.mock_messaging_app.R;
import app.huzayfa.mock_messaging_app.ui.adapters.ChatAdapter;
import butterknife.*;

import android.os.Bundle;

public class ChatActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @BindView(R.id.chat_rv)
    RecyclerView chatRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        unbinder = ButterKnife.bind(this);
        ChatAdapter chatAdapter = new ChatAdapter(this);
        chatAdapter.notifyDataSetChanged();
        chatRv.setAdapter(chatAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}