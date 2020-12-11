package com.example.tulin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<ChatMessage> list;
    private ListView chatListview;
    private EditText chatInput;
    private Button chatSend;
    private ChatMessageAdapter chatAdapter;
    private ChatMessage chatMessage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //去除标题
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }


    private void initView() {
        chatListview = (ListView) findViewById(R.id.chat_listview);
        chatInput = (EditText) findViewById(R.id.chat_input_message);
        chatSend = (Button) findViewById(R.id.chat_send);
        chatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.chat_send:
                        chat();
                        break;
                }
            }
        });
    }

    // 初始化数据
    private void initData() {
        list = new ArrayList<ChatMessage>();
        list.add(new ChatMessage("你好。", ChatMessage.Type.INCOUNT, new Date()));
        chatAdapter = new ChatMessageAdapter(list);
        chatListview.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();  //notifyDataSetChanged用来通知ListView，告诉它Adapter的数据发生了变化，需要更新ListView的显示
    }


    // 发送消息聊天
    private void chat() {
        // 判断是否输入内容
        final String send_message = chatInput.getText().toString().trim();  //tirm : 去掉字符串两端的多余的空格
        if (TextUtils.isEmpty(send_message)) {
            Toast.makeText(MainActivity.this, "对不起，您还未发送任何消息",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // 自己输入信息
        ChatMessage sendChatMessage = new ChatMessage();
        sendChatMessage.setMessage(send_message);
        sendChatMessage.setData(new Date());
        sendChatMessage.setType(ChatMessage.Type.OUTCOUNT);
        list.add(sendChatMessage);
        chatAdapter.notifyDataSetChanged();
        chatInput.setText("");

        // 发送消息，返回数据
        new Thread() {
            public void run() {
                ChatMessage chat = HTTPUtils.sendMessage(send_message);
                Message message = new Message();
                //Message是封装了需要传递的数据交由Handler 处理的对象
                //用于存放传递的数据；
                //是主线程和子线程传递数据的载体
                message.what = 10086;
                message.obj = chat;
                handler.sendMessage(message);
            }
        }.start();
    }

    //  handler
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            if (msg.what == 10086) {
                if (msg.obj != null) {
                    chatMessage = (ChatMessage) msg.obj;
                }
                // 添加数据到list中，更新数据
                list.add(chatMessage);
                chatAdapter.notifyDataSetChanged();
            }
        }

        ;
    };


}