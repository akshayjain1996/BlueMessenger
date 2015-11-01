package ca.toronto.csc301.chat;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/**
 * Created by akshay on 31/10/15.
 */
public class ConnectedDevice {

    private HandelConnectedThread handelConnectedThread;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private boolean connected;

    ConnectedDevice(BluetoothDevice device, BluetoothSocket bluetoothSocket){
        this.device = device;
        this.socket = bluetoothSocket;
        connected = false;
        handelConnectedThread = new HandelConnectedThread(socket, device);
        handelConnectedThread.start();
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public BluetoothSocket getSocket() {
        return socket;
    }

    public void setSocket(BluetoothSocket socket) {
        this.socket = socket;
    }

    public void connectionComplete(){
        connected = true;
    }

    public void connectionLost(){
        connected = false;
    }

    public HandelConnectedThread getHandelConnectedThread() {
        return handelConnectedThread;
    }
}
