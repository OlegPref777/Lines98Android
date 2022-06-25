package ru.ls.lines98.game.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import ru.ls.lines98.R;
import ru.ls.lines98.game.option.GameInfo;

public class SoundManager {

	private final SoundPool soundPool;
	int JUMP_SOUND_ID;
	int JUMP_SOUND_STREAM_ID = 0;

	public SoundManager(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			AudioAttributes	audioAttributes	= new AudioAttributes.Builder()
					.setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
					.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
					.build();
			soundPool = new SoundPool
					.Builder()
					.setMaxStreams(3)
					.setAudioAttributes(audioAttributes)
					.build();
		} else {
			soundPool = new SoundPool(3,	AudioManager.STREAM_MUSIC,0);
		}
		JUMP_SOUND_ID = soundPool.load(context,	R.raw.jump,1);
	}

	public static void playMoveSound() {
//		// Release old clip before playing new one
//		closeIfOpen(moveClip);
//		moveClip = soundManager.play(MOVE);
	}

	public static void playCantMoveSound() {
//		// This method can be constantly called when user tries to move a ball to an
//		// impossible square. If we constantly close and play the clip again there will
//		// be a delay
//		if (cantMoveClip != null && cantMoveClip.isRunning()) {
//			return;
//		}
//
//		closeIfOpen(cantMoveClip);
//		cantMoveClip = soundManager.play(CANTMOVE);
	}

	public static void playJumSound() {
		// This play function
		// takes five parameter
		// leftVolume, rightVolume,
		// priority, loop and rate.
		if (GameInfo.getCurrentInstance().isBallJumpingSound()) {
			soundManager.JUMP_SOUND_STREAM_ID = soundManager.soundPool.play(soundManager.JUMP_SOUND_ID, 1, 1, 0, -1, 0.5f);
			//soundManager.soundPool.autoPause();
		}

//		closeIfOpen(jumpClip);
//		jumpClip = soundManager.play(JUMP);
	}

	public static void playDestroySound() {
//		closeIfOpen(destroyClip);
//		destroyClip = soundManager.play(DESTROY);
	}

	public static void playJumSoundStop() {
		if (soundManager.JUMP_SOUND_STREAM_ID != 0){
			soundManager.soundPool.stop(soundManager.JUMP_SOUND_STREAM_ID);
			soundManager.JUMP_SOUND_STREAM_ID = 0;
		}
	}

	@Override
	protected void finalize() throws Throwable {
//		closeIfOpen(moveClip);
//		closeIfOpen(cantMoveClip);
//		closeIfOpen(jumpClip);
//		closeIfOpen(destroyClip);
	}

//	private Clip play(String fileName) {
//		Clip clip = null;
//
//		try {
//			AudioInputStream stream = AudioSystem.getAudioInputStream(Lines.class.getResource(fileName));
//			AudioFormat format = stream.getFormat();
//			DataLine.Info info = new DataLine.Info(Clip.class, format);
//			try {
//				clip = (Clip) AudioSystem.getLine(info);
//				clip.open(stream);
//				clip.start();
//			} catch (LineUnavailableException e) {
//				e.printStackTrace();
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (UnsupportedAudioFileException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return clip;
//	}
//
//	private static void closeIfOpen(Clip clip) {
//		if (clip != null && clip.isOpen()) {
//			clip.close();
//		}
//	}

	private static final String DESTROY = "DESTROY2.WAV";
	private static final String JUMP = "JUMP.WAV";
	private static final String CANTMOVE = "CANTMOVE.WAV";
	private static final String MOVE = "MOVE.WAV";

//	private static Clip moveClip;
//	private static Clip cantMoveClip;
//	private static Clip jumpClip;
//	private static Clip destroyClip;

	public static SoundManager soundManager;
}