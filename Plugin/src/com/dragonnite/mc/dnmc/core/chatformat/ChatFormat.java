package com.dragonnite.mc.dnmc.core.chatformat;


import javax.annotation.Nonnull;

public class ChatFormat {
    private String chatformat;
    private int priority;

    ChatFormat(@Nonnull String chatformat, int priority) {
        this.chatformat = chatformat;
        this.priority = priority;
    }

    public String getChatformat() {
        return chatformat;
    }

    void setChatformat(String chatformat) {
        this.chatformat = chatformat;
    }

    public int getPriority() {
        return priority;
    }

    void setPriority(int priority) {
        this.priority = priority;
    }
}

