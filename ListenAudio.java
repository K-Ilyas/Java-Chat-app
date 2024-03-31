import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class ListenAudio extends Thread {

    private final InputStream in;

    public ListenAudio(InputStream in) {
        this.in = in;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Obtain the system's default audio output device (speakers)
                SourceDataLine line;
                AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();

                // Create an input stream to receive audio data from the client
                byte[] buffer = new byte[100];
                int bytesRead;
                while ((bytesRead = in.read(buffer, 0, buffer.length)) != -1) {
                    line.write(buffer, 0, bytesRead);
                }
            } catch (LineUnavailableException | IOException e) {
                e.printStackTrace();
            }
        }
    }

}
