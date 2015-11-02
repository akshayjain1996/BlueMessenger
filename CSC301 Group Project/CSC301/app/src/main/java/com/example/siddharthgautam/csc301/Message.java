package com.example.siddharthgautam.csc301;

import android.bluetooth.BluetoothDevice;

import java.util.UUID;

/**
 * Created by Alex on 10/31/2015.
 */
public class Message {

    private BluetoothDevice device;
    private String message;

    public Message(BluetoothDevice device, String message){
        this.device = device;
        this.message = message;
    }

    public String getDisplayName(){
        return device.getName();
    }

    public String getMessage(){
        return message;
    }

    @Override
    public String toString(){
        if (device!=null) {
            return getDisplayName() + ": " + message;
        }
        return "You: " + message;
    }


    public BluetoothDevice getMessageOrigin() {
        return device;
    }

    public void setMessageOrigin(BluetoothDevice messageOrigin) {
        this.device = messageOrigin;
    }

    public BluetoothDevice getDevice() {
        return device;
    }
}
