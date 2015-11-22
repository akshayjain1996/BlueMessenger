package com.example.siddharthgautam.csc301;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import ca.toronto.csc301.chat.ConnectionsList;
import ca.toronto.csc301.chat.Event;
import ca.toronto.csc301.chat.GroupChat;

public class GroupChatActivity extends AppCompatActivity {

    public static int MAX_MSGS_ON_SCREEN = 5;
    static GroupChatActivity instance;
    private static final UUID uuid = UUID.fromString("63183176-0f7c-4673-b120-ac4116843e65");
    private Button sendButton;
    private TextView messageTextView;
    private ListView messageView;
    private ArrayAdapter<String> stringArrayAdapter;
    private ArrayList<String> stringList;
    GroupChat groupChat;



    private String mac;
    private Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        Bundle b = getIntent().getExtras();
        groupChat = b.getParcelable("GroupChat");

        setTitle("Chat: " + groupChat.getName());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sendButton = (Button) findViewById(R.id.group_send);
        messageTextView = (TextView) findViewById(R.id.group_new_message);
        messageView = (ListView) findViewById(R.id.group_message_list);

        stringList = new ArrayList<String>();
        stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringList);
        messageView.setAdapter(stringArrayAdapter);

        appContext = getApplicationContext();
        instance = this;

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
                messageTextView.setText("");
            }
        });
    }

    public static GroupChatActivity getInstance() {
        if(instance != null) {
            return instance;
        } else {
            GroupChatActivity groupChatActivity = new GroupChatActivity();
            instance = groupChatActivity;
            return  instance;
        }
    }

    private void sendMessage(){
        String message = messageTextView.getText().toString();

        stringArrayAdapter.add("You: " + message); //Todo: replace with message
        stringArrayAdapter.notifyDataSetChanged();

        Event e = new Event();
        e.setType(1);
        e.addAllowedClientsFromSet(groupChat.getmembers());
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        e.addExcludedTarget(bluetooth.getAddress());

        e.setMessage(message);

        if(true){//fix after
            //t.sendMessage(message);
            ConnectionsList.getInstance().sendEvent(e);
            Toast.makeText(appContext, "Broadcast a msg", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(appContext, "No connection available right now", Toast.LENGTH_LONG).show();
        }
    }
    public void recieveMessage(String message, String senderMac){
        String senderName = ConnectionsList.getInstance().getNameFromMac(senderMac);
        Toast.makeText(appContext, "Got a message", Toast.LENGTH_LONG).show();
        stringArrayAdapter.add("Them: " + message);
        //If this chat is not open

        if(!(groupChat.checkMemberByMAC(senderMac))){
            //Creates event and sets details
            Event e = new Event();
            e.setType(1);
            e.setSender(senderMac);
            e.setMessage(message);
            try {
                //Opens file and writes to it
                FileOutputStream out = new FileOutputStream(senderMac+".txt");
                ObjectOutputStream serializer = new ObjectOutputStream(out);
                serializer.writeObject(e);
            }catch(FileNotFoundException ex){
                System.out.println("File store.data not found");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        if(stringArrayAdapter.getCount() > MAX_MSGS_ON_SCREEN){
            stringArrayAdapter.remove(stringArrayAdapter.getItem(0));
        }

        stringArrayAdapter.notifyDataSetChanged();
        //BluetoothController.getInstance().sendMessage(new Message());
        //implement this!
    }


}
