package com.mokylin.cater_server.socket.message;

public class MessageEntity {
    private String type;
    private Object content;

    /**
     * @param type
     * @param content
     */
    public MessageEntity(String type, Object content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
