package com.example.siddharthgautam.csc301;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import ca.toronto.csc301.chat.ConnectionsList;

public class contactsActivity extends AppCompatActivity {
    private BluetoothAdapter bluetooth;
    ListView contactsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bluetooth = BluetoothAdapter.getDefaultAdapter();
        setContentView(R.layout.activity_contacts);
        contactsList = (ListView)findViewById(R.id.listContacts);
        updateContactsList();
    }

    private void updateContactsList(){
        ConnectionsList c = ConnectionsList.getInstance();
        Set<BluetoothDevice> d = bluetooth.getBondedDevices();
        ArrayList cL = new ArrayList();
        Iterator<BluetoothDevice> i = d.iterator();

        while(i.hasNext()){
            BluetoothDevice device = i.next();
            String device_name = device.getName();
            cL.add(device_name);
            /**if(device_name.contains("BlueM-")){ //Doesnt properly work for s6 edge so temporarily disabled(other wise I cant test..)
                cL.add(device_name.substring(6));
            }
            else //Exception for testing "Priyen Galaxy S6 edge" because the setName method apparntly has no effect on it
            if(device_name == "Priyen Galaxy S6 edge"){
                cL.add(device_name);
            }**/
        }
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, cL);
        contactsList.setAdapter(adapter);
    }
}
