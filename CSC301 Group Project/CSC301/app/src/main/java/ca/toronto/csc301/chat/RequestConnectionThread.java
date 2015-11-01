package ca.toronto.csc301.chat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by akshay on 31/10/15.
 */
public class RequestConnectionThread extends Thread{
    private static final UUID uuid = UUID.fromString("a9a8791e-10f3-4223-b0c7-5ade55943a84");
    private static final String NAME = "BluetoothChat";

    private BluetoothDevice device;
    private BluetoothSocket socket;
    BluetoothAdapter bluetoothAdapter;


    public RequestConnectionThread(BluetoothDevice bluetoothDevice){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        device = bluetoothDevice;
        BluetoothSocket tmp = null;
        try {
            tmp = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) { }
        socket = tmp;
    }

    @Override
    public void run() {
        try {
            socket.connect();
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }
}
