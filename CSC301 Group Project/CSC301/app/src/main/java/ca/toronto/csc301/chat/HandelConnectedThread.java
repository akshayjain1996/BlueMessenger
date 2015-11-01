package ca.toronto.csc301.chat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by akshay on 31/10/15.
 */
public class HandelConnectedThread extends Thread {
    private static final int MAX_MESSAGE_SIZE = 1024;
    private static final UUID uuid = UUID.fromString("a9a8791e-10f3-4223-b0c7-5ade55943a84");
    private static final String NAME = "BluetoothChat";

    private BluetoothSocket socket;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    BluetoothAdapter bluetoothAdapter;

    public HandelConnectedThread(BluetoothSocket bluetoothSocket){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        socket = bluetoothSocket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputStream = tmpIn;
        outputStream = tmpOut;

    }

    @Override
    public void run() {
        byte[] buffer = new byte[MAX_MESSAGE_SIZE];
        int bytes;

        try {
            bytes = inputStream.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}