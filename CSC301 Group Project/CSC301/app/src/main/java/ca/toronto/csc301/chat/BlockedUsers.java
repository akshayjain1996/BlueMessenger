package ca.toronto.csc301.chat;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by akshay on 22/11/15.
 */
public class BlockedUsers {

    Set<String> blockedUsersMAC;
    static BlockedUsers instance;

    private BlockedUsers(){
        blockedUsersMAC = new HashSet<>();
    }

    static BlockedUsers getInstance(){
        if(getInstance() == null){
            instance = new BlockedUsers();
        }
        return instance;
    }

    public void addUserToBlackList(String mac){
        blockedUsersMAC.add(mac);
    }

    public Set<String> getBlockedUsersMAC(){
        return blockedUsersMAC;
    }
}
