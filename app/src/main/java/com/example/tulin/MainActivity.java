package com.example.tulin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
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


    private ListView chatListview;
    private EditText chatInput;
    private Button chatSend;

    private ChatMessageAdapter chatAdapter;
    private ChatMessage chatMessage = null;
    private List<ChatMessage> list;

    //用于申请权限
    private static final int REQUEST_PERMISSIONS_CODE = 1;
    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.INTERNET};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                chat();
            }
        });
    }

    // 初始化数据
    private void initData() {
        list = new ArrayList<ChatMessage>();
        list.add(new ChatMessage("你好。", ChatMessage.Type.Robot, new Date()));
        chatAdapter = new ChatMessageAdapter(list);
        chatListview.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
    }


    // 发送消息聊天
    private void chat() {
        // 判断是否输入内容
        final String send_message = chatInput.getText().toString().trim();  //tirm : 去掉字符串两端的多余的空格
        if (TextUtils.isEmpty(send_message)) {
            Toast.makeText(MainActivity.this, "对不起，您还未发送任何消息", Toast.LENGTH_SHORT).show();
            return;
        }

        // 自己输入的信息
        ChatMessage sendChatMessage = new ChatMessage();
        sendChatMessage.setMessage(send_message);
        sendChatMessage.setData(new Date());
        sendChatMessage.setType(ChatMessage.Type.Me);
        list.add(sendChatMessage);
        chatAdapter.notifyDataSetChanged();    //notifyDataSetChanged用来通知ListView，告诉它Adapter的数据发生了变化，需要更新ListView的显示
        chatInput.setText("");                //发送出去信息后清空输入框

        // 发送消息，返回数据
        new Thread() {
            public void run() {
                ChatMessage chatMessage = HTTPUtils.sendMessage(send_message);
                Message message = new Message();
                //Message是封装了需要传递的数据交由Handler处理的对象
                //用于存放传递的数据；
                //是主线程和子线程传递数据的载体
                message.what = 10086;
                message.obj = chatMessage;
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


    };

    @Override
    protected void onResume() {
        super.onResume();
        // 动态权限检查
        if (!isRequiredPermissionsGranted() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //以下是AppCompat的一个方法，输入需要申请的权限的字符数组，会自动调用函数弹窗询问用户是否允许权限使用；
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_PERMISSIONS_CODE);
        }
    }

    /**
     * 判断我们需要的权限是否被授予，只要有一个没有授权，我们都会返回 false。
     *
     * @return true 权限都被授权
     */
    private boolean isRequiredPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
}
