package ca.toronto.csc301.chat;

import java.util.HashSet;
import java.util.Set;
import java.io.Serializable;

/**
 * Created by Priyen on 2015-11-19.
 */
public class Event implements Serializable{
    /**
     * type:
     *  int 1 = broadcast msg, visibility to only target clients
     *  int 2 = request copy of receivers' clients
     *  int 3 = recieving type 2 request, in data value
     */
    private int type;
    private String message;
    private String receiver;
    private String sender;
    private HashSet<String> data = new HashSet<String>();
    //who can see the MESSAGE?
    private HashSet<String> allowedClients = new HashSet<String>();//mac addrs'
    //who has already seen/received this event?
    private HashSet<String> pendingTargets = new HashSet<String>();

    public void setData(HashSet<String> s)
    {
        this.data = s;
    }

    public HashSet<String> getData(){
        return this.data;
    }

    public void setSender(String s){
        this.sender = s;
    }

    public String getSender(){
        return this.sender;
    }

    public void setType(int s){
        this.type = s;
    }

    public void setMessage(String s){
        this.message = s;
    }

    public void addTarget(String s){
        pendingTargets.add(s);
    }

    public void removeTarget(String s){
        pendingTargets.remove(s);
    }

    public Set<String> getPendingTargets(){
        return new HashSet<String>(this.pendingTargets);
    }

    public int getType(){
        return this.type;
    }

    public String getMessage(){
        return this.message;
    }

    //lets this client see the msg
    public void allowClient(String s){
        allowedClients.add(s);
    }
}
