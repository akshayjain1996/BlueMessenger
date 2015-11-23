package com.example.siddharthgautam.csc301;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ca.toronto.csc301.chat.BlockedUsers;
import ca.toronto.csc301.chat.ConnectedThread;
import ca.toronto.csc301.chat.ConnectionsList;
import ca.toronto.csc301.chat.Event;
import ca.toronto.csc301.chat.GroupChat;
import ca.toronto.csc301.chat.GroupController;

public class AllContactsFrag extends Fragment {

    private BluetoothAdapter bluetooth;
    private ListView contactsList;
    private ArrayList cL = new ArrayList();
    private ArrayAdapter adapter;
    public static AllContactsFrag newInstance() {
        AllContactsFrag fragment = new AllContactsFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectionsList.getInstance().setHandler(mHandler);
        ConnectionsList.getInstance().accept();
        Toast.makeText(getContext(),"Connecting to paired devices..." +
                " asking for all network devices upon connect", Toast.LENGTH_LONG).show();
        BluetoothAdapter local = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> paired = local.getBondedDevices();
        Iterator<BluetoothDevice> it = paired.iterator();
        while(it.hasNext()){
            BluetoothDevice dev = it.next();
            ConnectedThread t = ConnectionsList.getInstance().getConnectedThread(dev);
            if(t != null){
                if(t.getSocket().isConnected() == true){
                    continue;
                }
                if(t.getSocket().getRemoteDevice() != null){
                    continue;
                }
            }
            ConnectionsList.makeConnectionTo(dev);
        }

        IntentFilter f1 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        IntentFilter f2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        IntentFilter f3 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        getContext().registerReceiver(receiver, f1);
        getContext().registerReceiver(receiver, f2);
        getContext().registerReceiver(connectedReceiver, f3);
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String address = null;
            switch (msg.what) {
                case 1:
                    byte[] readBuf = (byte[]) msg.obj;
                    Event e;
                    try{
                        e = (Event) Event.deserialize(readBuf);
                        int type = e.getType();
                        switch(type) {
                            case 1:
                                Toast.makeText(getContext(), "Recieved a broadcast event", Toast.LENGTH_LONG).show();

                                String m = e.getMessage();
                                if(e.isClientAllowed(bluetooth.getAddress())) {
                                    showNotification("BlueM - Message from " + e.getSenderName(),
                                            e.getMessage());
                                }
                                chatActivity.getInstance().recieveMessage(m, e.getSender(), e);
                                    //this client can see it
                                //}
                                //code to forward
                                ConnectionsList.getInstance().sendEvent(e);
                                break;
                            case 2:
                                Toast.makeText(getContext(), "Some device asked for a devices update, sending them", Toast.LENGTH_LONG).show();
                                ConnectionsList.getInstance().sendEvent(e);
                                break;
                            case 3:
                                Toast.makeText(getContext(), "Recieved a network devices event update", Toast.LENGTH_LONG).show();
                                ConnectionsList.getInstance().sendEvent(e);
                                break;
                            case 4:
                                //Toast.makeText(getContext(), "a new device joined the network", Toast.LENGTH_LONG).show();
                                ConnectionsList.getInstance().sendEvent(e);
                                break;
                            case 5:
                                if(e.getGroupChat().checkMemberByMAC(BluetoothAdapter.getDefaultAdapter().getAddress())) {
                                    HandleType5(e);
                                } else{
                                    ConnectionsList.getInstance().sendEvent(e);
                                }
                                break;
                            case 6:
                                //Toast.makeText(getContext(), "Keep alive from " + e.getSenderName(), Toast.LENGTH_LONG).show();
                                ConnectionsList.getInstance().sendEvent(e);
                                break;
                            case 7:
                                GroupChat g = e.getGroupChat();
                                if(g.checkMemberByMAC(BluetoothAdapter.getDefaultAdapter().getAddress())){
                                    GroupChatActivity.getInstance().recieveMessage(e.getMessage(),
                                            e.getSender(), g.getName());
                                }
                                ConnectionsList.getInstance().sendEvent(e);
                                break;
                        }

                    }
                    catch(Exception ex) {

                    }
                    updateContactsList();
                    break;
                case 2:
                    updateContactsList();
                    break;

            }
        }
    };

    public void HandleType5(Event event){
        Toast.makeText(getActivity(), "you have been added to a grp chat", Toast.LENGTH_LONG).show();
        GroupController.getInstance().addGroupChat(event.getGroupChat());
        ConnectionsList.getInstance().sendEvent(event);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.activity_all_contacts_frag, container, false);
        View view = inflater.inflate(R.layout.activity_all_contacts_frag, container, false);

        ListView listView = (ListView) view.findViewById(R.id.contact_list);
        contactsList = listView;
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, cL);

        if (adapter == null) {
            // listview is empty, so add a button
            listView.setEmptyView(view.findViewById(R.id.emptyView));
        }
            // listview has data, so have a button as a header and hide empty button
            //listView.setVisibility(View.GONE);

            // add the listview header button
        Button button = new Button(getActivity());
        button.setText("Scan for Devices");
        button.setBackgroundColor(getResources().getColor(R.color.lightblue));
        listView.setBackgroundColor(getResources().getColor(R.color.white));
        button.setTextColor(getResources().getColor(R.color.white));
        listView.addHeaderView(button);

        updateContactsList();

        contactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String deviceName = contactsList.getItemAtPosition(position).toString();
                boolean connection = false;
                if (deviceName.contains("✓")) {
                    connection = true;
                    deviceName = deviceName.substring(2);//to account for possible checkmark
                }
                BluetoothDevice device = null;
                try {
                    device = getDeviceByName(deviceName);
                } catch (Exception ex) {
                    //device isnt connected yet/maybe a outer network device
                }
                String mac;
                if (device != null) {//for paired devices
                    mac = device.getAddress();
                    ConnectedThread t = ConnectionsList.getInstance().getConnectedThread((device));
                    if (t == null) {//no connection available, try to connect
                        ConnectionsList.getInstance().makeConnectionTo(device);
                        Toast.makeText(getContext(), deviceName + " is a non connected paired device" +
                                "   Trying to connect..", Toast.LENGTH_LONG).show();
                        ConnectionsList.getInstance().closeConnection(device);
                        ConnectionsList.getInstance().makeConnectionTo(device);
                        return;
                    }
                    if (t.getSocket().isConnected() == false) {
                        Toast.makeText(getContext(), deviceName + " is a non connected paired device" +
                                "   Trying to connect..", Toast.LENGTH_LONG).show();
                        ConnectionsList.getInstance().closeConnection(device);
                        ConnectionsList.getInstance().makeConnectionTo(device);
                        return;
                    }
                } else {//this device is not paired, but maybe its in the network, check.
                    mac = ConnectionsList.getInstance().getMacFromName(deviceName);
                    if (ConnectionsList.getInstance().isDeviceInNetwork(mac) == false) {
                        Toast.makeText(getContext(), deviceName + " is currently not in the network", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                goToChat(getView(), mac);
            }
        });
        return view;
    }

    public void goToChat(View v, String mac){
    Intent intent = new Intent(getActivity(), chatActivity.class);
    intent.putExtra("mac", mac);
    startActivity(intent);
    }

    public void showNotification(String title, String text) {
        PendingIntent pi = PendingIntent.getActivity(getActivity(), 0, new Intent(getActivity(),
                AllContactsFrag.class), 0);
        Resources r = getResources();
        Notification notification = new NotificationCompat.Builder(this.getActivity())
                .setTicker(title)
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getActivity().
                getSystemService(getActivity().NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    public static BluetoothDevice getDeviceByName(String name) {
        for (BluetoothDevice device : BluetoothAdapter.getDefaultAdapter().getBondedDevices()) {
            if (device.getName().equals(name)) {
                return device;
            }
        }
        return null;
    }



    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override//disconnected
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
           // Toast.makeText(getContext(), device.getName() + " disconnected", Toast.LENGTH_LONG).show();
            ConnectionsList.getInstance().closeConnection(device);
            //ConnectionsList.getInstance().makeConnectionTo(device);
            updateContactsList();
        }
    };

    private final BroadcastReceiver connectedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            //Toast.makeText(getContext(), device.getName() + " connected!", Toast.LENGTH_LONG).show();
            boolean status = ConnectionsList.getInstance().onConnected(device);
            if(status == false){
                //ConnectionsList.getInstance().closeConnection(device);
                //ConnectionsList.getInstance().makeConnectionTo(device);
            }
            else{
                Toast.makeText(getContext(), device.getName() + " connected!", Toast.LENGTH_SHORT).show();
            }
            updateContactsList();
        }
    };

    private void updateContactsList(){
        cL.clear();
        bluetooth = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> d = bluetooth.getBondedDevices();
        Iterator<BluetoothDevice> i = d.iterator();

        while(i.hasNext()){
            BluetoothDevice device = i.next();
            String device_name = device.getName();
            ConnectedThread t = ConnectionsList.getInstance().getConnectedThread(device);
            if(t == null){
                //Toast.makeText(getContext(), device.getName() + " -- trying to connect to it", Toast.LENGTH_SHORT).show();
                //ConnectionsList.getInstance().closeConnection(device);
                //ConnectionsList.getInstance().makeConnectionTo(device);
            }
            else{
                if(t.getSocket().isConnected() == false){
                    //ConnectionsList.getInstance().closeConnection(device);
                    //ConnectionsList.getInstance().makeConnectionTo(device);
                }
                else {
                    device_name = "✓ " + device_name;
                }
            }
            if(device_name.contains("✓") == false && ConnectionsList.getInstance().isDeviceInNetwork(device.getAddress())){//some other client has this client connected
                device_name = "✓ " + device_name;
            }
            cL.add(device_name);
        }
        // now add from network devices
        Iterator<String> di = ConnectionsList.getInstance().getNamesOfConnectedDevices().iterator();
        while(di.hasNext()){
            String name = di.next();
            if(cL.contains(name) || cL.contains("✓ " + name)){
                continue;
            }
            cL.add("✓ "+name);
        }
        contactsList.setAdapter(adapter);
    }

}
