package com.driver;

import java.util.*;

import net.bytebuddy.dynamic.DynamicType;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
  //  private HashSet<String> userMobile;
    private HashMap<String,User>userMap;

    private HashMap<Integer,Message>messageMap;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        //this.userMobile = new HashSet<>();
        this.userMap=new HashMap<>();
        this.messageMap=new HashMap<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public HashMap<Group, List<User>> getGroupUserMap() {
        return groupUserMap;
    }

    public void setGroupUserMap(HashMap<Group, List<User>> groupUserMap) {
        this.groupUserMap = groupUserMap;
    }

    public HashMap<Group, List<Message>> getGroupMessageMap() {
        return groupMessageMap;
    }

    public void setGroupMessageMap(HashMap<Group, List<Message>> groupMessageMap) {
        this.groupMessageMap = groupMessageMap;
    }

    public HashMap<Message, User> getSenderMap() {
        return senderMap;
    }

    public void setSenderMap(HashMap<Message, User> senderMap) {
        this.senderMap = senderMap;
    }

    public HashMap<Group, User> getAdminMap() {
        return adminMap;
    }

    public void setAdminMap(HashMap<Group, User> adminMap) {
        this.adminMap =adminMap;
    }

    public HashMap<String, User> getUserMap() {
        return userMap;
    }

    public void setUserMap(HashMap<String, User> userMap) {
        this.userMap = userMap;
    }

    public HashMap<Integer, Message> getMessageMap() {
        return messageMap;
    }

    public void setMessageMap(HashMap<Integer, Message> messageMap) {
        this.messageMap = messageMap;
    }

    public int getCustomGroupCount() {
        return customGroupCount;
    }

    public void setCustomGroupCount(int customGroupCount) {
        this.customGroupCount = customGroupCount;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public Optional<Boolean> ContainsMobile(String mobile) {
          if(userMap.containsKey(mobile)){
               return Optional.of(true);
          }
          return Optional.empty();
    }
    public void createUser(String name, String mobile) {
        User user=new User(name,mobile);

        userMap.put(mobile,user);
    }

    public Group createNewChat(List<User> users) {
          Group newChat=new Group(users.get(1).getName(),2);
          this.groupUserMap.put(newChat,users);
          return newChat;
    }

    public Group createGroup(List<User> users) {
            this.customGroupCount++;
            String groupName="Group "+this.customGroupCount;
            Group newGroup=new Group(groupName,users.size());
            this.groupUserMap.put(newGroup,users);
            this.adminMap.put(newGroup,users.get(0));
            return newGroup;
    }


    public int createMessage(String content) {
           this.messageId++;
           Message message=new Message(this.messageId,content);
           this.messageMap.put(this.messageId,message);
           return this.messageId;
    }

    public Optional<List<User>> isGroupFound(Group group) {
        if(this.groupUserMap.containsKey(group)){
            return Optional.of(this.groupUserMap.get(group));
        }
        return Optional.empty();
    }


    public int sendMessage(Message message, User sender, Group group) {
        List<Message>oldMessage=this.groupMessageMap.get(group);
        oldMessage.add(message);
        this.groupMessageMap.put(group,oldMessage);
        this.senderMap.put(message,sender);
        return oldMessage.size();
    }

    public Optional<Boolean> approverIsAdmin(User approver, Group group) {

        if(this.adminMap.containsKey(group) && this.adminMap.get(group).equals(approver)){
            return Optional.of(true);
        }
        return Optional.empty();
    }

    public void changeAdmin(User user, Group group) {
        this.adminMap.put(group,user);
    }

    public Optional<Boolean> findUser(User user) {
        if(this.userMap.containsValue(user)){
            return Optional.of(true);
        }

        return Optional.empty();
    }

    public Optional<Boolean> isAdmin(User user) {
        if(this.adminMap.containsValue(user)){
            return Optional.of(true);
        }

        return Optional.empty();
    }


    public void removeUser(User user) {



        for(Group key : groupUserMap.keySet()){
            List<User>userList=groupUserMap.get(key);
            for(User val :userList){
                if(val.equals(user)){
                    userList.remove(user);
                }
            }
            groupUserMap.put(key,userList);
        }
        for(Message key : this.senderMap.keySet()){
            if(this.senderMap.get(key).equals(user)){
                this.senderMap.remove(key);
            }
        }

        for(String key : this.userMap.keySet()){
            if(this.userMap.get(key).equals(user)){
                this.userMap.remove(key);
            }
        }

    }


}
