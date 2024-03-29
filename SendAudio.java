import java.io.IOException;
import java.io.OutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class SendAudio extends Thread {

    private final OutputStream out;

    public SendAudio(OutputStream out) {
        this.out = out;
    }

    @Override
    public void run() {
        while (true) {
            try {
                TargetDataLine line;
                AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();

                byte[] buffer = new byte[100];
                int bytesRead;
                while ((bytesRead = line.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();


                try {
                    Thread.sleep(200); // Adjust the sleep duration as needed
                } catch (InterruptedException e) {
                    e.printStackTrace(); // Handle interruption
                }

            } catch (LineUnavailableException | IOException e) {
                e.printStackTrace();
            }
        }

    }

}
