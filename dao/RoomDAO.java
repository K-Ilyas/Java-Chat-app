package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;

import classes.MessageRoom;
import classes.Room;
import classes.UserInformation;

public class RoomDAO extends DAO<Room> {

  public RoomDAO(Connection conn) {
    super(conn);
  }

  @Override
  public boolean create(Room obj) {
    String uuid = java.util.UUID.randomUUID().toString();
    PreparedStatement statement = null;
    try {

      String query = "INSERT INTO room (uuid_room,roomname,image,uuid_owner) VALUES (?,?,?,?)";
      statement = this.connect.prepareStatement(query);
      statement.setString(1, uuid);
      statement.setString(2, obj.getRoomname());
      statement.setString(3, obj.getImage());
      statement.setString(4, obj.getUuid_owner());
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    } finally {
      try {
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }

    }
    return true;
  }

  // crate room with a list of users in it
  public boolean createRoomWithUsers(Room obj, UserInformation admin, LinkedList<UserInformation> users) {
    PreparedStatement statement = null;
    String uuid = java.util.UUID.randomUUID().toString();

    try {
      System.out.println("hhhhhhh" + obj.getRoomname() + " " + obj.getImage() + " " + obj.getUuid_owner());
      if (!this.roomExists(obj.getRoomname())) {
        System.out.println("hhhhhhh" + obj.getRoomname() + " " + obj.getImage() + " " + obj.getUuid_owner());

        String query = "INSERT INTO room (uuid_room,roomname,image,uuid_owner) VALUES (?,?,?,?)";
        statement = this.connect.prepareStatement(query);
        statement.setString(1, uuid);
        statement.setString(2, obj.getRoomname());
        statement.setString(3, obj.getImage());
        statement.setString(4, obj.getUuid_owner());
        statement.executeUpdate();
        // get the uuid of the room

        // add all users to the room

        for (UserInformation user : users) {
          if (!user.getUuid().equals(admin.getUuid())) {
            query = "INSERT INTO connected (uuid_user,uuid_room) VALUES (?,?)";
            statement = this.connect.prepareStatement(query);
            statement.setString(1, user.getUuid());
            statement.setString(2, uuid);
            statement.executeUpdate();
          }
        }

        query = "INSERT INTO connected (uuid_user,uuid_room) VALUES (?,?)";
        statement = this.connect.prepareStatement(query);
        statement.setString(1, admin.getUuid());
        statement.setString(2, uuid);
        statement.executeUpdate();
        return true;
      } else {
        return false;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    } finally {
      try {
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }

    }
  }

  public Map<Room, Map<UserInformation, LinkedList<UserInformation>>> getRoomsAndUsers() {
    Map<Room, Map<UserInformation, LinkedList<UserInformation>>> roomsAndUsers = new HashMap<>();
    String roomQuery = "SELECT * FROM room";
    try (PreparedStatement roomStatement = connect.prepareStatement(roomQuery);
        ResultSet roomResultSet = roomStatement.executeQuery()) {
      while (roomResultSet.next()) {
        Room room = new Room(
            roomResultSet.getString("uuid_room"),
            roomResultSet.getString("roomname"),
            roomResultSet.getString("image"),
            roomResultSet.getString("uuid_owner"));

        String ownerQuery = "SELECT * FROM user WHERE uuid_user = ?";
        try (PreparedStatement ownerStatement = connect.prepareStatement(ownerQuery)) {
          ownerStatement.setString(1, room.getUuid_owner());
          ResultSet ownerResultSet = ownerStatement.executeQuery();
          if (ownerResultSet.next()) {
            UserInformation owner = new UserInformation(
                ownerResultSet.getString("uuid_user"),
                ownerResultSet.getString("username"),
                "",
                ownerResultSet.getString("email"),
                ownerResultSet.getString("image"),
                ownerResultSet.getBoolean("isadmin"));

            LinkedList<UserInformation> users = new LinkedList<>();
            String connectedUsersQuery = "SELECT * FROM connected INNER JOIN user ON user.uuid_user = connected.uuid_user WHERE uuid_room = ?";
            try (PreparedStatement connectedUsersStatement = connect.prepareStatement(connectedUsersQuery)) {
              connectedUsersStatement.setString(1, room.getUuid_room());
              ResultSet connectedUsersResultSet = connectedUsersStatement.executeQuery();
              while (connectedUsersResultSet.next()) {
                UserInformation connectedUser = new UserInformation(
                    connectedUsersResultSet.getString("uuid_user"),
                    connectedUsersResultSet.getString("username"),
                    "",
                    connectedUsersResultSet.getString("email"),
                    connectedUsersResultSet.getString("image"),
                    connectedUsersResultSet.getBoolean("isadmin"));
                users.add(connectedUser);
              }
            }
            Map<UserInformation, LinkedList<UserInformation>> usersMap = new HashMap<>();
            usersMap.put(owner, users);
            roomsAndUsers.put(room, usersMap);
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return roomsAndUsers;
  }

  public Map<Room, Map<UserInformation, LinkedList<UserInformation>>> getRoomsYouArePart(UserInformation user) {
    Map<Room, Map<UserInformation, LinkedList<UserInformation>>> roomsAndUsers = new HashMap<>();
    String roomQuery = "SELECT * FROM room INNER JOIN connected ON room.uuid_room = connected.uuid_room WHERE connected.uuid_user = ?";
    try (PreparedStatement roomStatement = connect.prepareStatement(roomQuery)) {
      roomStatement.setString(1, user.getUuid());
      ResultSet roomResultSet = roomStatement.executeQuery();
      while (roomResultSet.next()) {
        Room room = new Room(
            roomResultSet.getString("uuid_room"),
            roomResultSet.getString("roomname"),
            roomResultSet.getString("image"),
            roomResultSet.getString("uuid_owner"));

        String ownerQuery = "SELECT * FROM user WHERE uuid_user = ?";
        try (PreparedStatement ownerStatement = connect.prepareStatement(ownerQuery)) {
          ownerStatement.setString(1, room.getUuid_owner());
          ResultSet ownerResultSet = ownerStatement.executeQuery();
          if (ownerResultSet.next()) {
            UserInformation owner = new UserInformation(
                ownerResultSet.getString("uuid_user"),
                ownerResultSet.getString("username"),
                "",
                ownerResultSet.getString("email"),
                ownerResultSet.getString("image"),
                ownerResultSet.getBoolean("isadmin"));

            LinkedList<UserInformation> users = new LinkedList<>();
            String connectedUsersQuery = "SELECT * FROM connected INNER JOIN user ON user.uuid_user = connected.uuid_user WHERE uuid_room = ? and connected.uuid_user <> ?";
            try (PreparedStatement connectedUsersStatement = connect.prepareStatement(connectedUsersQuery)) {
              connectedUsersStatement.setString(1, room.getUuid_room());
              connectedUsersStatement.setString(2, user.getUuid());
              ResultSet connectedUsersResultSet = connectedUsersStatement.executeQuery();
              while (connectedUsersResultSet.next()) {
                UserInformation connectedUser = new UserInformation(
                    connectedUsersResultSet.getString("uuid_user"),
                    connectedUsersResultSet.getString("username"),
                    "",
                    connectedUsersResultSet.getString("email"),
                    connectedUsersResultSet.getString("image"),
                    connectedUsersResultSet.getBoolean("isadmin"));
                users.add(connectedUser);
              }
            }
            Map<UserInformation, LinkedList<UserInformation>> usersMap = new HashMap<>();
            usersMap.put(owner, users);
            roomsAndUsers.put(room, usersMap);
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return roomsAndUsers;
  }

  // send a message in a room

  public boolean sendMessageInRoom(UserInformation user, MessageRoom messageRoom) throws SQLException {
    String query = "INSERT INTO messageroom (uuid_room,uuid_user,message,message_date) VALUES (?,?,?,?)";
    try (PreparedStatement statement = this.connect.prepareStatement(query)) {

      statement.setString(1, messageRoom.getUuidRoom());
      statement.setString(2, user.getUuid());
      statement.setString(3, messageRoom.getMessage());
      statement.setString(4, messageRoom.getMessageDate());

      statement.executeUpdate();

      // Update messageRoom

      // get the id of the inserted message

      String query2 = "SELECT * FROM messageroom WHERE uuid_user = ? AND uuid_room = ? ORDER BY message_id DESC LIMIT 1";
      try (PreparedStatement statement2 = this.connect.prepareStatement(query2)) {
        statement2.setString(1, user.getUuid());
        statement2.setString(2, messageRoom.getUuidRoom());

        try (ResultSet resultSet = statement2.executeQuery()) {
          if (resultSet.next()) {
            messageRoom.setDeleted(resultSet.getBoolean("isDeleted"));
            messageRoom.setMessageDate(resultSet.getString("message_date"));
            messageRoom.setUuidUser(resultSet.getString("uuid_user"));
            messageRoom.setUuidRoom(resultSet.getString("uuid_room"));
            messageRoom.setMessage(resultSet.getString("message"));
            // System.out.println("HHHHHH it is ME HHHHHHHHHHHHHHHHHHHHh");

          }
        }

        return true;
      } catch (SQLException e) {
        e.printStackTrace();
      }

      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  // get all message with the user that sent that message in a room
  public Hashtable<UserInformation, MessageRoom> getMessagesInRoom(String uuid_room) {
    Hashtable<UserInformation, MessageRoom> messages = new Hashtable<UserInformation, MessageRoom>();
    try {
      String query = "SELECT * FROM messageroom WHERE uuid_room = ?";
      PreparedStatement statement = this.connect.prepareStatement(query);
      statement.setString(1, uuid_room);
      ResultSet result = statement.executeQuery();
      while (result.next()) {
        String uuid_user = result.getString("uuid_user");
        UserInformation user = new UserDAO(this.connect).find(uuid_user);
        MessageRoom message = new MessageRoom(
            result.getString("uuid_room"),
            result.getString("uuid_user"),
            result.getString("message"),
            result.getString("message_date"),
            result.getBoolean("isDeleted"));
        messages.put(user, message);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return messages;
  }

  // get all users connected in a room
  public LinkedList<UserInformation> getUsersInRoom(String uuid_room) {
    LinkedList<UserInformation> users = new LinkedList<UserInformation>();
    try {
      String query = "SELECT * FROM connected WHERE uuid_room = ?";
      PreparedStatement statement = this.connect.prepareStatement(query);
      statement.setString(1, uuid_room);
      ResultSet result = statement.executeQuery();
      while (result.next()) {
        String uuid_user = result.getString("uuid_user");
        UserInformation user = new UserDAO(this.connect).find(uuid_user);
        users.add(user);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return users;
  }

  @Override
  public boolean delete(Room obj) {
    PreparedStatement statement = null;

    try {
      // Check if room exists before deleting that is not possible to delete romm does
      // not exist in interface but we essure that
      if (!roomExists(obj.getUuid_room())) {
        System.out.println("Room does not exist.");
        return false;
      }

      String query = "DELETE FROM room WHERE uuid_room = ?";
      statement = this.connect.prepareStatement(query);
      statement.setString(1, obj.getUuid_room());

      int rowsAffected = statement.executeUpdate();
      if (rowsAffected > 0) {
        System.out.println("Room deleted successfully.");
        return true;
      } else {
        System.out.println("Failed to delete Room.");
        return false;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean update(Room obj) {
    PreparedStatement statement = null;
    try {
      if (!roomExists(obj.getUuid_room())) {
        String query = "UPDATE room SET roomname = ?, image = ? WHERE uuid_room = ?";
        statement = this.connect.prepareStatement(query);
        statement.setString(1, obj.getRoomname());
        statement.setString(2, obj.getImage());
        statement.setString(3, obj.getUuid_room());
        statement.executeUpdate();
      } else {
        return false;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    } finally {
      try {
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    return true;
  }

  // information about room
  @Override
  public Room find(String uuid) {
    Room room = new Room();
    try {
      ResultSet result = this.connect.createStatement(
          ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_READ_ONLY).executeQuery("SELECT * FROM room WHERE uuid_room = " + uuid);
      if (result.first())
        room = new Room(
            result.getString("uuid_room"),
            result.getString("roomname"),
            result.getString("image"),

            result.getString("uuid_owner"));
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return room;
  }

  public boolean roomExists(String uuid_room) {
    try {
      // spécifient que le ResultSet sera insensible aux modifications et en lecture
      // seule.
      ResultSet result = this.connect.createStatement(
          ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_READ_ONLY)
          .executeQuery(String.format("SELECT * FROM room WHERE uuid_room = '%s'", uuid_room));
      // Cette ligne vérifie si le ResultSet contient au moins une ligne de résultat.
      if (result.first())
        return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  // all room exesting
  public LinkedList<Room> findAllRoom() {
    LinkedList<Room> list = new LinkedList<Room>();
    try {
      ResultSet result = this.connect.createStatement(
          ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_READ_ONLY).executeQuery("SELECT * FROM room");
      while (result.next())
        list.add(new Room(
            result.getString("uuid_room"),
            result.getString("roomname"),
            result.getString("image"),
            result.getString("uuid_owner")));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }
}
