package ca.toronto.csc301.chat;

import android.bluetooth.BluetoothDevice;

import com.example.siddharthgautam.csc301.Message;

/**
 * Created by akshay on 31/10/15.
 */
public class BluetoothController {

    ConnectionsList connectionsList;

    BluetoothController(){
        connectionsList = new ConnectionsList();

    }

    public void establishConnection(BluetoothDevice device){
        RequestConnectionThread connectionThread = new RequestConnectionThread(device);
        connectionThread.start();
    }

    public void sendMessage(Message message){


    }

    public void handelRecievedMessage(){

    }

}
