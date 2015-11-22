package com.example.siddharthgautam.csc301;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import ca.toronto.csc301.chat.ConnectedThread;
import ca.toronto.csc301.chat.ConnectionsList;

public class GroupFrag extends Fragment {

    private BluetoothAdapter bluetooth;
    private ListView groupList;
    private ArrayList gL = new ArrayList();
    private ArrayAdapter adapter;
    String[] items;

    public static GroupFrag newInstance() {
        GroupFrag fragment = new GroupFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_group_frag, container, false);
        //TextView textView = (TextView) view;
        //textView.setText("Group Frag");
        //items = new String[] { "Vegetables","Fruits","Flower Buds","Legumes","Bulbs","Tubers" };

        ListView listView = (ListView) view.findViewById(R.id.group_list);
        Button button = new Button(getActivity());
        button.setText("Add a Group");
        button.setBackgroundColor(getResources().getColor(R.color.lightblue));
        button.setTextColor(getResources().getColor(R.color.white));

        if (adapter == null) {
            // the list view is empty, just display the "Add a Group" button
            listView.setEmptyView(view.findViewById(R.id.emptyView));
            listView.setBackgroundColor(getResources().getColor(R.color.beige));
        } else {
            listView.addHeaderView(button);
            listView.setBackgroundColor(getResources().getColor(R.color.beige));
        }
        //listView.addHeaderView(button);
        //ListView emptyText = (ListView)view.findViewById(R.id.group_list);
        //emptyText.setEmptyView(button);
        //listView.setEmptyView(button);


        return view;
    }

    /*
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_all_contacts_frag, container, false);

        ListView listView = (ListView) view.findViewById(R.id.contact_list);
        Button button = new Button(getActivity());
        button.setText("Scan for Devices");
        button.setBackgroundColor(getResources().getColor(R.color.lightblue));
        button.setTextColor(getResources().getColor(R.color.white));
        listView.addHeaderView(button);

        bluetooth = BluetoothAdapter.getDefaultAdapter();
        contactsList = (ListView) view;
        Set<BluetoothDevice> d = bluetooth.getBondedDevices();
        Iterator<BluetoothDevice> i = d.iterator();

        while(i.hasNext()){
            BluetoothDevice device = i.next();
            ConnectionsList.getInstance().makeConnectionTo(device);
            String device_name = device.getName();
            cL.add(device_name);
        }
        // now add from network devices
        Iterator<String> di = ConnectionsList.getInstance().getNamesOfConnectedDevices().iterator();
        while(di.hasNext()){
            String name = di.next();
            if(cL.contains(name)){
                continue;
            }
            cL.add(name);
        }

        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, cL);
        contactsList.setAdapter(adapter);


        contactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String deviceName = contactsList.getItemAtPosition(position).toString();
                BluetoothDevice device = MainActivity.getDeviceByName(deviceName);
                if (device != null) {//for paired devices
                    ConnectedThread t = ConnectionsList.getInstance().getConnectedThread((device));
                    if (t == null) {//no connection available, try to connect
                        ConnectionsList.getInstance().makeConnectionTo(device);
                    }
                }
                if (ConnectionsList.getInstance().isDeviceInNetwork(device.getAddress()) == false) {

                    return;

                }

                //goToChat(getView(), device);
            }
        });
        return view;
    }
    */

}