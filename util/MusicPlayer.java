package util;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

/**
 * Utility class for playing short audio clips in a loop for background music.
 */
public class MusicPlayer {
    private Clip clip;

    /**
     * Loads the audio file at the given path and plays it continuously.
     *
     * @param path path to the audio file
     */
    public void playLoop(String path) {

        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(path));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Loops forever
            clip.start();
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Music Loading Error: " + e.getMessage());
        }
    }

    /**
     * Stops any currently playing audio.
     */
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}