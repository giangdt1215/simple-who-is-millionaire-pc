package com.nhom6.altp.model;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager {

	private Clip clip;

	public SoundManager() {
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void playSound(String filename, boolean loop) {
		try {
			clip.stop();
			clip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.getAudioInputStream(this
					.getClass().getResource("/resource/sound/" + filename));
			clip.open(ais);
			if (loop) {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			} else {
				clip.start();
			}
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopSound() {
		clip.stop();
	}

	public boolean isPlaying() {
		if (clip.isRunning()) {
			return true;
		} else {
			return false;
		}
	}
}
