package client;

import java.net.Socket;

import classes.FriendRequest;
import classes.MessageTo;
import classes.UserInformation;
import classes.MessageRoom;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class ReciveNotification implements Runnable {

    private Socket soc = null;
    private boolean shouldRun = true;

    public ReciveNotification(Socket soc) {
        this.soc = soc;
    }

    public void run() {

        try (InputStream in = soc.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(in);
                DataInputStream din = new DataInputStream(in)) {
            int status = 0;

            while (this.shouldRun) {
                System.out.println("Waiting for notification");
                status = din.readInt();
                if (status == 1) {
                    System.out.println("Notification recived : message");
                    UserInformation sender = (UserInformation) ois.readObject();
                    System.out.println("Recived message from " + sender.getUuid());
                    MessageTo message = (MessageTo) ois.readObject();
                    System.out.println("Recived message from " + message);
                } else if (status == 2) {
                    System.out.println("Notification recived : friend request");
                    UserInformation sender = (UserInformation) ois.readObject();
                    System.out.println("Recived friend request from " + sender.getUuid());
                    FriendRequest friendRequest = (FriendRequest) ois.readObject();
                    System.out.println("Recived friend request from " + friendRequest);
                } else if (status == 3) {
                    // recive a message in room
                    System.out.println("Notification recived : message in room");
                    UserInformation sender = (UserInformation) ois.readObject();
                    System.out.println("Recived message from " + sender.getUuid());
                    MessageRoom message = (MessageRoom) ois.readObject();
                    System.out.println("Recived message from " + message);
                } else if (status == 4) {

                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setShouldRun(boolean shouldRun) {
        synchronized (this) {
            this.shouldRun = shouldRun;
        }
    }

}