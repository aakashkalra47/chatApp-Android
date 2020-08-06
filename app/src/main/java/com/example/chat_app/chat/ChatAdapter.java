package com.example.chat_app.chat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chat_app.R;
import com.example.chat_app.chat.Chat;

import java.util.List;
public class ChatAdapter extends BaseAdapter {

    private Context context;
    private List<Chat> list;

    public ChatAdapter(Activity context, List<Chat> list) {
        super();
        this.context=context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.chat_view_layout, parent, false);
        TextView textView = (TextView) convertView.findViewById(R.id.chat_text_view);
        textView.setText(((Chat) getItem(position)).friend);
        return convertView;
    }
}