import java.util.Hashtable;
import java.util.LinkedList;

import classes.FriendRequest;
import classes.UserInformation;
import client.SocketClient;

public class Example {

    public static void main(String[] args) {

        SocketClient client = new SocketClient();
        // client.startConversation();z
        UserInformation user = null;

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        user = client.login("Amine", "Amine");

        System.out.println(user.getPseudo() + " - " + user.getUuid());

        LinkedList<UserInformation> userList = client.getUsers();

        // print all users
        for (UserInformation userInformation : userList) {
            System.out.println(userInformation.getPseudo() + " - " + userInformation.getUuid());
        }

        Hashtable<UserInformation, FriendRequest> friendRequests = client.getFriendRequests(user);

        FriendRequest recivedFriendRequest = null;
        if (friendRequests != null ) {

        for (UserInformation userInformation : friendRequests.keySet()) {
            if (userInformation.getPseudo().equals("sofia")) {
                recivedFriendRequest = friendRequests.get(userInformation);
            }
            System.out.println(userInformation.getPseudo() + " - " + userInformation.getUuid());
        }

        // accept invitation
            System.out.println(recivedFriendRequest);
            System.out.println(user);
            client.acceptInvitation(user, recivedFriendRequest, "accepted");
        }

        LinkedList<UserInformation> friends = client.getFriends(user);
        System.out.println("Friends : ");

        for (UserInformation userInformation : friends) {
            System.out.println(userInformation.getPseudo() + " - " + userInformation.getUuid());
        }

        

        // client.brodcastMessage("wow", user);
    }

}
