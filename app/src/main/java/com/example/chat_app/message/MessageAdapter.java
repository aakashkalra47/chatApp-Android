package com.example.chat_app.message;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chat_app.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends BaseAdapter
{

    public List<Message> messageList;
    public Context context;
    public String userId;
    public ArrayList<DataSetObserver> observers = new ArrayList<DataSetObserver>();
    public MessageAdapter(Context context,List<Message> messageList,String userId) {
        super();
        this.context=context;
        this.messageList=messageList;
        this.userId=userId;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return  messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(Message message)
    {
        messageList.add(message);
    }

    public void registerDataSetObserver(DataSetObserver observer) {

      observers.add(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        observers.remove(observer);
    }

    public void notifyDataSetChanged(){
        //Log.i("notify","called");

        for (DataSetObserver observer: observers) {
            observer.onChanged();
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(messageList.get(position).getSenderId().toString().equals(userId.toString())) {

            LayoutInflater layoutInflater=((Activity) context).getLayoutInflater();
            convertView=layoutInflater.inflate(R.layout.send_message,parent,false);
            TextView textView=(TextView)convertView.findViewById(R.id.sender_message);
            textView.setText(((Message)getItem(position)).getContent());
        }
        else{
            LayoutInflater layoutInflater=((Activity) context).getLayoutInflater();
            convertView=layoutInflater.inflate(R.layout.reieve_message, parent,false);
            TextView textView=(TextView)convertView.findViewById(R.id.reciever_message);
            textView.setText(((Message)getItem(position)).getContent());
        }
        return convertView;
    }
}
