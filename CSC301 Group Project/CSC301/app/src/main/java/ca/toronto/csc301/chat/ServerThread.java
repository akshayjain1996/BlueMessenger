package ca.toronto.csc301.chat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by akshay on 31/10/15.
 */
public class ServerThread extends Thread{
    private BluetoothServerSocket serverSocket = null;
    private BluetoothAdapter bluetoothAdapter = null;

    private static final UUID uuid = UUID.fromString("a9a8791e-10f3-4223-b0c7-5ade55943a84");
    private static final String NAME = "BluetoothChat";


    public ServerThread(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, uuid);
        } catch (IOException e) { }
        serverSocket = tmp;
    }

    @Override
    public void run() {
        BluetoothSocket clientSocket = null;

        while(true){
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(clientSocket !=null){
                ConnectedDevice connectedDevice = new ConnectedDevice(clientSocket.getRemoteDevice(), clientSocket);
                ConnectionsList.getInstance().addConnection(connectedDevice);
            }
        }
    }
}
