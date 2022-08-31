package com.example.project3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project3.Model.Message;
import com.example.project3.Model.Model;

import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private List<Message> mMessageList;

    public ConversationAdapter(Context context, List<Message> messageList) {
        mContext = context;
        mMessageList = messageList;
    }
    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
    @Override
    public int getItemViewType(int position) {
        Message message = (Message) mMessageList.get(position);

        if (message.getSenderId() == Model.getInstance().getUser().id) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }


    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_sent_item, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_received_item, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = (Message) mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }


    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
//            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }

        void bind(Message message) {
            messageText.setText(message.getMessageBody());

            // Format the stored timestamp into a readable String using method.
//            timeText.setText(Utils.formatDateTime(message.getCreatedAt()));
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
//        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_other);
//            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_gchat_user_other);
//            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }

        void bind(Message message) {
            messageText.setText(message.getMessageBody());

            // Format the stored timestamp into a readable String using method.
//            timeText.setText(Utils.formatDateTime(message.getCreatedAt()));

//            nameText.setText(message.getSenderId().getNickname());

            // Insert the profile image from the URL into the ImageView.
//            Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }
}