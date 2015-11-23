package ca.toronto.csc301.chat;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by akshay on 22/11/15.
 */
public class FavouriteUsers {

    private Set<String> favMacAddrs;
    private static FavouriteUsers instance;

    private FavouriteUsers(){
        favMacAddrs = new HashSet<>();
    }

    public static FavouriteUsers getInstance(){
        if(instance == null){
            instance = new FavouriteUsers();
        }
        return instance;
    }

    public Set<String> getFavMacAddrs(){
        return favMacAddrs;
    }

    public void addFav(String mac){
        favMacAddrs.add(mac);
    }
}
