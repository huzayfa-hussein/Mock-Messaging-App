package app.huzayfa.mock_messaging_app.ui.adapters;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import app.huzayfa.mock_messaging_app.R;
import app.huzayfa.mock_messaging_app.data.helper.MethodUtility;
import app.huzayfa.mock_messaging_app.data.models.Message;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private FragmentActivity context;
    private List<Message> messages;


    public ChatAdapter(FragmentActivity context, List<Message> messages) {
        this.context = context;
        this.messages = messages;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType == 0 ? R.layout.chat_sender_item :
                R.layout.chat_receiver_item, parent, false);
        return viewType == 0 ? new SenderViewHolder(view) : new ReceivedViewHolder(view);
    }


    @Override
    public int getItemViewType(int position) {
        //0 means its the sender, 1 the received msg
        return messages.get(position).getSentMessage() != null ? 0 : 1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (holder.getItemViewType() == 0) {
            ((SenderViewHolder) holder).bindUi(message);
        } else
            ((ReceivedViewHolder) holder).bindUi(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public class SenderViewHolder extends RecyclerView.ViewHolder {

        private TextView sender_msg_tv;
        private TextView sender_msg_date_tv;


        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            sender_msg_tv = itemView.findViewById(R.id.sender_msg_tv);
            sender_msg_date_tv = itemView.findViewById(R.id.sender_msg_date_tv);
        }

        public void bindUi(Message message) {
            sender_msg_tv.setText(message.getSentMessage());
//            String stDate = (String) DateUtils.getRelativeTimeSpanString(message.getSendsAt().getTime(), Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS);
            sender_msg_date_tv.setText(MethodUtility.dateToString(message.getSendsAt()));
        }

    }

    private class ReceivedViewHolder extends RecyclerView.ViewHolder {

        private TextView receiver_msg_tv;
        private TextView receiver_date_tv;

        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);

            receiver_msg_tv = itemView.findViewById(R.id.receiver_msg_tv);
            receiver_date_tv = itemView.findViewById(R.id.receiver_msg_date_tv);

        }

        public void bindUi(Message message) {
            receiver_msg_tv.setText(message.getReceivedMessage());
            receiver_date_tv.setText(MethodUtility.dateToString(message.getSendsAt()));

        }
    }


}
