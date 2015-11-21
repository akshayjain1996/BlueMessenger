package ca.toronto.csc301.chat;

import java.util.HashSet;

/**
 * Created by akshay on 21/11/15.
 */
public class GroupChat {
    private HashSet<String> members;
    private String name;

    public GroupChat(String name, String member){
        this.name = name;
        members = new HashSet<>();
        members.add(member);
    }

    public void addMember(String member){
        members.add(member);
    }

    public HashSet<String> getmembers(){
        return members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
