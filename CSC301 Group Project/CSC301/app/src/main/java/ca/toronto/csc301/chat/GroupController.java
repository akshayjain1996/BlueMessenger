package ca.toronto.csc301.chat;

import android.bluetooth.BluetoothAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by akshay on 21/11/15.
 */
public class GroupController {

    private static GroupController instance;
    List<GroupChat> groupChats;


    private void createGroupChat(){
        groupChats = new ArrayList<>();
    }

    public static GroupController getInstance(){
        if(instance == null){
            instance = new GroupController();
            return instance;
        }
        return instance;
    }

    public GroupChat createNewGroupChat(String name){
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        GroupChat groupChat = new GroupChat(name, bluetooth.getAddress());
        groupChats.add(groupChat);
        return groupChat;
    }

    public GroupChat getGroupChatByName(String name){
        Iterator<GroupChat> groupChatIterator = groupChats.iterator();
        while(groupChatIterator.hasNext()){
            GroupChat groupChat = groupChatIterator.next();
            if(groupChat.getName().equals(name)){
                return groupChat;
            }
        }
        return null;
    }

    public List<GroupChat> getGroupChats(){
        return groupChats;
    }
}
