package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.LinkedList;
import classes.Amis;
import classes.FriendRequest;
import classes.UserInformation;

public class AmisDAO extends DAO<Amis> {

  public AmisDAO(Connection conn) {
    super(conn);

  }

  @Override
  public boolean create(Amis obj) {
    PreparedStatement statement = null;
    if (this.findIsFriend(obj.getUuid_user(), obj.getUuid_second_user()) == null) {
      try {
        String query = "INSERT INTO amis (uuid_first_user,uuid_second_user) VALUES (?,?)";
        statement = this.connect.prepareStatement(query);
        statement.setString(1, obj.getUuid_user());
        statement.setString(2, obj.getUuid_second_user());
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
    } else
      return false;
  }

  @Override
  public boolean delete(Amis obj) {
    PreparedStatement statement = null;

    if (this.findIsFriend(obj.getUuid_user(), obj.getUuid_second_user()) != null) {
      try {
        String query = "DELETE FROM amis WHERE (uuid_first_user = ? and uuid_second_user = ?) or (uuid_second_user = ? and uuid_user = ?)";
        statement = this.connect.prepareStatement(query);
        statement.setString(1, obj.getUuid_user());
        statement.setString(2, obj.getUuid_second_user());
        statement.setString(3, obj.getUuid_user());
        statement.setString(4, obj.getUuid_second_user());
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

    } else
      return false;

  }

  @Override
  public boolean update(Amis obj) {
    return false;
  }

  public Amis find(String uuid_user) {
    return null;
  }

  public Amis findIsFriend(String uuid_user, String uuid_second_user) {
    PreparedStatement statement = null;
    ResultSet result = null;
    try {
      String query = "SELECT * FROM amis WHERE (uuid_first_user = ? and uuid_second_user = ?) or(uuid_second_user = ? and uuid_first_user = ?)";
      statement = this.connect.prepareStatement(query);
      statement.setString(1, uuid_user);
      statement.setString(2, uuid_second_user);
      statement.setString(3, uuid_user);
      statement.setString(4, uuid_second_user);
      result = statement.executeQuery();
      if (result.next()) {
        Amis ami = new Amis();
        ami.setUuid_user(result.getString("uuid_first_user"));
        ami.setUuid_second_user(result.getString("uuid_second_user"));
        return ami;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (result != null) {
          result.close();
        }
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public boolean acceptDclineInvitation(FriendRequest request) {
    PreparedStatement statement = null;
    if (this.findInvitationExist(request.getUuidSender(), request.getUuidReceiver(), 2)) {
      try {
        if (request.getRequestStatus().equals("accepted") || request.getRequestStatus().equals("rejected")) {
          String query = "UPDATE friend_request SET request_status = ? WHERE uuid_sender = ? and uuid_reciver = ?";
          statement = this.connect.prepareStatement(query);
          statement.setString(1, request.getRequestStatus());

          statement.setString(2, request.getUuidSender());
          statement.setString(3, request.getUuidReceiver());

          statement.executeUpdate();
        } else {
          return false;
        }

        if (request.getRequestStatus().equals("accepted")) {
          Amis ami = new Amis();
          ami.setUuid_user(request.getUuidSender());
          ami.setUuid_second_user(request.getUuidReceiver());
          if (this.create(ami))
            return true;
          else
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
    } else
      return false;
    return true;
  }

  public Boolean findInvitationExist(String uuid_user, String uuid_second_user, int state) {
    PreparedStatement statement = null;
    ResultSet result = null;
    try {
      String query = "";

      if (state == 1) {
        query = "SELECT * FROM friend_request WHERE (uuid_sender = ? and uuid_reciver=?) or (uuid_reciver = ? and uuid_sender= ?) and ( request_status = 'pending' or request_status = 'accepted')";
      } else if (state == 2) {
        query = "SELECT * FROM friend_request WHERE (uuid_sender = ? and uuid_reciver=?) or (uuid_reciver = ? and uuid_sender= ?) and ( request_status = 'pending')";
      }

      statement = this.connect.prepareStatement(query);
      statement.setString(1, uuid_user);
      statement.setString(2, uuid_second_user);
      statement.setString(3, uuid_user);
      statement.setString(4, uuid_second_user);

      result = statement.executeQuery();

      if (result.next()) {
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (result != null) {
          result.close();
        }
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  public Hashtable<UserInformation, FriendRequest> userSendingPendingRequests(String uuid) {

    Hashtable<UserInformation, FriendRequest> requests = new Hashtable<UserInformation, FriendRequest>();
    PreparedStatement statement = null;
    ResultSet result = null;
    try {
      String query = "SELECT * FROM friend_request INNER JOIN user on user.uuid_user = uuid_reciver WHERE uuid_sender = ? and request_status = 'pending'";
      statement = this.connect.prepareStatement(query);
      statement.setString(1, uuid);
      result = statement.executeQuery();
      while (result.next()) {
        System.out.println("userSendingPendingRequests    EXIST *******************************************");
        FriendRequest request = new FriendRequest();
        request.setUuidSender(result.getString("uuid_sender"));
        request.setUuidReceiver(result.getString("uuid_reciver"));
        request.setRequestStatus(result.getString("request_status"));
        request.setRequest_date(result.getString("request_date"));

        UserInformation user = new UserInformation();
        user.setUuid(result.getString("uuid_user"));
        user.setPseudo(result.getString("username"));
        user.setEmail(result.getString("email"));
        user.setImage(result.getString("image"));
        user.setIsadmin(result.getBoolean("isadmin"));
        requests.put(user, request);

      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (result != null) {
          result.close();
        }
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return requests;
  }

  public Hashtable<UserInformation, FriendRequest> userReceivingPendingRequests(String uuid) {
    Hashtable<UserInformation, FriendRequest> requests = new Hashtable<UserInformation, FriendRequest>();
    PreparedStatement statement = null;
    ResultSet result = null;
    try {
      String query = "SELECT * FROM friend_request INNER JOIN user on uuid_sender = user.uuid_user  WHERE uuid_reciver = ? and request_status = 'pending'";
      statement = this.connect.prepareStatement(query);
      statement.setString(1, uuid);
      result = statement.executeQuery();
      while (result.next()) {
        FriendRequest request = new FriendRequest();
        request.setUuidSender(result.getString("uuid_sender"));
        request.setUuidReceiver(result.getString("uuid_reciver"));
        request.setRequestStatus(result.getString("request_status"));
        request.setRequest_date(result.getString("request_date"));

        UserInformation user = new UserInformation();
        user.setUuid(result.getString("uuid_user"));
        user.setPseudo(result.getString("username"));
        user.setEmail(result.getString("email"));
        user.setImage(result.getString("image"));
        user.setIsadmin(result.getBoolean("isadmin"));
        requests.put(user, request);

      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (result != null) {
          result.close();
        }
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return requests;
  }

  public Boolean sendInvitation(FriendRequest request) {

    PreparedStatement statement = null;
    if (!this.findInvitationExist(request.getUuidSender(), request.getUuidReceiver(), 1)) {
      try {
        String query = "INSERT INTO friend_request (uuid_sender,uuid_reciver) VALUES (?,?)";
        statement = this.connect.prepareStatement(query);
        statement.setString(1, request.getUuidSender());
        statement.setString(2, request.getUuidReceiver());
        statement.executeUpdate();

        query = "select * from friend_request where uuid_sender = ? and uuid_reciver = ?";
        statement = this.connect.prepareStatement(query);
        statement.setString(1, request.getUuidSender());
        statement.setString(2, request.getUuidReceiver());
        ResultSet result = statement.executeQuery();

        if (result.next()) {
          request.setUuidSender(result.getString("uuid_sender"));
          request.setUuidReceiver(result.getString("uuid_reciver"));
          request.setRequestStatus(result.getString("request_status"));
          request.setRequest_date(result.getString("request_date"));
          return true;
        }

        return false;
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
    } else
      return false;

  }

  // user that are not friend of the current user

  public LinkedList<UserInformation> notFrineds(UserInformation user) {
    LinkedList<UserInformation> notFriends = new LinkedList<UserInformation>();
    PreparedStatement statement = null;
    ResultSet result = null;
    try {
      String query = "SELECT * FROM user WHERE uuid_user not in (SELECT amis.uuid_first_user FROM user inner join amis on user.uuid_user = amis.uuid_second_user where  user.uuid_user = ? UNION SELECT amis.uuid_second_user FROM user inner join amis  on user.uuid_user = amis.uuid_first_user  where user.uuid_user = ? ) and uuid_user != ?";
      statement = this.connect.prepareStatement(query);
      statement.setString(1, user.getUuid());
      statement.setString(2, user.getUuid());
      statement.setString(3, user.getUuid());
      result = statement.executeQuery();
      while (result.next()) {
        UserInformation notFriend = new UserInformation();
        notFriend.setUuid(result.getString("uuid_user"));
        notFriend.setPseudo(result.getString("username"));
        notFriend.setEmail(result.getString("email"));
        notFriend.setImage(result.getString("image"));
        notFriend.setIsadmin(result.getBoolean("isadmin"));
        notFriends.add(notFriend);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (result != null) {
          result.close();
        }
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return notFriends;
  }

  // find all friends of a user
  public LinkedList<UserInformation> findAllAmis(String uuid_user) {
    LinkedList<UserInformation> amis = new LinkedList<UserInformation>();
    PreparedStatement statement = null;
    ResultSet result = null;
    try {
      String query = "SELECT * FROM user WHERE uuid_user in (SELECT amis.uuid_first_user FROM user inner join amis on user.uuid_user = amis.uuid_second_user where  user.uuid_user = ? UNION SELECT amis.uuid_second_user FROM user inner join amis  on user.uuid_user = amis.uuid_first_user  where user.uuid_user = ? )";
      statement = this.connect.prepareStatement(query);
      statement.setString(1, uuid_user);
      statement.setString(2, uuid_user);
      result = statement.executeQuery();
      while (result.next()) {
        UserInformation ami = new UserInformation();
        ami.setUuid(result.getString("uuid_user"));
        ami.setPseudo(result.getString("username"));
        ami.setEmail(result.getString("email"));
        ami.setImage(result.getString("image"));
        ami.setIsadmin(result.getBoolean("isadmin"));
        amis.add(ami);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (result != null) {
          result.close();
        }
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return amis;
  }
}