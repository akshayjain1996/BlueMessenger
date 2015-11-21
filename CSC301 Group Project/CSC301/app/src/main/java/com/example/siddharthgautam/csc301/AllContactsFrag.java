package com.example.siddharthgautam.csc301;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import ca.toronto.csc301.chat.ConnectedThread;
import ca.toronto.csc301.chat.ConnectionsList;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_all_contacts_frag, container, false);

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

                goToChat(getView(), device);
            }
        });
        return view;
    }

    public void goToChat(View v, BluetoothDevice device){
    Intent intent = new Intent(getActivity(), chatActivity.class);
    intent.putExtra("BluetoothDevice", device);
    startActivity(intent);
    }


    private void updateContactsList(){

    }

}
