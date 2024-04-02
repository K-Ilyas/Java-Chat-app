import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;

import classes.FriendRequest;
import classes.MessageRoom;
import classes.Room;
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
        if (friendRequests != null) {

            // get friend requests
            FriendRequest recivedFriendRequest = friendRequests.get(userList.get(0));

            client.sentInvitation(user, userList.get(0));

            for (UserInformation userInformation : friendRequests.keySet()) {
                recivedFriendRequest = friendRequests.get(userInformation);
            }
            client.acceptInvitation(user, recivedFriendRequest, "accepted");
        }

        LinkedList<UserInformation> friends = client.getFriends(user);
        System.out.println("Friends : ");

        for (UserInformation userInformation : friends) {
            System.out.println(userInformation.getPseudo() + " - " + userInformation.getUuid());
        }

        client.createRoomWithUsers(user, userList, "jimaa", "");

        Map<Room, Map<UserInformation, LinkedList<UserInformation>>> rooms = client.getRoomsWithUsers(user);

        Room room_tosent = null;
        if (rooms == null)
            System.out.println("No rooms");
        else
            for (Room room : rooms.keySet()) {

                room_tosent = room;
                Map<UserInformation, LinkedList<UserInformation>> users = rooms.get(room);

                for (UserInformation userInformation : users.keySet()) {
                    System.out.println(userInformation.getPseudo());
                    LinkedList<UserInformation> usersInRoom = users.get(userInformation);
                    for (UserInformation userInformation2 : usersInRoom) {
                        System.out.println(userInformation2.getPseudo());
                    }
                }

            }

        client.sendMessageToRoom(user, room_tosent, "hello");

        Hashtable<UserInformation, MessageRoom> messages = client.getMessagesFromRoom(user, room_tosent);

        if (messages == null)
            System.out.println("No messages");
        else
            for (UserInformation userInformation : messages.keySet()) {
                System.out.println(userInformation.getPseudo());
                System.out.println(messages.get(userInformation).getMessage());
            }

        LinkedList<UserInformation> notFrineds = client.getNotFriends(user);
        System.out.println("Not Friends : ");

        if (notFrineds == null)
            System.out.println("No not friends");
        else
            for (UserInformation userInformation : notFrineds) {
                System.out.println(userInformation.getPseudo() + " - " + userInformation.getUuid());
            }

        
        client.startVoiceCall(user, userList.get(0));
    }
}
