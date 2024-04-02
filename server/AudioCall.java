package server;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import classes.UserInformation;

public class AudioCall {


    private UserInformation userInformation;
    private UserInformation friend;
    private Server socketServer;

    public AudioCall(UserInformation userInformation, UserInformation friend, Server socketServer) {
        this.userInformation = userInformation;
        this.friend = friend;
        this.socketServer = socketServer;
    }
    
    public void call() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Run the task every 4 seconds
        scheduler.scheduleAtFixedRate(new Runnable() {
            int count = 0;

            @Override
            public void run() {
                // Execute your code here
                System.out.println("Executing code...");

                Socket reciver = socketServer.findUserSocketMessage(friend.getUuid());

                if (reciver != null) {
                    try {
                        OutputStream outRecive = reciver.getOutputStream();
                        ObjectOutputStream oosRecive = new ObjectOutputStream(outRecive);

                        DataOutputStream dosRecive = new DataOutputStream(outRecive);
                        dosRecive.writeInt(4);
                        dosRecive.flush();
                        oosRecive.writeObject(userInformation);
                        oosRecive.flush();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // Increment the count
                count++;

                // Check if 10 seconds have passed
                if (count >= 10) {
                    // Shutdown the scheduler after 10 seconds
                    scheduler.shutdown();
                }
            }
        }, 0, 4, TimeUnit.SECONDS); // Initial delay of 0 seconds, repeat every 4 seconds

        // Wait for the scheduler to terminate
        try {
            scheduler.awaitTermination(10, TimeUnit.SECONDS); // Wait for 10 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
