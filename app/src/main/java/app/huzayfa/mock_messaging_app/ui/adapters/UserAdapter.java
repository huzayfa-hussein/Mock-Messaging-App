package app.huzayfa.mock_messaging_app.ui.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import app.huzayfa.mock_messaging_app.R;
import app.huzayfa.mock_messaging_app.data.helper.Constants;
import app.huzayfa.mock_messaging_app.data.helper.MethodUtility;
import app.huzayfa.mock_messaging_app.data.models.User;
import app.huzayfa.mock_messaging_app.ui.ChatActivity;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private FragmentActivity context;
    private List<User> users, filteredList;

    public UserAdapter(FragmentActivity context, List<User> users) {
        this.context = context;
        this.users = users;
        this.filteredList = new ArrayList<>();


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = filteredList == null || filteredList.isEmpty() ? users.get(position) : filteredList.get(position);
        holder.userNameTv.setText(user.getName());
        Picasso.get().load(Uri.parse(user.getImage())).into(holder.userProfileIv);

        holder.last_msg_date_tv.setText(user.getSent_at() == null ? "" : MethodUtility.dateToString(users.get(position).getSent_at()));
        holder.latestMsgTv.setText(user.getMsg() != null ? users.get(position).getMsg() : "");

    }

    @Override
    public int getItemCount() {
        return filteredList == null || filteredList.isEmpty() ? users.size() : filteredList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * This method is used to filter
     * {@link #users} with the @param query
     * and returned the filteredList
     */

    public void filterUsers(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(users);
        } else {
            for (User user : users) {
                if (user.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(user);
                }
            }
            notifyDataSetChanged();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView userNameTv;
        private ImageView userProfileIv;
        private TextView last_msg_date_tv;
        private TextView latestMsgTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTv = itemView.findViewById(R.id.user_name_tv);
            userProfileIv = itemView.findViewById(R.id.user_iv);
            last_msg_date_tv = itemView.findViewById(R.id.last_msg_date_tv);
            latestMsgTv = itemView.findViewById(R.id.latest_msg_tv);
            //redirect to chat activity
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(Constants.USER, users.get(getAdapterPosition()));
                context.startActivityForResult(intent, Constants.REQUEST_CODE);
            });


        }
    }


}
