package Server;

import model.*;
import DAO.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



import java.util.Map.Entry;

public class ServerClientHandler implements Runnable {

    private Socket client = null;
    private Server socketServer = null;
    private Socket clientMessage = null;

    private static int index = 1;

    public ServerClientHandler(Socket client, Socket clientMessage, Server socketServer) {
        this.client = client;
        this.socketServer = socketServer;
        this.clientMessage = clientMessage;
    }

    public static int getIndex() {
        index++;
        return index - 1;
    }

    public void run() {
        try (OutputStream out = this.client.getOutputStream();
                InputStream in = this.client.getInputStream();
                DataInputStream dis = new DataInputStream(in);
                DataOutputStream dos = new DataOutputStream(out);
                ObjectInputStream ois = new ObjectInputStream(in);
                ObjectOutputStream oos = new ObjectOutputStream(out);) {

            int i = 0;
            boolean isTrue = true;
            UserDAO user_orm = new UserDAO(this.socketServer.getConnect());
            UserInformation userInformation = null;
            UserInformation friend = null;
            MessageToDAO message_orm = new MessageToDAO(this.socketServer.getConnect());
            AmisDAO amis_orm = new AmisDAO(this.socketServer.getConnect());
            RoomDAO room_orm = new RoomDAO(this.socketServer.getConnect());

            LinkedList<MessageTo> messages = null;
            LinkedList<Amis> amis = null;

            String message = "";

            while (isTrue) {
                i = 0;
                i = dis.readInt();

                switch (i) {
                    case 1:
                        System.out.println("SERVER : YOU CAN LOG IN");
                        userInformation = null;
                        try {
                            userInformation = (UserInformation) ois.readObject();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                        userInformation = user_orm.findAndCheckPassword(userInformation);
                        System.out.println(userInformation);

                        if (!userInformation.getUuid().equals("")) {

                            if (!this.socketServer.isLogedInOrInit(userInformation, this.client, this.clientMessage)) {
                                this.socketServer.addUser(userInformation, client, this.clientMessage);
                            }

                            System.out.println("user connected :::" + userInformation.getUuid());
                            dos.writeInt(1);
                            dos.writeUTF("SERVER : YOUR CONNEXION HAS ESTABLISHED SUCCESFULLY");
                            dos.flush();
                            oos.writeObject(userInformation);
                            oos.flush();

                        } else {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER :YOUR OBJECT SEND IS NULL !!!");
                            dos.flush();
                            System.out.println("SERVER : YOU HAVE AN ERROR IN YOUR CONNECTION FAILD");
                        }

                        break;
                    case 2:

                        System.out.println("SERVER : YOU CAN SING IN");
                        System.out.println(dis.readUTF());
                        userInformation = null;
                        try {
                            userInformation = (UserInformation) ois.readObject();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        System.out.println(dis.readUTF());

                        if (userInformation != null) {

                            if (user_orm.create(userInformation)) {
                                System.out.println("user created :::" + userInformation.getUuid());
                                // this.socketServer.addUser(userInformation, this.client);
                                dos.writeInt(1);
                                dos.writeUTF("SERVER : YOUR REGISTARTION HAS ESTABLISHED SUCCESFULY");
                                dos.flush();
                                oos.writeObject(userInformation);
                                oos.flush();
                            } else {
                                dos.writeInt(0);
                                dos.writeUTF(
                                        "SERVER :YOUR PSEUDO IS NOT SUPORTABLE OR PASSWORD NOT FORT PLEASE RETAIT");
                                dos.flush();
                            }
                        } else {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER :YOUR OBJECT SEND IS NULL !!!");
                            dos.flush();
                            System.out.println("SERVER : YOU HAVE AN ERROR IN YOUR CONNECTION FAILD");
                        }
                        break;

                    case 3:
                        System.out.println(userInformation.getUuid());
                        LinkedList<UserInformation> otherUsers = user_orm.findAll(userInformation.getUuid());
                        oos.writeObject(otherUsers);
                        oos.flush();
                        break;

                    case 4:

                        friend = (UserInformation) ois.readObject();
                        userInformation = (UserInformation) ois.readObject();
                        messages = message_orm.findAll(userInformation.getUuid(), friend.getUuid());
                        oos.writeObject(messages == null ? new LinkedList<MessageTo>() : messages);
                        oos.flush();

                        break;

                    case 5:

                        message = dis.readUTF();
                        friend = (UserInformation) ois.readObject();
                        userInformation = (UserInformation) ois.readObject();

                        Socket reciver = this.socketServer.findUserSocketMessage(friend.getUuid());

                        MessageTo messageTo = new MessageTo(userInformation.getUuid(), friend.getUuid(), message,
                                LocalDateTime.now().toString(), false);

                        if (message_orm.create(messageTo)) {

                            OutputStream outSender = this.clientMessage.getOutputStream();
                            ObjectOutputStream soos = new ObjectOutputStream(outSender);
                            DataOutputStream dosSender = new DataOutputStream(outSender);

                            dosSender.writeInt(1);
                            dosSender.flush();
                            soos.writeObject(userInformation);
                            soos.flush();
                            soos.writeObject(messageTo);
                            soos.flush();

                            if (reciver != null) {
                                try {
                                    OutputStream outRecive = reciver.getOutputStream();
                                    ObjectOutputStream oosRecive = new ObjectOutputStream(outRecive);

                                    DataOutputStream dosRecive = new DataOutputStream(outRecive);
                                    dosRecive.writeInt(1);
                                    dosRecive.flush();
                                    oosRecive.writeObject(userInformation);
                                    oosRecive.flush();
                                    oosRecive.writeObject(messageTo);
                                    oosRecive.flush();
                                    isTrue = true;

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            dos.writeBoolean(true);
                            dos.flush();

                        } else {
                            dos.writeBoolean(false);
                            dos.flush();
                        }
                        break;
                    case 6:
                        userInformation = (UserInformation) ois.readObject();
                        if (userInformation != null) {
                            if (this.socketServer.findUserSocket(userInformation.getUuid()) != null) {
                                dos.writeInt(1);
                                dos.writeUTF("SERVER : YOUR LOG OUT SUCCESFLY");
                                dos.flush();
                                isTrue = false;
                                this.socketServer.logOut(userInformation);
                            } else {
                                dos.writeInt(0);
                                dos.writeUTF("SERVER :SOMETHING WENT WRONG PLEASE RETRY!!!");
                                dos.flush();
                            }
                        } else {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER : YOUR SENDED OBJECT IS NULL !!!");
                            dos.flush();
                        }
                        break;

                    case 7:
                        // to prodcast a message
                        message = dis.readUTF();
                        userInformation = (UserInformation) ois.readObject();
                        String time = LocalDateTime.now().toString();
                        final String messageToUser = message;
                        final UserInformation finalUserInformation = userInformation;
                        LinkedList<MessageTo> messageToAllUsers = new LinkedList<MessageTo>();
                        LinkedList<UserInformation> usersExceptSender = user_orm
                                .findAll(finalUserInformation.getUuid());

                        for (UserInformation user : usersExceptSender) {
                            messageToAllUsers.add(new MessageTo(finalUserInformation.getUuid(), user.getUuid(),
                                    messageToUser, time, false));
                        }

                        if (message_orm.createAll(messageToAllUsers)) {

                            OutputStream outSender = this.clientMessage.getOutputStream();
                            ObjectOutputStream soos = new ObjectOutputStream(outSender);
                            DataOutputStream dosSender = new DataOutputStream(outSender);

                            dosSender.writeInt(1);
                            dosSender.flush();
                            soos.writeObject(userInformation);
                            soos.flush();

                            soos.writeObject(new MessageTo(userInformation.getUuid(), userInformation.getUuid(),
                                    messageToUser, time, false));
                            soos.flush();

                            Set<Entry<UserInformation, Socket>> users = this.socketServer.getMsg_table().entrySet();
                            for (Entry<UserInformation, Socket> entry : users) {

                                if (entry.getKey().compareTo(userInformation) != 0) {
                                    try {
                                        OutputStream outRecive = entry.getValue().getOutputStream();
                                        ObjectOutputStream oosRecive = new ObjectOutputStream(outRecive);
                                        DataOutputStream dosRecive = new DataOutputStream(outRecive);

                                        dosRecive.writeInt(1);
                                        dosRecive.flush();
                                        oosRecive.writeObject(userInformation);
                                        oosRecive.flush();
                                        oosRecive.writeObject(
                                                new MessageTo(userInformation.getUuid(), entry.getKey().getUuid(),
                                                        messageToUser, time, false));
                                        oosRecive.flush();

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            dos.writeBoolean(true);
                            dos.flush();
                        } else {
                            dos.writeBoolean(false);
                            dos.flush();
                        }
                        break;

                    case 8:
                        // send invitation to a friend to be a friend
                        friend = (UserInformation) ois.readObject();
                        userInformation = (UserInformation) ois.readObject();
                        FriendRequest friendRequest = new FriendRequest();

                        friendRequest.setUuidSender(userInformation.getUuid());
                        friendRequest.setUuidReceiver(friend.getUuid());

                        if (amis_orm.sendInvitation(friendRequest)) {

                            dos.writeInt(1);
                            dos.writeUTF("SERVER : YOUR INVITATION HAS BEEN SENT SUCCESFULLY");
                            dos.flush();
                            oos.writeObject(friendRequest);
                            oos.flush();

                            Socket reciver_notification = this.socketServer.findUserSocketMessage(friend.getUuid());
                            if (reciver_notification != null) {
                                try {
                                    OutputStream outRecive = reciver_notification.getOutputStream();
                                    DataOutputStream dosRecive = new DataOutputStream(outRecive);
                                    ObjectOutputStream oosRecive = new ObjectOutputStream(outRecive);

                                    dosRecive.writeInt(2);
                                    dosRecive.flush();
                                    oosRecive.writeObject(userInformation);
                                    oosRecive.flush();
                                    oosRecive.writeObject(friendRequest);
                                    oosRecive.flush();

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER : SOMETHING WENT WRONG PLEASE RETRY");
                            dos.flush();
                        }
                        break;
                    case 9:

                        // to accept a friend request
                        FriendRequest friendRequestAccept = (FriendRequest) ois.readObject();
                        userInformation = (UserInformation) ois.readObject();

                        System.out.println(friendRequestAccept.getUuidReceiver().equals(userInformation.getUuid()));

                        if (friendRequestAccept.getUuidReceiver().equals(userInformation.getUuid())
                                && amis_orm.acceptDclineInvitation(friendRequestAccept)) {
                            dos.writeInt(1);
                            dos.writeUTF("SERVER : YOUR INVITATION HAS BEEN ACCEPTED SUCCESFULLY");
                            dos.flush();
                            oos.writeObject(friendRequestAccept);
                            oos.flush();

                            Socket reciver_notification = this.socketServer
                                    .findUserSocketMessage(friendRequestAccept.getUuidSender());

                            if (reciver_notification != null) {
                                try {
                                    OutputStream outRecive = reciver_notification.getOutputStream();
                                    DataOutputStream dosRecive = new DataOutputStream(outRecive);
                                    ObjectOutputStream oosRecive = new ObjectOutputStream(outRecive);

                                    dosRecive.writeInt(2);
                                    dosRecive.flush();
                                    oosRecive.writeObject(userInformation);
                                    oosRecive.flush();
                                    oosRecive.writeObject(friendRequestAccept);
                                    oosRecive.flush();

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER : SOMETHING WENT WRONG PLEASE RETRY");
                            dos.flush();
                        }
                        break;

                    case 10:
                        // liste of pending friend request
                        userInformation = (UserInformation) ois.readObject();
                        Hashtable<UserInformation, FriendRequest> friendRequests = amis_orm
                                .userReceivingPendingRequests(userInformation.getUuid());
                        if (friendRequests.size() != 0) {
                            dos.writeInt(1);
                            dos.writeUTF("SERVER : YOU HAVE PENDING REQUESTS");
                            dos.flush();
                            oos.writeObject(friendRequests);
                            oos.flush();
                        } else {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER : YOU HAVE NO PENDING REQUESTS");
                            dos.flush();
                        }
                        break;

                    case 11:
                        // list of pending sended request
                        userInformation = (UserInformation) ois.readObject();
                        Hashtable<UserInformation, FriendRequest> friendRequestsSended = amis_orm
                                .userSendingPendingRequests(userInformation.getUuid());

                        if (friendRequestsSended.size() != 0) {
                            dos.writeInt(1);
                            dos.writeUTF("SERVER : YOU HAVE PENDING REQUESTS SENDED");
                            dos.flush();
                            oos.writeObject(friendRequestsSended);
                            oos.flush();
                        } else {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER : YOU HAVE NO PENDING REQUESTS SENDED");
                            dos.flush();
                        }
                        break;

                    case 12:
                        // print the list of friends
                        userInformation = (UserInformation) ois.readObject();
                        LinkedList<UserInformation> friends = amis_orm.findAllAmis(userInformation.getUuid());
                        if (friends.size() != 0) {
                            dos.writeInt(1);
                            dos.writeUTF("SERVER : YOU HAVE FRIENDS");
                            dos.flush();
                            oos.writeObject(friends);
                            oos.flush();
                        } else {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER : YOU HAVE NO FRIENDS");
                            dos.flush();
                        }
                        break;

                    case 13:
                        // delete a friend
                        userInformation = (UserInformation) ois.readObject();
                        friend = (UserInformation) ois.readObject();
                        Amis relation = new Amis(userInformation.getUuid(), friend.getUuid());

                        if (amis_orm.delete(relation)) {
                            dos.writeUTF("SERVER : YOUR FRIEND HAS BEEN DELETED SUCCESFULLY");
                            dos.writeBoolean(true);
                            dos.flush();

                        } else {
                            dos.writeUTF("SERVER : SOMETHING WENT WRONG PLEASE RETRY");
                            dos.writeBoolean(false);
                            dos.flush();
                        }
                        break;
                    case 14:
                        // create a room with users in it and with admin
                        userInformation = (UserInformation) ois.readObject();
                        Room room = (Room) ois.readObject();

                        @SuppressWarnings("unchecked")
                        LinkedList<UserInformation> users = (LinkedList<UserInformation>) ois.readObject();
                        if (room_orm.createRoomWithUsers(room, userInformation, users)) {
                            dos.writeUTF("SERVER : YOUR ROOM HAS BEEN CREATED SUCCESFULLY");
                            oos.writeObject(oos);
                            dos.writeBoolean(true);
                            dos.flush();
                        } else {
                            dos.writeUTF("SERVER : SOMETHING WENT WRONG PLEASE RETRY");
                            dos.writeBoolean(false);
                            dos.flush();
                        }
                        break;

                    case 15:
                        // get all rooms with users
                        userInformation = (UserInformation) ois.readObject();
                        Map<Room, Map<UserInformation, LinkedList<UserInformation>>> list = room_orm.getRoomsAndUsers();
                        if (list.size() != 0) {
                            dos.writeInt(1);
                            dos.writeUTF("SERVER : YOU HAVE ROOMS");
                            dos.flush();
                            oos.writeObject(list);
                            oos.flush();
                        } else {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER : YOU HAVE NO ROOMS");
                            dos.flush();
                        }
                        break;

                    case 16:
                        // get all rooms with users for a specific user
                        userInformation = (UserInformation) ois.readObject();
                        Map<Room, Map<UserInformation, LinkedList<UserInformation>>> rooms_part = room_orm
                                .getRoomsYouArePart(userInformation);

                        if (rooms_part.size() != 0) {
                            dos.writeInt(1);
                            dos.writeUTF("SERVER : YOU HAVE ROOMS");
                            dos.flush();
                            oos.writeObject(rooms_part);
                            oos.flush();
                        } else {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER : YOU HAVE NO ROOMS");
                            dos.flush();
                        }
                        break;

                    case 17:
                        // send a message to a specific room

                        String messageRoom = dis.readUTF();

                        Room roomMessage = (Room) ois.readObject();

                        userInformation = (UserInformation) ois.readObject();

                        MessageRoom messageRoomObj = new MessageRoom(
                                roomMessage.getUuid_room(), userInformation.getUuid(), messageRoom,
                                LocalDateTime.now().toString(), false);

                        System.out.println(roomMessage.getRoomname() + " - " + roomMessage.getUuid_room() + " - "
                                + messageRoomObj.getMessage());

                        boolean result = room_orm.sendMessageInRoom(userInformation, messageRoomObj);

                        if (result) {

                            dos.writeInt(1);
                            dos.writeUTF("SERVER : YOUR MESSAGE HAS BEEN SENT SUCCESFULLY");
                            dos.flush();

                            LinkedList<UserInformation> usersConnected = room_orm
                                    .getUsersInRoom(roomMessage.getUuid_room());
                            Map<UserInformation, Socket> usersInRoom = this.socketServer.getMsg_table();
                            for (Entry<UserInformation, Socket> entry : usersInRoom.entrySet()) {

                                if (entry.getKey().compareTo(userInformation) == 0
                                        || usersConnected.contains(entry.getKey())) {

                                    System.out.println("SENDING MESSAGE TO : " + entry.getKey().getUuid());
                                    try {
                                        OutputStream outRecive = entry.getValue().getOutputStream();
                                        ObjectOutputStream oosRecive = new ObjectOutputStream(outRecive);
                                        DataOutputStream dosRecive = new DataOutputStream(outRecive);

                                        dosRecive.writeInt(3);
                                        dosRecive.flush();
                                        oosRecive.writeObject(userInformation);
                                        oosRecive.flush();
                                        oosRecive.writeObject(messageRoomObj);
                                        oosRecive.flush();

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        } else {
                            dos.write(0);
                            dos.writeUTF("SERVER : SOMETHING WENT WRONG PLEASE RETRY");
                            dos.flush();
                        }
                        break;

                    case 18:
                        // get all messages in a room
                        userInformation = (UserInformation) ois.readObject();
                        Room roomMessages = (Room) ois.readObject();
                        Hashtable<UserInformation, MessageRoom> messagesRoom = room_orm
                                .getMessagesInRoom(roomMessages.getUuid_room());
                        if (messagesRoom.size() != 0) {
                            dos.writeInt(1);
                            dos.writeUTF("SERVER : YOU HAVE MESSAGES");
                            dos.flush();
                            oos.writeObject(messagesRoom);
                            oos.flush();
                        } else {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER : YOU HAVE NO MESSAGES");
                            dos.flush();
                        }
                        break;

                    // all users that are not friends to a specific user
                    case 19:

                        userInformation = (UserInformation) ois.readObject();
                        LinkedList<UserInformation> otherUsersNotFriends = amis_orm.notFrineds(userInformation);

                        if (otherUsersNotFriends.size() != 0) {
                            dos.writeInt(1);
                            dos.writeUTF("SERVER : YOU HAVE OTHER USERS NOT FRIENDS");
                            dos.flush();
                            oos.writeObject(otherUsersNotFriends);
                            oos.flush();
                        } else {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER : YOU HAVE NO OTHER USERS NOT FRIENDS");
                            dos.flush();
                        }
                        break;
                    case 20:
                        userInformation = (UserInformation) ois.readObject();

                        UserInformation frined = (UserInformation) ois.readObject();

                        break;

                    case 21 :
                        
                        userInformation = (UserInformation) ois.readObject();
                        LinkedList<UserInformation> usersNotInRoom = amis_orm.notFrinedsAndNotSentRequest(userInformation);
                        if (usersNotInRoom.size() != 0) {
                            dos.writeInt(1);
                            dos.writeUTF("SERVER : YOU HAVE USERS NOT IN ROOM");
                            dos.flush();
                            oos.writeObject(usersNotInRoom);
                            oos.flush();
                        } else {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER : YOU HAVE NO USERS NOT IN ROOM");
                            dos.flush();
                        }
                        break;
                    

                    default:
                        System.out.println("You have an error in your cmmande please retrait");
                        break;
                    
                }

            }

            // LinkedList<UserInformation> amis =
            // user_orm.findAll(userInformation.getUuid());
            // oos.writeObject(amis);
            // oos.flush();

            // int result = dis.readInt();
            // System.out.println(result);
            // if (result == 0) {
            // int second = dis.readInt();
            // messages = message_orm.findAll(userInformation.getUuid(),
            // amis.get(second).getUuid());
            // oos.writeObject(messages == null ? new LinkedList<MessageTo>() : messages);
            // oos.flush();
            // }

            // isTrue = true;
            // String message = "", data = "";

            // UserInformation friend = null;

            // while (isTrue) {

            // message = dis.readUTF();
            // System.out.println(message);

            // if (message.toUpperCase().equals("N")) {
            // isTrue = false;
            // userInformation = null;
            // try {
            // friend = (UserInformation) ois.readObject();
            // userInformation = (UserInformation) ois.readObject();
            // } catch (

            // ClassNotFoundException e) {
            // e.printStackTrace();
            // }
            // if (userInformation != null) {

            // if (this.socketServer.logOut(userInformation)) {

            // dos.writeInt(0);
            // dos.writeUTF("SERVER : YOUR LOG OUT SUCCESFLY");
            // dos.flush();
            // System.out.println("SERVER : bye bye" + userInformation.getUuid() + " - "
            // + userInformation.getPseudo() + " !!!!");
            // isTrue = false;

            // } else {
            // dos.writeInt(0);
            // dos.writeUTF("SERVER :SOMETHING WENT WRONG PLEASE RETRY!!!");
            // dos.flush();
            // }
            // } else {
            // dos.writeInt(0);
            // dos.writeUTF("SERVER : YOUR SENDED OBJECT IS NULL !!!");
            // dos.flush();
            // System.out.println("SERVER : YOU HAVE AN ERROR, YOUR CONNECTION FAILD !!");
            // }

            // } else {

            // try {
            // friend = (UserInformation) ois.readObject();
            // userInformation = (UserInformation) ois.readObject();
            // } catch (ClassNotFoundException e) {
            // e.printStackTrace();
            // }

            // if (!message.equals("")) {

            // Socket reciver = this.socketServer.findUserSocket(friend.getUuid());

            // MessageTo messageTo = new MessageTo(userInformation.getUuid(),
            // friend.getUuid(), message,
            // LocalDateTime.now().toString(), false);
            // message_orm.create(messageTo);

            // dos.writeInt(1);
            // dos.writeUTF("you");
            // dos.writeUTF(messageTo.getMessage_date().toString());
            // dos.writeUTF(messageTo.getMessage());
            // dos.flush();

            // if (reciver != null) {
            // try {
            // OutputStream outRecive = reciver.getOutputStream();
            // DataOutputStream dosRecive = new DataOutputStream(outRecive);
            // dosRecive.writeInt(1);
            // dosRecive.writeUTF(userInformation.getPseudo() + " - " +
            // userInformation.getUuid());
            // dosRecive.writeUTF(messageTo.getMessage_date().toString());
            // dosRecive.writeUTF(messageTo.getMessage());
            // dosRecive.flush();
            // isTrue = true;
            // } catch (FileNotFoundException e) {
            // e.printStackTrace();
            // } catch (IOException e) {
            // e.printStackTrace();
            // }

            // }
            // } else {

            // System.out.println("BROADCAST A MESSAGE TO THE SERVER");
            // Set<Entry<UserInformation, Socket>> users =
            // this.socketServer.getCon_table().entrySet();
            // for (Entry<UserInformation, Socket> entry : users) {
            // if (entry.getKey().compareTo(userInformation) != 0) {
            // try {
            // OutputStream outRecive = entry.getValue().getOutputStream();
            // DataOutputStream dosRecive = new DataOutputStream(outRecive);
            // dosRecive.writeInt(1);
            // dosRecive.writeUTF(userInformation.getPseudo());
            // dosRecive.writeUTF(LocalDateTime.now().toString());
            // dosRecive.writeUTF(data);
            // dosRecive.flush();
            // isTrue = true;
            // } catch (FileNotFoundException e) {
            // e.printStackTrace();
            // } catch (IOException e) {
            // e.printStackTrace();
            // }
            // }
            // }
            // }

            // }

            // }

        } catch (

        FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (this.client != null) {
                    this.client.close();
                }
                if (this.clientMessage != null) {
                    this.clientMessage.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}