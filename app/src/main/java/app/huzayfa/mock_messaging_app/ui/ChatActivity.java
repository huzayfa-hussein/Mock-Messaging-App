package app.huzayfa.mock_messaging_app.ui;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import app.huzayfa.mock_messaging_app.R;
import app.huzayfa.mock_messaging_app.data.helper.Constants;
import app.huzayfa.mock_messaging_app.data.models.Message;
import app.huzayfa.mock_messaging_app.data.models.Status;
import app.huzayfa.mock_messaging_app.data.models.User;
import app.huzayfa.mock_messaging_app.ui.adapters.ChatAdapter;
import app.huzayfa.mock_messaging_app.viewModels.ChatViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class ChatActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @BindView(R.id.chat_rv)
    RecyclerView chatRv;

    @BindView(R.id.chat_toolbar)
    Toolbar chatToolbar;

    @BindView(R.id.user_name_tv)
    TextView userNameTv;

    @BindView(R.id.user_iv)
    ImageView userImage;

    private User user;

    @BindView(R.id.message_et)
    EditText messageEt;

    private String message = "";
    private ChatViewModel chatViewModel;
    private List<Message> userMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        unbinder = ButterKnife.bind(this);
        setSupportActionBar(chatToolbar);
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        chatViewModel = viewModelProvider.get(ChatViewModel.class);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        user = getIntent().getParcelableExtra(Constants.USER);

        userNameTv.setText(user.getName());
        Picasso.get().load(Uri.parse(user.getImage())).into(userImage);

        loadUserMessages();


    }

    private void loadUserMessages() {
        userMessages = new ArrayList<>();
        chatViewModel.fetchUserMessages(user.getUId(), this);
        chatViewModel.getUserMessagesData().observe(this, arrayListResource -> {
            if (arrayListResource.status == Status.SUCCESS) {
                chatViewModel.getUserMessagesData().removeObservers(this);
                userMessages = arrayListResource.data;
                setChatAdapter();
            }
        });
    }

    private void setChatAdapter() {
        ChatAdapter chatAdapter = new ChatAdapter(this, userMessages);
        chatAdapter.notifyDataSetChanged();
        chatRv.setAdapter(chatAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }

    @OnTextChanged(R.id.message_et)
    public void setMessage(CharSequence charSequence) {
        message = messageEt.getText().toString();
    }

    @OnClick(R.id.send_msg_btn)
    public void sendMessage(View view) {
        if (message.equals("")) {
            return;
        }
        Message senderMsg = new Message();
        senderMsg.setSentMessage(message);
        senderMsg.setUserId(user.getUId());
        senderMsg.setSendsAt(Calendar.getInstance().getTime());
        String msg = message;
        messageEt.setText("");
        saveMessage(senderMsg);
        replyMessage(msg);

    }

    private void replyMessage(String msg) {
        long duration = (long) (Math.random() * 500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Message rcMsg = new Message();
                rcMsg.setReceivedMessage(msg);
                rcMsg.setUserId(user.getUId());
                rcMsg.setSendsAt(Calendar.getInstance().getTime());
                saveMessage(rcMsg);
                user.setMsg(msg);
                user.setSent_at(rcMsg.getSendsAt());
                chatViewModel.updateUser(user);

            }
        }, duration);
    }

    private void saveMessage(Message senderMsg) {
        chatViewModel.saveNewMessage(senderMsg);
        userMessages.add(senderMsg);
        setChatAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}