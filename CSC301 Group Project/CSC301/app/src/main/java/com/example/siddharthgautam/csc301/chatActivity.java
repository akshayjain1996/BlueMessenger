package com.example.siddharthgautam.csc301;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Handler;

public class chatActivity extends AppCompatActivity {

    private BluetoothAdapter bluetooth;
    private Set<BluetoothDevice> devices;
    private static final UUID uuid = UUID.fromString("63183176-0f7c-4673-b120-ac4116843e65");
    private Button sendButton;
    private TextView messageTextView;
    private ListView messageView;
    private ArrayAdapter<String> stringArrayAdapter;
    private ArrayList<String> stringList;

    //private final Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Toast.makeText(getApplicationContext(), "new activity opened", Toast.LENGTH_LONG).show();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String bluetoothValue = extras.getString("BLUETOOTH_VALUE");
            Toast.makeText(getApplicationContext(), bluetoothValue, Toast.LENGTH_LONG).show();

        }
        sendButton = (Button) findViewById(R.id.sendMessageButton);
        setSendButtonFunction(sendButton);
        messageTextView = (TextView) findViewById(R.id.messageTextView);
        messageView = (ListView) findViewById(R.id.messageView);
        stringList = new ArrayList<String>();
        stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringList);
        messageView.setAdapter(stringArrayAdapter);
    }

    private void setSendButtonFunction(Button sendButton){
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage(){
        Toast.makeText(this, messageTextView.getText(), Toast.LENGTH_SHORT);
        stringArrayAdapter.add("Hi");
        //implement this!
    }








}
