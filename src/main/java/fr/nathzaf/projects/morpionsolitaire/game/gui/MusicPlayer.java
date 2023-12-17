package fr.nathzaf.projects.morpionsolitaire.game.gui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

/**
 * The MusicPlayer class is responsible for playing background music and sound effects in the game GUI.
 * It uses the JavaFX MediaPlayer to play audio files.
 */
public class MusicPlayer {

    private static MusicPlayer instance;

    private MediaPlayer mediaPlayer;

    /**
     * Plays a sound effect from the specified file.
     *
     * @param fileName the name of the sound effect file
     */
    public static void playSoundEffectFromGUIPackage(String fileName){
        URL soundEffectFile = MusicPlayer.class.getResource(fileName);
        MusicPlayer musicPlayer = MusicPlayer.getInstance();
        musicPlayer.playSoundEffect(soundEffectFile.toString());
    }

    /**
     * Plays a music from the specified file.
     *
     * @param fileName the name of the music file
     */
    public static MediaPlayer playMusicFromGUIPackage(String fileName){
        URL musicFile = MusicPlayer.class.getResource(fileName);
        MusicPlayer musicPlayer = MusicPlayer.getInstance();
        musicPlayer.setMusic(musicFile.toString());
        return musicPlayer.playMusic();
    }

    /**
     * Stops the current music
     */
    public static void stopMusic() {
        MusicPlayer musicPlayer = MusicPlayer.getInstance();
        musicPlayer.mediaPlayer.stop();
    }

    public static MusicPlayer getInstance(){
        if(instance == null){
            instance = new MusicPlayer();
        }
        return instance;
    }

    /**
     * Sets the background music to be played by the MusicPlayer.
     * If there is already a music playing, it stops the current music and replaces it with the new one.
     *
     * @param musicFilePath the file path of the music file
     */
    private void setMusic(String musicFilePath){
        if(mediaPlayer != null){
            mediaPlayer.stop();
        }
        Media musicMedia = new Media(musicFilePath);
        this.mediaPlayer = new MediaPlayer(musicMedia);
    }

    /**
     * Plays the background music.
     * If there is no background music set, this method has no effect.
     */
    private MediaPlayer playMusic(){
        if(mediaPlayer != null){
            mediaPlayer.setVolume(1);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        }
        return mediaPlayer;
    }

    /**
     * Plays a sound effect.
     *
     * @param soundEffectPath the file path of the sound effect
     */
    private void playSoundEffect(String soundEffectPath){
        Media soundEffectMedia = new Media(soundEffectPath);
        mediaPlayer = new MediaPlayer(soundEffectMedia);
        mediaPlayer.play();
    }
}

