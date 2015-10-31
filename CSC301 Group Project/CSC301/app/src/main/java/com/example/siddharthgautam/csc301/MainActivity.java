package com.example.siddharthgautam.csc301;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.bluetooth.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements Serializable{

    private BluetoothAdapter bluetooth;
    private Set<BluetoothDevice> devices;
    Button scan, list;
    ListView listView;
    private static final UUID uuid = UUID.fromString("a9a8791e-10f3-4223-b0c7-5ade55943a84");
    private BluetoothServerSocket blueSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scan = (Button)findViewById(R.id.deviceScan);
        list = (Button)findViewById(R.id.deviceList);

        bluetooth = BluetoothAdapter.getDefaultAdapter();
        int flag = 0;
        listView = (ListView)findViewById(R.id.listView);

        // Check if Bluetooth is supported. If so enable it if necessary
        if (bluetooth == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not supported on this device",
                    Toast.LENGTH_LONG).show();
            //System.exit(1);
        } else if (!bluetooth.isEnabled()) {
            flag = 1;
            Intent start = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(start, 1);
            Toast.makeText(getApplicationContext(), "Bluetooth is disabled", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth is ready", Toast.LENGTH_LONG).show();
        }

        if (flag == 1) {
            Toast.makeText(getApplicationContext(), "Bluetooth has been enabled", Toast.LENGTH_LONG).show();
        }

    }

    public  void show(View view){
        Intent show = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(show, 0);

        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

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

    public void listDevices(View view){
        // set focus
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

        devices = bluetooth.getBondedDevices();
        ArrayList deviceList = new ArrayList();

        for(BluetoothDevice blueDevice : devices) {
            String name = blueDevice.getName();
            deviceList.add(blueDevice.getName());
            //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
        }

        //Toast.makeText(getApplicationContext(),"Showing Paired Devices",Toast.LENGTH_SHORT).show();


        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, deviceList);
        listView.setAdapter(adapter);


    }

    public void startChat(View view) {
        //Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_LONG).show();
        devices = bluetooth.getBondedDevices();
        ArrayList deviceList = new ArrayList();

        for(BluetoothDevice blueDevice : devices) {
            String name = blueDevice.getName();
            deviceList.add(blueDevice.getName());
            //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
        }
        //Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
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
}
