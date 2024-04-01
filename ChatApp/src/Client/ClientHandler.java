package Client;


import Controller.MessagesContoller;
import model.UserInformation;

public class ClientHandler {
  private static UserInformation thisUser;
  private static SocketClient clientSocket = null;
  private static UserInformation selectedUser;



  public static void setLoggedInUser(UserInformation user) {
      thisUser = user;
  }
  public static void initSocker(){
    // if(clientSocket != null)
      clientSocket = new SocketClient();
  }
  public static void initSocker(MessagesContoller messageController){
    initSocker();
    clientSocket.setMessageController(messageController);
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
public static void setSelectedUser(UserInformation selectedUser) {
    ClientHandler.selectedUser = selectedUser;
}

public static UserInformation getSelectedUser() {
    return selectedUser;
}
public static void initSocket(MessagesContoller messagesController) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'initSocket'");
}




}