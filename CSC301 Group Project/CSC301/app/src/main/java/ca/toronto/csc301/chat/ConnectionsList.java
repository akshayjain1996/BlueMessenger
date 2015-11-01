package ca.toronto.csc301.chat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 31/10/15.
 */
public class ConnectionsList {

    List<ConnectedDevice> connectedDevices;

    static ConnectionsList instance;

    public ConnectionsList(){
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
        return instance;
    }
}
