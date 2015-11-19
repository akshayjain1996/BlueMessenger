package ca.toronto.csc301.chat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by akshay on 31/10/15.
 */
public class ConnectionsList {
    HashMap<BluetoothDevice,ConnectedThread> map = new HashMap<BluetoothDevice,ConnectedThread>();
    //mac -> name
    HashMap<String, String> networkDevices = new HashMap<String, String>();
    private Handler mHandler;
    static ConnectionsList instance;
    static private AcceptThread acceptThread;

    public void setHandler(Handler h){
        mHandler = h;
    }

    private ConnectionsList(){
        instance = this;;
    }

    public void newDeviceInNetwork(String mac, String name){
        networkDevices.put(mac, name);
    }

    public void sendEvent(Event e){
        Set<String> excludedMacs = e.getExcludedTargets();
        Set<BluetoothDevice> keys = map.keySet();
        Iterator<BluetoothDevice> i = keys.iterator();
        //exclude the ones I can send to
        while(i.hasNext()){
            e.addExcludedTarget(i.next().getAddress());
        }
        //exclude myself
        e.addExcludedTarget(BluetoothAdapter.getDefaultAdapter().getAddress());
        //send to the ones i can send to, then everyone else will do the same..
        Iterator<BluetoothDevice> it = keys.iterator();
        while(it.hasNext()){
            BluetoothDevice d = it.next();
            if(excludedMacs.contains(d.getAddress()) == false) {
                getConnectedThread(d).sendEvent(e);
            }
        }
    }

    public String getNameFromMac(String mac){
        if(networkDevices.get(mac) == null){
            return "";
        }
        return networkDevices.get(mac);
    }

    public void newConnection(BluetoothSocket s, BluetoothDevice d){

        if(map.get(d) == null){
            final ConnectedThread t = new ConnectedThread(s, mHandler);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    t.start();
                }
            }).start();
            this.map.put(d, t);
        }
        else{ //In case connection fails or is bad remake it
            if(map.get(d).getSocket().isConnected() == false){
                closeConnection(d);
                makeConnectionTo(d);
            }
        }
    }

    public ConnectedThread getConnectedThread(BluetoothDevice d){
        return this.map.get(d);
    }

    public void closeConnection(BluetoothDevice device){
        ConnectedThread s = this.map.get(device);
        s.cancel();
        this.map.put(device, null);
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

    public static void accept(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                AcceptThread t = new AcceptThread();
                t.start();
            }
        }).start();
    }

    public static void makeConnectionTo(final BluetoothDevice device){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ConnectThread t = new ConnectThread(device);
                t.start();
            }
        }).start();
    }

}
