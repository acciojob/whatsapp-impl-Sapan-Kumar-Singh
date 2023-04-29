package com.driver;

import java.util.List;
import java.util.Optional;

public class WhatsappService {

    WhatsappRepository whatsappRepository=new WhatsappRepository();
    public String createUser(String name, String mobile) throws Exception {

         Optional<Boolean> mobOpt=whatsappRepository.ContainsMobile(mobile);
         if(mobOpt.isPresent()){
             throw new Exception("User already exists");
         }
         whatsappRepository.createUser(name,mobile);
         return "SUCCESS";
    }

    public Group createGroup(List<User> users) {

        if(users.size()==2){
            return whatsappRepository.createNewChat(users);
        }
        return whatsappRepository.createGroup(users);
    }

    public int createMessage(String content) {
        return whatsappRepository.createMessage(content);
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {

        Optional<List<User>>groupOpt=whatsappRepository.isGroupFound(group);
         if(groupOpt.isEmpty()){
             throw new Exception("Group does not exist");
         }
         List<User>users=groupOpt.get();
         if(!users.contains(sender)){
             throw new Exception("You are not allowed to send message");
         }
         return whatsappRepository.sendMessage(message,sender,group);
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {

        Optional<List<User>>groupOpt=whatsappRepository.isGroupFound(group);
        if(groupOpt.isEmpty()){
            throw new Exception("Group does not exist");
        }
         Optional<Boolean>adminOpt=whatsappRepository.approverIsAdmin(approver,group);
        if(adminOpt.isEmpty()){
            throw new Exception("Approver does not have rights");
        }
        List<User>users=groupOpt.get();
        if(!users.contains(user)){
            throw new Exception("User is not a participant");
        }
         whatsappRepository.changeAdmin(user,group);
        return "SUCCESS";
    }
}
