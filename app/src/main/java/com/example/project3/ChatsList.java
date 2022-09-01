package com.example.project3;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatsList extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] names;
    private final String[] mMessages;


//    private final Integer[] mConvIds;

    public ChatsList(Activity context, String[] s, String[] messages)
    {
        super(context, R.layout.chats_list_view_item, s);
        this.context = context;
        names = s;
        mMessages=messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View linear_layout;
        //convertView is a linearlayout that is offscreen that we can reuse
        if(convertView==null) {
            LayoutInflater inflater = context.getLayoutInflater();
            linear_layout = inflater.inflate(R.layout.chats_list_view_item, null, true);
        }
        else
        {
            linear_layout=convertView;
        }
        TextView name = (TextView) linear_layout.findViewById(R.id.name_text_view);
        TextView last = (TextView) linear_layout.findViewById(R.id.last_message_text_view);

        ImageView image = (ImageView) linear_layout.findViewById(R.id.img);
        name.setText(names[position]);
        last.setText(mMessages[position]);
        image.setImageResource(R.drawable.ic_action_name);
//        conv_id.setText(mConvIds[position]);
        return linear_layout;
    }
}