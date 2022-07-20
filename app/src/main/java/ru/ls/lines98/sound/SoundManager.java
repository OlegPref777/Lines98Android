package ru.ls.lines98.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import ru.ls.lines98.R;
import ru.ls.lines98.option.GameInfo;

public class SoundManager {

	public static SoundManager soundManager;
	private final SoundPool soundPool;
	int DESTROY_SOUND_ID ;
	int CANT_MOVE_SOUND_ID;
	int MOVE_SOUND_ID;
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
		CANT_MOVE_SOUND_ID = soundPool.load(context, R.raw.cantmove,1);
		DESTROY_SOUND_ID = soundPool.load(context, R.raw.destroy2,1);
		MOVE_SOUND_ID = soundPool.load(context,	R.raw.move,1);
		JUMP_SOUND_ID = soundPool.load(context,	R.raw.jump,1);
	}

	public static void playMoveSound() {
		if (GameInfo.getCurrentInstance().isMovementSound()) {
			soundManager.soundPool.play(soundManager.MOVE_SOUND_ID, 1, 1, 0, 0, 1);
		}
	}

	public static void playCantMoveSound() {
		if (GameInfo.getCurrentInstance().isCantMoveSound()) {
			soundManager.soundPool.play(soundManager.CANT_MOVE_SOUND_ID, 1, 1, 0, 0, 1);
		}

	}

	public static void playJumSound() {
		if (GameInfo.getCurrentInstance().isBallJumpingSound()) {
			soundManager.JUMP_SOUND_STREAM_ID = soundManager.soundPool.play(soundManager.JUMP_SOUND_ID, 1, 1, 0, -1, 1.0f);
		}
	}

	public static void playDestroySound() {
		if (GameInfo.getCurrentInstance().isDestroySound()) {
			soundManager.soundPool.play(soundManager.DESTROY_SOUND_ID, 1, 1, 0, 0, 1);
		}
	}

	public static void playJumSoundStop() {
		if (soundManager.JUMP_SOUND_STREAM_ID != 0){
			soundManager.soundPool.stop(soundManager.JUMP_SOUND_STREAM_ID);
			soundManager.JUMP_SOUND_STREAM_ID = 0;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		//TODO Close Sound
	}
}