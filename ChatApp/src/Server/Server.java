package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import classes.*;
import connection.*;
import model.UserInformation;


public class Server {

    final static int PORT_CONN = 4040;
    final static int PORT_FILE = 4041;
    private ServerSocket server = null;
    private ServerSocket serverMessages = null;
    private boolean isConnected = false;

    private Hashtable<UserInformation, Socket> con_table = null;
    private Hashtable<UserInformation, Socket> msg_table = null;

    public static Hashtable<UserInformation, BlockingQueue<Integer>> threadQueues = new Hashtable<UserInformation, BlockingQueue<Integer>>();

    private Connection connect = null;

    public Server() {
        this.server = null;
        try {
            this.server = new ServerSocket(PORT_CONN);
            this.serverMessages = new ServerSocket(PORT_FILE);

            this.isConnected = true;
            System.out.println("SERVER : CONNECTED");
            this.connect = MySQLConnectSingleton.getInstance();
            this.con_table = new Hashtable<UserInformation, Socket>();
            this.msg_table = new Hashtable<UserInformation, Socket>();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Hashtable<UserInformation, Socket> getMsg_table() {
        return msg_table;
    }

    public boolean isLogedInOrInit(UserInformation user, Socket soc,Socket soc2) {
        if (!this.con_table.isEmpty()) {
            Enumeration<UserInformation> list = this.con_table.keys();
            while (list.hasMoreElements()) {
                if (list.nextElement().compareTo(user) == 0) {
                    this.con_table.put(user, soc);
                    this.msg_table.put(user, soc2);
                    return true;
                }
            }
            return false;
        } else
            return false;
    }

    public Socket findUserSocket(String uuid) {
        if (!this.con_table.isEmpty()) {

            Set<Entry<UserInformation, Socket>> list = this.con_table.entrySet();
            Iterator<Entry<UserInformation, Socket>> iterator = list.iterator();

            while (iterator.hasNext()) {
                Entry<UserInformation, Socket> user = iterator.next();
                System.out.println(String.format("'%s'", user.getKey().getUuid()));
                if (String.format("'%s'", user.getKey().getUuid()).equals(String.format("'%s'", uuid))) {
                    System.out.println(" ****************************** found");
                    return user.getValue();

                }
            }
        }
        return null;
    }

    
    public Socket findUserSocketMessage(String uuid) {
        if (!this.msg_table.isEmpty()) {
            Set<Entry<UserInformation, Socket>> list = this.msg_table.entrySet();
            Iterator<Entry<UserInformation, Socket>> iterator = list.iterator();
            while (iterator.hasNext()) {
                Entry<UserInformation, Socket> user = iterator.next();
                System.out.println(String.format("'%s'", user.getKey().getUuid()));
                if (String.format("'%s'", user.getKey().getUuid()).equals(String.format("'%s'", uuid))) {
                    System.out.println(" ****************************** found");
                    return user.getValue();

                }
            }
        }
        return null;
    }

    public boolean isUserExist(UserInformation user) {

        if (!this.con_table.isEmpty()) {

            Enumeration<UserInformation> list = this.con_table.keys();

            while (list.hasMoreElements()) {

                if (list.nextElement().compareTo(user) == 0) {
                    return true;
                }
            }
            return false;
        } else
            return false;
    }

    public boolean addUser(UserInformation user, Socket soc,Socket soc2) {
        if (!isUserExist(user)) {
            this.con_table.put(user, soc);
            this.msg_table.put(user, soc2);

            System.out.println("NEW SING IN :  " + "[" + user + "] \t" + soc);
            return true;
        }
        return false;
    }

    public boolean logOut(UserInformation user) {

        if (!this.con_table.isEmpty()) {

            Iterator<UserInformation> iterator = this.con_table.keySet().iterator();

            while (iterator.hasNext()) {
                UserInformation currentUser = iterator.next();
                if (currentUser.compareTo(user) == 0) {
                    iterator.remove();
                    this.msg_table.remove(user);                    
                    return true;
                }
            }
            return false;
        } else
            return false;

    }

    public void startConversation() {

        ExecutorService executorService = Executors.newCachedThreadPool();
        if (this.isConnected) {
            Socket soc = null;
            Socket soc2 = null;
            for (;;) {
                try {
                    soc = this.server.accept();
                    soc2 = this.serverMessages.accept();
                    executorService.submit(new ServerClientHandler(soc,soc2, this));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else
            System.out.println("THE SERER IS NOT CONNECTED !!!");
    }

    public Hashtable<UserInformation, Socket> getCon_table() {
        return con_table;
    }

    public Connection getConnect() {
        return connect;
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.startConversation();
    }
}