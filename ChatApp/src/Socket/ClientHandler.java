package Socket;

import DAO.UserInformation;

public class ClientHandler {
  private static UserInformation thisUser;
  private static SocketClient clientSocket = null;


  public static void setLoggedInUser(UserInformation user) {
      thisUser = user;
  }
  public static void initSocker(){
      clientSocket = new SocketClient();
  }

  public static UserInformation getLoggedInUser() {
      return thisUser;
  }

  public static boolean isLoggedIn() {
      return thisUser != null;
  }

  public static SocketClient getClientSocket() {
      System.out.println(clientSocket == null? "nullp": "not nullp");
      return clientSocket; 
  }

}