package com.example.siddharthgautam.csc301;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Iterator;
import java.util.Set;

import ca.toronto.csc301.chat.ConnectionsList;

public class contactsActivity extends AppCompatActivity {
    private BluetoothAdapter bluetooth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bluetooth = BluetoothAdapter.getDefaultAdapter();
        setContentView(R.layout.activity_contacts);
        updateContactsList();
    }

    private void updateContactsList(){
        ConnectionsList c = ConnectionsList.getInstance();
        Set<BluetoothDevice> d = bluetooth.getBondedDevices();
        Iterator<BluetoothDevice> i = d.iterator();

    }
}
