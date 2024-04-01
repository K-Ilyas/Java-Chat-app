package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnectSingleton {

    private String url = "";
    private String user = "";
    private String passwd = "";
    private static Connection connect;

    private MySQLConnectSingleton() {
      	
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat?useLegacyDatetimeCode=false&serverTimezone=Europe/Paris","root","0000");  
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getInstance() {
        if (connect == null) {

            synchronized (MySQLConnectSingleton.class) {
                if (connect == null) {
                    new MySQLConnectSingleton();
                    System.out.println("INSTANCIATION DE LA CONNEXION SQL ! ");
                }
            }
        } else {
            System.out.println("CONNEXION SQL EXISTANTE ! ");
        }
        return connect;
    }

    // for test purposes :
    public static void main(String[] args) {
        MySQLConnectSingleton.getInstance();
    }
}
