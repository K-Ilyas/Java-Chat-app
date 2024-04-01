import java.util.Hashtable;
import java.util.LinkedList;

import classes.FriendRequest;
import classes.UserInformation;
import client.SocketClient;

public class Examplle {
    public static void main(String[] args) {

        SocketClient client = new SocketClient();
        // client.startConversation();

        UserInformation user = null;

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        user = client.login("sofia", "sofia");

        System.out.println(user.getPseudo() + " - " + user.getUuid());

        LinkedList<UserInformation> userList = client.getUsers();

        // print all users
        for (UserInformation userInformation : userList) {
            System.out.println(userInformation.getPseudo() + " - " + userInformation.getUuid());
        }

        client.sendMessage("how are you sofia", user, userList.get(0));

        client.brodcastMessage("OK thanks", user);

        Hashtable<UserInformation, FriendRequest> friendRequests = client.getSentFriendReuests(user);


        if (friendRequests == null)
            System.out.println("No friend requests sent");
        else
            for (UserInformation userInformation : friendRequests.keySet()) {
                System.out.println(userInformation);
            }

        // get friend requests 
        FriendRequest recivedFriendRequest = friendRequests.get(userList.get(0));

        client.sentInvitation(user, userList.get(0));

        for (UserInformation userInformation : friendRequests.keySet()) {
            recivedFriendRequest = friendRequests.get(userInformation);
        }

        LinkedList<UserInformation> friends = client.getFriends(user);
        System.out.println("Friends : ");

        for (UserInformation userInformation : friends) {
            System.out.println(userInformation.getPseudo() + " - " + userInformation.getUuid());
        }

        client.acceptInvitation(user,recivedFriendRequest, "accepted");

    }
}
