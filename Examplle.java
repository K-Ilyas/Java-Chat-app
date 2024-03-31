import java.util.LinkedList;

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

        client.logOut(user);
    }
}
