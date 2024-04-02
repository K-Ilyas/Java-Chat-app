import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class AudioServer {

    private void startVideoServer() throws ClassNotFoundException {
        try (ServerSocket serverSocketAudio = new ServerSocket(8081)) {

            System.out.println("Waiting for client connection...");
            System.out.println("wait...");

            while (true) {

                Socket first_socket_client = serverSocketAudio.accept();

                Socket second_socket_client = serverSocketAudio.accept();

                new Thread(new SendAudio(first_socket_client.getOutputStream())).start();

                // new Thread(new SendAudio(second_socket_client.getOutputStream())).start();

                // new Thread(new ReciveAudio(first_socket_client.getInputStream())).start();

                InputStream inner = first_socket_client.getInputStream();   

                new Thread(new ReciveAudio(inner)).start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        AudioServer audioServer = new AudioServer();

        try {
            audioServer.startVideoServer();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}