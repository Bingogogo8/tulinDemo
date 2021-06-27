package com.example.tulin;


import java.util.Date;

/**
  聊天消息的实体类
*/
public class ChatMessage {

    private String message;// 消息
    private Type type;// 类型：0.发送者 1.接受者
    private Date data;// 时间

    public ChatMessage() {
    }

    public ChatMessage(String message, Type type, Date data) {
        super();
        this.message = message;
        this.type = type;
        this.data = data;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public enum Type {
        // 类型
        Robot, Me
    }
}


