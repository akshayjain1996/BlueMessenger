package com.example.siddharthgautam.csc301;

import android.bluetooth.BluetoothDevice;

import java.util.UUID;

/**
 * Created by Alex on 10/31/2015.
 */
public class Message {

    private BluetoothDevice device;
    private String message;
    private String originMac;
    public Message(BluetoothDevice device, String message, String origin){
        this.device = device;
        this.message = message;
        this.originMac = origin;
    }

    public String getDisplayName(){
        return device.getName();
    }

    public String getMessage(){
        return message;
    }

    @Override
    public String toString(){
        return getDisplayName() + ": " + message;
    }


    public String getMessageOriginMac() {
        return originMac;
    }

    public void setMessageOrigin(BluetoothDevice messageOrigin) {
        this.device = messageOrigin;
    }

    public BluetoothDevice getDevice() {
        return device;
    }
}
