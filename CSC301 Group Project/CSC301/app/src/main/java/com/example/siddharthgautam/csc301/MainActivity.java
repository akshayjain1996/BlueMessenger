package com.example.siddharthgautam.csc301;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.bluetooth.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.util.ArrayList;
import java.util.UUID;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements Serializable{

    private BluetoothAdapter bluetooth;
    private Set<BluetoothDevice> devices;
    private Set<BluetoothDevice> connectedDevices;
    Button scan, contacts;
    ListView devicesList;
    private static final UUID uuid = UUID.fromString("a9a8791e-10f3-4223-b0c7-5ade55943a84");
    private BluetoothServerSocket blueSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Creates a hash set to store the devices found during scanning
        devices = new HashSet<BluetoothDevice>();

        scan = (Button)findViewById(R.id.scanButton);
        contacts = (Button)findViewById(R.id.deviceList);

        bluetooth = BluetoothAdapter.getDefaultAdapter();
        devicesList = (ListView)findViewById(R.id.listView);

        // Check if Bluetooth is supported. If so enable it if necessary
        if (bluetooth == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not supported on this device",
                    Toast.LENGTH_LONG).show();
            //System.exit(1);
        }

        if (!bluetooth.isEnabled()) {
            Intent start = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(start, 1);
            Toast.makeText(getApplicationContext(), "Bluetooth is disabled", Toast.LENGTH_LONG).show();
        }

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(receiver, filter);

        scan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String device_name = bluetooth.getName();
                if (!device_name.contains("BlueM-")) {
                    bluetooth.setName("BlueM-" + device_name);
                }
                findNewDevices();
            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openContacts();
            }
        });

        devicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "I will connect", Toast.LENGTH_LONG).show();
                String deviceName = devicesList.getItemAtPosition(position).toString();
                if (deviceName != null) {
                    connectDevice(deviceName);
                }
                //connectDevice(deviceName);
            }
        });
    }

    //Go to contacts activity
    public void openContacts() {
        startActivity(new Intent(MainActivity.this, contactsActivity.class));
    }

    public void findNewDevices(){
        bluetooth.startDiscovery();
    }


    // Reciever for discovering new devices..
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device != null) {
                    devices.add(device);
                    Toast.makeText(getApplicationContext(), "New Device Discovered", Toast.LENGTH_LONG).show();
                    listDevices();
                }

            }
        }
    };
    //Pairs with a bluetooth device with deviceName.
    public void connectDevice(String deviceName){
        //Checks if name matches
        for(BluetoothDevice device : devices){
            if(device.getName().equals(deviceName)){
                try{
                    //Connects
                    Method m = device.getClass().getMethod("createBond", (Class[]) null);
                    m.invoke(device, (Object[]) null);
                    Toast.makeText(getApplicationContext(), device.getName(), Toast.LENGTH_LONG).show();
                    try {
                        connectedDevices.add(device);
                    } catch (NullPointerException e) {
                        Toast.makeText(getApplicationContext(), "cannot add null devices", Toast.LENGTH_LONG);
                    }
                    //connectedDevices.add(device);
                    //Exception handling
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // Disconnects paired device with name deviceName.
    public void disconnectDevice(String deviceName){
        //Checks if name matches
        for(BluetoothDevice device : devices) {
            if (device.getName().equals(deviceName))
                try {
                    //Connects
                    Method m = device.getClass().getMethod("removeBond", (Class[]) null);
                    m.invoke(device, (Object[]) null);
                    try {
                        connectedDevices.remove(device);
                    } catch (NullPointerException e) {
                        Toast.makeText(getApplicationContext(), "cannot add null devices", Toast.LENGTH_LONG);
                    }
                    //Exception handling
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
        }
    }


    // Creates a bluetooth socket
    public void startSocket(View view) {
        BluetoothServerSocket socket = null;
        //BluetoothSocket tmp = null;
        try {
            socket = bluetooth.listenUsingRfcommWithServiceRecord("Bluetooth", uuid);
            Toast.makeText(getApplicationContext(), "Socket has been created", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Socket not created", Toast.LENGTH_LONG).show();
        }
        blueSocket = socket;
    }
    //Lists all the devices found during scanning.
    public void listDevices(){

        ArrayList deviceList = new ArrayList();

        for(BluetoothDevice blueDevice : devices) {
            String name = blueDevice.getName();
            deviceList.add(blueDevice.getName());
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, deviceList);
        devicesList.setAdapter(adapter);
    }
    // Connects with a perticular device
    public void connect() {
        for (BluetoothDevice bt : devices) {
            Toast.makeText(getApplicationContext(), bt.getAddress(), Toast.LENGTH_LONG).show();
        }
    }
    //Starts a chat.
    public void startChat(View view) {
        //Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_LONG).show();
        devices = bluetooth.getBondedDevices();
        ArrayList deviceList = new ArrayList();

        for(BluetoothDevice blueDevice : devices) {
            String name = blueDevice.getName();
            deviceList.add(blueDevice.getName());
            //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
        }
        //Intent intent = new Intent(MainActivity.this, chatActivity.class);
        Intent intent = new Intent(getApplicationContext(), chatActivity.class);
        intent.putExtra("BLUETOOTH_VALUE", bluetooth.toString());

        MainActivity.this.startActivity(intent);

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onResume(){
        File resourceLocation = new File(this.getApplicationContext().getFilesDir().getAbsolutePath());

        super.onResume();
    }

}
