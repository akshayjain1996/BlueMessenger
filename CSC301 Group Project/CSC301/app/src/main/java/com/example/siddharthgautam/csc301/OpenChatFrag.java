package com.example.siddharthgautam.csc301;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ca.toronto.csc301.chat.ConnectionsList;
import ca.toronto.csc301.chat.FavouriteUsers;
import ca.toronto.csc301.chat.GroupChat;

public class OpenChatFrag extends Fragment {

    ListView favouritesList;
    ArrayAdapter<String> adapter;

    public static OpenChatFrag newInstance() {
        OpenChatFrag fragment = new OpenChatFrag();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_open_chat_frag, container, false);

        favouritesList = (ListView) view.findViewById(R.id.favourites_list);
        Set<String> favMAC = FavouriteUsers.getInstance().getFavMacAddrs();
        List<String> favNames = new ArrayList<>();
        for(String mac : favMAC){
            favNames.add(ConnectionsList.getInstance().getNameFromMac(mac));
        }
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, favNames);
        favouritesList.setAdapter(adapter);

        Button refresh = (Button) view.findViewById(R.id.refresh_fav);

        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                List<String> favNames = new ArrayList<>();
                for(String mac : FavouriteUsers.getInstance().getFavMacAddrs()){
                    favNames.add(ConnectionsList.getInstance().getNameFromMac(mac));
                }
                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, favNames);
                favouritesList.setAdapter(adapter);

            }
        });

        return view;
    }

}