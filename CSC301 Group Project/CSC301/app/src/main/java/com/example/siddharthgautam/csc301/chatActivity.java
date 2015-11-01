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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Handler;

public class ChatActivity extends AppCompatActivity {

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

    @Override
    protected void onStart() {
        //// TODO: 10/31/2015 replace "Test.txt" with unique identifier for chat.
        loadMessages(getApplicationContext().getFilesDir().getAbsoluteFile(), "Test.txt");
        super.onStart();
    }

    @Override
    protected void onStop() {
        //// TODO: 10/31/2015 replace "Test.txt" with unique identifier for chat.
        saveMessages(getApplicationContext().getFilesDir().getAbsoluteFile(), "Test.txt");
        super.onStop();
    }

    /**
     * Set sendButton's onClickListener to sendMessage.
     * @param sendButton A button, which you want to use as the send message button.
     */
    private void setSendButtonFunction(Button sendButton){
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    /**
     * Called when the send button is pressed. Get the text from the messageTextView,
     * send it through a BlueTooth connection and write the message on your own message log.
     */
    private void sendMessage(){
        Toast.makeText(this, messageTextView.getText(), Toast.LENGTH_SHORT);
        stringArrayAdapter.add("You: " + messageTextView.getText().toString()); //Todo: replace with message
        stringArrayAdapter.notifyDataSetChanged();
        //implement this!
    }



    /**
     * Loads all the messages from a given message log file onto the UI.
     * @param dir A file containing the location of the app's resources.
     * @param username A unique identifier indicating which message log file you want to view.
     */
    public void loadMessages(File dir, String username){
        File loadFile = new File(dir, username + ".ser");
        try {
            FileReader loadFileReader = new FileReader(loadFile);
            BufferedReader br = new BufferedReader(loadFileReader);
            String current;
            while((current = br.readLine()) != null){
                stringArrayAdapter.add(current);
            }
            stringArrayAdapter.notifyDataSetChanged();
        } catch (FileNotFoundException e) {
            //no saved messages
        } catch (IOException e) {
            stringList.clear();
        }
    }

    /**
     * Writes all the messages from the current message buffer array (on the ListView)
     * to a file in the app's resources folder.
     * @param dir A file of the app's resource folder.
     * @param username A unique identifier for this message log file. Must be able to identify
     *                 its name for later use.
     */
    public void saveMessages(File dir, String username){
        File saveFile = new File(dir, username + ".ser");
        try {
            BufferedWriter messageSaveWriter = new BufferedWriter(new FileWriter(saveFile));
            for(int i = 0; i < messageView.getCount(); i++){
                messageSaveWriter.write(stringArrayAdapter.getItem(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
