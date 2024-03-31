import java.util.LinkedList;

import classes.UserInformation;
import client.SocketClient;

public class Example {

    public static void main(String[] args) {
        SocketClient client = new SocketClient();
        // client.startConversation();

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


        client.brodcastMessage("wow", user);
    }

}
