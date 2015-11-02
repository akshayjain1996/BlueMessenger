package ca.toronto.csc301.chat;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by akshay on 31/10/15.
 */
public class ConnectionsList {

    List<ConnectedDevice> connectedDevices;

    static ConnectionsList instance;

    private ConnectionsList(){
        instance = this;
        connectedDevices = new ArrayList<ConnectedDevice>();
    }

    public List<ConnectedDevice> getConnectedDevices() {
        return connectedDevices;
    }

    public void addConnection(ConnectedDevice connectedDevice){
        connectedDevices.add(connectedDevice);
    }

    public static ConnectionsList getInstance() {
        if(instance != null) {
            return instance;
        } else {
            ConnectionsList connectionsList = new ConnectionsList();
            instance = connectionsList;
            return  instance;
        }
    }

    public ConnectedDevice findConnectedDevide(BluetoothDevice device){
        for(ConnectedDevice connectedDevice : connectedDevices){
            if(connectedDevice.getDevice().equals(device)){
                return connectedDevice;
            }
        }
        return null;
    }
}
