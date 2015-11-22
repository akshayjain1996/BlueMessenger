package com.example.siddharthgautam.csc301;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.RelativeLayout;

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
        //View view = inflater.inflate(R.layout.activity_all_contacts_frag, container, false);
        View view = inflater.inflate(R.layout.activity_all_contacts_frag, container, false);

        ListView listView = (ListView) view.findViewById(R.id.contact_list);

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
        button.setTextColor(getResources().getColor(R.color.white));
        listView.addHeaderView(button);



        bluetooth = BluetoothAdapter.getDefaultAdapter();
        //contactsList = (ListView) view;
        contactsList = listView;
        Set<BluetoothDevice> d = bluetooth.getBondedDevices();
        Iterator<BluetoothDevice> i = d.iterator();

        while(i.hasNext()){
            BluetoothDevice device = i.next();
            ConnectionsList.getInstance().makeConnectionTo(device);
            String device_name = device.getName();
            if(cL.contains(device_name) == false){
                cL.add(device_name);
            }
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
                BluetoothDevice device = null;
                try{
                    device  = MainActivity.getDeviceByName(deviceName);
                }
                catch(Exception ex){
                    //device isnt connected yet/maybe a outer network device
                }
                String mac;
                if (device != null) {//for paired devices
                    mac = device.getAddress();
                    ConnectedThread t = ConnectionsList.getInstance().getConnectedThread((device));
                    if (t == null) {//no connection available, try to connect
                        ConnectionsList.getInstance().makeConnectionTo(device);
                        Toast.makeText(getContext(), deviceName + " is currently not in the network." +
                                "   Trying to connect..", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(t.getSocket().isConnected() == false){
                        Toast.makeText(getContext(), deviceName + " is currently not in the network." +
                                "   Trying to connect..", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                else{//this device is not paired, but maybe its in the network, check.
                    mac = ConnectionsList.getInstance().getMacFromName(deviceName);
                    if(ConnectionsList.getInstance().isDeviceInNetwork(mac) == false){
                        Toast.makeText(getContext(), deviceName + " is currently not in the network." +
                                "   Trying to connect..", Toast.LENGTH_LONG).show();
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


    private void updateContactsList(){

    }

}
