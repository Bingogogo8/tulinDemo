package com.example.tulin;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ChatMessageAdapter extends BaseAdapter {

    private List<ChatMessage> list;

    public ChatMessageAdapter(List<ChatMessage> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.isEmpty() ? 0 : list.size();
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
    public int getItemViewType(int position) {
        ChatMessage chatMessage = list.get(position);
        // 如果是机器人：0，我：1
        if (chatMessage.getType() == ChatMessage.Type.INCOUNT) {
            return 0;
        }
        return 1;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessage = list.get(position);
        if (convertView == null) {
            ViewHolder viewHolder = null;
            // 通过ItemType加载不同的布局
            if (getItemViewType(position) == 0) {

                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.left, null);
                viewHolder = new ViewHolder();
                viewHolder.chatTime = (TextView) convertView
                        .findViewById(R.id.chat_left_time);
                viewHolder.chatMessage = (TextView) convertView
                        .findViewById(R.id.chat_left_message);
            } else {

                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.right, null);
                viewHolder = new ViewHolder();
                viewHolder.chatTime = (TextView) convertView
                        .findViewById(R.id.chat_right_time);
                viewHolder.chatMessage = (TextView) convertView
                        .findViewById(R.id.chat_right_message);
            }
            convertView.setTag(viewHolder);
        } else {
            ViewHolder viewHolder = null;

            if (chatMessage.getType() == ChatMessage.Type.INCOUNT) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.left, null);
                viewHolder = new ViewHolder();
                viewHolder.chatTime = (TextView) convertView
                        .findViewById(R.id.chat_left_time);
                viewHolder.chatMessage = (TextView) convertView
                        .findViewById(R.id.chat_left_message);
            }
            if (chatMessage.getType() == ChatMessage.Type.OUTCOUNT) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.right, null);
                viewHolder = new ViewHolder();
                viewHolder.chatTime = (TextView) convertView
                        .findViewById(R.id.chat_right_time);
                viewHolder.chatMessage = (TextView) convertView
                        .findViewById(R.id.chat_right_message);
            }
            convertView.setTag(viewHolder);

        }
        ;
        //else { chatMessage.setType(ChatMessage.Type.OUTCOUNT); }
        // 设置数据
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.chatTime.setText(ChatDate.dateToString(chatMessage.getData()));
        viewHolder.chatMessage.setText(chatMessage.getMessage());

        return convertView;
    }


    private class ViewHolder {
        private TextView chatTime, chatMessage;
    }
}