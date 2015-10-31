package com.example.siddharthgautam.csc301;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

    public final int MAX_MESSAGES = 100;

    private BluetoothAdapter bluetooth;
    private Set<BluetoothDevice> devices;
    private static final UUID uuid = UUID.fromString("63183176-0f7c-4673-b120-ac4116843e65");
    private ListView chatBoxListView;
    private ArrayAdapter adapter;
    private ArrayList<String> msgs;
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

        chatBoxListView = (ListView) findViewById(R.id.chatBoxListView);
        msgs = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, msgs);
    }

    @Override
    protected void onResume() {
        super.onResume();
        File deserialize_prev_messages = new File(this.getApplicationContext().getFilesDir().getAbsolutePath());
        loadMessages(deserialize_prev_messages, "Test.txt"); //Todo: replace with customized filename based on partner id
    }

    /**
     * Takes in a file representing the location of this app's resources as well as the filename to load.
     * Loads the messages in filename int this activity's adapter if filename exists.
     * Otherwise, do nothing.
     * Note that each line is of form <Username>: <Message></Message>
     * @param filesDir A file which has the files directory for this app.
     * @param fileName The name of the raw text file to load.
     */
    private void loadMessages(File filesDir, String fileName){
        File msgHolder = new File(filesDir, fileName);
        try {
            FileReader msgFileReader = new FileReader(msgHolder);
            BufferedReader bufferedFileReader = new BufferedReader(msgFileReader);

        } catch (FileNotFoundException e) {
            //do nothing; no messages to load
            Log.i("FileLoader:", "No message buffer file found.");
        }
    }

    private void updateMessages(String message, String user) {
        adapter.add(user + ": " + message);
        if (adapter.getCount() > MAX_MESSAGES) {
            adapter.remove(adapter.getItem(1));
        }
        adapter.notifyDataSetChanged();
    }

    private void loadPrevMessages(){

    }
}