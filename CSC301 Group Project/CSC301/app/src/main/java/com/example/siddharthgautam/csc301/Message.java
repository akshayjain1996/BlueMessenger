package com.example.siddharthgautam.csc301;

import java.util.UUID;

/**
 * Created by Alex on 10/31/2015.
 */
public class Message {
    private String displayName;
    private String message;
    private UUID uuid;
    public Message(String displayName, String message, UUID uuid){
        this.displayName = displayName;
        this.message = message;
        this.uuid = uuid;
    }

    public String getDisplayName(){
        return displayName;
    }

    public String getMessage(){
        return message;
    }

    public UUID getUuid(){
        return uuid;
    }

    @Override
    public String toString(){
        return displayName + ": " + message;
    }
}
